package com.namoadigital.prj001.ui.act034;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by d.luche on 27/11/2017.
 */

public interface Act034_Main_View {

    void startReceivers(boolean start_stop);

    void callAct005(Context context);

    void callAct035(Context context, HMAux item);

    void showPD(String ttl,String msg);

    void disablePD();

    void startRoomInfoTask(String socket_id,String room_code);

    void startDownloadMemberImgTask(String[] imgUrlList);
}
