package com.namoadigital.prj001.ui.act019;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.sql.FCMMessage_Sql_001;
import com.namoadigital.prj001.ui.act018.Act018_Main_Presenter;
import com.namoadigital.prj001.ui.act018.Act018_Main_View;

import java.util.List;

/**
 * Created by neomatrix on 17/04/17.
 */

public class Act019_Main_Presenter_Impl implements Act019_Main_Presenter {

    private Context context;
    private Act018_Main_View mView;

    private FCMMessageDao fcmMessageDao;

    public Act019_Main_Presenter_Impl(Context context, Act018_Main_View mView, FCMMessageDao fcmMessageDao) {
        this.context = context;
        this.mView = mView;

        this.fcmMessageDao = fcmMessageDao;
    }

    @Override
    public void setAdapterData() {
        List<HMAux> fcmMessages = getFcmMessageList();

        mView.loadMessages(fcmMessages);
    }

    @Override
    public List<HMAux> getFcmMessageList() {
        List<HMAux> fcmMessageList =
                fcmMessageDao.query_HM(
                        new FCMMessage_Sql_001().toSqlQuery()
                );

        return fcmMessageList;
    }

    @Override
    public void addFormInfoToBundle(HMAux item) {

    }

    @Override
    public void onBackPressedClicked() {

    }
}
