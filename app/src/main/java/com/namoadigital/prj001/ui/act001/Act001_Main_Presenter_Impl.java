package com.namoadigital.prj001.ui.act001;

import static com.namoadigital.prj001.util.ConstantBaseApp.DB_PREFIX_CUSTOM;
import static com.namoadigital.prj001.util.ToolBox_Inf.OLD_PACKAGE_NAME;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.core.data.domain.model.EnvironmentPrefModel;
import com.namoadigital.prj001.core.data.domain.model.EnvironmentType;
import com.namoadigital.prj001.core.data.local.preferences.environment.EnvironmentDevelopementPref;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.service.WS_GetCustomer;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Act001_Main_Presenter_Impl implements Act001_Main_Presenter {

    private final int TOLERANCE_UPDATE_DIALOG_DAYS = 1;

    private Context context;

    private Act001_Main_View mView;

    private EnvironmentDevelopementPref pref;

    public Act001_Main_Presenter_Impl(Context context, Act001_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void executeLoginProcess(String user, String password, String nfc, int status_jump) {
        executeLoginProcess(user, password, nfc, status_jump, true);
    }

    @Override
    public void executeLoginProcess(String user, String password, String nfc, int status_jump, boolean userValidation) {
        Intent mIntent = new Intent(context, WBR_GetCustomer.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GC_USER_CODE, user);
        bundle.putString(Constant.GC_PWD, ToolBox_Inf.md5(password).toUpperCase());
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putInt(Constant.GC_STATUS_JUMP, status_jump);
        bundle.putBoolean(WS_GetCustomer.GC_USER_VALIDATION_BUNDLE_PARAM, userValidation);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void validateLogin(String login, String password, String nfc_code) {

        if (nfc_code.trim().length() == 0) {

            if (login.trim().length() == 0) {
                mView.showAlertMsg(
                        context.getString(R.string.alert_title_login_fill_field),
                        context.getString(R.string.msg_login_required)
                );
                mView.fieldFocus(Act001_Main.ET_LOGIN);
                return;
            }
            //
            if (password.trim().length() == 0) {
                mView.showAlertMsg(
                        context.getString(R.string.alert_title_login_fill_field),
                        context.getString(R.string.msg_pwd_required)
                );
                mView.fieldFocus(Act001_Main.ET_PASSWORD);
                return;
            }
        }
        //Verifica se exite conexao antes de chamar WS
        if(ToolBox_Con.isOnline(context)){
            mView.showPD();
            //
            executeLoginProcess(login, password, nfc_code, 0);
        } else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }

    }

    @Override
    public void checkLogin() {

        String user_code = ToolBox_Con.getPreference_User_Code(context);
        long customer_code = ToolBox_Con.getPreference_Customer_Code(context);
        if (!user_code.equals("")) {
            if (Constant.DEVELOPMENT_BASE) {
                EnvironmentType prefEnvironment = pref.read().getEnvironment();
                setEnvironmentSelected(prefEnvironment.getString());
            }
            if (customer_code != -1) {
                mView.call_Act003_Main(context);
            } else {
                mView.call_Act002_Main(context);
            }
        } else {
            mView.setSplashScreen(false);
        }
    }

    @Override
    public void checkEnvironmentDevelopment() {
        if (Constant.DEVELOPMENT_BASE) {
            pref = EnvironmentDevelopementPref.Companion.instance(context);
            EnvironmentType environmentType = pref.read().getEnvironment();
            mView.developmentMode(environmentType);
        }
    }

    @Override
    public void setEnvironmentSelected(String environmentSelected) {
        if (environmentSelected.equals(EnvironmentType.DEVELOPMENT.getString())) {
            pref.write(new EnvironmentPrefModel(EnvironmentType.DEVELOPMENT));
            Constant.WS_PREFIX = toEnvironmentType(environmentSelected).getString();
            ConstantBase.DEVELOPMENT_TYPE = toEnvironmentType(environmentSelected).getString();
        } else if (environmentSelected.equals(EnvironmentType.HOMOLOG.getString())) {
            pref.write(new EnvironmentPrefModel(EnvironmentType.HOMOLOG));
            Constant.WS_PREFIX = toEnvironmentType(environmentSelected).getString();
            ConstantBase.DEVELOPMENT_TYPE = toEnvironmentType(environmentSelected).getString();
        }
        Constant.refreshUrl();
    }


    private EnvironmentType toEnvironmentType(String type) {
        if (type.equals(EnvironmentType.DEVELOPMENT.getString()))
            return EnvironmentType.DEVELOPMENT;
        if (type.equals(EnvironmentType.HOMOLOG.getString())) return EnvironmentType.HOMOLOG;
        return EnvironmentType.NULL;
    }


    @Override
    public EnvironmentType getEnvironmentDevelopment() {
        return pref.read().getEnvironment();
    }


    @Override
    public void processEnvironmentSelected(String environmentSelected) {
        setEnvironmentSelected(environmentSelected);
        //
        EnvironmentType prefEnvironment = pref.read().getEnvironment();
        if (!environmentSelected.equals(prefEnvironment.getString())
        && prefEnvironment != EnvironmentType.NULL) {
            deleteLocalFiles();
        }
    }


    private void deleteLocalFiles() {
        ArrayList<File> listToDelete = new ArrayList<>();
        //
        File[] files_db = ToolBox_Inf.getListDB("namoa_sms");
        File[] files_db_mult = ToolBox_Inf.getListDB(DB_PREFIX_CUSTOM);
        File[] files_db_chat = ToolBox_Inf.getListDB(ConstantBaseApp.DB_NAME_CHAT.substring(0, Constant.DB_NAME_CHAT.length() - 4));
        File[] files_token = ToolBox_Inf.getListOfFiles_v5(ConstantBaseApp.TOKEN_PATH, "");
        File[] files_support = ToolBox_Inf.getListOfFiles_v5(ConstantBaseApp.SUPPORT_PATH, "");
        File[] files_cc_cache = ToolBox_Inf.getListOfFiles_v5(ConstantBaseApp.CACHE_PATH, "");
        File[] files_cc_cache_photo = ToolBox_Inf.getListOfFiles_v5(ConstantBaseApp.CACHE_PATH_PHOTO, "");
        File[] files_cc_cache_chat = ToolBox_Inf.getListOfFiles_v5(ConstantBaseApp.CACHE_CHAT_PATH, "");
        File[] files_cc_cache_pdf = ToolBox_Inf.getListOfFiles_v5(ConstantBaseApp.CACHE_PDF, "");
        //
        Collections.addAll(listToDelete, files_db);
        Collections.addAll(listToDelete, files_db_mult);
        Collections.addAll(listToDelete, files_db_chat);
        Collections.addAll(listToDelete, files_token);
        Collections.addAll(listToDelete, files_support);
        Collections.addAll(listToDelete, files_cc_cache);
        Collections.addAll(listToDelete, files_cc_cache_photo);
        Collections.addAll(listToDelete, files_cc_cache_chat);
        Collections.addAll(listToDelete, files_cc_cache_pdf);
        //
        ToolBox_Inf.deleteFileListExceptionSafe(listToDelete);
        ToolBox_Con.cleanPreferences(context);
    }

    @Override
    public boolean isPackageInstalled() {
        return ToolBox_Inf.isPackageInstalled(OLD_PACKAGE_NAME, context.getPackageManager());
    }

    /**
     * Metodo que verifica a qtds dias foi exibido o dialog de update para saber se deve
     * ou não exibi-lo novamente.
     * Foi definido que o popup de atualização deve aparecer apenas a cada X dias onde X é o valor
     * da constante TOLERANCE_UPDATE_DIALOG_DAYS
     *
     * @return
     */
    private boolean allowUpdatePopup() {
//        Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed =" + daysSinceLastUpdatePopupShowed);
        //Se daysSinceLastUpdatePopupShowed null, cairá no catch. ISSO não deveria acontecer
        try{
//                Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed 0 && pref false" );
                updateInAppDialogShowedPreference(true);
                //
                return true;
        }catch (Exception e){
//            Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed Exception...");
            //Reseta pref pq se teve exception acho deu errado e talvez nesse caso, seja interessante
            //o resete
            updateInAppDialogShowedPreference(false);
            ToolBox_Inf.registerException(getClass().getName(),e);
            return true;
        }
    }


    private void updateInAppDialogShowedPreference(boolean prefValue) {
        ToolBox_Con.setBooleanPreference(
            context,
            ConstantBaseApp.PREFERENCE_HAS_INAPP_DIALOG_ALREADY_SHOWED,
            prefValue
        );
    }
}
