package com.namoadigital.prj001.ui.act007;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.util.Constant;
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

    public Act007_Main_Presenter_Impl(Context context, Act007_Main_View mView, String file_name, MD_Product_SerialDao productSerialDao, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.file_name = file_name;
        this.productSerialDao = productSerialDao;
        this.hmAux_trans = hmAux_trans;
    }

    @Override
    public void getLog() {
        File file = new File(Constant.TOKEN_PATH,file_name);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        if(file.exists()){
            try {
                ArrayList<Serial_Log_Obj> logList = gson.fromJson(
                        ToolBox_Inf.getContents(file),
                        new TypeToken<ArrayList<Serial_Log_Obj>>() {
                        }.getType()
                );
                //
                if(logList != null && logList.size() > 0){
                    mView.loadLogList(logList);
                }
            }catch (JsonSyntaxException e){
                ToolBox_Inf.registerException(getClass().getName(),e);
                //ALERT????
            }
            //

        }else{
            //CASO ARQUIVO NÃO EXISTA
        }

    }

    @Override
    public void onBackPressedClicked() {
        ToolBox_Inf.deleteFileListExceptionSafe(Constant.TOKEN_PATH,Constant.PREFIX_LOG_FILE_SERIAL);
        //
        mView.callAct005(context);
    }
}
