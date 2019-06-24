package hbs.com.snackb.utils

import android.graphics.Point
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class SceneformManager {
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

    private fun placeObject(fragment: ArFragment, createAnchor: Anchor, model: Uri) {
        ModelRenderable.builder().setSource(fragment.context, model).build().thenAccept {
            if (it != null)
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

    private fun addNode(fragment: ArFragment, createAnchor: Anchor, renderable: ModelRenderable?) {
        val anchorNode = AnchorNode(createAnchor)
        TransformableNode(fragment.transformationSystem).apply {
            this.renderable = renderable
            this.setParent(anchorNode)
            this.select()
        }
        fragment.arSceneView.scene.addChild(anchorNode)
    }

    private fun getScreenCenter(contentView:View): Point {
        return Point(contentView.getWidth() / 2, contentView.getHeight() / 2)
    }
}