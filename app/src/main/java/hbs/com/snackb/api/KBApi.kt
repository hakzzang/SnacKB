package hbs.com.snackb.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface KBApi {
    @POST("getAccountAll/")
    fun getAccountAll(@HeaderMap headerMap: Map<String, String>, @Body bodyMap:HashMap<String, Map<String, String>>): Call<ResponseBody>

    @GET("getDetailBranchInfo/")
    fun getDetailBranchInfo(@HeaderMap headerMap: Map<String, String>): Single<List<String>>

    @GET("getLawAreaList/")
    fun getLawAreaList(@HeaderMap headerMap: Map<String, String>): Single<List<String>>
}