package com.namoadigital.prj001.sql.transaction.ticket

import android.content.Context
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_StepDao
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.TK_Ticket_Step
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_008
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class TransactionSaveTicketStepCtrlAtForm(
    context: Context,
    val ticketDao: TK_TicketDao,
    val stepDao: TK_Ticket_StepDao,
    mDB_NAME: String = ToolBox_Con.customDBPath(
        ToolBox_Con.getPreference_Customer_Code(
            context
        )
    ),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
) {

    @Throws(Exception::class)
    fun saveTicket(
        tkTicketStep: TK_Ticket_Step
    ):Boolean{
        openDB()
        var transactionResult = true
        db.beginTransaction()
        //
        try {
            //
            val daoObjReturn: DaoObjReturn = stepDao.addUpdate(tkTicketStep, db)
            //
            if(!daoObjReturn.hasError()){
                if (tkTicketStep.update_required == 1) {
                    val dbResult  = ticketDao.addUpdate(
                        Sql_WS_TK_Ticket_Save_008(
                            tkTicketStep.customer_code,
                            tkTicketStep.ticket_prefix,
                            tkTicketStep.ticket_code,
                            1
                        ).toSqlQuery(),
                        db
                    )
                    if (dbResult.hasError()) {
                        transactionResult = false
                    }
                }
            }else{
                transactionResult = false
            }
            //
            if (transactionResult) {
                db.setTransactionSuccessful()
            }
        }catch (e:Exception){
            transactionResult = false
            ToolBox_Inf.registerException(javaClass.name, e)
        }finally {
            db.endTransaction()
            closeDB()
        }
        return transactionResult
    }


}