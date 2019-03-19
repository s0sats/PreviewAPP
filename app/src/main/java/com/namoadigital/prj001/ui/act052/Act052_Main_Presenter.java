package com.namoadigital.prj001.ui.act052;

import android.content.Context;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;

public class Act052_Main_Presenter implements Act052_Main_Contract.I_Presenter {
    private Context context;
    private Act052_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_Serial_Process_Record record;

    private static final String IN_CONF  = "IN_CONF";
    private static final String IN_PUT_AWAY  = "IN_PUT_AWAY";
    private static final String MOVE_PLANNED  = "MOVE_PLANNED";
    private static final String MOVE  = "MOVE";
    private static final String OUT_PICKING = "OUT_PICKING";
    private static final String OUT_CONF  = "OUT_CONF";

    public Act052_Main_Presenter(Context context, Act052_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct051();
    }

    @Override
    public void defineIOSerialFlow(IO_Serial_Process_Record data) {
        switch (data.getProcess_type()){
            case IN_CONF:
                Toast.makeText(context, "IN_CONF", Toast.LENGTH_SHORT).show();
                break;
            case IN_PUT_AWAY:
                Toast.makeText(context, "IN_PUT_AWAY", Toast.LENGTH_SHORT).show();
                break;
            case MOVE_PLANNED:
                Toast.makeText(context, "MOVE_PLANNED", Toast.LENGTH_SHORT).show();
                break;
            case MOVE:
                Toast.makeText(context, "MOVE", Toast.LENGTH_SHORT).show();
                break;
            case OUT_PICKING:
                Toast.makeText(context, "OUT_PICKING", Toast.LENGTH_SHORT).show();
                break;
            case OUT_CONF:
                Toast.makeText(context, "OUT_CONF", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
