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
            script.append("create table if not exists [ch_rooms] ([room_code] text not null, [room_type] text not null COLLATE NOCASE, [room_desc] text not null COLLATE NOCASE, [customer_code] int not null , [room_obj] text DEFAULT '' COLLATE NOCASE,[room_image] text DEFAULT '' COLLATE NOCASE,[room_image_local] text DEFAULT '' COLLATE NOCASE, constraint pk_rooms primary key(room_code));");
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

