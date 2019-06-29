package hbs.com.defender.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import hbs.com.defender.R
import kotlinx.android.synthetic.main.fragment_report_one.view.*
import kotlinx.android.synthetic.main.fragment_report_one.view.btn_next_report
import kotlinx.android.synthetic.main.fragment_report_three.view.*

class ReportFragmentThree : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_report_three, container, false)
        val reportViewModel = activity?.let { ViewModelProviders.of(it).get(ReportViewModel::class.java) }
        reportViewModel?.setThirdReportAnswer(Pair("0","EMPTY"))
        view.btn_next_report.setOnClickListener{
            if(view.et_comment.text == null){
                reportViewModel?.setThirdReportAnswer(Pair("0", ""))
            }else{
                reportViewModel?.setThirdReportAnswer(Pair("0", view.et_comment.text.toString()))
            }
            reportViewModel?.addTabPosition()
        }
        return view
    }
}