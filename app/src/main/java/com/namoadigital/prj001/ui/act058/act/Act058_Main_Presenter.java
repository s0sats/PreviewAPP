package com.namoadigital.prj001.ui.act058.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_IO_Blind_Move_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Item_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_IO_Blind_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Save;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.IO_Blind_Move_Sql_002;
import com.namoadigital.prj001.sql.IO_Blind_Move_Sql_004;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

class Act058_Main_Presenter implements Act058_Main_Contract.I_Presenter {
    public static final String NEXT_TMP = "next_tmp";
    private IO_Blind_MoveDao blindMoveDao;
    private MD_Product_SerialDao productSerialDao;
    private IO_MoveDao ioMoveDao;
    private IO_Move_TrackingDao ioMoveTrackingDao;
    private Context context;
    private Act058_Main mView;
    private HMAux hmAux_trans;

    public Act058_Main_Presenter(Context context, Act058_Main mView, HMAux hmAux_trans) {
        this.context = context;
        this.blindMoveDao = new IO_Blind_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.productSerialDao = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.ioMoveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.ioMoveTrackingDao = new IO_Move_TrackingDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.hmAux_trans = hmAux_trans;
        this.mView = mView;
    }

    @Override
    public IO_Move getMoveInfo(int movePrefix, int moveCode) {
        return ioMoveDao.getByString(new IO_Move_Order_Item_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                movePrefix,
                moveCode).toSqlQuery());
    }


    @Override
    public MD_Product_Serial getSerialInfo(long product_code, int serial_code) {
        return productSerialDao.getByString(new MD_Product_Serial_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code,
                serial_code).toSqlQuery());
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
    public int getViewMode(String move_type) {

        switch (move_type) {
            case ConstantBaseApp.IO_INBOUND:
            case ConstantBaseApp.IO_OUTBOUND:
                return 1;
            case ConstantBaseApp.IO_PROCESS_MOVE:
            case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
            default:
                return 0;
        }

    }

    @Override
    public void executeMovePlannedPersistence(long customer_code, int move_prefix, int move_code, Integer to_zone_code, Integer to_local_code, Integer to_class_code, Integer reason_code, String comments, String done_date, MD_Product_Serial serial, IO_Move io_move, List<IO_Move_Tracking> trackingFromMove) {
        io_move.setCustomer_code(customer_code);
        io_move.setMove_prefix(move_prefix);
        io_move.setMove_code(move_code);
        io_move.setTo_zone_code(to_zone_code);
        io_move.setTo_local_code(to_local_code);
        io_move.setTo_class_code(to_class_code);
        io_move.setReason_code(reason_code);
        io_move.setDone_date(done_date);
        io_move.setProduct_code(serial.getProduct_code());
        io_move.setSerial_code((int) serial.getSerial_code());
        io_move.setDone_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        io_move.setDone_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        io_move.getSerial().add(serial);
        io_move.setStatus(Constant.SYS_STATUS_WAITING_SYNC);
        io_move.setCustomer_code(customer_code);
        io_move.setMove_prefix(move_prefix);
        io_move.setMove_code(move_code);
        if (!io_move.getMove_type().equals(ConstantBaseApp.IO_PROCESS_MOVE_PLANNED)) {
            io_move.setUpdate_required(1);
        }else{
            io_move.setUpdate_required(0);
        }

        DaoObjReturn daoObjReturnIoMove = ioMoveDao.addUpdate(io_move);

        if (daoObjReturnIoMove.hasError()) {
            mView.showAlert(
                    hmAux_trans.get("alert_offline_save_error_ttl"),
                    hmAux_trans.get("alert_offline_save_error_msg")
            );
        }else {
            boolean hasError =false;
            for (IO_Move_Tracking tracking : trackingFromMove) {
                DaoObjReturn daoObjReturnIoMoveTracking = ioMoveTrackingDao.addUpdate(tracking);
                if (daoObjReturnIoMoveTracking.hasError()) {
                    hasError =true;
                }
            }
//            if (hasError) {
//                mView.showAlert(
//                        hmAux_trans.get("alert_offline_save_tracking_error_ttl"),
//                        hmAux_trans.get("alert_offline_save_tracking_error_msg")
//                );
//            }
            if (ToolBox_Con.isOnline(context)) {
                switch (io_move.getMove_type()) {
                    case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                        callWS_IO_Move_Save();
                        break;
                    case ConstantBaseApp.IO_INBOUND:
                        Toast.makeText(context, ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY, Toast.LENGTH_SHORT).show();
//                        callWS_IO_Move_Save();
                        callWS_IO_Inbound_Item();
                        break;
                    case ConstantBaseApp.IO_OUTBOUND:
                        Toast.makeText(context, ConstantBaseApp.IO_PROCESS_OUT_PICKING, Toast.LENGTH_SHORT).show();
                        callWS_IO_Move_Save();
//                        callWS_IO_Outbound_Item(io_move);
                        break;
                }
            } else {
                mView.showAlert(
                        hmAux_trans.get("alert_offline_save_ttl"),
                        hmAux_trans.get("alert_offline_save_msg")
                );
            }
        }
    }

    private void callWS_IO_Move_Save() {
        mView.setWs_process(WS_IO_Move_Save.class.getName());
        //
        mView.showPD(
                hmAux_trans.get("dialog_save_move_ttl"),
                hmAux_trans.get("dialog_save_move_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_IO_Move_Save.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void callWS_IO_Inbound_Item() {

        mView.setWs_process(WS_IO_Inbound_Item_Save.class.getName());
        //
        mView.showPD(
                hmAux_trans.get("dialog_save_move_ttl"),
                hmAux_trans.get("dialog_save_move_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_IO_Inbound_Item_Save.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void callWS_IO_Outbound_Item(IO_Move io_move) {
        //TBD FAzer select de item, se tiver atualiza, caso contrario nao adicionar ou atualizar
//        IO_Outbound_Item item = new IO_Outbound_Item();
//        IO_Outbound_ItemDao io_outbound_itemDao = new IO_Outbound_ItemDao(context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM);
//        item.setCustomer_code(io_move.getCustomer_code());
//        item.setInbound_prefix(io_move.getInbound_prefix());
//        item.setInbound_code(io_move.getInbound_code());
//        item.setInbound_item(io_move.getInbound_item());
//        item.setProduct_code(io_move.getProduct_code());
//        item.setSerial_code(io_move.getSerial_code());
//        item.setStatus(io_move.getStatus());
//        io_outbound_itemDao.addUpdate(item);
        //todo mudar para serviço de outbound_item
        mView.setWs_process(WS_IO_Move_Save.class.getName());
        //
        mView.showPD(
                hmAux_trans.get("dialog_save_move_ttl"),
                hmAux_trans.get("dialog_save_move_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_IO_Move_Save.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void onBackPressed(String actRequest) {
        switch (actRequest) {
            case ConstantBaseApp.ACT054:
            case ConstantBaseApp.ACT055:
                mView.callAct054();
            case ConstantBaseApp.ACT052:
            case ConstantBaseApp.ACT051:
            default:
                mView.callAct051();
        }
    }

    @Override
    public IO_Blind_Move getMoveInfo(int blind_tmp, long product_code, String serial_id) {
        return blindMoveDao.getByString(new IO_Blind_Move_Sql_004(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        blind_tmp,
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
    }

    @Override
    public void executeMovePersistence(long customer_code, int blind_tmp, Integer zone_code, Integer local_code, Integer classCode, Integer reasonCode, String date_confirm, MD_Product_Serial serial, IO_Move movePlanned, List<IO_Move_Tracking> trackingFromMove) {
        IO_Blind_Move io_blind_move = new IO_Blind_Move();
        io_blind_move.setCustomer_code(customer_code);
        io_blind_move.setZone_code(zone_code);
        io_blind_move.setLocal_code(local_code);
        io_blind_move.setClass_code(classCode);
        io_blind_move.setReason_code(reasonCode);
        io_blind_move.setSave_date(date_confirm);
        io_blind_move.setSite_code(Integer.valueOf(ToolBox_Con.getPreference_Site_Code(context)));
        io_blind_move.setProduct_code(serial.getProduct_code());
        io_blind_move.setSerial_id(serial.getSerial_id());
        io_blind_move.setSerial_code((int) serial.getSerial_code());
        io_blind_move.setStatus(Constant.SYS_STATUS_WAITING_SYNC);
        io_blind_move.setBlind_tmp(blind_tmp);

        blindMoveDao.addUpdate(io_blind_move);

        IO_Blind_Move_TrackingDao ioBlindTrackingDao= new IO_Blind_Move_TrackingDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        IO_Blind_Move_Tracking blindMoveTracking = new IO_Blind_Move_Tracking();

        for (IO_Move_Tracking tracking : trackingFromMove) {
            blindMoveTracking.setCustomer_code(tracking.getCustomer_code());
            blindMoveTracking.setBlind_tmp(io_blind_move.getBlind_tmp());
            blindMoveTracking.setTracking(tracking.getTracking());
            ioBlindTrackingDao.addUpdate(blindMoveTracking);
        }

        if (ToolBox_Con.isOnline(context)) {
            mView.setWs_process(WS_IO_Blind_Move_Save.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_save_move_ttl"),
                    hmAux_trans.get("dialog_save_move_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Blind_Move_Save.class);
            Bundle bundle = new Bundle();
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showAlert(
                    hmAux_trans.get("alert_offline_save_ttl"),
                    hmAux_trans.get("alert_offline_save_msg")
            );
        }
    }
    @Override
    public int getBlindTmp() {
        List<HMAux> blind_move = blindMoveDao.query_HM(new IO_Blind_Move_Sql_002().toSqlQuery());

        if(blind_move == null || !blind_move.get(0).hasConsistentValue(NEXT_TMP)){
            return 1;
        }
        return Integer.valueOf(blind_move.get(0).get(NEXT_TMP));
    }

}
