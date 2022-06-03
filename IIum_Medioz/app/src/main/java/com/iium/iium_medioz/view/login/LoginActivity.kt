package com.iium.iium_medioz.view.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityLoginBinding
import com.iium.iium_medioz.model.rest.login.LoginSMS
import com.iium.iium_medioz.model.rest.login.LoginSend
import com.iium.iium_medioz.util.`object`.Constant.LOGIN_PHONE
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.encrypt.AESAdapter
import com.iium.iium_medioz.util.encrypt.Base64Util
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.view.intro.permission.PermissionManager
import com.iium.iium_medioz.view.login.sign.SignUpActivity
import com.iium.iium_medioz.view.term.*
import com.iium.iium_medioz.viewmodel.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var apiServices: APIService
    private var mViewModel : LoginViewModel? = null

    private val PROCESS_PHONENUM = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel
        mViewModel = LoginViewModel(prefs)

        mBinding.tvTitle.text = Html.fromHtml(getString(R.string.login_desc1))
        mBinding.tvTitleSub.text = getText(R.string.login_subtitle1)

        inStatusBar()
        initView()
        mViewModel!!.setAuthNum("1234")
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.white)
    }

    private fun initView() {
        initTextBtn()
        callresult()
        if (PermissionManager.getPermissionGranted(this, Manifest.permission.READ_PHONE_STATE)) {
            val manager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_NUMBERS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            @SuppressLint("HardwareIds") var telNo = manager.line1Number
            if (telNo != null) {
                telNo = telNo.replace("+82", "0")
                mViewModel?.setPhoneNum(telNo)
            }
        }

        if (comm.defaultPhoneNumber != null && comm.defaultPhoneNumber!!.isNotEmpty()) {
            val encResult: String =
                comm.defaultPhoneNumber!!.substring(comm.defaultPhoneNumber!!.length - 2)
            // 암호화
            val resultText: String? = when {
                encResult.contains("==") -> {
                    AESAdapter.decAES(prefs.getEncIv()!!, prefs.getEncKey()!!, comm.defaultPhoneNumber)
                } // Base64
                encResult.contains("=") -> {
                    Base64Util.decode(comm.defaultPhoneNumber)
                } // plane text
                else -> {
                    comm.defaultPhoneNumber
                }
            }
            resultText?.let { mViewModel?.setPhoneNum(it) }
        }
    }

    private fun callresult() {
        ResultView =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            }
    }

    private fun initTextBtn() {
        val phoneAgainSpan = SpannableString(mBinding.tvPhonenumInputAgain.text)
        var start = 0
        var end = mBinding.tvPhonenumInputAgain.length()

        val phoneClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                //super.updateDrawState(ds);
                ds.isUnderlineText = false
            }

            override fun onClick(widget: View) {
                phoneFirst(true)
            }
        }

        phoneAgainSpan.setSpan(
            phoneClickableSpan,
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        phoneAgainSpan.setSpan(
            ForegroundColorSpan(getColor(R.color.gray2)),
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        mBinding.tvPhonenumInputAgain.text = phoneAgainSpan
        mBinding.tvPhonenumInputAgain.isClickable = true
        mBinding.tvPhonenumInputAgain.movementMethod = LinkMovementMethod.getInstance()

        val authNumAgainSpan = SpannableString(mBinding.tvAuthnumRequestAgain.text)
        start = 0
        end = mBinding.tvAuthnumRequestAgain.length()

        val authClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }

            override fun onClick(widget: View) {
            }
        }

        authNumAgainSpan.setSpan(
            authClickableSpan,
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        authNumAgainSpan.setSpan(
            ForegroundColorSpan(getColor(R.color.gray2)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        mBinding.tvAuthnumRequestAgain.text = authNumAgainSpan
        mBinding.tvAuthnumRequestAgain.isClickable = true
        mBinding.tvAuthnumRequestAgain.movementMethod = LinkMovementMethod.getInstance()

    }

    fun onClearPhoneClick(v: View?) {
        mViewModel?.setPhoneNum("")
        mBinding.etPhonenum.text = null
    }

    fun onClearAuthClick(v: View?) {
        mViewModel?.setAuthNum("")
        authrequest()
    }

    fun onReSendClick(v: View?) {

    }

    fun onServiceClick(v: View) {
        val dialog = FirstDialog()
        dialog.show(supportFragmentManager,"CustomDialog")
    }

    fun onGetClick(V: View) {
        val dialog = SecondDialog()
        dialog.show(supportFragmentManager,"CustomDialog")

    }

    fun onMedicalClick(v: View) {
        val dialog = ThridDialog()
        dialog.show(supportFragmentManager,"CustomDialog")

    }

    fun onThirdClick(v: View) {
        val dialog = FourDialog()
        dialog.show(supportFragmentManager,"CustomDialog")

    }

    fun onMarkertClick(v: View) {
        val dialog = FiveDialog()
        dialog.show(supportFragmentManager,"CustomDialog")
    }

    fun onOkClick(v: View?) {
        if(mBinding.btnAgreeService.isChecked) {
            if(mBinding.btnAgreeGet.isChecked) {
                if(mBinding.btnAgreeMedical.isChecked) {
                    signup()
                }
                else {
                    termcheck()
                }
            }
            else {
                termcheck()
            }
        }
        else {
            termcheck()
        }
    }

    private fun signup() {
        mBinding.btnAgree.isSelected =! mBinding.btnAgree.isSelected
        val phone = mBinding.etPhonenum.text.toString()

        val intent = Intent(this, SignUpActivity::class.java)
        intent.putExtra(LOGIN_PHONE, phone)
        startActivity(intent)
    }

    fun onSendClick(v: View?) {
        val et_phone = mBinding.etPhonenum.text.toString()
        if (et_phone.length<10) {
            phonecheck()
        }
        else {
            requestAuthSms()
            phoneSecond()
        }
    }

    ///////////////////////// 상태 체크 /////////////////////////
    private fun phoneFirst(resetPhoneNum: Boolean) {
        mViewModel?.setLoginProcess(PROCESS_PHONENUM)
        mViewModel?.setAuthNum("")
        if (resetPhoneNum) {
            mViewModel?.setPhoneNum("")
        }
        if (mBinding.tvTitleSub.visibility == View.GONE) {
            mBinding.tvTitleSub.visibility = View.VISIBLE
        }
        mBinding.tvTitle.text = getString(R.string.login_desc1)
        mBinding.tvTitleSub.text = getText(R.string.login_subtitle1)
        mBinding.btnOk.text = getString(R.string.login_btn_sms_send)
    }

    private fun phoneSecond() {
        if(mBinding.clAuthNumParent.visibility==View.GONE) {
            mBinding.clAuthNumParent.visibility=View.VISIBLE
            mBinding.tvPhonenumAuth.text = mBinding.etPhonenum.text.toString()
        }
        mBinding.tvTitle.text = getString(R.string.login_desc2)
        mBinding.tvTitleSub.text = getText(R.string.login_subtitle2)
        if (mBinding.tvTitleSub.visibility == View.GONE) {
            mBinding.tvTitleSub.visibility = View.VISIBLE
        }
        if(mBinding.btnOk2.visibility == View.GONE) {
            mBinding.btnOk2.visibility = View.GONE
        }
        if(mBinding.clOk2.visibility == View.GONE) {
            mBinding.clOk2.visibility = View.VISIBLE
        }
        mBinding.clOk2.visibility = View.VISIBLE
        mBinding.btnOk.text = getString(R.string.ok)
        mBinding.etAuthnum.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    private fun phoneThrid() {
        if (mBinding.clCheck.visibility == View.GONE) {
            mBinding.clCheck.visibility = View.VISIBLE
            mBinding.clOk.visibility =View.GONE
            checkButton()
        }
    }

    private fun checkButton() {
        mBinding.btnAgree.setOnClickListener {
            onCheckChanged(mBinding.btnAgree)
        }
        mBinding.btnAgreeService.setOnClickListener {
            onCheckChanged(mBinding.btnAgreeService)
        }
        mBinding.btnAgreeGet.setOnClickListener {
            onCheckChanged(mBinding.btnAgreeGet)
        }
        mBinding.btnAgreeMedical.setOnClickListener {
            onCheckChanged(mBinding.btnAgreeMedical)
        }
        mBinding.btnThirdTerms.setOnClickListener {
            onCheckChanged(mBinding.btnThirdTerms)
        }
        mBinding.btnMarketing.setOnClickListener {
            onCheckChanged(mBinding.btnMarketing)
        }
    }

    private fun onCheckChanged(compoundButton: Button) {
        when(compoundButton.id) {
            R.id.btn_agree -> {
                if(mBinding.btnAgree.isChecked) {
                    mBinding.btnAgreeService.isChecked = true
                    mBinding.btnAgreeGet.isChecked = true
                    mBinding.btnAgreeMedical.isChecked = true
                    mBinding.btnThirdTerms.isChecked = true
                    mBinding.btnMarketing.isChecked = true
                }
                else {
                    mBinding.btnAgreeService.isChecked = false
                    mBinding.btnAgreeGet.isChecked = false
                    mBinding.btnAgreeMedical.isChecked = false
                    mBinding.btnThirdTerms.isChecked = false
                    mBinding.btnMarketing.isChecked = false
                }
            }
            else -> {
                mBinding.btnAgree.isChecked = (
                        mBinding.btnAgreeService.isChecked &&
                                mBinding.btnAgreeGet.isChecked &&
                                mBinding.btnAgreeMedical.isChecked &&
                                mBinding.btnThirdTerms.isChecked &&
                                mBinding.btnMarketing.isChecked)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                val btnRect = Rect()
                v.getGlobalVisibleRect(outRect)
                mBinding.btnOk.getGlobalVisibleRect(btnRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt()) && !btnRect.contains(
                        ev.rawX.toInt(), ev.rawY.toInt()
                    )
                ) {
                    v.clearFocus()
                    hideSoftKeyboard(v)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /////////////////////API/////////////////////////////
    private fun requestAuthSms() {
        LLog.e("로그인_문자메세지 API")
        if (mBinding.clOk.visibility == View.VISIBLE) {
            mBinding.clOk.visibility = View.GONE
        }
        val data = LoginSMS(mBinding.etPhonenum.text.toString())
        apiServices.getSMS(data).enqueue(object : Callback<LoginSMS> {
            override fun onResponse(call: Call<LoginSMS>, response: Response<LoginSMS>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"snedsms Success -> ${result.user_auth_number}")
                }
                else {
                    Log.d(TAG,"sendsms API ERROR -> ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<LoginSMS>, t: Throwable) {
                Log.d(TAG,"sendsms ERROR -> $t")
            }
        })
    }

    private fun authrequest() {
        LLog.e("로그인_로그인 API")
        val phone = mBinding.etPhonenum.text.toString()
        val smsnumber = mBinding.etAuthnum.text.toString()

        val data = LoginSend(phone, smsnumber)
        apiServices.getLogin(data).enqueue(object : Callback<LoginSend> {
            override fun onResponse(call: Call<LoginSend>, response: Response<LoginSend>) {
                val result = response.body()
                if(response.isSuccessful&&result!=null) {
                    Log.d(TAG,"getLogin Success -> ${result.accesstoken}")
                    prefs.myaccesstoken = result.accesstoken.toString()
                    moveMain()
                }
                else {
                    Log.d(TAG,"getLogin Fail -> ${response.errorBody()}")
                    if (response.code() == 404) {
                        phoneThrid()
                        prefs.myaccesstoken = result?.accesstoken.toString()
                    }else if (response.code() == 403) {
                        phoneThrid()
                        prefs.myaccesstoken = result?.accesstoken.toString()
                    }

                }
            }
            override fun onFailure(call: Call<LoginSend>, t: Throwable) {
                Log.d(TAG,"authrequest ERROR -> $t")
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        movestartLogin()
    }
}