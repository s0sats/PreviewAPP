package com.namoadigital.prj001.ui.act035;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act035_Main_View {

    void reloadMessages(ArrayList<HMAux> dados);

    void callAct034(Context context);

    void callAct038(Context context, HMAux hmAux);

    void callCamera(int mId, int mType, String mFName, boolean mEdit, boolean mEnabled);

    void startRoomPrivateWS(String user_code, String customer_code, Integer active, @Nullable String room_code);

    void showPD(String ttl, String msg);

    void updatePD(String ttl, String msg);

    void setWSProcess(String ws_process);

    void executeApSyncWsViaInfo(HMAux hmAux);

    void showAlert(String ttl, String msg);

    void callAct070(Bundle bundle);

    void callAct027(Context context);

    void cleanWsTmpItem();

    void showAlertWithAction(String ttl, String msg, DialogInterface.OnClickListener listener);
}
