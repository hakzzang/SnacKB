package hbs.com.snackb.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class PermissionManager{
    private val REQUEST_CODE_PERMISSIONS = 2001
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted(baseContext: Context) = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun checkPermission(baseContext: Context){
        if (allPermissionsGranted(baseContext)) {
            postCameraView(cameraManager, view_finder, this)
        } else {
            showPermissionToast(this@MainActivity)
            finish()
        }
    }
}