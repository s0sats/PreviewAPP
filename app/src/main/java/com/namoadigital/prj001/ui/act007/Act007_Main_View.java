package com.namoadigital.prj001.ui.act007;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.model.Serial_Log_Obj;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act007_Main_View {

    void showPD(String title, String msg);

    void setWsProcess(String wsProcess);

    void showNoConnecionMsg();

    void showNoFileMsg();

    void showEmptyLogMsg();

    void loadLogList(ArrayList<Serial_Log_Obj> logList);

    void sendResult(Intent intent);

    void callAct026(Context context);

    void callAct027(Context context, Bundle bundle);
}
