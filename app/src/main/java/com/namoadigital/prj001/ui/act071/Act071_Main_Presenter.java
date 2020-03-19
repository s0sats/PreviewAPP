package com.namoadigital.prj001.ui.act071;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Act071_Main_Presenter implements Act071_Main_Contract.I_Presenter {

    private Context context;
    private Act071_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    private TK_Ticket_CtrlDao ticketCtrlDao;
    private MD_PartnerDao mdPartnerDao;
    private MD_Schedule_ExecDao scheduleExecDao;

    public Act071_Main_Presenter(Context context, Act071_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.ticketDao = new TK_TicketDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        this.ticketCtrlDao = new TK_Ticket_CtrlDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        this.mdPartnerDao = new MD_PartnerDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        this.scheduleExecDao = new MD_Schedule_ExecDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    /**
     * LUCHE- 12/03/2020
     * <p></p>
     * Metodo que verifica se parametros do bundle são validos.
     * Atualizado o metodo adicionando as pk do agendamento pois, nesse caso, o prefix do ticket pode ser 0
     *
     * @param mTkActionPrefix - Ticket Prefix
     * @param mTkActionCode   - Ticket Code
     * @param mTkActionSeq    - Ticket Seq
     * @param mSchedulePrefix - Schedule Prefix
     * @param mScheduleCode   - Schedule Code
     * @param mScheduleExec   - Schedule Exec
     * @return - Verdadeiro se a pk do ticket maior que zero ou se pk agendamento , mas ticket code e seq maior que 0
     */
    @Override
    public boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq, int mSchedulePrefix, int mScheduleCode, int mScheduleExec) {
        return ((mTkActionPrefix > 0 || (mSchedulePrefix > 0 && mScheduleCode > 0 && mScheduleExec > 0))
            && mTkActionCode > 0 && mTkActionSeq > 0
        );
    }

    @Override
    public TK_Ticket_Ctrl getTicketCtrlObj(int mActionPrefix, int mActionCode, int mActionSeq) {
        return ticketCtrlDao.getByString(
            new TK_Ticket_Ctrl_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                mActionPrefix,
                mActionCode,
                mActionSeq
            ).toSqlQuery()
        );
    }

    @Override
    public String getFormattedInfo(String ctrl_end_date, String ctrl_end_user_name) {
        String sFormatted = ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(ctrl_end_date),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );
        //
        sFormatted += " (" + ctrl_end_user_name + ")";
        //
        return sFormatted;
    }

    @Override
    public String getFormattedSeqText(String seq) {
        return hmAux_Trans.get("seq_lbl") + " " + seq;
    }

    @Override
    public boolean getReadOnlyDefinition(TK_Ticket_Ctrl mTicketCtrl) {
        return isReadOnlyStatus(mTicketCtrl.getCtrl_status())
            || !hasActionExecProfile()
            || !hasPartnerProfile(mTicketCtrl.getPartner_code());
    }

    private boolean hasPartnerProfile(Integer partner_code) {
        if (partner_code == null) {
            return true;
        }
        //
        MD_Partner partner = mdPartnerDao.getByString(
            new MD_Partner_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                partner_code
            ).toSqlQuery()
        );
        //
        if (partner != null && partner.getCustomer_code() > 0) {
            return true;
        }
        //
        return false;
    }

    private boolean hasActionExecProfile() {
        return ToolBox_Inf.profileExists(
            context,
            ConstantBaseApp.PROFILE_MENU_TICKET,
            ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_ACTION_EXEC
        );
    }

    public boolean hasCheckinAlertByStatus(String ticketStatus) {
        return isReadOnlyStatus(ticketStatus);
    }

    private boolean isReadOnlyStatus(String ticketStatus) {
        return !ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(ticketStatus)
            && !ConstantBaseApp.SYS_STATUS_PROCESS.equalsIgnoreCase(ticketStatus);
    }

    /**
     * Gera path_local caso seja uma nova foto ou retorna path_local existente
     * @param action - obj
     * @return - String com o path local
     */
    @Override
    public String generateActionPhotoLocalPath(TK_Ticket_Action action) {
        if (action.getAction_photo() == null && action.getAction_photo_local() == null) {
            return ToolBox_Inf.buildTicketActionImgPath(action);
        }
        //
        return action.getAction_photo_local();
    }

    @Override
    public boolean newActionPhotoExists(TK_Ticket_Action action) {
        String localPath = generateActionPhotoLocalPath(action);
        try {
            File file = new File(ConstantBase.CACHE_PATH_PHOTO, localPath);
            return file.exists();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean fileExists(String path) {
        try {
            File file = new File(ConstantBase.CACHE_PATH_PHOTO, path);
            return file.exists();
        } catch (Exception e) {
            return false;
        }
    }

    private void checkActionPhotoToDel(TK_Ticket_Action action) {
        if (!ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(action.getAction_status())
            && action.getAction_photo_local() == null
            && newActionPhotoExists(action)
        ) {
            deleteNewActionPhoto(action);
        } else {
            if (action.getAction_photo_local() != null) {
                mView.restoreActionImage();
            }
        }
    }

    private void deleteNewActionPhoto(TK_Ticket_Action action) {
        String localPath = generateActionPhotoLocalPath(action);
        File file = new File(ConstantBase.CACHE_PATH_PHOTO, localPath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
    //


    @Override
    public boolean updateTicketAction(TK_Ticket_Ctrl mTicketCtrl) {
        DaoObjReturn daoObjReturn = ticketCtrlDao.addUpdate(mTicketCtrl);
        TK_Ticket tkTicket = getTicketbyPk(mTicketCtrl.getTicket_prefix(),mTicketCtrl.getTicket_code());
        if (!daoObjReturn.hasError()) {
            /*ticketDao.addUpdate(
                new TK_Ticket_Sql_005(
                    mTicketCtrl.getCustomer_code(),
                    mTicketCtrl.getTicket_prefix(),
                    mTicketCtrl.getTicket_code(),
                    1
                ).toSqlQuery()
            );*/
            tkTicket.setUpdate_required(1);
            tkTicket.setTicket_status( mView.isScheduledTicket() ? ConstantBaseApp.SYS_STATUS_WAITING_SYNC : tkTicket.getTicket_status());
            //
            daoObjReturn = ticketDao.addUpdate(tkTicket);
            if(!daoObjReturn.hasError()){
                if(mView.isScheduledTicket()){
                    //
                    updateScheduleStatus(
                        mView.getmSchedulePrefix(),
                        mView.getmScheduleCode(),
                        mView.getmScheduleExec(),
                        mTicketCtrl.getCtrl_status()
                    );
                }
                //
                if (mTicketCtrl.getAction().getAction_photo_local() != null
                    && !mTicketCtrl.getAction().getAction_photo_local().isEmpty()
                ) {
                    uploadActionImage(mTicketCtrl);
                }
                //
                return true;
            }
        }
        //TODO OQUE FAZER NOS CASOS DE ERRO ? CRIAR ANTES COPIA DOS OBJ PARA "ROLLBACK" NA MÃO OU COLOCAR TUDO EM UM TRANSACTION
        return false;
    }

    /**
     * LUCHE - 14/02/2020
     * <p>
     * Atualiza status da tabela de agendamentos.
     *
     * @param schedule_prefix
     * @param schedule_code
     * @param schedule_exec
     * @param status
     * @return
     */
    private boolean updateScheduleStatus(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec, String status) {
        MD_Schedule_Exec scheduleExec = scheduleExecDao.getByString(
            new MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery()
        );
        //
        if (MD_Schedule_Exec.isValidScheduleExec(scheduleExec)) {
            scheduleExec.setStatus(status);
            DaoObjReturn daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
            //Retorna verdadeiro se não teve erro.
            return !daoObjReturn.hasError();
        }
        //
        return false;
    }

    private void uploadActionImage(TK_Ticket_Ctrl mTicketCtrl) {
        GE_FileDao geFileDao = new GE_FileDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + mTicketCtrl.getAction().getAction_photo_local());
        if (sFile.exists()) {
            GE_File geFile = new GE_File();
            geFile.setFile_code(mTicketCtrl.getAction().getAction_photo_local().replace(".png", "").replace(".jpg", ""));
            geFile.setFile_path(mTicketCtrl.getAction().getAction_photo_local());
            geFile.setFile_status("OPENED");
            geFile.setFile_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            //
            geFileDao.addUpdate(geFile);
            //
            startUploadImg();
        }
    }

    private void startUploadImg() {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void execTicketSave() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Ticket_Save.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_ticket_save_ttl"),
                hmAux_Trans.get("dialog_ticket_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Save.class);
            Bundle bundle = new Bundle();
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            //Se falso, será exibi msg na tela.
            if (checkOfflineTicketDone(mView.getAction())) {
                mView.showAlert(
                    hmAux_Trans.get("alert_offline_save_ttl"),
                    hmAux_Trans.get("alert_offline_save_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.postTicketSave();
                        }
                    }
                );
            }
        }
    }

    @Override
    public void processSaveReturn(int mPrefix, int mCode, String jsonRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn> checkinReturns = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        //
        if (jsonRet != null && !jsonRet.isEmpty()) {
            try {
                checkinReturns = gson.fromJson(
                    jsonRet,
                    new TypeToken<ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn>>() {
                    }.getType());

            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
            //
            if (checkinReturns != null && checkinReturns.size() > 0) {
                boolean ticketResult = true;
                int ticketNextIdx = 0;
                HMAux auxResult = new HMAux();
                //
                for (WS_TK_Ticket_Save.TicketSaveActReturn actReturn : checkinReturns) {
                    String ticketCode = actReturn.getPrefix() + "." + actReturn.getCode();
                    //Se a ação da tela é agendamento  e a pk do agendamento bater com a retornada,
                    //atualiza o prefix e code para gerar o extrato correto.
                    if (isScheduleCreationForThisAction(actReturn)) {
                        mPrefix = actReturn.getPrefix();
                        mCode = actReturn.getCode();
                        mView.updateTicketPk(mPrefix,mCode);
                    }
                    //
                    if (!auxResult.containsKey(ticketCode)
                        || (auxResult.containsKey(ticketCode)
                        && !actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK))
                    ) {
                        //Se erro, verifica se erro de processamento qual erro foi e pega msg
                        auxResult.put(ticketCode, getResultMsgFormmated(actReturn));
                    }
                }
                //For no resumido por ticket montando msg a ser exibida
                for (Map.Entry<String, String> item : auxResult.entrySet()) {
                    String ticketPk = mPrefix + "." + mCode;
                    HMAux hmAux = new HMAux();
                    //
                    //Monta HmAux
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("ticket_lbl"));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.getKey());
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.getValue());
                    //
                    if (item.getKey().equals(ticketPk)) {
                        ticketResult = item.getValue().equals(ConstantBaseApp.MAIN_RESULT_OK);
                        resultList.add(ticketNextIdx, hmAux);
                        ticketNextIdx++;
                    } else {
                        resultList.add(hmAux);
                    }
                }
                //
                mView.showResult(resultList, ticketResult);
            } else {
                mView.showAlert(
                    hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                    hmAux_Trans.get("alert_none_ticket_returned_msg"),
                    null
                );
            }
        } else {
            mView.showAlert(
                hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                hmAux_Trans.get("alert_none_ticket_returned_msg"),
                null
            );
        }
    }

    private boolean isScheduleCreationForThisAction(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
        return
            mView.isScheduledTicket()
                && actReturn.getSchedulePrefix() == mView.getmSchedulePrefix()
                && actReturn.getScheduleCode() == mView.getmScheduleCode()
                && actReturn.getScheduleExec() == mView.getmScheduleExec();
    }

    @Override
    public String hasCheckinBlockBy(int ticket_prefix, int ticket_code) {
        TK_Ticket ticket = getTicketbyPk(ticket_prefix, ticket_code);
        String preference_user_code = ToolBox_Con.getPreference_User_Code(context);
        if (preference_user_code != null) {
            if (!preference_user_code.equals(ticket.getCheckin_user()) && ticket.getCheckin_date() != null) {

                return getFormattedInfo(ticket.getCheckin_date(), ticket.getCheckin_user_name());
            }
        }
        return "";
    }

    private String getResultMsgFormmated(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
        if (actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)) {
            return actReturn.getRetStatus();
        } else {
            return actReturn.isProcessError() ? actReturn.getProcessStatus() + "\n" + actReturn.getProcessMsg() : actReturn.getRetStatus() + "\n" + actReturn.getRetMsg();
        }
    }

    @Override
    public void definePostTicketSaveFlow(int ticket_prefix, int ticket_code) {
        proceedPostSaveFlow(getTicketbyPk(ticket_prefix, ticket_code));
    }

    @Override
    public void definePostTicketSaveFlow(int mSchedulePrefix, int mScheduleCode, int mScheduleExec) {
        proceedPostSaveFlow(getTicketBySchedule(mSchedulePrefix,mScheduleCode,mScheduleExec));
    }

    private void proceedPostSaveFlow(TK_Ticket tkTicket) {
        if (tkTicket != null && tkTicket.getCustomer_code() > 0) {
            if(!mView.isScheduledTicket()){
                if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(tkTicket.getTicket_status())
                    || !hasActionNotExec(tkTicket)
                ) {
                    mView.callAct069(false);
                } else {
                    mView.callAct070();
                }
            }else{
                mView.callAct017();
            }
        }
    }

    private TK_Ticket getTicketbyPk(int ticket_prefix, int ticket_code) {
        return getTicket(
            new TK_Ticket_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ticket_prefix,
                ticket_code
            ).toSqlQuery());
    }

    private TK_Ticket getTicket(String query) {
        return ticketDao.getByString(query);
    }

    private TK_Ticket getTicketBySchedule(int schedulePrefix, int scheduleCode, int scheduleExec) {
        return getTicket(
            new TK_Ticket_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedulePrefix,
                scheduleCode,
                scheduleExec
            ).toSqlQuery()
        );

    }

    private boolean checkOfflineTicketDone(TK_Ticket_Action ctrl) {
        final TK_Ticket tkTicket = getTicketbyPk(ctrl.getTicket_prefix(), ctrl.getTicket_code());
        //
        if (!hasActionNotExec(tkTicket)) {
            tkTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            tkTicket.setUpdate_required(1);
            DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
            //
            if (daoObjReturn.hasError()) {
                mView.showAlert(
                    hmAux_Trans.get("alert_error_on_offline_done_ttl"),
                    hmAux_Trans.get("alert_error_on_offline_done_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.checkPostTicketSaveFlow();
                            //definePostTicketSaveFlow(tkTicket.getTicket_prefix(), tkTicket.getTicket_code());
                        }
                    }
                );
                return false;
            } else {
                return true;
            }
        }
        //
        return true;
    }

    /**
     * Varre os controles em busca de algum que ainda esteja pendente de execução.
     *
     * @param tkTicket - Ticket pai do controle/action
     * @return - Verdadeiro se encontrar ao menos uma acão pendente de execução.
     */
    private boolean hasActionNotExec(TK_Ticket tkTicket) {
        //
        for (TK_Ticket_Ctrl ctrl : tkTicket.getCtrl()) {
            if (!ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(ctrl.getCtrl_status())
                && !ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equalsIgnoreCase(ctrl.getCtrl_status())) {
                return true;
            }
        }
        //
        return false;
    }

    //
    @Override
    public void onBackPressedClicked(final String requestingAct) {
        switch (requestingAct) {
            case ConstantBaseApp.ACT014:
                mView.callAct070();
                break;
            case ConstantBaseApp.ACT070:
            case ConstantBaseApp.ACT017:
            default:
                if (mView.hasUnsavedData()) {
                    ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_unsaved_data_will_be_lost_ttl"),
                        hmAux_Trans.get("alert_unsaved_data_will_be_lost_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                backActionFlow(requestingAct);
                            }
                        },
                        1
                    );
                } else {
                    backActionFlow(requestingAct);
                }
                break;
        }
    }

    private void backActionFlow(String requestingAct) {
        checkActionPhotoToDel(mView.getAction());
        //
        switch (requestingAct){
            case ConstantBaseApp.ACT070:
            case ConstantBaseApp.ACT068:
                mView.callAct070();
                break;
            case ConstantBaseApp.ACT017:
                if(mView.isScheduledTicket()){
                    mView.callAct017();
                }else{
                    mView.callAct070();
                }
                break;
            case ConstantBaseApp.ACT069:
                mView.callAct069(true);
                break;
        }
    }
}
