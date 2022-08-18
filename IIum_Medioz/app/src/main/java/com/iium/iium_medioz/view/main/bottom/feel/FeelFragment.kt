package com.iium.iium_medioz.view.main.bottom.feel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iium.iium_medioz.databinding.FragmentFeelBinding
import com.iium.iium_medioz.util.common.AutoClearedValue

class FeelFragment : Fragment() {

    private var binding by AutoClearedValue<FragmentFeelBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentFeelBinding.inflate(layoutInflater, container, false).also { binding = it }.root

}