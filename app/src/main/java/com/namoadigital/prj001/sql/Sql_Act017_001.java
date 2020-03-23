package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 11/04/2017.
 *
 * Modificado by DANIEL.LUCHE on 06/07/2018.
 *
 * NOVO CONCEITO de atrasados 06/07/2018
 * Agora considera atrasado todos forms com data(dia, mes, ano) menor ou igual a de hoje.
 *
 * Modificado by DANIEL.LUCHE on 16/08/2018.
 * Adicionado campos schedule_comments e require_serial_done no retorno da query.
 *
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 *
 * LUCHE - 13/02/2020
 *
 * Modificado query para usar a nova tabela de agendamento
 */

public class Sql_Act017_001 implements Specification {

    private long s_customer_code;
    private String sqlite_date_format;
    private String selected_date;
    private String serial_id;
    private String filter_only_delay;
    private String site_logged;
    private String deviceGMT = ToolBox.getDeviceGMT(false);
    private String customerGMT;

    public Sql_Act017_001(Context context, long s_customer_code ,String selected_date, String serial_id, boolean late, boolean site_logged) {
        this.s_customer_code = s_customer_code;
        this.sqlite_date_format = ToolBox_Inf.nlsDate2SqliteDate(context);
        this.selected_date = selected_date;
        this.serial_id = serial_id.trim().length() > 0 ?serial_id : null;
        this.filter_only_delay = late ? "filter" : null;
        this.site_logged = site_logged ? ToolBox_Con.getPreference_Site_Code(context) : null;
        this.customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "'"+Constant.MODULE_CHECKLIST +"' "+ Act017_Main.ACT017_MODULE_KEY +" ,\n" +
                        "  s.customer_code,\n" +
                        "  s.custom_form_type,\n" +
                        "  s.custom_form_type_desc,\n" +
                        "  s.custom_form_code,\n" +
                        "  s.custom_form_version,\n" +
                        "  s.custom_form_desc,\n" +
                        "  s.product_code,\n" +
                        "  s.product_desc,\n" +
                        "  s.product_id,\n" +
                        "  d.custom_form_data,\n" +
                        "  s.status,\n" +
                        "  s.site_code,\n" +
                        "  s.site_id,\n" +
                        "  s.site_desc,\n" +
                        "  CASE WHEN LENGTH(s.serial_id) <> 0 \n" +
                        "       THEN s.serial_id\n" +
                        "       ELSE d.serial_id\n" +
                        "  END  serial_id,\n" +
                        "  d.date_start, \n" +
                        "  d.date_end ,\n" +
                        "  s.date_start ||' "+customerGMT+"' "+MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT+",\n"+
                        "  s.date_end ||' "+customerGMT+"' "+MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT+",\n"+
                        "  strftime('%Y-%m-%d',s.date_start ||' "+customerGMT+"','"+customerGMT+"') "+Act017_Main.ACT017_ADAPTER_DATE_REF+",\n"+
                        "  (strftime('%s',s.date_start ||' "+customerGMT+"','"+customerGMT+"') * 1000)  "+Act017_Main.ACT017_ADAPTER_DATE_REF_MS+",\n"+
                        "  s.require_serial,\n"+
                        "  s.allow_new_serial_cl,\n"+
                        "  s.require_serial_done,\n"+
                        "  s.comments\n,"+
                        "  s.schedule_prefix||'.'||s.schedule_code||'.'||s.schedule_exec schedule_pk, \n "+
                        "  s.schedule_prefix,\n" +
                        "  s.schedule_code,\n" +
                        "  s.schedule_desc,\n" +
                        "  s.schedule_exec \n "+
                        " \n" +
                        "  FROM\n" +
                        "   " + MD_Schedule_ExecDao.TABLE+ " s\n" +
                        "   LEFT JOIN " + GE_Custom_Form_DataDao.TABLE+ " d ON \n " +
                        "      s.customer_code = d.customer_code \n " +
                        "      AND s.custom_form_type = d.custom_form_type\n" +
                        "      AND s.custom_form_code = d.custom_form_code\n" +
                        "      AND s.custom_form_version = d.custom_form_version\n" +
                        "      AND s.schedule_prefix = d.schedule_prefix\n" +
                        "      AND s.schedule_code = d.schedule_code\n" +
                        "      AND s.schedule_exec = d.schedule_exec\n" +
                        "  WHERE\n" +
                        "      s."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' \n" +
                        "      AND s.custom_form_type is not null \n" +
                        "      AND s.custom_form_code is not null \n" +
                        "      AND s.custom_form_version is not null \n" +
                        "      AND ('"+selected_date+"' is null or strftime('%Y-%m-%d',s.date_start ||' "+customerGMT+"','"+customerGMT+"') = '"+selected_date+"') \n" +
                        "      AND ('"+serial_id+"' is null or s.serial_id like '%"+serial_id+"%' or d.serial_id like '%"+serial_id+"%' ) \n" +
                        "      AND ('"+site_logged+"' is null or s.site_code = '"+site_logged+"') \n" +
                        "      AND ('"+filter_only_delay+"' is null or ( (strftime('%Y-%m-%d',s.date_start ||' "+customerGMT+"','"+customerGMT+"' ) <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))  and s.status = '"+ Constant.SYS_STATUS_SCHEDULE+"')) \n" +
                        "  ORDER BY\n" +
                        "      strftime('%Y-%m-%d %H:%M',s.date_start,'"+customerGMT+"'), \n" +
                        "      CASE WHEN s.status = '"+Constant.SYS_STATUS_IN_PROCESSING+"' THEN 0\n" +
                        "           WHEN s.status = '"+Constant.SYS_STATUS_FINALIZED+"' THEN 1\n" +
                        "           WHEN s.status = '"+Constant.SYS_STATUS_SCHEDULE+"' THEN 2\n" +
                        "           ELSE 3\n" +
                        "      END \n," +
                        "      (CASE WHEN s.status = '"+Constant.SYS_STATUS_IN_PROCESSING+"' THEN d.date_start\n" +
                        "            ELSE  '31/12/9999 23:59'\n" +
                        "       END) ASC,\n" +
                        "      (CASE WHEN s.status = '"+Constant.SYS_STATUS_FINALIZED+"' THEN d.date_end\n" +
                        "            ELSE  '01/01/1900 00:00'\n" +
                        "       END) DESC , \n" +
                        "      s.custom_form_type, \n" +
                        "      s.product_code, \n" +
                        "      s.serial_id, \n" +
                        "      CASE WHEN d.custom_form_data IS NULL\n" +
                        "           THEN s.schedule_prefix||s.schedule_code||s.schedule_exec\n" +
                        "           ELSE d.custom_form_data\n" +
                        "      END\n" +
                        ";")

                .toString()
                .replace("'null'","null");
    }
}
