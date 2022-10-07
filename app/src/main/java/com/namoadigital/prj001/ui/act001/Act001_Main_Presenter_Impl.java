package com.namoadigital.prj001.ui.act001;

import static com.namoadigital.prj001.util.ToolBox_Inf.OLD_PACKAGE_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.service.WS_GetCustomer;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Act001_Main_Presenter_Impl implements Act001_Main_Presenter {

    private final int TOLERANCE_UPDATE_DIALOG_DAYS = 1;

    private Context context;

    private Act001_Main_View mView;

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
            if (customer_code != -1) {
                mView.call_Act003_Main(context);
            } else {
                mView.call_Act002_Main(context);
            }
        }
    }

    @Override
    public boolean isPackageInstalled() {
        return ToolBox_Inf.isPackageInstalled(OLD_PACKAGE_NAME, context.getPackageManager());
    }

    @Override
    public void checkUpdateAvailable(AppUpdateManager updateManager) {
//        Log.i("inRonaldo", "checkUpdateAvailable acessado" );
        updateManager
            .getAppUpdateInfo()
            .addOnSuccessListener(appUpdateInfo -> {
//                Log.i("inRonaldo", "updateAvailability =" + appUpdateInfo.updateAvailability());
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
//                    if( allowUpdatePopup(appUpdateInfo.clientVersionStalenessDays())
//                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
//                    ){
//                        callImmediateUpdateFlow(updateManager,appUpdateInfo);
//                    }
//                } else {
////                    Log.i("inRonaldo", "Reseta pref por nao ter atualizacao e ao impedir o inapp na proxima atualizacao " );
//                    updateInAppDialogShowedPreference(false);
//                }
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    callImmediateUpdateFlow(updateManager,appUpdateInfo);
                } else {
                    checkLogin();
                }
            })
            .addOnFailureListener(
                    appUpdateInfo -> {
                        checkLogin();
                    }
            );
    }

    @Override
    public void checkUpdateInProgess(AppUpdateManager updateManager) {

        updateManager
            .getAppUpdateInfo()
            .addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
                {
                    callImmediateUpdateFlow(updateManager,appUpdateInfo);
                }
            });
    }

    private void callImmediateUpdateFlow(AppUpdateManager updateManager, AppUpdateInfo appUpdateInfo) {
        try {
            updateManager.startUpdateFlowForResult(
                appUpdateInfo,// Pass the intent that is returned by 'getAppUpdateInfo()'.
                AppUpdateType.IMMEDIATE,
                mView.getActivity(), // The current activity making the update request.
                ConstantBaseApp.PLAYSTORE_UPDATE_REQUEST_CODE // Include a request code to later monitor this update request.
            );
        } catch (IntentSender.SendIntentException e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
            ToolBox.toastMSG(
                context,
                context.getString(R.string.error_on_inapp_start_update_flow)
            );
        }
    }

    /**
     * Metodo que verifica a qtds dias foi exibido o dialog de update para saber se deve
     * ou não exibi-lo novamente.
     * Foi definido que o popup de atualização deve aparecer apenas a cada X dias onde X é o valor
     * da constante TOLERANCE_UPDATE_DIALOG_DAYS
     *
     * @param daysSinceLastUpdatePopupShowed - Qtd de dias decorridos ou null se nunca foi exibido
     * @return
     */
//    private boolean allowUpdatePopup(Integer daysSinceLastUpdatePopupShowed) {
////        Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed =" + daysSinceLastUpdatePopupShowed);
//        //Se daysSinceLastUpdatePopupShowed null, cairá no catch. ISSO não deveria acontecer
//        try{
//            if( daysSinceLastUpdatePopupShowed == 0
//                && !ToolBox_Con.getBooleanPreferencesByKey(
//                        context,
//                        ConstantBaseApp.PREFERENCE_HAS_INAPP_DIALOG_ALREADY_SHOWED,
//                        false
//                    )
//            ){
////                Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed 0 && pref false" );
//                updateInAppDialogShowedPreference(true);
//                //
//                return true;
//            } else {
////                Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed "+daysSinceLastUpdatePopupShowed);
//                //Se qtd de dias maior que 0, reseta preferencia.
//                if(daysSinceLastUpdatePopupShowed > 0){
////                    Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed > 0, rest pref && compara qtd de dias com TOLERANCE_UPDATE_DIALOG_DAYS");
//                    updateInAppDialogShowedPreference(false);
//                    //Se o qtd de dias for diferente da qt
//                    return !(daysSinceLastUpdatePopupShowed == TOLERANCE_UPDATE_DIALOG_DAYS);
//                }
//                //Se chegou aqui, qtd de dias 0 e ja foi exibido entao , não exibe novamente.
//                return false;
//            }
//        }catch (Exception e){
////            Log.i("inRonaldo", "daysSinceLastUpdatePopupShowed Exception...");
//            //Reseta pref pq se teve exception acho deu errado e talvez nesse caso, seja interessante
//            //o resete
//            updateInAppDialogShowedPreference(false);
//            ToolBox_Inf.registerException(getClass().getName(),e);
//            return true;
//        }
//    }


//    private void updateInAppDialogShowedPreference(boolean prefValue) {
//        ToolBox_Con.setBooleanPreference(
//            context,
//            ConstantBaseApp.PREFERENCE_HAS_INAPP_DIALOG_ALREADY_SHOWED,
//            prefValue
//        );
//    }
}
