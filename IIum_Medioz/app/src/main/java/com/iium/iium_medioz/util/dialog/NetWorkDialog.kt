package com.iium.iium_medioz.util.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.FragmentFirstDialogBinding
import com.iium.iium_medioz.databinding.FragmentNetWorkDialogBinding

class NetWorkDialog :  DialogFragment() {

    private var _binding: FragmentNetWorkDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNetWorkDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.okBtn.setOnClickListener {
            dismiss()    // 대화상자를 닫는 함수
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}