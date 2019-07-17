package com.namoadigital.prj001.ui.act014;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.sql.SM_SO_Sql_015;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_011;
import com.namoadigital.prj001.sql.Sql_Act014_001;
import com.namoadigital.prj001.sql.Sql_Act014_003;
import com.namoadigital.prj001.sql.Sql_Act014_004;
import com.namoadigital.prj001.sql.Sql_Act014_005;
import com.namoadigital.prj001.sql.Sql_Act014_006;
import com.namoadigital.prj001.sql.Sql_Act058_001;
import com.namoadigital.prj001.sql.Sql_Act058_007;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public class Act014_Main_Presenter_Impl implements Act014_Main_Presenter {

    private Context context;
    private Act014_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private SM_SODao sm_soDao;
    private SO_Pack_Express_LocalDao soPackExpressLocalDao;

    private HMAux hmAux_Trans;

    public Act014_Main_Presenter_Impl(Context context, Act014_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, SM_SODao sm_soDao, SO_Pack_Express_LocalDao soPackExpressLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.sm_soDao = sm_soDao;
        this.soPackExpressLocalDao = soPackExpressLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSentData() {
        ArrayList<HMAux> senListF =
                (ArrayList<HMAux>) customFormLocalDao.query_HM(
                        new Sql_Act014_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                hmAux_Trans
                        ).toSqlQuery()
                );
        ArrayList<HMAux> senList = new ArrayList<>();

        senList.addAll(senListF);
        //
        //FINALIZADOS FORM AP
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(context);
        List<HMAux> NFormAPHistoric = formApDao.query_HM(
                new Sql_Act014_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        hmAux_Trans
                ).toSqlQuery()
        );
        //
        senList.addAll(NFormAPHistoric);
        //
        //if (ToolBox_Inf.parameterExists(context, new String[]{Constant.PARAM_SO/*, Constant.PARAM_SO_MOV*/})) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO,null)) {
            ArrayList<HMAux> senListSO =
                    (ArrayList<HMAux>) sm_soDao.query_HM(
                            new SM_SO_Sql_015(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    hmAux_Trans
                            ).toSqlQuery()
                    );

            HMAux hmAuxSO = senListSO.get(0);
            HMAux hmAuxTotal = new HMAux();
            //
            if(ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO,Constant.PROFILE_MENU_SO_EXPRESS)) {
                ArrayList<HMAux> senListSOExpress =
                        (ArrayList<HMAux>) soPackExpressLocalDao.query_HM(
                                new SO_Pack_Express_Local_Sql_011(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        hmAux_Trans
                                ).toSqlQuery()
                        );

                HMAux hmAuxSOExpress = senListSOExpress.get(0);


                int mTotal = ToolBox_Inf.convertStringToInt(hmAuxSO.get(SM_SO_Sql_015.SENT_QTY)) +
                        ToolBox_Inf.convertStringToInt(hmAuxSOExpress.get(SM_SO_Sql_015.SENT_QTY));


                hmAuxTotal.put(SM_SO_Sql_015.TYPE, hmAuxSO.get(SM_SO_Sql_015.TYPE));
                hmAuxTotal.put(SM_SO_Sql_015.SENT_QTY, String.valueOf(mTotal));
            }else{
                hmAuxTotal = hmAuxSO;
            }

            senList.add(hmAuxTotal);

        }

        if(ToolBox_Inf.profileExists(context , Constant.PROFILE_MENU_IO , Constant.PROFILE_MENU_IO_PARAM_INBOUND)){

            IO_InboundDao ioInboundDao = new IO_InboundDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            List<HMAux> inboundHistoric = ioInboundDao.query_HM(
                    new Sql_Act014_004(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            hmAux_Trans
                    ).toSqlQuery()
            );
            //
            senList.addAll(inboundHistoric);
        }

        if(ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_IO , Constant.PROFILE_MENU_IO_PARAM_MOVE_REQUEST)){
            IO_MoveDao ioMovedDao = new IO_MoveDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            List<HMAux> moveHistoric = ioMovedDao.query_HM(
                    new Sql_Act014_005(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            hmAux_Trans
                    ).toSqlQuery()
            );
            //
            senList.addAll(moveHistoric);
        }

        if(ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_IO ,Constant.PROFILE_MENU_IO_PARAM_OUTBOUND)){
            IO_OutboundDao ioOutboundDao = new IO_OutboundDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            List<HMAux> outboundHistoric = ioOutboundDao.query_HM(
                    new Sql_Act014_006(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            hmAux_Trans
                    ).toSqlQuery()
            );
            //
            senList.addAll(outboundHistoric);
        }


        //
        mView.loadSentData(senList);
    }

    @Override
    public void checkSentFlow(HMAux item) {

        if (!item.get(Sql_Act014_001.SENT_QTY).equalsIgnoreCase("0")) {

            if (item.get(Sql_Act014_001.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_CHECKLIST))) {
                mView.callAct015(context);
            }

            if (item.get(Sql_Act014_001.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_OS))) {
                mView.callAct032(context);
            }

            if (item.get(Sql_Act014_003.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_FORM_AP))) {
                mView.callAct039(context);
            }

            if (item.get(Sql_Act014_003.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_IO_INBOUND))) {
                mView.callAct057(context);
            }

            if (item.get(Sql_Act014_003.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_IO_MOVE))) {

                Bundle bundle = new Bundle();

                ArrayList<IO_Move_Search_Record> searchRecords = new ArrayList<>();

                setBundle(bundle, searchRecords);

                mView.callAct055(context, bundle);
            }

            if (item.get(Sql_Act014_003.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_IO_OUTBOUND))) {
                mView.callAct066(context);

            }

        } else {
            mView.showMsg();
        }
    }

    private void setBundle(Bundle bundle, ArrayList<IO_Move_Search_Record> searchRecords) {

        setMoveListForBundle(bundle, searchRecords);
        bundle.putBoolean(ConstantBaseApp.FROM_HISTORIC, true);
        bundle.putBoolean(ConstantBaseApp.IS_LOCAL_PROCESS, true);
        bundle.putSerializable(Constant.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT014);

    }

    private void setMoveListForBundle(Bundle bundle, ArrayList<IO_Move_Search_Record> searchRecords) {
        IO_MoveDao moveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        ArrayList<HMAux> moveOrders = (ArrayList<HMAux>) moveDao.query_HM(
                new Sql_Act058_007(ToolBox_Con.getPreference_Customer_Code(context)).toSqlQuery()
        );

        for (HMAux move : moveOrders) {
            IO_Move_Search_Record aux = getHmAuxToMoveSearchRecord(move);
            if (aux != null) {
                searchRecords.add(aux);
            }
        }
        bundle.putSerializable(Constant.MAIN_WS_LIST_VALUES, searchRecords);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
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
}
