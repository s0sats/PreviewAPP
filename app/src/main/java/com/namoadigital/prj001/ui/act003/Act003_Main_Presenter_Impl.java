package com.namoadigital.prj001.ui.act003;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.service.ScreenStatusService;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_004;
import com.namoadigital.prj001.sql.MD_Site_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;

/**
 * Created by neomatrix on 17/01/17.
 */

public class Act003_Main_Presenter_Impl implements Act003_Main_Presenter {

    private Context context;
    private Act003_Main_View mView;
    private MD_SiteDao md_siteDao;
    private HMAux item;
    private CH_MessageDao messageDao;

    public Act003_Main_Presenter_Impl(Context context, Act003_Main_View mView) {
        this.context = context;
        this.mView = mView;
        //
        this.md_siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.messageDao = new CH_MessageDao(context);
    }

    @Override
    public void getSites(HMAux hmAux_Trans) {
        mView.loadSites(
                md_siteDao.query_HM(
                        new MD_Site_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                )
        );
    }

    @Override
    public boolean checkPreferenceIsSet() {
        if (!ToolBox_Con.getPreference_Site_Code(context).equals("-1")) {
            return true;
        }
        return false;
    }

    @Override
    public void startChatService() {
        //Se Possui Acesso ao Chat, inicia serviço
        if (ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT)) {
            try {
                //
                if (!ScreenStatusService.isRunning) {
                    Intent mIntent = new Intent(context, ScreenStatusService.class);
                    context.startService(mIntent);
                }
                File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
                if (ToolBox_Inf.isUsrAppLogged(context)) {
                    //Se usr esta logado, mas não tem preferencias se site e operação
                    //Ou é primeiro login ou change customer e requer ação
                    //Caso contrario(Else), usr ja chegou a act005 e não faz nada.
                    if (ToolBox_Con.getPreference_Site_Code(context).equals("-1")
                        && ToolBox_Con.getPreference_Operation_Code(context) == -1L) {

                        if (!AppBackgroundService.isRunning) {
                            //
                            HMAux msgAux = messageDao.getByStringHM(
                                    new CH_Message_Sql_004().toSqlQuery()
                            );
                            //
                            if (msgAux.size() > 0) {
                                ToolBox_Con.setPreference_Chat_Msg_Code(
                                        context,
                                        Long.parseLong(msgAux.get(CH_MessageDao.TMP))
                                );
                                //
                                ToolBox_Con.setPreference_Chat_Msg_Token(
                                        context,
                                        Long.parseLong(msgAux.get(CH_MessageDao.MSG_TOKEN))
                                );
                            }
                            Intent chatIntent = new Intent(context, AppBackgroundService.class);
                            chatIntent.putExtra(Constant.CHAT_START_SERVICE_CALLER, getClass().getName());
                            context.startService(chatIntent);

                            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Act003 Startou o Serviço \n", log_file);
                            Log.d("ChatEvent", ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Act003 Startou o Serviço\n");
                        } else {
                            SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                            singletonWebSocket.attemptSendLogin();
                            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " -  Act003 Rodou singletonGetInstance() e attemptLogin \n", log_file);
                            Log.d("ChatEvent", ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Act003 Rodou singletonGetInstance() e attemptLogin\n");
                        }
                    }else{
                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " -  Act003 Usr com preferencias setadas, não faz nada \n", log_file);
                        Log.d("ChatEvent", ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Act003 Usr com preferencias setadas, não faz nada \n");
                    }
                } else {
                    throw new Exception("USER_NOT_LOGGED_TRYIED_START_WEB_SOCKET");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
            }


//Esquema funcional até 29/01
//            if(!AppBackgroundService.isRunning){
//                //
//                //
//                HMAux msgAux = messageDao.getByStringHM(
//                        new CH_Message_Sql_004().toSqlQuery()
//                );
//                //
//                if (msgAux.size() > 0) {
//                    ToolBox_Con.setPreference_Chat_Msg_Code(
//                            context,
//                            Long.parseLong(msgAux.get(CH_MessageDao.TMP))
//                    );
//                    //
//                    ToolBox_Con.setPreference_Chat_Msg_Token(
//                            context,
//                            Long.parseLong(msgAux.get(CH_MessageDao.MSG_TOKEN))
//                    );
//                }
//                Intent chatIntent = new Intent(context, AppBackgroundService.class);
//                chatIntent.putExtra(Constant.CHAT_START_SERVICE_CALLER,getClass().getName());
//                context.startService(chatIntent);
//            }else{
//                if(ToolBox_Inf.isUsrAppLogged(context)) {
//                    try{
////                        File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
////                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Act003 Startou o singletonGetInstance()\n", log_file);
//                        Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Act003 Startou o singletonGetInstance()\n");
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
//                    singletonWebSocket.attemptSendLogin();
//                }else{
//                    try {
//                        throw new Exception("USER_NOT_LOGGED_TRYIED_START_WEB_SOCKET");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ToolBox_Inf.registerException(getClass().getName(),e);
//                    }
//                }
//            }
            //end do se tem preferencias
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct002(context, false);
    }

    @Override
    public void setSiteCode(HMAux item) {
        ToolBox_Con.setPreference_Site_Code(context, item.get(MD_SiteDao.SITE_CODE));
        //mView.callAct004(context);
        mView.callAct033(context);
    }

}
