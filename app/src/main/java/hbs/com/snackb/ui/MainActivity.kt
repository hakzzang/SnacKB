package hbs.com.snackb.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Plane
import com.google.ar.sceneform.ux.ArFragment
import hbs.com.snackb.R
import hbs.com.snackb.utils.AppAnchorState
import hbs.com.snackb.utils.SceneformManager
import hbs.com.snackb.utils.SnackbarHelper
import hbs.com.snackb.utils.StorageManager
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity(), LifecycleOwner {
    private val arFragment by lazy{
        supportFragmentManager.findFragmentById(R.id.fragment_scene) as ArFragment
    }
    private val contentView by lazy { findViewById<View>(android.R.id.content) }
    private val fragmentScene by lazy{ fragment_scene as CloudAnchorFragment }
    private val sceneformManager:SceneformManager = SceneformManager(this, SnackbarHelper(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fragmentScene.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            run {
                sceneformManager.updateAnchorState(AppAnchorState.HOSTING)
                sceneformManager.addWidget(contentView, arFragment)
                sceneformManager.addObject(contentView, arFragment, Uri.parse("model.sfb"))
                sceneformManager.checkUpdatedAnchor()
            }
        }

        btn_make_ar.setOnClickListener {
            sceneformManager.updateAnchorState(AppAnchorState.HOSTING)
            sceneformManager.addWidget(contentView, arFragment)
            sceneformManager.addObject(contentView, arFragment, Uri.parse("model.sfb"))
            sceneformManager.checkUpdatedAnchor()
        }


        resolve_button.setOnClickListener {
            val storageManager = StorageManager(this)
            storageManager.currentShortCode(object : StorageManager.ShortCodeListener{
                override fun onShortCodeAvailable(shortCode: Int?) {
                    shortCode?.let { sceneformManager.getCloudAnchor(fragmentScene, shortCode, "model.sfb") }
                }
            })
        }
    }
}
