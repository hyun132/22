package com.iium.iium_medioz.view.main.bottom.home.calendar.edit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityCalendarEditBinding
import com.iium.iium_medioz.databinding.ActivityMainBinding
import com.iium.iium_medioz.model.calendar.CalendarFeel
import com.iium.iium_medioz.model.calendar.CalendarModel
import com.iium.iium_medioz.model.calendar.FeelModel
import com.iium.iium_medioz.model.calendar.SendFeelModel
import com.iium.iium_medioz.model.document.DocumentModel
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.feel.ResultState
import com.iium.iium_medioz.util.feel.getColorSave
import com.iium.iium_medioz.util.feel.getDeviceId
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.bottom.home.calendar.create.select
import com.iium.iium_medioz.view.main.bottom.home.calendar.create.unselect
import com.iium.iium_medioz.viewmodel.calendar.EditViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CalendarEditActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var mBinding : ActivityCalendarEditBinding
    private lateinit var apiServices: APIService
    private var bgcolor = 0
    private var isEdit = false
    private var myJurnal: FeelModel? = null
    private val viewModel by viewModels<EditViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_calendar_edit)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        val title = intent.getStringExtra("title") ?: ""
        val myDate = intent.getStringExtra("date") ?: ""
        var feeling = intent.getIntExtra("feeling", 0)

        mBinding.etMsg.requestFocus()
        val emotes = listOf(
            mBinding.llEditAngry,
            mBinding.llEditSad,
            mBinding.llEditHappy,
            mBinding.llEditThanks,
            mBinding.llEditSurprise
        )

        emotes.forEachIndexed { index, lyt ->
            lyt.setOnClickListener {
                emotes.forEach { view -> view.unselect() }
                lyt.select()
                feeling = index
            }
        }

        if (intent.hasExtra("jurnal")) {
            isEdit = true
            myJurnal = intent.getParcelableExtra("jurnal") ?: FeelModel()
            Log.e("MyJurnal", myJurnal.toString())

            mBinding.tvTitle.setText(myJurnal?.title)
            mBinding.etMsg.setText(myJurnal?.msg)
            feeling = myJurnal?.feeling ?: 1
            emotes[feeling].select()
            mBinding.date.text = myJurnal?.date
            bgcolor = myJurnal?.background ?: 0
            mBinding.bg.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    getColorSave(bgcolor)
                )
            )
        } else {
            isEdit = false
            mBinding.date.text = myDate
            emotes[feeling].select()
            mBinding.tvTitle.setText(title)
        }

        mBinding.date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, this, year, month, day)
                .show()
        }

        mBinding.bgColor.setOnClickListener {
            val colorPickerBottomSheet = EditColorSheet()
            colorPickerBottomSheet.show(supportFragmentManager, "")
            colorPickerBottomSheet.setClickListener { color ->
                bgcolor = color
                mBinding.bg.setBackgroundColor(ContextCompat.getColor(this, getColorSave(bgcolor)))
            }
        }

        viewModel.resultStatusSaveJurnal.observe(this) {
            when (it) {
                is ResultState.Success<*> -> {
                    finish()
                }
                is ResultState.Error -> {
                    Toast.makeText(this, "${it.e.message}", Toast.LENGTH_SHORT).show()
                }
                is ResultState.Loading -> {
                    mBinding.progressbar.isVisible = it.loading
                }
            }
        }
    }

    fun onCancleClick(v: View) {
        finish()
    }

    fun onSaveClick(v: View) {
        val android_id = getDeviceId()
        val feeling = intent.getIntExtra("feeling", 0)
        val title = intent.getStringExtra("title") ?: ""
        val myDate = intent.getStringExtra("date") ?: ""
        val jurnalId = myJurnal?.jurnalId ?: UUID.randomUUID().toString()
        val bgcolor = bgcolor
        val deviceId = android_id

        val calendarfeel = CalendarFeel(title,feeling,myDate,bgcolor,jurnalId,deviceId)

        LLog.e("병상일지 API")
        apiServices.postFeel(prefs.newaccesstoken,calendarfeel).enqueue(object : Callback<CalendarModel> {
            override fun onResponse(call: Call<CalendarModel>, response: Response<CalendarModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"병상일지 SUCCESS -> $result")
                    initFireBase()
                    modeCalendar()
                }
                else {
                    Log.d(TAG,"병상일지 ERROR -> ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<CalendarModel>, t: Throwable) {
                Log.d(TAG,"병상일지 API FAIL -> $t")
            }
        })
    }

    private fun initFireBase() {
        val android_id = getDeviceId()
        val feeling = intent.getIntExtra("feeling", 0)
        if (isEdit) {
            val jurnal = SendFeelModel(
                android_id,
                HashMap<String, Any>().apply {
                    put("fileName", myJurnal?.fileName ?: mutableListOf<String>())
                    put("msg", mBinding.etMsg.text.toString())
                    put("title", mBinding.tvTitle.text.toString())
                    put("feeling", feeling)
                    put("date", mBinding.date.text.toString())
                    put("background", bgcolor)
                    put("jurnalId", myJurnal?.jurnalId ?: UUID.randomUUID().toString())
                    put("deviceId", android_id)
                }
            )
            viewModel.editJurnal(jurnal)
        } else {
            val jurnal = SendFeelModel(
                android_id,
                HashMap<String, Any>().apply {
                    put("msg", mBinding.etMsg.text.toString())
                    put("title", mBinding.tvTitle.text.toString())
                    put("feeling", feeling)
                    put("date", mBinding.date.text.toString())
                    put("background", bgcolor)
                    put("jurnalId", UUID.randomUUID().toString())
                    put("deviceId", android_id)
                }
            )
            viewModel.saveJurnal(jurnal)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        mCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        val formatter = SimpleDateFormat("yyyy년 MMM dd일, EEE요일")
        mBinding.date.text = formatter.format(mCalendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}