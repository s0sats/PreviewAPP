package com.namoadigital.prj001.ui.act083

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.MyActions

class Act083ViewModel(
                      private val ticketDao: TK_TicketDao,
                      private val ticketCacheDao: TkTicketCacheDao,
                      private val scheduleDao: MD_Schedule_ExecDao,
                      private val formApDao: GE_Custom_Form_ApDao,
                      private val formLocalDao: GE_Custom_Form_LocalDao
                      ): ViewModel()
{
    private val mutableList = mutableListOf<MyActions>()

    fun getMyActionsList(mketTextFilter: String, tabFilter: Boolean){

    }

    private fun getLocalTickets(mketTextFilter: String, tabFilter: Boolean){

    }

    private fun getCachedTickets(mketTextFilter: String, tabFilter: Boolean){

    }

    private fun getSchedules(mketTextFilter: String, tabFilter: Boolean){

    }

    private fun getFormAp(mketTextFilter: String, tabFilter: Boolean){

    }

    private fun getLocalForms(mketTextFilter: String, tabFilter: Boolean){

    }

}

class Act083ViewModelFactory(
                             private val ticketDao: TK_TicketDao,
                             private val ticketCacheDao: TkTicketCacheDao,
                             private val scheduleDao: MD_Schedule_ExecDao,
                             private val formApDao: GE_Custom_Form_ApDao,
                             private val formLocalDao: GE_Custom_Form_LocalDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Act083ViewModel(ticketDao, ticketCacheDao, scheduleDao, formApDao, formLocalDao) as T
    }
}
