package com.namoadigital.prj001.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by neomatrix on 18/01/17.
 */

public class DatabaseHelperMulti extends SQLiteOpenHelper {

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
            script.append("create table if not exists [ge_custom_form_types] ([customer_code] int not null, [custom_form_type] int not null, constraint pk_form_types primary key(customer_code,custom_form_type));");

            script.append("create table if not exists [ge_custom_forms] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [require_signature] int not null, [require_location] int not null DEFAULT 0, [automatic_fill] text NOT NULL DEFAULT '' COLLATE nocase, constraint pk_forms primary key(customer_code,custom_form_type,custom_form_code,custom_form_version));");
            script.append("create table if not exists [ge_custom_form_fields] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_seq] int not null, [custom_form_data_type] text not null DEFAULT '' COLLATE NOCASE, [custom_form_data_size] int, [custom_form_data_mask] text not null DEFAULT '' COLLATE NOCASE, [custom_form_data_content] text not null DEFAULT '' COLLATE NOCASE, [custom_form_local_link] text not null DEFAULT '' COLLATE NOCASE, [custom_form_order] int not null, [page] int not null, [required] int not null, [automatic] text not null DEFAULT '' COLLATE NOCASE, [comment] text not null DEFAULT '' COLLATE NOCASE, constraint pk_form_fields primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_seq));");

            script.append("create table if not exists [ge_custom_forms_local]([customer_code] int NOT NULL, [custom_form_type] int NOT NULL, [custom_form_type_desc] text NOT NULL DEFAULT '' COLLATE nocase, [custom_form_code] int NOT NULL, [custom_form_version] int NOT NULL, [custom_form_desc] text NOT NULL DEFAULT '' COLLATE nocase, [custom_form_data] int NOT NULL, [custom_form_pre] text NOT NULL DEFAULT '' COLLATE nocase, [custom_form_status] text NOT NULL DEFAULT '' COLLATE nocase, [custom_form_data_serv] int , [custom_product_code] int NOT NULL, [custom_product_desc] text NOT NULL DEFAULT '' COLLATE nocase, [custom_product_id] text NOT NULL DEFAULT '' COLLATE nocase, [serial_id] text NOT NULL DEFAULT '' COLLATE nocase, [require_signature] int NOT NULL, [require_location] int not null DEFAULT 0, [automatic_fill] text NOT NULL DEFAULT '' COLLATE nocase, [schedule_date_start_format] text NOT NULL DEFAULT '' COLLATE NOCASE, [schedule_date_end_format] text NOT NULL DEFAULT '' COLLATE NOCASE,[schedule_date_start_format_ms] INT NOT NULL DEFAULT 0,[schedule_date_end_format_ms] INT NOT NULL DEFAULT 0, [require_serial] int not null DEFAULT 0, [allow_new_serial_cl] int not null DEFAULT 0, CONSTRAINT [pk_forms] PRIMARY KEY([customer_code], [custom_form_type], [custom_form_code], [custom_form_version], [custom_form_data]));");
            script.append("create table if not exists [ge_custom_form_fields_local] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_data] int not null,[custom_form_data_serv] int, [custom_form_seq] int not null, [custom_form_data_type] text not null DEFAULT '' COLLATE NOCASE, [custom_form_data_size] int, [custom_form_data_mask] text not null DEFAULT '' COLLATE NOCASE, [custom_form_data_content] text not null DEFAULT '' COLLATE NOCASE, [custom_form_local_link] text not null DEFAULT '' COLLATE NOCASE, [custom_form_order] int not null, [page] int not null, [required] int not null, [automatic] text not null DEFAULT '' COLLATE NOCASE, [comment] text not null DEFAULT '' COLLATE NOCASE, [custom_form_field_desc] text not null DEFAULT '' COLLATE NOCASE, constraint pk_form_fields primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,custom_form_seq));");
            script.append("create table if not exists [ge_custom_form_blobs_local]([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [blob_code] int not null, [blob_name] text not null DEFAULT '' COLLATE NOCASE, [blob_url] text not null DEFAULT '' COLLATE NOCASE, [blob_url_local] text not null DEFAULT '' COLLATE NOCASE, constraint [pk_ge_custom_form_blobs] primary key([customer_code], [custom_form_type] , [custom_form_code] , [custom_form_version] , [blob_code]));");

            script.append("create table if not exists [ge_custom_form_datas]( [customer_code] int NOT NULL, [custom_form_type] int NOT NULL, [custom_form_code] int NOT NULL, [custom_form_version] int NOT NULL, [custom_form_data] int NOT NULL, [custom_form_data_serv] int, [custom_form_status] text NOT NULL DEFAULT '' COLLATE nocase, [product_code] int NOT NULL, [serial_id] text NOT NULL DEFAULT '' COLLATE nocase, [date_start] text NOT NULL DEFAULT '' COLLATE nocase, [date_end] text NOT NULL DEFAULT '' COLLATE nocase, [user_code] int NOT NULL, [site_code] int NOT NULL, [operation_code] int NOT NULL, [signature] text DEFAULT '' COLLATE NOCASE, [signature_name] text DEFAULT '' COLLATE nocase, [token] text NOT NULL DEFAULT '' COLLATE nocase, [location_type] text DEFAULT '' COLLATE nocase, [location_lat] text DEFAULT '' COLLATE nocase, [location_lng] text DEFAULT '' COLLATE nocase, CONSTRAINT [pk_form_datas] PRIMARY KEY([customer_code], [custom_form_type], [custom_form_code], [custom_form_version], [custom_form_data]));");
            script.append("create table if not exists [ge_custom_form_data_fields] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_data] int not null, [custom_form_seq] int not null, [custom_form_data_serv] int, [value] text not null DEFAULT '' COLLATE NOCASE, [value_extra] text not null DEFAULT '' COLLATE NOCASE, constraint pk_form_data_fields primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,custom_form_seq));");
            script.append("create table if not exists [ge_custom_form_products] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [product_code] int not null, constraint pk_ge_custom_form_products primary key(customer_code, custom_form_type, custom_form_code, custom_form_version, product_code));");
            script.append("create table if not exists [ge_custom_form_blobs]([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [blob_code] int not null, [blob_name] text not null DEFAULT '' COLLATE NOCASE, [blob_url] text not null DEFAULT '' COLLATE NOCASE, [blob_url_local] text not null DEFAULT '' COLLATE NOCASE, constraint [pk_ge_custom_form_blobs] primary key([customer_code], [custom_form_type] , [custom_form_code] , [custom_form_version] , [blob_code]));");
            script.append("create table if not exists [ge_custom_form_operations]([customer_code] int NOT NULL, [custom_form_type] int NOT NULL, [custom_form_code] int NOT NULL, [custom_form_version] int NOT NULL, [operation_code] int NOT NULL, PRIMARY KEY([customer_code], [custom_form_type], [custom_form_code], [custom_form_version], [operation_code]));");
            script.append("create table if not exists [ge_files]([file_code] text not null DEFAULT '' COLLATE NOCASE, [file_path] text not null DEFAULT '' COLLATE NOCASE, [file_status] text not null DEFAULT '' COLLATE NOCASE, [file_date] text not null DEFAULT '' COLLATE NOCASE, primary key(file_code));");

            script.append("create table if not exists [md_products] ([customer_code] int not null, [product_code] int not null, [product_id] text not null DEFAULT '' COLLATE NOCASE, [product_desc] text not null DEFAULT '' COLLATE NOCASE, [require_serial] int not null, [allow_new_serial_cl] int not null, constraint pk_md_products primary key(customer_code, product_code));");
            script.append("create table if not exists [md_product_groups]( [customer_code] int not null,  [group_code] int not null, [recursive_code] int not null, [recursive_code_father] int, [group_id] text not null DEFAULT '' COLLATE NOCASE,  [group_desc] text not null DEFAULT '' COLLATE NOCASE,  constraint pk_md_product_groups primary key(customer_code, group_code));");
            script.append("create table if not exists [md_product_group_products]( [customer_code] int not null,  [group_code] int not null, [product_code] int not null,  constraint pk_md_product_group_products primary key(customer_code, group_code,product_code));");
            script.append("create table if not exists [md_product_serials]([customer_code] int not null,[product_code] int not null,[serial_code] int not null,[serial_id] text not null collate nocase,[site_code] int not null,[zone_code] int not null,[local_code] int not null,[site_code_owner] int,[brand_code] int,[model_code] int,[color_code] int,[segment_code] int,[category_price_code] int,[update_required] int not null default 0,constraint [pk_md_product_serials] primary key([customer_code],[product_code],[serial_code]));");
            script.append("create table if not exists [md_operations] ([customer_code] int not null, [operation_code] int not null, [operation_id] text not null DEFAULT '' COLLATE NOCASE, [operation_desc] text not null DEFAULT '' COLLATE NOCASE, [alias_service_oper] int not null, [alias_service_com] int not null, constraint pk_md_operations primary key(customer_code, operation_code));");
            script.append("create table if not exists [md_sites] ([customer_code] int not null, [site_code] int not null, [site_id] text not null DEFAULT '' COLLATE NOCASE,  [site_desc] text not null DEFAULT '' COLLATE NOCASE, constraint pk_md_sites primary key(customer_code, site_code));");
            script.append("CREATE TABLE IF NOT EXISTS [md_site_zones]([customer_code] int not null,[site_code] int not null,[zone_code] int not null,[zone_id] text not null collate nocase,[zone_desc] text not null collate nocase,[blocked] int not null  default 0,[process_seq] int,constraint [pk_md_site_zones] primary key([customer_code],[site_code],[zone_code]));");
            script.append("CREATE TABLE IF NOT EXISTS [md_site_zone_locals]([customer_code] int not null,[site_code] int not null,[zone_code] int not null,[local_code] int not null,[local_id] text not null collate nocase,[capacity] int not null,constraint [pk_md_site_zone_locals] primary key([customer_code],[site_code],[zone_code],[local_code]));");
            script.append("create table if not exists [sync_checklist]([customer_code] int not null, [product_code] int not null, [last_update] text not null , CONSTRAINT [pk_sync_checklist] primary key([customer_code], [product_code]));");
            //TABLES SO
            script.append("create table if not exists [sm_sos]([customer_code] int not null,[so_prefix] int not null,[so_code] int not null,[so_desc] text not null collate nocase,[so_scn] int not null,[product_code] int not null,[product_id] text not null collate nocase,[product_desc] text not null collate nocase,[serial_code] int not null,[serial_id] text not null collate nocase,[category_price_code] int not null,[category_price_id] text not null collate nocase,[category_price_desc] text not null collate nocase,[segment_code] int not null,[segment_id] text not null collate nocase,[segment_desc] text not null collate nocase,\t[site_code] int not null,[site_id] text not null collate nocase,[site_desc] text not null collate nocase,\t[operation_code] int not null,[operation_id] text not null collate nocase,[operation_desc] text not null collate nocase,\t\t[contract_code] int not null,[contract_desc] text not null collate nocase,[contract_po_erp] text collate nocase,[contract_po_client1] text collate nocase,[contract_po_client2] text  collate nocase,[priority_code] int not null,[priority_desc] text not null collate nocase,[status] text not null collate nocase,[quality_approval_user] int,[quality_approval_date] text,[comments] text collate nocase,[so_father_prefix] int,[so_father_code] int,[deadline] text collate nocase,[origin] text not null collate nocase,[client_type] text not null collate nocase,[client_user] int,[client_code] int,[client_id] text collate nocase,[client_name] text not null collate nocase,[client_email] text collate nocase,[client_phone] text collate nocase,[client_approval_image] int,[client_approval_date] text,[client_approval_flag] int not null,constraint [pk_sm_sos] primary key([customer_code],[so_prefix],[so_code]));");
            script.append("create table if not exists [md_segments]([customer_code] int not null,[segment_code] int not null,[segment_id] text not null collate nocase,[segment_desc] text not null collate nocase,constraint [pk_md_segments] primary key([customer_code],[segment_code]));");
            script.append("create table if not exists [md_category_prices]([customer_code] int not null,[category_price_code] int not null,[category_price_id] text not null collate nocase,[category_price_desc] text not null collate nocase,constraint [pk_md_category_prices] primary key([customer_code],[category_price_code]));");
            script.append("create table if not exists [md_brands]([customer_code] int not null,[brand_code] int not null,[brand_id] text not null collate nocase,[brand_desc] text not null collate nocase,constraint [pk_md_brands] primary key([customer_code],[brand_code]));");
            script.append("create table if not exists [md_brand_models]([customer_code] int not null,[brand_code] int not null,[model_code] int not null,[model_id] text not null collate nocase,[model_desc] text not null collate nocase,constraint [pk_md_brand_models] primary key([customer_code],[brand_code],[model_code]));");
            script.append("create table if not exists [md_brand_colors]([customer_code] int not null,[brand_code] int not null,[color_code] int not null,[color_id] text not null collate nocase,[color_desc] text not null collate nocase,constraint [pk_md_brand_colors] primary key([customer_code],[brand_code],[color_code]));");

            //
            script_dados.append(" insert into ev_modules (module_code, module_name) values ('APP_PRJ001', 'APP PRJ 01');");
            script_dados.append(" insert into ev_modules (module_code, module_name) values ('CUST_FORM', 'Custom FormF');");
            script_dados.append(" insert into ev_modules (module_code, module_name) values ('SYS', 'System');");
            //
            String[] scripts = script.toString().split(";");
            String[] scripts_dados = script_dados.toString().split(";");
            //
            for (int i = 0; i < scripts.length; i++) {
                db.execSQL(scripts[i].toLowerCase() + ";");
            }
            //
            for (int i = 0; i < scripts_dados.length; i++) {
                db.execSQL(scripts_dados[i]);
            }

        } catch (Exception e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder script = new StringBuilder();
        //
        script.append("drop table if exists ev_modules;");
        script.append("drop table if exists ev_module_ress;");
        script.append("drop table if exists ev_module_res_txts;");
        script.append("drop table if exists ev_module_res_txt_transs;");
        script.append("drop table if exists ge_custom_form_types;");
        script.append("drop table if exists ge_custom_forms;");
        script.append("drop table if exists ge_custom_form_fields;");
        script.append("drop table if exists ge_custom_form_datas;");
        script.append("drop table if exists ge_custom_form_data_fields;");
        script.append("drop table if exists ge_custom_form_products;");
        script.append("drop table if exists md_products;");
        script.append("drop table if exists md_product_categorys;");
        script.append("drop table if exists md_operations;");
        script.append("drop table if exists md_sites;");
        //
        String[] scripts = script.toString().split(";");
        //
        for (int i = 0; i < scripts.length; i++) {
            db.execSQL(scripts[i].toLowerCase() + ";");
        }
        //
        onCreate(db);
    }
}

