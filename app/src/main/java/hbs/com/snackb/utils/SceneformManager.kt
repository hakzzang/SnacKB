package hbs.com.snackb.utils

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import hbs.com.snackb.R
import hbs.com.snackb.ui.CloudAnchorFragment


class SceneformManager(val context:Context, val snackbarHelper: SnackbarHelper) {
    private var accountRenderable : Renderable? = null
    private var cloudAnchor : Anchor? = null
    var appAnchorState = AppAnchorState.NONE
    private val storageManager by lazy{
        StorageManager(context)
    }

    fun getCloudAnchor(fragmentScene:CloudAnchorFragment,shortCode:Int, parse:String){
        storageManager.getCloudAnchorID(shortCode, object : StorageManager.CloudAnchorIdListener{
            override fun onCloudAnchorIdAvailable(cloudAnchorId: String?) {
                fragmentScene.arSceneView.session?.resolveCloudAnchor(cloudAnchorId)?.let { resolvedAnchor->
                    setCloudAnchor(resolvedAnchor)
                    placeObject(fragmentScene, cloudAnchor!!, Uri.parse(parse))
                    snackbarHelper.showMessage("Now Resolving Anchor...")
                    appAnchorState = AppAnchorState.RESOLVING
                }
            }
        })
    }

    fun addObject(contentView: View, arFragment: ArFragment, parse: Uri) {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter(contentView)
        if (frame != null) {
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (i in hits.indices) {
                val trackable = hits[i].trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hits[i].hitPose)) {
                    placeObject(arFragment, hits[i].createAnchor(), parse)
                }
            }
        }
    }

    fun addWidget(contentView: View, arFragment: ArFragment) {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter(contentView)
        if (frame != null) {
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (i in hits.indices) {
                val trackable = hits[i].trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hits[i].hitPose)) {
                    placeWidget(arFragment, hits[i].createAnchor())
                }
            }
        }
    }

    private fun placeWidget(fragment: ArFragment, createAnchor: Anchor) {
        ViewRenderable.builder()
            .setView(fragment.context, R.layout.view_ar_account_info)
            .build()
            .thenAccept { renderable ->
                updateHost(fragment.arSceneView.session, createAnchor)
                addNode(fragment, createAnchor, renderable)}
    }

    private fun updateHost(session: Session?, anchor: Anchor){
        session?.hostCloudAnchor(anchor)?.let {newAnchor->
            setCloudAnchor(newAnchor)
        }
    }

    private fun placeObject(fragment: ArFragment, createAnchor: Anchor, model: Uri) {
        ModelRenderable.builder().setSource(fragment.context, model).build().thenAccept {
            updateHost(fragment.arSceneView.session, createAnchor)
            addNode(fragment, createAnchor, it)
        }.exceptionally {
            val builder = fragment.context?.let{ it ->
                AlertDialog.Builder(it)
            }
            builder?.apply {
                setMessage(it.message)?.setTitle("error!")
                create()
            }?.show()
            return@exceptionally null
        }
    }

    private fun addNode(fragment: ArFragment, createAnchor: Anchor, renderable: ModelRenderable?) : TransformableNode {
        val anchorNode = AnchorNode(createAnchor)
        val transformableNode = TransformableNode(fragment.transformationSystem).apply {
            val frontRotation = Quaternion.axisAngle(Vector3(0.0f, -0.98f, 0.0f), 90f)
            this.scaleController.maxScale = 0.25f
            this.scaleController.minScale = 0.2f
            this.renderable = renderable
            this.localRotation = frontRotation
            this.setParent(anchorNode)
            this.select()
        }
        fragment.arSceneView.scene.addChild(anchorNode)
        return transformableNode
    }

    private fun addNode(fragment: ArFragment, createAnchor: Anchor, renderable: Renderable?) {
        val anchorNode = AnchorNode(createAnchor)
        TransformableNode(fragment.transformationSystem).apply {
            this.renderable = renderable
            this.localPosition = Vector3(0.0f, 0.15f, 0.0f)
            this.setParent(anchorNode)
        }
        fragment.arSceneView.scene.addChild(anchorNode)
    }

    private fun getScreenCenter(contentView:View): Point {
        return Point(contentView.getWidth() / 2, contentView.getHeight() / 2)
    }

    @Synchronized
    fun checkUpdatedAnchor() {
        if (appAnchorState !== AppAnchorState.HOSTING && appAnchorState !== AppAnchorState.RESOLVING) {
            return
        }
        cloudAnchor?.cloudAnchorState?.let{cloudState->
            if (appAnchorState === AppAnchorState.HOSTING) {
                if (cloudState.isError) {
                    snackbarHelper.showMessageWithDismiss("Error hosting anchor.. $cloudState")
                    appAnchorState = AppAnchorState.NONE
                } else if (cloudState == Anchor.CloudAnchorState.SUCCESS) {
                    storageManager.nextShortCode(object : StorageManager.ShortCodeListener{
                        override fun onShortCodeAvailable(shortCode: Int?) {
                            if (shortCode == null) {
                                snackbarHelper.showMessageWithDismiss( "Could not get shortCode")
                                return
                            }
                            storageManager.storeUsingShortCode(shortCode, cloudAnchor?.cloudAnchorId)
                            snackbarHelper.showMessageWithDismiss("Anchor hosted! Cloud Short Code: " + shortCode)
                        }
                    })
                    appAnchorState = AppAnchorState.HOSTED
                }
            } else if (appAnchorState === AppAnchorState.RESOLVING) {
                if (cloudState.isError) {
                    snackbarHelper.showMessageWithDismiss("Error resolving anchor.. $cloudState")
                    appAnchorState = AppAnchorState.NONE
                } else if (cloudState == Anchor.CloudAnchorState.SUCCESS) {
                    snackbarHelper.showMessageWithDismiss("Anchor resolved successfully")
                    appAnchorState = AppAnchorState.RESOLVED
                }
            }
        }
    }

    private fun setCloudAnchor (newAnchor:Anchor){
        cloudAnchor?.apply{ detach() }
        cloudAnchor = newAnchor
        appAnchorState = AppAnchorState.NONE
    }

    fun updateAnchorState(anchorState: AppAnchorState){
        appAnchorState = anchorState
    }
}