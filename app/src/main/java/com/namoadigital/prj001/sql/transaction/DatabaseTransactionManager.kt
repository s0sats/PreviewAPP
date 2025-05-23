package com.namoadigital.prj001.sql.transaction

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.sqlite.transaction
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class DatabaseTransactionManager(
    context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(
        ToolBox_Con.getPreference_Customer_Code(
            context
        )
    ),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
) {

    constructor(context: Context) : this(
        context = context,
        mDB_NAME = ToolBox_Con.customDBPath(
            ToolBox_Con.getPreference_Customer_Code(context)
        ),
        mDB_VERSION = Constant.DB_VERSION_CUSTOM
    )

    fun executeTransaction(
        block: (db: SQLiteDatabase) -> Unit
    ): IResult<Unit> {
        openDB()
        try {
            //
            var transactionResult = false
            db.transaction {
                block(this)
                transactionResult = true
            }
            //
            closeDB()
            return if (transactionResult) {
                IResult.success(Unit)
            } else {
                failed(Exception("DB transaction not successful"))
            }
        } catch (e: Exception) {
            closeDB()
            ToolBox_Inf.registerException(this::javaClass.name, e)
            return failed(e)
        }
    }

    fun executeTransactionDaoObjReturn(
        block: (db: SQLiteDatabase) -> DaoObjReturn
    ): IResult<DaoObjReturn> {
        var daoObjReturn = DaoObjReturn()
        openDB()
        try {
            //
            db.transaction {
                daoObjReturn = block(this)
            }
            //
            closeDB()
            return if (!daoObjReturn.hasError()) {
                IResult.success(daoObjReturn)
            } else {
                failed(Exception(daoObjReturn.errorMsg ?: "DB transaction not successful"))
            }
        } catch (e: Exception) {
            closeDB()
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(this::javaClass.name, e)
            return failed(Exception(daoObjReturn.errorMsg ?: "DB transaction not successful"))
        }
    }
}