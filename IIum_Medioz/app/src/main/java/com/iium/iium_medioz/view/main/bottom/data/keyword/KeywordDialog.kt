package com.iium.iium_medioz.view.main.bottom.data.keyword

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.iium.iium_medioz.databinding.DialogKeywordBinding
import com.iium.iium_medioz.view.main.bottom.data.DataUploadActivity

class KeywordDialog(
    private val context: Context,
    listener: DataUploadActivity
) {
    private val mCallback = listener
    private val dialog = Dialog(context)

    fun createDialog(keyword: String) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val binding = DialogKeywordBinding.inflate(LayoutInflater.from(context))
        binding.keyword = keyword

        val params: ViewGroup.LayoutParams? = dialog.window!!.attributes
        val deviceWidth = size.x
        params!!.width = (deviceWidth * 0.9).toInt()
        dialog.window!!.attributes = params as WindowManager.LayoutParams

        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.tvSignUp.setOnClickListener {
            mCallback.keyWordListener(keyword)
            dialog.dismiss()
        }

        dialog.show()
    }
}