package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

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
 *
 * LUCHE - 17/02/2020
 *
 * Modificado query para usar a nova tabela de agedamento (md_schedule_exec)
 *
 */

public class Sql_Act005_003 implements Specification {

    public static final String BADGE_SCHEDULED_QTY = "scheduled_qty";
    private String customer_code;
    private int forward_hour;
    private String deviceGMT = ToolBox.getDeviceGMT(false);
    private String customerGMT;

    public Sql_Act005_003(Context context, String customer_code, int forward_hour) {
        this.customer_code = customer_code;
        this.forward_hour = forward_hour;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) "+ BADGE_SCHEDULED_QTY +" \n" +
                        " FROM\n" +
                        "   " + MD_Schedule_ExecDao.TABLE + " s \n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+customer_code+"' \n" +
                        "   and (strftime('%Y-%m-%d',s.date_start,'"+customerGMT+"' ) <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))  \n"+
                        "   and s.status = '"+ Constant.SYS_STATUS_SCHEDULE+"'\n")
                .append(";")
                //.append(BADGE_SCHEDULED_QTY)
                .toString();
    }
}
