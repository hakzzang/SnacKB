package hbs.com.snackb.utils

import android.graphics.Matrix
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.LifecycleOwner

interface CameraManager{
    fun startCamera(viewFinder:TextureView, lifecycleOwner: LifecycleOwner)
    fun updateTransform(viewFinder: TextureView)
    fun addLayoutChangeListener(viewFinder: TextureView)
    fun makePreviewConfig(viewFinder:TextureView): PreviewConfig
    fun postCameraView(cameraManager: CameraManager, viewFinder:TextureView, lifecycleOwner: LifecycleOwner)
}

class CameraManagerImpl : CameraManager{
    override fun startCamera(viewFinder:TextureView, lifecycleOwner: LifecycleOwner) {
        // Build the viewfinder use case
        val preview = Preview(makePreviewConfig(viewFinder))

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {
            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform(viewFinder)
        }

        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.bindToLifecycle(lifecycleOwner, preview)
    }

    override fun updateTransform(viewFinder: TextureView) {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }


    override fun addLayoutChangeListener(viewFinder: TextureView) {
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform(viewFinder)
        }
    }

    // Create configuration object for the viewfinder use case
    override fun makePreviewConfig(viewFinder:TextureView): PreviewConfig {
        return PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(viewFinder.measuredWidth, viewFinder.measuredHeight))
        }.build()
    }

    override fun postCameraView(cameraManager: CameraManager, viewFinder:TextureView, lifecycleOwner: LifecycleOwner){
        viewFinder.post { cameraManager.startCamera(viewFinder,lifecycleOwner) }
    }

    /*private val cameraManager : CameraManager = CameraManagerImpl()
    private val permissionManager = PermissionManager()
    private val arCoreManager : ARCoreManager = ARCoreManagerImpl(this)*/

    /*override fun onCreate(savedInstanceState: Bundle?) {
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
        arCoreManager.initSession()?.let { session->
            session.sharedCamera
        }
    }*/
}