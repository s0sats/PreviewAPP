package com.namoadigital.prj001.ui.act022;

import android.content.Context;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;


/**
 * Created by d.luche on 22/06/2017.
 */

public class Act022_Main_Presenter implements Act022_Main_Contract.I_Presenter {

    private Context context;
    private Act022_Main_Contract.I_View mView;
    private MD_ProductDao mdProductDao;

    public Act022_Main_Presenter(Context context, Act022_Main_Contract.I_View mView, MD_ProductDao mdProductDao) {
        this.context = context;
        this.mView = mView;
        this.mdProductDao = mdProductDao;
    }

    @Override
    public MD_Product getMD_Produt(String product_code) {
        return null;
    }

    @Override
    public void processValidation(String product_code_org, String serial_id_org, String product_code_inf, String serial_id_inf) {
        if (product_code_inf.trim().length() != 0){
            if (product_code_org.equalsIgnoreCase(product_code_inf) && serial_id_org.equalsIgnoreCase(serial_id_inf)){
                mView.sendReturn("OK");
            } else {
                mView.showMSG();
            }
        } else {
            if (serial_id_org.equalsIgnoreCase(serial_id_inf)){
                mView.sendReturn("OK");
            } else {
                mView.showMSG();
            }

        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.sendReturn("");
    }
}
