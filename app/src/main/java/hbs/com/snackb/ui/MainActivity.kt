package hbs.com.snackb.ui

import android.os.Bundle
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import hbs.com.snackb.utils.*
import hbs.com.snackb.utils.PermissionManager.Companion.REQUEST_CODE_PERMISSIONS
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LifecycleOwner {
    private val cameraManager : CameraManager = CameraManagerImpl()
    private val permissionManager = PermissionManager()
    private val arCoreManager : ARCoreManager = ARCoreManagerImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hbs.com.snackb.R.layout.activity_main)

        checkPermission(true)
        // Every time the provided texture view changes, recompute layout
        addLayoutChangeListener(view_finder)
        checkInstallARCore()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            checkPermission(false)
        }
    }

    private fun addLayoutChangeListener(viewFinder:TextureView){
        cameraManager.addLayoutChangeListener(viewFinder)
    }

    private fun checkPermission(isFirst:Boolean){
        if(permissionManager.allPermissionsGranted(this)){
            cameraManager.postCameraView(cameraManager, view_finder, this)
        }else{
            responseForFailedPermission(isFirst)
        }
    }

    private fun responseForFailedPermission(isFirst: Boolean){
        if(isFirst){
            permissionManager.requestPermissions(this)
        }else{
            permissionManager.showPermissionToast(this)
            finish()
        }
    }

    private fun checkInstallARCore(){
        // Make sure ARCore is installed and up to date.
        arCoreManager.initSession().run {}
    }
}
