package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.DatabaseHelper;
import com.namoadigital.prj001.database.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class UserDao implements Dao<User> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<User, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, User> toUserMapper;

    public static final String TABLE = "users";
    public static final String USER_CODE = "user_code";
    public static final String USER_NICK = "user_nick";
    public static final String EMAIL_P = "email_p";
    public static final String PASSWORD = "password";
    public static final String NFC_CODE = "nfc_code";
    public static final String DTSYNC = "dtsync";

    private String[] columns = {USER_CODE, USER_NICK, EMAIL_P, PASSWORD, NFC_CODE, DTSYNC};


    public UserDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new UserToContentValuesMapper();
        this.toUserMapper = new CursorToUserMapper();
    }

    @Override
    public void addUpdate(User user) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(user)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(user.getUser_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(user), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<User> users, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (User user : users) {
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

            if (db != null) {
                db.close();
            }
        }
    }



    @Override
    public void addUpdate(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

//    @Override
//    public void remove(long id) {
//        SQLiteDatabase db = null;
//
//        try {
//
//            db = openHelper.getWritableDatabase();
//
//            StringBuilder sbWhere = new StringBuilder();
//            sbWhere.append(USER_CODE).append(" = '").append(id).append("'");
//
//            db.delete(TABLE, sbWhere.toString(), null);
//
//        } catch (Exception e) {
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//    }

    @Override
    public void remove(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

//    @Override
//    public User getById(long id) {
//        User user = null;
//        SQLiteDatabase db = null;
//
//        try {
//
//            db = openHelper.getReadableDatabase();
//            StringBuilder sbWhere = new StringBuilder();
//            sbWhere.append(USER_CODE).append(" = '").append(id).append("'");
//
//            Cursor cursor = db.query(TABLE, columns, sbWhere.toString(), null, null, null, null);
//
//            while (cursor.moveToNext()) {
//                user = toUserMapper.map(cursor);
//
//            }
//
//            cursor.close();
//        } catch (Exception e) {
//
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return user;
//    }

    @Override
    public User getByString(String s_query) {
        User user = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                user = toUserMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return user;
    }

    @Override
    public List<User> query(String s_query) {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                User uAux = toUserMapper.map(cursor);
                users.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return users;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> users = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                users.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return users;
    }

    private class UserToContentValuesMapper implements Mapper<User, ContentValues> {
        @Override
        public ContentValues map(User user) {
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
            if (user.getPassword() != null) {
                contentValues.put(PASSWORD, user.getPassword());
            }
            if (user.getNfc_code() != null) {
                contentValues.put(NFC_CODE, user.getNfc_code());
            }
            if (user.getDtsync() != null) {
                contentValues.put(DTSYNC, user.getDtsync());
            }

            return contentValues;
        }
    }

    private class CursorToUserMapper implements Mapper<Cursor, User> {
        @Override
        public User map(android.database.Cursor cursor) {
            User user = new User();

            user.setUser_code(cursor.getLong(cursor.getColumnIndex(USER_CODE)));
            user.setUser_nick(cursor.getString(cursor.getColumnIndex(USER_NICK)));
            user.setEmail_p(cursor.getString(cursor.getColumnIndex(EMAIL_P)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
            user.setNfc_code(cursor.getString(cursor.getColumnIndex(NFC_CODE)));
            user.setDtsync(cursor.getString(cursor.getColumnIndex(DTSYNC)));

            return user;
        }
    }

}
