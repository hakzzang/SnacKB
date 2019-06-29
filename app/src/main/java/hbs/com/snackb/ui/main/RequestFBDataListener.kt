package hbs.com.snackb.ui.main

import hbs.com.snackb.models.AroundBank

interface RequestFBDataListener {
    fun onResponse(list:ArrayList<AroundBank>)
    fun onFailed(msg:String)
}