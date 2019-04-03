package com.namoadigital.prj001.ui.act061;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act061_Main_Presenter implements Act061_Main_Contract.I_Presenter {

    private Context context;
    private Act061_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_InboundDao inboundDao;

    public Act061_Main_Presenter(Context context, Act061_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.inboundDao = new IO_InboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }


    @Override
    public IO_Inbound getInbound(int prefix, int code) {
        IO_Inbound ioInbound = inboundDao.getByString(
                new IO_Inbound_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        prefix,
                        code
                ).toSqlQuery()
        );
        //
        return ioInbound;
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct056();
    }
}
