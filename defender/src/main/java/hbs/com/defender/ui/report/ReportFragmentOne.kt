package hbs.com.defender.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import hbs.com.defender.R
import kotlinx.android.synthetic.main.fragment_report_one.view.*

class ReportFragmentOne : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_report_one, container, false)
        val reportViewModel = activity?.let { ViewModelProviders.of(it).get(ReportViewModel::class.java) }
        reportViewModel?.setFirstReportAnswer(Pair("1",view.context.resources.getString(R.string.all_text_using_network)))
        view.btn_next_report.setOnClickListener{
            reportViewModel?.addTabPosition()
        }
        view.btn_answer_1.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_1)
            reportViewModel?.setFirstReportAnswer(Pair("1",view.context.resources.getString(R.string.all_text_using_network)))
        }
        view.btn_answer_2.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_2)
            reportViewModel?.setFirstReportAnswer(Pair("2",view.context.resources.getString(R.string.all_text_switch_screen)))
        }
        view.btn_answer_3.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_3)
            reportViewModel?.setFirstReportAnswer(Pair("3",view.context.resources.getString(R.string.all_text_start_and_end_app)))
        }
        view.btn_answer_4.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_4)
            reportViewModel?.setFirstReportAnswer(Pair("4",view.context.resources.getString(R.string.all_text_etc)))
        }
        return view
    }

    private fun clearRadioButtons(){
        view?.btn_answer_1?.isChecked = false
        view?.btn_answer_2?.isChecked = false
        view?.btn_answer_3?.isChecked = false
        view?.btn_answer_4?.isChecked = false
    }

    private fun checkRadioButton(radioButton: RadioButton){
        radioButton.isChecked = true
    }
}