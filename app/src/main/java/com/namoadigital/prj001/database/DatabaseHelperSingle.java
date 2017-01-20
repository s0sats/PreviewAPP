package com.namoadigital.prj001.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by neomatrix on 18/01/17.
 */

public class DatabaseHelperSingle extends SQLiteOpenHelper {

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
            script.append("create table if not exists [parametros] ([parametro_code] int not null, [nome] text not null COLLATE NOCASE, [descricao] text not null COLLATE NOCASE, [valor_default] text not null COLLATE NOCASE, [valor_customizado] text not null COLLATE NOCASE, constraint pk_parametros primary key(parametro_code));");
            script.append("create table if not exists [ev_users] ([user_code] int not null, [user_nick] text not null COLLATE NOCASE, [email_p] text not null COLLATE NOCASE, constraint pk_users primary key(user_code));");
            script.append("create table if not exists [ev_user_customers] ([user_code] int not null, [customer_code] int not null, [customer_name] text not null COLLATE NOCASE, [translate_code] int not null, [language_code] text not null COLLATE NOCASE, [translate_desc] text not null COLLATE NOCASE, [nls_date_format] text not null COLLATE NOCASE, [keyuser] int not null, [blocked] int not null, [session_app] text not null COLLATE NOCASE,[pending] int not null DEFAULT 0, constraint pk_list_customers primary key(user_code,customer_code));");
            //
            String[] scripts        = script.toString().split(";");
            String[] scripts_dados =  script_dados.toString().split(";");
            //
            for (int i = 0; i < scripts.length; i++) {
                db.execSQL(scripts[i].toLowerCase());
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
        script.append("drop table if exists parametros;");
        script.append("drop table if exists ev_users;");
        script.append("drop table if exists ev_user_customers;");
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

