package com.namoadigital.prj001.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.util.ConstantBaseApp

fun Context.sendCtrlFromToStatus( stepCode: Int, ticketSeq: Int, ticketSeqTmp: Int) {
    val mIntent = Intent()
    mIntent.setAction(ConstantBaseApp.BR_TICKET_SAVE)
    mIntent.addCategory(Intent.CATEGORY_DEFAULT)
    val bundle = Bundle()
    bundle.putString(
        ConstantBaseApp.SW_TYPE,
        ConstantBaseApp.TK_TICKET_INTENT_FILTER_ACTION_CTRL_UPDATE
    )
    bundle.putInt(TK_Ticket_CtrlDao.STEP_CODE, stepCode)
    bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, ticketSeq)
    bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ_TMP, ticketSeqTmp)
    mIntent.putExtras(bundle)
    //
    LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
}