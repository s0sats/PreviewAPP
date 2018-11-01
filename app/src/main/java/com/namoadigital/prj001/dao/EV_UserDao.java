package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.ErrorCfg;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 18/01/17.
 */

public class EV_UserDao extends BaseDao implements DaoN<EV_User> {
    private final Mapper<EV_User, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_User> toUserMapper;

    public static final String TABLE = "ev_users";
    public static final String USER_CODE = "user_code";
    public static final String USER_NICK = "user_nick";
    public static final String EMAIL_P = "email_p";
    public static final String ADMIN = "admin";
    public static final String EXIST_NFC = "exist_nfc";
    public static final String NFC_BLOCKED = "nfc_blocked";

    private String[] columns = {USER_CODE, USER_NICK, EMAIL_P, ADMIN, EXIST_NFC, NFC_BLOCKED};

    public EV_UserDao(Context context) {
        super(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE, Constant.DB_MODE_SINGLE);

        this.toContentValuesMapper = new UserToContentValuesMapper();
        this.toUserMapper = new CursorToUserMapper();
    }

    public EV_UserDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_SINGLE);

        this.toContentValuesMapper = new UserToContentValuesMapper();
        this.toUserMapper = new CursorToUserMapper();
    }


    @Override
    public void addUpdate(EV_User user, ErrorCfg mError) {
        openDB();

        try {
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(user.getUser_code())).append("'");

            long rows = db.update(TABLE, toContentValuesMapper.map(user), sbWhere.toString(), null);

            if (rows == 0) {
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(user));
            }

            mError.clearError();

        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        //
        closeDB();
    }

    @Override
    public void addUpdate(List<EV_User> users, boolean status, ErrorCfg mError) {
        openDB();

        try {
            if (!ismIgnoreCounter()) {
                db.beginTransaction();
            }

            if (status) {
                db.delete(TABLE, null, null);
            }


            StringBuilder sbWhere;

            for (EV_User user : users) {

                sbWhere = new StringBuilder();
                sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(user.getUser_code())).append("'");

                long rows = db.update(TABLE, toContentValuesMapper.map(user), sbWhere.toString(), null);

                if (rows == 0) {
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(user));
                }

                mError.clearError();
            }

            if (!ismIgnoreCounter()) {
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            if (!ismIgnoreCounter()) {
                db.endTransaction();
            }
        }

        closeDB();
    }

    @Override
    public void addUpdate(String sQuery, ErrorCfg mError) {
        openDB();

        try {

            db.execSQL(sQuery);
            mError.clearError();

            // Metodo nao confiavel ja que nao garante  a operacao em sequencia. Outro metodo em paralelo pode invalidar o contador
            // long rows = DatabaseUtils.longForQuery(db, "SELECT changes()", null);

        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
    }

    @Override
    public void remove(String sQuery, ErrorCfg mError) {
        openDB();

        try {

            db.execSQL(sQuery);
            mError.clearError();

            // Metodo nao confiavel ja que nao garante  a operacao em sequencia. Outro metodo em paralelo pode invalidar o contador
            // long rows = DatabaseUtils.longForQuery(db, "SELECT changes()", null);

        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
    }


    @Override
    public EV_User getByString(String s_query, ErrorCfg mError) {
        EV_User user = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                user = toUserMapper.map(cursor);
            }

            mError.clearError();
            cursor.close();
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return user;
    }

    @Override
    public HMAux getByStringHM(String sQuery, ErrorCfg mError) {
        HMAux hmAux = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                hmAux = CursorToHMAuxMapper.mapN(cursor);
            }

            mError.clearError();
            cursor.close();
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return hmAux;
    }


    @Override
    public List<EV_User> query(String sQuery, ErrorCfg mError) {
        List<EV_User> users = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                EV_User uAux = toUserMapper.map(cursor);
                users.add(uAux);
            }

            mError.clearError();
            cursor.close();
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return users;
    }

    @Override
    public List<HMAux> query_HM(String sQuery, ErrorCfg mError) {
        List<HMAux> users = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                users.add(CursorToHMAuxMapper.mapN(cursor));
            }

            mError.clearError();
            cursor.close();
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return users;
    }

    private class UserToContentValuesMapper implements Mapper<EV_User, ContentValues> {
        @Override
        public ContentValues map(EV_User user) {
            ContentValues contentValues = new ContentValues();


            contentValues.put(USER_CODE, user.getUser_code());

            if (user.getUser_nick() != null) {
                contentValues.put(USER_NICK, user.getUser_nick());
            }
            if (user.getEmail_p() != null) {
                contentValues.put(EMAIL_P, user.getEmail_p());
            }
            if (user.getAdmin() > -1) {
                contentValues.put(ADMIN, user.getAdmin());
            }
            if (user.getExist_nfc() > -1) {
                contentValues.put(EXIST_NFC, user.getExist_nfc());
            }
            if (user.getNfc_blocked() > -1) {
                contentValues.put(NFC_BLOCKED, user.getNfc_blocked());
            }

            return contentValues;
        }
    }

    private class CursorToUserMapper implements Mapper<Cursor, EV_User> {
        @Override
        public EV_User map(Cursor cursor) {
            EV_User user = new EV_User();

            user.setUser_code(cursor.getLong(cursor.getColumnIndex(USER_CODE)));
            user.setUser_nick(cursor.getString(cursor.getColumnIndex(USER_NICK)));
            user.setEmail_p(cursor.getString(cursor.getColumnIndex(EMAIL_P)));
            user.setAdmin(cursor.getInt(cursor.getColumnIndex(ADMIN)));
            user.setExist_nfc(cursor.getInt(cursor.getColumnIndex(EXIST_NFC)));
            user.setNfc_blocked(cursor.getInt(cursor.getColumnIndex(NFC_BLOCKED)));

            return user;
        }
    }

}
