package com.namoadigital.prj001.ui.act071;

import android.app.AlertDialog;
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
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_009;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
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
    private TK_Ticket_StepDao ticketStepDao;
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
        this.ticketStepDao = new TK_Ticket_StepDao(
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
    public TK_Ticket_Ctrl getTicketCtrlObj(int mActionPrefix, int mActionCode, int mActionSeq, int mStepCode) {
        return ticketCtrlDao.getByString(
            new TK_Ticket_Ctrl_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                mActionPrefix,
                mActionCode,
                mActionSeq,
                mStepCode
            ).toSqlQuery()
        );
    }

    //region NOVO_TICKET

    @Override
    public TK_Ticket_Step getStepInfo(int mTicketPrefix, int mTicketCode, int mStepCode) {
        TK_Ticket_Step ticketStep =
            ticketStepDao.getByString(
                new TK_Ticket_Step_Sql_001(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    mTicketPrefix,
                    mTicketCode,
                    mStepCode
                ).toSqlQuery()
            );
        //
        if(ticketStep != null){
            //APENAS PARA NÃO CARREGAR MUITO A MEMORIA - TEM QUE ESCOVAR BYTE, MAS NÃO PRECISA PLANEJAR SAKA
            ticketStep.setCtrl(new ArrayList<TK_Ticket_Ctrl>());
        }
        return ticketStep;
    }

    @Override
    public int getStepColor(TK_Ticket_Step ticketStep) {
        return
            ticketStep != null
                ? ToolBox_Inf.getStatusColorV2(context,ticketStep.getStep_status())
                : R.color.namoa_status_pending
            ;
    }

    @Override
    public String getStepNumFormatted(TK_Ticket_Step ticketStep) {
        return
            ticketStep != null
                ? TK_Ticket_Step.getStepNumFormatted(ticketStep.getStep_order(),ticketStep.getStep_order_seq())
                : "";

    }

    @Override
    public String getStepDesc(TK_Ticket_Step ticketStep) {
        return  ticketStep != null
                ? ticketStep.getStep_desc()
                : "";
    }

    //endregion

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

    @Override
    public boolean isClosedStatus(String ctrl_status) {
        return isReadOnlyStatus(ctrl_status);
    }

    /**
     * Gera path_local caso seja uma nova foto ou retorna path_local existente
     * @param action - obj
     * @return - String com o path local
     */
    @Override
    public String generateActionPhotoLocalPath(TK_Ticket_Action action) {
        if (action.getAction_photo_url() == null && action.getAction_photo_local() == null) {
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

    /**
     * LUCHE - 23/03/2020
     * <P></P>
     * Metodo que atualiza o ticket com os dados do controle.
     *
     * Modificado metodo para que ao invés de atualizar somente o control, atualize o ticket por completo,
     * garantindo o rollback em caso de erro.
     * @param mTicketCtrl Objeto carregado e alterado na tela.
     * @return - Verdadeiro somente se todas as atualizações forem salvas.
     */
    @Override
    public boolean updateTicketAction(TK_Ticket_Ctrl mTicketCtrl) {
        TK_Ticket tkTicket = getTicketbyPk(mTicketCtrl.getTicket_prefix(),mTicketCtrl.getTicket_code());
        //Em teoria, nunca deveria ser null, mas vai saber.
        if(tkTicket != null && tkTicket.getCtrl() != null){
            int ctrlIdx = getCtrlIdx(mTicketCtrl, tkTicket);
            if(ctrlIdx > -1){
                tkTicket.getCtrl().set(ctrlIdx,mTicketCtrl);
                tkTicket.setUpdate_required(1);
                tkTicket.setTicket_status( mView.isScheduledTicket() ? ConstantBaseApp.SYS_STATUS_WAITING_SYNC : tkTicket.getTicket_status());
                //Atualiza Ticket completo, para garantir rolback caso erro ao atualizar ctrls
                DaoObjReturn daoObjReturn  = ticketDao.addUpdate(tkTicket);
                //
                if(!daoObjReturn.hasError()){
                    //Se agendamento, atualiza status do agendamento.
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
        }
        //Metodo de update original, modificado para garantir rollback via transation
        /*DaoObjReturn daoObjReturn = ticketCtrlDao.addUpdate(mTicketCtrl);
        if (!daoObjReturn.hasError()) {
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
        }*/
        return false;
    }

    /**
     * LUCHE - 23/03/2020
     * <p></p>
     * Metodo que retorna o indice do control que esta sendo alterado.
     * Na teoria, sempre retornará um valor, por o crl sempre existe.
     * @param mTicketCtrl Obj controle alterado pelo usr
     * @param tkTicket Obj Ticket ao qual o controle pertence
     * @return - Idx do ctrl ou -1 caso não encontre.
     */
    private int getCtrlIdx(TK_Ticket_Ctrl mTicketCtrl, TK_Ticket tkTicket) {
        for (int i = 0; i < tkTicket.getCtrl().size(); i++) {
            if(
                tkTicket.getCtrl().get(i).getTicket_prefix() == mTicketCtrl.getTicket_prefix()
                && tkTicket.getCtrl().get(i).getTicket_code() == mTicketCtrl.getTicket_code()
                && tkTicket.getCtrl().get(i).getTicket_seq() == mTicketCtrl.getTicket_seq()
                && tkTicket.getCtrl().get(i).getStep_code() == mTicketCtrl.getStep_code()
            ){
                return i;
            }
        }
        return -1;
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
        MD_Schedule_Exec scheduleExec = getScheduleExec(schedule_prefix, schedule_code, schedule_exec);
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

    private MD_Schedule_Exec getScheduleExec(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        return scheduleExecDao.getByString(
            new MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery()
        );
    }

    @Override
    public boolean isScheduleAbortProcess(int mSchedulePrefix, int mScheduleCode, int mScheduleExec) {
         MD_Schedule_Exec scheduleExec = getScheduleExec(mSchedulePrefix,mScheduleCode,mScheduleExec);
         if(
             MD_Schedule_Exec.isValidScheduleExec(scheduleExec)
             && scheduleExec.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PROCESS)
             && hasScheduleWarningInfo(scheduleExec)
         ){
            return true;
         }
        return false;
    }

    private boolean hasScheduleWarningInfo(MD_Schedule_Exec scheduleExec) {
        return scheduleExec.getFcm_new_status() != null
                && scheduleExec.getFcm_user_nick() != null;
    }

    @Override
    public void showScheduleCancelMsg(int mSchedulePrefix, int mScheduleCode, int mScheduleExec) {
        final MD_Schedule_Exec scheduleExec = getScheduleExec(mSchedulePrefix,mScheduleCode,mScheduleExec);
        //
        android.app.AlertDialog.Builder dialogScheduleWarning = new android.app.AlertDialog.Builder(context);
        dialogScheduleWarning.setTitle(hmAux_Trans.get("alert_schedule_cancelled_by_server_ttl"));
        dialogScheduleWarning.setMessage(
            ToolBox_Inf.getFormattedScheduleWarningInfo(
                hmAux_Trans.get("alert_schedule_warning_new_status_lbl"),
                hmAux_Trans.get(scheduleExec.getFcm_new_status()),
                hmAux_Trans.get("alert_warning_user_nick_lbl"),
                scheduleExec.getFcm_user_nick(),
                null,
                null,
                hmAux_Trans.get("alert_schedule_cancelled_by_server_msg")+"\n"
            )
        );
        dialogScheduleWarning.setCancelable(true);
        dialogScheduleWarning.setPositiveButton(
            hmAux_Trans.get("sys_alert_btn_ok"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelScheduleAndTicket(scheduleExec);
                }
            }
        );
        //
        AlertDialog dialog = dialogScheduleWarning.create();
        dialog.show();
        //
        dialog.getButton(
            DialogInterface.BUTTON_POSITIVE
        ).setTextColor(context.getResources().getColor(R.color.namoa_lime_green));
    }

    private void cancelScheduleAndTicket(MD_Schedule_Exec scheduleExec) {
        String erroMsg = "";
        String finalStatus =
            scheduleExec.getFcm_new_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PROCESS)
            ? ConstantBaseApp.SYS_STATUS_CANCELLED
            : scheduleExec.getFcm_new_status();
        TK_Ticket tkTicket = getTicketBySchedule(scheduleExec.getSchedule_prefix(),scheduleExec.getSchedule_code(),scheduleExec.getSchedule_exec());
        //
        if(tkTicket != null){
            if(tkTicket.getCtrl() != null && tkTicket.getCtrl().size() > 0){
                tkTicket.getCtrl().get(0).setCtrl_status(finalStatus);
                tkTicket.getCtrl().get(0).getAction().setAction_status(finalStatus);
            }
            tkTicket.setTicket_status(finalStatus);
            scheduleExec.setStatus(finalStatus);
            scheduleExec.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            //
            DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
            if(!daoObjReturn.hasError()){
                daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
                if(daoObjReturn.hasError()){
                    erroMsg = hmAux_Trans.get("alert_error_on_cancel_schedule_msg");
                }
            }
        } else{
            erroMsg = hmAux_Trans.get("alert_error_ticket_not_found_msg");
        }
        //
        if(!erroMsg.isEmpty()){
            mView.showAlert(
                hmAux_Trans.get("alert_error_on_cancel_schedule_ttl"),
                erroMsg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressedClicked(mView.getRequestingAct());
                    }
                }
            );
        }else{
            onBackPressedClicked(mView.getRequestingAct());
        }
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
            case ConstantBaseApp.ACT070:
            case ConstantBaseApp.ACT068:
            default:
                mView.callAct070();
                break;
        }
    }

}
