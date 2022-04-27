package com.namoadigital.prj001.migrations

import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.dao.EV_Module_ResDao
import com.namoadigital.prj001.dao.GE_Custom_FormDao
import com.namoadigital.prj001.database.MigrationSQLite

val MigrationV1 = object : MigrationSQLite(1, 2){
    override fun migrate(db: SQLiteDatabase) {
        val cursor = db.query(GE_Custom_FormDao.TABLE,
            arrayOf(
                GE_Custom_FormDao.CUSTOMER_CODE,
                GE_Custom_FormDao.CUSTOM_FORM_TYPE,
                GE_Custom_FormDao.CUSTOM_FORM_CODE,
                GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                GE_Custom_FormDao.REQUIRE_SIGNATURE,
                GE_Custom_FormDao.REQUIRE_LOCATION,
                GE_Custom_FormDao.REQUIRE_SERIAL_DONE,
                GE_Custom_FormDao.AUTOMATIC_FILL,
                GE_Custom_FormDao.ALL_SITE,
                GE_Custom_FormDao.ALL_OPERATION,
                GE_Custom_FormDao.ALL_PRODUCT,
                GE_Custom_FormDao.TAG_OPERATIONAL_CODE,
                GE_Custom_FormDao.IS_SO,
                GE_Custom_FormDao.SO_EDIT_START_END,
                GE_Custom_FormDao.SO_ORDER_TYPE_CODE_DEFAULT,
                GE_Custom_FormDao.SO_ALLOW_CHANGE_ORDER_TYPE,
                GE_Custom_FormDao.SO_ALLOW_BACKUP,
                GE_Custom_FormDao.SO_OPTIONAL_JUSTIFY_PROBLEM,
                GE_Custom_FormDao.BLOCK_SPONTANEOUS,
                GE_Custom_FormDao.CUSTOM_FORM_DESC
            ), null, null, null, null, null, null)
    }
}


