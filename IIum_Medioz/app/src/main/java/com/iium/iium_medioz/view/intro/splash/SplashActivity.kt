package com.iium.iium_medioz.view.intro.splash

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.getkeepsafe.relinker.BuildConfig
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySplashBinding
import com.iium.iium_medioz.model.rest.base.*
import com.iium.iium_medioz.util.`object`.ActivityControlManager
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.util.network.ResultListener
import com.iium.iium_medioz.util.root.RootUtil
import com.iium.iium_medioz.view.login.StartLoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var mBinding: ActivitySplashBinding
    private lateinit var apiServices: APIService
    private var devpolicyversion: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        inStatusBar()
//        moveSignup()
        checkNetwork()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    // 네트워크 체크
    private fun checkNetwork() {
        LLog.e("1. 네트워크 확인")
        if(isInternetAvailable(this)) {
            checkVerification()
        } else {
            networkDialog()
            return
        }
    }

    // 2. 루팅 확인
    private fun checkLoot() {
        LLog.e("2. 루팅 확인")
        if (!BuildConfig.DEBUG && RootUtil.isDeviceRooted) {
            rootingDialog()
            return
        }
        moveLogin()
    }

    // 3. 검증 API 호출키 및 Hash키 검증
    private fun checkVerification() {
        LLog.e("3. 검증 API 호출키 및 Hash키 검증")
        requestVerificationJson(object : ResultListener {
            override fun onSuccess() {
                checkPolicyVersion()
            }
        })
    }

    // 4. 정책 버전 비교 및 저장
    private fun checkPolicyVersion() {
        LLog.e("4. 정책 버전 비교 및 저장")
        requestPolicy(object : ResultListener {
            override fun onSuccess() {
                checkAppVersion()
            }
        })
    }

    // App 버전 체크
    @SuppressLint("ObsoleteSdkInt")
    private fun checkAppVersion() {
        LLog.e("6. 앱 버전 체크")
        val versionCode: Policy? =
            realm.where(Policy::class.java).equalTo("id", "APP_VER_ANDROID").findFirst()
        if (versionCode != null) {
            val refrenceCode: Int = versionCode.value!!.toInt()
            var currentCode: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                currentCode = try {
                    packageManager.getPackageInfo(
                        this.packageName,
                        PackageManager.GET_ACTIVITIES
                    ).longVersionCode
                        .toInt()
                } catch (e: PackageManager.NameNotFoundException) {
                    -1
                }
            } else {
                val manager = this.packageManager
                val info: PackageInfo
                try {
                    info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
                    currentCode = info.versionCode
                } catch (e: PackageManager.NameNotFoundException) {
                    currentCode = -1
                }
            }
            if (currentCode < refrenceCode) {
                showAppUpdate()
                return
            }
        } else {
            serverDialog()
            return
        }
    }

    private fun showAppUpdate() {
        LLog.e("7. 앱 업데이트")
        updateDialog()
    }

    private fun requestVerificationJson(listener: ResultListener) {
        LLog.e("검증 API")
        val vercall: Call<Verification> = apiServices.getVerification("qwer")
        vercall.enqueue(object : Callback<Verification> {
            override fun onResponse(call: Call<Verification>, response: Response<Verification>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    val encrypt = Encrypt()
                    encrypt.key.toString()
                    encrypt.iv.toString()
                    result.result
                    result.encrypt
                    result.policy_ver
                    devpolicyversion = result.policy_ver.toString()
                    getinformation(result, listener)
                    Log.d(TAG,"Vertification result SUCESS -> $result")
                }
                else {
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<Verification>, t: Throwable) {
                Log.d(TAG, "loadVerAPI error -> $t")
                serverDialog()
            }
        })
    }

    private fun getinformation(result: Verification?, networkListener: ResultListener) {
        LLog.e("프리프런스")
        val aes_iv: String? = Encrypt().iv
        val aes_key: String? = Encrypt().key
        prefs.myeniv = aes_iv
        prefs.myenkey = aes_key
        prefs.mylangcode = "LANG_0001"
        prefs.myhash = hash.toString()
        networkListener.onSuccess()
    }

    private fun requestPolicy(listener: ResultListener) {
        LLog.e("정책 API")
        val vercall: Call<AppPolicy> = apiServices.getPolicy()
        vercall.enqueue(object : Callback<AppPolicy> {
            override fun onResponse(call: Call<AppPolicy>, response: Response<AppPolicy>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Policy response SUCCESS -> $result")
                    paresePolicy(result,listener)
                    requestLogin()

                }
                else {
                    Log.d(TAG,"Policy response ERROR -> $result")
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<AppPolicy>, t: Throwable) {
                Log.d(TAG, "Policy error -> $t")
                serverDialog()
            }
        })
    }

    private fun paresePolicy(result: AppPolicy, listener: ResultListener) {
        LLog.e("렐름")
        realm.executeTransaction { realm ->
            realm.where(Policy::class.java).findAll().deleteAllFromRealm()
            result.policy?.let { realm.copyToRealm(it) }
            listener.onSuccess()
        }

    }

    private fun requestLogin() {
        LLog.e("자동 로그인 API")
        val vercall: Call<AutoLogin> = apiServices.getautoLogin(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<AutoLogin> {
            override fun onResponse(call: Call<AutoLogin>, response: Response<AutoLogin>) {
                val result = response.body()
                if (response.code() == 404 || response.code() == 401) {
                    prefs.newaccesstoken=result?.accesstoken
                    moveLogin()
                }
                else if(response.code() == 200){
                    getPreferences(0).edit().remove("PREF_ACCESS_TOKEN").apply()
                    prefs.newaccesstoken=result?.accesstoken
                    moveMain()
                }
            }
            override fun onFailure(call: Call<AutoLogin>, t: Throwable) {
                Log.d(TAG, "requestLogin error -> $t")
                serverDialog()
            }
        })
    }

    private fun moveLogin() {
        ActivityControlManager.delayRun({
            moveAndFinishActivity(StartLoginActivity::class.java) },
            Constant.SPLASH_WAIT
        )
    }
}