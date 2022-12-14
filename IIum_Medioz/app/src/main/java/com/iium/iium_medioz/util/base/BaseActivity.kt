package com.iium.iium_medioz.util.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.network.MyState
import com.iium.iium_medioz.util.`object`.ActivityControlManager
import com.iium.iium_medioz.util.`object`.Constant.PROGRESS_TIMEOUT
import com.iium.iium_medioz.util.common.CommonData
import com.iium.iium_medioz.util.dialog.AlertDialogManager
import com.iium.iium_medioz.util.dialog.DialogCancelListener
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.util.log.LLog.e
import com.iium.iium_medioz.util.network.NetworkStatusTracker
import com.iium.iium_medioz.util.network.NetworkStatusViewModel
import com.iium.iium_medioz.view.intro.permission.PermissionManager
import com.iium.iium_medioz.view.login.LoginActivity
import com.iium.iium_medioz.view.login.StartLoginActivity
import com.iium.iium_medioz.view.login.sign.SignUpActivity
import com.iium.iium_medioz.view.main.MainActivity
import com.iium.iium_medioz.view.main.bottom.data.DataDetyailActivity
import com.iium.iium_medioz.view.main.bottom.data.DataUploadActivity
import com.iium.iium_medioz.view.main.bottom.data.SaveActivity
import com.iium.iium_medioz.view.main.bottom.data.SaveSendActivity
import com.iium.iium_medioz.view.main.bottom.data.pdf.MakePDFActivity
import com.iium.iium_medioz.view.main.bottom.data.search.SearchActivity
import com.iium.iium_medioz.view.main.bottom.data.send.SendActivity
import com.iium.iium_medioz.view.main.bottom.home.calendar.CalendarActivity
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.DocumentActivity
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalActivity
import com.iium.iium_medioz.view.main.bottom.insurance.search.SearchAddressActivity
import com.iium.iium_medioz.view.main.bottom.mypage.cs.CsActivity
import com.iium.iium_medioz.view.main.bottom.mypage.cs.CsUploadActivity
import com.iium.iium_medioz.view.main.bottom.mypage.notice.NoticeActivity
import com.iium.iium_medioz.view.main.bottom.mypage.setting.SettingActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.one_button_dialog.view.*
import kotlinx.android.synthetic.main.two_button_dialog.view.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.system.exitProcess

open class BaseActivity : AppCompatActivity() {
    internal open var instance: BaseActivity?=null
    val comm: CommonData = CommonData().getInstance()
    var ResultView: ActivityResultLauncher<Intent>? = null
    private var doubleBackToExit = false
    var progress: AppCompatDialog? = null
    private var mTimeoutHandler: Handler? = null
    var alert: AlertDialogManager? = null


    internal val realm by lazy {
        Realm.getDefaultInstance()
    }

    private val viewModel: NetworkStatusViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val networkStatusTracker = NetworkStatusTracker(this@BaseActivity)
                    return NetworkStatusViewModel(networkStatusTracker) as T
                }
            },
        )[NetworkStatusViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        instance = this
        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        viewModel.state.observe(this) { state ->
           when(state) {
               MyState.Error -> networkDialog()
               MyState.Fetched -> networkDialog()
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
        mTimeoutHandler = null
    }

    override fun onResume() {
        super.onResume()
        if(isInternetAvailable(this)) {
            Log.d(TAG,"???????????? ?????????")
        } else {
            networkDialog()
            return
        }
    }

    override fun onStop() {
        super.onStop()
    }


    open fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    open fun moveAndFinishActivity(activity: Class<*>?) {
        ActivityControlManager.moveAndFinishActivity(this, activity)
    }

    open fun hideSoftKeyboard(v: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    @SuppressLint("ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    val hash: String?
        @SuppressLint("ObsoleteSdkInt")
        get() {
            val context = applicationContext
            val pm = context.packageManager
            val packageName = context.packageName
            var cert: String? = null
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    @SuppressLint("WrongConstant") val packageInfo = packageManager.getPackageInfo(
                        getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )
                    val signatures = packageInfo.signingInfo.apkContentsSigners
                    val md = MessageDigest.getInstance("SHA1")
                    for (signature in signatures) {
                        md.update(signature.toByteArray())
                        cert = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                        cert = cert.replace(
                            Objects.requireNonNull(System.getProperty("line.separator")).toRegex(),
                            ""
                        )
                        Log.i("test", "Cert=$cert")
                    }
                } else {
                    @SuppressLint("PackageManagerGetSignatures") val packageInfo =
                        pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                    val certSignature = packageInfo.signatures[0]
                    val msgDigest = MessageDigest.getInstance("SHA1")
                    msgDigest.update(certSignature.toByteArray())
                    cert = Base64.encodeToString(msgDigest.digest(), Base64.DEFAULT)
                    cert = cert.replace(
                        Objects.requireNonNull(System.getProperty("line.separator")).toRegex(), ""
                    )
                    Log.i("test", "Cert=$cert")
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e("????????????")
            } catch (e: NoSuchAlgorithmException) {
                e("????????????")
            }
            return cert
        }

    open fun showProgress() {
        if (this.isFinishing) {
            return
        }
        //if ( (progress != null && progress.isShowing()) || this.isFinishing() || getApplicationContext() == null) {
        if (progress != null && progress!!.isShowing || this.isFinishing || applicationContext == null) {
            return
        } else {
            progress = AppCompatDialog(this)
            progress!!.setCancelable(false)
            progress!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progress!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progress!!.window!!.setDimAmount(0f)
            //            progress.getWindow().setNavigationBarContrastEnforced(true);
            progress!!.setContentView(R.layout.custom_loading)
            progress!!.show()

            // ???????????? ??????
            val looper = Looper.myLooper()
            mTimeoutHandler = null
            mTimeoutHandler = Handler(looper!!)
            mTimeoutHandler!!.postDelayed({
                if (progress != null && progress!!.isShowing) {
                    mTimeoutHandler?.removeMessages(0)
                    //progressTimeout();
                    if (!this.isFinishing) {
                        progress!!.dismiss()
                        progress = null
                    }
                }
            }, PROGRESS_TIMEOUT.toLong())
        }
    }

    open fun getLocationPermissionGranted(): Boolean {
        return PermissionManager.getPermissionGranted(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && PermissionManager.getPermissionGranted(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }


    open fun totalGpsCheck(listener: DialogCancelListener?): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!getLocationPermissionGranted()) {
            alert?.showDialog(getString(R.string.error_permission_location_request)) { positive ->
                PermissionManager.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
            return false
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alert?.showDialog(
                getString(R.string.gps_popup_title),
                getString(R.string.gps_popup_btn_positive),
                { v ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                },
                getString(R.string.gps_popup_btn_negative)
            ) { v ->
                listener?.onCancelSelected()
            }
            return false
        }
        return true
    }

    open fun stopProgress() {
        /*if (this.isFinishing()) {
            return;
        }*/
        if (progress != null && progress!!.isShowing) {
            // ?????? ????????? ???????????? ??????
            if (mTimeoutHandler != null) {
                mTimeoutHandler!!.removeMessages(0)
            }
            progress!!.dismiss()
            progress = null
        }
    }

    internal fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveSignup() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun movestartLogin() {
        val intent = Intent(this, StartLoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveLogins() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveUpload() {
        val intent = Intent(this, DataUploadActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveSave() {
        val intent = Intent(this, SaveActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveSaveSend() {
        val intent = Intent(this, SaveSendActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveSetting() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveNotice() {
        val intent = Intent(this, NoticeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveCs() {
        val intent = Intent(this, CsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveUploadCs() {
        val intent = Intent(this, CsUploadActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveDetail() {
        val intent = Intent(this, DataDetyailActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun movesend() {
        val intent = Intent(this, SendActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun movePDF() {
        val intent = Intent(this, MakePDFActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveHospital() {
        val intent = Intent(this, HospitalActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun modeCalendar() {
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun searchAddress() {
        val intent = Intent(this, SearchAddressActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun document() {
        val intent = Intent(this, DocumentActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    @SuppressLint("ResourceType")
    internal fun serverDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.server_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun networkDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.network_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun rootingDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.warning_rooting)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun smscheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.auth_num_wrong_text)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun termcheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.term_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun phonecheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.phone_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun passcheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.password_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun nickcheck() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.login_hint_nickname)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    internal fun updateDialog() {
        val myLayout = layoutInflater.inflate(R.layout.two_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.update_check)
        val dialog = build.create()
        dialog.show()
        myLayout.finish_btn.setOnClickListener {
            dialog.dismiss()
        }
        myLayout.update_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    internal fun ErrorDialog() {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("???????????????") //??????
        dlg.setMessage("????????? ????????? ???????????? ????????????. ?????? ?????? ?????????????????????") // ?????????
        dlg.setPositiveButton("??????") { dialog, which ->
            finishAffinity()
            dialog.dismiss()
            exitProcess(0)
        }
        dlg.show()
    }
}