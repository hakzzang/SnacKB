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
import hbs.com.snackb.repository.KBRepository
import hbs.com.snackb.repository.KBRepositoryImpl
import hbs.com.snackb.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.http.HeaderMap

class MainActivity : AppCompatActivity(), FindingCategoryAdapterListener {
    private val aroundBankList = listOf<String>()
    private val categoryMutableList = mutableListOf<String>()
    private val findingCategoryAdapter
            by lazy { FindingCategoryAdapter(categoryMutableList, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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