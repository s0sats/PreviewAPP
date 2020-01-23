package com.namoadigital.prj001.ui.act072;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act072_Main_Presenter implements Act072_Main_Contract.I_Presenter {

    private Context context;
    private Act072_Main mView;
    private HMAux hmAuxTrans;
    private MD_ProductDao mdProductDao;

    public Act072_Main_Presenter(Context context, Act072_Main mView, HMAux hmAuxTrans) {
        this.context = context;
        this.mView = mView;
        this.hmAuxTrans = hmAuxTrans;
        //
        initDaos();
    }

    private void initDaos() {
        mdProductDao = new MD_ProductDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void defineFlow(MD_Product_Serial productSerial) {
        mView.callAct073(
            getAct073Bundle(productSerial)
        );
    }

    private Bundle getAct073Bundle(MD_Product_Serial productSerial) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL);
        bundle.putString(MD_ProductDao.PRODUCT_CODE, productSerial != null ? String.valueOf(productSerial.getProduct_code()) : null);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, String.valueOf(productSerial.getSerial_id()));
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, false);
        //
        return bundle;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct068();
    }
}
