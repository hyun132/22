package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityHospitalInFoBinding
import com.iium.iium_medioz.util.`object`.Constant.DETAIL_WORDS
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_ADDRESS
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_CALL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_IMGURL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_NAME
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_WEBSITE
import com.iium.iium_medioz.util.`object`.Constant.FRIDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.FRIDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.FRIDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.MONDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.MONDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.MONDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.PAYMENT_ADDRESS
import com.iium.iium_medioz.util.`object`.Constant.PAYMENT_CALL
import com.iium.iium_medioz.util.`object`.Constant.PAYMENT_NAME
import com.iium.iium_medioz.util.`object`.Constant.SATURDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.SATURDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.SATURDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.SUNDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.SUNDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.SUNDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.`object`.Constant.THURSDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.THURSDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.THURSDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.TUESDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.TUESDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.TUESDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.WEDNESDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.WEDNESDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.WEDNESDAY_TIME_START
import com.iium.iium_medioz.util.base.BaseActivity
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*


class HospitalInFoActivity : BaseActivity() {

    private lateinit var mBinding : ActivityHospitalInFoBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hospital_in_fo)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        runOnUiThread {
            initView()
            inStatusBar()
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }

    private fun initView() {
        val name = intent.getStringExtra(DOCUMENT_NAME)
        val address = intent.getStringExtra(DOCUMENT_ADDRESS)
        val call = intent.getStringExtra(DOCUMENT_CALL)
        val image = intent.getStringExtra(DOCUMENT_IMGURL)
        val web = intent.getStringExtra(DOCUMENT_WEBSITE)
        val image_banner = mBinding.ivBannerImage
        val detail_words = intent.getStringExtra(DETAIL_WORDS)

        mBinding.tvInfoPlaceName.text = name
        mBinding.tvInfoAddressName.text = address
        mBinding.tvInfoCall.text = call
        mBinding.tvInfoWeb.text = web
        Glide.with(image_banner.context)
            .load(image)
            .transform(CenterCrop())
            .into(image_banner)
        mBinding.tvDetailWords.text = detail_words

        val call_text =  mBinding.tvInfoCall.text.toString()
        val tel = "tel:${call_text}"

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(tel))
                startActivity(intent)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@HospitalInFoActivity,"전화 연결 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        mBinding.tvInfoCall.setOnClickListener {
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("[설정] 에서 권한을 열어줘야 전화 연결이 가능합니다.")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check()
        }

        mBinding.tvInfoWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(web))
            startActivity(intent)
        }

        initSunday()
        initMonday()
        initTuesday()
        initWednesday()
        initThursday()
        initFriday()
        initSaturday()
        initdate()
    }

    private fun initSunday() {
        val sunday_time_start = intent.getStringExtra(SUNDAY_TIME_START)
        val sunday_time_end = intent.getStringExtra(SUNDAY_TIME_END)
        val sunday_day_off = intent.getStringExtra(SUNDAY_DAY_OFF)

        val start_ab = sunday_time_start?.substring(2)
        val start_abc = start_ab?.substring(0, start_ab.length - 2)

        val end_ab = sunday_time_end?.substring(2)
        val end_abc = end_ab?.substring(0, end_ab.length - 2)

        val day_ab = sunday_day_off?.substring(2)
        val day_abc = day_ab?.substring(0, day_ab.length - 2)

        if(day_abc?.equals("true") == true) {
            mBinding.tvSundayStart.text = "휴무"
            mBinding.tvSundayStart.setTextColor(ContextCompat.getColor(this,R.color.red_temp))
        } else {
            mBinding.tvSundayStart.text = "$start_abc ~ $end_abc"

        }
    }

    private fun initMonday() {
        val monday_time_start = intent.getStringExtra(MONDAY_TIME_START)
        val monday_time_end = intent.getStringExtra(MONDAY_TIME_END)
        val monday_day_off = intent.getStringExtra(MONDAY_DAY_OFF)

        val start_ab = monday_time_start?.substring(2)
        val start_abc = start_ab?.substring(0, start_ab.length - 2)

        val end_ab = monday_time_end?.substring(2)
        val end_abc = end_ab?.substring(0, end_ab.length - 2)

        val day_ab = monday_day_off?.substring(2)
        val day_abc = day_ab?.substring(0, day_ab.length - 2)

        if(day_abc?.equals("true") == true) {
            mBinding.tvMondayStart.text = "휴무"
            mBinding.tvMondayStart.setTextColor(ContextCompat.getColor(this,R.color.red_temp))
        } else {
            mBinding.tvMondayStart.text = "$start_abc ~ $end_abc"

        }
    }

    private fun initTuesday() {
        val tuesday_time_start = intent.getStringExtra(TUESDAY_TIME_START)
        val tuesday_time_end = intent.getStringExtra(TUESDAY_TIME_END)
        val tuesday_day_off = intent.getStringExtra(TUESDAY_DAY_OFF)

        val start_ab = tuesday_time_start?.substring(2)
        val start_abc = start_ab?.substring(0, start_ab.length - 2)

        val end_ab = tuesday_time_end?.substring(2)
        val end_abc = end_ab?.substring(0, end_ab.length - 2)

        val day_ab = tuesday_day_off?.substring(2)
        val day_abc = day_ab?.substring(0, day_ab.length - 2)

        if(day_abc?.equals("true") == true) {
            mBinding.tvTuesdayStart.text = "휴무"
            mBinding.tvTuesdayStart.setTextColor(ContextCompat.getColor(this,R.color.red_temp))

        } else {
            mBinding.tvTuesdayStart.text = "$start_abc ~ $end_abc"

        }
    }

    private fun initWednesday() {
        val wednesday_time_start = intent.getStringExtra(WEDNESDAY_TIME_START)
        val wednesday_time_end = intent.getStringExtra(WEDNESDAY_TIME_END)
        val wednesday_day_off = intent.getStringExtra(WEDNESDAY_DAY_OFF)

        val start_ab = wednesday_time_start?.substring(2)
        val start_abc = start_ab?.substring(0, start_ab.length - 2)

        val end_ab = wednesday_time_end?.substring(2)
        val end_abc = end_ab?.substring(0, end_ab.length - 2)

        val day_ab = wednesday_day_off?.substring(2)
        val day_abc = day_ab?.substring(0, day_ab.length - 2)

        if(day_abc?.equals("true") == true) {
            mBinding.tvWednesdayStart.text = "휴무"
            mBinding.tvWednesdayStart.setTextColor(ContextCompat.getColor(this,R.color.red_temp))


        } else {
            mBinding.tvWednesdayStart.text = "$start_abc ~ $end_abc"

        }
    }

    private fun initThursday() {
        val thursday_time_start = intent.getStringExtra(THURSDAY_TIME_START)
        val thursday_time_end = intent.getStringExtra(THURSDAY_TIME_END)
        val thursday_day_off = intent.getStringExtra(THURSDAY_DAY_OFF)

        val start_ab = thursday_time_start?.substring(2)
        val start_abc = start_ab?.substring(0, start_ab.length - 2)

        val end_ab = thursday_time_end?.substring(2)
        val end_abc = end_ab?.substring(0, end_ab.length - 2)

        val day_ab = thursday_day_off?.substring(2)
        val day_abc = day_ab?.substring(0, day_ab.length - 2)

        if(day_abc?.equals("true") == true) {
            mBinding.tvThursdayStart.text = "휴무"
            mBinding.tvThursdayStart.setTextColor(ContextCompat.getColor(this,R.color.red_temp))

        } else {
            mBinding.tvThursdayStart.text = "$start_abc ~ $end_abc"

        }
    }

    private fun initFriday() {
        val friday_time_start = intent.getStringExtra(FRIDAY_TIME_START)
        val friday_time_end = intent.getStringExtra(FRIDAY_TIME_END)
        val friday_day_off = intent.getStringExtra(FRIDAY_DAY_OFF)

        val start_ab = friday_time_start?.substring(2)
        val start_abc = start_ab?.substring(0, start_ab.length - 2)

        val end_ab = friday_time_end?.substring(2)
        val end_abc = end_ab?.substring(0, end_ab.length - 2)

        val day_ab = friday_day_off?.substring(2)
        val day_abc = day_ab?.substring(0, day_ab.length - 2)

        if(day_abc?.equals("true") == true) {
            mBinding.tvFridayStart.text = "휴무"
            mBinding.tvFridayStart.setTextColor(ContextCompat.getColor(this,R.color.red_temp))


        } else {
            mBinding.tvFridayStart.text = "$start_abc ~ $end_abc"

        }
    }

    private fun initSaturday() {
        val saturday_time_start = intent.getStringExtra(SATURDAY_TIME_START)
        val saturday_time_end = intent.getStringExtra(SATURDAY_TIME_END)
        val saturday_day_off = intent.getStringExtra(SATURDAY_DAY_OFF)

        val start_ab = saturday_time_start?.substring(2)
        val start_abc = start_ab?.substring(0, start_ab.length - 2)

        val end_ab = saturday_time_end?.substring(2)
        val end_abc = end_ab?.substring(0, end_ab.length - 2)

        val day_ab = saturday_day_off?.substring(2)
        val day_abc = day_ab?.substring(0, day_ab.length - 2)

        if(day_abc?.equals("true") == true) {
            mBinding.tvSaturdayStart.text = "휴무"
            mBinding.tvSaturdayStart.setTextColor(ContextCompat.getColor(this,R.color.red_temp))

        } else {
            mBinding.tvSaturdayStart.text = "$start_abc ~ $end_abc"

        }
    }

    private fun initdate() {
        val today : String? = initData()

        val sunday = mBinding.tvSunday.text.toString()
        val monday = mBinding.tvMonday.text.toString()
        val tuesday = mBinding.tvTuesday.text.toString()
        val wednesday = mBinding.tvWednesday.text.toString()
        val thursday = mBinding.tvThursday.text.toString()
        val friday = mBinding.tvFriday.text.toString()
        val saturday = mBinding.tvSaturday.text.toString()

        if(sunday == today) {
            mBinding.tvSunday.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
            mBinding.tvSundayStart.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
        } else if(monday == today) {
            mBinding.tvMonday.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
            mBinding.tvMondayStart.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
        } else if (tuesday == today) {
            mBinding.tvTuesday.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
            mBinding.tvTuesdayStart.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
        } else if(wednesday == today) {
            mBinding.tvWednesday.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
            mBinding.tvWednesdayStart.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
        } else if(thursday == today) {
            mBinding.tvThursday.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
            mBinding.tvThursdayStart.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
        } else if(friday == today) {
            mBinding.tvFriday.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
            mBinding.tvFridayStart.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
        } else if(saturday == today) {
            mBinding.tvSaturday.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
            mBinding.tvSaturdayStart.setTextColor(ContextCompat.getColor(this,R.color.weekend_time))
        }

    }

    private fun initData(): String? {
        val cal: Calendar = Calendar.getInstance()
        var strWeek: String? = null

        when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> {
                strWeek = "일"
            }
            2 -> {
                strWeek = "월"
            }
            3 -> {
                strWeek = "화"
            }
            4 -> {
                strWeek = "수"
            }
            5 -> {
                strWeek = "목"
            }
            6 -> {
                strWeek = "금"
            }
            7 -> {
                strWeek = "토"
            }
        }
        return strWeek
    }

    fun onSendClick(v: View) {
        val intent = Intent(this, DocumentActivity::class.java)

        val name = mBinding.tvInfoPlaceName.text.toString()
        val address = mBinding.tvInfoAddressName.text.toString()
        val call = mBinding.tvInfoCall.text.toString()

        intent.putExtra(PAYMENT_NAME,name)
        intent.putExtra(PAYMENT_ADDRESS, address)
        intent.putExtra(PAYMENT_CALL, call)
        startActivity(intent)
    }

}