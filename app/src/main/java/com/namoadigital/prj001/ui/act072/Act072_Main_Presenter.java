package com.namoadigital.prj001.ui.act072;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
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

    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct068();
    }
}
