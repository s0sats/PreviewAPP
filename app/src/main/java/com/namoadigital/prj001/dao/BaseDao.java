package com.namoadigital.prj001.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.namoadigital.prj001.database.DatabaseHelperChat;
import com.namoadigital.prj001.database.DatabaseHelperMulti;
import com.namoadigital.prj001.database.DatabaseHelperSingle;
import com.namoadigital.prj001.database.DatabaseManager;
import com.namoadigital.prj001.util.ToolBox_Inf;

import static com.namoadigital.prj001.util.ConstantBaseApp.DB_MODE_CHAT;
import static com.namoadigital.prj001.util.ConstantBaseApp.DB_MODE_MULTI;
import static com.namoadigital.prj001.util.ToolBox_Con.getDBHelperName;

/**
 * Created by neomatrix on 18/01/17.
 */

public class BaseDao {

    protected Context context;
    protected SQLiteDatabase db;

    private String mDB_NAME;
    private int mDB_VERSION;
    private String mMode;

    private boolean mIgnoreCounter = false;

    public boolean ismIgnoreCounter() {
        return mIgnoreCounter;
    }

    public void setmIgnoreCounter(boolean mIgnoreCounter) {
        this.mIgnoreCounter = mIgnoreCounter;
    }

    public BaseDao(Context context, String mDB_NAME, int mDB_VERSION, String mMode) {
        this.context = context;

        this.mDB_NAME = mDB_NAME;
        this.mDB_VERSION = mDB_VERSION;
        this.mMode = mMode;
    }

    public void openDB() {
        DatabaseManager.getInstance();

        try {
            switch (mMode) {
                case DB_MODE_MULTI:
                    DatabaseManager.addDatabaseHelper(new DatabaseHelperMulti(context, mDB_NAME, mDB_VERSION));

                    this.db = DatabaseManager.getInstance().openDatabase(getDBHelperName(mDB_NAME, mDB_VERSION), mIgnoreCounter);

                    break;

                case DB_MODE_CHAT:
                    DatabaseManager.addDatabaseHelper(new DatabaseHelperChat(context, mDB_NAME, mDB_VERSION));

                    this.db = DatabaseManager.getInstance().openDatabase(getDBHelperName(mDB_NAME, mDB_VERSION), mIgnoreCounter);


                    break;

                default:
                    DatabaseManager.addDatabaseHelper(new DatabaseHelperSingle(context, mDB_NAME, mDB_VERSION));

                    this.db = DatabaseManager.getInstance().openDatabase(getDBHelperName(mDB_NAME, mDB_VERSION), mIgnoreCounter);


                    break;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();
        }
    }

    public void closeDB() {
        try {
            switch (mMode) {
                case DB_MODE_MULTI:
                    DatabaseManager.getInstance().closeDatabase(getDBHelperName(mDB_NAME, mDB_VERSION), mIgnoreCounter);
                    break;
                case DB_MODE_CHAT:
                    DatabaseManager.getInstance().closeDatabase(getDBHelperName(mDB_NAME, mDB_VERSION), mIgnoreCounter);
                    break;
                default:
                    DatabaseManager.getInstance().closeDatabase(getDBHelperName(mDB_NAME, mDB_VERSION), mIgnoreCounter);
                    break;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();

        }
    }
}
