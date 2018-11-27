package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 07/03/2018.
 *
 * Seleciona qtd de ap "atrasados"
 * OBS: Por mais estranho que possa parecer,
 * Nesse caso,como o strftime pegará a data em millisegundos,
 * NÃO DEVEMOS USAR O MODIFICADOR 'localtime'
 *
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

public class Sql_Act005_006 implements Specification {

    public static final String BADGE_SCHEDULED_QTY = "scheduled_qty";
    private String customer_code;
    private int forward_hour;
    private String deviceGMT = ToolBox_Inf.getDeviceGMT(false);

    public Sql_Act005_006(String customer_code, int forward_hour) {
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
                        GE_Custom_Form_ApDao.TABLE +" a\n" +
                        " WHERE\n" +
                        "   a.customer_code = '"+customer_code+"' \n" +
                        "   and a.ap_when is not null \n" +
                        /*"   and (\n" +
                        "           ((strftime('%s', a.ap_when)  * 1000 ) < (strftime('%s', 'now')  * 1000 ))\n" +
                        "           or ((strftime('%s', a.ap_when)  * 1000 ) < (strftime('%s', 'now','+"+forward_hour+" hour')  * 1000 ))\n" +
                        "       )\n" +*/
                        "        and (strftime('%Y-%m-%d',a.ap_when ,'"+deviceGMT+"' ) <= strftime('%Y-%m-%d','now','"+deviceGMT+"')) \n"+
                        "   and a.ap_status not in('"+ Constant.SYS_STATUS_DONE+"','"+ Constant.SYS_STATUS_CANCELLED+"') \n")
                .append(";")
                //.append(BADGE_SCHEDULED_QTY)
                .toString();
    }
}
