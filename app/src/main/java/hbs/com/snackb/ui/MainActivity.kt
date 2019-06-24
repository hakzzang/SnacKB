package hbs.com.snackb.ui

import android.os.Bundle
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import hbs.com.snackb.R
import hbs.com.snackb.utils.CameraManagerImpl
import hbs.com.snackb.utils.PermissionManager
import hbs.com.snackb.utils.PermissionManager.Companion.REQUEST_CODE_PERMISSIONS
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LifecycleOwner {
    private val cameraManager  = CameraManagerImpl()
    private val permissionManager = PermissionManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission(true)
        // Every time the provided texture view changes, recompute layout
        addLayoutChangeListener(view_finder)
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

}
