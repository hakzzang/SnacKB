package hbs.com.snackb.ui.main

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hbs.com.snackb.models.AroundBank
import hbs.com.snackb.models.BankAccount
import hbs.com.snackb.models.BankTransaction

class RequestFBAccountList(val listener: RequestFBAccountListener) {

    public interface RequestFBAccountListener {
        fun onResponse(list: ArrayList<BankTransaction>)
        fun onFailed(msg: String)
    }

    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val accountListRef = firebaseDatabase.getReference().child("my_account")


    public fun requestFBAccountList() {
        accountListRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                listener.onFailed(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var appList = mutableListOf<BankTransaction>()
                if (p0!!.exists()) {
                    for (childDataSnap: DataSnapshot in p0.children) {
                        if (childDataSnap.key.equals("transactions")) {
                            for (itemDataSnap: DataSnapshot in childDataSnap.children) {
                                val bankTransaction = itemDataSnap.getValue(BankTransaction::class.java)
                                if (bankTransaction != null) {
                                    appList.add(bankTransaction)
                                }
                            }
                        }
                    }
                    if (listener != null) {
                        listener.onResponse(appList as ArrayList<BankTransaction>)
                    }
                }
            }

        })
    }
}