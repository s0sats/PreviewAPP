package com.namoadigital.prj001.ui.act032;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.receiver.WBR_SO_Approval;
import com.namoadigital.prj001.sql.SM_SO_Sql_007;
import com.namoadigital.prj001.sql.SM_SO_Sql_008;
import com.namoadigital.prj001.util.Constant;

import java.io.File;
import java.util.HashMap;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act032_Main_Presenter_Impl implements Act032_Main_Presenter {

    private Context context;
    private Act032_Main_View mView;
    private HMAux hmAux_Trans;

    private SM_SODao soDao;

    public Act032_Main_Presenter_Impl(Context context, Act032_Main_View mView, HMAux hmAux_Trans, SM_SODao soDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.soDao = soDao;
    }

    @Override
    public void onProcessApproval(HashMap<String, String> data) {
        soDao.addUpdate(
                new SM_SO_Sql_007(
                        Long.parseLong(data.get(SM_SODao.CUSTOMER_CODE)),
                        Integer.parseInt(data.get(SM_SODao.SO_PREFIX)),
                        Integer.parseInt(data.get(SM_SODao.SO_CODE)),
                        ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z")
                ).toSqlQuery()
        );

        callApproval(
                Integer.parseInt(data.get(SM_SODao.SO_PREFIX)),
                Integer.parseInt(data.get(SM_SODao.SO_CODE)),
                null,
                null
        );
    }

    @Override
    public void onProcessNFCPassWord(HashMap<String, String> data, String nfc, String password) {
        soDao.addUpdate(
                new SM_SO_Sql_007(
                        Long.parseLong(data.get(SM_SODao.CUSTOMER_CODE)),
                        Integer.parseInt(data.get(SM_SODao.SO_PREFIX)),
                        Integer.parseInt(data.get(SM_SODao.SO_CODE)),
                        ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z")
                ).toSqlQuery()
        );

        callApproval(
                Integer.parseInt(data.get(SM_SODao.SO_PREFIX)),
                Integer.parseInt(data.get(SM_SODao.SO_CODE)),
                nfc,
                password
        );

    }

    @Override
    public void onProcessSignature(HashMap<String, String> data, String sFile) {

        if (sFile.trim().length() != 0) {

            File ssFile = new File(Constant.CACHE_PATH_PHOTO + "/" + sFile);
            if (ssFile.exists()) {

                soDao.addUpdate(
                        new SM_SO_Sql_008(
                                Long.parseLong(data.get(SM_SODao.CUSTOMER_CODE)),
                                Integer.parseInt(data.get(SM_SODao.SO_PREFIX)),
                                Integer.parseInt(data.get(SM_SODao.SO_CODE)),
                                ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"),
                                ssFile.getName(),
                                ssFile.getName(),
                                "USER"
                        ).toSqlQuery()
                );

                callApproval(
                        Integer.parseInt(data.get(SM_SODao.SO_PREFIX)),
                        Integer.parseInt(data.get(SM_SODao.SO_CODE)),
                        null,
                        null
                );

            } else {
                mView.errorMsg(
                        hmAux_Trans.get("alert_no_signature_title"),
                        hmAux_Trans.get("alert_no_signature_msg")
                );
            }
        } else {
            mView.errorMsg(
                    hmAux_Trans.get("alert_no_signature_title"),
                    hmAux_Trans.get("alert_no_signature_msg")
            );
        }
    }

    private void callApproval(int prefix, int code, String nfc, String password) {
        Intent mIntent = new Intent(context, WBR_SO_Approval.class);
        Bundle bundle = new Bundle();

        bundle.putInt(Constant.SO_PARAM_PREFIX, prefix);
        bundle.putInt(Constant.SO_PARAM_CODE, code);
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putString(Constant.GC_PWD, password);

        mIntent.putExtras(bundle);

        context.sendBroadcast(mIntent);

    }

    @Override
    public void onBackPressedAction() {
        mView.callAct027(context);
    }
}
