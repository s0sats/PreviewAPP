package com.namoadigital.prj001.core.module.repository

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.md_site.MdSiteRepository
import com.namoadigital.prj001.core.data.local.repository.md_site.MdSiteRepositoryImpl
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepositoryImp
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_FormDao
import com.namoadigital.prj001.dao.TkTicketVGDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun providesRepositoryTicket(
        @ApplicationContext app: Context,
        dao: TK_TicketDao,
        formDao: TK_Ticket_FormDao,
        ticketVGDao: TkTicketVGDao
    ): TicketRepository = TicketRepositoryImp(app, dao, formDao, ticketVGDao)

   @Provides
    fun providesRepositoryMdSite(
        @ApplicationContext app: Context,
        dao: MD_SiteDao,
    ): MdSiteRepository = MdSiteRepositoryImpl(app, dao)

}