package com.namoadigital.prj001.ui.act048;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

public class Act048_Main_Presenter implements Act048_Main_Contract.I_Presenter {

    private Context context;
    private Act048_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private MD_ProductDao mdProductDao;
    private MD_Product mdProduct;

    public Act048_Main_Presenter(Context context, Act048_Main_Contract.I_View mView, HMAux hmAux_Trans, MD_ProductDao mdProductDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = mdProductDao;
    }

    @Override
    public void getProductInfo(long product_code) {
        mdProduct = mdProductDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        String.valueOf(product_code),
                        ""
                ).toSqlQuery()
        );
        //
        if(mdProduct == null || mdProduct.getProduct_code() < 0){
            mView.showMsg(
                    hmAux_Trans.get("alert_product_not_found_ttl"),
                    hmAux_Trans.get("alert_product_not_found_msg")
            );
        }else{
            mView.setProductObj(mdProduct);
        }
    }

    @Override
    public void checkSerialListJump(ArrayList<MD_Product_Serial> serialList) {
        if(serialList != null && serialList.size() > 0){
            mView.loadSerialList(serialList);
        }
    }

    @Override
    public void prepareEditionParams(MD_Product_Serial productSerial, boolean new_serial) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, productSerial.getSerial_id());
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT048);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, new_serial);
        //
        mView.callAct049(context, bundle);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct040(context);
    }
}
