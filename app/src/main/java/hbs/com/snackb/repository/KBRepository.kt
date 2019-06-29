package hbs.com.snackb.repository

import hbs.com.snackb.api.KBApi
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.HeaderMap


interface KBRepository {
    fun getDetailBranchInfo(headerMap: Map<String,String>): Single<List<String>>

    fun getLawAreaList(headerMap: Map<String, String>) : Single<List<String>>

    fun getAccountAll(headerMap: Map<String, String>, bodyMap:HashMap<String, Map<String, String>>) : Single<retrofit2.Call<ResponseBody>>


}

class KBRepositoryImpl constructor(private val kbApi: KBApi) : KBRepository{
    override fun getAccountAll(headerMap: Map<String, String>, bodyMap:HashMap<String, Map<String, String>>): Single<retrofit2.Call<ResponseBody>> {
        return kbApi.getAccountAll(headerMap, bodyMap)
    }

    override fun getLawAreaList(headerMap: Map<String, String>): Single<List<String>> {
        return kbApi.getLawAreaList(headerMap)
    }

    override fun getDetailBranchInfo(headerMap: Map<String,String>): Single<List<String>> {
        return kbApi.getDetailBranchInfo(headerMap)
    }


}