package com.example.mylotto.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylotto.config.MyApplication
import com.example.mylotto.util.room.AppDatabase
import com.example.mylotto.util.room.Lotto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PurchaseListViewModel : ViewModel() {

    private var db: AppDatabase? = null
    var isLoading = MutableLiveData<Boolean>()
    var isChecked = MutableLiveData<Boolean>()
    var dataList= ArrayList<Lotto?>()
    private var _deleteCnt = MutableLiveData<Int?>()
    val deleteCnt: LiveData<Int?>
        get() = _deleteCnt

    private var _list = MutableLiveData<MutableList<Lotto?>?>()
    val list: LiveData<MutableList<Lotto?>?>
        get() = _list

    init {
        db = AppDatabase.getInstance(MyApplication.applicationContext())
    }

    fun initLoding() {
        isLoading.value = true

        viewModelScope.launch {
            delay(2000)
            initView()
            isLoading.value = false
        }
    }

    fun initView() {
        val select = db?.contactsDao()?.select()
        dataList.clear()
        if (select?.size != 0) {
            // 무한스크롤 아이템수 12개로 설정
            if (select?.size!! >= 12) {
                for (i in 0..11) {
                    dataList.add(select[i])
                }
                // 12미만일때
            } else {
                for (i in 0 until select.size) {
                    dataList.add(select[i])
                }
            }
            _list.value = dataList
        }
    }

    fun delete(number: String, time: String) {
        viewModelScope.launch {
            val cnt = db?.contactsDao()?.delete(number, time)
            _deleteCnt.value = cnt
        }
    }

    fun deleteCntInit() {
        _deleteCnt.value = 0
    }

    fun test(checked: Boolean, price: String, grade: String,
             content: String, number: String, time: String) {
        viewModelScope.launch {
            db?.contactsDao()?.update(checked, price, grade, content, number, time)
            isChecked.value = true
        }
        isChecked.value = false
    }
}