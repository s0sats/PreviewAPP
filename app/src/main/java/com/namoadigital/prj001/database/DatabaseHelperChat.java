package com.namoadigital.prj001.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luche on 29/11/2017.
 */

public class DatabaseHelperChat extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHelperChat(Context context, String DB_NAME, int DB_VERSION) {
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
            script.append("create table if not exists [ch_rooms] ([room_code] text not null COLLATE NOCASE, [room_type] text not null COLLATE NOCASE, [room_desc] text not null COLLATE NOCASE, [customer_code] int , [room_obj] text DEFAULT '' COLLATE NOCASE,[room_image] text DEFAULT '' COLLATE NOCASE,[room_image_local] text DEFAULT null COLLATE NOCASE,[first_msg_prefix] int not null, [first_msg_code] int not null, constraint pk_rooms primary key(room_code));");
            script.append("create table if not exists [ch_messages] ([msg_prefix] int not null, [msg_code] int not null, [tmp] int not null, [room_code] text not null COLLATE NOCASE,[msg_date] text not null COLLATE NOCASE,[msg_obj] text not null COLLATE NOCASE, [message_image_local] text DEFAULT '' COLLATE NOCASE,[msg_origin] text not null COLLATE NOCASE,[delivered]  int not null DEFAULT 0,[delivered_date] text COLLATE NOCASE,[read]  int not null DEFAULT 0,[read_date] text COLLATE NOCASE,[msg_pk] text COLLATE NOCASE,[user_code]  int not null ,[user_nick] text not null COLLATE NOCASE,[all_delivered] int not null DEFAULT 0 ,[all_read] int not null DEFAULT 0 ,[status_update] int not null DEFAULT 0 ,constraint pk_ch_messages primary key(msg_prefix,tmp));");
            script.append("create table if not exists [ch_files] ([file_code] text not null DEFAULT '' COLLATE NOCASE, [file_path] text not null DEFAULT '' COLLATE NOCASE,[file_path_new] text collate nocase, [file_status] text not null DEFAULT '' COLLATE NOCASE, [file_date] text not null DEFAULT '' COLLATE NOCASE, primary key(file_code));");
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
            String res = e.toString();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder script = new StringBuilder();
        //
        script.append("drop table if exists [ch_rooms];");
        script.append("drop table if exists [ch_messages];");
        script.append("drop table if exists [ch_files];");
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

