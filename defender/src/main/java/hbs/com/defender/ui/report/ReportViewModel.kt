package hbs.com.defender.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel : ViewModel(){
    private val _reportTabPosition = MutableLiveData<Int>()
    val reportTabPosition:LiveData<Int> = _reportTabPosition

    private val _firstReportAnswer = MutableLiveData<Pair<String,String>>()
    val firstReportAnswer: LiveData<Pair<String, String>> = _firstReportAnswer

    private val _secondReportAnswer = MutableLiveData<Pair<String,String>>()
    val secondReportAnswer: LiveData<Pair<String, String>> = _secondReportAnswer

    private val _thirdReportAnswer = MutableLiveData<Pair<String,String>>()
    val thirdReportAnswer: LiveData<Pair<String, String>> = _thirdReportAnswer

    fun initTabPosition(){
        _reportTabPosition.postValue(0)
    }
    fun addTabPosition(){
        _reportTabPosition.postValue(reportTabPosition.value?.plus(1))
        Log.d("_reportTabPosition",_reportTabPosition.value.toString())
    }

    fun setFirstReportAnswer(pair: Pair<String,String>){
        _firstReportAnswer.postValue(pair)
    }

    fun setSecondReportAnswer(pair: Pair<String,String>){
        _secondReportAnswer.postValue(pair)
    }

    fun setThirdReportAnswer(pair: Pair<String,String>){
        _thirdReportAnswer.postValue(pair)
    }
}