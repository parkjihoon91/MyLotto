package com.example.mylotto.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import com.example.mylotto.R

class CustomLodingDialog(context: Context, text: String) : Dialog(context) {
    private var tv: TextView? = null
    private var dialogText = ""

    init {
        dialogText = text
        setCanceledOnTouchOutside(false)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.dialog_progress)

        tv = findViewById(R.id.tv_dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tv!!.text = dialogText
    }

}