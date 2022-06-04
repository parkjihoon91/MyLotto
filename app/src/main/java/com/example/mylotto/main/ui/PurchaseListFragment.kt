package com.example.mylotto.main.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylotto.R
import com.example.mylotto.config.BaseFragment
import com.example.mylotto.databinding.FragmentPurchaseListBinding
import com.example.mylotto.main.SwipeHelperCallback
import com.example.mylotto.main.OnItemClick
import com.example.mylotto.main.viewmodel.PurchaseListViewModel
import com.example.mylotto.main.adapter.PurchaseListAdapter
import com.example.mylotto.util.Utils
import com.example.mylotto.util.room.AppDatabase
import com.example.mylotto.util.room.Lotto
import kotlinx.coroutines.*

class PurchaseListFragment :
    BaseFragment<FragmentPurchaseListBinding>(R.layout.fragment_purchase_list), OnItemClick {
    private var db: AppDatabase? = null
    private lateinit var adapter: PurchaseListAdapter
    private lateinit var viewModel: PurchaseListViewModel
    private lateinit var swipeHelperCallback: SwipeHelperCallback
    private lateinit var totalList: MutableList<Lotto?>
    private lateinit var list: ArrayList<Lotto?>
    private var totalCnt = 0
    private var isLoading = false
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("111PurchaseListFragment", "onCreateView: ")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("111PurchaseListFragment", "onViewCreated: ")
        db = AppDatabase.getInstance(requireContext())
        viewModel = ViewModelProvider(this)[PurchaseListViewModel::class.java]
        binding.lifecycleOwner = this
        binding.purchaseListViewModel = viewModel

        totalList = db?.contactsDao()?.select() as MutableList<Lotto?>
        totalCnt = totalList.size

        swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(200f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recordRecyclerview)

        // 비동기 처리 동안 로딩바
        viewModel.initLoding()
        viewModel.isLoading.observe(viewLifecycleOwner) {
            when {
                it == null -> {
                }
                it -> {
                    showCustomDialog(requireContext(), "데이터를 가져오는 중..")
                }
                else -> {
                    dismissCustomDialog()
                }
            }
        }

//        viewModel.initView()
        viewModel.list.observe(viewLifecycleOwner) { data ->
            if (data == null) {
                return@observe
            }

            list = data as ArrayList<Lotto?>
            adapter = PurchaseListAdapter(viewModel, data, this)

            binding.apply {
                recordRecyclerview.layoutManager =
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                recordRecyclerview.setHasFixedSize(true)
                recordRecyclerview.addItemDecoration(DividerItemDecoration(requireContext(), 1))
                recordRecyclerview.adapter = adapter

                recordRecyclerview.setOnTouchListener { _, _ ->
                    swipeHelperCallback.removePreviousClamp(recordRecyclerview)
                    false
                }

            }

        }

        binding.recordRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 21 10 10 1 0
                if (!isLoading) {
                    Log.d("TAG", "onViewCreated1111: ${list.size}")
                    if ((recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == list.size - 1) {
                        Log.e("true", "True")
                        totalCnt -= 13
                        Log.d("TAG", "onScrolled: $totalCnt")
                        if (totalCnt > 0) {
                            isLoading = true
                            moreItems()
                        }
                    }

                }

            }
        })

        viewModel.deleteCnt.observe(requireActivity()) {
            if (it != null && it > 0) {
                adapter.setList(db?.contactsDao()?.select() as MutableList<Lotto?>)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, adapter.itemCount)
                swipeHelperCallback.removePreviousClamp3(binding.recordRecyclerview)
//                viewModel.delete2()
            }
        }


    }

    fun moreItems() {
        // null일때 로딩바를 띄우기위해서 add
        list.add(null)
        adapter.notifyItemInserted(list.size - 1)

        lifecycleScope.launch {
            delay(1000)
            list.removeAt(list.size - 1)
            val scrollPosition = list.size
            adapter.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            var nextLimit = currentSize + 13
            Log.e("hello", "${nextLimit}")

            if (currentSize < totalList.size - 13) {
                while (currentSize - 1 < nextLimit) {
                    list.add(totalList[currentSize])
                    currentSize++
                }
            } else {
                while (currentSize != totalList.size) {
                    list.add(totalList[currentSize])
                    currentSize++
                }
            }

            adapter.setList(list)
            adapter.notifyItemRangeChanged(scrollPosition, adapter.itemCount)
            isLoading = false
        }
    }

    override fun onDeleteClick(number: String, time: String, position: Int) {
        Utils.showAlertDialogSecondOnClickListener(
            requireContext(),
            "안내", "정말 삭제하시겠습니까?"
        ) {
            this.position = position
            viewModel.delete(number, time)
        }
    }

    override fun onModifyClick(data: Lotto, position: Int) {
        val dialog =
            AlertDialog.Builder(requireContext()).setView(R.layout.dialog_lotto_modify)
                .create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val params = WindowManager.LayoutParams()
        params.copyFrom(dialog?.window?.attributes)
        params.width = 900
        params.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.show()

        val window = dialog?.window
        window?.attributes = params
        window?.setDimAmount(0.7f)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        val modifyBtn: TextView = dialog.findViewById(R.id.dialog_modify_po)
        val neBtn: TextView = dialog.findViewById(R.id.dialog_modify_ne)
        val index: TextView = dialog.findViewById(R.id.dialog_modify_index)
        val number: TextView = dialog.findViewById(R.id.dialog_modify_number)
        val time: TextView = dialog.findViewById(R.id.dialog_modify_time)
        val isChecked: ImageView = dialog.findViewById(R.id.dialog_modify_check)
        val price: EditText = dialog.findViewById(R.id.dialog_price)
        val grade: EditText = dialog.findViewById(R.id.dialog_grade)
        val content: EditText = dialog.findViewById(R.id.dialog_content)

        index.text = data.seq
        number.text = data.number
        time.text = data.time

        if (data.price != null) {
            price.setText(data.price.toString())
        }
        if (data.grade != null) {
            grade.setText(data.grade.toString())
        }

        price.setText("0원")
        grade.setText("꽝")
        val text = data.content ?: ""
        content.setText(text)

        if (data.checked) {
            isChecked.setImageResource(R.drawable.lotto_true)
        } else {
            isChecked.setImageResource(R.drawable.lotto_false)
        }

        var isCheckedValue = false
        isChecked.setOnClickListener {
            val baseImage = isChecked.drawable
            val equalsImage = ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.lotto_true,
                null
            )
            val baseBitmap = (baseImage as BitmapDrawable).bitmap
            val equalsBitmap = (equalsImage as BitmapDrawable).bitmap
            if (baseBitmap.equals(equalsBitmap)) {
                isChecked.setImageResource(R.drawable.lotto_false)
                isCheckedValue = false
            } else {
                isChecked.setImageResource(R.drawable.lotto_true)
                isCheckedValue = true
            }
        }

        modifyBtn.setOnClickListener {
            lifecycleScope.launch {
                db?.contactsDao()?.update(
                    isCheckedValue, price.text.toString(), grade.text.toString(),
                    content.text.toString(), number.text.toString(), time.text.toString()
                )
                adapter.setList(db?.contactsDao()?.select() as MutableList<Lotto?>)
                adapter.notifyItemChanged(position)
                swipeHelperCallback.removePreviousClamp2(binding.recordRecyclerview)
                dialog.dismiss()
            }
        }

        neBtn.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.deleteCntInit()
    }
}