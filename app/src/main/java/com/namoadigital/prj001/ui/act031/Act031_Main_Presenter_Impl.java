package com.namoadigital.prj001.ui.act031;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act031_Main_Presenter_Impl implements Act031_Main_Presenter{


    private Context context;
    private Act031_Main_View mView;
    private HMAux hmAux_Trans;
    private MD_ProductDao mdProductDao;
    private MD_Product_SerialDao serialDao;
    private long product_code;
    private String bundle_serial_id;


    public Act031_Main_Presenter_Impl(Context context, Act031_Main_View mView, HMAux hmAux_Trans, MD_ProductDao mdProductDao, MD_Product_SerialDao serialDao, long product_code, String bundle_serial_id) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = mdProductDao;
        this.serialDao = serialDao;
        this.product_code = product_code;
        this.bundle_serial_id = bundle_serial_id;
    }

    /**
     * Fluxo de Serial quando
     * @param serial
     */
    @Override
    public void serialFlow(String serial) {

        if(hasSerial(serial)){

            if(ToolBox_Con.isOnline(context)) {
                mView.showPD(
                        hmAux_Trans.get("progress_serial_search_ttl"),
                        hmAux_Trans.get("progress_serial_search_msg")
                );
                //
                executeSerialSearch(product_code, serial);
            }else{
                ToolBox_Inf.showNoConnectionDialog(context);
            }

        }else{
            mView.fieldFocus();
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_no_serial_typed_title"),
                    hmAux_Trans.get("alert_no_serial_typed_msg")
            );
        }
    }

    public boolean hasSerial(String serial) {
        //Verifica se Serial foi preenchido
        if (serial.trim().length() > 0){
            return true;
        }
        return false;
    }

    public void executeSerialSearch(Long product_code, String serial_id) {
        mView.setWs_process(Act031_Main.SO_WS_SEARCH_SERIAL);

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
    public void getProductInfo() {
        MD_Product md_product = null;
        md_product = mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
        //
        if(md_product.getCustomer_code() > 0 ){
            mView.setProductValues(md_product);
        }else{
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_product_not_found_title"),
                    hmAux_Trans.get("alert_product_not_found_msg")
            );
        }


    }

    @Override
    public void onBackPressedClicked() {

    }

    @Override
    public void executeSoSearch(Long product_code, String serial_id, boolean save_serial) {

    }

    @Override
    public void getSerialInfo(Long product_code, String serial_id) {
        HMAux md_product_serial = serialDao.getByStringHM(
                new MD_Product_Serial_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        mView.setSerialValues(md_product_serial);

    }


    @Override
    public void updateSerialInfo(MD_Product_Serial productSerial) {
        //Salva dados alterados do S.O
        serialDao.addUpdate(productSerial);
        if(ToolBox_Con.isOnline(context)) {
            //Chama consulta de S.O informando qe o serial precisa ser alterado.
            executeSoSearch(productSerial.getProduct_code(), productSerial.getSerial_id(), true);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }
}
