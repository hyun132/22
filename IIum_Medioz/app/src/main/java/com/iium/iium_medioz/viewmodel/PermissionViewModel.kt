package com.iium.iium_medioz.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iium.iium_medioz.util.base.BasePerMissionModel
import com.iium.iium_medioz.util.base.BaseViewModel

class PermissionViewModel (application: Application) : BasePerMissionModel(application) {

    private val _downloadCount = MutableLiveData<Int>(4000)
    val downloadCount: LiveData<Int>
        get() = _downloadCount

    private val _securityAccidentCount = MutableLiveData<Int>(0)
    val securityAccidentCount: LiveData<Int>
        get() = _securityAccidentCount

}