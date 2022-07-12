package com.iium.iium_medioz.util.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.ItemJurnalBinding
import com.iium.iium_medioz.databinding.NoJurnalLayoutBinding
import com.iium.iium_medioz.model.calendar.CalendarFeel
import com.iium.iium_medioz.model.calendar.Jurnal
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.feel.getColorSave
import com.iium.iium_medioz.util.feel.getFeeling
import com.iium.iium_medioz.util.feel.getFeelingStatus
import com.iium.iium_medioz.view.main.bottom.mypage.notice.NoticeDetailActivity


class CalendarAdapter(private val data: MutableList<Jurnal>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var listener: (Jurnal) -> Unit
    private lateinit var delete: (Jurnal) -> Unit
    private val binderHelper = ViewBinderHelper()
    var isSearchPage = false

    fun setClickListener(listener: (Jurnal) -> Unit) {
        this.listener = listener
    }

    fun setClickDelete(delete: (Jurnal) -> Unit) {
        this.delete = delete
    }

    private fun deleteJurnal(position: Int) {
        this.data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                NoJurnalViewHolder(
                    NoJurnalLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                HomeViewHolder(
                    ItemJurnalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeViewHolder) {
            val item = data[position]
            holder.bind(item)
            holder.v.frontLayout.setOnClickListener {
                listener(item)
            }
            binderHelper.bind(holder.v.swipeLayout, item.title)
            holder.v.deleteLayout.setOnClickListener {
                deleteJurnal(position)
                delete.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = if (data.isEmpty()) 1 else data.size

    override fun getItemViewType(position: Int): Int {
        return if (data.isEmpty()) {
            0
        } else {
            1
        }
    }

    @SuppressLint("SetTextI18n")
    inner class NoJurnalViewHolder(v: NoJurnalLayoutBinding) :
        RecyclerView.ViewHolder(v.root) {
        init {
            if (isSearchPage) {
                v.wordingStatus.text = "데이터가 없습니다!"
            } else {
                v.wordingStatus.text = v.root.context.getString(R.string.no_journal_for_this_day)
            }
        }
    }
}

class HomeViewHolder(val v: ItemJurnalBinding) : RecyclerView.ViewHolder(v.root) {
    fun bind(jurnal: Jurnal) {
        v.bgColor.setBackgroundColor(
            ContextCompat.getColor(
                v.root.context,
                if (jurnal.background == 0) R.color.white else
                    getColorSave(jurnal.background)
            )
        )
        v.ivFeeling.getFeeling(jurnal.feeling)
        v.tvTitle.text = jurnal.title
        v.tvStatusFeeling.getFeelingStatus(jurnal.feeling)
    }
}