package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 03/03/2020
 *
 * Query que busca o form_local via dados do agendamentos
 *
 * LUCHE - 17/09/2021
 * Alterado query, adicionando filtro por status diferente de deleted, ja que agora, será possivel
 * "deletar" agendamentos.
 */

public class MD_Schedule_Exec_Sql_006 implements Specification {

    private String  customer_code;
    private String schedule_prefix;
    private String schedule_code;
    private String schedule_exec;

    public MD_Schedule_Exec_Sql_006(String customer_code, String schedule_prefix, String schedule_code, String schedule_exec) {
        this.customer_code = customer_code;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
        this.schedule_exec = schedule_exec;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
            .append(" SELECT\n" +
                    "   l.*\n" +
                    " FROM\n" +
                    "   "+ GE_Custom_Form_LocalDao.TABLE +" l\n" +
                    " WHERE \n" +
                    "   l.customer_code = '"+customer_code+"'\n" +
                    "   and l.schedule_prefix = '"+schedule_prefix+"'\n" +
                    "   and l.schedule_code = '"+schedule_code+"'\n" +
                    "   and l.schedule_exec = '"+schedule_exec+"'\n" +
                    "   and l.custom_form_status <> '"+ ConstantBaseApp.SYS_STATUS_DELETED +"'")
            .toString();
    }
}
