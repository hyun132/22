package com.iium.iium_medioz.util.dialog

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.FragmentKeyWordDialogBinding
import com.iium.iium_medioz.util.common.AutoClearedValue
import com.iium.iium_medioz.util.common.setOnDebounceClickListener
import com.iium.iium_medioz.util.extensions.getColor
import com.iium.iium_medioz.util.pixel.PixelRatio
import com.iium.iium_medioz.viewmodel.data.DataViewModel
import kotlin.math.roundToInt

class keyWordDialog : DialogFragment() {

    private var binding by AutoClearedValue<FragmentKeyWordDialogBinding>()
    private val viewModel by activityViewModels<DataViewModel>()

    val pixelRatio: PixelRatio
        get() {
            TODO()
        }

    private val title: String
        get() = arguments?.getString("title") ?: ""
    private val color: Int
        get() = arguments?.getInt("color", getColor(R.color.blue_temp)) ?: getColor(R.color.blue_temp)
    private val clickListener: ClickListener?
        get() = if (parentFragment == null) (activity as? ClickListener) else (parentFragment as? ClickListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showKeyboard()
        binding.enter.setBackgroundResource(R.drawable.edit_border_active)

        binding.title.text = title
        viewModel.numClothes[viewModel.choicedClothesTabIndex.value!!].observe(viewLifecycleOwner) {
            if (it <= 50) {
                binding.tagCount.text = it.toString()
            }
        }
        binding.btnAdd setOnDebounceClickListener {
            binding.enter.text?.toString()?.let { it1 -> clickListener?.onClickYes(it1) }
            binding.enter.setText("")
        }
        binding.btnCancel setOnDebounceClickListener {
            clickListener?.onClickNo()

            hideKeyboard()
            dismiss()
        }
        binding.enter.setOnFocusChangeListener { _, hasFocus ->
            setCountVisibility(hasFocus)
        }
        binding.enter.addTextChangedListener {
            binding.textDeleteBtn.isVisible = !it.isNullOrBlank()
            binding.textDeleteBtn setOnDebounceClickListener {
                binding.enter.setText("")
            }
            if (!it.isNullOrBlank()) {
                if (!binding.btnAdd.isEnabled)
                    setButtonEnabled(true)
                binding.textCount.setTextColor(color)
                binding.textCount.text = it.length.toString()

            } else {
                if (binding.btnAdd.isEnabled)
                    setButtonDisabled(false)
                binding.textCount.setTextColor(getColor(R.color.sub_grey_6))
                binding.textCount.text = "0"
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val width = (pixelRatio.screenShort * 0.88f).coerceAtMost(pixelRatio.toPixel(309).toFloat())
        dialog?.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(0.2f)
            setLayout(width.roundToInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            isCancelable = false
            attributes.height = (resources.displayMetrics.density * 181).roundToInt()
        }
    }

    interface ClickListener {
        fun onClickYes(text: String) {}
        fun onClickNo() {}
    }

    companion object {
        fun newInstance(
            title: String? = null,
            color: Int? = null
        ) = keyWordDialog().apply {
            arguments = bundleOf("title" to title, "color" to color)
        }
    }

    private fun setCountVisibility(hasFocus: Boolean) {
        binding.textCount.isVisible = hasFocus
        binding.textCount2.isVisible = hasFocus
    }

    private fun setButtonEnabled(isEnable: Boolean) {
        val colorChangeActive = AnimatorInflater.loadAnimator(context, R.animator.color_change_active_anim) as AnimatorSet
        colorChangeActive.setTarget(binding.btnAdd)
        colorChangeActive.start()
        binding.btnAdd.isEnabled = isEnable
    }

    private fun setButtonDisabled(isEnable: Boolean) {
        val colorChange = AnimatorInflater.loadAnimator(context, R.animator.color_change_anim) as AnimatorSet
        colorChange.setTarget(binding.btnAdd)
        colorChange.start()
        binding.btnAdd.isEnabled = isEnable
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.enter.windowToken, 0)
    }

    private fun showKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        setKeyboardMode()
    }

    private fun setKeyboardMode() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

}