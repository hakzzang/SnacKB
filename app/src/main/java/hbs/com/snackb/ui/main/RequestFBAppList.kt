package hbs.com.snackb.ui.main

import android.util.Log
import com.google.firebase.database.*
import hbs.com.snackb.models.AroundBank

class RequestFBAppList(val listener: RequestFBDataListener) {

    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val appListRef = firebaseDatabase.getReference().child("app_info")
    val appTotalPointRef = firebaseDatabase.getReference().child("my_point")


    public fun requestFBAppList() {
        appListRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                listener.onFailed(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var appList = mutableListOf<AroundBank>()
                if (p0!!.exists()) {
                    for (childDataSnap in p0.children) {
                        val aroundBank = childDataSnap.getValue(AroundBank::class.java)
                        if (aroundBank != null) {
                            Log.d("RequestFBAppList", aroundBank.appTitle+", "+aroundBank.appRegistDate)
                            appList.add(aroundBank)
                        }
                    }
                    if(listener!=null){
                        listener.onResponse(appList as ArrayList<AroundBank>)
                    }
                }
            }

        })
    }
}