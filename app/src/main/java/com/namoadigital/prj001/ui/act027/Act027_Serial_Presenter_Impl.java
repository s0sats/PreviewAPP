package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_001;
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
    private HMAux data;

    public Act027_Serial_Presenter_Impl(Context context, Act027_Serial_View mView, MD_ProductDao mdProductDao, Long product_code, HMAux hmAux_Trans, MD_Product_SerialDao serialDao, HMAux data) {
        this.context = context;
        this.mView = mView;
        this.mdProductDao = mdProductDao;
        this.product_code = product_code;
        this.hmAux_Trans = hmAux_Trans;
        this.serialDao = serialDao;
        this.data = data;
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
                        Long.parseLong(data.get("product_code"))
                ).toSqlQuery()
        );
    }

    @Override
    public void executeSerialSave(Long product_code, String serial_id, boolean save_serial) {
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
        if (ToolBox_Con.isOnline(context)) {
            //Chama consulta de S.O informando qe o serial precisa ser alterado.
            executeSerialSave(productSerial.getProduct_code(), productSerial.getSerial_id(), true);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processSerialSaveResult(String product_code, String serial_id, HMAux hmSaveResult) {

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
                        && serial_id.equalsIgnoreCase(pk[1])
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
            getSerialInfo(Long.valueOf(product_code), serial_id);
            //
            //if(returnList.size() == 1){
            if(returnList.size() == 1){
                mView.showSingleResultMsg(ttl,msg);
            }else{
                mView.showSerialResults(returnList);
            }
        }else{
            mView.showSingleResultMsg(
                    hmAux_Trans.get("alert_save_serial_return_ttl"),
                    hmAux_Trans.get("alert_no_serial_return_msg")
            );
        }


    }

}
