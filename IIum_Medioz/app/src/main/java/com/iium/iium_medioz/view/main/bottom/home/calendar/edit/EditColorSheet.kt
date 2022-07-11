package com.iium.iium_medioz.view.main.bottom.home.calendar.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentEditColorSheetBinding
import com.iium.iium_medioz.databinding.FragmentTitleBinding
import com.iium.iium_medioz.databinding.ItemBgLayoutBinding
import com.iium.iium_medioz.util.feel.dp
import com.iium.iium_medioz.util.feel.hide
import com.iium.iium_medioz.util.feel.show
import com.iium.iium_medioz.util.horizon.HorizontalSpaceItemDecoration

class EditColorSheet : BottomSheetDialogFragment() {

    private lateinit var mBinding : FragmentEditColorSheetBinding
    private lateinit var onClickColor: (Int) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_color_sheet, container, false)
        mBinding.fragment = this
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    fun setClickListener(onClickColor: (Int) -> Unit) {
        this.onClickColor = onClickColor
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colors = mutableListOf(
            R.drawable.bg_item,
            R.drawable.bg_item_biru,
            R.drawable.bg_item_kuning,
            R.drawable.bg_item_pink
        )
        val adapter = ColorsAdapter(colors)
        mBinding.rvBg.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvBg.addItemDecoration(HorizontalSpaceItemDecoration(16.dp))
        mBinding.rvBg.adapter = adapter

        mBinding.btnClose.setOnClickListener {
            dismiss()
        }

        mBinding.btnCheck.setOnClickListener {
            dismiss()
        }
    }

    inner class ColorsAdapter(private val data: MutableList<Int>) :
        RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder =
            ColorsViewHolder(
                ItemBgLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

        override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
            holder.onBind(data[position])
            if (position == 0) {
                holder.v.tvDefault.show()
            } else {
                holder.v.tvDefault.hide()
            }
            holder.itemView.setOnClickListener {
                onClickColor(position)
            }
        }

        override fun getItemCount(): Int = data.size

        inner class ColorsViewHolder(val v: ItemBgLayoutBinding) : RecyclerView.ViewHolder(v.root) {
            fun onBind(color: Int) {
                v.bgColor.setBackgroundResource(color)
            }
        }
    }
}