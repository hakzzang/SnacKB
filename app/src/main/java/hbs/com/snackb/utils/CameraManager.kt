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
    fun makePreviewConfig(viewFinder:TextureView): PreviewConfig
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

    // Create configuration object for the viewfinder use case
    override fun makePreviewConfig(viewFinder:TextureView): PreviewConfig {
        return PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(viewFinder.measuredWidth, viewFinder.measuredHeight))
        }.build()
    }

}