package com.iium.iium_medioz.util.calendar.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.model.test.CalendarPreview
import com.iium.iium_medioz.util.calendar.WeeklyView
import com.iium.iium_medioz.util.calendar.convertWeeklyIndexToFirstDateOfWeekCalendar
import com.iium.iium_medioz.util.calendar.weekOfMonth
import com.iium.iium_medioz.util.calendar.yearMonthFormat
import com.iium.iium_medioz.viewmodel.test.YearMonthFormat
import java.time.LocalDate

class WeeklyAdapter(
    private val animLiveData: LiveData<Float>,
    private val data: LiveData<Map<YearMonthFormat, List<CalendarPreview?>>>,
    private val lifecycleOwner: LifecycleOwner,
    private val onClickDateListener: (date: LocalDate) -> Unit,
) : RecyclerView.Adapter<WeeklyAdapter.WeeklyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyHolder {
        return WeeklyHolder(WeeklyView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun getItemCount(): Int {
        return MAX_ITEM_COUNT
    }

    override fun onBindViewHolder(holder: WeeklyHolder, position: Int) {
        holder.bind(position)
    }

    inner class WeeklyHolder(private val view: WeeklyView) : RecyclerView.ViewHolder(view) {
        init {
            animLiveData.observe(lifecycleOwner) {
                view.animValue = it
            }
            data.observe(lifecycleOwner) {
                val weekOfMonth = view.firstDateInCalendar.weekOfMonth - 1
                view.data =
                    it[view.firstDateInCalendar.yearMonthFormat]?.subList(weekOfMonth * 7, (weekOfMonth + 1) * 7)
            }
            view.onClickDateListener = onClickDateListener
        }

        fun bind(position: Int) {
            val date = convertWeeklyIndexToFirstDateOfWeekCalendar(position)
            val weekOfMonth = date.weekOfMonth - 1
            view.firstDateInCalendar = date
            view.data = data.value?.get(date.yearMonthFormat)?.subList(weekOfMonth * 7, (weekOfMonth + 1) * 7)
        }
    }

    companion object {
        const val MAX_ITEM_COUNT = Int.MAX_VALUE
    }
}