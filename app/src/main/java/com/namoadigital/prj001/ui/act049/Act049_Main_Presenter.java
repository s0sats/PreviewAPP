package com.namoadigital.prj001.ui.act049;

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
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

public class Act049_Main_Presenter implements Act049_Main_Contract.I_Presenter {

    private Context context;
    private Act049_Main_Contract.I_View mView;
    private long product_code;
    private HMAux hmAux_Trans;
    private MD_ProductDao mdProductDao;
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_TrackingDao trackingDao;

    public Act049_Main_Presenter(Context context, Act049_Main_Contract.I_View mView, long product_code, HMAux hmAux_Trans, MD_ProductDao mdProductDao, MD_Product_SerialDao serialDao, MD_Product_Serial_TrackingDao trackingDao) {
        this.context = context;
        this.mView = mView;
        this.product_code = product_code;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = mdProductDao;
        this.serialDao = serialDao;
        this.trackingDao = trackingDao;
    }


    @Override
    public void getProductInfo() {
        MD_Product md_product = null;
        md_product = mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
        //
        if (ToolBox_Inf.isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
            //
            mView.setProductValues(null);
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct040(context);
    }

    @Override
    public void executeSerialSearch(Long product_code, String product_id, String serial_id) {
        mView.setWs_process(WS_Serial_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, "");
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSerialSave() {
        mView.setWs_process(WS_Serial_Save.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_serial_save_ttl"),
                hmAux_Trans.get("progress_serial_save_msg")
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
    public void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {
        mView.setWs_process(WS_Serial_Tracking_Search.class.getName());
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
    public void updateSerialData(MD_Product_Serial productSerial) {
        //Remove os tracking para reinserir os que ficaram
        trackingDao.remove(new
                MD_Product_Serial_Tracking_Sql_002(
                        productSerial.getCustomer_code(),
                        productSerial.getProduct_code(),
                        productSerial.getSerial_tmp()
                ).toSqlQuery()
        );
        //Salva dados alterados do S.O
        serialDao.addUpdateTmp(productSerial);
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
                //Chave adiciona para validação posterior
                aux.put(MD_ProductDao.PRODUCT_CODE, pk[0]);
                //
                aux.put(Generic_Results_Adapter.VALUE_ITEM_2, pk[1]);
                aux.put(Generic_Results_Adapter.VALUE_ITEM_3, status);

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
            refreshMdProductSerialReference(product_code, serial_id);
            //
            //if(returnList.size() == 1){
            if (returnList.size() == 1) {
                mView.showSingleResultMsg(ttl, msg,true);
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

    public MD_Product_Serial getSerialInfo(Long product_code, String serial_id) {
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
    public void searchLocalSerial(long product_code, String serial_id) {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        //
        MD_Product_Serial objSerial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        if (objSerial != null && objSerial.getSerial_code() > -1) {
            mView.applyReceivedSerialToFrag(objSerial);
        } else {
            mView.reApplySerialIdToFrag();
        }
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
        if (serial_list != null) {
            if (serial_list.size() == 0) {
                mView.reApplySerialIdToFrag();
            } else if (serial_list.size() == 1) {
                mView.applyReceivedSerialToFrag(serial_list.get(0));
            } else {
                //FUDEU
                ToolBox_Inf.registerException(getClass().getName(),new Exception("extractSearchResult more than 1 serial returned"));
            }
        } else {
            //FUDEU 2
            ToolBox_Inf.registerException(getClass().getName(),new Exception("extractSearchResult no serial returned"));
        }
    }
}
