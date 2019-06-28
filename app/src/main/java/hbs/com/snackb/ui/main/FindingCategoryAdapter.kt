package hbs.com.snackb.ui.main

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Interpolator
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import hbs.com.snackb.R
import hbs.com.snackb.utils.FindingCategory
import kotlinx.android.synthetic.main.item_category.view.*

interface FindingCategoryAdapterListener {
    fun onFindCategory(findingCategory: FindingCategory)
}

class FindingCategoryAdapter(
    private val list: List<String>,
    private val findingCategoryAdapterListener: FindingCategoryAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tvTemp: TextView? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return FindingCategoryViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FindingCategoryViewHolder).itemView.apply {
            setTextCategoryView(this.tv_category, list[position])
            setCategoryClickListener(this.tv_category, position)
            initCategoryStyle(this.tv_category)
        }
    }

    class FindingCategoryViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

    private fun initCategoryStyle(textView: TextView) {
        if (tvTemp != null) {
            return
        }
        textView.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, (textView.textSize * 1.2).toFloat())
            setTextColor(textView.resources.getColor(R.color.md_teal_A400))
            tvTemp = this
        }
    }

    private fun getCategory(position: Int): FindingCategory {
        when (position) {
            FindingCategory.RECOMMENDED.ordinal -> return FindingCategory.RECOMMENDED
            FindingCategory.RECENT.ordinal -> return FindingCategory.RECENT
            FindingCategory.USING.ordinal -> return FindingCategory.USING
        }
        return FindingCategory.RECOMMENDED
    }

    private fun setCategoryClickListener(textView: TextView, position: Int) {
        textView.setOnClickListener {
            val findingCategory: FindingCategory = getCategory(position)
            findingCategoryAdapterListener.onFindCategory(findingCategory)
            if (tvTemp == textView) {
                return@setOnClickListener
            }
            textView.let {
                scaleUpAnimator(it)
                toDestinationColorAnimator(it)
            }
            tvTemp?.let {
                scaleDownAnimator(it)
                toResourceColorAnimator(it)
            }
            tvTemp = textView
        }
    }

    private fun setTextCategoryView(textView: TextView, category: String) {
        textView.text = category
    }

    private fun toDestinationColorAnimator(textView: TextView) {
        val colorDestination = textView.context.getColor(R.color.md_teal_A400)
        ObjectAnimator.ofObject(textView,"textColor",ArgbEvaluator(),textView.currentTextColor,colorDestination)
            .apply {
                duration = 300
            }.start()
    }

    private fun toResourceColorAnimator(textView: TextView) {
        val colorResource = textView.context.getColor(R.color.md_teal_700)
        ObjectAnimator.ofObject(textView,"textColor",ArgbEvaluator(),textView.currentTextColor,colorResource)
            .apply {
                duration = 300
            }.start()
    }


    private fun scaleUpAnimator(textView: TextView) {
        ValueAnimator.ofFloat(textView.textSize, (textView.textSize * 1.2).toFloat())
            .apply {
                duration = 300
                interpolator = FastOutLinearInInterpolator()
                addUpdateListener {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it.animatedValue).toString().toFloat())
                }
            }.start()
    }

    private fun scaleDownAnimator(textView: TextView) {
        ValueAnimator.ofFloat(textView.textSize, textView.context.resources.getDimension(R.dimen.headline_small))
            .apply {
                addUpdateListener {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it.animatedValue).toString().toFloat())
                }
                duration = 300
                interpolator = FastOutLinearInInterpolator()

            }.start()
    }
}