package com.namoadigital.prj001.ui.act079.view

import android.content.Context
import android.view.LayoutInflater
import com.namoadigital.prj001.databinding.Act079ViewNcTabBinding
import com.namoadigital.prj001.model.TkTicketOriginNc

class Act079ViewNcTab(
    context: Context,
    nc: TkTicketOriginNc
) : Act079ViewNcBase(
    context,
    nc
) {
    private val binding : Act079ViewNcTabBinding by lazy {
        Act079ViewNcTabBinding.inflate(LayoutInflater.from(context),this,true)
    }

    init {
        initialize()
    }

    private fun initialize() {
        binding.act079ViewNcTabTvDesc.text = getTabDesc()
    }

    private fun getTabDesc(): String {
        return "${nc.getPage()} - ${nc.getDescription()}"
    }
}