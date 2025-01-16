package com.namoadigital.prj001.database.scripts.multi.masterdata

import com.namoadigital.prj001.dao.md.MDRegionDao.Companion.CUSTOMER_CODE
import com.namoadigital.prj001.dao.md.MDRegionDao.Companion.REGION_CODE
import com.namoadigital.prj001.dao.md.MDRegionDao.Companion.REGION_DESC
import com.namoadigital.prj001.dao.md.MDRegionDao.Companion.REGION_ID
import com.namoadigital.prj001.dao.md.MDRegionDao.Companion.TABLE_NAME



const val CREATE_REGION_TABLE = """create table if not exists [$TABLE_NAME]
            (
            [$CUSTOMER_CODE] int not null,
            [$REGION_CODE] int not null,
            [$REGION_ID] int not null,
            [$REGION_DESC] text not null collate nocase,
    constraint [pk_$TABLE_NAME] primary key([$CUSTOMER_CODE],[$REGION_CODE])
);"""


const val REMOVE_TABLE = "DELETE FROM [$TABLE_NAME]"

