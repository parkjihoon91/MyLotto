package com.example.mylotto.main.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mylotto.R
import com.example.mylotto.config.BaseFragment
import com.example.mylotto.databinding.FragmentPurchaseBinding
import com.example.mylotto.main.viewmodel.PurchaseViewModel

class PurchaseFragment : BaseFragment<FragmentPurchaseBinding>(R.layout.fragment_purchase) {

    private lateinit var viewModel: PurchaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("111PurchaseFragment", "onCreateView: ")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("111PurchaseFragment", "onViewCreated: ")
        viewModel = ViewModelProvider(this)[PurchaseViewModel::class.java]
        binding.lifecycleOwner = this
        binding.purchaseViewModel = viewModel

    }

    override fun onPause() {
        super.onPause()
        viewModel.test()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("111PurchaseFragment", "onDestroyView: ")
    }

}

