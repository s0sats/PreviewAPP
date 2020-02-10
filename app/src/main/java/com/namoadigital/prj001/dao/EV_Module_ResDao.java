package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_Module_ResDao extends BaseDao implements Dao<EV_Module_Res> {
    private final Mapper<EV_Module_Res, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Module_Res> toEV_Module_ResMapper;

    public static final String TABLE = "ev_module_ress";
    public static final String MODULE_CODE = "module_code";
    public static final String RESOURCE_CODE = "resource_code";
    public static final String RESOURCE_NAME = "resource_name";
    private String[] columns = {MODULE_CODE, RESOURCE_CODE, RESOURCE_NAME};

    public EV_Module_ResDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new EV_Module_ResToContentValuesMapper();
        this.toEV_Module_ResMapper = new CursorEV_Module_ResMapper();
    }

    @Override
    public void addUpdate(EV_Module_Res module_res) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(module_res)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(MODULE_CODE).append(" = '").append(module_res.getModule_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res.getResource_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(module_res), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<EV_Module_Res> module_ress, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Module_Res module_res : module_ress) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(module_res)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(MODULE_CODE).append(" = '").append(module_res.getModule_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res.getResource_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(module_res), sbWhere.toString(), null);
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
    public EV_Module_Res getByString(String s_query) {
        EV_Module_Res module_res = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                module_res = toEV_Module_ResMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return module_res;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        HMAux hmAux = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                hmAux = CursorToHMAuxMapper.mapN(cursor);
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
    public List<EV_Module_Res> query(String s_query) {
        List<EV_Module_Res> module_ress = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Module_Res uAux = toEV_Module_ResMapper.map(cursor);
                module_ress.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return module_ress;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> module_ress = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);


            while (cursor.moveToNext()) {
                module_ress.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return module_ress;
    }

    /**
     * LUCHE - 07/02/2020
     *
     * Metodo que LIMPA TODOS OS REGISTROS DAS TABELAS:
     *  - EV_Module_Res
     *  - EV_Module_Res_Txt
     *  - EV_Module_Res_Txt_Trans
     *
     * Só deve ser usado no sincronismo geral, quando as traduções serão recontruidas do 0.
     *
     * @return - DaoObj com qtd de itens removidos e se deu ou não erro.
     */
    public DaoObjReturn truncateModuleResTables(){
        openDB();
        //
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //
        try {
            db.beginTransaction();
            //
            addUpdateRet += db.delete(EV_Module_ResDao.TABLE, null,null);
            addUpdateRet += db.delete(EV_Module_Res_TxtDao.TABLE, null,null);
            addUpdateRet += db.delete(EV_Module_Res_Txt_TransDao.TABLE, null,null);
            //
            db.setTransactionSuccessful();
        }catch (SQLiteException e){
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage());
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                getClass().getName(),
                new Exception(
                    e.getMessage() + "\n" + daoObjReturn.getErrorMsg()
                )
            );

        }catch (Exception e){
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true);
            ToolBox_Inf.registerException(getClass().getName(), e);
        }finally {
            db.endTransaction();
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }

        closeDB();

        return daoObjReturn;
    }

    /**
     * LUCHE - 07/02/2020
     *
     * Metodo que apaga todos as traduções de um MODULO espeficico
     *
     * Utilizado quando o WS_Sync é chamado para baixar forms de um produto que ainda não possui forms
     * baixados.
     *
     * @param module_code - Modulo que deve ser apagado. Se null, não apaga nada
     * @return - DaoObj com qtd de itens removidos e se deu ou não erro.
     */
    public DaoObjReturn deleteModuleTrans(@NonNull String module_code) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        daoObjReturn.setAction(DaoObjReturn.DELETE);
        daoObjReturn.setActionReturn(addUpdateRet);
        //Se só tenta o dele se o modulo for diferente de null
        if(module_code != null) {
            openDB();
            String moduleResWhere = EV_Module_ResDao.MODULE_CODE + " = '" + module_code + "'";
            //
            try {
                db.beginTransaction();
                //
                addUpdateRet += db.delete(EV_Module_ResDao.TABLE, moduleResWhere, null);
                addUpdateRet += db.delete(EV_Module_Res_TxtDao.TABLE, moduleResWhere, null);
                addUpdateRet += db.delete(EV_Module_Res_Txt_TransDao.TABLE, moduleResWhere, null);
                //
                db.setTransactionSuccessful();
            } catch (SQLiteException e) {
                //Chama metodo que baseado na exception gera obj de retorno setado como erro
                //e contendo msg de erro tratada.
                daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage());
                //Gera arquivo de exception usando dados da exception e do obj de retorno
                ToolBox_Inf.registerException(
                    getClass().getName(),
                    new Exception(
                        e.getMessage() + "\n" + daoObjReturn.getErrorMsg()
                    )
                );

            } catch (Exception e) {
                //Seta obj de retorno com flag de erro e gera arquivo de exception
                daoObjReturn.setError(true);
                ToolBox_Inf.registerException(getClass().getName(), e);
            } finally {
                db.endTransaction();
                //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
                //ou rowId do ultimo insert.
                daoObjReturn.setActionReturn(addUpdateRet);
            }

            closeDB();
        }
        return daoObjReturn;
    }

    private class CursorEV_Module_ResMapper implements Mapper<Cursor, EV_Module_Res> {
        @Override
        public EV_Module_Res map(Cursor cursor) {
            EV_Module_Res module_res = new EV_Module_Res();

            module_res.setModule_code(cursor.getString(cursor.getColumnIndex(MODULE_CODE)));
            module_res.setResource_code(cursor.getInt(cursor.getColumnIndex(RESOURCE_CODE)));
            module_res.setResource_name(cursor.getString(cursor.getColumnIndex(RESOURCE_NAME)));

            return module_res;
        }
    }

    private class EV_Module_ResToContentValuesMapper implements Mapper<EV_Module_Res, ContentValues> {
        @Override
        public ContentValues map(EV_Module_Res ev_module_res) {
            ContentValues contentValues = new ContentValues();

            if (ev_module_res.getModule_code() != null) {
                contentValues.put(MODULE_CODE, ev_module_res.getModule_code());
            }
            if (ev_module_res.getResource_code() > -1) {
                contentValues.put(RESOURCE_CODE, ev_module_res.getResource_code());
            }
            if (ev_module_res.getResource_name() != null) {
                contentValues.put(RESOURCE_NAME, ev_module_res.getResource_name());
            }

            return contentValues;
        }
    }
}
