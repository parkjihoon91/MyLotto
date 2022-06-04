package com.example.mylotto.main

import com.example.mylotto.util.room.Lotto

interface OnItemClick {
    fun onDeleteClick(number: String, time: String, position: Int)
    fun onModifyClick(data: Lotto, position: Int)
}