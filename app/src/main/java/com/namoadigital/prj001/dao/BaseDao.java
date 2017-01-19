package com.namoadigital.prj001.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.namoadigital.prj001.database.DatabaseHelperMulti;
import com.namoadigital.prj001.database.DatabaseHelperSingle;

/**
 * Created by neomatrix on 18/01/17.
 */

public class BaseDao {

    private Context context;
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
        switch (mMode) {
            case "MULTI":
                DatabaseHelperMulti SQLHelper_var_multi = new DatabaseHelperMulti(context, mDB_NAME,
                        mDB_VERSION);

                this.db = SQLHelper_var_multi.getWritableDatabase();

                break;

            default:
                DatabaseHelperSingle SQLHelper_var_single = new DatabaseHelperSingle(context, mDB_NAME,
                        mDB_VERSION);

                this.db = SQLHelper_var_single.getWritableDatabase();

                break;
        }
    }

    public void closeDB() {
        if (this.db != null) {
            this.db.close();
        }
    }


}
