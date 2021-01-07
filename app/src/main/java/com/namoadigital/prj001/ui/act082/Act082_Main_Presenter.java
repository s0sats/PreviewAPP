package com.namoadigital.prj001.ui.act082;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.Act082_Form_Data;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Main_User_Rec;
import com.namoadigital.prj001.receiver.WBR_TK_Header_N_Group_Save;
import com.namoadigital.prj001.receiver.WBR_TK_Main_User_List;
import com.namoadigital.prj001.service.WS_TK_Header_N_Group_Save;
import com.namoadigital.prj001.service.WS_TK_Main_User_List;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Act082_Main_Presenter implements Act082_Main_Contract.I_Presenter {
    Context context;
    Act082_Main_Contract.I_View mView;
    HMAux hmAux_trans;
    private TK_TicketDao ticketDao;
    private ArrayList<HMAux> mainUserOptionList;

    public Act082_Main_Presenter(Context context, Act082_Main_Contract.I_View mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
        //
        this.ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public boolean getDateEditionProfile() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_EDIT_FORECAST);
    }

    @Override
    public boolean getHeaderEditionProfile() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_EDIT_HEADER);
    }

    @Override
    public boolean getStepEditTimeProfile() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_STEP_EDIT_TIME);
    }

    @Override
    public void onBackPressedClicked(String mainRequestingAct) {

    }

    @Override
    public TK_Ticket getTicketData(int ticketPrefix, int ticketCode) {
        TK_Ticket ticket = ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticketPrefix,
                        ticketCode
                ).toSqlQuery()
        );
        //
        return ticket;
    }

    @Override
    public void callMainUserService(TK_Ticket ticket) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Main_User_List.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_main_user_search_ttl"),
                    hmAux_trans.get("dialog_main_user_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Main_User_List.class);
            Bundle bundle = new Bundle();
            //
            int edit_header = getHeaderEditionProfile()? 1 : 0;
            //
            bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticket.getTicket_prefix());
            bundle.putInt(TK_TicketDao.TICKET_CODE, ticket.getTicket_code());
            bundle.putInt(TK_TicketDao.SCN, ticket.getScn());
            bundle.putInt(WS_TK_Main_User_List.EDIT_HEADER, edit_header);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
            mView.handleReadOnly(true);
        }
    }

    @Override
    public void setMainUserList(String mLink) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        List<T_TK_Main_User_Rec> main_user_list = gson.fromJson(mLink, new TypeToken<ArrayList<T_TK_Main_User_Rec>>() {}.getType());
        ArrayList<HMAux> hmAuxMainUse = new ArrayList<>();
        for(T_TK_Main_User_Rec item: main_user_list){
            HMAux hmAux = new HMAux();
            //
            hmAux.put(SearchableSpinner.CODE, String.valueOf(item.getUser_code()));
            hmAux.put(SearchableSpinner.ID, item.getUser_nick());
            hmAux.put(SearchableSpinner.DESCRIPTION, item.getUser_name());
            //
            hmAuxMainUse.add(hmAux);
        }
        mView.setMainUserSSList(hmAuxMainUse);

    }

    @Override
    public String getDateFromForm(String date, String time) {
        String dateTime = date + " " + time;

        return dateTime;
    }

    @Override
    public String getTimeFromForm(String day, String hour, String minutes) {
        String time = day + " " + hour + ":" + minutes;
        return time;

    }

    @Override
    public void callEditHeaderService(int ticket_prefix, int ticket_code, int scn, Integer main_user_code, String main_user_name, String main_user_nick, String forecast_time, String start_date, String forecast_date, String timeAction, String internalComments, int move_other_date, int move_steps) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Header_N_Group_Save.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_edit_header_date_ttl"),
                    hmAux_trans.get("dialog_edit_header_date_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Header_N_Group_Save.class);
            Bundle bundle = new Bundle();
            //
            bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticket_prefix);
            bundle.putInt(TK_TicketDao.TICKET_CODE, ticket_code);
            bundle.putInt(TK_TicketDao.SCN, scn);
            if(main_user_code != -1) {
                bundle.putInt(TK_TicketDao.MAIN_USER, main_user_code);
                bundle.putString(TK_TicketDao.MAIN_USER_NAME, main_user_name);
                bundle.putString(TK_TicketDao.MAIN_USER_NICK, main_user_nick);
            }
            if(start_date != null) {
                bundle.putString(TK_TicketDao.START_DATE, start_date);
            }
            //
            if(forecast_date != null) {
                bundle.putString(TK_TicketDao.FORECAST_DATE, forecast_date);
            }
            //
            if(forecast_time != null) {
                bundle.putString(TK_TicketDao.FORECAST_TIME, forecast_time);
            }
            //
            if(internalComments != null) {
                bundle.putString(TK_TicketDao.INTERNAL_COMMENTS, internalComments);
            }
            bundle.putString(WS_TK_Header_N_Group_Save.TIME_ACTION, timeAction);
            bundle.putInt(WS_TK_Header_N_Group_Save.MOVE_OTHER_DATE, move_other_date);
            bundle.putInt(WS_TK_Header_N_Group_Save.MOVE_STEPS,  move_steps);
            bundle.putBoolean(WS_TK_Header_N_Group_Save.IS_HEADER_DATETIME_CHANGES, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public Long getElapsedTime(TK_Ticket mTk_ticket) {
        long start_date = ToolBox_Inf.dateToMilliseconds(mTk_ticket.getStart_date());
        long current_date = ToolBox_Inf.dateToMilliseconds(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        long elapsed_time = current_date - start_date;
        return elapsed_time;
    }

    @Override
    public String getFormattedDate(long time) {
        boolean negativeTime = false;
        String formattedDate;

        if(time < 0){
            time = time * -1;
            negativeTime = true;
        }

        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        String sign = "";
        if(negativeTime){
            sign = "-";
        }
        formattedDate = sign + days + " " + (hours % 24 > 9 ? hours % 24 : "0" + hours % 24 ) + ":" + (minutes % 60 > 9 ? minutes % 60 : "0" +  minutes % 60 ) ;
        return formattedDate;
    }

    @Override
    public Long getRemainingTime(TK_Ticket mTk_ticket) {
        long forecast_date =0;
        if(mTk_ticket.getForecast_date() != null) {
            forecast_date = ToolBox_Inf.dateToMilliseconds(mTk_ticket.getForecast_date());
        }else{
            return null;
        }
        long current_date = ToolBox_Inf.dateToMilliseconds(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        long remaining_time =  forecast_date - current_date;
        return remaining_time;
    }

    @Override
    public boolean hasAnyOnlinePendency(Context context, TK_Ticket tkTicket) {
        return ToolBox_Inf.hasOffHandFormInProcess(context,tkTicket.getTicket_prefix(),tkTicket.getTicket_code())
                || ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, tkTicket.getTicket_prefix(),tkTicket.getTicket_code())
                || ToolBox_Inf.hasFormGpsPendencyWithinTicket(context, tkTicket.getTicket_prefix(),tkTicket.getTicket_code())
                || tkTicket.getUpdate_required() == 1
                || tkTicket.getSync_required() == 1
                || tkTicket.getUpdate_required_product() == 1;
    }

    @Override
    public boolean checkForHeaderEditFileCreation(boolean hasAnyFieldValueChange, HMAux mainUserAux, String internalComment, boolean rb_start_dateStatus, boolean rb_end_dateStatus, boolean rb_timeStatus, String mkStartDate, String mkEndDate, String forecasttimeFromForm, boolean chk_shift_ticket_start_dateChecked, boolean chk_shift_step_start_dateChecked, boolean chk_shift_ticket_end_dateChecked, boolean chk_shift_step_end_dateChecked, boolean chk_shift_step_service_timeChecked) {
        //Stunks, mas se não teve alteração, não precisa salvar o arquivo e o retorn é true.
        if(!hasAnyFieldValueChange){
            return true;
        }
        //
        Act082_Form_Data formData = new Act082_Form_Data(
            mainUserAux.hasConsistentValue(SearchableSpinner.CODE) ?  mainUserAux.get(SearchableSpinner.CODE) : null,
            mainUserAux.hasConsistentValue(SearchableSpinner.ID) ?  mainUserAux.get(SearchableSpinner.ID) : null,
            mainUserAux.hasConsistentValue(SearchableSpinner.DESCRIPTION) ?  mainUserAux.get(SearchableSpinner.DESCRIPTION) : null,
            internalComment,
            getCurrentRdChoice(rb_start_dateStatus,rb_end_dateStatus,rb_timeStatus),
            mkStartDate,
            mkEndDate,
            forecasttimeFromForm,
            chk_shift_ticket_start_dateChecked,
            chk_shift_step_start_dateChecked,
            chk_shift_ticket_end_dateChecked,
            chk_shift_step_end_dateChecked,
            chk_shift_step_service_timeChecked
        );
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonContent = gson.toJson(formData);
        try {
            createHeaderFormEditJsonFile(jsonContent);
            return true;
        } catch (IOException e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
            return false;
        }
    }

    private String getCurrentRdChoice(boolean rb_start_dateStatus, boolean rb_end_dateStatus, boolean rb_timeStatus) {
        if(rb_start_dateStatus){
            return ConstantBaseApp.TK_TICKET_START_DATE;
        }else if(rb_end_dateStatus){
            return ConstantBaseApp.TK_TICKET_FORECAST_DATE;
        } else if(rb_timeStatus){
            return ConstantBaseApp.TK_TICKET_FORECAST_TIME;
        }
        //NUNCA DEVE ACONTECER MAS VAI SABER KKK
        return null;
    }

    private File createHeaderFormEditJsonFile(String workGroupEditionContent) throws IOException {
        File json_file = getHeaderEditionFile();
        if(json_file.exists()){
            json_file.delete();
        }
        ToolBox_Inf.writeIn(workGroupEditionContent, json_file);
        return json_file;
    }

    private File getHeaderEditionFile() {
        return new File(ConstantBaseApp.TICKET_JSON_PATH, ConstantBaseApp.TICKET_HEADER_EDITION_JSON_FILE);
    }

    private File getMainUserListFile() {
        return new File(ConstantBaseApp.TICKET_JSON_PATH, ConstantBaseApp.TICKET_MAIN_USER_LIST_JSON_FILE);
    }

    public ArrayList<HMAux> getSSMainUserList(TK_Ticket mTicket) {
        if(mainUserOptionList != null){
            return mainUserOptionList;
        }
        //Tenta carregar lista do json, já atualiza a propertie
        mainUserOptionList = loadMainUSerListFromJson();
        //Novamente teste o null e retorna lista caso esteja preenchida.
        if(mainUserOptionList != null){
            return mainUserOptionList;
        }else {
            //Se não existe lista em memoria e nem no json, tenta busca.
            if(!hasAnyOnlinePendency(context, mTicket)
            && !ToolBox_Inf.isReadOnlyStatus(mTicket.getTicket_status())) {
                callMainUserService(mTicket);
            }
            return null;
        }
    }

    private ArrayList<HMAux> loadMainUSerListFromJson() {
        File file = getMainUserListFile();
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        try {
            if (file.exists()) {
                ArrayList<T_TK_Main_User_Rec> mainUserObjList = gson.fromJson(
                    ToolBox_Inf.getContents(file),
                    new TypeToken<ArrayList<T_TK_Main_User_Rec>>() {
                    }.getType()
                );
                //
                if (mainUserObjList != null) {
                    return generateHmAuxWorkgroupList(mainUserObjList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        return null;
    }

    private ArrayList<HMAux> generateHmAuxWorkgroupList(ArrayList<T_TK_Main_User_Rec> mainUserObjList) {
        ArrayList<HMAux> hmAuxMainUse = new ArrayList<>();
        //
        for(T_TK_Main_User_Rec item: mainUserObjList){
            HMAux hmAux = new HMAux();
            //
            hmAux.put(SearchableSpinner.CODE, String.valueOf(item.getUser_code()));
            hmAux.put(SearchableSpinner.ID, item.getUser_nick());
            hmAux.put(SearchableSpinner.DESCRIPTION, item.getUser_name());
            //
            hmAuxMainUse.add(hmAux);
        }
        //
        return hmAuxMainUse;
    }

    @Override
    public void processMainUserList() {
        ArrayList<HMAux> hmAuxes = loadMainUSerListFromJson();
        if(hmAuxes != null && hmAuxes.size() > 0 ){
            mainUserOptionList = hmAuxes;
            mView.setMainUserSSList(mainUserOptionList);
        }//msg de erro?
    }

    @Override
    public Act082_Form_Data getFormDataJsonInfo(TK_Ticket tkTicket) {
        File headerEditionFile = getHeaderEditionFile();
        Act082_Form_Data formData = null;
        if(headerEditionFile.exists() && !hasAnyOnlinePendency(context, tkTicket)){
            formData = recoverFormDataFromFile(headerEditionFile);
        }
        //
        if(formData == null){
            formData = new Act082_Form_Data(
                String.valueOf(tkTicket.getMain_user()),
                tkTicket.getMain_user_nick(),
                tkTicket.getMain_user_name(),
                tkTicket.getInternal_comments(),
                null,
                tkTicket.getStart_date(),
                tkTicket.getForecast_date(),
                tkTicket.getForecast_time(),
                false,
                false,
                false,
                false,
                false
            );
        }
        return formData;
    }

    private Act082_Form_Data recoverFormDataFromFile(File headerEditionFile) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Act082_Form_Data formData = null;
        try {
            formData = gson.fromJson(
                ToolBox_Inf.getContents(headerEditionFile),
                Act082_Form_Data.class
            );
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        return formData;
    }

    @Override
    public void deleteHeaderEditionFile() {
        File headerEditionFile = getHeaderEditionFile();
        if(headerEditionFile.exists()){
            headerEditionFile.delete();
        }
    }

    @Override
    public void deleteMainUserListFile() {
        File mainUserListFile = getMainUserListFile();
        if(mainUserListFile.exists()){
            mainUserListFile.delete();
        }
    }
}
