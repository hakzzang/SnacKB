package hbs.com.snackb.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import hbs.com.snackb.R
import hbs.com.snackb.databinding.ActivityMapBinding
import hbs.com.snackb.viewmodel.MapViewModel

class MapActivity : AppCompatActivity() {
    private val activityMapBinding: ActivityMapBinding by lazy{
        DataBindingUtil.setContentView<ActivityMapBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMapBinding.apply{
            lifecycleOwner = this@MapActivity
            mapViewModel = ViewModelProviders.of(this@MapActivity).get(MapViewModel::class.java)
        }
    }
}