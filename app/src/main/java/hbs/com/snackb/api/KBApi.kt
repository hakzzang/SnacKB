package hbs.com.snackb.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface KBApi {

    @GET("getDetailBranchInfo/")
    fun getDetailBranchInfo(@HeaderMap headerMap: Map<String, String>): Single<List<String>>

    @GET("getLawAreaList/")
    fun getLawAreaList(@HeaderMap headerMap: Map<String, String>): Single<List<String>>
}