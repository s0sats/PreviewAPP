package com.namoadigital.prj001.ui.act061.frag_drawer;

import android.content.Context;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.sql.Sql_Act061_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act061_Frag_Drawer_Presenter implements Act061_Frag_Drawer_Contract.I_Presenter {

    private Context context;
    private Act061_Frag_Drawer_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_Inbound_ItemDao ioInboundItemDao;

    public Act061_Frag_Drawer_Presenter(Context context, Act061_Frag_Drawer_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.ioInboundItemDao = new IO_Inbound_ItemDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    //
    @Override
    public HMAux getPercents(int inbound_prefix, int inbound_code) {
        HMAux percentAux = new HMAux();
        //
        percentAux = ioInboundItemDao.getByStringHM(
                    new Sql_Act061_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        inbound_prefix,
                        inbound_code
                    ).toSqlQuery()
        );
        //
        return percentAux;
    }
}
