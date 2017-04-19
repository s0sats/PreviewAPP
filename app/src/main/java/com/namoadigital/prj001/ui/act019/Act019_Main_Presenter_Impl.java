package com.namoadigital.prj001.ui.act019;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.sql.FCMMessage_Sql_001;
import com.namoadigital.prj001.sql.FCMMessage_Sql_004;
import com.namoadigital.prj001.sql.FCMMessage_Sql_005;
import com.namoadigital.prj001.ui.act018.Act018_Main_Presenter;
import com.namoadigital.prj001.ui.act018.Act018_Main_View;

import java.util.List;

/**
 * Created by neomatrix on 17/04/17.
 */

public class Act019_Main_Presenter_Impl implements Act019_Main_Presenter {

    private Context context;
    private Act019_Main_View mView;

    private FCMMessageDao fcmMessageDao;

    public Act019_Main_Presenter_Impl(Context context, Act019_Main_View mView, FCMMessageDao fcmMessageDao) {
        this.context = context;
        this.mView = mView;

        this.fcmMessageDao = fcmMessageDao;
    }

    @Override
    public void setData(long fcmmessage_code) {
        FCMMessage fcmMessage = fcmMessageDao.getByString(
                new FCMMessage_Sql_005(
                        String.valueOf(fcmmessage_code)
                ).toSqlQuery()
        );

        mView.loadMessage(fcmMessage);

    }

    @Override
    public void modifyDBRead(long fcmmessage_code) {
        fcmMessageDao.addUpdate(
                new FCMMessage_Sql_004(String.valueOf(fcmmessage_code)).toSqlQuery()
        );
    }
}
