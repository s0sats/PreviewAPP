package com.namoadigital.prj001.migrations

import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.dao.EV_User_CustomerDao
import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.database.MigrationSQLite

val MigrationSingleV12 = object : MigrationSQLite(12, 13) {

    override fun migrate(db: SQLiteDatabase) {
        if (!isFieldExist(db, EV_User_CustomerDao.TABLE, EV_User_CustomerDao.AUTOMATIC_SITE_CODE)) {
            db.execSQL(""" ALTER TABLE [${EV_User_CustomerDao.TABLE}] ADD [${EV_User_CustomerDao.AUTOMATIC_SITE_CODE}] int ;""".trimIndent())
        }
        if (!isFieldExist(db, EV_User_CustomerDao.TABLE, EV_User_CustomerDao.FIELD_SERVICE)) {
            db.execSQL(""" ALTER TABLE [${EV_User_CustomerDao.TABLE}] ADD [${EV_User_CustomerDao.FIELD_SERVICE}] int not null DEFAULT 0;""".trimIndent())
        }
        if (!isFieldExist(db, EV_User_CustomerDao.TABLE, EV_User_CustomerDao.FIELD_SERVICE_MODE_ONLY)) {
            db.execSQL(""" ALTER TABLE [${EV_User_CustomerDao.TABLE}] ADD [${EV_User_CustomerDao.FIELD_SERVICE_MODE_ONLY}] int not null DEFAULT 0;""".trimIndent())
        }
    }
}