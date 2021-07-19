package com.namoadigital.prj001.ui.act085

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.databinding.Act085MainBinding
import com.namoadigital.prj001.databinding.Act085MainContentBinding

class Act085Main : Base_Activity_Frag() {

    private lateinit var binding : Act085MainContentBinding
    private val fm = supportFragmentManager
    private lateinit var bundle: Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act085MainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        binding = mainBinding.act085MainContent
        initBundle(savedInstanceState)
        initVars()
    }

    private fun initBundle(savedInstanceState: Bundle?) {
        bundle = (savedInstanceState?: intent.extras?: Bundle()) as Bundle
    }

    private fun initVars() {

    }

    private fun <T : BaseFragment?> setFrag(type: T, sTag: String) {
        if (fm.findFragmentByTag(sTag) == null) {
            val ft = fm.beginTransaction()
            ft.replace(binding.act085FrgPlaceholder.id, type as Fragment, sTag)
            ft.addToBackStack(null)
            ft.commit()
        }
    }
}