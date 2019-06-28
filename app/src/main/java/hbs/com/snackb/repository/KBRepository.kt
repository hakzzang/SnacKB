package hbs.com.snackb.repository

import hbs.com.snackb.api.KBApi
import io.reactivex.Single
import retrofit2.http.HeaderMap


interface KBRepository {
    fun getDetailBranchInfo(headerMap: Map<String,String>): Single<List<String>>

    fun getLawAreaList(headerMap: Map<String, String>) : Single<List<String>>
}

class KBRepositoryImpl constructor(private val kbApi: KBApi) : KBRepository{
    override fun getLawAreaList(headerMap: Map<String, String>): Single<List<String>> {
        return kbApi.getLawAreaList(headerMap)
    }

    override fun getDetailBranchInfo(headerMap: Map<String,String>): Single<List<String>> {
        return kbApi.getDetailBranchInfo(headerMap)
    }
}