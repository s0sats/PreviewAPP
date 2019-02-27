package com.namoadigital.prj001.ui.act025;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act025_Main_Presenter_Impl implements Act025_Main_Presenter {

    private Context context;
    private Act025_Main_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private MD_ProductDao productDao;

    public Act025_Main_Presenter_Impl(Context context, Act025_Main_View mView, HMAux hmAux_Trans, MD_ProductDao productDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.productDao = productDao;
    }

    @Override
    public void defineFlow(MD_Product_Serial productSerial) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL);
        bundle.putString(MD_ProductDao.PRODUCT_CODE, productSerial != null ? String.valueOf(productSerial.getProduct_code()) : null);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, String.valueOf(productSerial.getSerial_id()));
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, mView.isSerial_creation());
        //
        mView.callAct023(context, bundle);
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct021(context);
    }
}
