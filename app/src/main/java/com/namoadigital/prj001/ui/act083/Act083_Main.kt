package com.namoadigital.prj001.ui.act083

import android.os.Bundle
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act083MainBinding

class Act083_Main : Base_Activity() {

    private lateinit var binding: Act083MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Act083MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}