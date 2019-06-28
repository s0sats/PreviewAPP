package com.namoadigital.prj001.ui.act067.frag_drawer;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.sql.Sql_Act067_001;
import com.namoadigital.prj001.ui.act061.frag_drawer.Act061_Frag_Drawer_Contract;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act067_Frag_Drawer_Presenter implements Act061_Frag_Drawer_Contract.I_Presenter {

    private Context context;
    private Act067_Frag_Drawer_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_Outbound_ItemDao ioOutboundItemDao;

    public Act067_Frag_Drawer_Presenter(Context context, Act067_Frag_Drawer_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.ioOutboundItemDao = new IO_Outbound_ItemDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    //
    @Override
    public HMAux getPercents(int outbound_prefix, int outbound_code) {
        HMAux percentAux = new HMAux();
        //
        percentAux = ioOutboundItemDao.getByStringHM(
                    new Sql_Act067_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        outbound_prefix,
                        outbound_code
                    ).toSqlQuery()
        );
        //
        return percentAux;
    }
}
