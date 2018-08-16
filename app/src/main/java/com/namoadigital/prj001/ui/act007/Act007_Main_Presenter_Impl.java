package com.namoadigital.prj001.ui.act007;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.receiver.WBR_Serial_Log;
import com.namoadigital.prj001.service.WS_Serial_Log;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act007_Main_Presenter_Impl implements Act007_Main_Presenter {

    private Context context;
    private Act007_Main_View mView;
    private String file_name;
    private MD_Product_SerialDao productSerialDao;
    private HMAux hmAux_trans;

    public Act007_Main_Presenter_Impl(Context context, Act007_Main_View mView, MD_Product_SerialDao productSerialDao, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.productSerialDao = productSerialDao;
        this.hmAux_trans = hmAux_trans;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    @Override
    public void executeSerialLog(MD_Product_Serial mdProductSerial) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Log.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_serial_log_ttl"),
                    hmAux_trans.get("dialog_serial_log_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Log.class);
            Bundle bundle = new Bundle();
            //
            bundle.putLong(MD_Product_SerialDao.CUSTOMER_CODE, mdProductSerial.getCustomer_code());
            bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, mdProductSerial.getProduct_code());
            bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, mdProductSerial.getSerial_code());
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showNoConnecionMsg();
        }
    }

    @Override
    public void getLog() {
        File file = new File(Constant.TOKEN_PATH, file_name);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        try {
            if (file.exists()) {

                ArrayList<Serial_Log_Obj> logList = gson.fromJson(
                        ToolBox_Inf.getContents(file),
                        new TypeToken<ArrayList<Serial_Log_Obj>>() {
                        }.getType()
                );
                //
                if (logList != null && logList.size() > 0) {
                    mView.loadLogList(logList);
                } else {
                    mView.showEmptyLogMsg();
                }
                //
            } else {
                mView.showNoFileMsg();
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            //
            mView.showEmptyLogMsg();
        }

    }

    @Override
    public void deleteLogFile() {
        ToolBox_Inf.deleteFileListExceptionSafe(Constant.TOKEN_PATH, Constant.PREFIX_LOG_FILE_SERIAL);
    }

}
