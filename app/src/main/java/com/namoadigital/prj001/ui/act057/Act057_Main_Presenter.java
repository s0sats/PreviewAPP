package com.namoadigital.prj001.ui.act057;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Inbound_Search_Record;

import java.util.ArrayList;

public class Act057_Main_Presenter implements Act057_Main_Contract.I_Presenter{

    private Context context;
    private Act057_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;

    public Act057_Main_Presenter(Context context, Act057_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void processListInfo(long record_count, long record_page, ArrayList<IO_Inbound_Search_Record> records) {
        mView.setRecordInfo();
        //
        if(records != null || records.size() > 0){
            mView.loadInboundList();
            //
            if(records.size() == 1){
               defineFlow();
            }
        }
    }

    private void defineFlow() {

    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct056();
    }
}
