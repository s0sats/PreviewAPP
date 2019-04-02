package com.namoadigital.prj001.ui.act058;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

class Act058_Main_Presenter implements Act058_Main_Contract.I_Presenter{
    IO_MoveDao moveDao;
    MD_Product_SerialDao productSerialDao;
    Context context;
    Act058_Main act058_main;

    public Act058_Main_Presenter(Context context, Act058_Main act058_main, HMAux hmAux_trans) {
        this.context = context;
        this.moveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.productSerialDao = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        this.act058_main = act058_main;
    }

    @Override
    public IO_Move getMoveInfo(int movePrefix, int moveCode) {
        return  moveDao.getByString(new IO_Move_Order_Item_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                movePrefix,
                moveCode).toSqlQuery());
    }

    @Override
    public void getSerialHistoric() {

    }
    @Override
    public MD_Product_Serial getSerialInfo(long product_code, int serial_code){
        return productSerialDao.getByString(new MD_Product_Serial_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code,
                serial_code).toSqlQuery());
    }
}
