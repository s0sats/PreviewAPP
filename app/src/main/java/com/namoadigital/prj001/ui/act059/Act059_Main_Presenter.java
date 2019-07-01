package com.namoadigital.prj001.ui.act059;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.IO_Conf_TrackingDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Conf_Tracking;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Item_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.IO_Inbound_Item_Sql_006;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

import static com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create.DATE_FORMAT_MKDATE;

public class Act059_Main_Presenter implements Act059_Main_Contract.I_Presenter  {
    Context context;
    Act059_Main mView;
    HMAux hmAux_trans;
    IO_Inbound_ItemDao io_inbound_itemDao;

    public Act059_Main_Presenter(Context context, Act059_Main mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
        io_inbound_itemDao = new IO_Inbound_ItemDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public IO_Inbound_Item getInboudItem(Integer move_prefix, Integer move_code, Integer inbound_item) {
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
                mView.callAct061();
                break;
            case ConstantBaseApp.ACT052:
            case ConstantBaseApp.ACT051:
            default:
                mView.callAct051();
        }
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
    public void executeInConfPersistence(long customer_code, Integer io_prefix, Integer io_code, Integer to_zone_code, String to_zone_id, String to_zone_desc, Integer to_local_code, String to_local_id, String to_local_desc, Integer to_class_code, Integer reason_code, String comments, String done_date, MD_Product_Serial serial, IO_Inbound_Item item, List<IO_Move_Tracking> trackingFromMove) {
        item.setCustomer_code(customer_code);
        item.setInbound_prefix(io_prefix);
        item.setInbound_code(io_code);
        item.setZone_code(to_zone_code);
        item.setZone_id(to_zone_id);
        item.setZone_desc(to_zone_desc);
        item.setLocal_code(to_local_code);
        item.setClass_code(to_class_code);
        item.setConf_date(done_date);
        item.setProduct_code(serial.getProduct_code());
        item.setSerial_code((int) serial.getSerial_code());
        item.setSave_date(ToolBox.sDTFormat_Agora(DATE_FORMAT_MKDATE));
        item.getSerial().add(serial);
        item.setStatus(Constant.SYS_STATUS_WAITING_SYNC);
        item.setUpdate_required(1);
        item.setCustomer_code(customer_code);
        item.setComments(comments);


        DaoObjReturn daoObjReturnIoMove = io_inbound_itemDao.addUpdate(item);

        if (daoObjReturnIoMove.hasError()) {
            mView.showAlert(
                    hmAux_trans.get("alert_offline_save_error_ttl"),
                    hmAux_trans.get("alert_offline_save_error_msg")
            );
        }else {
            IO_Conf_TrackingDao io_conf_trackingdao = new IO_Conf_TrackingDao(context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM);
            DaoObjReturn objReturn = new DaoObjReturn();
            for (IO_Move_Tracking tracking : trackingFromMove) {
                IO_Conf_Tracking item_tracking = new IO_Conf_Tracking();
                item_tracking.setCustomer_code(tracking.getCustomer_code());
                item_tracking.setPrefix(item.getInbound_prefix());
                item_tracking.setCode(item.getInbound_code());
                item_tracking.setItem(item.getInbound_item());
                item_tracking.setType(ConstantBaseApp.IO_INBOUND);
                item_tracking.setTracking(tracking.getTracking());
                //ANALISAR O RETORNO E SE HOUVER ERRO NÃO PROSSEGUIR E EXIBIR MSG
                objReturn = io_conf_trackingdao.addUpdate(item_tracking);
            }

            if (ToolBox_Con.isOnline(context)) {
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

            } else {
                mView.showAlert(
                        hmAux_trans.get("alert_offline_save_ttl"),
                        hmAux_trans.get("alert_offline_save_msg")
                );
            }
        }


    }
}
