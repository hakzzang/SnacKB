package hbs.com.snackb.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import hbs.com.snackb.R
import hbs.com.snackb.utils.FindingCategory
import hbs.com.snackb.utils.StartSnapHelper
import kotlinx.android.synthetic.main.activity_main.*

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
    }

    private fun initView() {

        rv_finding_category.apply {
            LinearLayoutManager(this@MainActivity).apply{
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

    override fun onFindCategory(findingCategory: FindingCategory) {
        when (findingCategory) {
            FindingCategory.RECOMMENDED -> ""
            FindingCategory.RECENT -> ""
            FindingCategory.USING -> ""
        }
    }
}