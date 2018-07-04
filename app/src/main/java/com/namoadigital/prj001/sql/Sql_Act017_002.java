package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 06/03/2018.
 *
 * Query que seleciona todos os AP agendados na data selecionada
 *
 */

public class Sql_Act017_002 implements Specification {

    private long s_customer_code;
    private String sqlite_date_format;
    private String selected_date;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);
    private String serial_id;
    private String filter_only_delay;

    public Sql_Act017_002(Context context, long s_customer_code , String selected_date, String serial_id,boolean late) {
        this.s_customer_code = s_customer_code;
        this.sqlite_date_format = ToolBox_Inf.nlsDate2SqliteDate(context);
        this.selected_date = selected_date;
        this.serial_id = serial_id.trim().length() > 0 ?serial_id : null;
        this.filter_only_delay = late ? "filter" : null;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "'"+Constant.MODULE_FORM_AP +"' "+ Act017_Main.ACT017_MODULE_KEY +" ,\n" +
                        "  a.*\n," +
                        "  strftime('%Y-%m-%d',a.ap_when,'localtime') "+Act017_Main.ACT017_ADAPTER_DATE_REF+",\n" +
                        "  (strftime('%s',a.ap_when,'localtime') * 1000) "+Act017_Main.ACT017_ADAPTER_DATE_REF_MS+"\n" +
                        "  FROM\n" +
                        GE_Custom_Form_ApDao.TABLE+ " a\n" +
                        "  WHERE\n" +
                        "      a."+GE_Custom_Form_ApDao.CUSTOMER_CODE+" = '"+s_customer_code+"' \n" +
                        "      AND a.ap_when is not null \n" +
                        "      AND ('"+selected_date+"' is null or strftime('%Y-%m-%d',a.ap_when,'localtime') = '"+selected_date+"')\n" +
                        "      AND ('"+serial_id+"' is null or a.serial_id like '%"+serial_id+"%') \n" +
                        "      AND ('"+filter_only_delay+"' is null or ((strftime('%s',a.ap_when) * 1000) < (strftime('%s', 'now')  * 1000 ) and a.ap_status not in('"+Constant.SYS_STATUS_DONE+"','"+Constant.SYS_STATUS_CANCELLED+"') )) \n" +
                        "  ORDER BY\n" +
                        "      strftime('%Y-%m-%d %H:%M',a.ap_when,'localtime'), \n" +
                        "      CASE WHEN a.ap_status = '"+Constant.SYS_STATUS_EDIT+"' THEN 0\n" +
                        "           WHEN a.ap_status = '"+Constant.SYS_STATUS_PROCESS+"' THEN 1\n" +
                        "           WHEN a.ap_status = '"+Constant.SYS_STATUS_WAITING_ACTION+"' THEN 2\n" +
                        "           ELSE 3\n" +
                        "      END \n," +
                        "      a.custom_form_type, \n" +
                        "      a.custom_form_code, \n" +
                        "      a.custom_form_version, \n" +
                        "      a.custom_form_data, \n" +
                        "      a.ap_code \n" +
                        ";")
                .append( Act017_Main.ACT017_MODULE_KEY+"#")
                .append( Act017_Main.ACT017_ADAPTER_DATE_REF+"#")
                .append( Act017_Main.ACT017_ADAPTER_DATE_REF_MS+"#")
                .append(HmAuxFields)
                .toString()
                .replace("'null'","null");


    }
}
