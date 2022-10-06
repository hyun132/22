package com.iium.iium_medioz.view.main.bottom.home.calendar

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.ktx.toObjects
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityCalendarBinding
import com.iium.iium_medioz.databinding.ActivityMainBinding
import com.iium.iium_medioz.model.calendar.CalendarFeel
import com.iium.iium_medioz.model.calendar.CalendarModel
import com.iium.iium_medioz.model.calendar.FeelModel
import com.iium.iium_medioz.model.calendar.Jurnal
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.adapter.CalendarAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.feel.ResultState
import com.iium.iium_medioz.util.feel.getDeviceId
import com.iium.iium_medioz.util.firebase.FirebaseService
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.vertical.VerticalSpaceItemDecoration
import com.iium.iium_medioz.view.main.bottom.home.calendar.create.CreateFeelActivity
import com.iium.iium_medioz.view.main.bottom.home.calendar.edit.CalendarEditActivity
import com.iium.iium_medioz.view.main.bottom.home.calendar.search.CalendarSearchActivity
import com.iium.iium_medioz.viewmodel.calendar.CalendarViewModel
import com.iium.iium_medioz.viewmodel.calendar.EditViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var mBinding : ActivityCalendarBinding
    private lateinit var apiServices: APIService
    private val formatter = SimpleDateFormat("yyyy년 MMM dd일, EEE요일")
    private val viewModel by viewModels<CalendarViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_calendar)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        runOnUiThread {
            inStatusBar()
            initView()
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        mBinding.content.notif.setOnClickListener {
            val popup = PopupWindow(this)
            val layout = layoutInflater.inflate(R.layout.notif_popup_layout, null)
            popup.contentView = layout
            popup.height = WindowManager.LayoutParams.WRAP_CONTENT
            popup.width = WindowManager.LayoutParams.WRAP_CONTENT
            popup.isOutsideTouchable = true
            popup.isFocusable = true
            popup.setBackgroundDrawable(null)
            popup.showAsDropDown(mBinding.content.notif)
        }

        val date = Date()
        mBinding.content.tvToday.text = formatter.format(date)
        mBinding.content.currentDate.text = formatter.format(date)
        Log.e("DATE", formatter.format(date))

        mutableListOf(
            mBinding.content.currentDate
        ).forEach {
            it.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(this, this, year, month, day)
                    .show()
            }
        }

        mBinding.bottomNavView.menu.getItem(1).isEnabled = false
        mBinding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.icalendar -> {
                    val c = Calendar.getInstance()
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)

                    DatePickerDialog(this, this, year, month, day)
                        .show()
                }
                R.id.isearch -> {
                    Intent(this, CalendarSearchActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
            true
        }

        mutableListOf(
            mBinding.fab,
            mBinding.content.cardMood,
            mBinding.content.listEmote,
            mBinding.content.llAngry,
            mBinding.content.llSad,
            mBinding.content.llHappy,
            mBinding.content.llThanks,
            mBinding.content.llSurprise
        ).forEach {
            it.setOnClickListener {
                Intent(this, CreateFeelActivity::class.java).apply {
                    putExtra("date", mBinding.content.currentDate.text.toString())
                    startActivity(this)
                }
            }
        }

        mBinding.content.rvJurnal.layoutManager = LinearLayoutManager(this)
        mBinding.content.rvJurnal.addItemDecoration(VerticalSpaceItemDecoration(16))

        mBinding.content.pbhome.isVisible = true

        viewModel.resultStateDeleteJurnal.observe(this) {
            when (it) {
                is ResultState.Error -> {
                    FirebaseCrashlytics.getInstance().recordException(it.e)
                    Log.e("Delete Data", it.e.message.toString())
                }
                is ResultState.Success<*> -> {
                    Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()

                }
                is ResultState.Loading -> {

                }
            }
        }
        fetchData(formatter.format(date))

    }

    private fun fetchData(date: String) {
        FirebaseService.getText(getDeviceId())
            .whereEqualTo("date", date)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()


                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val model = snapshot.toObjects<Jurnal>().toMutableList()
                    val adapter = CalendarAdapter(model)
                    adapter.setClickListener {
                        val jurnal = FeelModel(
                            it.background,
                            it.date,
                            it.feeling,
                            it.fileName,
                            it.msg,
                            it.title,
                            it.jurnalId
                        )
                        Intent(this, CalendarEditActivity::class.java).apply {
                            putExtra("jurnal", jurnal)
                            startActivity(this)
                        }
                    }
                    mBinding.content.rvJurnal.adapter = adapter
                    adapter.setClickDelete(viewModel::deleteJurnal)
                } else {
                    val adapter = CalendarAdapter(mutableListOf())
                    mBinding.content.rvJurnal.adapter = adapter
                }



                mBinding.content.pbhome.isVisible = false
            }
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        mCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        val dateString = formatter.format(mCalendar.time)
        mBinding.content.currentDate.text = dateString
        fetchData(dateString)
    }
}