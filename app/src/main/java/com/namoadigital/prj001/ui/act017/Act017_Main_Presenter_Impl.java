package com.namoadigital.prj001.ui.act017;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.MD_Operation_Sql_004;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_006;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.sql.Sql_Act017_001;
import com.namoadigital.prj001.sql.Sql_Act017_002;
import com.namoadigital.prj001.sql.Sql_Act017_003;
import com.namoadigital.prj001.sql.Sql_Act017_004;
import com.namoadigital.prj001.sql.Sql_Act020_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_009;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_010;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;


/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public class Act017_Main_Presenter_Impl implements Act017_Main_Presenter {

    private Context context;
    private Act017_Main_View mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private GE_Custom_Form_ApDao formApDao;
    private HMAux hmAux_Trans;
    private MD_SiteDao siteDao;
    private MD_Schedule_ExecDao scheduleExecDao;
    private ScheduleRequestSerialDialog serialDialog;
    private TK_TicketDao ticketDao;
    private MD_OperationDao operationDao;


    public Act017_Main_Presenter_Impl(Context context, Act017_Main_View mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.formApDao = new GE_Custom_Form_ApDao(context);
        this.hmAux_Trans = hmAux_Trans;
        this.siteDao = new MD_SiteDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        this.scheduleExecDao = new MD_Schedule_ExecDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        this.ticketDao = new TK_TicketDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        this.operationDao = new MD_OperationDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );

    }

    @Override
    public void getSchedules(String selected_date, boolean filter_form, boolean filter_form_ap, boolean filter_ticket, String serial_id, boolean late, boolean filter_site_logged) {
        ArrayList<HMAux> schedules = new ArrayList<>();
        //Se atrasado, ignora data
        if (late) {
            selected_date = null;
        }
        //
        if (filter_form || (!filter_form && !filter_form_ap && !filter_ticket)) {
            ArrayList<HMAux> schedulesForm =
                (ArrayList<HMAux>) formLocalDao.query_HM(
                    new Sql_Act017_001(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        selected_date,
                        serial_id,
                        late,
                        filter_site_logged
                    ).toSqlQuery()
                );
            if (schedulesForm != null) {
                schedules.addAll(schedulesForm);
            }
        }
        //
        if (filter_form_ap || (!filter_form && !filter_form_ap && !filter_ticket)) {
            ArrayList<HMAux> schedulesFormAP =
                (ArrayList<HMAux>) formApDao.query_HM(
                    new Sql_Act017_002(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        selected_date,
                        serial_id,
                        late
                    ).toSqlQuery()
                );
            if (schedulesFormAP != null) {
                schedules.addAll(schedulesFormAP);
            }
        }
        //LUCHE - 11/03/2020
        //Busca ticket agendados.
        if (filter_ticket || (!filter_form && !filter_form_ap && !filter_ticket)) {
            ArrayList<HMAux> schedulesTicket =
                (ArrayList<HMAux>) ticketDao.query_HM(
                    new Sql_Act017_004(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        selected_date,
                        serial_id,
                        late,
                        filter_site_logged
                    ).toSqlQuery()
                );
            if (schedulesTicket != null) {
                schedules.addAll(schedulesTicket);
            }
        }

        //Seta Qtd no tv
        mView.setQty(schedules.size(), getTotalQty(selected_date, filter_form, filter_form_ap, late, filter_site_logged));
        //Ordena agendados por data
        sortSchedulesByDate(schedules);
        //Adiciona datas na lista de agendados e devole lista
        mView.loadSchedules(addDateMsgs(schedules));

    }

    private int getTotalQty(String selected_date, boolean filter_form, boolean filter_form_ap, boolean late, boolean filter_site_logged) {
        HMAux totQtyAux = formApDao.getByStringHM(
//                new Sql_Act017_003(
//                        context,
//                        ToolBox_Con.getPreference_Customer_Code(context),
//                        selected_date,
//                        filter_form,
//                        filter_form_ap,
//                        late,
//                        filter_site_logged
//                ).toSqlQuery()
            new Sql_Act017_003(
                context,
                ToolBox_Con.getPreference_Customer_Code(context),
                selected_date
            ).toSqlQuery()


        );
        //
        if (totQtyAux != null && totQtyAux.containsKey(Sql_Act017_003.TOTAL_QTY)) {
            return Integer.parseInt(totQtyAux.get(Sql_Act017_003.TOTAL_QTY));
        }
        //
        return -1;
    }

    private void sortSchedulesByDate(ArrayList<HMAux> schedules) {
        Collections.sort(schedules, new Comparator<HMAux>() {
            @Override
            public int compare(HMAux hmAux, HMAux t1) {
                return hmAux.get(Act017_Main.ACT017_ADAPTER_DATE_REF_MS).compareTo(t1.get(Act017_Main.ACT017_ADAPTER_DATE_REF_MS));
            }
        });
    }

    /**
     * LUCHE - 12/03/2020
     * <P></P>
     * Metodo que define do fluxo do agendamento baseado no tipo do mesmo
     * @param item - Agendamento selecionado
     */
    @Override
    public void checkScheduleFlow(final HMAux item) {
        switch (item.get(Act017_Main.ACT017_MODULE_KEY)) {
            case Constant.MODULE_CHECKLIST:
                processFormFlow(item);
                break;
            case Constant.MODULE_FORM_AP:
                prepareOpenFormAP(item);
                break;
            case ConstantBaseApp.PROFILE_MENU_TICKET:
                processTicketFlow(item);
                break;
        }
    }

    /**
     * LUCHE - 12/03/2020
     * <P></P>
     * Metodo que define o fluxo quando agendamento d é formulário
     * @param item - Agendamento selecionado
     */
    private void processFormFlow(HMAux item) {
        if (item.get(MD_Schedule_ExecDao.STATUS).equals(Constant.SYS_STATUS_SCHEDULE)) {
            if (isScheduleSiteDifferentThanLogged(item)) {
                startSiteChangeFlow(item);
            } else if (isAnyFormInProcessing(item)) {
                mView.showMsg(Act017_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING, item);
            } else {
                mView.showMsg(Act017_Main.MODULE_CHECKLIST_START_FORM, item);
            }
        } else {
            prepareOpenForm(item);
        }
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que verifica se o site do agendamento é diferente do site logado
     * @param item - Agendamento selecionado
     * @return - Verdadeiro se o site do agendamento for diferente do site logado.
     */
    private boolean isScheduleSiteDifferentThanLogged(HMAux item) {
        return item.get(MD_Schedule_ExecDao.SITE_CODE) != null &&
            !item.get(MD_Schedule_ExecDao.SITE_CODE).equalsIgnoreCase("null") &&
            !item.get(MD_Schedule_ExecDao.SITE_CODE).equalsIgnoreCase(ToolBox_Con.getPreference_Site_Code(context));
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que inicia fluxo para troca de site.
     * @param item - Agendamento selecionado
     */
    private void startSiteChangeFlow(final HMAux item) {
        //Verifica se o usuario possui acesso ao site do form com restrição
        //Se possuir, da opção do usr alterar para o site se não, apenas informa
        //sobre a restrição.
        if (hasScheduleSiteAccess(item.get(MD_Schedule_ExecDao.SITE_CODE))) {
            ToolBox.alertMSG_YES_NO(
                context,
                hmAux_Trans.get("alert_form_site_restriction_ttl"),
                hmAux_Trans.get("alert_form_site_restriction_confirm"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)
                            && !ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)
                        ) {
                            ToolBox_Con.setPreference_Site_Code(context, item.get(MD_Schedule_ExecDao.SITE_CODE));
                            ToolBox_Con.setPreference_Zone_Code(context, -1);
                            //
                            checkScheduleFlow(item);
                        } else {
                            ToolBox_Con.setPreference_Site_Code(context, item.get(MD_Schedule_ExecDao.SITE_CODE));
                            ToolBox_Con.setPreference_Zone_Code(context, -1);
                            mView.callAct033(context);
                        }
                    }
                },
                1
            );
        } else {
            ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_form_site_restriction_ttl"),
                hmAux_Trans.get("alert_form_site_restriction_no_access_msg"),
                null,
                0
            );
        }
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que processo o fluxo do ticket
     * @param item - Agendamento selecionado
     */
    private void processTicketFlow(HMAux item) {
        if(!item.get(MD_Schedule_ExecDao.STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_SCHEDULE)){
            prepareOpenTicket(item);
        }else {
            if(isScheduleSiteDifferentThanLogged(item)){
                startSiteChangeFlow(item);
            }else{
                mView.showMsg(
                    Act017_Main.MODULE_TICKET_EXEC_CONFIRM,
                    item
                );
            }
        }
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que inicia o processo de criação e navegação para o ticket.
     * @param item - Agendamento selecionado
     */
    @Override
    public void checkTicketFlow(HMAux item) {
        if(createTicketForSchedule(item)){
            mView.callAct071(getTicketActionFlowBundle(item));
        }else{
            mView.showMsg(
                Act017_Main.MODULE_SCHEDULE_TICKET_CREATION_ERROR,
                item
            );
        }
    }

    /**
     * LUCHE - 11/03/2020
     * <p></p>
     * Metodo que tenta a criação do ticket caso ele não exista.
     * Caso exista, atualiza item com os valores da pk do ticket.
     * @param item - item selecionado na lista.
     * @return - Verdadeiro se o item já existir na tabela de ticket, ou se ticket criado com sucesso.
     */
    private boolean createTicketForSchedule(HMAux item) {
        if(isTicketAlreadyCreated(item)){
            return true;
        }else{
            int nextTicketCode = getNextScheduleTicketCode();
            MD_Site md_site = getSiteObj(ToolBox_Con.getPreference_Site_Code(context));
            MD_Operation mdOperation = getOperationObj(ToolBox_Con.getPreference_Operation_Code(context));
            //
            if(nextTicketCode > 0 && MD_Site.isValid(md_site) && MD_Operation.isValid(mdOperation)){
                //Cria ticket
                TK_Ticket tkTicket = createTicket(item, nextTicketCode, md_site, mdOperation);
                //Add ctrl e action ao ticket
                tkTicket.getCtrl().add(
                    createTicketCtrl(item, tkTicket, md_site, mdOperation)
                );
                if(updateScheduleStatus(tkTicket.getSchedule_prefix(),tkTicket.getSchedule_code(),tkTicket.getSchedule_exec(), ConstantBaseApp.SYS_STATUS_PROCESS)){
                    DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
                    //
                    if (!daoObjReturn.hasError()) {
                        item.put(TK_TicketDao.TICKET_PREFIX, String.valueOf(tkTicket.getTicket_prefix()));
                        item.put(TK_TicketDao.TICKET_CODE, String.valueOf(tkTicket.getTicket_code()));
                        //EM 13/03/2020, a aexecução do ticket agendado sempre gerar um ticket finalizado, sendo assim, como essa será a unica ação,
                        //é possivel chumbar o valor de ticket_seq como 1, pois sempre será a primeira e unica ação deste ticket.
                        item.put(TK_Ticket_CtrlDao.TICKET_SEQ, "1");
                        return true;
                    }else{
                        updateScheduleStatus(tkTicket.getSchedule_prefix(),tkTicket.getSchedule_code(),tkTicket.getSchedule_exec(), ConstantBaseApp.SYS_STATUS_SCHEDULE);
                    }
                }
            }
        }
        //
        return false;
    }

    private TK_Ticket createTicket(HMAux item, int nextTicketCode, MD_Site md_site, MD_Operation mdOperation) {
        TK_Ticket tkTicket = new TK_Ticket();
        //
        tkTicket.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        tkTicket.setTicket_prefix(0);
        tkTicket.setTicket_code(nextTicketCode);
        tkTicket.setScn(0);
        tkTicket.setTicket_id("");
        tkTicket.setType_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.TICKET_TYPE)));
        tkTicket.setType_id(item.get(MD_Schedule_ExecDao.TICKET_TYPE_ID));
        tkTicket.setType_desc(item.get(MD_Schedule_ExecDao.TICKET_TYPE_DESC));
        tkTicket.setOpen_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        tkTicket.setOpen_user(ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context)));
        tkTicket.setOpen_user_name(
            ToolBox_Inf.getFullNick(
                ToolBox_Con.getPreference_User_Code_Nick(context),
                ToolBox_Con.getPreference_User_Code(context)
            )
        );
        tkTicket.setCurrent_site_code(ToolBox_Inf.convertStringToInt(md_site.getSite_code()));
        tkTicket.setCurrent_site_id(md_site.getSite_id());
        tkTicket.setCurrent_site_desc(md_site.getSite_desc());
        tkTicket.setCurrent_operation_code( (int) mdOperation.getOperation_code());
        tkTicket.setCurrent_operation_id(mdOperation.getOperation_id());
        tkTicket.setCurrent_operation_desc(mdOperation.getOperation_desc());
        tkTicket.setCurrent_product_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
        tkTicket.setCurrent_product_id(item.get(MD_Schedule_ExecDao.PRODUCT_ID));
        tkTicket.setCurrent_product_desc(item.get(MD_Schedule_ExecDao.PRODUCT_DESC));
        tkTicket.setCurrent_serial_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SERIAL_CODE)));
        tkTicket.setCurrent_serial_id(item.get(MD_Schedule_ExecDao.SERIAL_ID));
        tkTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_PROCESS);
        tkTicket.setSchedule_prefix(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)));
        tkTicket.setSchedule_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_CODE)));
        tkTicket.setSchedule_exec(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)));
        //
        return tkTicket;
    }

    private TK_Ticket_Ctrl createTicketCtrl(HMAux item, TK_Ticket tkTicket, MD_Site md_site, MD_Operation mdOperation) {
        TK_Ticket_Ctrl ticketCtrl = new TK_Ticket_Ctrl();
        ticketCtrl.setTicket_seq(1);
        ticketCtrl.setCtrl_type(ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION);
        ticketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_PENDING);
        ticketCtrl.setSite_code(ToolBox_Inf.convertStringToInt(md_site.getSite_code()));
        ticketCtrl.setSite_id(md_site.getSite_id());
        ticketCtrl.setSite_desc(md_site.getSite_desc());
        ticketCtrl.setOperation_code( (int) mdOperation.getOperation_code());
        ticketCtrl.setOperation_id(mdOperation.getOperation_id());
        ticketCtrl.setOperation_desc(mdOperation.getOperation_desc());
        ticketCtrl.setProduct_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
        ticketCtrl.setProduct_id(item.get(MD_Schedule_ExecDao.PRODUCT_ID));
        ticketCtrl.setProduct_desc(item.get(MD_Schedule_ExecDao.PRODUCT_DESC));
        ticketCtrl.setSerial_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SERIAL_CODE)));
        ticketCtrl.setSerial_id(item.get(MD_Schedule_ExecDao.SERIAL_ID));
        ticketCtrl.setCtrl_start_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        ticketCtrl.setCtrl_start_user(ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context)));
        ticketCtrl.setCtrl_start_user_name(
            ToolBox_Inf.getFullNick(
                ToolBox_Con.getPreference_User_Code_Nick(context),
                ToolBox_Con.getPreference_User_Code(context)
            )
        );
        //Add no ctrl
        ticketCtrl.setAction(new TK_Ticket_Action());
        //Seta Pk no controle e action
        ticketCtrl.setPK(tkTicket);
        //
        return ticketCtrl;
    }



    /**
     * LUCHE - 11/03/2020
     * <p></p>
     * Metodo que pega o proximo ticketCode para tickets criados via agendamento.
     * O tickets criados via agendamento, terão sempre o prefixo  = 0.
     * @return - Proximo ticket code  ou -1 em caso de erro.
     */
    private int getNextScheduleTicketCode() {
        HMAux auxCode = ticketDao.getByStringHM(
            new TK_Ticket_Sql_010(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        );
        //
        if (auxCode != null && auxCode.size() > 0 && auxCode.hasConsistentValue(TK_Ticket_Sql_010.NEXT_SCHEDULE_TICKET_CODE) ){
            try{
                return Integer.parseInt(auxCode.get(TK_Ticket_Sql_010.NEXT_SCHEDULE_TICKET_CODE));
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
            }
        }
        return -1;
    }

    /**
     * LUCHE - 11/03/2020
     * <p></p>
     * Metodo que verifica se o ticket ja existe na tabela ticket
     * @param item - item selecionado
     * @return - Verdadeiro se o ticket ja existir na tabela.
     */
    private boolean isTicketAlreadyCreated(HMAux item) {
        TK_Ticket tkTicket = getTicketBySchedule(item);
        if(tkTicket != null && TK_Ticket.isValidTkTicket(tkTicket) ){
            item.put(TK_TicketDao.TICKET_PREFIX, String.valueOf(tkTicket.getTicket_prefix()));
            item.put(TK_TicketDao.TICKET_CODE, String.valueOf(tkTicket.getTicket_code()));
            //EM 13/03/2020, a aexecução do ticket agendado sempre gerar um ticket finalizado, sendo assim, como essa será a unica ação,
            //é possivel chumbar o valor de ticket_seq como 1, pois sempre será a primeira e unica ação deste ticket.
            item.put(TK_Ticket_CtrlDao.TICKET_SEQ, "1");
            return true;
        }
        return false;
    }
    /**
     * LUCHE - 11/03/2020
     * <p></p>
     * Metodo que resgata o ticket da tabela de ticket.
     * @param item - item selecionado
     * @return - Obj ticket ou null se não encontrar
     */
    private TK_Ticket getTicketBySchedule(HMAux item) {
        return ticketDao.getByString(
            new TK_Ticket_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX),
                item.get(MD_Schedule_ExecDao.SCHEDULE_CODE),
                item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)
            ).toSqlQuery()
        );
    }

    private void prepareOpenTicket(HMAux item) {
        if(ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_PREFIX)) > 0
           && ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_CODE)) > 0
        ){
            mView.callAct070(getTicketFlowBundle(item));
        }else{
            mView.callAct071(getTicketActionFlowBundle(item));
        }

    }

    private Bundle getTicketFlowBundle(HMAux item) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT017);
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_PREFIX)));
        bundle.putInt(TK_TicketDao.TICKET_CODE, ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_CODE)));
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, ToolBox_Inf.convertStringToInt(item.get(TK_Ticket_CtrlDao.TICKET_SEQ)));
        bundle.putString(Constant.ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
        return bundle;
    }

    private Bundle getTicketActionFlowBundle(HMAux item) {
       Bundle bundle = new Bundle();
        //
       bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT017);
       bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_PREFIX, ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)));
       bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_CODE, ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_CODE)));
       bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_EXEC, ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)));
       bundle.putInt(TK_TicketDao.TICKET_PREFIX, ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_PREFIX)));
       bundle.putInt(TK_TicketDao.TICKET_CODE, ToolBox_Inf.convertStringToInt(item.get(TK_TicketDao.TICKET_CODE)));
       //EM 13/03/2020, a aexecução do ticket agendado sempre gerar um ticket finalizado, sendo assim, como essa será a unica ação,
        //é possivel chumbar o valor de ticket_seq como 1, pois sempre será a primeira e unica ação deste ticket.
       bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, 1);
       bundle.putString(TK_TicketDao.TICKET_ID, item.get(TK_TicketDao.TICKET_ID));
       bundle.putString(TK_TicketDao.TYPE_PATH, item.get(TK_TicketDao.TYPE_PATH));
       bundle.putString(TK_TicketDao.TYPE_DESC, item.get(TK_TicketDao.TYPE_DESC));
       bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN,false);
       bundle.putString(Constant.ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
        //
        return bundle;
    }

    private boolean hasScheduleSiteAccess(String site_code) {
        boolean access = false;
        //
        MD_Site formSite = getSiteObj(site_code);
        //
        if (formSite != null && formSite.getSite_code().equalsIgnoreCase(site_code)) {
            access = true;
        }
        //
        return access;
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que resgata obj com dados do site.
     * @param site_code - Codigo do Site
     * @return - MD_Site com dados do site ou null se site não encontrado.
     */
    @Nullable
    private MD_Site getSiteObj(String site_code) {
        return siteDao.getByString(
            new MD_Site_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                site_code
            ).toSqlQuery()
        );
    }

    private MD_Operation getOperationObj(long operationCode){
        return operationDao.getByString(
            new MD_Operation_Sql_004(
                ToolBox_Con.getPreference_Customer_Code(context),
                operationCode
            ).toSqlQuery()
        );
    }

    private void prepareOpenFormAP(HMAux hmAux) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017);
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
        bundle.putString(Constant.ACT_SELECTED_DATE, hmAux.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
        //
        mView.callAct038(context, bundle);
    }

    /**
     * LUCHE - 03/03/2020
     * Metdo que define a ação para o form selecionado.
     * @param item
     */
    @Override
    public void checkFormFlow(HMAux item) {
        if (!item.get(MD_Schedule_ExecDao.STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SCHEDULE)) {
            prepareOpenForm(item);
        } else if(hasSerialDefined(item)){
                buildRequestSerialDialog(
                    item,
                    getProduct(Long.parseLong(item.get(MD_Schedule_ExecDao.PRODUCT_CODE))),
                    false
                );
                executeSerialSearch(
                    item.get(MD_Schedule_ExecDao.PRODUCT_CODE),
                    item.get(MD_Schedule_ExecDao.PRODUCT_ID),
                    item.get(MD_Schedule_ExecDao.SERIAL_ID),
                    true
                );
        } else {
            //Cria e exibe dialog que requer serial.
            buildRequestSerialDialog(
                item,
                getProduct(Long.parseLong(item.get(MD_Schedule_ExecDao.PRODUCT_CODE))),
                true
            );
        }
    }

    /**
     *
     * @param item
     * @return
     */
    private Bundle getFormFlowBundle(HMAux item) {
        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, item.get(MD_Schedule_ExecDao.PRODUCT_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, item.get(MD_Schedule_ExecDao.PRODUCT_DESC));
        bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(MD_Schedule_ExecDao.PRODUCT_ID));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(MD_Schedule_ExecDao.SERIAL_ID));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION));
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, item.get(MD_Schedule_ExecDao.SITE_CODE));
        return bundle;
    }

    /**
     * Verifica se  tem serial definido
     * @param item
     * @return
     */
    private boolean hasSerialDefined(HMAux item) {
        return item.hasConsistentValue(MD_Schedule_ExecDao.SERIAL_ID)
            && item.get(MD_Schedule_ExecDao.SERIAL_ID).length() > 0;
    }

    private void prepareOpenForm(final HMAux item) {
        final Bundle bundle = getFormFlowBundle(item);
        mView.callAct011(context,bundle);
    }

    /**
     * LUCHE - 02/03/2020
     * Metodo que  construi dialog para coletar o serial e exibe somente se showDialog for true.
     * Foi feito dessa maneira para aproveitar o dialog como holder da informação do item seleconado,
     * já após retorno do WS é necessario comparar os serial seleciona com os retornados.
     * @param item - Item selecionado
     * @param product - Obj Produto
     * @param showDialog - Flag que indica se o dialog deve ser exibido após criado ou não
     */
    private void buildRequestSerialDialog(final HMAux item, MD_Product product, boolean showDialog) {
        serialDialog = new ScheduleRequestSerialDialog(
            context,
            item,
            product.getSerial_rule(),
            product.getSerial_min_length(),
            product.getSerial_max_length(),
            new ScheduleRequestSerialDialog.OnScheduleRequestSerialDialogListeners() {
                @Override
                public void processToForm() {
                    Bundle bundle = new Bundle();
                    if(createFormLocalForSchedule(item, bundle )){
                        //Atualiza fomr_data no item
                        item.put(
                            GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                            bundle.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,"0")
                        );
                        //
                        prepareOpenForm(item);
                    }else{
                        mView.showMsg(Act017_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR, item);
                    }
                }

                @Override
                public void processToSearchSerial(String serialID) {
                    executeSerialSearch(
                        item.get(MD_Schedule_ExecDao.PRODUCT_CODE),
                        item.get(MD_Schedule_ExecDao.PRODUCT_ID),
                        serialID,
                        false);
                }

                @Override
                public void addMketControl(MKEditTextNM mketSerial) {
                    mView.addControlToActivity(mketSerial);
                }

                @Override
                public void removeMketControl(MKEditTextNM mketSerial) {
                    mView.removeControlFromActivity(mketSerial);
                }
            }
        );
        //
        if(showDialog) {
            serialDialog.show();
        }
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que chama Ws de busca de serial
     * @param productCode - Codigo do produto
     * @param productId - Id do produto
     * @param serialID - Id do serial
     * @param searchExact - Verdadeiro, busca serial exato, se falso, busca por like
     */
    private void executeSerialSearch(String productCode, String productId, String serialID, boolean searchExact) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Search.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE,productCode);
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID,productId);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serialID);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, searchExact ? 1 : 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            offlineSerialSearch();
        }
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que busca o serial offline
     */
    private void offlineSerialSearch() {
        HMAux item = serialDialog.getAuxSchedule();
        ArrayList<MD_Product_Serial> serial_list = hasLocalSerial(
            item.get(MD_Schedule_ExecDao.PRODUCT_ID),
            item.hasConsistentValue(MD_Schedule_ExecDao.SERIAL_ID) ? item.get(MD_Schedule_ExecDao.SERIAL_ID) : serialDialog.getSerialId()
        );
        //
        if (serial_list.size() > 0) {
            defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size());
        } else {
            if ( item.get(MD_Schedule_ExecDao.ALLOW_NEW_SERIAL_CL).equalsIgnoreCase("0")
                 && item.get(MD_Schedule_ExecDao.REQUIRE_SERIAL).equalsIgnoreCase("1")
            ){
                ToolBox_Inf.showNoConnectionDialog(context);
            } else {
                defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size());
            }
        }
    }

    private ArrayList<MD_Product_Serial> hasLocalSerial(String product_id, String serial_id) {
        MD_Product_SerialDao serialDao =
            new MD_Product_SerialDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        ArrayList<MD_Product_Serial> serial_list =
            (ArrayList<MD_Product_Serial>) serialDao.query(
                new Sql_Act020_002(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    ToolBox_Con.getPreference_Site_Code(context),
                    product_id,
                    serial_id,
                    ""
                ).toSqlQuery()
            );

        return serial_list;
    }

    @Override
    public void extractSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
            result,
            TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        defineSearchResultFlow(serial_list, rec.getRecord_count(), rec.getRecord_page());
    }

    private void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page) {
        HMAux item = serialDialog.getAuxSchedule();
        //
        if (ToolBox_Inf.productConfigPreventToProceed(item) && (serial_list == null || serial_list.size() == 0)) {
            //Se serial não definido, significa que não avançou para proxima tela pois o produto não permite criação de serial.
            mView.showMsg(
                !item.get(MD_Schedule_ExecDao.SERIAL_ID).isEmpty() ? Act017_Main.EMPTY_SERIAL_SEARCH : Act017_Main.SERIAL_CREATION_DENIED,
                new HMAux()
            );
        } else {
            int idx = getIdxIfEquals(
                serial_list,
                item.get(MD_Schedule_ExecDao.PRODUCT_CODE),
                item.hasConsistentValue(MD_Schedule_ExecDao.SERIAL_ID) && !item.get(MD_Schedule_ExecDao.SERIAL_ID).isEmpty()
                    ? item.get(MD_Schedule_ExecDao.SERIAL_ID)
                    : serialDialog.getSerialId()
            );
            //
            Bundle bundle = new Bundle();
            bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(MD_Schedule_ExecDao.PRODUCT_ID));
            if (idx >= 0) {
                ArrayList<MD_Product_Serial> serialArrayList = new ArrayList<>();
                serialArrayList.add(serial_list.get(idx));
                //
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serialArrayList);
            } else {
                if (serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(serialDialog.getSerialId())) {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                } else {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                }
            }
            //
            bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, serialDialog.getSerialId());
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
            bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
            bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION));
            bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
            //
            if(createFormLocalForSchedule(item,bundle)){
                mView.callAct020(context, bundle);
            }else{
                mView.showMsg(Act017_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR, item);
            }
        }
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que retorna o indice do serial buscado.
     * Faz loop na lista de seriais retornados e caso encontre o serial, retorna seu indice.
     *
     * @param serial_list - Lista de seriais encontradas
     * @param productCode - Codigo do produto buscado
     * @param serialId - Id do serial buscado
     * @return - Retorna indice do serial buscado ou -1 se serial não encontrado.
     */
    private int getIdxIfEquals(ArrayList<MD_Product_Serial> serial_list, String productCode, String serialId) {
        for (int i = 0; i < serial_list.size();i++) {
            MD_Product_Serial serial = serial_list.get(i);
            if( serial.getProduct_code() == ToolBox_Inf.convertStringToInt(productCode)
                && serial.getSerial_id().equalsIgnoreCase(serialId)
            ){
                return i;
            }
        }
        //
        return -1;
    }

    private boolean createFormLocalForSchedule(HMAux item, Bundle bundle) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        GE_Custom_FormDao custom_formDao = new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao = new GE_Custom_Form_Field_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_FieldDao custom_form_fieldDao = new GE_Custom_Form_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao = new GE_Custom_Form_Blob_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        boolean creationOk = false;
        ///
        if(scheduelFormLocalExists(item,bundle)){
            creationOk = true;
        } else{
            //region Implementação2
            HMAux nextFormData = custom_formDao.getByStringHM(
                new GE_Custom_Form_Local_Sql_002(
                    item.get(MD_Schedule_ExecDao.CUSTOMER_CODE),
                    item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE),
                    item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE),
                    item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION)
                ).toSqlQuery().toLowerCase()
            );
            //
            if (nextFormData != null && nextFormData.size() > 0 && nextFormData.hasConsistentValue("id")) {
                GE_Custom_Form customForm = custom_formDao.getByString(
                    new GE_Custom_Form_Sql_001_TT(
                        item.get(MD_Schedule_ExecDao.CUSTOMER_CODE),
                        item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE),
                        item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE),
                        item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION)
                    ).toSqlQuery().toLowerCase()

                );
                MD_Product productInfo = getProduct(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
                //
                GE_Custom_Form_Local customFormLocal = new GE_Custom_Form_Local();
                //
                customFormLocal.setCustomer_code(customForm.getCustomer_code());
                customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
                customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
                customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
                customFormLocal.setCustom_form_data(Long.parseLong(nextFormData.get("id")));
                customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
                customFormLocal.setCustom_form_status(ConstantBaseApp.SYS_STATUS_SCHEDULE);
                customFormLocal.setCustom_product_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
                customFormLocal.setCustom_product_desc(item.get(MD_Schedule_ExecDao.PRODUCT_DESC));
                customFormLocal.setCustom_product_id(item.get(MD_Schedule_ExecDao.PRODUCT_ID));
                customFormLocal.setCustom_product_icon_name(productInfo.getProduct_icon_name());
                customFormLocal.setCustom_product_icon_url(productInfo.getProduct_icon_url());
                customFormLocal.setCustom_product_icon_url_local(productInfo.getProduct_icon_url_local());
                customFormLocal.setCustom_form_type_desc(item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
                customFormLocal.setCustom_form_desc(item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
                customFormLocal.setSerial_id(item.get(MD_Schedule_ExecDao.SERIAL_ID));
                customFormLocal.setRequire_signature(customForm.getRequire_signature());
                customFormLocal.setAutomatic_fill(customForm.getAutomatic_fill());
                customFormLocal.setSchedule_date_start_format(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT));
                customFormLocal.setSchedule_date_end_format(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT));
                customFormLocal.setSchedule_date_start_format_ms(ToolBox_Inf.dateToMilliseconds(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT)));
                customFormLocal.setSchedule_date_end_format_ms(ToolBox_Inf.dateToMilliseconds(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT)));
                customFormLocal.setRequire_location(customForm.getRequire_location());
                customFormLocal.setRequire_serial_done(customForm.getRequire_serial_done());
                customFormLocal.setSchedule_comments(item.get(MD_Schedule_ExecDao.COMMENTS));
                customFormLocal.setSchedule_prefix(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)));
                customFormLocal.setSchedule_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_CODE)));
                customFormLocal.setSchedule_exec(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)));
                customFormLocal.setSite_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SITE_CODE)));
                customFormLocal.setSite_id(item.get(MD_Schedule_ExecDao.SITE_ID));
                customFormLocal.setSite_desc(item.get(MD_Schedule_ExecDao.SITE_DESC));
                //
                //LUCHE -  14/03/2019
                //Alteração Dao de insert com exception NOVO METODO DAO
                //custom_form_LocalDao.addUpdate(customFormLocal);
                daoObjReturn = formLocalDao.addUpdateThrowException(customFormLocal);
                //
                if (!daoObjReturn.hasError()) {
                    //Seta form data no bundle que será enviado para as proximas acts
                    bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, String.valueOf(customFormLocal.getCustom_form_data()));
                    //
                    ArrayList<HMAux> items = (ArrayList<HMAux>) custom_form_fieldDao.query_HM(
                        new Sql_Act011_002(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            ToolBox_Con.getPreference_Translate_Code(context),
                            String.valueOf(customFormLocal.getCustom_form_data())
                        ).toSqlQuery().toLowerCase()
                    );
                    //
                    custom_form_field_LocalDao.addUpdate(items);
                    //
                    custom_form_blob_localDao.addUpdate(
                        custom_form_blob_localDao.query(
                            new GE_Custom_Form_Blob_Sql_001(
                                String.valueOf(customFormLocal.getCustomer_code()),
                                String.valueOf(customFormLocal.getCustom_form_type()),
                                String.valueOf(customFormLocal.getCustom_form_code()),
                                String.valueOf(customFormLocal.getCustom_form_version())
                            ).toSqlQuery().toLowerCase()
                        )
                        ,
                        false
                    );
                    creationOk = true;
                }
            }
        }

        //
        return creationOk;
    }

    /**
     * LUCHE - 03/03/2020
     *
     * Metodo que verifica se já existe form_local para o agendamento
     * Se existir, atualiza form_data no bundle
     * @param item - Item selecionando
     * @param bundle - Bundle a ser enviado e que tera o custom_form_data setado se existir.
     * @return - Verdadeiro se o form_locla ja existir
     */
    private boolean scheduelFormLocalExists(HMAux item, Bundle bundle) {
        GE_Custom_Form_Local customFormLocal =
            formLocalDao.getByString(
                new MD_Schedule_Exec_Sql_006(
                    item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                    item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX),
                    item.get(MD_Schedule_ExecDao.SCHEDULE_CODE),
                    item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)
                ).toSqlQuery()
            );

        if (customFormLocal != null) {
            bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, String.valueOf(customFormLocal.getCustom_form_data()));
            return true;
        }
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

    /**
     * LUCHE - 12/02/2020
     * Metodo que busca obj do produto usado no form
     * Chamado apenas na criação de form para setar no obj formLocal
     * o nome e URL do icone do prod
     *
     * @param product_code
     * @return
     */
    private MD_Product getProduct(long product_code) {
        MD_ProductDao md_productDao = new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        MD_Product result = md_productDao.getByString(
            new MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery()
        );
        //
        return result != null ? result : new MD_Product();
    }

    public boolean isAnyFormInProcessing(HMAux item) {

        GE_Custom_Form_Local customFormLocal =
            formLocalDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                    item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                    "0",
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                    item.get(GE_Custom_Form_LocalDao.SERIAL_ID)
                ).toSqlQuery()
            );

        if (customFormLocal != null) {
            return true;
        }
        return false;
    }

    private List<HMAux> addDateMsgs(List<HMAux> schedules) {
        List<HMAux> newSchedules = new ArrayList<>();
        String date_ref = "";
        //
        for (int i = 0; i < schedules.size(); i++) {
            if (!date_ref.equals(schedules.get(i).get(Act017_Main.ACT017_ADAPTER_DATE_REF))) {
                date_ref = schedules.get(i).get(Act017_Main.ACT017_ADAPTER_DATE_REF);
                //
                HMAux aux = new HMAux();
                aux.put(Act017_Main.ACT017_MODULE_KEY, Act017_Main.MODULE_SCHEDULE_DATE_REF);
                aux.put(Act017_Main.ACT017_ADAPTER_DATE_REF, getDateDesc(date_ref));
                newSchedules.add(aux);
            }
            //
            newSchedules.add(schedules.get(i));
        }
        //
        return newSchedules;
    }

    @Override
    public String getDateDesc(String scheduled_date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat showFormat = new SimpleDateFormat("EEEE, dd/MMM/yyyy");
        Date date;
        String final_date = "";
        String day_desc = "";
        String month_desc = "";

        try {
            //date = showFormat.format(format.parse(scheduled_date));
            date = format.parse(scheduled_date);
            //
            day_desc = ToolBox_Inf.getDayTranslate(date);
            month_desc = ToolBox_Inf.getMonthTranslate(date);
            //formata data do oracle para format
            //e troca MM por ** para substituir mes por extenso no final.
            String customer_format =
                ToolBox_Con.getPreference_Customer_nls_date_format(context)
                    .replace("DD", "dd")
                    .replace("/", " ")
                    .replace("MM", "**")
                    .replace("RRRR", "yyyy");
            //
            showFormat = new SimpleDateFormat(customer_format);
            final_date = day_desc + ", " + showFormat.format(date).replace("**", month_desc);

        } catch (Exception e) {
            date = format.getCalendar().getTime();
            final_date = showFormat.format(date);
        }

        return final_date;
    }

    /**
     * LUCHE - 21/02/2020
     * Metodo que salva na preferencia o valor do checkbox
     *
     * @param checkboxConstant - Constante da preferecia do checkbox.
     * @param isChecked        - Valor do checkbox.
     */
    @Override
    public void saveCheckBoxStatusIntoPreference(String checkboxConstant, boolean isChecked) {
        ToolBox_Con.setBooleanPreference(context, checkboxConstant, isChecked);
    }

    /**
     * LUCHE - 21/02/2020
     * Metodo que resgata da preferencia o valor do checkbox
     *
     * @param checkboxConstant - Constante da preferecia do checkbox.
     * @param defaultValue     - Caso a preferencia não exista, retorna o valor definido via codigo.
     * @return
     */
    @Override
    public boolean loadCheckboxStatusFromPreferencie(String checkboxConstant, boolean defaultValue) {
        return ToolBox_Con.getBooleanPreferencesByKey(context, checkboxConstant, defaultValue);
    }

    /**
     * LUCHE - 11/03/2020
     * <p>
     * Metodo que devolve o texto a ser exibido no dialog de comentario do agendamento.
     * @param item - Item agendado
     * @return - String
     */
    @Override
    public SpannableString getCommentMessage(HMAux item) {
        SpannableString finalString = null;
        String commentMsg = "";
        switch (item.get(Act017_Main.ACT017_MODULE_KEY)) {
            case Constant.MODULE_CHECKLIST:
                commentMsg =
                    hmAux_Trans.get("dialog_schedule_desc_lbl") +": \n"
                    + item.get(MD_Schedule_ExecDao.SCHEDULE_DESC) + "\n"
                    + hmAux_Trans.get("form_type_dialog_lbl") + ": \n"
                    + item.get(GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE) + " - " + item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC)+ "\n";
                //Seta negrito nas area necessarias
                finalString = new SpannableString(commentMsg);
                try {
                    finalString.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        0,
                        commentMsg.indexOf(item.get(MD_Schedule_ExecDao.SCHEDULE_DESC)),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE

                    );
                    finalString.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        commentMsg.indexOf(hmAux_Trans.get("form_type_dialog_lbl") + ":"),
                        commentMsg.indexOf(item.get(GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE)),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE

                    );
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case ConstantBaseApp.PROFILE_MENU_TICKET:
                commentMsg =
                        hmAux_Trans.get("dialog_schedule_desc_lbl") +": \n"
                        + item.get(MD_Schedule_ExecDao.SCHEDULE_DESC) + "\n";
                //
                finalString = new SpannableString(commentMsg);
                try {
                    finalString.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        0,
                        commentMsg.indexOf(item.get(MD_Schedule_ExecDao.SCHEDULE_DESC)),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE

                    );
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
        }
        //
        return finalString;
    }

    @Override
    public void onBackPressedClicked() {

        if (mView.getmRequesting_ACT().equalsIgnoreCase(Constant.ACT046)) {
            mView.callAct046(context);
        } else {
            mView.callAct016(context);
        }
    }
}
