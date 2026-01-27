package com.namoadigital.prj001.database.scripts.multi.ticket

import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.TkTicketVGDao

val tkTicketVGDatabaseTable =
    DatabaseTable(
        name = TkTicketVGDao.TABLE_NAME,
        columns = listOf(
            Column(TkTicketVGDao.CUSTOMER_CODE, ColumnType.INT, isNullable = false),
            Column(TkTicketVGDao.TICKET_PREFIX, ColumnType.INT, isNullable = false),
            Column(TkTicketVGDao.TICKET_CODE, ColumnType.INT, isNullable = false),
            Column(TkTicketVGDao.VG_CODE, ColumnType.INT, isNullable = false),

        ),
        primaryKey = listOf(
            TkTicketVGDao.CUSTOMER_CODE,
            TkTicketVGDao.TICKET_PREFIX,
            TkTicketVGDao.TICKET_CODE,
            TkTicketVGDao.VG_CODE,
        )
    ).generateCreateTableScript()
