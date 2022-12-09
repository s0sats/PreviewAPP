package com.namoadigital.prj001.model;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class MD_Schedule_Exec {
    @SerializedName("customer_code") private long customer_code;
    @SerializedName("schedule_prefix") private int schedule_prefix;
    @SerializedName("schedule_code") private int schedule_code;
    @SerializedName("schedule_exec") private int schedule_exec;
    @SerializedName("schedule_desc") private String schedule_desc;
    @SerializedName("schedule_type") private String schedule_type;
    @SerializedName("status") private String status;
    @SerializedName("site_code") private int site_code;
    @SerializedName("site_id") private String site_id;
    @SerializedName("site_desc") private String site_desc;
    @SerializedName("zone_code") private Integer zone_code;
    @SerializedName("zone_id") private String zone_id;
    @SerializedName("zone_desc") private String zone_desc;
    @SerializedName("operation_code") private int operation_code;
    @SerializedName("operation_id") private String operation_id;
    @SerializedName("operation_desc") private String operation_desc;
    @SerializedName("product_code") private int product_code;
    @SerializedName("product_id") private String product_id;
    @SerializedName("product_desc") private String product_desc;
    @Nullable
    @SerializedName("serial_code") private Integer serial_code;
    @Nullable
    @SerializedName("serial_id") private String serial_id;
    @Nullable
    @SerializedName("custom_form_type") private Integer custom_form_type;
    @Nullable
    @SerializedName("custom_form_code") private Integer custom_form_code;
    @Nullable
    @SerializedName("custom_form_version") private Integer custom_form_version;
    @Nullable
    @SerializedName("custom_form_desc") private String custom_form_desc;
    @Nullable
    @SerializedName("ticket_type") private Integer ticket_type;
    @Nullable
    @SerializedName("ticket_type_id") private String ticket_type_id;
    @Nullable
    @SerializedName("ticket_type_desc") private String ticket_type_desc;

    @SerializedName("local_control") private int local_control;
    @SerializedName("io_control") private int io_control;
    @Nullable
    @SerializedName("serial_rule") private String serial_rule;
    @Nullable
    @SerializedName("serial_min_length") private Integer serial_min_length;
    @Nullable
    @SerializedName("serial_max_length") private Integer serial_max_length;
    @SerializedName("site_restriction") private int site_restriction;
    @Nullable
    @SerializedName("product_icon_name") private String product_icon_name;
    @Nullable
    @SerializedName("product_icon_url") private String product_icon_url;
    @SerializedName("product_icon_url_local") private String product_icon_url_local;
    @SerializedName("require_location") private int require_location;

    @SerializedName("date_start") private String date_start;
    @SerializedName("date_end") private String date_end;
    @Nullable
    @SerializedName("comments") private String comments;
    @SerializedName("require_serial") private int require_serial;
    @SerializedName("allow_new_serial_cl") private int allow_new_serial_cl;
    @SerializedName("require_serial_done") private int require_serial_done;
    @SerializedName("sync_process") private int sync_process;
    @Nullable
    @SerializedName("fcm_new_status") private String fcm_new_status;
    @Nullable
    @SerializedName("fcm_user_nick") private String fcm_user_nick;
    @Nullable
    @SerializedName("schedule_erro_msg") private String schedule_erro_msg;
    @Nullable
    @SerializedName("close_date") private String close_date;
    @SerializedName("tag_operational_code") private int tag_operational_code;
    @SerializedName("tag_operational_id") private String tag_operational_id;
    @SerializedName("tag_operational_desc") private String tag_operational_desc;
    @Nullable
    @SerializedName("has_Nc") private Integer has_Nc;
    //LUCHE - 17/09/2021 - Flag que indica se originalmente o agendamento tinha um serial
    @SerializedName("serial_defined_by_server") private int serial_defined_by_server;
    @SerializedName("is_so") private int is_so;//indica se o form é tipo o.s

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

    public Integer getZone_code() {
        return zone_code;
    }

    public void setZone_code(Integer zone_code) {
        this.zone_code = zone_code;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getZone_desc() {
        return zone_desc;
    }

    public void setZone_desc(String zone_desc) {
        this.zone_desc = zone_desc;
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

    public int getIs_so() {
        return is_so;
    }

    public void setIs_so(int is_so) {
        this.is_so = is_so;
    }

    public MyActions toMyActionsObj(Context context, @Nullable String lastScheduleSelected) {
        String customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
        String statusToUse = ConstantBaseApp.SYS_STATUS_IN_PROCESSING.equals(status) ? ConstantBaseApp.SYS_STATUS_PROCESS : status;
        Integer midIcon = R.drawable.ic_baseline_cloud_done_24_blue;
        Integer rightIcon = null;
        if (status != null){
            switch (status) {
                case ConstantBaseApp.SYS_STATUS_WAITING_SYNC:
                    midIcon = R.drawable.ic_cloud_upload_24_red;
                    break;
                case ConstantBaseApp.SYS_STATUS_NOT_EXECUTED:
                case ConstantBaseApp.SYS_STATUS_CANCELLED:
                    rightIcon = R.drawable.ic_baseline_cancel_24;
                    break;
                case ConstantBaseApp.SYS_STATUS_ERROR:
                case ConstantBaseApp.SYS_STATUS_IGNORED:
                    midIcon = R.drawable.ic_baseline_clear_24dp_red;
                    break;
                case ConstantBaseApp.SYS_STATUS_IN_PROCESSING:
                case ConstantBaseApp.SYS_STATUS_PROCESS:
                    midIcon = R.drawable.ic_baseline_cloud_upload_24_gray;
                    rightIcon = R.drawable.ic_baseline_group_24;
                    break;
                case ConstantBaseApp.SYS_STATUS_SCHEDULE:
                    rightIcon = R.drawable.ic_baseline_group_24;
                    break;
                case ConstantBaseApp.SYS_STATUS_DONE:
                    rightIcon = R.drawable.ic_baseline_check_circle_24;
                    break;
                default:
                    midIcon = R.drawable.ic_baseline_cloud_done_24_blue;
                    break;
            }
        }

        Integer leftIcon =
            has_Nc != null && has_Nc.equals(1)
                    ? R.drawable.ic_baseline_report_24_yellow
                    : null;
        String doneDate = null;
        if(close_date != null){
            doneDate = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(close_date),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            );
        }
        //
        String formattedZone = ToolBox_Inf.getProductSerialZone(
                context,
                site_code,
                zone_desc,
                new MD_Product_SerialDao(context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                product_code,
                serial_id
        );
        //
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
                midIcon,
                rightIcon,
                //LUCHE - getStepStartEndDateFormated ao inves do metodo scheduled, pois la espera da formtada igual exibição
                ToolBox_Inf.getMyActionStartEndDateFormated(context, date_start + " " + customerGMT, date_end + " " + customerGMT),
                tag_operational_desc,
                product_desc,
                serial_id == null ? "" : serial_id,
                schedule_desc,
                ConstantBaseApp.MD_SCHEDULE_TYPE_FORM.equals(schedule_type) ? custom_form_desc : ticket_type_desc,
                    comments,
                null,
                site_code,
                site_desc,
                    formattedZone,
                null,
                doneDate,
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(date_start + " " + customerGMT),
                    "yyyyMMddHHmm"
                ),
                null,
                -1,
                ConstantBaseApp.SYS_STATUS_IN_PROCESSING.contentEquals(status),
                ToolBox_Inf.isItemLate(date_start + " " + customerGMT),
                ToolBox_Inf.isItemLate(date_end + " " + customerGMT),
                isLastSelectedItem,
                    false,
                    false,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null,
                    null,
                    null
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
                leftIcon,
                midIcon,
                rightIcon,
                //LUCHE - getStepStartEndDateFormated ao inves do metodo scheduled, pois la espera da formtada igual exibição
                ToolBox_Inf.getMyActionStartEndDateFormated(context, date_start + " " + customerGMT, date_end + " " + customerGMT),
                tag_operational_desc,
                product_desc,
                serial_id == null ? "" : serial_id,
                schedule_desc,
                ConstantBaseApp.MD_SCHEDULE_TYPE_FORM.equals(schedule_type) ? custom_form_desc : ticket_type_desc,
                null,
                comments,
                site_code,
                site_desc,
                    formattedZone,
                null,
                close_date,
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(date_start + " " + customerGMT),
                    "yyyyMMddHHmm"
                ),
                null,
                -1,
                ConstantBaseApp.SYS_STATUS_IN_PROCESSING.contentEquals(status),
                ToolBox_Inf.isItemLate(date_start + " " + customerGMT),
                ToolBox_Inf.isItemLate(date_end + " " + customerGMT),
                isLastSelectedItem,
                    false,
                    false,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null,
                    null,
                    null
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
