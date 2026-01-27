package com.namoadigital.prj001.database;

import static com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial.ProductScriptKt.PRODUCT_CREATE_SCRIPT;
import static com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial.ProductSerialScriptKt.PRODUCT_SERIAL_CREATE_SCRIPT;
import static com.namoadigital.prj001.util.ConstantBaseApp.DB_MULTI_STATUS_ERROR;
import static com.namoadigital.prj001.util.ConstantBaseApp.FCM_MODULE_SYNC;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.namoadigital.prj001.database.scripts.multi.FSCreateScriptKt;
import com.namoadigital.prj001.database.scripts.multi.SmSoScriptKt;
import com.namoadigital.prj001.database.scripts.multi.custom_form.GeCustomFormCreateScriptsKt;
import com.namoadigital.prj001.database.scripts.multi.custom_form.GeCustomFormFieldsKt;
import com.namoadigital.prj001.database.scripts.multi.custom_form.GeCustomFormLocalFieldsKt;
import com.namoadigital.prj001.database.scripts.multi.event.EventManualScriptKt;
import com.namoadigital.prj001.database.scripts.multi.ge_os.GeOsDeviceItemScriptKt;
import com.namoadigital.prj001.database.scripts.multi.ge_os.GeOsScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.GEOsVgScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.MDItemCheckLabelIconScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.MDItemCheckLabelScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.MDItemCheckScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.MDProductSerialTpDeviceItemScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.OrderTypeScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.RegionScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.SiteScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.VerificationGroupKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial.MDProductSerialVGScriptKt;
import com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial.TpDeviceItemHistScriptKt;
import com.namoadigital.prj001.database.scripts.multi.ticket.TkCreateScriptsKt;
import com.namoadigital.prj001.database.scripts.multi.ticket.TkTicketVGScriptKt;
import com.namoadigital.prj001.migrations.MigrationsKt;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 18/01/17.
 */

public class DatabaseHelperMulti extends DatabaseBaseHelper {

    private Context context;

    public DatabaseHelperMulti(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, null, DB_VERSION);
        //
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            StringBuilder script = new StringBuilder();
            StringBuilder script_dados = new StringBuilder();
            //
            script.append("create table if not exists [ev_modules] ([module_code] text not null DEFAULT '' COLLATE NOCASE, [module_name] text not null DEFAULT '' COLLATE NOCASE, constraint pk_modules primary key(module_code));");
            script.append("create table if not exists [ev_module_ress] ([module_code] text not null DEFAULT '' COLLATE NOCASE, [resource_code] int not null, [resource_name] text not null DEFAULT '' COLLATE NOCASE, constraint pk_module_ress primary key(module_code,resource_code));");
            script.append("create table if not exists [ev_module_res_txts] ([module_code] text not null DEFAULT '' COLLATE NOCASE, [resource_code] int not null, [txt_code] text not null DEFAULT '' COLLATE NOCASE, [txt_ref] int not null, constraint pk_module_res_txts primary key(module_code,resource_code,txt_code));");
            script.append("create table if not exists [ev_module_res_txt_transs] ([module_code] text not null DEFAULT '' COLLATE NOCASE, [resource_code] int not null, [txt_code] text not null DEFAULT '' COLLATE NOCASE, [translate_code] int not null, [txt_value] text not null DEFAULT '' COLLATE NOCASE, constraint pk_module_res_txt_transs primary key(module_code,resource_code,txt_code,translate_code));");
            script.append("create table if not exists [ev_profiles]([customer_code] INT NOT NULL,[menu_code] TEXT NOT NULL DEFAULT '' COLLATE NOCASE, [parameter_code] TEXT COLLATE NOCASE);");

            script.append("create table if not exists [ge_custom_form_types] ([customer_code] int not null, [custom_form_type] int not null, constraint pk_form_types primary key(customer_code,custom_form_type));");

            script.append("create table if not exists [ge_custom_forms]([customer_code]int not null, [custom_form_type]int not null, [custom_form_code]int not null, [custom_form_version]int not null, [tag_operational_code]int not null, [require_signature]int not null, [require_location]int not null DEFAULT 0, [require_serial_done]int not null DEFAULT 0, [is_so]int not null DEFAULT 0, [so_edit_start_end]int not null DEFAULT 0, [so_order_type_code_default]int, [so_allow_change_order_type]int not null DEFAULT 0, [so_allow_backup]int not null DEFAULT 0, [so_optional_justify_problem] int not null DEFAULT 0, [automatic_fill] text NOT NULL DEFAULT '' COLLATE nocase, [all_product]int not null DEFAULT 0, [all_site]int not null DEFAULT 0, [all_operation]int not null DEFAULT 0, [block_spontaneous]int not null DEFAULT 0, [block_spontaneous_in_ticket]int not null DEFAULT 0, [justify_group_code] int, [nc_recognize_email_in_comment] int not null default 0, constraint pk_forms primary key(customer_code, custom_form_type, custom_form_code, custom_form_version));");
            script.append(GeCustomFormFieldsKt.getGeCustomFormFieldsDatabaseTable());

            script.append("create table if not exists [ge_custom_forms_local]([customer_code] int NOT NULL, [custom_form_type] int NOT NULL, [custom_form_code] int NOT NULL, [custom_form_version] int NOT NULL, [custom_form_desc] text NOT NULL DEFAULT '' COLLATE nocase, [custom_form_data] int NOT NULL, [custom_form_pre] text NOT NULL DEFAULT '' COLLATE nocase, [custom_form_status] text NOT NULL DEFAULT '' COLLATE nocase,[tag_operational_code] int not null , [tag_operational_id] text not null collate nocase, [tag_operational_desc] text not null collate nocase, [is_so] int not null DEFAULT 0, [so_edit_start_end] int not null DEFAULT 0, [so_order_type_code_default] int, [so_allow_change_order_type] int not null DEFAULT 0, [so_allow_backup] int not null DEFAULT 0, [so_optional_justify_problem] int not null DEFAULT 0, [custom_product_code] int NOT NULL, [custom_product_desc] text NOT NULL DEFAULT '' COLLATE nocase, [custom_product_id] text NOT NULL DEFAULT '' COLLATE nocase, [custom_product_icon_name] text DEFAULT '' COLLATE NOCASE, [custom_product_icon_url] text DEFAULT '' COLLATE NOCASE, [custom_product_icon_url_local] text not null DEFAULT '' COLLATE nocase, [serial_id] text NOT NULL DEFAULT '' COLLATE nocase, [require_signature] int NOT NULL, [require_location] int not null DEFAULT 0, [require_serial_done] int not null DEFAULT 0,[automatic_fill] text NOT NULL DEFAULT '' COLLATE nocase, [schedule_date_start_format] text NOT NULL DEFAULT '' COLLATE NOCASE, [schedule_date_end_format] text NOT NULL DEFAULT '' COLLATE NOCASE,[schedule_date_start_format_ms] INT NOT NULL DEFAULT 0,[schedule_date_end_format_ms] INT NOT NULL DEFAULT 0, [require_serial] int not null DEFAULT 0, [allow_new_serial_cl] int not null DEFAULT 0, [all_product] int not null DEFAULT 0, [all_site] int not null DEFAULT 0, [all_operation] int not null DEFAULT 0, [site_code] int NOT NULL, [site_id] text NOT NULL DEFAULT '' COLLATE nocase, [site_desc] text NOT NULL DEFAULT '' COLLATE nocase,[zone_code] int, [zone_id] text COLLATE nocase, [zone_desc] text COLLATE nocase, [io_control] int NOT NULL, [inbound_auto_create] int NOT NULL, [operation_code] int NOT NULL, [operation_id] text NOT NULL DEFAULT '' COLLATE nocase, [operation_desc] text NOT NULL DEFAULT '' COLLATE nocase, [local_control] int NOT NULL, [product_io_control] int NOT NULL, [site_restriction] int NOT NULL, [serial_rule] text COLLATE nocase, [serial_min_length] int, [serial_max_length] int,[schedule_comments] text COLLATE NOCASE , [schedule_prefix] int , [schedule_code] int , [schedule_exec] int , [ticket_prefix] int, [ticket_code] int, [ticket_seq] int, [ticket_seq_tmp] int, [step_code] int, [nc_recognize_email_in_comment] int not null default 0,  CONSTRAINT [pk_forms] PRIMARY KEY([customer_code], [custom_form_type], [custom_form_code], [custom_form_version], [custom_form_data]));");
            script.append(GeCustomFormLocalFieldsKt.getGeCustomFormFieldsLocalTable());
            script.append("create table if not exists [ge_custom_form_blobs_local]([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [blob_code] int not null, [blob_name] text not null DEFAULT '' COLLATE NOCASE, [blob_url] text not null DEFAULT '' COLLATE NOCASE, [blob_url_local] text not null DEFAULT '' COLLATE NOCASE, constraint [pk_ge_custom_form_blobs] primary key([customer_code], [custom_form_type] , [custom_form_code] , [custom_form_version] , [blob_code]));");

            script.append(GeCustomFormCreateScriptsKt.GE_CUSTOM_FORM_FORM_CREATE_SCRIPT);
            script.append("create table if not exists [ge_custom_form_data_fields] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_data] int not null, [custom_form_seq] int not null, [value] text not null DEFAULT '' COLLATE NOCASE, [value_extra] text not null DEFAULT '' COLLATE NOCASE, [is_active] int not null default 0, constraint pk_form_data_fields primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,custom_form_seq));");
            script.append("create table if not exists [ge_custom_form_products] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [product_code] int not null, constraint pk_ge_custom_form_products primary key(customer_code, custom_form_type, custom_form_code, custom_form_version, product_code));");
            script.append("create table if not exists [ge_custom_form_blobs]([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [blob_code] int not null, [blob_name] text not null DEFAULT '' COLLATE NOCASE, [blob_url] text not null DEFAULT '' COLLATE NOCASE, [blob_url_local] text not null DEFAULT '' COLLATE NOCASE, constraint [pk_ge_custom_form_blobs] primary key([customer_code], [custom_form_type] , [custom_form_code] , [custom_form_version] , [blob_code]));");
            script.append("create table if not exists [ge_custom_form_operations]([customer_code] int NOT NULL, [custom_form_type] int NOT NULL, [custom_form_code] int NOT NULL, [custom_form_version] int NOT NULL, [operation_code] int NOT NULL, PRIMARY KEY([customer_code], [custom_form_type], [custom_form_code], [custom_form_version], [operation_code]));");
            script.append("create table if not exists [ge_custom_form_sites]([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [site_code] int not null, constraint pk_ge_custom_form_sites primary key([customer_code], [custom_form_type], [custom_form_code], [custom_form_version], [site_code]));");
            script.append("create table if not exists [ge_files]([file_code] text not null DEFAULT '' COLLATE NOCASE, [file_path] text not null DEFAULT '' COLLATE NOCASE,[file_path_new] text collate nocase, [file_status] text not null DEFAULT '' COLLATE NOCASE, [file_date] text not null DEFAULT '' COLLATE NOCASE, primary key(file_code));");

            script.append(PRODUCT_CREATE_SCRIPT);
            script.append("create table if not exists [md_product_groups]( [customer_code] int not null,  [group_code] int not null, [recursive_code] int not null, [recursive_code_father] int, [group_id] text not null DEFAULT '' COLLATE NOCASE,  [group_desc] text not null DEFAULT '' COLLATE NOCASE, [spare_part] int not null default 0,  constraint pk_md_product_groups primary key(customer_code, group_code));");
            script.append("create table if not exists [md_product_group_products]( [customer_code] int not null,  [group_code] int not null, [product_code] int not null,  constraint pk_md_product_group_products primary key(customer_code, group_code,product_code));");
            script.append(PRODUCT_SERIAL_CREATE_SCRIPT);
            script.append("CREATE TABLE IF NOT EXISTS [md_product_serial_trackings]([customer_code] int not null,[product_code] int not null,[serial_code] int not null,[serial_tmp] int not null,[tracking] text not null COLLATE NOCASE,constraint [pk_md_product_serial_trackings] primary key([customer_code],[product_code],[serial_tmp],[tracking]));");
            script.append("create table if not exists [md_operations] ([customer_code] int not null, [operation_code] int not null, [operation_id] text not null DEFAULT '' COLLATE NOCASE, [operation_desc] text not null DEFAULT '' COLLATE NOCASE, [alias_service_oper] int not null, [alias_service_com] int not null, constraint pk_md_operations primary key(customer_code, operation_code));");
            script.append(SiteScriptKt.MD_SITE_CREATE_SCRIPT);
            script.append("CREATE TABLE IF NOT EXISTS [md_site_zones]([customer_code] int not null,[site_code] int not null,[zone_code] int not null,[zone_id] text not null collate nocase,[zone_desc] text not null collate nocase,[blocked] int not null  default 0,[process_seq] int,constraint [pk_md_site_zones] primary key([customer_code],[site_code],[zone_code]));");
            script.append("CREATE TABLE IF NOT EXISTS [md_site_zone_locals]([customer_code] int not null,[site_code] int not null,[zone_code] int not null,[local_code] int not null,[local_id] text not null collate nocase,[capacity] int not null,constraint [pk_md_site_zone_locals] primary key([customer_code],[site_code],[zone_code],[local_code]));");
            script.append("create table if not exists [sync_checklist]([customer_code] int not null, [product_code] int not null, [last_update] text not null , CONSTRAINT [pk_sync_checklist] primary key([customer_code], [product_code]));");
            //TABLE PRODUCT SEM PROFILE
            script.append("create table if not exists [md_all_products] ([customer_code] int not null, [product_code] int not null, [product_id] text not null DEFAULT '' COLLATE NOCASE, [product_desc] text not null DEFAULT '' COLLATE NOCASE, [require_serial] int not null, [allow_new_serial_cl] int not null, [un] text COLLATE nocase, [sketch_code] int, [sketch_url] text COLLATE nocase, [sketch_url_local] text not null DEFAULT '' COLLATE nocase,[sketch_lines] int, [sketch_columns] int, [sketch_color] text COLLATE nocase,[flag_offline] int not null default 0,[local_control] int not null default 0, [io_control] int not null default 0, [serial_rule] text collate nocase, [serial_min_length] int, [serial_max_length] int, [spare_part] int not null default 0, [has_group] int not null default 0, constraint pk_md_all_products primary key(customer_code, product_code));");
            script.append("create table if not exists [md_all_product_groups]( [customer_code] int not null,  [group_code] int not null, [recursive_code] int not null, [recursive_code_father] int, [group_id] text not null DEFAULT '' COLLATE NOCASE,  [group_desc] text not null DEFAULT '' COLLATE NOCASE, [spare_part] int not null default 0,  constraint pk_md_all_product_groups primary key(customer_code, group_code));");
            script.append("create table if not exists [md_all_product_group_products]( [customer_code] int not null,  [group_code] int not null, [product_code] int not null,  constraint pk_md_all_product_group_products primary key(customer_code, group_code,product_code));");

            //TABLES SO
            script.append(SmSoScriptKt.SM_SOS_CREATE_SCRIPT);
            script.append(SmSoScriptKt.SM_SO_FILES_CREATE_SCRIPT);
            script.append(SmSoScriptKt.SM_SO_PACKS_CREATE_SCRIPT);

            script.append("CREATE TABLE if not exists [so_pack_expresss] ( [customer_code] int NOT NULL, [site_code] int NOT NULL, [operation_code] int NOT NULL, [product_code] int NOT NULL, [express_code] text NOT NULL COLLATE nocase, [pack_desc] text NOT NULL COLLATE nocase, [category_price_code]int NOT NULL, [contract_code]int NOT NULL, [segment_code]int NOT NULL, [price_list_code]int NOT NULL, [pipeline_desc] text collate nocase, [pack_code]int NOT NULL, [add_pack_service]int NOT NULL,  [price] real NOT NULL,  [billing_add_inf1_view] text not null collate nocase, [billing_add_inf1_text] text collate nocase, [billing_add_inf1_tracking] int not null default 0, [billing_add_inf2_view] text not null collate nocase, [billing_add_inf2_text] text collate nocase, [billing_add_inf2_tracking] int not null default 0, [billing_add_inf3_view] text not null collate nocase, [billing_add_inf3_text] text collate nocase, [billing_add_inf3_tracking] int not null default 0, PRIMARY KEY([customer_code],[site_code],[operation_code],[product_code],[express_code]));");
            script.append("CREATE TABLE if not exists [so_pack_expresss_local] ([customer_code] int NOT NULL, [site_code] int NOT NULL, [operation_code] int NOT NULL, [product_code] int NOT NULL, [express_code] text NOT NULL COLLATE nocase, [express_tmp] int NOT NULL, [serial_id] text NOT NULL COLLATE nocase, [partner_code] int NOT NULL, [so_prefix] int, [so_code] int, [so_desc] text COLLATE nocase, [so_status] text COLLATE nocase, [contract_code] int, [contract_desc] text COLLATE nocase, [priority_code] int, [priority_desc] text COLLATE nocase, [exec_site_code] int NOT NULL, [exec_site_id] text COLLATE nocase, [exec_site_desc] text COLLATE nocase, [exec_zone_code] int NOT NULL, [exec_zone_id] text COLLATE nocase, [exec_zone_desc] text COLLATE nocase, [operation_id] text COLLATE nocase, [operation_desc] text COLLATE nocase, [product_id] text COLLATE nocase, [product_desc] text COLLATE nocase, [serial_code] int, [segment_code] int, [segment_id] text COLLATE nocase, [segment_desc] text COLLATE nocase, [pipeline_desc] text collate nocase, [billing_add_inf1_value] text collate nocase, [billing_add_inf2_value] text collate nocase, [billing_add_inf3_value] text collate nocase, [billing_add_inf1_tracking] int not null default 0, [billing_add_inf2_tracking] int not null default 0, [billing_add_inf3_tracking] int not null default 0, [ret_code] text COLLATE nocase, [ret_msg] text COLLATE nocase, [status] text NOT NULL COLLATE nocase, [so_id] text COLLATE nocase, [log_date] text, [token] text not null default '' collate nocase, PRIMARY KEY([customer_code],[site_code],[operation_code], [product_code],[express_code],[express_tmp]));");
            script.append("CREATE TABLE if not exists [so_pack_express_packs_local] ([customer_code] int NOT NULL, [site_code] int NOT NULL, [operation_code] int NOT NULL,  [product_code] int NOT NULL,  [express_code] text NOT NULL COLLATE nocase,  [express_tmp] int NOT NULL, [price_list_code] int NOT NULL, [pack_code] int NOT NULL, [pack_seq] int NOT NULL, [type_ps] text COLLATE nocase not null, [pack_service_desc] text COLLATE nocase, [pack_service_desc_full] text COLLATE nocase,[manual_price] int NOT NULL default 0, [price] real NOT NULL default 0, [qty] int NOT NULL default 0, [service_code] int, [comments] text COLLATE nocase, PRIMARY KEY([customer_code], [site_code], [operation_code], [product_code], [express_code], [express_tmp], [price_list_code], [pack_code], [pack_seq], [type_ps]));");
            script.append("CREATE TABLE if not exists [so_pack_express_services_local] ([customer_code] int NOT NULL, [site_code] int NOT NULL, [operation_code] int NOT NULL,  [product_code] int NOT NULL,  [express_code] text NOT NULL COLLATE nocase,  [express_tmp] int NOT NULL, [price_list_code] int NOT NULL, [pack_code] int NOT NULL, [pack_seq] int NOT NULL, [type_ps] text COLLATE nocase not null, [service_code] int NOT NULL, [service_seq] int NOT NULL,[service_desc] text COLLATE nocase, [service_desc_full] text COLLATE nocase, [price] real,[manual_price] int NOT NULL default 0, [qty] int NOT NULL default 0, [comments] text COLLATE nocase, PRIMARY KEY([customer_code], [site_code], [operation_code], [product_code], [express_code], [express_tmp], [price_list_code], [pack_code], [pack_seq], [type_ps], [service_code], [service_seq]));");

            script.append("CREATE TABLE if not exists [sm_so_services] ( [customer_code] int NOT NULL, [so_prefix] int NOT NULL, [so_code] int NOT NULL, [price_list_code] int NOT NULL, [pack_code] int NOT NULL, [pack_seq] int NOT NULL, [category_price_code] int NOT NULL, [service_code] int NOT NULL, [service_seq] int NOT NULL, [service_id] text NOT NULL COLLATE nocase, [service_desc] text NOT NULL COLLATE nocase, [service_oper_id] text, [status] text NOT NULL, [qty] int NOT NULL, [optional] int NOT NULL, [manual_price] int NOT NULL, [express] int NOT NULL, [time_exec_standard] int NOT NULL, [price] real, [cost] real, [exec_type] text NOT NULL, [exec_seq_oper] int NOT NULL, [approval_budget_user] int, [approval_budget_user_nick] text, [approval_budget_date] text, [partner_code] int, [partner_id] text, [partner_desc] text, [require_approval] text NOT NULL, [comments] text, [site_code] int,[site_id] text, [site_desc] text ,[zone_code] int ,[zone_id] text ,[zone_desc] text , PRIMARY KEY([customer_code],[so_prefix],[so_code],[price_list_code],[pack_code],[pack_seq],[category_price_code],[service_code],[service_seq]));");

            script.append("CREATE TABLE if not exists [sm_so_service_execs] ( [customer_code] int NOT NULL, [so_prefix] int NOT NULL, [so_code] int NOT NULL, [price_list_code] int NOT NULL, [pack_code] int NOT NULL, [pack_seq] int NOT NULL, [category_price_code] int NOT NULL, [service_code] int NOT NULL, [service_seq] int NOT NULL, [exec_code] int, [exec_tmp] int NOT NULL, [status] text NOT NULL, [partner_code] int, [partner_id] text, [partner_desc] text, PRIMARY KEY([customer_code],[so_prefix],[so_code],[price_list_code],[pack_code],[pack_seq],[category_price_code],[service_code],[service_seq],[exec_tmp]));");
            script.append("CREATE TABLE if not exists [sm_so_service_exec_tasks] ( [customer_code] int NOT NULL, [so_prefix] int NOT NULL, [so_code] int NOT NULL, [price_list_code] int NOT NULL, [pack_code] int NOT NULL, [pack_seq] int NOT NULL, [category_price_code] int NOT NULL, [service_code] int NOT NULL, [service_seq] int NOT NULL, [exec_code] int , [task_code] int, [exec_tmp] int NOT NULL, [task_tmp] int NOT NULL,[task_seq_oper] int NOT NULL, [task_user] int NOT NULL, [task_user_nick] text NOT NULL, [start_date] text NOT NULL, [end_date] text, [exec_time] int, [exec_time_format] text, [task_perc] int NOT NULL, [qty_people] int NOT NULL, [status] text NOT NULL, [site_code] int, [site_id] text, [site_desc] text, [zone_code] int, [zone_id] text, [zone_desc] text , [local_code] int, [local_id] text, [comments] text, PRIMARY KEY([customer_code],[so_prefix],[so_code],[price_list_code],[pack_code],[pack_seq],[category_price_code],[service_code],[service_seq],[exec_tmp],[task_tmp]));");
            script.append("CREATE TABLE if not exists [sm_so_service_exec_task_files]( [customer_code] int NOT NULL, [so_prefix] int NOT NULL, [so_code] int NOT NULL, [price_list_code] int NOT NULL, [pack_code] int NOT NULL, [pack_seq] int NOT NULL, [category_price_code] int NOT NULL, [service_code] int NOT NULL, [service_seq] int NOT NULL, [exec_code] int, [task_code] int, [file_code] int, [exec_tmp] int NOT NULL, [task_tmp] int NOT NULL,[file_tmp] int NOT NULL, [file_name] text NOT NULL, [file_url] text,[file_url_local] text,PRIMARY KEY([customer_code] ,[so_prefix] ,[so_code] ,[price_list_code] ,[pack_code] ,[pack_seq] ,[category_price_code] ,[service_code] ,[service_seq] ,[exec_tmp] ,[task_tmp] ,[file_tmp]));");
            //
            script.append("create table if not exists [sm_so_product_events] ([customer_code] int not null, [so_prefix] int not null, [so_code] int not null, [seq] int not null, [seq_tmp] int not null, [product_code] int not null , [product_id] text not null default '' collate nocase, [product_desc] text not null default '' collate nocase, [un] text not null default '' collate nocase,[flag_apply] int not null, [flag_repair] int not null, [flag_inspection] int not null, [qty_apply] text, [sketch_code] int,[sketch_name] text collate nocase, [sketch_url] text collate nocase, [sketch_url_local] text collate nocase,[sketch_lines] int, [sketch_columns] int, [sketch_color] text collate nocase,[comments] text collate nocase,[status] text collate nocase,[create_date] text collate nocase, [create_user] int not null,[create_user_nick] text collate nocase,[done_date] text collate nocase, [done_user] int,[done_user_nick] text collate nocase,[integrated] int not null, constraint pk_sm_so_product_events primary key(customer_code, so_prefix, so_code, seq_tmp));");
            script.append("create table if not exists [sm_so_product_event_files] ([customer_code] int not null, [so_prefix] int not null, [so_code] int not null, [seq] int not null, [seq_tmp] int not null, [file_code] int not null , [file_tmp] int not null , [file_name] text not null collate nocase, [file_url] text collate nocase, [file_url_local] text collate nocase, constraint pk_sm_so_product_event_files primary key(customer_code, so_prefix, so_code, seq_tmp, file_tmp));");
            script.append("create table if not exists [sm_so_product_event_sketchs] ([customer_code] int not null, [so_prefix] int not null, [so_code] int not null, [seq] int not null, [seq_tmp] int not null, [line] int not null , [col] int not null , constraint pk_sm_so_product_event_sketchs primary key(customer_code, so_prefix, so_code, seq_tmp, line, col));");

            script.append("create table if not exists [sm_priority] ([customer_code] int not null, [priority_code] int not null, [priority_desc] text not null collate nocase, [priority_weight] int not null, [priority_default] int not null, [priority_color] text not null collate nocase, constraint pk_sm_priority primary key(customer_code, priority_code));");

            script.append("create table if not exists [md_segments]([customer_code] int not null,[segment_code] int not null,[segment_id] text not null collate nocase,[segment_desc] text not null collate nocase,constraint [pk_md_segments] primary key([customer_code],[segment_code]));");
            script.append("create table if not exists [md_category_prices]([customer_code] int not null,[category_price_code] int not null,[category_price_id] text not null collate nocase,[category_price_desc] text not null collate nocase,constraint [pk_md_category_prices] primary key([customer_code],[category_price_code]));");
            script.append("create table if not exists [md_brands]([customer_code] int not null,[brand_code] int not null,[brand_id] text not null collate nocase,[brand_desc] text not null collate nocase,constraint [pk_md_brands] primary key([customer_code],[brand_code]));");
            script.append("create table if not exists [md_brand_models]([customer_code] int not null,[brand_code] int not null,[model_code] int not null,[model_id] text not null collate nocase,[model_desc] text not null collate nocase,constraint [pk_md_brand_models] primary key([customer_code],[brand_code],[model_code]));");
            script.append("create table if not exists [md_brand_colors]([customer_code] int not null,[brand_code] int not null,[color_code] int not null,[color_id] text not null collate nocase,[color_desc] text not null collate nocase,constraint [pk_md_brand_colors] primary key([customer_code],[brand_code],[color_code]));");
            script.append("create table if not exists [md_partners]([customer_code] int not null,[partner_code] int not null,[partner_id] text not null collate nocase,[partner_desc] text not null collate nocase, [operational] int not null default 0, [billing] int not null default 0, constraint [pk_md_partners] primary key([customer_code],[partner_code]));");
            script.append("create table if not exists [md_product_brands]([customer_code] int not null,[product_code] int not null,[brand_code] int not null,constraint [pk_md_product_brands] primary key([customer_code],[product_code],[brand_code]));");
            script.append("create table if not exists [md_product_segments]([customer_code] int not null,[product_code] int not null,[segment_code] int not null,constraint [pk_md_product_segments] primary key([customer_code],[product_code],[segment_code]));");
            script.append("create table if not exists [md_product_category_prices]([customer_code] int not null,[product_code] int not null,[category_price_code] int not null,constraint [pk_md_product_category_prices] primary key([customer_code],[product_code],[category_price_code]));");
            script.append("create table if not exists [md_classes]([customer_code] int not null, [class_code] int not null, [class_id] text not null collate nocase, [class_type] text not null collate nocase, [class_color] text not null collate nocase, [class_available] int not null,constraint [pk_md_classes] primary key([customer_code],[class_code]));");
            //tabelas ActionPlan
            script.append("create table if not exists [md_departments]([customer_code] int not null,[department_code] int not null,[department_id] text not null COLLATE NOCASE,[department_desc] text not null COLLATE NOCASE,constraint pk_md_departments primary key(customer_code,department_code));");
            script.append("create table if not exists [md_users]([customer_code] int not null,[user_code] int not null,[user_nick] text not null COLLATE NOCASE,[user_name] text not null COLLATE NOCASE,[ap] int not null default 0,constraint pk_md_users primary key(customer_code,user_code));");
            script.append("create table if not exists [ge_custom_form_aps]([customer_code] int not null,[custom_form_type] int not null,[custom_form_code] int not null,[custom_form_version] int not null,[custom_form_desc] text not null COLLATE NOCASE,[custom_form_data] text not null COLLATE NOCASE, [tag_operational_code] int not null , [tag_operational_id] itext collate nocase, [tag_operational_desc] text collate nocase , [custom_form_url] text COLLATE NOCASE,[custom_form_url_local] text not null DEFAULT '' COLLATE NOCASE, [ap_code] int not null, [ap_description] text not null COLLATE NOCASE,[ap_status] text not null COLLATE NOCASE,[ap_comments] text COLLATE NOCASE,[ap_what] text COLLATE NOCASE,[ap_where] text COLLATE NOCASE,[ap_why] text COLLATE NOCASE,[ap_who] int, [ap_who_nick] text COLLATE NOCASE,[ap_how] text COLLATE NOCASE,[ap_how_much] text COLLATE NOCASE,[ap_when] text COLLATE NOCASE,[department_code] text COLLATE NOCASE,[department_id] text COLLATE NOCASE,[department_desc] text COLLATE NOCASE,[room_code] text COLLATE NOCASE,[ap_scn] int not null,[product_code] int not null,[product_id] text not null COLLATE NOCASE,[product_desc] text not null COLLATE NOCASE,[serial_code] int ,[serial_id] text COLLATE NOCASE,[sync_required] int not null default 0, [upload_required] int not null default 0,[last_update] text not null COLLATE NOCASE ,[create_date] text COLLATE NOCASE, [create_user] text COLLATE NOCASE, constraint pk_ge_custom_form_aps primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,ap_code));");
            //Tabelas IO
            script.append("create table if not exists [io_move_reason]([customer_code]int not null, [reason_code]int not null , [reason_id] text not null collate nocase, [reason_desc] text not null collate nocase, constraint pk_io_move_reason primary key(customer_code,reason_code));");
            script.append("create table if not exists [io_inbound]([customer_code] int not null ,[inbound_prefix] int not null ,[inbound_code] int not null ,[inbound_id] text collate nocase not null ,[inbound_desc] text collate nocase ,[scn] int not null default 1,[origin] text collate nocase not null ,[transport_order] text collate nocase ,[invoice_number] text collate nocase ,[invoice_date] text collate nocase ,[eta_date] text collate nocase ,[arrival_date] text collate nocase ,[from_type] text collate nocase not null ,[from_partner_code] int ,[from_partner_id] text collate nocase ,[from_partner_desc] text collate nocase ,[from_site_code] int,[from_site_id] text collate nocase ,[from_site_desc] text collate nocase ,[to_site_code] int not null ,[carrier_code] int ,[carrier_id] text collate nocase ,[carrier_desc] text collate nocase ,[truck_number] text collate nocase ,[driver] text collate nocase ,[comments] text collate nocase ,[status] text collate nocase not null ,[perc_done] real,[inbound_auto_seq] int not null ,[modal_code] int ,[modal_id] text collate nocase,[modal_desc] text collate nocase,[allow_new_item] int not null default 0,[zone_code_conf] int ,[zone_id_conf] text collate nocase,[zone_desc_conf] text collate nocase,[local_code_conf] int ,[local_id_conf] text collate nocase,[put_away_process] int not null default 0,[done_automatic] int not null default 0,[update_required] int NOT NULL default 0, [sync_required] int NOT NULL default 0, [token] text NOT NULL DEFAULT '' COLLATE nocase, constraint pk_io_inbound primary key(customer_code,inbound_prefix,inbound_code));");
            //script.append("create table if not exists [io_inbound_file]([customer_code] int not null ,[inbound_prefix] int not null ,[inbound_code] int not null ,[file_code] int not null ,[file_code_tmp] int not null ,[file_name] text collate nocase,[file_url] text collate nocase,[file_url_local] text collate nocase,constraint pk_io_inbound_file primary key(customer_code,inbound_prefix,inbound_code,file_code_tmp));");
            script.append("create table if not exists [io_inbound_item]([customer_code] int not null,[inbound_prefix] int not null,[inbound_code] int not null,[inbound_item] int not null,[product_code] int not null,[serial_code] int not null,[site_code] int,[zone_code] int,[zone_id] text collate nocase,[zone_desc] text collate nocase,[local_code] int,[local_id] text collate nocase,[class_code] int,[class_id] text collate nocase,[conf_date] text collate nocase,[status] text not null collate nocase,[comments] text collate nocase,[planned_zone_code] int,[planned_local_code] int,[planned_class_code] int,[save_date] text collate nocase ,[update_required] int default 0, constraint pk_io_inbound_item primary key(customer_code,inbound_prefix,inbound_code,inbound_item));");
            script.append("create table if not exists [io_move]([customer_code] int not null ,[move_prefix] int not null ,[move_code] int not null ,[product_code] int not null ,[serial_code] int not null ,[site_code] int not null ,[from_zone_code] int ,[from_local_code] int ,[from_class_code] int ,[planned_zone_code] int ,[planned_local_code] int ,[planned_class_code] int ,[to_zone_code] int ,[to_local_code] int ,[to_class_code] int ,[move_type] text collate nocase not null ,[reason_code] int ,[inbound_prefix] int ,[inbound_code] int ,[inbound_item] int ,[outbound_prefix] int ,[outbound_code] int ,[outbound_item] int ,[done_date] text collate nocase ,[done_user] int , [done_user_nick] text collate nocase ,[status] text collate nocase not null , [update_required] int NOT NULL default 0, [token] text NOT NULL DEFAULT '' COLLATE nocase, constraint pk_io_move primary key(customer_code, move_prefix, move_code));");
            script.append("create table if not exists [io_move_tracking]([customer_code] int not null,[move_prefix] int not null,[move_code] int not null,[tracking] text not null COLLATE NOCASE,constraint [pk_io_move_tracking] primary key([customer_code],[move_prefix],[move_code],[tracking]));");
            script.append("create table if not exists [io_outbound]([customer_code] int not null , [outbound_prefix] int not null ,[outbound_code] int not null ,[outbound_desc] text collate nocase ,[outbound_id] text not null collate nocase ,[scn] int not null ,[origin] text not null collate nocase ,[transport_order] text collate nocase ,[invoice_number] text collate nocase ,[invoice_date] text collate nocase ,[eta_date] text collate nocase ,[departure_date] text collate nocase ,[loading_date] text collate nocase ,[from_site_code] int not null ,[to_type] text not null collate nocase ,[to_partner_code] int ,[to_partner_id] text collate nocase ,[to_partner_desc] text collate nocase ,[to_site_code] int ,[to_site_id] text collate nocase ,[to_site_desc] text collate nocase ,[carrier_code] int ,[carrier_id] text collate nocase ,[carrier_desc] text collate nocase ,[truck_number] text collate nocase ,[driver] text collate nocase ,[comments] text collate nocase ,[status] text not null collate nocase , [perc_done] real ,[modal_code] int ,[modal_id] text ,[modal_desc] text ,[zone_code_picking] int ,[zone_id_picking] text ,[zone_desc_picking] text ,[local_id_picking] text ,[allow_new_item] int not null default 0 ,[local_code_picking] int ,[picking_process] int not null default 0 ,[done_automatic] int not null default 0 ,[update_required] int NOT NULL default 0,[sync_required] int NOT NULL default 0, [token] text NOT NULL DEFAULT '' COLLATE nocase,  constraint pk_io_outbound primary key(customer_code, outbound_prefix, outbound_code));");
            script.append("create table if not exists [io_outbound_item]([customer_code]int not null, [outbound_prefix]int not null, [outbound_code]int not null, [outbound_item]int not null, [product_code]int not null, [serial_code]int not null, [class_code] int,[class_id] text collate nocase, [conf_date]text collate nocase, [status]text not null collate nocase, [inbound_prefix]int, [inbound_code]int, [inbound_item]int, [comments]text collate nocase, [save_date]text collate nocase,  [update_required] int, [out_conf_done] int ,constraint pk_io_outbound_item primary key(customer_code, outbound_prefix, outbound_code, outbound_item));");
            script.append("create table if not exists [io_blind_move]([customer_code] int not null,[blind_tmp] int not null,[product_code] int not null,[serial_code] int not null,[serial_id] text not null,[blind_prefix] int ,[blind_code] int,[site_code] int not null,[zone_code] int not null,[local_code] int not null,[reason_code] int not null,[class_code] int,[flag_blind] int not null default 0,[token] text,[save_date] text not null,[status] text not null, [error_msg] text,constraint [pk_io_blind_move] primary key([customer_code],[blind_tmp],[product_code],[serial_id]));");
            script.append("create table if not exists [io_blind_move_tracking]([customer_code] int not null,[blind_tmp] int not null,[tracking] text not null COLLATE NOCASE,constraint [pk_io_blind_move_tracking] primary key([customer_code],[blind_tmp],[tracking]));");
            script.append("create table if not exists [io_conf_tracking]([customer_code] int not null,[prefix] int not null,[code] int not null,[item] int not null,[type] text not null collate nocase,[tracking] text not null collate nocase,constraint [pk_io_conf_tracking] primary key([customer_code],[prefix],[code],[item],[type],[tracking]));");
            //script.append("create table if not exists [io_outbound_file]([customer_code] int not null ,[outbound_prefix] int not null ,[outbound_code] int not null ,[file_code] int not null ,[file_code_tmp] int not null ,[file_name] text collate nocase,[file_url] text collate nocase,[file_url_local] text collate nocase,constraint pk_io_outbound_file primary key(customer_code,outbound_prefix,outbound_code,file_code_tmp));");
            //Tabelas Ticket
            script.append(TkCreateScriptsKt.TK_TICKET_CREATE_SCRIPT);
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_step] ([customer_code] int not null,[ticket_prefix] int not null,[ticket_code] int not null,[step_code] int not null,[step_id] text COLLATE NOCASE,[step_desc] text COLLATE NOCASE, [step_order] int not null default 0, [step_order_seq] int, [forecast_start] text COLLATE NOCASE,[forecast_end] text COLLATE NOCASE,[exec_type] text not null COLLATE NOCASE,[scan_serial] int not null default 0,[allow_new_obj] int not null default 0, [move_next_step] int not null default 0, [step_start_date] text COLLATE NOCASE,[step_start_user] int ,[step_start_user_nick] text COLLATE NOCASE,[step_end_date] text COLLATE NOCASE,[step_end_user] int ,[step_end_user_nick] text COLLATE NOCASE,[step_status] text not null COLLATE NOCASE, [user_focus] int not null default 0, [has_item_check] int, [group_code] int, [group_desc] text COLLATE NOCASE, [zone_site_group_code] int, [zone_site_group_desc] text COLLATE NOCASE, [pc_level_target] text COLLATE NOCASE, [ap_group_code] int, [ap_group_desc] text COLLATE NOCASE,[ap_zone_site_group_code] int, [ap_zone_site_group_desc] text COLLATE NOCASE, [ap_pc_level_target] text COLLATE NOCASE, [update_required] int not null default 0, CONSTRAINT [pk_tk_ticket_step] PRIMARY KEY( [customer_code] , [ticket_prefix] , [ticket_code] , [step_code]));");
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_product] ([customer_code] int not null, [ticket_prefix] int not null, [ticket_code] int not null, [product_code] int not null, [product_id] text not null COLLATE NOCASE, [product_desc] text not null COLLATE NOCASE, [un] text not null COLLATE NOCASE, [qty_planned] real, [qty] real, [qty_used] real, [pickup_status] text COLLATE NOCASE, [qty_returned] real, [return_status] text COLLATE NOCASE , CONSTRAINT [pk_tk_ticket_product] PRIMARY KEY([customer_code] , [ticket_prefix] , [ticket_code] , [product_code]));");
            script.append(TkCreateScriptsKt.TK_TICKET_CTRL_CREATE_SCRIPT);
            /*
             BARRIONUEVO 31-10-2023
             Encapsulamento de script de criacao de tabela.
             */
            script.append(TkCreateScriptsKt.TK_TICKET_FORM_CREATE_SCRIPT);
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_action] ( [customer_code] int NOT NULL, [ticket_prefix] int NOT NULL, [ticket_code] int NOT NULL, [ticket_seq] int NOT NULL, [ticket_seq_tmp] int NOT NULL default 0, [step_code] int not null, [action_comments] text COLLATE NOCASE, [action_photo] text COLLATE NOCASE, [action_photo_local] text COLLATE NOCASE, [action_photo_name] text COLLATE NOCASE, [action_photo_code] int, [action_photo_changed] int not null default 0,[action_status] text COLLATE NOCASE, CONSTRAINT [pk_tk_ticket_action] PRIMARY KEY( [customer_code] , [ticket_prefix] , [ticket_code] , [step_code] , [ticket_seq_tmp]));");
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_measure]( [customer_code] int NOT NULL, [ticket_prefix] int NOT NULL, [ticket_code] int NOT NULL, [ticket_seq] int NOT NULL, [step_code] int not null, [measure_tp_code] int not null, [measure_tp_id] text COLLATE nocase, [measure_tp_desc] text COLLATE nocase, [measure_value] int not null, [value_sufix] text COLLATE nocase,  [measure_date] text COLLATE nocase, [measure_info] text COLLATE nocase, CONSTRAINT [pk_tk_ticket_measure] PRIMARY KEY([customer_code], [ticket_prefix], [ticket_code], [step_code] , [ticket_seq]));");
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_approval] ([customer_code] int NOT NULL, [ticket_prefix] int NOT NULL, [ticket_code] int NOT NULL, [ticket_seq] int NOT NULL, [step_code] int not null, [approval_status] text not null COLLATE NOCASE, [approval_question] text COLLATE NOCASE, [approval_type] text COLLATE NOCASE, [approval_comments] text COLLATE NOCASE, CONSTRAINT [pk_tk_ticket_approval] PRIMARY KEY( [customer_code] , [ticket_prefix] , [ticket_code] , [step_code] , [ticket_seq]));");
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_approval_rejection] ([customer_code] int NOT NULL, [ticket_prefix] int NOT NULL, [ticket_code] int NOT NULL, [ticket_seq] int NOT NULL, [step_code] int not null, [seq] int not null, [rejection_comments] text not null COLLATE NOCASE, [rejection_date] text not null COLLATE NOCASE, [rejection_user] int not null, [rejection_user_nick] text not null COLLATE NOCASE, CONSTRAINT [pk_tk_ticket_approval_rejection]PRIMARY KEY( [customer_code] , [ticket_prefix] , [ticket_code] , [step_code] , [ticket_seq], [seq]));");
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_brief] ([customer_code] int not null, [ticket_prefix] int not null, [ticket_code] int not null,  [ticket_id] text NOT NULL COLLATE nocase, [scn] int not null, [open_site_code] int not null, [open_site_desc] text not null collate nocase, [open_product_desc] text not null collate nocase, [open_serial_id] text not null collate nocase, [current_step_order] int, [ticket_status] text not null collate nocase, [origin_desc] text not null collate nocase, [step_desc] text COLLATE NOCASE, [step_order_seq] text COLLATE NOCASE, [forecast_start] text COLLATE NOCASE, [forecast_end] text COLLATE NOCASE, [step_count] int, [fcm] int not null default 0, [client_code] int, [client_name] text COLLATE nocase, [contract_code] int, [contract_desc] text COLLATE nocase, CONSTRAINT [pk_tk_ticket_brief] PRIMARY KEY([customer_code], [ticket_prefix], [ticket_code]));");
            script.append("CREATE TABLE IF NOT EXISTS [tk_ticket_origin_nc] ([customer_code] int not null, [ticket_prefix] int not null, [ticket_code] int not null, [page] int not null, [custom_form_order] int not null, [custom_form_data_type] text not null COLLATE NOCASE, [description] text not null COLLATE NOCASE, [data_value] text COLLATE NOCASE, [data_value_txt] text COLLATE NOCASE, [data_value_local] text COLLATE NOCASE, [data_photo1_url] text COLLATE NOCASE, [data_photo1_url_local] text COLLATE NOCASE, [data_photo2_url] text  COLLATE NOCASE, [data_photo2_url_local] text COLLATE NOCASE, [data_photo3_url] text COLLATE NOCASE, [data_photo3_url_local] text COLLATE NOCASE, [data_photo4_url] text COLLATE NOCASE, [data_photo4_url_local] text COLLATE NOCASE, [data_comment] text COLLATE NOCASE, [picture_lines] int, [picture_columns] int, [picture_color]  text COLLATE NOCASE, [picture_url] text COLLATE NOCASE, [picture_url_local] text COLLATE NOCASE, constraint pk_tk_ticket_origin_nc primary key(customer_code,ticket_prefix,ticket_code,page,custom_form_order));");
            //Tabelas Agendamento 2.0
            script.append("CREATE TABLE IF NOT EXISTS [md_schedule_exec] ( [customer_code] int NOT NULL, [schedule_prefix] int NOT NULL, [schedule_code] int NOT NULL, [schedule_exec] int NOT NULL, [schedule_desc] text NOT NULL COLLATE nocase, [schedule_type] text NOT NULL COLLATE nocase, [status] text not null default '" + ConstantBaseApp.SYS_STATUS_SCHEDULE + "' COLLATE nocase,[tag_operational_code] int NOT NULL, [tag_operational_id] text collate nocase, [tag_operational_desc] text collate nocase, [site_code] int NOT NULL, [site_id] text COLLATE nocase, [site_desc] text COLLATE nocase, [zone_code] int, [zone_id] text COLLATE nocase, [zone_desc] text COLLATE nocase, [operation_code] int NOT NULL, [operation_id] text COLLATE nocase, [operation_desc] text COLLATE nocase,  [product_code] int NOT NULL, [product_id] text COLLATE nocase, [product_desc] text COLLATE nocase, [serial_code] int, [serial_id] text COLLATE nocase, [custom_form_type] int, [custom_form_code] int , [custom_form_version] int , [custom_form_desc] text COLLATE NOCASE, [ticket_type] int , [ticket_type_id] text COLLATE NOCASE, [ticket_type_desc] text COLLATE NOCASE, [local_control] int NOT NULL DEFAULT 0, [io_control] int NOT NULL DEFAULT 0, [serial_rule] text COLLATE nocase, [serial_min_length] int, [serial_max_length] int, [site_restriction] int NOT NULL DEFAULT 0,   [product_icon_name] text COLLATE nocase, [product_icon_url] text COLLATE nocase, [product_icon_url_local] text NOT NULL DEFAULT '' COLLATE nocase, [require_location] int NOT NULL DEFAULT 0, [date_start] text NOT NULL COLLATE nocase, [date_end] text NOT NULL COLLATE nocase, [comments] text COLLATE nocase, [require_serial] int not null default 0,[allow_new_serial_cl] int not null default 0,[require_serial_done] int not null default 0,[sync_process] int not null default 0, [fcm_new_status] text default null COLLATE nocase, [fcm_user_nick] text default null COLLATE nocase, [schedule_erro_msg] text default null COLLATE nocase, [close_date] text default NULL COLLATE nocase, [serial_defined_by_server] int NOT NULL default 0, [is_so] int NOT NULL default 0, CONSTRAINT [pk_md_schedule_exec] PRIMARY KEY( [customer_code] , [schedule_prefix] , [schedule_code] , [schedule_exec] ));");
            //Review UI 4.0 the beggining of the end
            script.append("CREATE TABLE IF NOT EXISTS [md_tag] ([customer_code] int not null, [tag_code] int not null,[tag_id] text not null collate nocase,[tag_desc] text not null collate nocase, constraint [pk_md_tag] primary key(customer_code,tag_code));");
            script.append(TkCreateScriptsKt.TK_TICKET_CACHE_CREATE_SCRIPT);
            //Projeto OS no N-Form.
            script.append("CREATE TABLE IF NOT EXISTS [me_measure_tp] ([customer_code] int not null, [measure_tp_code] int not null,[measure_tp_id] text not null collate nocase,[measure_tp_desc] text not null collate nocase,[value_sufix] text collate nocase, [restriction_type] text collate nocase, [restriction_min] real,[restriction_max] real, [restriction_decimal] int,[value_cycle_size] real,[cycle_tolerance] int, [without_measure] int not null DEFAULT 0,  constraint [pk_me_measure_tp] primary key(customer_code,measure_tp_code));");
            script.append("CREATE TABLE IF NOT EXISTS [md_device_tp] ([customer_code] int not null, [device_tp_code] int not null,[device_tp_id] text not null collate nocase,[device_tp_desc] text not null collate nocase,  constraint [pk_md_device_tp] primary key(customer_code,device_tp_code));");
            script.append(MDItemCheckScriptKt.ITEM_CHECK_CREATE_SCRIPT);
            script.append(OrderTypeScriptKt.ORDER_TYPE_CREATE_SCRIPT);
            script.append("CREATE TABLE IF NOT EXISTS [md_product_serial_tp_device] ([customer_code] int not null,[product_code] int not null, [serial_code] int not null, [device_tp_code] int not null, [order_seq] int not null, [tracking_number] text collate nocase, [show_empty] int not null, constraint [pk_md_product_serial_tp_device] primary key(customer_code,product_code,serial_code,device_tp_code));");
            script.append(MDProductSerialTpDeviceItemScriptKt.getMdProductSerialTpDeviceItemTableScript());
            script.append(TpDeviceItemHistScriptKt.getMDProductSerialTpDeviceItemHistScript());
            script.append("CREATE TABLE IF NOT EXISTS [md_product_serial_tp_device_item_material]( [customer_code] int not null, [product_code] int not null, [serial_code] int not null, [device_tp_code] int not null, [item_check_code] int not null, [item_check_seq] int not null, [material_code] int not null, [qty] real not null, [origin] text, constraint [pk_md_product_serial_tp_device_item_material] primary key(customer_code,product_code,serial_code,device_tp_code,item_check_code,item_check_seq,material_code));");
            //Projeto OS no N-Form. TAbelas de Resposta
            script.append(GeOsScriptKt.GE_OS_CREATE_SCRIPT);
            script.append("CREATE TABLE IF NOT EXISTS [ge_os_device] ([customer_code] int not null, [custom_form_type]int not null, [custom_form_code]int not null, [custom_form_version]int not null, [custom_form_data]int not null, [product_code] int not null, [serial_code] int not null, [device_tp_code] int not null, [device_tp_id] text not null default '' collate nocase, [device_tp_desc] text not null default '' collate nocase, [order_seq] int not null, [tracking_number] text collate nocase,  [show_empty] int not null, constraint [pk_ge_os_device] primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,product_code,serial_code,device_tp_code));");
            script.append(GeOsDeviceItemScriptKt.getGeOsDeviceItemScript());
            script.append("CREATE TABLE IF NOT EXISTS [ge_os_device_item_material]( [customer_code] int not null, [custom_form_type]int not null, [custom_form_code]int not null, [custom_form_version]int not null, [custom_form_data]int not null, [product_code] int not null, [serial_code] int not null, [device_tp_code] int not null, [item_check_code] int not null, [item_check_seq] int not null, [material_code] int not null, [material_id] text not null collate nocase, [material_desc] text not null collate nocase, [material_qty] real not null, [material_unit] text collate nocase, [creation_ms] int not null, [material_planned] int not null default 0, [material_planned_used] int not null default 0, [material_planned_qty] real, [origin] text , constraint [pk_ge_os_device_item_material] primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,product_code,serial_code,device_tp_code,item_check_code,item_check_seq,material_code));");
            script.append("CREATE TABLE IF NOT EXISTS [ge_os_device_item_hist]( [customer_code] int not null, [custom_form_type]int not null, [custom_form_code]int not null, [custom_form_version]int not null, [custom_form_data]int not null, [product_code] int not null, [serial_code] int not null, [device_tp_code] int not null, [item_check_code] int not null, [item_check_seq] int not null, [seq] int not null, [exec_type] text not null collate nocase, [exec_value] real not null, [exec_date] text not null collate nocase, [exec_comment] text collate nocase, [exec_material] int not null, [change_adjust] int not null default 0, constraint [pk_ge_os_device_item_hist] primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,product_code,serial_code,device_tp_code,item_check_code,item_check_seq,seq));");
            //
            script_dados.append(" insert into ev_modules (module_code, module_name) values ('APP_PRJ001', 'APP PRJ 01');");
            script_dados.append(" insert into ev_modules (module_code, module_name) values ('CUST_FORM', 'Custom FormF');");
            script_dados.append(" insert into ev_modules (module_code, module_name) values ('SYS', 'System');");
            //
            script.append("CREATE TABLE IF NOT EXISTS [md_justify_item] ([customer_code] int not null,[justify_group_code] int not null,[justify_item_code] int not null collate nocase,[justify_item_id] text not null collate nocase,[justify_item_desc] text not null collate nocase,[required_comment] int not null collate nocase,[reschedule] int not null collate nocase, constraint [pk_md_tag] primary key(customer_code,justify_group_code,justify_item_code));");
            //
            script.append("create table if not exists [tk_ticket_type_products] ([customer_code] int not null, [ticket_type_code] int not null, [product_code] int not null, constraint pk_tk_ticket_type_products primary key([customer_code], [ticket_type_code], [product_code]));");
            script.append("create table if not exists [tk_ticket_type_operations] ([customer_code] int NOT NULL, [ticket_type_code] int NOT NULL, [operation_code] int not null, constraint pk_tk_ticket_type_operations primary key([customer_code], [ticket_type_code], [operation_code]));");
            script.append("create table if not exists [tk_ticket_type_sites] ([customer_code] int not null, [ticket_type_code] int not null, [site_code] int not null, constraint pk_tk_ticket_type_sites primary key([customer_code], [ticket_type_code], [site_code]));");
            script.append("create table if not exists [tk_ticket_type] ([customer_code] int not null, [ticket_type_code] int not null, [ticket_type_id] text not null collate nocase, [ticket_type_desc] text not null collate nocase,[all_site] int not null,[all_operation] int not null,[all_product] int not null,[tag_operational_code] int not null, constraint pk_tk_ticket_type_sites primary key([customer_code], [ticket_type_code]));");
            //
            script.append("create table if not exists [md_product_serial_tp_device_item_hist_mat]\n" +
                    "(\n" +
                    "    [customer_code]   int  not null,\n" +
                    "    [product_code]    int  not null,\n" +
                    "    [serial_code]     int  not null,\n" +
                    "    [device_tp_code]  int  not null,\n" +
                    "    [item_check_code] int  not null,\n" +
                    "    [item_check_seq]  int  not null,\n" +
                    "    [seq]             int  not null,\n" +
                    "    [material_code]   int  not null,\n" +
                    "    [un]              text not null collate nocase,\n" +
                    "    [qty]             real not null,\n" +
                    "    [qty_planned]     real  not null,\n" +
                    "    [material_action] int  not null,\n" +
                    "    constraint [pk_md_product_serial_tp_device_item_hist_mat] primary key (\n" +
                    "    [customer_code],\n" +
                    "    [product_code],\n" +
                    "    [serial_code],\n" +
                    "    [device_tp_code],\n" +
                    "    [item_check_seq],\n" +
                    "    [item_check_code],\n" +
                    "    [seq],\n" +
                    "    [material_code]));");
            //
            script.append(FSCreateScriptKt.FS_TRIP_CREATE_SCRIPT);
            script.append(FSCreateScriptKt.FS_TRIP_EVENT_TYPE_CREATE_SCRIPT);
            script.append(FSCreateScriptKt.FS_TRIP_USER_CREATE_SCRIPT);
            script.append(FSCreateScriptKt.FS_TRIP_EVENT_CREATE_SCRIPT);
            script.append(FSCreateScriptKt.FS_TRIP_POSITION_CREATE_SCRIPT);
            script.append(FSCreateScriptKt.FS_TRIP_DESTINATION_CREATE_SCRIPT);
            script.append(FSCreateScriptKt.FS_TRIP_DESTINATION_ACTION_CREATE_SCRIPT);
            script.append(RegionScriptKt.MD_REGION_CREATE_SCRIPT);
            script.append(VerificationGroupKt.getMdVerificationGroupDatabaseTable());
            script.append(TkTicketVGScriptKt.getTkTicketVGDatabaseTable());
            script.append(MDProductSerialVGScriptKt.getVGProductSerialScript());
            script.append(GEOsVgScriptKt.getGEOsVgScript());
            script.append(EventManualScriptKt.getEventManualTable().generateCreateTableScript());
            script.append(MDItemCheckLabelScriptKt.getMDItemCheckLabelTable().generateCreateTableScript());
            script.append(MDItemCheckLabelIconScriptKt.getMDItemCheckLabelIconTable().generateCreateTableScript());
            //
            String[] scripts = script.toString().split(";");
            String[] scripts_dados = script_dados.toString().split(";");
            //
            for (int i = 0; i < scripts.length; i++) {
                db.execSQL(scripts[i].toLowerCase() + ";");
                //
                Log.d("script", scripts[i].toLowerCase());
            }
            //
            for (int i = 0; i < scripts_dados.length; i++) {
                db.execSQL(scripts_dados[i]);
                Log.d("script 2", scripts[i].toLowerCase());
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            setMigrationError(true);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
        try {
            switch (oldVersion) {
                case 1:
                    MigrationsKt.getMigrationV1().migrate(db);
                case 2:
                    MigrationsKt.getMigrationV2().migrate(db);
                case 3:
                    MigrationsKt.getMigrationV3().migrate(db);
                case 4:
                    MigrationsKt.getMigrationV4().migrate(db);
                case 5:
                    MigrationsKt.getMigrationV5().migrate(db);
                case 6:
                    MigrationsKt.getMigrationV6().migrate(db);
                case 7:
                    MigrationsKt.getMigrationV7().migrate(db);
                case 8:
                    MigrationsKt.getMigrationV8().migrate(db);
                case 9:
                    MigrationsKt.getMigrationV9().migrate(db);
                case 10:
                    MigrationsKt.getMigrationV10().migrate(db);
                case 11:
                    MigrationsKt.getMigrationV11().migrate(db);
                case 12:
                    MigrationsKt.getMigrationV12().migrate(db);
                case 13:
                    MigrationsKt.getMigrationV13().migrate(db);
                case 14:
                    MigrationsKt.getMigrationV14().migrate(db);
                case 15:
                    MigrationsKt.getMigrationV15().migrate(db);
                case 16:
                    MigrationsKt.getMigrationV16().migrate(db);
                case 17:
                    MigrationsKt.getMigrationV17().migrate(db);
                case 18:
                    MigrationsKt.getMigrationV18().migrate(db);
                case 19:
                    MigrationsKt.getMigrationV19().migrate(db);
                case 20:
                    MigrationsKt.getMigrationV20().migrate(db);
                case 21:
                    MigrationsKt.getMigrationV21().migrate(db);
                case 22:
                    MigrationsKt.getMigrationV22().migrate(db);
                case 23:
                    MigrationsKt.getMigrationV23().migrate(db);
                case 24:
                    MigrationsKt.getMigrationV24().migrate(db);
                case 25:
                    MigrationsKt.getMigrationV25().migrate(db);
                case 26:
                    MigrationsKt.getMigrationV26().migrate(db);
                case 27:
                    MigrationsKt.getMigrationV27().migrate(db);
                case 28:
                    MigrationsKt.getMigrationV28().migrate(db);
                case 29:
                    MigrationsKt.getMigrationV29().migrate(db);
                case 30:
                    MigrationsKt.getMigrationV30().migrate(db);
                case 31:
                    MigrationsKt.getMigrationV31().migrate(db);
                    break;
            }

            updateSyncUser();
            sendFCMStatus();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            setMigrationError(true);
        }
    }

    private void updateSyncUser() {
        ToolBox_Inf.updateUserCustomerSync(
                context,
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                ToolBox_Con.getPreference_User_Code(context),
                1
        );
    }

    private void sendFCMStatus() {
        Intent mIntent = new Intent();
        mIntent.setAction(Constant.WS_FCM);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(ConstantBaseApp.SW_TYPE, FCM_MODULE_SYNC);
        //
        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);
    }

    private void setMigrationError(boolean hasError) {
        ToolBox_Con.setBooleanPreference(
                context,
                DB_MULTI_STATUS_ERROR,
                hasError
        );
    }
}

