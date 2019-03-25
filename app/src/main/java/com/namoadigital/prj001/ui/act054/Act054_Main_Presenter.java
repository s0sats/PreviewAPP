package com.namoadigital.prj001.ui.act054;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.T_IO_Serial_Process_Response;
import com.namoadigital.prj001.receiver.WBR_IO_Serial_Process_Search;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Search;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act054_Main_Presenter implements Act054_Main_Contract.I_Presenter {

    private Context context;
    private Act054_Main_Contract.I_View mView;

    private MD_ProductDao productDao;
    private MD_Product_SerialDao serialDao;

    private HMAux hmAux_Trans;
    private MD_Product mdProduct;

    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;


    public Act054_Main_Presenter(Context context, Act054_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;

        this.serialDao = new MD_Product_SerialDao(context);
    }


}
