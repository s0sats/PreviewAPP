package com.namoadigital.prj001.ui.act073;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Search;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_TK_Ticket_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

public class Act073_Main_Presenter implements Act073_Main_Contract.I_Presenter {
    private Context context;
    private Act073_Main mView;
    private HMAux hmAux_Trans;
    private long product_code;
    private MD_ProductDao mdProductDao;
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_TrackingDao trackingDao;

    public Act073_Main_Presenter(Context context, Act073_Main mView, HMAux hmAux_Trans,long product_code) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.product_code = product_code;
        //
        initDaos();
    }

    private void initDaos() {
        mdProductDao = new MD_ProductDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        serialDao = new MD_Product_SerialDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        trackingDao = new MD_Product_Serial_TrackingDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void getProductInfo(long product_code) {
        MD_Product md_product = getMDProduct();
        //
        if (ToolBox_Inf.isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
            mView.setProductValues(null);
        }
    }

    @Override
    public void defineBackFlow() {

    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct068();
    }

    @Override
    public void executeSerialSearch(Long product_code, String serial_id) {

    }

    @Override
    public void saveSerialInfo(MD_Product_Serial md_product_serial) {
        serialDao.addUpdateTmp(md_product_serial);
    }

    @Override
    public void executeTicketDownload(long productCode, long serialCode, String serialId) {
        mView.setWsProcess(WS_TK_Ticket_Search.class.getName());

        mView.showPD(
            hmAux_Trans.get("dialog_search_ticket_ttl"),
            hmAux_Trans.get("dialog_search_ticket_start")
        );
        //

        Intent mIntent = new Intent(context, WBR_TK_Ticket_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(productCode));
        bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(serialCode));
        //bundle.putString(MD_Product_SerialDao.SERIAL_ID, serialId);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public MD_Product getMDProduct() {
        return mdProductDao.getByString(
            new MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery()
        );
    }

    @Override
    public void executeSerialSearch(long product_code, String serial_id) {
        mView.setWsProcess(WS_Serial_Search.class.getName());

        mView.showPD(
            hmAux_Trans.get("dialog_serial_search_ttl"),
            hmAux_Trans.get("dialog_serial_search_start")
        );

        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        bundle.putBoolean(Constant.WS_SERIAL_SEARCH_SAVE_PROCESS, true);
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult) {
        if (hmSaveResult.size() > 0) {
            ArrayList<HMAux> returnList = new ArrayList<>();
            String ttl = "";
            String msg = "";
            //
            for (Map.Entry<String, String> item : hmSaveResult.entrySet()) {
                HMAux aux = new HMAux();
                String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                String status = item.getValue();

                MD_Product mdProduct = mdProductDao.getByString(
                    new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        Long.parseLong(pk[0])
                    ).toSqlQuery()
                );
                //
                if (mdProduct != null) {
                    aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mdProduct.getProduct_code() + " - " + mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc());
                }
                aux.put(Generic_Results_Adapter.VALUE_ITEM_2, pk[1]);
                aux.put(Generic_Results_Adapter.VALUE_ITEM_3, status);
                aux.put(MD_Product_SerialDao.PRODUCT_CODE, pk[0]);
                returnList.add(aux);
                //
                if (product_code == Long.parseLong(pk[0])
                    && serial_id.equals(pk[1])
                ) {

                    if (status.equals("OK")) {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_ok_msg");
                    } else {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_error_msg") + "\n" + status;

                    }
                }
            }
            //Atualiza dados dos serial na tela e spinners
            mView.refreshUI();
            //
            //Atualiza dados dos serial na tela e spinners
            refreshMdProductSerialReference(product_code, serial_id);
            //
            //if(returnList.size() == 1){
            if (returnList.size() == 1) {
                boolean returnOk = false;
                if( returnList.get(0).hasConsistentValue(Generic_Results_Adapter.VALUE_ITEM_3)
                    && returnList.get(0).get(Generic_Results_Adapter.VALUE_ITEM_3).equals("OK")
                ){
                    returnOk = true;
                }
                //
                mView.showSingleResultMsg(ttl, msg, returnOk);
            } else {
                mView.showSerialResults(returnList);
            }
        } else {
            mView.showSingleResultMsg(
                hmAux_Trans.get("alert_save_serial_return_ttl"),
                hmAux_Trans.get("alert_no_serial_return_msg"),
                false
            );
        }
    }

    @Override
    public void processTicketDownload(HMAux hmAux) {
        if(hmAux != null && hmAux.size() > 0 ){
            int qtyReturned =
                hmAux.hasConsistentValue(WS_TK_Ticket_Search.RETURNED_TICKET_QTY)
                    ? ToolBox_Inf.convertStringToInt(hmAux.get(WS_TK_Ticket_Search.RETURNED_TICKET_QTY))
                    : 0 ;
            if(qtyReturned == 0){
                mView.showAlert(
                    hmAux_Trans.get("alert_no_ticket_found_ttl"),
                    hmAux_Trans.get("alert_no_ticket_found_msg")
                );
            }else if(qtyReturned == 1){
                if(hmAux.hasConsistentValue(TK_TicketDao.TICKET_PREFIX)
                    && hmAux.hasConsistentValue(TK_TicketDao.TICKET_CODE)
                ) {
                    //Chama Act com a pk do ticket.
                    mView.callAct070(
                        buildAct070Bundle(hmAux)
                    );
                }else{
                    mView.showAlert(
                        hmAux_Trans.get("alert_ticket_params_not_found_ttl"),
                        hmAux_Trans.get("alert_ticket_params_not_found_msg")
                    );
                }
            }else{
                mView.callAct069();
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_invalid_ticket_return_ttl"),
                hmAux_Trans.get("alert_invalid_ticket_return_msg")
            );
        }
    }

    private Bundle buildAct070Bundle(HMAux hmAux) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT073);
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.TICKET_PREFIX)));
        bundle.putInt(TK_TicketDao.TICKET_CODE, ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.TICKET_CODE)));
        return bundle;
    }

    private void refreshMdProductSerialReference(long product_code, String serial_id) {
        mView.updateProductSerialValues(
            getSerialInfo(
                product_code,
                serial_id
            )
        );
        //
        mView.refreshUI();
    }

    private MD_Product_Serial getSerialInfo(long product_code, String serial_id) {
        MD_Product_Serial serialObjDb = serialDao.getByString(
            new MD_Product_Serial_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code,
                serial_id
            ).toSqlQuery()
        );
        //
        return serialObjDb;
    }

    @Override
    public void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {
        mView.setWsProcess( WS_Serial_Tracking_Search.class.getName());
        //
        mView.showPD(
            hmAux_Trans.get("progress_tracking_search_ttl"),
            hmAux_Trans.get("progress_tracking_search_msg")
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
    public void updateSerialData(MD_Product_Serial mdProductSerial) {
        //Remove os tracking para reinserir os que ficaram
        trackingDao.remove(new
            MD_Product_Serial_Tracking_Sql_002(
                mdProductSerial.getCustomer_code(),
                mdProductSerial.getProduct_code(),
                mdProductSerial.getSerial_tmp()
            ).toSqlQuery()
        );
        //Salva dados alterados
        serialDao.addUpdateTmp(mdProductSerial);
    }

    @Override
    public void executeSerialSave() {
        mView.setWsProcess(WS_Serial_Save.class.getName());
        //
        mView.showPD(
            hmAux_Trans.get("dialog_serial_save_ttl"),
            hmAux_Trans.get("dialog_serial_save_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void extractSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
            result,
            TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        if(serial_list != null){
            if(serial_list.size() == 0){
                mView.reApplySerialIdToFrag();
            }else if(serial_list.size() == 1){
                mView.applyReceivedSerialToFrag(serial_list.get(0));
            }else{
                //FUDEU
            }
        }else{
            //FUDEU 2
        }
    }


    //region InterfacesSemAcao
    @Override
    public void searchLocalSerial(long product_code, String serial_id) {
        //Não existe busca offline nesse caso.
    }

    @Override
    public void executeAddressSuggestion(String site_code, long product_code) {
        //Não existe nesse caso.
    }

    @Override
    public void processAddresSuggestionResult(String result) {
        //Não existe nesse caso.
    }
    //endregion
}
