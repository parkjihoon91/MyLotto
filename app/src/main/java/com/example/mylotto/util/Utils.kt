package com.example.mylotto.util

import android.content.Context
import android.view.View

object Utils {

    fun showAlertDialogSecondOnClickListener(
        ctx: Context,
        title: String,
        msg: String,
        onClickListener: View.OnClickListener,
    ) {
        CustomAlertDialogSecond(ctx)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("확인", onClickListener)
            .setNegativeButton("취소", null)
            .show()
    }

}