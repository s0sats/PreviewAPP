package com.namoadigital.prj001.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by neomatrix on 18/01/17.
 */

public class DatabaseHelperSingle extends DatabaseBaseHelper {

    private Context context;

    public DatabaseHelperSingle(Context context, String DB_NAME, int DB_VERSION) {
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
            script.append("create table if not exists [parametros] ([parametro_code] int not null, [nome] text not null DEFAULT '' COLLATE NOCASE, [descricao] text not null DEFAULT '' COLLATE NOCASE, [valor_default] text not null DEFAULT '' COLLATE NOCASE, [valor_customizado] text not null DEFAULT '' COLLATE NOCASE, constraint pk_parametros primary key(parametro_code));");
            script.append("create table if not exists [ev_users]([user_code] int CONSTRAINT [pk_users] PRIMARY KEY NOT NULL, [user_nick] text NOT NULL DEFAULT '' COLLATE nocase, [email_p] text NOT NULL DEFAULT '' COLLATE nocase, [admin] int NOT NULL DEFAULT 0,[exist_nfc] int NOT NULL DEFAULT 0, [nfc_blocked] int NOT NULL DEFAULT 0);");
            script.append("create table if not exists [ev_user_customers] ([user_code] int not null, [customer_code] int not null, [customer_name] text not null DEFAULT '' COLLATE NOCASE, [translate_code] int not null, [language_code] text not null DEFAULT '' COLLATE NOCASE, [translate_desc] text not null DEFAULT '' COLLATE NOCASE, [nls_date_format] text not null DEFAULT '' COLLATE NOCASE, [keyuser] int not null, [blocked] int not null, [session_app] text not null DEFAULT '' COLLATE NOCASE,[pending] int not null DEFAULT 0, [logo_url] text NOT NULL DEFAULT '' COLLATE NOCASE, [tracking] int not null default 0, [sync_required] int not null default 0, [timezone] text not null COLLATE NOCASE, [license_control_type] text not null COLLATE NOCASE, [license_site_code] int, [license_site_desc] text COLLATE nocase, [license_user_level_code] int, [license_user_level_id] text COLLATE nocase, [license_user_level_value] int, [license_user_level_changed] int, constraint pk_list_customers primary key(user_code,customer_code));");
            script.append("create table if not exists [ev_user_customer_parameters]([customer_code] INT NOT NULL, [parameter_code] TEXT NOT NULL DEFAULT '' COLLATE NOCASE, PRIMARY KEY([customer_code], [parameter_code]));");
            script.append("create table if not exists [fcmmessages] ([fcmmessage_code] INTEGER PRIMARY KEY AUTOINCREMENT, [customer] text not null DEFAULT '' COLLATE NOCASE, [type] text not null DEFAULT '' COLLATE NOCASE, [title] text not null DEFAULT '' COLLATE NOCASE, [msg_short] text not null DEFAULT '' COLLATE NOCASE, [msg_long] text not null DEFAULT '' COLLATE NOCASE, [module] text not null DEFAULT '' COLLATE NOCASE, [sender] text not null DEFAULT '' COLLATE NOCASE, [sync] text not null DEFAULT '' COLLATE NOCASE, [status] text not null DEFAULT '0' COLLATE NOCASE, [date_create] text not null DEFAULT '1900-01-01' COLLATE NOCASE, [date_create_ms] int not null );");
            //
            String[] scripts = script.toString().split(";");
            String[] scripts_dados = script_dados.toString().split(";");
            //
            for (int i = 0; i < scripts.length; i++) {
                db.execSQL(scripts[i].toLowerCase());
            }
            //
            for (int i = 0; i < scripts_dados.length; i++) {
                db.execSQL(scripts_dados[i]);
            }

        } catch (Exception e) {
            String res = e.toString();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder script = new StringBuilder();
        //
        script.append("drop table if exists [parametros];");
        script.append("drop table if exists [ev_users];");
        script.append("drop table if exists [ev_user_customers];");
        script.append("drop table if exists [ev_user_customer_parameters];");
        script.append("drop table if exists [fcmmessages];");
        //
        String[] scripts = script.toString().split(";");
        //
        for (int i = 0; i < scripts.length; i++) {
            db.execSQL(scripts[i].toLowerCase());
        }
        //
        onCreate(db);
    }
}

