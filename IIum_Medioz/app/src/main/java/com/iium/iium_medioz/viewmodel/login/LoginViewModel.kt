package com.iium.iium_medioz.viewmodel.login

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.iium.iium_medioz.util.encrypt.AESAdapter.encAES
import com.iium.iium_medioz.util.preference.PreferenceManager

class LoginViewModel(prefs: PreferenceManager) : BaseObservable() {
    private var prefs: PreferenceManager? = null
    private var phoneNum: String? = null
    private var authNum: String? = null
    private var loginProcess = 0

    private val birthday: String? = null
    private val selectedGender: String? = null
    private val termsAgree = false
    private var isJoin = false

    private val yearArray: List<String>? = null
    private val monthArray: List<String>? = null
    private val dayArray: List<String>? = null

    fun LoginObservable(pref: PreferenceManager) {
        prefs = pref
    }

    @Bindable
    fun getPhoneNum(): String? {
        return phoneNum
    }

    fun getEncPhoneNum(): String? {
        return encAES(prefs?.getEncIv()!!, prefs!!.getEncKey()!!, phoneNum)
    }

    fun setPhoneNum(phoneNum: String) {
        this.phoneNum = phoneNum
        notifyChange()
    }

    @Bindable
    fun getAuthNum(): String? {
        return authNum
    }

    fun setAuthNum(authNum: String?) {
        this.authNum = authNum
        notifyChange()
    }

    @Bindable
    fun getLoginProcess(): Int {
        return loginProcess
    }

    fun setLoginProcess(loginProcess: Int) {
        this.loginProcess = loginProcess
        notifyChange()
    }

    fun isJoin(): Boolean {
        return isJoin
    }

    fun setJoin(join: Boolean) {
        isJoin = join
    }


}