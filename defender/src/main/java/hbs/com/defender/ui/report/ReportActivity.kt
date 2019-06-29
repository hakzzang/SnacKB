package hbs.com.defender.ui.report;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import hbs.com.defender.R

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        val reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        reportViewModel.initTabPosition()
        reportViewModel.reportTabPosition.observe(this, Observer<Int> { position ->
            when (position) {
                0 -> changeFragmentTo(ReportFragmentOne())
                1 -> changeFragmentTo(ReportFragmentTwo())
                2 -> changeFragmentTo(ReportFragmentThree())
                3 -> {
                    setResult(200, intent)
                    finish()
                }
            }
        })
    }

    private fun changeFragmentTo(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_report_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commitNow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}
