package hbs.com.snackb.ui.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hbs.com.snackb.BuildConfig
import hbs.com.snackb.R
import hbs.com.snackb.api.BaseAPI
import hbs.com.snackb.api.KBApi
import hbs.com.snackb.api.NetModule
import hbs.com.snackb.models.AroundBank
import hbs.com.snackb.models.BankAccount
import hbs.com.snackb.models.BankTransaction
import hbs.com.snackb.repository.KBRepository
import hbs.com.snackb.repository.KBRepositoryImpl
import hbs.com.snackb.utils.FindingCategory
import hbs.com.snackb.utils.SecretUtils
import hbs.com.snackb.utils.StartSnapHelper
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.NetworkInterface
import java.text.NumberFormat
import java.util.*




class MainActivity : AppCompatActivity(), FindingCategoryAdapterListener {



    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val appTotalPointRef = firebaseDatabase.getReference().child("my_point")

    private val aroundBankList = mutableListOf<AroundBank>()
    private val categoryMutableList = mutableListOf<String>()
    private val findingCategoryAdapter
            by lazy { FindingCategoryAdapter(categoryMutableList, this) }
    private val aroundBankCardListAdapter by lazy {
        AroundBankCardListAdapter(aroundBankList)
    }

    private var totalPoint:Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val requestAppList = RequestFBAppList(object : RequestFBDataListener{
            override fun onResponse(list: ArrayList<AroundBank>) {
                aroundBankList.addAll(list)
                aroundBankCardListAdapter.notifyDataSetChanged()

                for (obj in aroundBankList) {
                    totalPoint = totalPoint + Integer.parseInt(obj.appPoint)
                    Log.d("sortResult1", obj.appRegistDate)
                }
                aroundBankList.sortedWith(compareBy({it.appRegistDate}))
                for (obj in aroundBankList) {
                    Log.d("sortResult2", obj.appRegistDate)
                }
                tv_total_point.text = NumberFormat.getNumberInstance(Locale.US).format(totalPoint)
            }

            override fun onFailed(msg: String) {
                Log.d("RequestFBAppList", "onFailed : "+msg)
            }

        })

        requestFBTotalPoint()
        requestAppList.requestFBAppList()

        initCategory()
        initView()

        getAccountAll()

    }

    private fun initView() {
        rv_finding_bank.apply {
            LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = this
            }
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.margin_main_view_space_side).toInt()))
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
        categoryMutableList.add(getString(R.string.category_hit))
        categoryMutableList.add(getString(R.string.category_recent))
        categoryMutableList.add(getString(R.string.category_point))
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


    private fun getAccountAll() {
        val headerMap: HashMap<String, String> = hashMapOf()
        headerMap["apikey"] = BaseAPI.apiKey
        val bodyMap: HashMap<String, Map<String, String>> = hashMapOf()
        bodyMap["dataHeader"] = makeDataHeaderMap()
        bodyMap["dataBody"] = makeDataBody()
        val jsonObject = JSONObject(bodyMap)
        val hsKey= SecretUtils.getHsKey(BaseAPI.apiKey, jsonObject.toString())
        headerMap["hsKey"] = hsKey.toString()
        val kbRepository = initKBRepository()
        kbRepository
            .getAccountAll(headerMap, bodyMap).enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("error", t.message)
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val responseBody = response.body() as ResponseBody
                    //you can do whatever with the response body now...
                    val responseBodyString = responseBody.string()
                    val account = accountParser(responseBodyString)
                    Log.d("account",account.toString())
                }

            })
    }

    private fun accountParser(bodyContent:String): BankAccount {
        val transactionList = mutableListOf<BankTransaction>()
        val json = JSONObject(bodyContent)
        val dataBody = json.getJSONObject("dataBody")
        val userName = dataBody.getString("고객명")
        val dataList = dataBody.getJSONArray("데이터부")
        for(index in 0 until dataList.length()){
            val jsonObject = dataList.getJSONObject(index)
            val account = jsonObject.getString("상품명")
            val balance= jsonObject.getString("잔액")
            val accountAlias = jsonObject.getString("계좌별명")
            val accountNum = jsonObject.getString("계좌번호")
            val bankTransaction = BankTransaction(account, balance, accountAlias, accountNum)
            transactionList.add(bankTransaction)
        }
        return BankAccount(userName, transactionList)
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


    private fun requestFBTotalPoint(){
        var totalPoint:String = ""

        appTotalPointRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    totalPoint = p0.value.toString()

                    tv_total_my_point.text = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(totalPoint))

                }
            }

        })

    }

}