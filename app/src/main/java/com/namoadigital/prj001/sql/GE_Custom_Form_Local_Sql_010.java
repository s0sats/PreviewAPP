package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 3/24/17.
 *
 * Query chamada pela act005 e que deleta os form_local sem form data.
 *
 * LUCHE - 17/02/2020
 *
 * Substituido o campo custom_form_data_serv , que identificava agendamento, pelos campos da pk do
 * novo agendamento(md_schedule_exec)
 *
 */

public class GE_Custom_Form_Local_Sql_010 implements Specification {


    public GE_Custom_Form_Local_Sql_010() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" delete\n" +
                        " \n" +
                        " from ge_custom_forms_local\n" +
                        " \n" +
                        " where customer_code  || '|' || custom_form_type  || '|' || custom_form_code  || '|' || custom_form_version || '|' || custom_form_data  not in (\n" +
                        " \n" +
                        " select customer_code  || '|' || custom_form_type  || '|' || custom_form_code  || '|' || custom_form_version || '|' || custom_form_data  id from ge_custom_form_datas\n" +
                        " \n" +
                        " ) \n" +
                        " AND schedule_prefix is null \n" +
                        " AND schedule_code is null \n" +
                        " AND schedule_exec is null \n"
                )
                .toString();
    }
}
