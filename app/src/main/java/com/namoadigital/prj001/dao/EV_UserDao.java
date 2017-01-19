package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 18/01/17.
 */

public class EV_UserDao extends BaseDao implements Dao<EV_User> {
    private final Mapper<EV_User, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_User> toUserMapper;

    public static final String TABLE = "ev_users";
    public static final String USER_CODE = "user_code";
    public static final String USER_NICK = "user_nick";
    public static final String EMAIL_P = "email_p";

    private String[] columns = {USER_CODE, USER_NICK, EMAIL_P};

    public EV_UserDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_SINGLE);

        this.toContentValuesMapper = new UserToContentValuesMapper();
        this.toUserMapper = new CursorToUserMapper();
    }

    @Override
    public void addUpdate(EV_User user) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(user)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(user.getUser_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(user), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<EV_User> users, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_User user : users) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(user)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(USER_CODE).append(" = ").append(String.valueOf(user.getUser_code()));

                    db.update(TABLE, toContentValuesMapper.map(user), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }

        closeDB();
    }


    @Override
    public void addUpdate(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void remove(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public EV_User getByString(String s_query) {
        EV_User user = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                user = toUserMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return user;
    }

    @Override
    public List<EV_User> query(String s_query) {
        List<EV_User> users = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_User uAux = toUserMapper.map(cursor);
                users.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return users;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> users = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                users.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return users;
    }

    private class UserToContentValuesMapper implements Mapper<EV_User, ContentValues> {
        @Override
        public ContentValues map(EV_User user) {
            ContentValues contentValues = new ContentValues();

            if (user.getUser_code() > -1) {
                contentValues.put(USER_CODE, user.getUser_code());
            }
            if (user.getUser_nick() != null) {
                contentValues.put(USER_NICK, user.getUser_nick());
            }
            if (user.getEmail_p() != null) {
                contentValues.put(EMAIL_P, user.getEmail_p());
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

            return user;
        }
    }

}
