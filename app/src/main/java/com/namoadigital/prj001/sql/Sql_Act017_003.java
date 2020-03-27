package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by DANIEL.LUCHE on 04/07/2018.
 *
 * Query que seleciona total de agendados + form_ap para todos os dias ou
 * um dia especifico.
 *
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 *
 * LUCHE 17/02/2020
 *
 * Modificado query substituindo o campo custom_form_data_serv pelos campso da pk do agendamento(md_schedule_exec)
 * Removido left join aparentemente desnecessario.
 *
 */

public class Sql_Act017_003 implements Specification {
    public static final String TOTAL_QTY = "total_qty";

    private long s_customer_code;
    private String selected_date;
    private String deviceGMT = ToolBox.getDeviceGMT(false);
    private String customerGMT;

    public Sql_Act017_003(Context context, long s_customer_code, String selected_date) {
        this.s_customer_code = s_customer_code;
        this.selected_date = selected_date;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "    SUM(T.QTY) "+TOTAL_QTY+"\n" +
                        " FROM(\n" +
                        "     SELECT\n" +
                        "       count(1) qty\n" +
                        "     FROM\n" +
                        "       "+ MD_Schedule_ExecDao.TABLE+"  s\n" +
                        "     WHERE\n" +
                        "       s.customer_code = '"+s_customer_code+"'\n" +
                        "       AND ( '"+selected_date+"' is null or strftime('%Y-%m-%d',s.date_start,'"+customerGMT+"') = '"+selected_date+"' )\n" +
                        "       \n" +
                        "     UNION  ALL \n" +
                        "        \n" +
                        "     SELECT\n" +
                        "       count(1) qty\n" +
                        "     FROM\n" +
                        "      "+ GE_Custom_Form_ApDao.TABLE+" A\n" +
                        "     WHERE\n" +
                        "       A.customer_code = '"+s_customer_code+"'\n" +
                        "       AND a.ap_when is not null \n" +
                        "       AND ( '"+selected_date+"' is null or strftime('%Y-%m-%d',a.ap_when,'"+deviceGMT+"') = '"+selected_date+"')\n" +
                        " ) T\n;")
                .toString()
                .replace("'null'","null");


    }
}
