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
            script.append("create table if not exists [ev_modules] ([module_code] text not null COLLATE NOCASE, [module_name] text not null COLLATE NOCASE, constraint pk_modules primary key(module_code));");
            script.append("create table if not exists [ev_module_ress] ([module_code] text not null COLLATE NOCASE, [resource_code] int not null, [resource_name] text not null COLLATE NOCASE, constraint pk_module_ress primary key(module_code,resource_code));");
            script.append("create table if not exists [ev_module_res_txts] ([module_code] text not null COLLATE NOCASE, [resource_code] int not null, [txt_code] text not null COLLATE NOCASE, [txt_ref] int not null, constraint pk_module_res_txts primary key(module_code,resource_code,txt_code));");
            script.append("create table if not exists [ev_module_res_txt_transs] ([module_code] text not null COLLATE NOCASE, [resource_code] int not null, [txt_code] text not null COLLATE NOCASE, [translate_code] int not null, [txt_value] text not null COLLATE NOCASE, constraint pk_module_res_txt_transs primary key(module_code,resource_code,txt_code,translate_code));");
            script.append("create table if not exists [ge_custom_form_types] ([customer_code] int not null, [custom_form_type] int not null, [active] int not null, constraint pk_form_types primary key(customer_code,custom_form_type));");
            script.append("create table if not exists [ge_custom_forms] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_status] text not null COLLATE NOCASE, [require_signature] int not null,constraint pk_forms primary key(customer_code,custom_form_type,custom_form_code,custom_form_version));");
            script.append("create table if not exists [ge_custom_form_fields] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_seq] int not null, [custom_form_data_type] text not null COLLATE NOCASE, [custom_form_data_size] int not null, [custom_form_data_mask] text not null COLLATE NOCASE, [custom_form_data_content] text not null COLLATE NOCASE, [custom_form_local_link] text not null COLLATE NOCASE, [custom_form_order] int not null, [page] int not null, [required] int not null, constraint pk_form_fields primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_seq));");
            script.append("create table if not exists [ge_custom_form_datas] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_data] int not null,[custom_form_status] text not null COLLATE NOCASE, [product_code] int not null, [serial_id] text not null COLLATE NOCASE,[date_start] text not null COLLATE NOCASE, [date_end] text not null COLLATE NOCASE, [user_code_start] int not null, [user_code_end] int not null, [token] text not null COLLATE NOCASE, constraint pk_forms primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data));");
            script.append("create table if not exists [ge_custom_form_data_fields] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [custom_form_data] int not null, [custom_form_seq] int not null,[value] text not null COLLATE NOCASE, [value_extra] text not null COLLATE NOCASE,\tconstraint pk_forms primary key(customer_code,custom_form_type,custom_form_code,custom_form_version,custom_form_data,custom_form_seq));");
            script.append("create table if not exists [ge_custom_form_products] ([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [product_code] int not null, [active] int not null, constraint pk_ge_custom_form_products primary key(customer_code, custom_form_type, custom_form_code, custom_form_version, product_code));");
            script.append("create table if not exists [md_products] ([customer_code] int not null, [product_code] int not null, [product_id] text not null COLLATE NOCASE, [product_desc] text not null COLLATE NOCASE, [require_serial] int not null, [allow_new_serial_cl] int not null, constraint pk_md_products primary key(customer_code, product_code));");
            script.append("create table if not exists [md_product_groups]( [customer_code] int not null,  [group_code] int not null, [recursive_code] int not null, [recursive_code_father] int, [group_id] text not null COLLATE NOCASE,  [group_desc] text not null COLLATE NOCASE,  constraint pk_md_product_groups primary key(customer_code, group_code));");
            script.append("create table if not exists [md_product_group_products]( [customer_code] int not null,  [group_code] int not null, [product_code] int not null,  constraint pk_md_product_group_products primary key(customer_code, group_code,product_code));");
            script.append("create table if not exists [md_operations] ([customer_code] int not null, [operation_code] int not null, [operation_desc] text not null COLLATE NOCASE, [alias_service_oper] int not null, [alias_service_com] int not null, constraint pk_md_operations primary key(customer_code, operation_code));");
            script.append("create table if not exists [md_sites] ([customer_code] int not null, [site_code] int not null, [site_id] text not null COLLATE NOCASE,  [site_desc] text not null COLLATE NOCASE, constraint pk_md_sites primary key(customer_code, site_code));");
            script.append("create table if not exists [ge_custom_form_blobs]([customer_code] int not null, [custom_form_type] int not null, [custom_form_code] int not null, [custom_form_version] int not null, [blob_code] int not null, [blob_name] text not null COLLATE NOCASE, [blob_url] text not null COLLATE NOCASE, constraint [pk_ge_custom_form_blobs] primary key([customer_code], [custom_form_type] , [custom_form_code] , [custom_form_version] , [blob_code]));");
            //
            script_dados.append(" insert into ev_modules (module_code, module_name) values ('APP_SMS001', 'APP SMS 01');");
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

