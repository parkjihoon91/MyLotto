package com.example.mylotto.config

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.mylotto.util.CustomLodingDialog

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")
    var loadingLodingDialog: CustomLodingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun showCustomToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showCustomDialog(context: Context, text: String) {
        loadingLodingDialog = CustomLodingDialog(context, text)
        loadingLodingDialog?.show()
    }

    fun dismissCustomDialog() {
        if (loadingLodingDialog?.isShowing == true) {
            loadingLodingDialog?.dismiss()
            loadingLodingDialog = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        loadingLodingDialog = null
    }

}