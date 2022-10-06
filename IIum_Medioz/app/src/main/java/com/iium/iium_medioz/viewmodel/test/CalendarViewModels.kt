package com.iium.iium_medioz.viewmodel.test

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.iium.iium_medioz.api.calendar.CalendarAPI
import com.iium.iium_medioz.api.calendar.WeathyAPI
import com.iium.iium_medioz.di.Api
import com.iium.iium_medioz.model.calendar.weathy.Weathy
import com.iium.iium_medioz.model.test.CalendarPreview
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.calendar.*
import com.iium.iium_medioz.util.extensions.launchCatch
import com.iium.iium_medioz.util.extensions.updateMap
import com.iium.iium_medioz.util.log.debugE
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

typealias YearMonthFormat = String

class CalendarViewModels
@ViewModelInject constructor(
    @Api private val calendarAPI: CalendarAPI, @Api private val weathyAPI: WeathyAPI
) : ViewModel() {

    private val _curDate = MutableLiveData(LocalDate.now())
    val curDate: LiveData<LocalDate> = _curDate

    private val _selectedDate = MutableLiveData(LocalDate.now())
    val selectedDate: LiveData<LocalDate> = _selectedDate

    private val _isPastThanAvailable = MutableLiveData(false)
    val isPastThanAvailable: LiveData<Boolean> = _isPastThanAvailable

    private val _calendarData = MutableLiveData<Map<YearMonthFormat, List<CalendarPreview?>>>(mapOf())
    val calendarData: LiveData<Map<YearMonthFormat, List<CalendarPreview?>>> = _calendarData

    private val _isMoreMenuShowing = MutableLiveData(false)
    val isMoreMenuShowing: LiveData<Boolean> = _isMoreMenuShowing

    private val selectedWeathy = MutableLiveData<Weathy?>(null)

    val todayWeathy = MutableLiveData<Weathy?>(null)
    val curWeathy: LiveData<Weathy?> = selectedWeathy
    val weathyDate = selectedDate.map {
        it.koFormat
    }
    val weathyLocation = curWeathy.map {
        it?.region?.name ?: ""
    }
    val weathyTempHigh = curWeathy.map {
        it?.dailyWeather?.temperature?.maxTemp?.toString()?.plus("°") ?: "0°"
    }
    val weathyTempLow = curWeathy.map {
        it?.dailyWeather?.temperature?.minTemp?.toString()?.plus("°") ?: "0°"
    }
    val weathyStampIcon = curWeathy.map {
        it?.stampId?.iconRes
    }
    val weathyStampRepresentation = curWeathy.map {
        it?.stampId?.representationPast
    }
    val weathyStampTextColor = curWeathy.map {
        it?.stampId?.colorRes
    }
    val weathyTopClothes = curWeathy.map {
        it?.closet?.top?.clothes?.joinToString(" · ") { it.name } ?: ""
    }
    val weathyBottomClothes = curWeathy.map {
        it?.closet?.bottom?.clothes?.joinToString(" · ") { it.name } ?: ""
    }
    val weathyOuterClothes = curWeathy.map {
        it?.closet?.outer?.clothes?.joinToString(" · ") { it.name } ?: ""
    }
    val weathyEtcClothes = curWeathy.map {
        it?.closet?.etc?.clothes?.joinToString(" · ") { it.name } ?: ""
    }
    val weathyFeedback = curWeathy.map {
        it?.feedback?: ""
    }
    val weathyImage = curWeathy.map{
        it?.imgUrl?: ""
    }

    init {
        collectSelectedFlow()
        collectYearMonthFlow()
        observeAppEvents()
    }

    private fun closeMoreMenu() {
        _isMoreMenuShowing.value = false
    }

    private fun openMoreMenu() {
        _isMoreMenuShowing.value = true
    }

    fun onClickMoreMenu() {
        if (_isMoreMenuShowing.value == true) {
            closeMoreMenu()
        } else {
            openMoreMenu()
        }
    }

    fun onClickContainer() {
        if (isMoreMenuShowing.value == true) _isMoreMenuShowing.value = false
    }

    fun onCurDateChanged(date: LocalDate) {
//        debugE("curDate: $date")
        _curDate.value = date
    }

    fun onSelectedDateChanged(date: LocalDate) {
//        debugE("selectedDate $date")
        _selectedDate.value = date
    }

    private fun collectSelectedFlow() {
        viewModelScope.launch {
            selectedDate.asFlow().collect {
                _isPastThanAvailable.value = it.isPast()

                if (!it.isPast() && !it.isFuture()) fetchSelectedDateWeathy()
            }
        }
    }

    private fun collectYearMonthFlow() = viewModelScope.launch {
        curDate.asFlow().map { it.yearMonthFormat }.distinctUntilChanged().collect {
            fetchCurrentMonthDataIfNeeded()
        }
    }

    private fun observeAppEvents() {
        viewModelScope.launch {
            AppEvent.onWeathyUpdated.collect {
                debugE("weathyUpdated")
                launch { fetchCurrentMonthData() }
                launch { fetchLastMonthData() }
                launch { fetchSelectedDateWeathy() }
            }
        }
    }
    //GetWeathy
    private fun fetchSelectedDateWeathy() {
        launchCatch({
            weathyAPI.fetchWeathyWithDate(selectedDate.value!!.dateString)
        }, onSuccess = {
            selectedWeathy.value = it.weathy
            if (LocalDate.of(it.weathy!!.dailyWeather.date.year, it.weathy.dailyWeather.date.month, it.weathy.dailyWeather.date.day) == LocalDate.now()) {
                todayWeathy.value = it.weathy
            }
        }, onFailure = {
            selectedWeathy.value = null
        })
    }

    private fun fetchCurrentMonthDataIfNeeded() {
        val date = curDate.value!!
        if (!calendarData.value!!.containsKey(date.yearMonthFormat)) {
            fetchCurrentMonthData()
            fetchLastMonthData()
        }
    }

    private fun fetchCurrentMonthData() {
        val date = curDate.value!!
        launchCatch({
            val s = getStartDateStringInCalendar(date.year, date.monthValue)
            val e = getEndDateStringInCalendar(date.year, date.monthValue)
            calendarAPI.getFeel(prefs.newaccesstoken)
        }, onSuccess = { (list) ->
            _calendarData.updateMap {
                this[date.yearMonthFormat] = list
            }
        })
    }

    private fun fetchLastMonthData() {
        val date = curDate.value!!
        launchCatch({
            val s = getStartDateStringInCalendar(date.year, date.monthValue-1)
            val e = getEndDateStringInCalendar(date.year, date.monthValue-1)
            calendarAPI.getFeel(prefs.newaccesstoken)
        }, onSuccess = { (list) ->
            _calendarData.updateMap {
                this[date.lastYearMonthFormat] = list
            }
        })
    }

    val onDeleteSuccess = SimpleEventLiveData()
    val onDeleteFailed = SimpleEventLiveData()

    fun delete() {
        closeMoreMenu()

        val weathy = curWeathy.value ?: return

        launchCatch({
            weathyAPI.deleteWeathy(weathy.id)
        }, onSuccess = {
            if (weathy.id == todayWeathy.value?.id)
                todayWeathy.value = null
            onDeleteSuccess.emit()
            AppEvent.onWeathyUpdated.emit()
        }, onFailure = {
            onDeleteFailed.emit()
            debugE(it)
        })
    }

}