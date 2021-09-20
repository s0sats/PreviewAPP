package com.namoadigital.prj001.model;

import android.content.Context;

import androidx.annotation.Nullable;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class MD_Schedule_Exec {
    private long customer_code;
    private int schedule_prefix;
    private int schedule_code;
    private int schedule_exec;
    private String schedule_desc;
    private String schedule_type;
    private String status;
    private int site_code;
    private String site_id;
    private String site_desc;
    private int operation_code;
    private String operation_id;
    private String operation_desc;
    private int product_code;
    private String product_id;
    private String product_desc;
    @Nullable
    private Integer serial_code;
    @Nullable
    private String serial_id;
    @Nullable
    private Integer custom_form_type;
    @Nullable
    private Integer custom_form_code;
    @Nullable
    private Integer custom_form_version;
    @Nullable
    private String custom_form_desc;
    @Nullable
    private Integer ticket_type;
    @Nullable
    private String ticket_type_id;
    @Nullable
    private String ticket_type_desc;

    private int local_control;
    private int io_control;
    @Nullable
    private String serial_rule;
    @Nullable
    private Integer serial_min_length;
    @Nullable
    private Integer serial_max_length;
    private int site_restriction;
    @Nullable
    private String product_icon_name;
    @Nullable
    private String product_icon_url;
    private String product_icon_url_local;
    private int require_location;

    private String date_start;
    private String date_end;
    @Nullable
    private String comments;
    private int require_serial;
    private int allow_new_serial_cl;
    private int require_serial_done;
    private int sync_process;
    @Nullable
    private String fcm_new_status;
    @Nullable
    private String fcm_user_nick;
    @Nullable
    private String schedule_erro_msg;
    @Nullable
    private String close_date;
    private int tag_operational_code;
    private String tag_operational_id;
    private String tag_operational_desc;
    @Nullable
    private Integer has_Nc;
    //LUCHE - 17/09/2021 - Flag que indica se originalmente o agendamento tinha um serial
    private int serial_defined_by_server;

    /**
     * LUCHE - 14/02/2020
     *
     * Valida se o agendamento passado é valido, ou seja, se obj com valores de pk preenchidos.
     * @param md_schedule_exec
     * @return
     */
    public static boolean isValidScheduleExec(MD_Schedule_Exec md_schedule_exec){
        return  md_schedule_exec != null
                && md_schedule_exec.getCustomer_code() > 0
                && md_schedule_exec.getSchedule_prefix() > 0
                && md_schedule_exec.getSchedule_code() > 0
                && md_schedule_exec.getSchedule_exec() > 0;
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSchedule_prefix() {
        return schedule_prefix;
    }

    public void setSchedule_prefix(int schedule_prefix) {
        this.schedule_prefix = schedule_prefix;
    }

    public int getSchedule_code() {
        return schedule_code;
    }

    public void setSchedule_code(int schedule_code) {
        this.schedule_code = schedule_code;
    }

    public int getSchedule_exec() {
        return schedule_exec;
    }

    public void setSchedule_exec(int schedule_exec) {
        this.schedule_exec = schedule_exec;
    }

    public String getSchedule_desc() {
        return schedule_desc;
    }

    public void setSchedule_desc(String schedule_desc) {
        this.schedule_desc = schedule_desc;
    }

    public String getSchedule_type() {
        return schedule_type;
    }

    public void setSchedule_type(String schedule_type) {
        this.schedule_type = schedule_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public int getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(int operation_code) {
        this.operation_code = operation_code;
    }

    public String getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(String operation_id) {
        this.operation_id = operation_id;
    }

    public String getOperation_desc() {
        return operation_desc;
    }

    public void setOperation_desc(String operation_desc) {
        this.operation_desc = operation_desc;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    @Nullable
    public Integer getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(@Nullable Integer serial_code) {
        this.serial_code = serial_code;
    }

    @Nullable
    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(@Nullable String serial_id) {
        this.serial_id = serial_id;
    }

    @Nullable
    public Integer getCustom_form_type() {
        return custom_form_type;
    }

    public void setCustom_form_type(@Nullable Integer custom_form_type) {
        this.custom_form_type = custom_form_type;
    }

    @Nullable
    public Integer getCustom_form_code() {
        return custom_form_code;
    }

    public void setCustom_form_code(@Nullable Integer custom_form_code) {
        this.custom_form_code = custom_form_code;
    }

    @Nullable
    public Integer getCustom_form_version() {
        return custom_form_version;
    }

    public void setCustom_form_version(@Nullable Integer custom_form_version) {
        this.custom_form_version = custom_form_version;
    }

    @Nullable
    public String getCustom_form_desc() {
        return custom_form_desc;
    }

    public void setCustom_form_desc(@Nullable String custom_form_desc) {
        this.custom_form_desc = custom_form_desc;
    }

    @Nullable
    public Integer getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(@Nullable Integer ticket_type) {
        this.ticket_type = ticket_type;
    }

    @Nullable
    public String getTicket_type_id() {
        return ticket_type_id;
    }

    public void setTicket_type_id(@Nullable String ticket_type_id) {
        this.ticket_type_id = ticket_type_id;
    }

    @Nullable
    public String getTicket_type_desc() {
        return ticket_type_desc;
    }

    public void setTicket_type_desc(@Nullable String ticket_type_desc) {
        this.ticket_type_desc = ticket_type_desc;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public int getLocal_control() {
        return local_control;
    }

    public void setLocal_control(int local_control) {
        this.local_control = local_control;
    }

    public int getIo_control() {
        return io_control;
    }

    public void setIo_control(int io_control) {
        this.io_control = io_control;
    }

    @Nullable
    public String getSerial_rule() {
        return serial_rule;
    }

    public void setSerial_rule(@Nullable String serial_rule) {
        this.serial_rule = serial_rule;
    }

    @Nullable
    public Integer getSerial_min_length() {
        return serial_min_length;
    }

    public void setSerial_min_length(@Nullable Integer serial_min_length) {
        this.serial_min_length = serial_min_length;
    }

    @Nullable
    public Integer getSerial_max_length() {
        return serial_max_length;
    }

    public void setSerial_max_length(@Nullable Integer serial_max_length) {
        this.serial_max_length = serial_max_length;
    }

    public int getSite_restriction() {
        return site_restriction;
    }

    public void setSite_restriction(int site_restriction) {
        this.site_restriction = site_restriction;
    }

    @Nullable
    public String getProduct_icon_name() {
        return product_icon_name;
    }

    public void setProduct_icon_name(@Nullable String product_icon_name) {
        this.product_icon_name = product_icon_name;
    }

    @Nullable
    public String getProduct_icon_url() {
        return product_icon_url;
    }

    public void setProduct_icon_url(@Nullable String product_icon_url) {
        this.product_icon_url = product_icon_url;
    }

    public String getProduct_icon_url_local() {
        return product_icon_url_local;
    }

    public void setProduct_icon_url_local(String product_icon_url_local) {
        this.product_icon_url_local = product_icon_url_local;
    }

    public int getRequire_location() {
        return require_location;
    }

    public void setRequire_location(int require_location) {
        this.require_location = require_location;
    }

    @Nullable
    public String getComments() {
        return comments;
    }

    public void setComments(@Nullable String comments) {
        this.comments = comments;
    }

    public int getRequire_serial() {
        return require_serial;
    }

    public void setRequire_serial(int require_serial) {
        this.require_serial = require_serial;
    }

    public int getAllow_new_serial_cl() {
        return allow_new_serial_cl;
    }

    public void setAllow_new_serial_cl(int allow_new_serial_cl) {
        this.allow_new_serial_cl = allow_new_serial_cl;
    }

    public int getRequire_serial_done() {
        return require_serial_done;
    }

    public void setRequire_serial_done(int require_serial_done) {
        this.require_serial_done = require_serial_done;
    }

    public int getSync_process() {
        return sync_process;
    }

    public void setSync_process(int sync_process) {
        this.sync_process = sync_process;
    }

    @Nullable
    public String getFcm_new_status() {
        return fcm_new_status;
    }

    public void setFcm_new_status(@Nullable String fcm_new_status) {
        this.fcm_new_status = fcm_new_status;
    }

    @Nullable
    public String getFcm_user_nick() {
        return fcm_user_nick;
    }

    public void setFcm_user_nick(@Nullable String fcm_user_nick) {
        this.fcm_user_nick = fcm_user_nick;
    }

    @Nullable
    public String getSchedule_erro_msg() {
        return schedule_erro_msg;
    }

    public void setSchedule_erro_msg(@Nullable String schedule_erro_msg) {
        this.schedule_erro_msg = schedule_erro_msg;
    }

    @Nullable
    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(@Nullable String close_date) {
        this.close_date = close_date;
    }

    public int getTag_operational_code() {
        return tag_operational_code;
    }

    public void setTag_operational_code(int tag_operational_code) {
        this.tag_operational_code = tag_operational_code;
    }

    public String getTag_operational_id() {
        return tag_operational_id;
    }

    public void setTag_operational_id(String tag_operational_id) {
        this.tag_operational_id = tag_operational_id;
    }

    public String getTag_operational_desc() {
        return tag_operational_desc;
    }

    public void setTag_operational_desc(String tag_operational_desc) {
        this.tag_operational_desc = tag_operational_desc;
    }

    public Integer getHas_Nc() {
        return has_Nc;
    }

    public void setHas_Nc(Integer has_Nc) {
        this.has_Nc = has_Nc;
    }

    public int getSerial_defined_by_server() {
        return serial_defined_by_server;
    }

    public void setSerial_defined_by_server(int serial_defined_by_server) {
        this.serial_defined_by_server = serial_defined_by_server;
    }

    public MyActions toMyActionsObj(Context context, @Nullable String lastScheduleSelected) {
        String customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        String statusToUse = ConstantBaseApp.SYS_STATUS_IN_PROCESSING.equals(status) ? ConstantBaseApp.SYS_STATUS_PROCESS : status;
        int rightIcon = R.drawable.ic_baseline_cloud_done_24_blue;
        if (status != null){
            switch (status) {
                case ConstantBaseApp.SYS_STATUS_WAITING_SYNC:
                    rightIcon = R.drawable.ic_cloud_upload_24_red;
                    break;
                case ConstantBaseApp.SYS_STATUS_ERROR:
                case ConstantBaseApp.SYS_STATUS_IGNORED:
                    rightIcon = R.drawable.ic_baseline_clear_24dp_red;
                    break;
                default:
                    rightIcon = R.drawable.ic_baseline_cloud_done_24_blue;
                    break;
            }
        }

        Integer leftIcon =
            has_Nc != null && has_Nc.equals(1)
            ? R.drawable.ic_alert_nc_on
            : null;
        String doneDate = close_date;
        if(close_date != null){
            doneDate = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(close_date),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            );
        }
        String processPk = ToolBox_Inf.formatSchedulePk(schedule_prefix, schedule_code, schedule_exec);
        boolean isLastSelectedItem = processPk.equals(lastScheduleSelected);
        //
        try {
            MyActions myActions = new MyActions(
                MyActions.MY_ACTION_TYPE_SCHEDULE,
                processPk,
                processPk,
                statusToUse,
                ConstantBaseApp.HMAUX_TRANS_LIB.get(statusToUse),
                leftIcon,
                rightIcon,
                //LUCHE - getStepStartEndDateFormated ao inves do metodo scheduled, pois la espera da formtada igual exibição
                ToolBox_Inf.getMyActionStartEndDateFormated(context, date_start + " " + customerGMT, date_end + " " + customerGMT),
                tag_operational_desc,
                product_desc,
                serial_id == null ? "" : serial_id,
                schedule_desc,
                ConstantBaseApp.MD_SCHEDULE_TYPE_FORM.equals(schedule_type) ? custom_form_desc : ticket_type_desc,
                comments,
                site_code,
                site_desc,
                null,
                null,
                null,
                doneDate,
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(date_start + " " + customerGMT),
                    "yyyyMMddHHmm"
                ),
                null,
                ConstantBaseApp.SYS_STATUS_IN_PROCESSING.contentEquals(status),
                ToolBox_Inf.isItemLate(date_start + " " + customerGMT),
                ToolBox_Inf.isItemLate(date_end + " " + customerGMT),
                isLastSelectedItem
            );
            myActions.setProductCode(product_code);
            myActions.setProductId(product_id);
            myActions.setCustomFormDesc(custom_form_desc);
            myActions.setErroMsg(schedule_erro_msg);
            return myActions;
        }catch (Exception e){
            /**
             * Bug muito estranho acontece somente de vez em nunca geralmente quand volta do ticket
             * e não espera carregar a lista de ações.
             * Apesar do apontar processStatus null, o status existe e tem tradução maior hipotese é a
             * var static dar algum pipoco e ser null...
             */
            ToolBox_Inf.registerException(getClass().getName(), new Exception(status +"\n"+ e.getMessage() ));
            //
            MyActions myActions = new MyActions(
                MyActions.MY_ACTION_TYPE_SCHEDULE,
                processPk,
                processPk,
                statusToUse,
                ConstantBaseApp.HMAUX_TRANS_LIB.get(statusToUse),
                null,
                rightIcon,
                //LUCHE - getStepStartEndDateFormated ao inves do metodo scheduled, pois la espera da formtada igual exibição
                ToolBox_Inf.getMyActionStartEndDateFormated(context, date_start + " " + customerGMT, date_end + " " + customerGMT),
                tag_operational_desc,
                product_desc,
                serial_id == null ? "" : serial_id,
                schedule_desc,
                ConstantBaseApp.MD_SCHEDULE_TYPE_FORM.equals(schedule_type) ? custom_form_desc : ticket_type_desc,
                comments,
                site_code,
                site_desc,
                null,
                null,
                null,
                close_date,
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(date_start + " " + customerGMT),
                    "yyyyMMddHHmm"
                ),
                null,
                ConstantBaseApp.SYS_STATUS_IN_PROCESSING.contentEquals(status),
                ToolBox_Inf.isItemLate(date_start + " " + customerGMT),
                ToolBox_Inf.isItemLate(date_end + " " + customerGMT),
                isLastSelectedItem
            );
            myActions.setProductCode(product_code);
            myActions.setProductId(product_id);
            myActions.setCustomFormDesc(custom_form_desc);
            myActions.setErroMsg(schedule_erro_msg);
            return myActions;
        }

    }

    private String formatErrorMsg() {
        String erroMsg = null;
        if(schedule_erro_msg != null && !schedule_erro_msg.isEmpty()){
            erroMsg = schedule_erro_msg;
            if(fcm_user_nick != null && !fcm_user_nick.isEmpty()){
                erroMsg+= " ("+fcm_user_nick+")";
            }
        }
        //
        return erroMsg;
    }
}
