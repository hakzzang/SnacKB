package hbs.com.defender.ui.error

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import hbs.com.defender.R
import hbs.com.defender.databinding.ActivityErrorBinding
import hbs.com.defender.ui.report.ReportActivity
import kotlinx.android.synthetic.main.activity_error.*


class ErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding

    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
    private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_error)

        binding.tvErrorLog.text = errorText

        binding.btnReload.setOnClickListener {
            startActivity(lastActivityIntent)
            finish()
        }

        binding.btnPostReport.setOnClickListener {
            startActivityForResult(Intent(this, ReportActivity::class.java), 200)
        }
    }

    companion object {
        const val EXTRA_INTENT = "EXTRA_INTENT"
        const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 200 && resultCode == 200){
            completeToPost()
        }
    }

    private fun completeToPost(){
        lottie_view.setAnimation("completed.json")
        btn_post_report.apply{
            background = resources.getDrawable(R.drawable.btn_rounded_grey)
            text = "전송 완료"
            isClickable = false
        }
        tv_error_main.text = "COMPLETED!!"
    }
}