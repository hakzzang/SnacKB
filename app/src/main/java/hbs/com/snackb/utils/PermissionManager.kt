package hbs.com.snackb.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager{
    companion object{
        const val REQUEST_CODE_PERMISSIONS = 2001
        val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }
    /**
     * Check if all permission specified in the manifest have been granted
     */
    fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(activity:Activity){
        ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    fun showPermissionToast(context: Context){
        Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
    }
}