package com.namoadigital.prj001.ui.act046;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;

public class Act046_Main_Presenter implements Act046_Main_Contract.I_Presenter {

    private Context context;
    private Act046_Main_Contract.I_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private MD_Product_SerialDao serialDao;
    private MD_ProductDao mdProductDao;

    public Act046_Main_Presenter(Context context, Act046_Main_Contract.I_View mView, HMAux hmAux_Trans, MD_Product_SerialDao serialDao, MD_ProductDao mdProductDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.serialDao = serialDao;
        this.mdProductDao = mdProductDao;
    }


}
