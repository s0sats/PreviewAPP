package com.namoadigital.prj001.ui.act058.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_Move_ReasonDao;
import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_001;
import com.namoadigital.prj001.sql.IO_Move_Reason_Sql_SS;
import com.namoadigital.prj001.sql.MD_Class_Sql_SS;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

class Act058_Main_Presenter implements Act058_Main_Contract.I_Presenter{
    IO_MoveDao moveDao;
    MD_Product_SerialDao productSerialDao;
    MD_ClassDao classDao;
    IO_MoveDao ioMoveDao;
    Context context;
    Act058_Main mView;
    HMAux hmAux_trans;

    public Act058_Main_Presenter(Context context, Act058_Main mView, HMAux hmAux_trans) {
        this.context = context;
        this.moveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.productSerialDao = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.ioMoveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.classDao = new MD_ClassDao(context);
        this.hmAux_trans = hmAux_trans;
        this.mView = mView;
    }

    @Override
    public IO_Move getMoveInfo(int movePrefix, int moveCode) {
        return  moveDao.getByString(new IO_Move_Order_Item_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                movePrefix,
                moveCode).toSqlQuery());
    }

    @Override
    public ArrayList<HMAux>  getClassList(){
        return (ArrayList<HMAux>) classDao.query_HM(new MD_Class_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
        ).toSqlQuery());
    }

    @Override
    public MD_Product_Serial getSerialInfo(long product_code, int serial_code){
        return productSerialDao.getByString(new MD_Product_Serial_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code,
                serial_code).toSqlQuery());
    }

    public ArrayList<HMAux> getMoveReasonList() {
        ArrayList<HMAux> moveReasonList = new ArrayList<>();

        IO_Move_ReasonDao ioMoveReasonDao =
                new IO_Move_ReasonDao(context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        moveReasonList = (ArrayList<HMAux>) ioMoveReasonDao.query_HM(
                new IO_Move_Reason_Sql_SS(ToolBox_Con.getPreference_Customer_Code(context))
                        .toSqlQuery());
        //

        //
        return moveReasonList;
    }

    @Override
    public void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {

        mView.setWs_process(WS_Serial_Tracking_Search.class.getName());
        //
        mView.showPD(
                hmAux_trans.get("progress_tracking_search_ttl"),
                hmAux_trans.get("progress_tracking_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Tracking_Search.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_SERIAL_CODE, String.valueOf(serial_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_TRACKING, tracking);
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_SITE_CODE, site_code);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
    @Override
    public int getViewMode(IO_Move moveInfo) {

        switch (moveInfo.getMove_type()) {
            case ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY:
            case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
                return 1;
            case ConstantBaseApp.IO_PROCESS_IN_CONF:
            case ConstantBaseApp.IO_PROCESS_OUT_CONF:
            case ConstantBaseApp.IO_PROCESS_MOVE:
            case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
            default:
                return 0;
        }

    }

    @Override
    public void executeMovePersistence(IO_Move io_move) {
        ioMoveDao.addUpdate(io_move);
    }
}
