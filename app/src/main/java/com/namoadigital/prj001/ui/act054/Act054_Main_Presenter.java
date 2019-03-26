package com.namoadigital.prj001.ui.act054;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.T_IO_Move_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Search;
import com.namoadigital.prj001.service.WS_IO_Move_Search;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.service.WS_IO_Move_Search.MOVE_ORIENTATION;

public class Act054_Main_Presenter implements Act054_Main_Contract.I_Presenter {

    private Context context;
    private Act054_Main_Contract.I_View mView;

    private MD_ProductDao productDao;
    private MD_Product_SerialDao serialDao;

    private HMAux hmAux_Trans;
    private MD_Product mdProduct;

    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;


    public Act054_Main_Presenter(Context context, Act054_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;

        this.serialDao = new MD_Product_SerialDao(context);
    }


    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act){
            case ConstantBaseApp.ACT051:
            default:
                mView.callAct051();
                break;
        }
    }

    @Override
    public void getMovements(boolean inboundStatus, boolean outboundStatus, boolean movePlannedStatus, String zone, boolean originStatus, boolean destinyStatus) {

        mView.setWsProcess(WS_IO_Move_Search.class.getName());

        mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
        );

        String moveType ="";
        String moveOrientation ="";

        moveType = getMoveTypeParams(inboundStatus, outboundStatus, movePlannedStatus, moveType);

        moveOrientation = getMoveOrientationParams(originStatus, destinyStatus, moveOrientation);

        Intent mIntent = new Intent(context, WBR_IO_Move_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(MD_SiteDao.SITE_CODE,ToolBox_Con.getPreference_Site_Code(context));
        bundle.putString(IO_MoveDao.MOVE_TYPE,moveType);
        bundle.putString(IO_MoveDao.FROM_ZONE_CODE, zone);
        bundle.putString(MOVE_ORIENTATION,moveOrientation);
        //
        mIntent.putExtras(bundle);
        context.sendBroadcast(mIntent);
}

    @Override
    public void processIOMoveSearch(String resultado) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        T_IO_Move_Search_Rec rec = gson.fromJson(
                resultado,
                T_IO_Move_Search_Rec.class
        );
        //
        ArrayList<IO_Move_Search_Record> record_list = rec.getRecord();

        mView.callAct055(record_list);
    }

    @Override
    public List<HMAux> getZoneList() {
        return
                new MD_Site_ZoneDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ).query_HM((
                        new MD_Site_Zone_Sql_SS(
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                ToolBox_Con.getPreference_Site_Code(context))

                        ).toSqlQuery()
                );
    }

    private String getMoveTypeParams(boolean inboundStatus, boolean outboundStatus, boolean movePlannedStatus, String moveType) {
        if(inboundStatus){
            moveType = "INBOUND";
        }
        moveType = addParamToString(outboundStatus, moveType, "OUTBOUND");
        moveType = addParamToString(movePlannedStatus, moveType, "MOVE_PLANNED");
        return moveType;
    }

    private String getMoveOrientationParams(boolean outboundStatus, boolean originStatus, String moveOrientation) {
        if(originStatus){
            moveOrientation = "ORIGIN";
        }
        moveOrientation = addParamToString(outboundStatus, moveOrientation, "DESTINY");
        return moveOrientation;
    }

    private String addParamToString(boolean cbStatus, String params, String param) {
        if (cbStatus) {
            params = addPipe(params) + param;
        }
        return params;
    }

    private String addPipe(String field) {
        if(!field.isEmpty()){
            field = field + "|";
        }
        return field;
    }
}
