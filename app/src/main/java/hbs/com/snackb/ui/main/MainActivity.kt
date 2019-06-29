package hbs.com.snackb.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import hbs.com.snackb.api.BaseAPI
import hbs.com.snackb.api.KBApi
import hbs.com.snackb.api.NetModule
import hbs.com.snackb.models.AroundBank
import hbs.com.snackb.repository.KBRepository
import hbs.com.snackb.repository.KBRepositoryImpl
import hbs.com.snackb.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.http.HeaderMap
import java.util.*
import kotlin.collections.HashMap
import android.telephony.SubscriptionInfo
import android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE
import android.telephony.SubscriptionManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import hbs.com.snackb.BuildConfig
import hbs.com.snackb.R
import java.net.NetworkInterface
import java.util.Base64.getEncoder
import android.provider.SyncStateContract.Helpers.update
import java.nio.file.Files.readAllBytes
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64.getEncoder
import androidx.annotation.RequiresApi
import com.google.android.gms.common.util.Hex
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity(), FindingCategoryAdapterListener {
    private val aroundBankList = mutableListOf<AroundBank>()
    private val categoryMutableList = mutableListOf<String>()
    private val findingCategoryAdapter
            by lazy { FindingCategoryAdapter(categoryMutableList, this) }
    private val aroundBankCardListAdapter by lazy {
        AroundBankCardListAdapter(aroundBankList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hbs.com.snackb.R.layout.activity_main)
        initAroundBank()
        initCategory()
        initView()

        val hmacUtils = HMACUtils()

        getAccountAll(JJwtHelper())
    }

    private fun initView() {
        rv_finding_bank.apply {
            LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = this
            }
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
            addItemDecoration(MarginItemDecoration(24))
            adapter = aroundBankCardListAdapter
        }
        rv_finding_category.apply {
            LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = this
            }
            val snapHelper = StartSnapHelper()
            snapHelper.attachToRecyclerView(this)
            adapter = findingCategoryAdapter
        }
    }

    private fun initCategory() {
        categoryMutableList.add(getString(R.string.category_recommended_title))
        categoryMutableList.add(getString(R.string.category_recent_title))
        categoryMutableList.add(getString(R.string.category_using_title))
    }

    private fun initAroundBank() {
        aroundBankList.add(AroundBank("기흥 국민은행", "기흥구 새우버거 집 앞", "2500m", "", "10분", "2명"))
        aroundBankList.add(AroundBank("수서 국민은행", "수서 감자튀김 집 앞", "1500m", "", "12분", "3명"))
        aroundBankList.add(AroundBank("새우버거 국민은행", "새우버거 놀이터 앞", "2000m", "", "8분", "4명"))
    }

    private fun initKBRepository(): KBRepository {
        val netModule = NetModule()
        val httpLoggingInterceptor = netModule.makeHttpLogging()
        val retrofit = netModule.makeRetrofit(httpLoggingInterceptor)
        val kbAPI = retrofit.create(KBApi::class.java)
        return KBRepositoryImpl(kbAPI)
    }

    override fun onFindCategory(findingCategory: FindingCategory) {
        when (findingCategory) {
            FindingCategory.RECOMMENDED -> ""
            FindingCategory.RECENT -> ""
            FindingCategory.USING -> ""
        }
    }


    private fun getAccountAll(jjwtHelper:JJwtHelper) {
        val headerMap: HashMap<String, String> = hashMapOf()
        headerMap["apikey"] = BaseAPI.apiKey
        val bodyMap: HashMap<String, Map<String, String>> = hashMapOf()
        bodyMap["dataHeader"] = makeDataHeaderMap()
        bodyMap["dataBody"] = makeDataBody()
        val jsonObject = JSONObject(bodyMap)
        val hsKey= SecretUtils.getHsKey(BaseAPI.apiKey, jsonObject.toString())
        headerMap["hsKey"] = hsKey.toString()
        Log.d("hsKey",hsKey.toString())
        val kbRepository = initKBRepository()
        Log.d("bodyMap", jsonObject.toString())
        kbRepository
            .getAccountAll(headerMap, bodyMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Log.d("result", result.execute().body().toString()) },
                { error -> Log.d("error",error.cause?.message) })
    }

    private fun makeDataHeaderMap(): HashMap<String, String> {
        val dataHeaderMap = hashMapOf<String, String>()
        dataHeaderMap["udId"] = makeUUID()
        dataHeaderMap["subChannel"] = makeSubChannel()
        dataHeaderMap["deviceModel"] = makeDeviceModel()
        dataHeaderMap["deviceOs"] = makeDeviceOs()
        dataHeaderMap["carrier"] = makeCarrierName()
        dataHeaderMap["connectionType"] = makeConnectionType()
        dataHeaderMap["appName"] = getString(hbs.com.snackb.R.string.app_name)
        dataHeaderMap["appVersion"] = makeVersionCode()
        dataHeaderMap["scrNo"] = "0"
        dataHeaderMap["scrName"] = "0"
        return dataHeaderMap
    }

    private fun makeDataBody() : HashMap<String, String>{
        val dataBodyMap = hashMapOf<String, String>()
        dataBodyMap["CI번호"] = makeAccountPin()
        dataBodyMap["계좌조회구분"] = makeProductNum()
        return dataBodyMap
    }
    private fun makeUUID(): String {
        return UUID.randomUUID().toString()
    }

    private fun makeSubChannel(): String {
        return "1"
    }

    private fun makeDeviceModel(): String {
        return  Build.MODEL;
    }

    private fun makeDeviceOs() : String{
        return Build.VERSION.RELEASE
    }

    private fun makeCarrierName() : String{
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    private fun makeConnectionType(): String {
        val eni: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces();
        while(eni.hasMoreElements()){
            val nii:NetworkInterface=eni.nextElement();
            if(nii.isUp)
                return nii.displayName
        }
        return ""
    }

    private fun makeVersionCode(): String {
        return BuildConfig.VERSION_CODE.toString()
    }

    private fun makeAccountPin() : String{
        return "123456789"
    }

    private fun makeProductNum() : String{
        return "1"
    }

}