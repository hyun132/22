package com.iium.iium_medioz.view.main.bottom.data.tablayout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentSendBinding
import com.iium.iium_medioz.model.send.SendList
import com.iium.iium_medioz.model.send.SendTestModel
import com.iium.iium_medioz.util.adapter.SendAdapter
import com.iium.iium_medioz.util.base.BaseFragment
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.main.bottom.data.DataViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendFragment : BaseFragment() {

    private lateinit var mBinding: FragmentSendBinding
    private var sendadapter: SendAdapter? = null
    private lateinit var callback: OnBackPressedCallback
    private val viewModel: DataViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_send, container, false)
        mBinding.fragment = this
//        initView()
        viewModel.initSendView()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sendDataList.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setAdapter(datalist: List<SendList>) {
        if (datalist.isEmpty()) {
            mBinding.SendListRecyclerView.visibility = View.GONE
            mBinding.tvDataSendNot.visibility = View.VISIBLE
        } else {
            mBinding.SendListRecyclerView.visibility = View.VISIBLE
            mBinding.tvDataSendNot.visibility = View.GONE
        }
        val adapter = SendAdapter(datalist, context!!)
        val rv = mBinding.SendListRecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        sendadapter?.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this@SendFragment)
                    ?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}