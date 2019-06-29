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

class ReportFragmentTwo : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_report_two, container, false)
        val reportViewModel = activity?.let { ViewModelProviders.of(it).get(ReportViewModel::class.java) }
        reportViewModel?.setSecondReportAnswer(Pair("1",view.context.resources.getString(R.string.all_text_one_per_day)))
        view.btn_next_report.setOnClickListener{
            reportViewModel?.addTabPosition()
        }
        view.btn_answer_1.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_1)
            reportViewModel?.setSecondReportAnswer(Pair("1",view.context.resources.getString(R.string.all_text_one_per_day)))
        }
        view.btn_answer_2.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_2)
            reportViewModel?.setSecondReportAnswer(Pair("2",view.context.resources.getString(R.string.all_text_one_per_week)))
        }
        view.btn_answer_3.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_3)
            reportViewModel?.setSecondReportAnswer(Pair("3",view.context.resources.getString(R.string.all_text_not_friendly_using)))
        }
        view.btn_answer_4.setOnClickListener {
            clearRadioButtons()
            checkRadioButton(view.btn_answer_4)
            reportViewModel?.setSecondReportAnswer(Pair("4",view.context.resources.getString(R.string.all_text_at_first)))
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