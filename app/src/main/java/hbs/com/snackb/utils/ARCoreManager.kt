package hbs.com.snackb.utils

import android.app.Activity
import android.widget.Toast
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session

interface ARCoreManager{
    fun initSession() : Session?
}

class ARCoreManagerImpl(private val activity: Activity) : ARCoreManager{
    private var userRequestedInstall = true
    private val session by lazy {
        initARSession()
    }

    override fun initSession() : Session? {
        isMakeSession(session)
        return session
    }

    private fun initARSession() : Session? {
        when (ArCoreApk.getInstance().requestInstall(activity, userRequestedInstall)) {
            ArCoreApk.InstallStatus.INSTALLED ->
                // Success, create the AR session.
                return Session(activity)
            ArCoreApk.InstallStatus.INSTALL_REQUESTED ->
                // Ensures next invocation of requestInstall() will either return
                // INSTALLED or throw an exception.
                userRequestedInstall = false
        }
        return null
    }

    private fun showPermissionToast(content:String){
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
    }

    private fun isMakeSession(session:Session?){
        if(session==null){
            showPermissionToast("You should be download ARCore in store.")
        }else{
            showPermissionToast("Ready to use ARCore.")
        }
    }

}