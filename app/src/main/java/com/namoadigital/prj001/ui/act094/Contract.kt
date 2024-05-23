package com.namoadigital.prj001.ui.act094

import android.content.Context
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import com.namoadigital.prj001.ui.act094.util.SelectDestinationUiEvent
import com.namoadigital.prj001.ui.base.BasePresenter
import com.namoadigital.prj001.ui.base.BaseView

interface Contract {

    interface View : BaseView<SelectDestinationUiEvent> {

        var wsProcess: String
        fun callAct005()
    }

    interface Presenter : BasePresenter<View> {

        fun execDestinationAvailable(filter: AvailableDestinationFilter? = null)

        fun execSelectDestination(
            destinationType: String,
            siteCode: Int?,
            ticketPrefix: Int?,
            ticketCode: Int?
        )

        fun getDestinationFilter(): AvailableDestinationFilter

        fun saveDestination(context: Context, response: String?, destination: SelectionDestinationAvailable)

    }
}