package com.namoadigital.prj001.ui.act059;

import android.content.Context;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.IO_Inbound_Item_Sql_003;
import com.namoadigital.prj001.sql.IO_Inbound_Item_Sql_006;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act059_Main_Presenter implements Act059_Main_Contract.I_Presenter  {
    Context context;
    Act059_Main mView;
    HMAux hmAux_trans;

    public Act059_Main_Presenter(Context context, Act059_Main mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
    }

    @Override
    public IO_Inbound_Item getInboudItem(Integer move_prefix, Integer move_code, Integer inbound_item) {
        IO_Inbound_ItemDao io_inbound_itemDao = new IO_Inbound_ItemDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        return io_inbound_itemDao.getByString(new IO_Inbound_Item_Sql_006(
                ToolBox_Con.getPreference_Customer_Code(context),
                move_prefix,
                move_code,
                inbound_item
        ).toSqlQuery());
    }

    @Override
    public MD_Product_Serial getSerialInfo(long product_code, int serial_code) {
        MD_Product_SerialDao productSerialDao = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        return productSerialDao.getByString(new MD_Product_Serial_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code,
                serial_code).toSqlQuery());
    }

    @Override
    public int getViewMode(String move_type, int has_put_away) {

        switch (move_type) {
            case ConstantBaseApp.IO_PROCESS_MOVE:
            case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                return 0;
            case ConstantBaseApp.IO_INBOUND:
            case ConstantBaseApp.IO_OUTBOUND:
                return 1;
            case ConstantBaseApp.IO_PROCESS_IN_CONF:
                if(has_put_away == 0) {
                    return 2;
                }else{
                    return 3;
                }
            default:
                return -1;
        }

    }

    @Override
    public void onBackPressed(String actRequest) {
        switch (actRequest) {
            case ConstantBaseApp.ACT054:
            case ConstantBaseApp.ACT055:
                mView.callAct054();
                break;
            case ConstantBaseApp.ACT061:
                mView.callAct054();
                break;
            case ConstantBaseApp.ACT052:
            case ConstantBaseApp.ACT051:
            default:
                mView.callAct051();
        }
    }
}
