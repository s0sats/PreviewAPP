package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 18/04/2017.
 *
 * Modificado by DANIEL.LUCHE on 06/07/2018.
 *
 * Modificado o conceito de atrasado.
 * Antes, a data era comparada em milisegundos, assim, um form só era considerado atrasado
 * caso da data hora minutos segundo fosse menor que a de agora + 12 hoas no futuro.
 *
 * NOVO CONCEITO 06/07/2018
 * Agora considera atrasado todos forms com data(dia, mes, ano) menor ou igual a de hoje.
 *
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 */

public class Sql_Act005_003 implements Specification {

    public static final String BADGE_SCHEDULED_QTY = "scheduled_qty";
    private String customer_code;
    private int forward_hour;
    private String deviceGMT = ToolBox.getDeviceGMT(false);

    public Sql_Act005_003(String customer_code, int forward_hour) {
        this.customer_code = customer_code;
        this.forward_hour = forward_hour;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) "+ BADGE_SCHEDULED_QTY +" \n" +
                        " FROM\n" +
                        "   ge_custom_forms_local l\n" +
                        " WHERE\n" +
                        "   l.customer_code = '"+customer_code+"' \n" +
                        "   and l.custom_form_data_serv is not null\n " +
                        /*"   and ( (l.schedule_date_start_format_ms < (strftime('%s', 'now')  * 1000 ))\n" +
                        "          or(l.schedule_date_start_format_ms < (strftime('%s', 'now','+"+forward_hour+" hour')  * 1000 ))\n" +
                        "          )\n" +*/
                        "   and (strftime('%Y-%m-%d',l.schedule_date_start_format ,'"+deviceGMT+"' ) <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))  \n"+
                        "   and l.custom_form_status = '"+ Constant.SYS_STATUS_SCHEDULE+"'\n")
                .append(";")
                //.append(BADGE_SCHEDULED_QTY)
                .toString();
    }
}
