package hbs.com.snackb.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hbs.com.snackb.databinding.ItemAroundBankBinding
import hbs.com.snackb.models.AroundBank
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


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

        applyBlur(
            holder.itemView.context,
            aroundBankViewHolder.itemAroundBankBinding.ivBackBlur,
            list[position].appIcon
        )
        aroundBankViewHolder.itemAroundBankBinding.tvBankDistance.setText(convertToDate(list[position].appRegistDate))
        aroundBankViewHolder.itemAroundBankBinding.tvPoint.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(list[position].appPoint)))
        aroundBankViewHolder.itemAroundBankBinding.ivGoToPlay.setOnClickListener {
            if (appInstalledOrNot(list[position].appPackageName, holder.itemView.context)) {
                val launchIntent: Intent = holder.itemView.context.packageManager.getLaunchIntentForPackage(list[position].appPackageName)
                holder.itemView.context.startActivity(launchIntent)
            } else {
                val marketIntent: Intent = Intent(Intent.ACTION_VIEW)
                marketIntent.setData(Uri.parse("market://details?id=" + list[position].appPackageName))
                holder.itemView.context.startActivity(marketIntent)
            }
        }

        aroundBankViewHolder.itemAroundBankBinding.aroundBank = list[position]
    }

    private fun applyBlur(context: Context, view: ImageView, imgUrl: String) {
        Glide.with(context).load(imgUrl)
            .into(view)
    }

    private fun appInstalledOrNot(uri: String, context: Context): Boolean {
        val pm = context.packageManager
        var app_installed = false
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            app_installed = true
        } catch (e: PackageManager.NameNotFoundException) {
            app_installed = false
        }

        return app_installed
    }

    private fun convertToDate(original:String):String{
        var resultString = ""

        val dateFormat:SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val date = dateFormat.parse(original)
        resultString = SimpleDateFormat("yyyy.MM.dd").format(date)

        return resultString
    }

    class AroundBankViewHolder(val itemAroundBankBinding: ItemAroundBankBinding) :
        RecyclerView.ViewHolder(itemAroundBankBinding.root)
}