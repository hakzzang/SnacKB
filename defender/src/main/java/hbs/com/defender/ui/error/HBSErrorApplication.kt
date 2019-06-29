package hbs.com.defender.ui.error

import android.app.Application


class HBSErrorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initCrashHandler(true)
    }

    private fun initCrashHandler(isUsingFabric:Boolean) {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
        }
        if(isUsingFabric){
            val fabricExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(
                HBSExceptionHandler(
                    this,
                    defaultExceptionHandler,
                    fabricExceptionHandler
                )
            )
        }else{
            Thread.setDefaultUncaughtExceptionHandler(
                HBSExceptionHandler(
                    this,
                    defaultExceptionHandler,
                    null
                )
            )
        }
    }
}