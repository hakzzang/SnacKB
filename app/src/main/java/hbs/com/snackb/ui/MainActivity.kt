package hbs.com.snackb.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.google.ar.sceneform.ux.ArFragment
import hbs.com.snackb.R
import hbs.com.snackb.utils.SceneformManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LifecycleOwner {
    private val sceneformManager = SceneformManager()
    private val arFragment by lazy{
        supportFragmentManager.findFragmentById(R.id.fragment_scene) as ArFragment
    }
    private val contentView by lazy {
        findViewById<View>(android.R.id.content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_make_ar.setOnClickListener {
            sceneformManager.addObject(contentView, arFragment, Uri.parse("model.sfb"))
        }
    }
}
