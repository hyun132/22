package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

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
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_ADDRESS
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_CALL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_IMGURL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_NAME
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class DocumentActivity : BaseActivity() {

    private lateinit var mBinding : ActivityDocumentBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_document)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        inStatusBar()
        runOnUiThread {
            initView()
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        val name = intent.getStringExtra(DOCUMENT_NAME)
        val address = intent.getStringExtra(DOCUMENT_ADDRESS)
        val call = intent.getStringExtra(DOCUMENT_CALL)

        mBinding.tvDoName.text = name.toString()
        mBinding.tvDoAddress.text = address.toString()
        mBinding.tvDoCall.text = call.toString()
    }


    fun onBackPressed(v: View) {
        moveHospital()
    }

    fun onDocumentClick(v: View) {
        initAPI()
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
        val userreqdocument = mBinding.etDo.text.toString()
        val imgUrl = intent.getStringExtra(DOCUMENT_IMGURL)
        val timestamp = mBinding.tvDoTimestamp.text.toString()

        val data = DocumentModel(doname, address,address_city, address_district, address_location,call,username,usernumber,usercall, userreqdocument,imgUrl, timestamp)

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

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveHospital()
    }
}