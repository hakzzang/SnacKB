package hbs.com.snackb.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import hbs.com.snackb.R
import hbs.com.snackb.utils.CameraManager
import hbs.com.snackb.utils.CameraManagerImpl
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.jar.Manifest


class MainActivity : AppCompatActivity(), LifecycleOwner {
    private val cameraManager  = CameraManagerImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (allPermissionsGranted()) {
            postCameraView(cameraManager, view_finder, this)
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        // Every time the provided texture view changes, recompute layout
        view_finder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            cameraManager.updateTransform(view_finder)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                postCameraView(cameraManager, view_finder, this)
            } else {
                showPermissionToast(this@MainActivity)
                finish()
            }
        }
    }


    private fun showPermissionToast(context: Context){
        Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
    }

    private fun postCameraView(cameraManager: CameraManager, viewFinder:TextureView, lifecycleOwner: LifecycleOwner){
        viewFinder.post { cameraManager.startCamera(viewFinder,lifecycleOwner) }
    }

}
