package com.namoadigital.prj001.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.namoadigital.prj001.database.DatabaseHelperChat;
import com.namoadigital.prj001.database.DatabaseHelperMulti;
import com.namoadigital.prj001.database.DatabaseHelperSingle;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 18/01/17.
 */

public class BaseDao {

    protected Context context;
    protected SQLiteDatabase db;

    private String mDB_NAME;
    private int mDB_VERSION;
    private String mMode;

    public BaseDao(Context context, String mDB_NAME, int mDB_VERSION, String mMode) {
        this.context = context;

        this.mDB_NAME = mDB_NAME;
        this.mDB_VERSION = mDB_VERSION;
        this.mMode = mMode;
    }

    public void openDB() {
        //DatabaseManager.getInstance();

        try {
            switch (mMode) {
                case "MULTI":
                    /*DatabaseManager.setmDatabaseHelperMulti(DatabaseHelperMulti.getInstance(context, mDB_NAME,
                            mDB_VERSION)
                    );

                    this.db = DatabaseManager.getInstance().openDatabaseMulti();*/
                    DatabaseHelperMulti SQLHelper_var_multi = new DatabaseHelperMulti(context, mDB_NAME,
                            mDB_VERSION);

                    this.db = SQLHelper_var_multi.getWritableDatabase();

                    break;

                case Constant.DB_MODE_CHAT:
                    /*DatabaseManager.setmDatabaseHelperChat(DatabaseHelperChat.getInstance(context, mDB_NAME,
                            mDB_VERSION)
                    );

                    this.db = DatabaseManager.getInstance().openDatabaseChat();*/
                    DatabaseHelperChat SQLHelper_var_chat = new DatabaseHelperChat(context, mDB_NAME,
                            mDB_VERSION);

                    this.db = SQLHelper_var_chat.getWritableDatabase();

                    break;

                default:
                    /*DatabaseManager.setmDatabaseHelperSingle(DatabaseHelperSingle.getInstance(context, mDB_NAME,
                            mDB_VERSION)
                    );

                    this.db = DatabaseManager.getInstance().openDatabaseSingle();*/
                    DatabaseHelperSingle SQLHelper_var_single = new DatabaseHelperSingle(context, mDB_NAME,
                            mDB_VERSION);

                    this.db = SQLHelper_var_single.getWritableDatabase();

                    break;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();
        }
    }

    public void closeDB() {
        /*switch (mMode) {
            case "MULTI":
                DatabaseManager.getInstance().closeDatabaseMulti();
                break;
            case Constant.DB_MODE_CHAT:
                DatabaseManager.getInstance().closeDatabaseChat();
                break;
            default:
                DatabaseManager.getInstance().closeDatabaseSingle();
                break;
        }*/
        if (this.db != null) {
            this.db.close();
        }
    }

}
