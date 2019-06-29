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
            // Crashlytics에서 기본 handler를 호출하기 때문에 이중으로 호출되는것을 막기위해 빈 handler로 설정
        }
        //Fabric.with(this, Crashlytics())
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