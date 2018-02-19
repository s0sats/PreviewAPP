package com.namoadigital.prj001.ui.act035;

import android.content.Context;
import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act035_Main_View {

    void reloadMessages(ArrayList<HMAux> dados);

    void callAct034(Context context);

    void callCamera(int mId, int mType, String mFName, boolean mEdit, boolean mEnabled);

    //void startRoomPrivateWS(String user_code, String customer_code);
    void startRoomPrivateWS(String user_code, String customer_code, Integer active, @Nullable String room_code);


}
