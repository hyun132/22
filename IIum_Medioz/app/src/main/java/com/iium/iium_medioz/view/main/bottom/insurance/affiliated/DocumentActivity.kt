package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDocumentBinding
import com.iium.iium_medioz.databinding.ActivityHospitalBinding
import com.iium.iium_medioz.model.document.DocumentModel
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_ADDRESS
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_CALL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_IMGURL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_NAME
import com.iium.iium_medioz.util.`object`.Constant.PAYMENT_ADDRESS
import com.iium.iium_medioz.util.`object`.Constant.PAYMENT_CALL
import com.iium.iium_medioz.util.`object`.Constant.PAYMENT_NAME
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*

class DocumentActivity : BaseActivity() {

    private lateinit var mBinding : ActivityDocumentBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_document)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        runOnUiThread {
            initView()
            inStatusBar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveHospital()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        val name = intent.getStringExtra(PAYMENT_NAME)
        val address = intent.getStringExtra(PAYMENT_ADDRESS)
        val call = intent.getStringExtra(PAYMENT_CALL)

        Log.d(TAG,"병원 이름 -> $name")
        Log.d(TAG,"병원 주소 -> $address")
        Log.d(TAG,"병원 번호 -> $call")


        mBinding.tvDoName.text = name.toString()
        mBinding.tvDoAddress.text = address.toString()
        mBinding.tvDoCall.text = call.toString()
    }

    private fun initAPI() {
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvDoTimestamp.text = onlyDate.toString()

        val doname = mBinding.tvDoName.text.toString()
        val address = mBinding.tvDoAddress.text.toString()
        val address_city = "대전"
        val address_district = "서구"
        val address_location = "월평동"
        val call = mBinding.tvDoCall.text.toString()
        val username = mBinding.etDoName.text.toString()
        val usernumber = "${mBinding.etDoNumberFirst.text}-${mBinding.etDoNumberSecond.text}"
        val usercall = mBinding.etDoCall.text.toString()
        val userreqdocument = ""
        val imgUrl = intent.getStringExtra(DOCUMENT_IMGURL)
        val inquiry_first = mBinding.tvCalendarFirst.text.toString()
        val inquiry_second = mBinding.tvCalendarSecond.text.toString()
        val inquiry_document = ""
        val timestamp = mBinding.tvDoTimestamp.text.toString()

        val data = DocumentModel(doname, address,address_city, address_district, address_location,call,username,usernumber,usercall, userreqdocument,imgUrl,inquiry_first,inquiry_second, inquiry_document,timestamp)

        LLog.e("제휴병원 서류신청")
        apiServices.postDocument(prefs.newaccesstoken,data).enqueue(object :
            Callback<DocumentModel> {
            override fun onResponse(call: Call<DocumentModel>, response: Response<DocumentModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"postDocument  API SUCCESS -> $result")
                    moveSave()
                }
                else {
                    Log.d(LLog.TAG,"postDocument  API ERROR -> ${response.errorBody()}")
                    ErrorDialog()
                }
            }
            override fun onFailure(call: Call<DocumentModel>, t: Throwable) {
                Log.d(LLog.TAG,"postDocument  Fail -> $t")
                ErrorDialog()
            }
        })
    }

    fun onBackPressed(v: View) {
        moveHospital()
    }

    fun onDocumentClick(v: View) {
        initAPI()
    }

    fun onCalendarFirst(v: View) {
        var dateString = ""
        val cal = Calendar.getInstance() //캘린더 뷰
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}. ${month+1}. $dayOfMonth"
            mBinding.tvCalendarFirst.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
            Calendar.DAY_OF_MONTH)).show()

    }

    fun onCalendarSecond(v: View) {
        var dateString = ""
        val cal = Calendar.getInstance() //캘린더 뷰
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}. ${month+1}. $dayOfMonth"
            mBinding.tvCalendarSecond.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
            Calendar.DAY_OF_MONTH)).show()
    }

    fun onSendDocument(v: View) {
        val intent = Intent(this,SendDocumentActivity::class.java)
        startActivity(intent)
    }
}