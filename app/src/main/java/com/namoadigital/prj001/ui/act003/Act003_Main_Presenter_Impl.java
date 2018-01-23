package com.namoadigital.prj001.ui.act003;

import android.content.Context;
import android.content.Intent;

import com.namoa_digital.namoa_library.util.HMAux;
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
        if (!ToolBox_Con.getPreference_Site_Code(context).equals("-1")){
            return true;
        }
        return false;
    }

    @Override
    public void startChatService() {
        //Se Possui Acesso ao Chat, inicia serviço
        if(ToolBox_Inf.parameterExists(context,Constant.PARAM_CHAT)) {
            //
            if (!ScreenStatusService.isRunning) {
                Intent mIntent = new Intent(context, ScreenStatusService.class);
                context.startService(mIntent);
            }
            //
            if(!AppBackgroundService.isRunning){
                //
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
                context.startService(chatIntent);
            }else{
                if(ToolBox_Inf.isUsrAppLogged(context)) {
                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                    singletonWebSocket.attemptSendLogin();
                }else{
                    try {
                        throw new Exception("USER_NOT_LOGGED_TRYIED_START_WEB_SOCKET");
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToolBox_Inf.registerException(getClass().getName(),e);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct002(context,false);
    }

    @Override
    public void setSiteCode(HMAux item) {
        ToolBox_Con.setPreference_Site_Code(context, item.get(MD_SiteDao.SITE_CODE));
        //mView.callAct004(context);
        mView.callAct033(context);
    }

}
