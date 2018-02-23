package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_User;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/02/2018.
 */

public class MD_UserDao extends BaseDao implements Dao<MD_User> {

    private final Mapper<MD_User, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_User> toMD_UserMapper;

    public static final String TABLE = "md_users";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String USER_CODE = "user_code";
    public static final String USER_NICK = "user_nick";
    public static final String USER_NAME = "user_name";
    public static final String AP = "ap";

    public static String[] columns = {
            CUSTOMER_CODE, USER_CODE, USER_NICK, USER_NAME,AP
    };

    public MD_UserDao(Context context) {
        super(context, Constant.DB_FULL_CUSTOM, Constant.DB_VERSION_CUSTOM, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new MD_UserToContentValuesMapper();
        this.toMD_UserMapper = new CursorToMD_UserMapper();
    }

    @Override
    public void addUpdate(MD_User md_user) {
        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(md_user)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_user.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(md_user.getUser_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_user), sbWhere.toString(), null);
            }
        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_User> md_users, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }
            for (MD_User md_user : md_users) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_user)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_user.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(md_user.getUser_code())).append("'");
                    db.update(TABLE, toContentValuesMapper.map(md_user), sbWhere.toString(), null);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
            db.endTransaction();
        }
        closeDB();
    }

    @Override
    public void addUpdate(String sQuery) {
        openDB();
        try {
            db.execSQL(sQuery);
        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public void remove(String sQuery) {
        openDB();
        try {
            db.execSQL(sQuery);
        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public MD_User getByString(String sQuery) {
        MD_User md_user = null;
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                md_user = toMD_UserMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_user;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        HMAux hmAux = null;
        openDB();
        String s_query_div[] = sQuery.split(";");
        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);
        try {
            Cursor cursor = db.rawQuery(s_query_div[0], null);
            while (cursor.moveToNext()) {
                hmAux = toHMAuxMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        closeDB();
        return hmAux;
    }

    @Override
    public List<MD_User> query(String sQuery) {
        List<MD_User> md_users = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_User uAux = toMD_UserMapper.map(cursor);
                md_users.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();
        return md_users;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_users  = new ArrayList<>();
        openDB();
        String s_query_div[] = sQuery.split(";");
        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);
        try {
            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_users.add(toHMAuxMapper.map(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
            String st = e.toString();
        } finally {
        }
        closeDB();

        return md_users;
    }

    private class CursorToMD_UserMapper implements Mapper<Cursor,MD_User> {
        @Override
        public MD_User map(Cursor cursor) {
            MD_User md_user = new MD_User();
            //
            md_user.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_user.setUser_code(cursor.getInt(cursor.getColumnIndex(USER_CODE)));
            md_user.setUser_name(cursor.getString(cursor.getColumnIndex(USER_NAME)));
            md_user.setUser_nick(cursor.getString(cursor.getColumnIndex(USER_NICK)));
            md_user.setAp(cursor.getInt(cursor.getColumnIndex(AP)));
            //
            return md_user;

        }
    }

    private class MD_UserToContentValuesMapper implements Mapper<MD_User,ContentValues> {
        @Override
        public ContentValues map(MD_User md_user) {
            ContentValues contentValues = new ContentValues();
            //
            if(md_user.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_user.getCustomer_code());
            }
            if(md_user.getUser_code() > -1){
                contentValues.put(USER_CODE,md_user.getUser_code());
            }
            if(md_user.getUser_name() != null){
                contentValues.put(USER_NAME,md_user.getUser_name());
            }
            if(md_user.getUser_nick() != null){
                contentValues.put(USER_NICK,md_user.getUser_nick());
            }
            if(md_user.getAp() > -1){
                contentValues.put(AP,md_user.getAp());
            }
            //
            return contentValues;
        }
    }
}
