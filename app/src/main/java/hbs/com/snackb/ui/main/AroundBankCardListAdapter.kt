package hbs.com.snackb.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hbs.com.snackb.R
import hbs.com.snackb.databinding.ItemAroundBankBinding
import hbs.com.snackb.models.AroundBank
import kotlinx.android.synthetic.main.item_around_bank.view.*

class AroundBankCardListAdapter(private val list: List<AroundBank>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemAroundBankBinding = ItemAroundBankBinding.inflate(LayoutInflater.from(parent.context))
        return AroundBankViewHolder(itemAroundBankBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val aroundBankViewHolder = holder as AroundBankViewHolder
        aroundBankViewHolder.itemAroundBankBinding.aroundBank = list[position]
    }

    class AroundBankViewHolder(val itemAroundBankBinding: ItemAroundBankBinding) :
        RecyclerView.ViewHolder(itemAroundBankBinding.root)
}