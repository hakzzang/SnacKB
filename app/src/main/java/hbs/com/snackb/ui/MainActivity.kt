package hbs.com.snackb.ui

import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import hbs.com.snackb.R


class MainActivity : AppCompatActivity(), LifecycleOwner {
    /*private val cameraManager : CameraManager = CameraManagerImpl()
    private val permissionManager = PermissionManager()
    private val arCoreManager : ARCoreManager = ARCoreManagerImpl(this)*/
    lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arFragment = supportFragmentManager.findFragmentById(R.id.fragment_scene) as ArFragment
        addObject(Uri.parse("model.sfb"))


    }

    private fun addObject(parse: Uri) {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter()
        if (frame != null) {
            val hits = frame.hitTest(point.x as Float, point.y as Float)

            for (i in hits.indices) {
                val trackable = hits[i].trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hits[i].hitPose)) {
                    placeObject(arFragment, hits[i].createAnchor(), parse)
                }
            }
        }
    }

    private fun placeObject(fragment: ArFragment, createAnchor: Anchor, model: Uri) {
        ModelRenderable.builder().setSource(fragment.context, model).build().thenAccept {
            if (it != null)
                this@MainActivity.addNode(arFragment, createAnchor, it)
        }.exceptionally {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage(it.message).setTitle("error!")
            val dialog = builder.create()
            dialog.show()
            null
        }
    }

    private fun addNode(fragment: ArFragment, createAnchor: Anchor, renderable: ModelRenderable?) {
        val anchorNode = AnchorNode(createAnchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        transformableNode.renderable = renderable
        transformableNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }

    private fun getScreenCenter(): Point {
        val vw = findViewById<View>(android.R.id.content)
        return Point(vw.getWidth() / 2, vw.getHeight() / 2)
    }
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
