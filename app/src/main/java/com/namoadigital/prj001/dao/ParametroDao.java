package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.Parametro;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 18/01/17.
 */

public class ParametroDao extends BaseDao implements Dao<Parametro> {
    private final Mapper<Parametro, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Parametro> toParametroMapper;

    public static final String TABLE = "parametros";
    public static final String PARAMETRO_CODE = "parametro_code";
    public static final String NOME = "nome";
    public static final String DESCRICAO = "descricao";
    public static final String VALOR_DEFAULT = "valor_default";
    public static final String VALOR_CUSTOMIZADO = "valor_customizado";

    private String[] columns = {PARAMETRO_CODE, NOME, DESCRICAO, VALOR_DEFAULT, VALOR_CUSTOMIZADO};

    public ParametroDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_SINGLE);
        //
        this.toContentValuesMapper = new ParametroToContentValuesMapper();
        this.toParametroMapper = new CursorToParametroMapper();
    }


    @Override
    public void addUpdate(Parametro parametro) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(parametro)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(PARAMETRO_CODE).append(" = '").append(String.valueOf(parametro.getParametro_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(parametro), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<Parametro> parametros, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (Parametro parametro : parametros) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(parametro)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(PARAMETRO_CODE).append(" = ").append(String.valueOf(parametro.getParametro_code()));

                    db.update(TABLE, toContentValuesMapper.map(parametro), sbWhere.toString(), null);
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

        closeDB();
    }

    @Override
    public void addUpdate(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
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
            if (db != null) {
                db.close();
            }
        }

        closeDB();
    }

    @Override
    public Parametro getByString(String s_query) {
        Parametro parametro = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                parametro = toParametroMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        closeDB();

        return parametro;
    }

    @Override
    public List<Parametro> query(String s_query) {
        List<Parametro> parametros = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                Parametro pAux = toParametroMapper.map(cursor);
                parametros.add(pAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        closeDB();

        return parametros;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> parametros = new ArrayList<>();

        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                parametros.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        closeDB();

        return parametros;
    }

    private class CursorToParametroMapper implements Mapper<Cursor, Parametro> {
        @Override
        public Parametro map(Cursor cursor) {
            Parametro parametro = new Parametro();

            parametro.setParametro_code(cursor.getLong(cursor.getColumnIndex(PARAMETRO_CODE)));
            parametro.setNome(cursor.getString(cursor.getColumnIndex(NOME)));
            parametro.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
            parametro.setValor_default(cursor.getString(cursor.getColumnIndex(VALOR_DEFAULT)));
            parametro.setValor_customizado(cursor.getString(cursor.getColumnIndex(VALOR_CUSTOMIZADO)));

            return parametro;
        }
    }

    private class ParametroToContentValuesMapper implements Mapper<Parametro, ContentValues> {
        @Override
        public ContentValues map(Parametro parametro) {
            ContentValues contentValues = new ContentValues();

            if (parametro.getParametro_code() > -1) {
                contentValues.put(PARAMETRO_CODE, parametro.getParametro_code());
            }
            if (parametro.getNome() != null) {
                contentValues.put(NOME, parametro.getNome());
            }
            if (parametro.getDescricao() != null) {
                contentValues.put(DESCRICAO, parametro.getDescricao());
            }
            if (parametro.getValor_default() != null) {
                contentValues.put(VALOR_DEFAULT, parametro.getValor_default());
            }
            if (parametro.getValor_customizado() != null) {
                contentValues.put(VALOR_CUSTOMIZADO, parametro.getValor_customizado());
            }

            return contentValues;
        }
    }
}
