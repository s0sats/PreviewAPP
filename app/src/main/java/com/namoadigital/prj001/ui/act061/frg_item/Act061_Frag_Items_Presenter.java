package com.namoadigital.prj001.ui.act061.frg_item;

import android.content.Context;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.sql.IO_Inbound_Item_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

public class Act061_Frag_Items_Presenter implements Act061_Frag_Items_Contract.I_Presenter  {

    private Context context;
    private Act061_Frag_Items_Contract.I_View mView;
    private HMAux hmAux_trans;
    private IO_Inbound_ItemDao itemDao;

    public Act061_Frag_Items_Presenter(Context context, Act061_Frag_Items_Contract.I_View mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
        this.itemDao = new IO_Inbound_ItemDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
        );
    }
    //


    @Override
    public ArrayList<HMAux> getItemList(int inboundPrefix, int inboundCode) {
        return (ArrayList<HMAux>) itemDao.query_HM(
            new IO_Inbound_Item_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                inboundPrefix,
                inboundCode
            ).toSqlQuery()
        );
    }
}
