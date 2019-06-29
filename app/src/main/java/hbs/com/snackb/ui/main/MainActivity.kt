package hbs.com.snackb.ui.main

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import hbs.com.snackb.R
import hbs.com.snackb.api.BaseAPI
import hbs.com.snackb.api.KBApi
import hbs.com.snackb.api.NetModule
import hbs.com.snackb.models.AroundBank
import hbs.com.snackb.repository.KBRepository
import hbs.com.snackb.repository.KBRepositoryImpl
import hbs.com.snackb.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.http.HeaderMap
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), FindingCategoryAdapterListener {
    private val testImg:String = "https://scontent-yyz1-1.cdninstagram.com/vp/8d061301416ab17dbe3accbfd648308c/5D49D1CB/t51.2885-15/e35/51054929_421437628597852_3501897158316313770_n.jpg?_nc_ht=scontent-yyz1-1.cdninstagram.com&se=8"
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

        requestAppList.requestFBAppList()

        initCategory()
        initView()


        val kbRepository = initKBRepository()
        val headerMap: HashMap<String, String> = hashMapOf()
        val hmacUtils = HMACUtils()
        val jjwtHelper = JJwtHelper()
        headerMap["apiKey"] = BaseAPI.apiKey
        /*headerMap["hsKey"] = jjwtHelper.convertJWS()*/

        kbRepository
            .getLawAreaList(headerMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> Log.d("result", result.toString()) },
                { error -> error.printStackTrace() })

        /*kbRepository
            .getDetailBranchInfo(headerMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> Log.d("result", result.toString()) },
                { error -> error.printStackTrace() })*/
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
}