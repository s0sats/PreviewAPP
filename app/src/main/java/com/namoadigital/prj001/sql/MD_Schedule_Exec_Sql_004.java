package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 11/02/2020
 *
 * UPDATE que reseta sync_process para zero em nos agendamento em PENDING
 *
 * LUCHE - 05/03/2020
 * Modificado query adicionando not exists com tabela de custom_form_local para não resetar os
 * agendamendados que já tiveram seus custom_form_local criados mas ainda estão no status de agendado.(Possivel quando o usr para no meio do processo de informar o serial)
 *
 * LUCHE - 23/03/2020
 * <p></p>
 * Anteriormente foi adicionando alias via "as" na tabela do update, fazendo com que ela desse um  erro
 * "silencioso". Para corrigir, foi necessario colocar o nome_da_tabela.campo, no not exists para que
 * o resultado fosse o esperado.
 */

public class MD_Schedule_Exec_Sql_004 implements Specification {

    private long customer_code;

    public MD_Schedule_Exec_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
                .append(" UPDATE "+MD_Schedule_ExecDao.TABLE+" SET\n" +
                        "      sync_process = 0\n" +
                        " WHERE \n" +
                        "      customer_code = '"+customer_code+"'\n" +
                        "      AND status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"'\n" +
                        "      AND NOT EXISTS (SELECT 1 \n" +
                        "                      FROM ge_custom_forms_local l\n" +
                        "                      WHERE l.customer_code = "+MD_Schedule_ExecDao.TABLE+".customer_code\n" +
                        "                            and l.schedule_prefix = "+MD_Schedule_ExecDao.TABLE+".schedule_prefix\n" +
                        "                            and l.schedule_code = "+MD_Schedule_ExecDao.TABLE+".schedule_code\n" +
                        "                            and l.schedule_exec = "+MD_Schedule_ExecDao.TABLE+".schedule_exec\n" +
                        "      )\n")
                .toString();
    }
}
