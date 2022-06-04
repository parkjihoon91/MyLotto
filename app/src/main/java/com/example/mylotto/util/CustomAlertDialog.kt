package com.example.mylotto.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.StringRes
import com.example.mylotto.R

class CustomAlertDialog(private val context: Context) {

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context).setView(view)
    }

    private val view: View by lazy {
        View.inflate(context, R.layout.dialog_custom, null)
    }

    private var dialog: AlertDialog? = null

    // 터치 리스너 구현
    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 5)
        }
        false
    }

    fun setTitle(@StringRes titleId: Int): CustomAlertDialog {
        view.findViewById<TextView>(R.id.dialog_title).text = context.getText(titleId)
        return this
    }

    fun setTitle(title: CharSequence?): CustomAlertDialog {
        view.findViewById<TextView>(R.id.dialog_title).text = title
        return this
    }

    fun setMessage(@StringRes messageId: Int): CustomAlertDialog {
        view.findViewById<TextView>(R.id.dialog_content).text = context.getText(messageId)
        return this
    }

    fun setMessage(message: CharSequence?): CustomAlertDialog {
        view.findViewById<TextView>(R.id.dialog_content).text = message
        return this
    }

    fun setPositiveButton(
        @StringRes textId: Int,
        listener: View.OnClickListener
    ): CustomAlertDialog {
        view.findViewById<TextView>(R.id.dialog_check).apply {
            setOnClickListener(listener)
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: View.OnClickListener?): CustomAlertDialog {
        view.findViewById<TextView>(R.id.dialog_check).apply {
            setOnClickListener(listener)
            setOnTouchListener(onTouchListener)

        }
        return this
    }

    fun setPositiveButton2(text: CharSequence, listener: View.OnClickListener): CustomAlertDialog {
        view.findViewById<TextView>(R.id.dialog_check).apply {
            setOnClickListener(listener)

        }
        return this
    }

    fun create() {
        dialog = builder.create()
    }

    fun show() {
        createDialog()
    }

    fun showCancelable() {
        createDialog()
        dialog?.setCancelable(false)
    }

    fun showOnDismiss(onDismiss: DialogInterface.OnDismissListener?) {
        createDialog()
        dialog?.setOnDismissListener(onDismiss)
    }

    private fun createDialog() {
        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val params = WindowManager.LayoutParams()
        params.copyFrom(dialog?.window?.attributes)
        params.width = 900
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.show()
        val window = dialog?.window
        window?.attributes = params
        window?.setDimAmount(0.7f)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}