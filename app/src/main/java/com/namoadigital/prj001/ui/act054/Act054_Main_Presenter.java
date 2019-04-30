package com.namoadigital.prj001.ui.act054;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.T_IO_Move_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Item_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Search;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Search;
import com.namoadigital.prj001.sql.IO_Inbound_Item_Sql_011;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_001;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_002;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_005;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.sql.Sql_Act058_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.namoadigital.prj001.service.WS_IO_Move_Search.MOVE_ORIENTATION;


public class Act054_Main_Presenter implements Act054_Main_Contract.I_Presenter {

    private Context context;
    private Act054_Main_Contract.I_View mView;

    private MD_ProductDao productDao;
    private MD_Product_SerialDao serialDao;
    private IO_MoveDao moveDao;
    private HMAux hmAux_Trans;
    private MD_Product mdProduct;

    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;

    private int pending_qty;



    private int waitingSyncMovePendency;
    private int waitingSyncPutAwayPendency;

    public Act054_Main_Presenter(Context context, Act054_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.pending_qty = 0;
        this.waitingSyncMovePendency = 0;

        this.serialDao = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        this.moveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
    }


    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act) {
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
                hmAux_Trans.get("dialog_move_order_search_ttl"),
                hmAux_Trans.get("dialog_move_order_search_start")
        );

        String moveType = "";
        String moveOrientation = "";

        moveType = getMoveTypeParams(inboundStatus, outboundStatus, movePlannedStatus, moveType);

        moveOrientation = getMoveOrientationParams(originStatus, destinyStatus, moveOrientation);

        Intent mIntent = new Intent(context, WBR_IO_Move_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
        bundle.putString(IO_MoveDao.MOVE_TYPE, moveType);
        bundle.putString(IO_MoveDao.FROM_ZONE_CODE, zone);
        bundle.putString(MOVE_ORIENTATION, moveOrientation);
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

        if (record_list.isEmpty()) {
            mView.showMsg(
                    hmAux_Trans.get("alert_move_order_not_found_ttl"),
                    hmAux_Trans.get("alert_move_order_not_found_msg")
            );
            mView.refreshPendencyCount();
        } else {
            callMoveOrderList(record_list);
        }
    }

    private void callMoveOrderList(ArrayList<IO_Move_Search_Record> record_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.MAIN_WS_LIST_VALUES, record_list);
        bundle.putSerializable(Constant.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT054);
        mView.callAct055(bundle);
    }

    @Override
    public void getPendenciesList() {
        ArrayList<IO_Move_Search_Record> searchRecords = new ArrayList<>();
        ArrayList<HMAux> moveOrders = (ArrayList<HMAux>) moveDao.query_HM(
                new Sql_Act058_001(ToolBox_Con.getPreference_Customer_Code(context)).toSqlQuery()
        );
        for (HMAux move : moveOrders) {
            IO_Move_Search_Record aux = getHmAuxToMoveSearchRecord(move);
            if (aux != null) {
                searchRecords.add(aux);
            }
        }
        callMoveOrderList(searchRecords);
    }

    private IO_Move_Search_Record getHmAuxToMoveSearchRecord(HMAux move) {
        IO_Move_Search_Record record = new IO_Move_Search_Record();

        try {

            if (move.hasConsistentValue(IO_MoveDao.CUSTOMER_CODE) && !move.get(IO_MoveDao.CUSTOMER_CODE).isEmpty()) {
                record.setCustomer_code(Integer.parseInt(move.get(IO_MoveDao.CUSTOMER_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.MOVE_PREFIX) && !move.get(IO_MoveDao.MOVE_PREFIX).isEmpty()) {
                record.setMove_prefix(Integer.parseInt(move.get(IO_MoveDao.MOVE_PREFIX)));
            }

            if (move.hasConsistentValue(IO_MoveDao.MOVE_CODE) && !move.get(IO_MoveDao.MOVE_CODE).isEmpty()) {
                record.setMove_code(Integer.parseInt(move.get(IO_MoveDao.MOVE_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.MOVE_TYPE) && !move.get(IO_MoveDao.MOVE_TYPE).isEmpty()) {
                record.setMove_type(move.get(IO_MoveDao.MOVE_TYPE));
            }

            if (move.hasConsistentValue(IO_MoveDao.PLANNED_LOCAL_CODE) && !move.get(IO_MoveDao.PLANNED_LOCAL_CODE).isEmpty()) {
                record.setPlanned_local_code(Integer.valueOf(move.get(IO_MoveDao.PLANNED_LOCAL_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.PLANNED_LOCAL_ID)) {
                record.setPlanned_local_id(move.get(IO_MoveDao.PLANNED_LOCAL_ID));
            }

            if (move.hasConsistentValue(IO_MoveDao.PLANNED_CLASS_CODE) && !move.get(IO_MoveDao.PLANNED_CLASS_CODE).isEmpty()) {
                record.setPlanned_class_code(Integer.valueOf(move.get(IO_MoveDao.PLANNED_CLASS_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.PLANNED_ZONE_CODE) && !move.get(IO_MoveDao.PLANNED_ZONE_CODE).isEmpty()) {
                record.setPlanned_zone_code(Integer.valueOf(move.get(IO_MoveDao.PLANNED_ZONE_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.PLANNED_ZONE_ID)) {
                record.setPlanned_zone_id(move.get(IO_MoveDao.PLANNED_ZONE_ID));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.ZONE_CODE) && !move.get(MD_Product_SerialDao.ZONE_CODE).isEmpty()) {
                record.setZone_code(Integer.valueOf(move.get(MD_Product_SerialDao.ZONE_CODE)));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.ZONE_DESC)) {
                record.setZone_desc(move.get(MD_Product_SerialDao.ZONE_DESC));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.ZONE_ID)) {
                record.setZone_id(move.get(MD_Product_SerialDao.ZONE_ID));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.LOCAL_CODE) && !move.get(MD_Product_SerialDao.LOCAL_CODE).isEmpty()) {
                record.setLocal_code(Integer.valueOf(move.get(MD_Product_SerialDao.LOCAL_CODE)));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.LOCAL_ID)) {
                record.setLocal_id(move.get(MD_Product_SerialDao.LOCAL_ID));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.PRODUCT_DESC)) {
                record.setProduct_desc(move.get(MD_Product_SerialDao.PRODUCT_DESC));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.PRODUCT_ID)) {
                record.setProduct_id(move.get(MD_Product_SerialDao.PRODUCT_ID));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.PRODUCT_CODE) && !move.get(IO_MoveDao.PRODUCT_CODE).isEmpty()) {
                record.setProduct_code(Integer.parseInt(move.get(MD_Product_SerialDao.PRODUCT_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.SERIAL_CODE) && !move.get(IO_MoveDao.SERIAL_CODE).isEmpty()) {
                record.setSerial_code(Integer.parseInt(move.get(IO_MoveDao.SERIAL_CODE)));
            }

            if (move.hasConsistentValue(MD_Product_SerialDao.SERIAL_ID) && !move.get(MD_Product_SerialDao.SERIAL_ID).isEmpty()) {
                record.setSerial_id(move.get(MD_Product_SerialDao.SERIAL_ID));
            }

            if (move.hasConsistentValue(IO_MoveDao.SITE_CODE)) {
                record.setSite_code(move.get(IO_MoveDao.SITE_CODE));
            }

            if (move.hasConsistentValue(IO_MoveDao.INBOUND_CODE) && !move.get(IO_MoveDao.INBOUND_CODE).isEmpty()) {
                record.setInbound_code(Integer.valueOf(move.get(IO_MoveDao.INBOUND_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.INBOUND_ITEM) && !move.get(IO_MoveDao.INBOUND_ITEM).isEmpty()) {
                record.setInbound_item(Integer.valueOf(move.get(IO_MoveDao.INBOUND_ITEM)));
            }

            if (move.hasConsistentValue(IO_MoveDao.INBOUND_PREFIX) && !move.get(IO_MoveDao.INBOUND_PREFIX).isEmpty()) {
                record.setInbound_prefix(Integer.valueOf(move.get(IO_MoveDao.INBOUND_PREFIX)));
            }

            if (move.hasConsistentValue(IO_MoveDao.OUTBOUND_CODE) && !move.get(IO_MoveDao.OUTBOUND_CODE).isEmpty()) {
                record.setOutbound_code(Integer.valueOf(move.get(IO_MoveDao.OUTBOUND_CODE)));
            }

            if (move.hasConsistentValue(IO_MoveDao.OUTBOUND_ITEM) && !move.get(IO_MoveDao.OUTBOUND_ITEM).isEmpty()) {
                record.setOutbound_item(Integer.valueOf(move.get(IO_MoveDao.OUTBOUND_ITEM)));
            }

            if (move.hasConsistentValue(IO_MoveDao.OUTBOUND_PREFIX) && !move.get(IO_MoveDao.OUTBOUND_PREFIX).isEmpty()) {
                record.setOutbound_prefix(Integer.valueOf(move.get(IO_MoveDao.OUTBOUND_PREFIX)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
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

    @Override
    public String getPendecies() {
        IO_MoveDao io_moveDao = new IO_MoveDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        HMAux resultPending = io_moveDao.getByStringHM((
                        new IO_Move_Order_Item_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        )
                ).toSqlQuery()
        );

        HMAux resultMoveWaitingSync = io_moveDao.getByStringHM((
                        new IO_Move_Order_Item_Sql_005(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ConstantBaseApp.IO_PROCESS_MOVE_PLANNED
                        )
                ).toSqlQuery()
        );

        HMAux resultInputAwayWaitingSync = io_moveDao.getByStringHM((
                        new IO_Move_Order_Item_Sql_005(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ConstantBaseApp.IO_INBOUND
                        )
                ).toSqlQuery()
        );

        pending_qty = 0;

        pending_qty = getPendencyCounterFromHmaux(resultPending);
        waitingSyncMovePendency = getPendencyCounterFromHmaux(resultMoveWaitingSync);
        waitingSyncPutAwayPendency = getPendencyCounterFromHmaux(resultInputAwayWaitingSync);
        pending_qty = pending_qty + waitingSyncMovePendency+waitingSyncPutAwayPendency;
        return "(" + pending_qty + ")";
    }

    private int getPendencyCounterFromHmaux(HMAux result) {
        int pendencies = 0;
        if (result != null && result.hasConsistentValue(IO_MoveDao.PENDING_QTY)) {
            try {
                pendencies = Integer.valueOf(result.get(IO_MoveDao.PENDING_QTY));
            } catch (Exception e) {
                pendencies = 0;
                e.printStackTrace();
            }
        }
        return pendencies;
    }

    @Override
    public void syncMovements() {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWs_process(WS_IO_Move_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_save_move_ttl"),
                    hmAux_Trans.get("dialog_save_move_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Move_Save.class);
            Bundle bundle = new Bundle();
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_offline_search_title"),
                    hmAux_Trans.get("alert_offline_search_msg"),
                    null,
                    0
            );
        }

    }



    private String getMoveTypeParams(boolean inboundStatus, boolean outboundStatus, boolean movePlannedStatus, String moveType) {
        if (inboundStatus) {
            moveType = "INBOUND";
        }
        moveType = addParamToString(outboundStatus, moveType, "OUTBOUND");
        moveType = addParamToString(movePlannedStatus, moveType, "MOVE_PLANNED");
        return moveType;
    }

    private String getMoveOrientationParams(boolean outboundStatus, boolean originStatus, String moveOrientation) {
        if (originStatus) {
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
        if (!field.isEmpty()) {
            field = field + "|";
        }
        return field;
    }

    public boolean hasPending_qty() {
        return waitingSyncMovePendency > 0;
    }


    @Override
    public void executeWsSaveItem() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Inbound_Item_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_save_inbound_item_ttl"),
                    hmAux_Trans.get("progress_save_inbound_item_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Inbound_Item_Save.class);
            Bundle bundle = new Bundle();
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processItemSaveReturn(int mPrefix, int mCode, String jsonRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn> actReturnList = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        try{
            actReturnList  = gson.fromJson(
                    jsonRet,
                    new TypeToken<ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn>>(){
                    }.getType() );
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        //
        if(actReturnList != null && actReturnList.size() > 0){
            boolean inboundResult = true;
            int inboundNextIdx = 0;
            HMAux auxResult = new HMAux();
            //Monta lista por inbound
            for (WS_IO_Inbound_Item_Save.InboundItemSaveActReturn actReturn : actReturnList) {
                String inboundCode = "";
                //
                if(actReturn.isMove()){
                    IO_Move ioMove =
                            moveDao.getByString(
                                    new IO_Move_Order_Item_Sql_001(
                                            actReturn.getCustomer_code(),
                                            actReturn.getPrefix(),
                                            actReturn.getCode()
                                    ).toSqlQuery()
                            );
                    if(ioMove != null){
                        inboundCode = ioMove.getInbound_prefix()+"."+ioMove.getInbound_code();
                    }
                }else{
                    inboundCode = actReturn.getPrefix() +"."+actReturn.getCode();
                }
                if(!auxResult.containsKey(inboundCode)
                        ||(auxResult.containsKey(inboundCode)
                        && !actReturn.getRetStatus().equals("OK")
                )
                ) {
                    auxResult.put(inboundCode, actReturn.getRetStatus());
                }
            }
            //For no resumido por inbound montando msg a ser exibida
            for(Map.Entry<String, String> item : auxResult.entrySet()){

                HMAux hmAux = new HMAux();
                //
                //Monta HmAux
                hmAux.put("title", hmAux_Trans.get("planned_move_lbl") );
                hmAux.put("label", item.getKey());
                hmAux.put("status",item.getValue());
                //
                resultList.add(hmAux);


            }
            //
            mView.showResult(resultList);
        }
    }
    @Override
    public boolean hasWaitingSyncMovePendency() {
        return waitingSyncMovePendency>0;
    }
    @Override
    public boolean hasWaitingSyncPutAwayPendency() {
        return waitingSyncPutAwayPendency >0;
    }

}
