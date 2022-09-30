package com.iium.iium_medioz.util.base

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleObserver
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.util.activity.ActivityController
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.intro.splash.SplashActivity
import com.iium.iium_medioz.view.main.MainActivity
import kotlin.system.exitProcess

open class BaseFragment : Fragment() , LifecycleObserver {

    lateinit var apiServices: APIService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(LLog.TAG,"onCreateView Fragment")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
        Log.i(LLog.TAG,"onAttach Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(LLog.TAG,"onViewCreated Fragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiServices = ApiUtils.apiService
        Log.i(LLog.TAG,"onCreate Fragment")

    }

    override fun onResume() {
        super.onResume()
        Log.i(LLog.TAG,"onResume Fragment")

    }

    override fun onPause() {
        super.onPause()
        Log.i(LLog.TAG,"onPause Fragment")

    }

    override fun onDetach() {
        super.onDetach()
        Log.i(LLog.TAG,"onDetach Fragment")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(LLog.TAG,"onDestroyView Fragment")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LLog.TAG,"onDestroy Fragment")

    }

    override fun onStop() {
        super.onStop()
        Log.i(LLog.TAG,"onStop Fragment")

    }

    override fun onStart() {
        super.onStart()
        Log.i(LLog.TAG,"onStart Fragment")

    }

    internal fun ErrorDialog() {

        val dlg: AlertDialog.Builder = AlertDialog.Builder(context,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("에러메시지") //제목
        dlg.setMessage("데이터 연결이 원할하지 않습니다. 앱을 다시 실행시켜주세요") // 메시지
        dlg.setPositiveButton("확인") { dialog, which ->
            dialog.dismiss()
            exitProcess(0)
        }
        dlg.show()
    }

    internal fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }
}