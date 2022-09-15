package com.iium.iium_medioz.util.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.util.log.LLog

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
}