package com.namoadigital.prj001.database;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private int mOpenCounterChat;
    private int mOpenCounterSingle;
    private int mOpenCounterMulti;

    private static DatabaseManager instance;
    private static DatabaseHelperChat mDatabaseHelperChat;
    private SQLiteDatabase mDatabaseChat;

    private static DatabaseHelperSingle mDatabaseHelperSingle;
    private SQLiteDatabase mDatabaseSingle;

    private static DatabaseHelperMulti mDatabaseHelperMulti;
    private SQLiteDatabase mDatabaseMulti;

    public static synchronized void initializeInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            initializeInstance();

//            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
//                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    // Chat
    public synchronized SQLiteDatabase openDatabaseChat() {
        mOpenCounterChat++;
        if (mOpenCounterChat == 1) {
            // Opening new database
            mDatabaseChat = mDatabaseHelperChat.getWritableDatabase();
        }
        return mDatabaseChat;
    }

    public synchronized void closeDatabaseChat() {
        mOpenCounterChat--;
        if (mOpenCounterChat == 0) {
            // Closing database
            mDatabaseChat.close();
        }
    }

    // Single
    public synchronized SQLiteDatabase openDatabaseSingle() {
        mOpenCounterSingle++;
        if (mOpenCounterSingle == 1) {
            // Opening new database
            mDatabaseSingle = mDatabaseHelperSingle.getWritableDatabase();
        }
        return mDatabaseSingle;
    }

    public synchronized void closeDatabaseSingle() {
        mOpenCounterSingle--;
        if (mOpenCounterSingle == 0) {
            // Closing database
            mDatabaseSingle.close();
        }
    }

    // Single
    public synchronized SQLiteDatabase openDatabaseMulti() {
        mOpenCounterMulti++;
        if (mOpenCounterMulti == 1) {
            // Opening new database
            mDatabaseMulti = mDatabaseHelperMulti.getWritableDatabase();
        }
        return mDatabaseMulti;
    }

    public synchronized void closeDatabaseMulti() {
        mOpenCounterMulti--;
        if (mOpenCounterMulti == 0) {
            // Closing database
            mDatabaseMulti.close();
        }
    }

    // Initializers
    public static void setmDatabaseHelperChat(DatabaseHelperChat mDatabaseHelperChat) {
        DatabaseManager.mDatabaseHelperChat = mDatabaseHelperChat;
    }

    public static void setmDatabaseHelperSingle(DatabaseHelperSingle mDatabaseHelperSingle) {
        DatabaseManager.mDatabaseHelperSingle = mDatabaseHelperSingle;
    }

    public static void setmDatabaseHelperMulti(DatabaseHelperMulti mDatabaseHelperMulti) {
        DatabaseManager.mDatabaseHelperMulti = mDatabaseHelperMulti;
    }
}
