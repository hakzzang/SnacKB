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
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*


class ErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding
    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
    private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }
    private lateinit var rootRef: DatabaseReference
    private val ERROR_KEY_ROOT = "error_contents"

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
        initFirebaseDatabase()
    }

    companion object {
        const val EXTRA_INTENT = "EXTRA_INTENT"
        const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == 200) {
            val appName = data?.getStringExtra("appName")
            val answer1 = data?.getStringExtra("answer1")
            val answer2 = data?.getStringExtra("answer2")
            val answer3 = data?.getStringExtra("answer3")
            val errorText = errorText
            val errorContent = HBSErrorContent(answer1, answer2, answer3, errorText)
            saveErrorData(errorContent)
            completeToPost()
        }
    }

    private fun completeToPost() {
        lottie_view.setAnimation("completed.json")
        btn_post_report.apply {
            background = resources.getDrawable(R.drawable.btn_rounded_grey)
            text = "전송 완료"
            isClickable = false
        }
        tv_error_main.text = "COMPLETED!!"
    }

    private fun initFirebaseDatabase() {
        val options = FirebaseOptions.Builder()
            .setApplicationId("1:229578747103:android:23f79f4791b2dad0") // Required for Analytics.
            .setApiKey("AIzaSyAM9VhuyVqMcC5ehF1KtahOS4wa4k5Aa-w") // Required for Auth.
            .setDatabaseUrl("https://snackb.firebaseio.com") // Required for RTDB.
            .build()
        FirebaseApp.initializeApp(this /* Context */, options, "secondary").let {
            rootRef = FirebaseDatabase.getInstance(it).reference.child(ERROR_KEY_ROOT)
        }
        DatabaseReference.goOnline()
    }

    private fun saveErrorData(errorContent: HBSErrorContent) {
        rootRef
            .child(applicationContext.packageName.replace(".",""))
            .push().setValue(errorContent)
    }
}