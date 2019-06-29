package hbs.com.snackb.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hbs.com.snackb.R
import hbs.com.snackb.models.BankTransaction
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MyAccountListAdapter(val context:Context, val accountList: ArrayList<BankTransaction>) :
    RecyclerView.Adapter<MyAccountListAdapter.MyAccountHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAccountHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_my_account, parent, false)
        return MyAccountHolder(view)
    }

    override fun getItemCount(): Int {
        return accountList.size
    }

    override fun onBindViewHolder(holder: MyAccountHolder, position: Int) {
        holder.bind(accountList.get(position), context)
    }

    inner class MyAccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_account_alias = itemView.findViewById<TextView>(R.id.tv_account_alias)
        val tv_account_name = itemView.findViewById<TextView>(R.id.tv_account_name)
        val tv_account_num = itemView.findViewById<TextView>(R.id.tv_account_num)
        val tv_account_balance = itemView.findViewById<TextView>(R.id.tv_account_balance)
        val tv_send_money = itemView.findViewById<TextView>(R.id.tv_send_money)

        fun bind(data: BankTransaction, context: Context){
            tv_account_alias.text = data.accountAlias
            tv_account_balance.text = data.balance+"원"
            tv_account_name.text = data.accountName
            tv_account_num.text = data.accountNum
        }
    }
}