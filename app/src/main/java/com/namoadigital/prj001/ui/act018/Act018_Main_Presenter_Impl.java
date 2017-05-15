package com.namoadigital.prj001.ui.act018;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.sql.FCMMessage_Sql_001;
import com.namoadigital.prj001.sql.FCMMessage_Sql_002;
import com.namoadigital.prj001.sql.FCMMessage_Sql_003;
import com.namoadigital.prj001.sql.FCMMessage_Sql_004;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.ui.act007.Act007_Main_View;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 17/04/17.
 */

public class Act018_Main_Presenter_Impl implements Act018_Main_Presenter {

    private Context context;
    private Act018_Main_View mView;

    private FCMMessageDao fcmMessageDao;

    public Act018_Main_Presenter_Impl(Context context, Act018_Main_View mView, FCMMessageDao fcmMessageDao) {
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
    public void modifyDBRead(long fcmmessage_code) {
        fcmMessageDao.addUpdate(
                new FCMMessage_Sql_004(String.valueOf(fcmmessage_code)).toSqlQuery()
        );
    }

    @Override
    public void addFormInfoToBundle(HMAux item) {

    }

    @Override
    public void onBackPressedClicked() {

    }
}
