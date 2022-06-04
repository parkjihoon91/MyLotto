package com.example.mylotto.main.viewmodel

import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylotto.config.MyApplication
import com.example.mylotto.util.room.AppDatabase
import com.example.mylotto.util.room.Lotto
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

private const val drawNumber = 10 // 반복횟수
class PurchaseViewModel : ViewModel() {

    private var db: AppDatabase? = null
    private var drawLottoNumberList = ArrayList<Int>()
    private var resultLottoList = ArrayList<String>()

    private val _lottoText = MutableLiveData<String>()
    val lottoText: LiveData<String>
        get() = _lottoText

    private val _seq = MutableLiveData<String>()
    val seq: LiveData<String>
        get() = _seq

    private val _cnt = MutableLiveData<String>()
    val cnt: LiveData<String>
        get() = _cnt

    init {
        db = AppDatabase.getInstance(MyApplication.applicationContext())
    }

    fun test() {
        _lottoText.value = ""
    }

    fun deleteContent() {
        _seq.value = ""
        _cnt.value = ""
        _lottoText.value = ""
    }

    fun decideLottoNumber(str: EditText) {
        viewModelScope.launch {
            val date = Date()
            val simpleDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val createTime = simpleDate.format(date)
            for (i in 0 until resultLottoList.size) {
                Log.d("TAG", "onCreate : $createTime")
                val lotto =
                    Lotto(
                        resultLottoList[i],
                        createTime,
                        str.text.toString(),
                        0,
                        0,null
                    )
                val a = db?.contactsDao()?.insert(lotto)
            }
            toast("번호 저장완료")
        }
    }

    fun createLottoNumber(input: EditText, cnt: EditText) {
        if (input.text.toString().isEmpty()) {
            toast("회차를 입력해주세요.")
            return
        }
        if (cnt.text.toString().isEmpty()) {
            toast("횟수를 입력해주세요.")
            return
        }

        var resultLottoNumber = ""
        for (i in 0 until cnt.text.toString().toInt()) {
            createLottoNumber() // 번호 생성
            resultLottoNumber += createResultLottoNumber() + "\n"

        }
        _lottoText.value = resultLottoNumber
        toast("번호 생성완료")

    }

    // 번호 ,로 연결
    private fun createResultLottoNumber(): String {
        var resultLottoNumber = ""
        drawLottoNumberList.sort()
        for (i in 0..5) {
            if (i == 5) {
                resultLottoNumber += drawLottoNumberList[i].toString()
                break
            }
            resultLottoNumber += drawLottoNumberList[i].toString() + ", "
        }
        resultLottoList.add(resultLottoNumber)
        return "[$resultLottoNumber]"
    }

    // 로또번호생성 1~45 중 6개를 뽑기 10번 반복
    private fun createLottoNumber() {
        drawLottoNumberList.clear()
        val range = (1..45)
        for (i in 0 until drawNumber) {
            while (drawLottoNumberList.size <= 5) {
                val num = range.random()
                if (!drawLottoNumberList.contains(num)) {
                    drawLottoNumberList.add(num)
                }
            }
            if (i != drawNumber - 1) {
                drawLottoNumberList.clear()
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(MyApplication.applicationContext(), message, Toast.LENGTH_SHORT).show()
    }

}