package hbs.com.snackb.models

import java.io.Serializable

data class AroundBank(
    val appIcon: String = "",
    val appTitle: String= "",
    val appPackageName: String= "",
    val appPublishName:String= "",
    val appHitCnt: Int = 0,
    val appRegistDate: String= "",
    val appPoint : String= ""
)