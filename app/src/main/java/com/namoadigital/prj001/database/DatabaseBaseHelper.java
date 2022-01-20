package com.namoadigital.prj001.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public abstract class DatabaseBaseHelper extends SQLiteOpenHelper {

    protected String mDBName;
    protected int mDBVersion;

    protected int mOpenCounter;
    protected SQLiteDatabase db;

    private boolean mIgnoreCounter = false;

    public boolean ismIgnoreCounter() {
        return mIgnoreCounter;
    }

    public void setmIgnoreCounter(boolean mIgnoreCounter) {
        this.mIgnoreCounter = mIgnoreCounter;
    }

    //TODO Esse metodo praticamento não faz mais nada, remover usa chamada?
    public String getDBHelperName() {
        try {
            if (mDBName == null || mDBName.isEmpty()) {
                return "";
            }

            //return mDBName.replace(".db3", "_") + String.valueOf(mDBVersion) + ".db3";
            return mDBName;

        } catch (Exception e) {
            return "";
        }
    }

    public boolean compareDBHelperNames(String full_name) {
        try {
            return mDBName.equalsIgnoreCase(full_name);
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (!mIgnoreCounter) {
            mOpenCounter++;
            if (mOpenCounter == 1) {
                db = getWritableDatabase();
            }
        }
        //Log.d("DB_NEW", mDBName.replace("/storage/emulated/0/namoa/","") +"  counter: " + String.valueOf(mOpenCounter));
        return db;
    }

    public synchronized void closeDatabase() {
        if (!mIgnoreCounter) {
            mOpenCounter--;
            if (mOpenCounter == 0) {
                db.close();
                db = null;
            }
        }
        //Log.d("DB_NEW",mDBName.replace("/storage/emulated/0/namoa/","") +"  counter: " +String.valueOf( mOpenCounter));
    }

    public DatabaseBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        mDBName = name;
        mDBVersion = version;

        try {
            if (name.contains("-1")) {
                throw new Exception(ConstantBaseApp.EXCEPTION_DATABASE_NO_CUSTOMER_PREFERENCE);
            }
        } catch (Exception e) {
            if (e.toString().contains(ConstantBaseApp.EXCEPTION_DATABASE_NO_CUSTOMER_PREFERENCE)) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
    }
}


