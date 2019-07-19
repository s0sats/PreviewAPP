package com.namoadigital.prj001.ui.act052;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_IO_Serial_Process_Download;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Download;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act052_Main_Presenter implements Act052_Main_Contract.I_Presenter {
    private Context context;
    private Act052_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private DialogInterface.OnClickListener otherSiteClickListner;

    public Act052_Main_Presenter(Context context, final Act052_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

    private DialogInterface.OnClickListener getOtherSiteClickListner(){
        if(otherSiteClickListner == null) {
            otherSiteClickListner = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mView.getSerialJump()) {
                        onBackPressedClicked();
                    }
                }
            };
        }
        //
        return otherSiteClickListner;
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct051();
    }

    @Override
    public void defineIOSerialFlow(HMAux hmAuxRet) {
        if(hmAuxRet.hasConsistentValue(Constant.HMAUX_PROCESS_KEY)) {
            String processType = hmAuxRet.get(Constant.HMAUX_PROCESS_KEY);
            //
            Bundle bundle = new Bundle();

            switch (processType) {
                case ConstantBaseApp.IO_PROCESS_IN_CONF:
                    bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD,Act061_Main.INBOUND_FRAG_ITEM);
                    bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_INBOUND);
                    bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, hmAuxRet.get(Constant.HMAUX_PREFIX_KEY));
                    bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, hmAuxRet.get(Constant.HMAUX_CODE_KEY));
                    bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, hmAuxRet.get(MD_Product_SerialDao.PRODUCT_CODE));
                    bundle.putString(MD_Product_SerialDao.SERIAL_CODE, hmAuxRet.get(MD_Product_SerialDao.SERIAL_CODE));
                    //bundle.putString(MD_Product_SerialDao.SERIAL_ID, hmAuxRet.get(MD_Product_SerialDao.SERIAL_ID));
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                    mView.callAct061(bundle);
                    break;
                case ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY:
                case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
                case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                    bundle.putString(IO_MoveDao.MOVE_PREFIX, hmAuxRet.get(Constant.HMAUX_PREFIX_KEY));
                    bundle.putString(IO_MoveDao.MOVE_CODE, hmAuxRet.get(Constant.HMAUX_CODE_KEY));
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                    mView.callAct058(bundle);
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE:

                    bundle.putInt(MD_Product_SerialDao.PRODUCT_CODE, Integer.parseInt(hmAuxRet.get(MD_Product_SerialDao.PRODUCT_CODE)));
                    bundle.putInt(MD_Product_SerialDao.SERIAL_CODE, Integer.parseInt(hmAuxRet.get(MD_Product_SerialDao.SERIAL_CODE)));
                    if(hmAuxRet.get(Constant.HMAUX_PLANNED_ZONE_CODE_KEY).isEmpty()) {
                        bundle.putInt(IO_Blind_MoveDao.ZONE_CODE,-1 );
                    }else{
                        bundle.putInt(IO_Blind_MoveDao.ZONE_CODE, Integer.parseInt(hmAuxRet.get(Constant.HMAUX_PLANNED_ZONE_CODE_KEY)));
                    }

                    if(hmAuxRet.get(Constant.HMAUX_PLANNED_LOCAL_CODE_KEY).isEmpty()) {
                        bundle.putInt(IO_Blind_MoveDao.LOCAL_CODE,-1 );
                    }else{
                        bundle.putInt(IO_Blind_MoveDao.LOCAL_CODE, Integer.parseInt(hmAuxRet.get(Constant.HMAUX_PLANNED_LOCAL_CODE_KEY)));
                    }

                    if(hmAuxRet.get(Constant.HMAUX_PLANNED_CLASS_CODE_KEY).isEmpty()) {
                        bundle.putInt(IO_Blind_MoveDao.CLASS_CODE,-1 );
                    }else{
                        bundle.putInt(IO_Blind_MoveDao.CLASS_CODE, Integer.parseInt(hmAuxRet.get(Constant.HMAUX_PLANNED_CLASS_CODE_KEY)));
                    }

                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                    mView.callAct058(bundle);
                    //callact058
                    break;
//                case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
//                    Toast.makeText(context, "OUT_PICKING", Toast.LENGTH_SHORT).show();
//                    //callact058
//                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_CONF:
                    bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD, Act067_Main.OUTBOUND_FRAG_ITEM);
                    bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_OUTBOUND);
                    bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, hmAuxRet.get(Constant.HMAUX_PREFIX_KEY));
                    bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, hmAuxRet.get(Constant.HMAUX_CODE_KEY));
                    bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, hmAuxRet.get(MD_Product_SerialDao.PRODUCT_CODE));
                    bundle.putString(MD_Product_SerialDao.SERIAL_CODE, hmAuxRet.get(MD_Product_SerialDao.SERIAL_CODE));
                    //bundle.putString(MD_Product_SerialDao.SERIAL_ID, hmAuxRet.get(MD_Product_SerialDao.SERIAL_ID));
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                    mView.callAct067(bundle);
                    break;
            }
        }
    }

    @Override
    public void executeWsProcessDownload(IO_Serial_Process_Record data) {
        if(ToolBox_Con.isOnline(context)){
            mView.setWsProcess(WS_IO_Serial_Process_Download.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_process_download_ttl"),
                    hmAux_Trans.get("dialog_process_download_starting_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Serial_Process_Download.class);
            Bundle bundle = new Bundle();

            bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(data.getProduct_code()));
            bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(data.getSerial_code()));
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
        }else{
            setFlowByProcessStatus(data);

//            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private void setFlowByProcessStatus(IO_Serial_Process_Record data) {

        String processType = data.getProcess_type();

        if(processType == null){
            processType = "";
        }

        Bundle bundle = new Bundle();
        switch (processType) {
            case ConstantBaseApp.IO_PROCESS_IN_CONF:
                IO_Inbound_Item inbound_item = getInboundItem(data);
                if(inbound_item != null) {
                    bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD,Act061_Main.INBOUND_FRAG_ITEM);
                    bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_INBOUND);
                    bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(inbound_item.getInbound_prefix()));
                    bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(inbound_item.getInbound_code()));
                    bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(inbound_item.getProduct_code()));
                    bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(inbound_item.getSerial_code()));
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                    mView.callAct061(bundle);
                } else{
                    ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_inbound_not_found_ttl"),
                        hmAux_Trans.get("alert_inbound_not_found_msg"),
                        null,
                        0
                    );
                }
                break;
            case ConstantBaseApp.IO_PROCESS_OUT_CONF:
                IO_Outbound_Item outboundItem = getOutboundItem(data);
                //
                if(outboundItem != null) {
                    bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD, Act067_Main.OUTBOUND_FRAG_ITEM);
                    bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_OUTBOUND);
                    bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(outboundItem.getOutbound_prefix()));
                    bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(outboundItem.getOutbound_code()));
                    bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(outboundItem.getProduct_code()));
                    bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(outboundItem.getSerial_code()));
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT052);
                    mView.callAct067(bundle);
                } else{
                    ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_outbound_not_found_ttl"),
                        hmAux_Trans.get("alert_outbound_not_found_msg"),
                        null,
                        0
                    );
                }
                break;
            case ConstantBaseApp.SYS_STATUS_PUT_AWAY:
            case ConstantBaseApp.SYS_STATUS_PICKING:
            case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                IO_Move io_move = getMove(data);
                bundle.putString(IO_MoveDao.MOVE_PREFIX, String.valueOf(io_move.getMove_prefix()));
                bundle.putString(IO_MoveDao.MOVE_CODE, String.valueOf(io_move.getMove_code()));
                bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                mView.callAct058(bundle);
                break;
            case ConstantBaseApp.IO_PROCESS_MOVE:
                MD_Product_Serial serial = getSerial(data);
                bundle.putInt(MD_Product_SerialDao.PRODUCT_CODE, (int) serial.getProduct_code());
                bundle.putInt(MD_Product_SerialDao.SERIAL_CODE, (int) serial.getSerial_code());
                try {
                    bundle.putInt(MD_Product_SerialDao.CLASS_CODE, (int) serial.getClass_code());
                }catch (Exception e ){
                    e.printStackTrace();
                    bundle.putInt(MD_Product_SerialDao.CLASS_CODE, -1);
                }
                bundle.putInt(IO_Blind_MoveDao.ZONE_CODE,-1 );
                bundle.putInt(IO_Blind_MoveDao.LOCAL_CODE,-1 );
                bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                mView.callAct058(bundle);
                break;
            default:

        }

    }

    private IO_Move getMove(IO_Serial_Process_Record data) {
        IO_MoveDao moveDao = new IO_MoveDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        IO_Move item = moveDao.getByString(
                new IO_Move_Order_Item_Sql_011(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        (int) data.getProduct_code(),
                        data.getSerial_code()
                ).toSqlQuery()
        );
        return item;

    }

    private IO_Inbound_Item getInboundItem(IO_Serial_Process_Record data) {

        IO_Inbound_ItemDao itemDao = new IO_Inbound_ItemDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        IO_Inbound_Item item = itemDao.getByString(
                new IO_Inbound_Item_Sql_011(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        (int) data.getProduct_code(),
                        data.getSerial_code()
                ).toSqlQuery()
        );
        return item;
    }

    private IO_Outbound_Item getOutboundItem(IO_Serial_Process_Record data) {

        IO_Outbound_ItemDao itemDao = new IO_Outbound_ItemDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        IO_Outbound_Item item = itemDao.getByString(
            new IO_Outbound_Item_Sql_011(
                ToolBox_Con.getPreference_Customer_Code(context),
                (int) data.getProduct_code(),
                data.getSerial_code()
            ).toSqlQuery()
        );
        //
        return item;
    }

    private MD_Product getMd_product(String mProduct_id) {
        MD_ProductDao mdProductDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        return mdProductDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        "",
                        mProduct_id
                ).toSqlQuery()
        );
    }

    @Override
    public void createNewSerialFlow(String mProduct_id, String mSerial_id) {
        MD_Product md_product = getMd_product(mProduct_id);
        MD_Product_Serial productSerial = md_product.createNewSerialForThisProduct(mSerial_id);
        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID,productSerial.getSerial_id());
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT052);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
        //
        mView.callAct053(bundle);
    }

    @Override
    public void processListItem(IO_Serial_Process_Record data) {
        //Nova logica - 09/07/2019
        //Se o process é null, serial não tem vinculo com I.O e tb não esta armazenado
        if(data.getProcess_type() == null){
            if(isSiteInboundAutoCreation()) {
                /**
                 * PONTO DE PROBLEMA, NESSE CASO NÃO TEM O OBJ SERIAL ENTÃO NÃO HÁ COMO
                 * CONTINUAR PARA EDIÇÃO O SERIAL.
                 */
                //Segue para tela de edição de serial
                editNonLocationSerial(data);

            }else {
                mView.showAlert(
                    hmAux_Trans.get("alert_serial_not_stored_ttl"),
                    hmAux_Trans.get("alert_serial_not_stored_msg"),
                    getOtherSiteClickListner()
                );
            }
        }else{
            //Já que possui process, valida se existe site
            if(data.getSite_code() != null){
                if(data.getSite_code() != Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context)) ){
                    mView.showAlert(
                        hmAux_Trans.get("alert_serial_out_site_title"),
                        hmAux_Trans.get("alert_serial_out_site_msg"),
                        getOtherSiteClickListner()
                        );
                } else{
                    executeWsProcessDownload(data);
                }
            } else {
                if(data.getProcess_type().equals(ConstantBaseApp.IO_PROCESS_IN_CONF)) {
                    executeWsProcessDownload(data);
                }else {
                    mView.showAlert(
                        hmAux_Trans.get("alert_serial_out_site_title"),
                        hmAux_Trans.get("alert_serial_out_site_msg"),
                        getOtherSiteClickListner()
                    );
                }
            }
        }
    }

    @Override
    public void editNonLocationSerial(IO_Serial_Process_Record record) {
        Bundle bundle = new Bundle();
        MD_Product_Serial serial = getSerial(record);
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(record.getProduct_code()));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID,record.getSerial_id());
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT052);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, false);
        //
        mView.callAct053(bundle);
    }


    private MD_Product_Serial getSerial(IO_Serial_Process_Record item) {
        MD_Product_Serial serial = null;
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        try {
            serial = serialDao.getByString(
                    new MD_Product_Serial_Sql_009(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            item.getProduct_code(),
                            item.getSerial_code()).toSqlQuery()
            );
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        //
        return serial;
    }

    /**
     * Verifica se site logado permite Inbound_auto_create
     * @return Inbound_auto_create
     */
    @Override
    public boolean isSiteInboundAutoCreation() {
        MD_SiteDao mdSiteDao = new MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        MD_Site mdSite = mdSiteDao.getByString(
                new MD_Site_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );
        //
        if(mdSite != null && mdSite.getInbound_auto_create() == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasCreateSerialPermission(String mProduct_id, String mSerial_id, boolean serial_jump) {
        MD_Product md_product = getMd_product(mProduct_id);
        return md_product != null
                && md_product.getLocal_control() == 1
                && md_product.getIo_control() == 1
                && mSerial_id != null
                && !mSerial_id.isEmpty()
                && !serial_jump
                && ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, Constant.PROFILE_PRJ001_PRODUCT_SERIAL_PARAM_EDIT)
                && isSiteInboundAutoCreation()
                && ToolBox_Con.isOnline(context);
    }

    @Override
    public void callBlindMove(String mProduct_id, String mSerial_id) {
        MD_Product md_product = getMd_product(mProduct_id);
        Bundle bundle = new Bundle();
        bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, md_product.getProduct_code());
        bundle.putString(MD_Product_SerialDao.PRODUCT_ID, md_product.getProduct_id());
        bundle.putString(MD_Product_SerialDao.SERIAL_ID,mSerial_id);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT052);
        //
        mView.callAct064(bundle);
    }
}
