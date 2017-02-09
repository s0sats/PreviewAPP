package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 *
 * Query que seleciona a qtd itens pendentes
 *  OBS: atualmente so pega checklista, posteriormente adicionar
 *      UNION com a mesma query so que tabelas dos outros modulos
 *      para ter uma lista de pendencias por "modulo"(OS,TS,etc)
 *
 */

public class Sql_Act012_001 implements Specification {

    public static final String PENDING_QTY = "pending_qty";
    public static final String TYPE = "type";

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     count(1) pending_qty,\n " +
                        "    'checklist' type\n " +
                        " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+" l\n " +
                        " WHERE\n" +
                        "   l."+GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS+" = '"+ GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS_IN_PROCESSING+"';")
                .append(PENDING_QTY+"#"+TYPE)
                .toString();
    }
}
