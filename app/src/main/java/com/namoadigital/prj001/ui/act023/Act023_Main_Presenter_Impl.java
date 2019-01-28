package com.namoadigital.prj001.ui.act023;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by d.luche on 22/06/2017.
 */

public class Act023_Main_Presenter_Impl implements Act023_Main_Presenter {

    private Context context;
    private Act023_Main mView;
    private String requesting_process;
    private Bundle bundle;
    private HMAux hmAux_Trans;
    private MD_ProductDao mdProductDao;
    private Long product_code;
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_TrackingDao trackingDao;

    public Act023_Main_Presenter_Impl(Context context, Act023_Main mView, String requesting_process, Bundle bundle, HMAux hmAux_Trans, MD_ProductDao mdProductDao, String product_code, MD_Product_SerialDao serialDao,MD_Product_Serial_TrackingDao trackingDao) {
        this.context = context;
        this.mView = mView;
        this.requesting_process = requesting_process;
        this.bundle = bundle;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = mdProductDao;
        this.product_code = Long.parseLong(product_code);
        this.serialDao = serialDao;
        this.trackingDao = trackingDao;
    }

    @Override
    public void getProductInfo() {
        MD_Product md_product = null;
        md_product = getMDProduct();
        //
        if (ToolBox_Inf.isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
            mView.setProductValues(null);
            //
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_product_not_found_title"),
                    hmAux_Trans.get("alert_product_not_found_msg")
            );
        }
    }

    private MD_Product getMDProduct() {

        return mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
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
        //Salva dados alterados do S.O
        serialDao.addUpdateTmp(mdProductSerial);
    }

    @Override
    public void defineBackFlow() {
        switch (requesting_process) {

            case Constant.MODULE_CHECKLIST:
                //mView.callAct008(context,product_code);
                mView.callAct022(context);
                break;
            case Constant.MODULE_SO_SEARCH_SERIAL:
                //mView.callAct025(context);
                mView.callAct021(context);
                break;
            case Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS:
                mView.callAct021(context);
                break;
            case Constant.MODULE_SO:
            default:
                mView.callAct022(context);
                break;

        }
    }

    @Override
    public void executeSerialSearch(Long product_code, String serial_id) {
        mView.setWs_process(WS_Serial_Search.class.getName());

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
    public void executeSerialSave() {
        mView.setWs_process(WS_Serial_Save.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_serial_search_ttl"),
                hmAux_Trans.get("progress_serial_search_msg")
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
    public void executeSoDownload(Long product_code, String serial_id) {
        mView.setWs_process(WS_SO_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_so_search_ttl"),
                hmAux_Trans.get("progress_so_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Search.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.WS_SO_SEARCH_PRODUCT_CODE, product_code);
        bundle.putString(Constant.WS_SO_SEARCH_SERIAL_ID, serial_id);
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
                    false);
        }
    }

    @Override
    public void processSoDownloadResult(HMAux so_download_result) {
        if (so_download_result.containsKey(WS_SO_Search.SO_PREFIX_CODE) && so_download_result.containsKey(WS_SO_Search.SO_LIST_QTY)) {
            if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
                //
                mView.showAlertDialog(
                        hmAux_Trans.get("alert_no_so_found_ttl"),
                        hmAux_Trans.get("alert_no_so_found_msg")
                );

            } else if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 1) {
                //
                if (so_download_result.get(WS_SO_Search.SO_PREFIX_CODE).contains(Constant.MAIN_CONCAT_STRING)) {
                    String[] so_prefix_code = so_download_result.get(WS_SO_Search.SO_PREFIX_CODE).split(Constant.MAIN_CONCAT_STRING);
                    Bundle bundleSingleSO = new Bundle();
                    //
                    bundleSingleSO.putString(SM_SODao.SO_PREFIX, so_prefix_code[0]);
                    bundleSingleSO.putString(SM_SODao.SO_CODE, so_prefix_code[1]);
                    //
                    mView.callAct027(context, bundleSingleSO);
                } else {
                    //SE NÃO TEM O PARAMETRO COM UNICA S.O BAIXADA, JOGA NA LISTA DE S.O
                    mView.callAct026(context);
                }
            } else {
                mView.callAct026(context);
            }
        } else {
            ToolBox_Inf.alertBundleNotFound(mView,hmAux_Trans);
        }
    }

    @Override
    public void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {
        mView.setWs_process( WS_Serial_Tracking_Search.class.getName());
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
    public void saveSerialInfo(MD_Product_Serial md_product_serial) {
        //Salva dados do serial
        serialDao.addUpdateTmp(md_product_serial);
    }

    @Override
    public void onBackPressedClicked() {
        defineBackFlow();
    }
}
