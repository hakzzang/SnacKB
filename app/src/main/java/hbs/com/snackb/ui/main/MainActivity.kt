package hbs.com.snackb.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.http.HeaderMap

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
        setContentView(R.layout.activity_main)
        initAroundBank()
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
}