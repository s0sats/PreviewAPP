package com.namoadigital.prj001.ui.act055;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Download;
import com.namoadigital.prj001.service.WS_IO_Move_Download;
import com.namoadigital.prj001.sql.Sql_Act058_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act055_Main_Presenter implements Act055_Main_Contract.I_Presenter {

    private Context context;
    private Act055_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;

    public Act055_Main_Presenter(Context context, Act055_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act){
            case ConstantBaseApp.ACT054:
                mView.callAct054();
                break;
            case ConstantBaseApp.ACT014:
                mView.callAct014();
                break;
            case ConstantBaseApp.ACT012:
                mView.callAct012();
                break;
            default:
                mView.callAct054();
                break;
        }
    }

    @Override
    public void getDownloadedMove(String moveCode){
        mView.setWsProcess(WS_IO_Move_Download.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_download_move_ttl"),
                hmAux_Trans.get("dialog_download_move_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_IO_Move_Download.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(IO_MoveDao.MOVE_CODE, moveCode);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void processSearchReturn(HMAux searchRet) {

        try {
            if(searchRet.hasConsistentValue(Constant.HMAUX_PREFIX_KEY)
              && searchRet.hasConsistentValue(Constant.HMAUX_CODE_KEY)
            && searchRet.hasConsistentValue(Constant.HMAUX_PROCESS_KEY)){

                Bundle bundle = new Bundle();
                bundle.putString(IO_MoveDao.MOVE_PREFIX, searchRet.get(Constant.HMAUX_PREFIX_KEY));
                bundle.putString(IO_MoveDao.MOVE_CODE, searchRet.get(Constant.HMAUX_CODE_KEY));

                mView.callAct058(bundle);
            }else{
                mView.showAlert(
                        hmAux_Trans.get("alert_no_move_found_ttl"),
                        hmAux_Trans.get("alert_no_move_found_msg")
                );
            }
        }catch (Exception e){
            mView.showAlert(
                    hmAux_Trans.get("alert_error_on_processing_return_ttl"),
                    hmAux_Trans.get("alert_error_on_processing_return_msg")
            );
            //Gerar Exception ?!
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }

    @Override
    public ArrayList<IO_Move_Search_Record> getPendenciesList() {
        ArrayList<IO_Move_Search_Record> searchRecords = new ArrayList<>();
        IO_MoveDao moveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        ArrayList<HMAux> moveOrders = (ArrayList<HMAux>) moveDao.query_HM(
                new Sql_Act058_001(ToolBox_Con.getPreference_Customer_Code(context)).toSqlQuery()
        );
        for (HMAux move : moveOrders) {
            IO_Move_Search_Record aux = getHmAuxToMoveSearchRecord(move);
            if (aux != null) {
                searchRecords.add(aux);
            }
        }
        return searchRecords;
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
    public void getOfflineMove(String moveKey) {

    }
}
