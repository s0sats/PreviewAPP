package com.namoadigital.prj001.ui.act070.view;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class TK_Ticket_Ctrl_Generic extends TK_Ticket_Ctrl_Super {


    public TK_Ticket_Ctrl_Generic(Context context, int ticketProductCode, int ticketSerialCode, TK_Ticket_Ctrl mTicketCtrl, HMAux HmAuxTrans, OnClickListener ivActionClickListener) {
        super(context, ticketProductCode, ticketSerialCode, mTicketCtrl, HmAuxTrans, ivActionClickListener);
    }

    @Override
    public void applyFilterVisibility() {
        setVisible(!ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(getmStatus()));
    }
}
