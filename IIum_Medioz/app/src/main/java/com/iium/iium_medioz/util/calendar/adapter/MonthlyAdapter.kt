package com.iium.iium_medioz.util.calendar.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.model.feel.CalendarPreview
import com.iium.iium_medioz.util.calendar.MonthlyView
import com.iium.iium_medioz.util.calendar.SimpleEventLiveData
import com.iium.iium_medioz.util.calendar.convertMonthlyIndexToDateToFirstDateOfMonthCalendar
import com.iium.iium_medioz.util.calendar.yearMonthFormat
import com.iium.iium_medioz.viewmodel.calendar.YearMonthFormat
import java.time.LocalDate

class MonthlyAdapter(
    private val animLiveData: LiveData<Float>,
    private val scrollEnabled: LiveData<Boolean>,
    private val onScrollToTop: SimpleEventLiveData,
    private val data: LiveData<Map<YearMonthFormat, List<CalendarPreview?>>>,
    private val selectedDate: LiveData<LocalDate>,
    private val lifecycleOwner: LifecycleOwner,
    private val onClickDateListener: (date: LocalDate) -> Unit,
) : RecyclerView.Adapter<MonthlyAdapter.MonthlyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyHolder {
        return MonthlyHolder(MonthlyView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun getItemCount(): Int {
        return MAX_ITEM_COUNT
    }

    override fun onBindViewHolder(holder: MonthlyHolder, position: Int) {
        holder.bind(position)
    }

    inner class MonthlyHolder(private val view: MonthlyView) : RecyclerView.ViewHolder(view) {
        init {
            animLiveData.observe(lifecycleOwner) {
                view.animValue = it
            }
            scrollEnabled.observe(lifecycleOwner) {
                view.scrollEnabled = it
            }
            onScrollToTop.observe(lifecycleOwner) {
                view.scrollToTop()
            }
            data.observe(lifecycleOwner) {
                view.data = it[view.firstDateInMonth.yearMonthFormat]
            }
            selectedDate.observe(lifecycleOwner) {
                view.selectedDate = it
            }
            view.onClickDateListener = onClickDateListener
        }

        fun bind(position: Int) {
            val (firstDateOfMonthInCalendar, firstDateOfMonth) = convertMonthlyIndexToDateToFirstDateOfMonthCalendar(
                position
            )

            view.firstDatesInCalednarAndMonth = firstDateOfMonthInCalendar to firstDateOfMonth

            view.data = data.value?.get(firstDateOfMonth.yearMonthFormat)
        }
    }

    companion object {
        const val MAX_ITEM_COUNT = Int.MAX_VALUE
    }
}