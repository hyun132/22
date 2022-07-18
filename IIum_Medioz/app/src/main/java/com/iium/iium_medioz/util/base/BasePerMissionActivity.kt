package com.iium.iium_medioz.util.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BasePerMissionActivity <T : ViewDataBinding>(
    private val activityId: Int
) : AppCompatActivity() {

    protected val scopeIo = CoroutineScope(Dispatchers.IO)
    protected val scopeMain = CoroutineScope(Dispatchers.Main)

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityId)

        binding = DataBindingUtil.setContentView(
            this,
            activityId
        )
    }


}