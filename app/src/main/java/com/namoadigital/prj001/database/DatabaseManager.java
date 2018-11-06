package com.namoadigital.prj001.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager {

    private static DatabaseManager instance;

    private static ArrayList<DatabaseBaseHelper> mDBHelpers;

    public static synchronized void initializeInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
            //
            mDBHelpers = new ArrayList<>();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            initializeInstance();
        }

        return instance;
    }

    public static void addDatabaseHelper(DatabaseBaseHelper mDatabaseBaseHelper) {
        boolean mHelperExists = false;

        for (int i = 0; i < mDBHelpers.size(); i++) {
            if (mDBHelpers.get(i).compareDBHelperNames(mDatabaseBaseHelper.getDBHelperName())) {
                mHelperExists = true;
                //
                break;
            }
        }

        if (!mHelperExists) {
            mDBHelpers.add(mDatabaseBaseHelper);
        }
    }

    public synchronized SQLiteDatabase openDatabase(String fullName, boolean mIgnoreCounter) {

        Log.d("DB_NEW","Qtd de DB: " + mDBHelpers.size());
        //
        for (int i = 0; i < mDBHelpers.size(); i++) {
            if (mDBHelpers.get(i).compareDBHelperNames(fullName)) {
                mDBHelpers.get(i).setmIgnoreCounter(mIgnoreCounter);

                return mDBHelpers.get(i).openDatabase();
            }
        }

        return null;
    }

    public synchronized void closeDatabase(String fullName, boolean mIgnoreCounter) {
        Log.d("DB_NEW","Qtd de DB: " + mDBHelpers.size());
        //
        for (int i = 0; i < mDBHelpers.size(); i++) {
            if (mDBHelpers.get(i).compareDBHelperNames(fullName)) {
                mDBHelpers.get(i).setmIgnoreCounter(mIgnoreCounter);
                mDBHelpers.get(i).closeDatabase();
            }
        }
    }
}
