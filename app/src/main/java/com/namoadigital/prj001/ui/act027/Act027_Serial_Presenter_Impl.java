package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_008;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by neomatrix on 12/07/17.
 */

public class Act027_Serial_Presenter_Impl implements Act027_Serial_Presenter {

    private Context context;
    private Act027_Serial_View mView;
    private MD_ProductDao mdProductDao;
    private Long product_code;
    private HMAux hmAux_Trans;
    private MD_Product_SerialDao serialDao;
    private SM_SO mSm_so;
    private MD_Product_Serial_TrackingDao trackingDao;
    private ArrayList<MD_Product_Serial_Tracking> tracking_list;

    public Act027_Serial_Presenter_Impl(Context context, Act027_Serial_View mView, MD_ProductDao mdProductDao, Long product_code, HMAux hmAux_Trans, MD_Product_SerialDao serialDao, SM_SO mSm_so, MD_Product_Serial_TrackingDao trackingDao, ArrayList<MD_Product_Serial_Tracking> tracking_list) {
        this.context = context;
        this.mView = mView;
        this.mdProductDao = mdProductDao;
        this.product_code = product_code;
        this.hmAux_Trans = hmAux_Trans;
        this.serialDao = serialDao;
        this.mSm_so = mSm_so;
        this.trackingDao = trackingDao;
        this.tracking_list = tracking_list;
    }

    @Override
    public void getProductInfo() {
        getSOProductInfoFlow();
    }

    private boolean isValidProduct(MD_Product md_product) {
        //Erro, produto não encontrado
        if (md_product != null && md_product.getProduct_code() > 0) {
            return true;
        }
        return false;
    }

    private void getSOProductInfoFlow() {
        MD_Product md_product = null;
        md_product = getMDProduct();
        //
        if (isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
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
                        mSm_so.getProduct_code()
                ).toSqlQuery()
        );
    }

    @Override
    public void executeSerialSave() {
        //
        mView.showPD(
                hmAux_Trans.get("progress_save_serial_ttl"),
                hmAux_Trans.get("progress_save_serial_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void getSerialInfo(Long product_code, int serial_code) {
        HMAux md_product_serial = serialDao.getByStringHM(
                new MD_Product_Serial_Sql_008(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_code
                ).toSqlQuery()
        );
        ///
        MD_Product_Serial serialObjDb = serialDao.getByString(
                new MD_Product_Serial_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_code
                ).toSqlQuery()
        );

        //
        //mView.setSerialValues(md_product_serial);
        mView.setSerialValuesV2(md_product_serial,serialObjDb);
    }

    @Override
    public void updateSerialInfo(MD_Product_Serial productSerial) {
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
        //if (ToolBox_Con.isOnline(context)) {
            //Chama consulta de S.O informando qe o serial precisa ser alterado.
            executeSerialSave();
        /*} else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }*/
    }

    @Override
    public void processSerialSaveResult(String product_code, int serial_code, HMAux hmSaveResult) {

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
                returnList.add(aux);
                //
                if (product_code.equals(pk[0])
                        && serial_code == ToolBox_Inf.convertStringToInt(pk[2])
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
            getSerialInfo(Long.valueOf(product_code), serial_code);
            //
            //if(returnList.size() == 1){
            if (returnList.size() == 1) {
                mView.showSingleResultMsg(ttl, msg);
            } else {
                mView.showSerialResults(returnList);
            }
        } else {
            mView.showSingleResultMsg(
                    hmAux_Trans.get("alert_save_serial_return_ttl"),
                    hmAux_Trans.get("alert_no_serial_return_msg")
            );
        }

    }

    @Override
    public void updateTrackingReference(ArrayList<MD_Product_Serial_Tracking> tracking_list) {
        this.tracking_list = tracking_list;
    }

    @Override
    public void processTrackingResult(HMAux auxResult, MD_Product_Serial serialObj) {
        if (auxResult.containsKey(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY)) {
            if (auxResult.get(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY).equals(WS_Serial_Tracking_Search.NOT_EXISTS)) {
                //
                tracking_list.add(
                        buildTrackingObj(serialObj, mView.getSearched_tracking())
                );
                //
                mView.appendTracking(mView.getSearched_tracking());
                //
                mView.setTrackingListChanged(true);
                //
                mView.cleanSearched_tracking();
                //
                mView.scrollToTracking();
            } else {
                mView.showAlertDialog(
                        hmAux_Trans.get("alert_tracking_unavailable_ttl"),
                        hmAux_Trans.get("alert_tracking_unavailable_msg")
                );
            }
        }
    }

    private MD_Product_Serial_Tracking buildTrackingObj(MD_Product_Serial serialObj, String searched_tracking) {
        MD_Product_Serial_Tracking auxTracking = new MD_Product_Serial_Tracking();
        //
        auxTracking.setTracking(searched_tracking);
        auxTracking.setPk(serialObj);
        //
        return auxTracking;
    }

    @Override
    public boolean isTrackingListed(String tracking) {
        for (int i = 0; i < tracking_list.size(); i++) {
            if (tracking_list.get(i).getTracking().equals(tracking)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {
        mView.setWsProcess(Act027_Main.WS_SEARCH_TRACKING);
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
}
