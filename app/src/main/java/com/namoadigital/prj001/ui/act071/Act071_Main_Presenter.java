package com.namoadigital.prj001.ui.act071;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

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
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_004;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_009;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act070.model.StepMain;
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
     * LUCHE - 07/08/2020
     * Atualizado o metodo adicionando isCreationCtrl para permitir carregar a tela sem ter ctrl criado.
     *
     * @param mTkActionPrefix - Ticket Prefix
     * @param mTkActionCode   - Ticket Code
     * @param mTkActionSeqTmp - Ticket Seq
     * @param mSchedulePrefix - Schedule Prefix
     * @param mScheduleCode   - Schedule Code
     * @param mScheduleExec   - Schedule Exec
     * @param isCreationCtrl  - Se e criação de ação ou não
     * @return - Verdadeiro se a pk do ticket maior que zero ou se pk agendamento , mas ticket code e seq maior que 0
     */
    @Override
    public boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeqTmp, int mSchedulePrefix, int mScheduleCode, int mScheduleExec, boolean isCreationCtrl) {
        return ((mTkActionPrefix > 0 || (mSchedulePrefix > 0 && mScheduleCode > 0 && mScheduleExec > 0))
                && mTkActionCode > 0
                && (mTkActionSeqTmp > 0 || isCreationCtrl)
        );
    }

    @Override
    public TK_Ticket_Ctrl getTicketCtrlObj(int mActionPrefix, int mActionCode, int mActionSeqTmp, int mStepCode) {
        return ticketCtrlDao.getByString(
            new TK_Ticket_Ctrl_Sql_004(
                ToolBox_Con.getPreference_Customer_Code(context),
                mActionPrefix,
                mActionCode,
                mActionSeqTmp
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
    public int getStepColor(TK_Ticket_Step ticketStep, boolean IsCurrentStep) {
        int stepColor = ContextCompat.getColor(context,R.color.namoa_color_pipeline_next_step);
        if(StepMain.usesStatusColorInStep(ticketStep.getStep_status())){
            stepColor = ToolBox_Inf.getStatusColorV2(context,ticketStep.getStep_status());
        }else if(IsCurrentStep){
            stepColor = ContextCompat.getColor(context,R.color.namoa_status_process);
        }
        return stepColor;
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

    @Override
    public void setStartInfoIfNeed(TK_Ticket_Ctrl mTicketCtrl) {
        if(ConstantBaseApp.SYS_STATUS_PENDING.equals(mTicketCtrl.getCtrl_status())
           && mTicketCtrl.getCtrl_start_user() == null
        ){
            mTicketCtrl.setCtrl_start_date(
                ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
            );
            mTicketCtrl.setCtrl_start_user(ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context)));
            mTicketCtrl.setCtrl_start_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
        }
    }

    @Override
    public void createActionIfNeed(TK_Ticket_Ctrl mTicketCtrl, boolean isCreationAction) {
        if(isCreationAction){
            TK_Ticket_Action tk_ticket_action = new TK_Ticket_Action();
            tk_ticket_action.setPK(mTicketCtrl);
            tk_ticket_action.setAction_status(ConstantBaseApp.SYS_STATUS_PENDING);
            mTicketCtrl.setAction(tk_ticket_action);
        }
    }

    @Override
    public TK_Ticket_Ctrl createTicketCtrlObj(int mActionPrefix, int mActionCode, int mStepCode, Bundle act081Bundle) {
        TK_Ticket tkTicket = getTicketbyPk(mActionPrefix, mActionCode);
        TK_Ticket_Step stepInfo = getStepInfo(mActionPrefix, mActionCode, mStepCode);
        TK_Ticket_Ctrl ticketCtrl = null;
        if(tkTicket!= null  && stepInfo != null) {
            try {
                String product_code = getBundleOrTicketInfo(tkTicket,act081Bundle,MD_ProductDao.PRODUCT_CODE);
                String product_id = getBundleOrTicketInfo(tkTicket,act081Bundle,MD_ProductDao.PRODUCT_ID);
                String product_desc = getBundleOrTicketInfo(tkTicket,act081Bundle,MD_ProductDao.PRODUCT_DESC);
                String serial_code = getBundleOrTicketInfo(tkTicket,act081Bundle,MD_Product_SerialDao.SERIAL_CODE);
                String serial_id = getBundleOrTicketInfo(tkTicket,act081Bundle,MD_Product_SerialDao.SERIAL_ID);
                //
                ticketCtrl = new TK_Ticket_Ctrl(
                                0,
                                ticketCtrlDao.getNextCtrlTicketSeqTmp(
                                    stepInfo.getCustomer_code(),stepInfo.getTicket_prefix(),stepInfo.getTicket_code(),stepInfo.getStep_code(),null
                                ),
                                ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION,
                                ToolBox_Inf.convertStringToInt(product_code),
                                product_id,
                                product_desc,
                                ToolBox_Inf.convertStringToInt(serial_code),
                                serial_id,
                                ConstantBaseApp.SYS_STATUS_PENDING,
                                stepInfo.getStep_order(),
                                0
                );
                //Seta PK baseado no Step recebido
                ticketCtrl.setPK(stepInfo);
                //
                setStartInfoIfNeed(ticketCtrl);
                createActionIfNeed(ticketCtrl, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ticketCtrl;
    }

    /**
     * LUCHE - 05/11/2020
     * Metodo usado na criação de ctrl espontaneo e define se usa o dado do bundle ou do cabeçalho do ticket.
     * Qual informação é retornada é definida pelo param infoKey
     * Caso o bundle exista, significa que veio do fluxo espontaneo novo. Se não, é processo antigo
     * ou agendamento.
     * @param tkTicket
     * @param act081Bundle
     * @param infoKey
     * @return
     */
    private String getBundleOrTicketInfo(TK_Ticket tkTicket, @Nullable Bundle act081Bundle, String infoKey) {
        if(act081Bundle == null){
            act081Bundle = new Bundle();
        }
        switch (infoKey){
            case MD_ProductDao.PRODUCT_CODE:
                return act081Bundle.getString(MD_ProductDao.PRODUCT_CODE, String.valueOf(tkTicket.getOpen_product_code()));
            case MD_ProductDao.PRODUCT_DESC:
                return act081Bundle.getString(MD_ProductDao.PRODUCT_DESC, tkTicket.getOpen_product_desc());
            case MD_ProductDao.PRODUCT_ID:
                return act081Bundle.getString(MD_ProductDao.PRODUCT_ID, tkTicket.getOpen_product_id());
            case MD_Product_SerialDao.SERIAL_CODE:
                return act081Bundle.getString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(tkTicket.getOpen_serial_code()));
            case MD_Product_SerialDao.SERIAL_ID:
                return act081Bundle.getString(MD_Product_SerialDao.SERIAL_ID, tkTicket.getOpen_serial_id());
            default:
                return "";
        }
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
        return isReadOnlyStatus(mTicketCtrl.getCtrl_status());
    }

    /**
     * LUCHE - 04/08/2020
     * Não haverá mais restrição de execução por causa do parceiro
     * ficará aqui pq pode tudo mudar.
     * @param partner_code
     * @return
     */
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
    //TODO VERIFICAR A NECESSIDADE DE INCLUIR QUANDO ACTION CREATION
    public String generateActionPhotoLocalPath(TK_Ticket_Action action) {
        //Se criação de foto, devolve o nome da foto com o seq_tmp
        if(mView.isCreationCtrl()){
            return ToolBox_Inf.buildTicketActionImgPath(action.getCustomer_code(),action.getTicket_prefix(),action.getTicket_code(),action.getTicket_seq_tmp());
        }
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
     *
     * LUCHE - 23/08/2020
     * Modificado metodo adicionando logica para criação do ctrl
     *
     * @param mTicketCtrl Objeto carregado e alterado na tela.
     * @return - Verdadeiro somente se todas as atualizações forem salvas.
     */
    @Override
    public boolean updateTicketAction(TK_Ticket_Ctrl mTicketCtrl) {
        TK_Ticket tkTicket = getTicketbyPk(mTicketCtrl.getTicket_prefix(),mTicketCtrl.getTicket_code());
        //Em teoria, nunca deveria ser null, mas vai saber.
        int stepIdx = getStepIdx(mTicketCtrl, tkTicket);
        if(
            tkTicket != null
            && stepIdx > -1 && stepIdx <= tkTicket.getStep().size()
            && tkTicket.getStep().get(stepIdx) != null
        ){
            int ctrlIdx = getCtrlIdx(mTicketCtrl, tkTicket.getStep().get(stepIdx));
            if(ctrlIdx > -1 || mView.isCreationCtrl()){
                TK_Ticket_Step ticketStep = tkTicket.getStep().get(stepIdx);
                //Se cração de ctrl, add, se não seta na posição original.
                if(mView.isCreationCtrl()) {
                    ticketStep.getCtrl().add(mTicketCtrl);
                }else{
                    ticketStep.getCtrl().set(ctrlIdx, mTicketCtrl);
                }
                //
                setCheckInOutWhenOneTouchStep(ticketStep,mTicketCtrl);
                //
                checkCloseStepForWaitingSync(ticketStep,mTicketCtrl);
                //
                if(! mView.isScheduledTicket() ) {
                    tkTicket.getNextUserFocus(stepIdx);
                }
                //
                mTicketCtrl.setUpdate_required(1);
                ticketStep.setUpdate_required(1);
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
        return false;
    }

    /**
     * LUCHE - 05/08/2020
     * <p></p>
     * Se step for one_touch, seta data de inicio
     * LUCHE - 09/11/2020
     * Modificado metodo adicionando a chamada do metdo forceNoneObjToWaitingSync que fecha o processo none planejado caso exista
     * @param ticketStep
     * @param mTicketCtrl
     */
    private void setCheckInOutWhenOneTouchStep(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        if(ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH.equals(ticketStep.getExec_type())
           && !ToolBox_Inf.hasConsistentValueString(ticketStep.getStep_start_date())
        ) {
            ticketStep.setStep_start_date(mTicketCtrl.getCtrl_start_date());
            ticketStep.setStep_start_user(mTicketCtrl.getCtrl_start_user());
            ticketStep.setStep_start_user_nick(mTicketCtrl.getCtrl_start_user_name());
            //LUCHE - 09/11/2020
            //Com a nova definição, se o step é check in manual e seu obj planejado é none, esse deve ser
            //finalizado junto com o checkin...
            ToolBox_Inf.forceNoneObjToWaitingSync(ticketStep, false);
        }
    }
    /**
     * LUCHE - 04/08/2020
     * <p></p>
     * Verifica se precisa setar o status do step como waiting sync impedindo adicionar novo ctrl
     * e impedindo que segui para a proxima etapa.
     * LUCHE - 10/08/2020
     * Modificado metodo para desconsiderar o tipo do step e considerar apenas o move next para fechar ou não
     * o step
     * @param ticketStep
     * @param mTicketCtrl
     */
    private void checkCloseStepForWaitingSync(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        int stepCtrlsFinalizedCounter = 0;
        for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
            if(ConstantBaseApp.SYS_STATUS_DONE.equals(ticketCtrl.getCtrl_status())
               || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(ticketCtrl.getCtrl_status())
            ){
                stepCtrlsFinalizedCounter++;
            }
        }
        //Se todos os ctrl estão finalizado e o step é one_touch ou for start_end com move_next_step,
        //faz checkout
        if( stepCtrlsFinalizedCounter == ticketStep.getCtrl().size()
            && ticketStep.getMove_next_step() == 1
        ){
            ticketStep.setStep_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            ticketStep.setStep_end_date(mTicketCtrl.getCtrl_end_date());
            ticketStep.setStep_end_user(mTicketCtrl.getCtrl_end_user());
            ticketStep.setStep_end_user_nick(mTicketCtrl.getCtrl_end_user_name());
        }
    }

    /**
     * LUCHE - 23/03/2020
     * <p></p>
     * Metodo que retorna o indice do control que esta sendo alterado.
     * Na teoria, sempre retornará um valor, por o crl sempre existe.
     * @param mTicketCtrl Obj controle alterado pelo usr
     * @param tkTicketStep Obj Ticket Step ao qual o controle pertence
     * @return - Idx do ctrl ou -1 caso não encontre.
     */
    private int getCtrlIdx(TK_Ticket_Ctrl mTicketCtrl, TK_Ticket_Step tkTicketStep) {
        for (int i = 0; i < tkTicketStep.getCtrl().size(); i++) {
            if(
                tkTicketStep.getCtrl().get(i).getTicket_prefix() == mTicketCtrl.getTicket_prefix()
                && tkTicketStep.getCtrl().get(i).getTicket_code() == mTicketCtrl.getTicket_code()
                && tkTicketStep.getCtrl().get(i).getTicket_seq() == mTicketCtrl.getTicket_seq()
                && tkTicketStep.getCtrl().get(i).getStep_code() == mTicketCtrl.getStep_code()
            ){
                return i;
            }
        }
        //
        return -1;
    }

    private int getStepIdx(TK_Ticket_Ctrl mTicketCtrl, TK_Ticket tkTicket) {
        for (int i = 0; i < tkTicket.getStep().size(); i++) {
            if(
                tkTicket.getStep().get(i).getTicket_prefix() == mTicketCtrl.getTicket_prefix()
                && tkTicket.getStep().get(i).getTicket_code() == mTicketCtrl.getTicket_code()
                && tkTicket.getStep().get(i).getStep_code() == mTicketCtrl.getStep_code()
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

    @Override
    public MD_Schedule_Exec getScheduleExec(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
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
    public boolean verifyProductForForm() {
        if(ToolBox_Inf.hasFormProductOutdate(context)){
            if (ToolBox_Con.isOnline(context)) {
                mView.setWsProcess(WS_Sync.class.getName());
                //
                mView.showPD(
                        hmAux_Trans.get("progress_sync_ttl"),
                        hmAux_Trans.get("progress_sync_msg")
                );
                //
                ArrayList<String> data_package = new ArrayList<>();
                data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
                //
                Intent mIntent = new Intent(context, WBR_Sync.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
                bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
                bundle.putLong(Constant.GS_PRODUCT_CODE, 0);
                bundle.putInt(Constant.GC_STATUS_JUMP, 1);
                bundle.putInt(Constant.GC_STATUS, 1);
                //
                mIntent.putExtras(bundle);
                //
                context.sendBroadcast(mIntent);
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

    @Override
    public void callWsSave() {
        mView.setWsProcess(WS_Save.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_ticket_form_save_ttl"),
                hmAux_Trans.get("dialog_ticket_form_save_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_Save.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);//Pula validação Update require
        bundle.putInt(Constant.GC_STATUS, 1);//Pula validação de other device
        bundle.putString(Act005_Main.WS_PROCESS_SO_STATUS, "SEND");

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void processWS_SaveReturn(String mLink) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        ArrayList<TSave_Rec.Error_Process> errorProcesses = null;
        try {
            errorProcesses = gson.fromJson(
                    mLink,
                    new TypeToken<ArrayList<TSave_Rec.Error_Process>>() {
                    }.getType()
            );
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        //
        if(errorProcesses != null && errorProcesses.size() > 0){
            ArrayList<HMAux> auxResults = new ArrayList<>();
            for (TSave_Rec.Error_Process error_process : errorProcesses) {
                //
                HMAux mHmAux = ToolBox_Inf.getWsSaveErrorProcessAuxResult(error_process);
                //
                HMAux aux = new HMAux();
                switch (mHmAux.get("type")) {
                    case ConstantBaseApp.SYS_STATUS_SCHEDULE:
                        aux.put(Generic_Results_Adapter.LABEL_TTL, mHmAux.get("label"));
                        aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mHmAux.get("final_status")+"\n"+mHmAux.get("status"));
                        break;
                    case TSave_Rec.Error_Process.ERROR_TYPE_TICKET:
                        aux.put(Generic_Results_Adapter.LABEL_TTL, mHmAux.get("label"));
                        aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mHmAux.get("final_status")+"\n"+mHmAux.get("status"));
                        break;
                }
                //
                auxResults.add(aux);
            }
            //
            mView.addResultList(auxResults);
        }

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
            //TODO RFAZER METODO
            if(tkTicket.getStep() != null && tkTicket.getStep().size() > 0){
                for (TK_Ticket_Step ticketStep : tkTicket.getStep()) {
                    ticketStep.setStep_status(finalStatus);
                    //
                    if(ticketStep.getCtrl() != null && ticketStep.getCtrl().size() > 0){
                        for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
                            ticketCtrl.setCtrl_status(finalStatus);
                            ticketCtrl.copyCtrlStatusForInnerProcess();
                        }
                    }
                }
            }
            //
            tkTicket.setTicket_status(finalStatus);
            scheduleExec.setStatus(finalStatus);
            scheduleExec.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            //
            DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
            if(!daoObjReturn.hasError()){
                daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
                //LUCHE - 18/01/2021 - Remove contador do app.
                checkAppExecutionDecrementUpdateNeeds(String.valueOf(scheduleExec.getSite_code()));
                //
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

    /**
     * LUCHE - 18/01/2021
     * Metodo que verifica se customer tem licença por site e se site não tem licença ativa  para entrão
     * atualizar o contador interno, removendo 1 do contador d app;
     * @param site_code
     */
    private void checkAppExecutionDecrementUpdateNeeds(String site_code) {
        if( ToolBox_Inf.isConcurrentBySiteLicense(context)
            && ToolBox_Inf.isSiteLicenseDisabled(context,site_code)
        ) {
            MD_SiteDao siteDao = new MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            MD_Site mdSite = ToolBox_Inf.getSiteObjInfo(context, site_code);
            mdSite.decreaseAppExecution();
            siteDao.addUpdate(mdSite);
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
            //LUCHE - 26/06/2020
            //Substituido chamada do conjunto WBR e WS_Upload_img, pelo Worker_Upload_img
            ToolBox_Inf.scheduleUploadImgWork(context);
        }
    }

    /**
     * LUCHE - 30/11/2020
     * Metodo que verifica necessidade de fechar o ticket se for ultimo processo e que exibe
     * msg de dados salvos offline por causa de form espontaneo em aberto.
     * @param context
     * @param mActionPrefix
     * @param mActionCode
     */
    @Override
    public void proceedOffHandSaveFlow(Context context, int mActionPrefix, int mActionCode) {
        //Se falso, será exibi msg na tela.
        if (checkOfflineTicketDone(mView.getAction())) {
            mView.showAlert(
                hmAux_Trans.get("alert_offline_save_by_open_form_ttl"),
                hmAux_Trans.get("alert_offline_save_by_open_form_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.postTicketSave();
                    }
                }
            );
        }
    }

    @Override
    public void execTicketSave(boolean forceOfflineProcess) {
        if (!forceOfflineProcess && ToolBox_Con.isOnline(context)) {
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
                String ticketPk = mPrefix + "." + mCode;
                //
                for (WS_TK_Ticket_Save.TicketSaveActReturn actReturn : checkinReturns) {
                    String ticketCode = actReturn.getPrefix() + "." + actReturn.getCode();
                    //Se a ação da tela é agendamento  e a pk do agendamento bater com a retornada,
                    //atualiza o prefix e code para gerar o extrato correto.
                    if (isScheduleCreationForThisAction(actReturn)) {
                        mPrefix = actReturn.getPrefix();
                        mCode = actReturn.getCode();
                        ticketPk = mPrefix + "." + mCode;
                        mView.updateTicketPk(mPrefix,mCode);
                    }
                    //
                    if (!auxResult.containsKey(ticketCode)
                        || (auxResult.containsKey(ticketCode)
                        && !actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK))
                    ) {
                        //Se erro, verifica se erro de processamento qual erro foi e pega msg
                        //auxResult.put(ticketCode, getResultMsgFormmated(actReturn));
                        if(actReturn.isProcessError()){
                            ticketResult = !actReturn.isProcessError();
                            auxResult.put(ticketCode, actReturn.getRetMsg());
                        }
                    }
                }
                //For no resumido por ticket montando msg a ser exibida
                for (Map.Entry<String, String> item : auxResult.entrySet()) {
                    HMAux hmAux = new HMAux();
                    //
                    //Monta HmAux
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("ticket_lbl"));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.getKey());
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.getValue());
                    //
                    if (item.getKey().equals(ticketPk)) {
                        //05/08/2020 - Modificado o set para ser feito no primeiro loop
                        //ticketResult = item.getValue().equals(ConstantBaseApp.MAIN_RESULT_OK);
                        resultList.add(ticketNextIdx, hmAux);
                        ticketNextIdx++;
                    } else {
                        resultList.add(hmAux);
                    }
                }
                //
                mView.addResultList(resultList);
                mView.showResult(ticketResult);
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

    /**
     * LUCHE - 10/09/2020
     * DEVE SEMPRE SER PRECEDIDO DA CHAMADA DO hasFormWaitingSyncWithinTicket
     * Metodo que define fluxo quando identificado que existe form pendente de envio para o ticket
     * @param mActionPrefix
     * @param mActionCode
     */
    @Override
    public void defineFormWaitingSyncFlow(int mActionPrefix, int mActionCode) {
        if(ToolBox_Inf.hasFormGpsPendencyWithinTicket(context,mActionPrefix,mActionCode)){
            if (checkOfflineTicketDone(mView.getAction())) {
                mView.showAlert(
                    hmAux_Trans.get("alert_form_location_pendency_ttl"),
                    hmAux_Trans.get("alert_offline_save_by_location_pendency_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.postTicketSave();
                        }
                    }
                );
            }
        }else{
            if(ToolBox_Con.isOnline(context)) {
                callWsSave();
            }else{
                execTicketSave(true);
            }
        }
    }

    @Override
    public void defineNextSaveFlow(int mActionPrefix, int mActionCode) {
        if(ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mActionPrefix, mActionCode)){
            defineFormWaitingSyncFlow(mActionPrefix, mActionCode);
        }else {
            if(ToolBox_Inf.hasOffHandFormInProcess(context,mActionPrefix,mActionCode)){
                execTicketSave(true);
            }else {
                execTicketSave(false);
            }
        }
    }

    @Override
    public void executeSerialSave() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Save.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("progress_serial_save_ttl"),
                hmAux_Trans.get("progress_serial_save_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Save.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
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
    public void processWsSerialSavelReturn(HMAux hmAux) {
        if (!hmAux.isEmpty() && hmAux.size() > 0) {
            ArrayList<HMAux> hmAuxList = new ArrayList<>();
            for (Map.Entry<String, String> item : hmAux.entrySet()) {
                HMAux aux = new HMAux();
                /**
                 * [0] - Product_code
                 * [1] - Serial ID
                 */
                String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                String status = item.getValue();
                String productInfo = getFormatedProductInfo(getMdProduct(ToolBox_Inf.convertStringToInt(pk[0])));
                //
                aux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("serial_lbl"));
                aux.put(Generic_Results_Adapter.LABEL_ITEM_1, productInfo + " - " + pk[1] );
                aux.put(Generic_Results_Adapter.VALUE_ITEM_1, status);
                //
                if (!ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(status)) {
                    //Só colocado dentro da lista pois o metodo addResultList requer uma lista.
                    hmAuxList.add(aux);
                }
            }
            //
            if(hmAuxList.size() > 0){
                mView.addResultList(hmAuxList);
            }
        }
    }

    private String getFormatedProductInfo(MD_Product mdProduct) {
        if (mdProduct != null) {
            return mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc();
        } else {
            return "";
        }
    }

    private MD_Product getMdProduct(int product_code) {
        MD_Product md_product;
        MD_ProductDao md_productDao = new MD_ProductDao(
            context,
            ToolBox_Con.customDBPath(
                ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        md_product = md_productDao.getByString(
            new MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery()
        );
        return md_product;
    }

    private boolean isScheduleCreationForThisAction(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
        return
            mView.isScheduledTicket()
                && actReturn.getSchedulePrefix() == mView.getmSchedulePrefix()
                && actReturn.getScheduleCode() == mView.getmScheduleCode()
                && actReturn.getScheduleExec() == mView.getmScheduleExec();
    }

    @Override
    //TODO REVE SE MOVER PARA O STEP
    public String hasCheckinBlockBy(int ticket_prefix, int ticket_code) {
        TK_Ticket ticket = getTicketbyPk(ticket_prefix, ticket_code);
        String preference_user_code = ToolBox_Con.getPreference_User_Code(context);
//        if (preference_user_code != null) {
//            if (!preference_user_code.equals(ticket.getCheckin_user()) && ticket.getCheckin_date() != null) {
//
//                return getFormattedInfo(ticket.getCheckin_date(), ticket.getCheckin_user_name());
//            }
//        }
        return "";
    }

    private String getResultMsgFormmated(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
        if ( actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)
            && (actReturn.getRetMsg() == null || actReturn.getRetMsg().isEmpty())
        ) {
            return actReturn.getRetStatus();
        } else {
            return actReturn.getRetStatus() + "\n" + actReturn.getRetMsg();
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
                //LUCHE - 13/11/2020
                //Modificado navegação pos save para sempre navegar para act070.
                mView.callAct070();
            }else{
                mView.callAct017();
            }
        }
    }

    @Override
    public TK_Ticket getTicketbyPk(int ticket_prefix, int ticket_code) {
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
        //TODO REFAZER METODO
        for (TK_Ticket_Step tkTicketStep : tkTicket.getStep()) {
            if (!ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(tkTicketStep.getStep_status())
                && !ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equalsIgnoreCase(tkTicketStep.getStep_status())) {
                return true;
            }
        }
        //
        return false;
    }

    /**
     * LUCHE - 13/11/2020
     * Metodo que controla a visibilida das infos de produto e serial
     * @param mActionPrefix
     * @param mActionCode
     * @param mTicketCtrl
     * @param tvProduct
     * @param tvSerial
     */
    @Override
    public void defineProductSerialViews(int mActionPrefix, int mActionCode, TK_Ticket_Ctrl mTicketCtrl, TextView tvProduct, TextView tvSerial) {
        TK_Ticket tkTicket = getTicketbyPk(mActionPrefix, mActionCode);
        int visibility = (mTicketCtrl.getProduct_code() != null
            && tkTicket.getOpen_product_code() != mTicketCtrl.getProduct_code())
            || (mTicketCtrl.getSerial_id() != null
            && !tkTicket.getOpen_serial_id().equals(mTicketCtrl.getSerial_id())) ? View.VISIBLE : View.GONE;

        if(mTicketCtrl.getProduct_code() != null &&  mTicketCtrl.getProduct_desc() != null && !mTicketCtrl.getProduct_desc().isEmpty()){
          tvProduct.setText(mTicketCtrl.getProduct_desc());
          tvProduct.setVisibility(visibility);
        }
        if(mTicketCtrl.getSerial_id() != null && !mTicketCtrl.getSerial_id().isEmpty()){
            tvSerial.setText(mTicketCtrl.getSerial_id());
            tvSerial.setVisibility(visibility);
        }
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
//              LUCHE -06/08/2020 - NÃO DEVE MAIS ACONTECER.....
//            case ConstantBaseApp.ACT069:
//                mView.callAct069(true);
//                break;
            case ConstantBaseApp.ACT012:
            case ConstantBaseApp.ACT074:
            case ConstantBaseApp.ACT068:
                if(mView.has_tk_ticket_is_form_off_hand()){
                    mView.callAct081();
                }else{
                    mView.callAct070();
                }
                break;
            case ConstantBaseApp.ACT070:

            case ConstantBaseApp.ACT076:
            default:
                mView.callAct070();
                break;
        }
    }

}
