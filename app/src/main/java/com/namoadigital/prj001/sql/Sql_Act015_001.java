package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 *
 * LUCHE - 17/02/2020
 *
 * Modificado query substituido o campos custom_form_data_serv pela pk do agedamento (md_schedule_exec)
 *
 * LUCHE - 25/03/2020
 * Modificado query do para retornar tb as informações de fcm e error_msg
 *
 * LUCHE - 15/04/2020
 * Modificado query adicionando join com a tabela GE_Custom_Form_Data_FieldDao.TABLE e group by pela
 * pk do form, para que fosse possivel criar campo que identifica se existe ou não NC no form e
 * que fosse possivel filtrar apenas forms com NC.
 * Add no construtor param que indica se filtro on ou off.
 * Add prorpriedade nonConformityFilter que recebe o filtro ,HAVING caso filtro ativo.
 */

public class Sql_Act015_001 implements Specification {

    private static final String CONST_NONCONFORMITY_LIKE_SEARCH = "%\",\"AP\":1,\"%";
    public static final String HAS_NONCONFORMITY_FIELD = "hasNonConformity";
    private long s_customer_code;
    private String sqlite_date_format;
    private String statusFilter = "";
    private String nonConformityFilter = "";

    public Sql_Act015_001(long s_customer_code, Context context, boolean isDone, boolean isNotExec, boolean isCancelled, boolean isIgnored, boolean onlyNonConformity) {
        this.s_customer_code = s_customer_code;
        this.sqlite_date_format = ToolBox_Inf.nlsDate2SqliteDate(context);

        if (isDone || isNotExec || isCancelled|| isIgnored) {
            statusFilter = "   and l.custom_form_status in(";
            statusFilter += isDone ? "'" + ConstantBaseApp.SYS_STATUS_DONE + "', " : "";
            statusFilter += isNotExec ? "'" + ConstantBaseApp.SYS_STATUS_NOT_EXECUTED + "', " : "";
            statusFilter += isCancelled ? "'" + ConstantBaseApp.SYS_STATUS_CANCELLED + "', " : "";
            statusFilter += isIgnored ? "'" + ConstantBaseApp.SYS_STATUS_IGNORED + "', " : "";
            statusFilter = statusFilter.substring(0, statusFilter.length() - ", ".length());
            statusFilter += " )\n";
        } else {
            statusFilter = "   and l.custom_form_status in (\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_DONE + "',\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_NOT_EXECUTED + "',\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_CANCELLED + "',\n" +
                    "                           '" + ConstantBaseApp.SYS_STATUS_IGNORED + "'\n" +
                    "                           )\n";
        }
        //Se filtra apenas não conformidade, adiciona having na query
        if (onlyNonConformity) {
            nonConformityFilter =
                "  HAVING\n" +
                "     IFNULL(SUM(CASE WHEN df.value_extra like '"+CONST_NONCONFORMITY_LIKE_SEARCH+"'\n" +
                "                     THEN 1\n" +
                "                     ELSE 0\n" +
                "                END),0) > 0 \n";
        }

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "  l.customer_code,\n" +
                        "  l.custom_form_type,\n" +
                        "  l.custom_form_type_desc,\n" +
                        "  l.custom_form_code,\n" +
                        "  l.custom_form_version,\n" +
                        "  l.custom_form_desc,\n" +
                        "  l.custom_product_code,\n" +
                        "  l.custom_product_desc,\n" +
                        "  l.custom_product_id,\n" +
                        "  l.custom_form_data,\n" +
                        "  l.custom_form_status,\n" +
                        "  d.so_prefix,\n" +
                        "  d.so_code,\n" +
                        "    CASE WHEN LENGTH(l.serial_id) <> 0 \n" +
                        "       THEN L.serial_id\n" +
                        "       ELSE d.serial_id\n" +
                        "  END  serial_id,\n"+
                        "  d.date_start,\n" +
                        "  d.date_end,\n "+
                        "  l.schedule_date_start_format,\n"+
                        "  l.schedule_date_end_format,\n"+
                        "  l.schedule_comments,\n" +
                        "  d.site_code,\n" +
                        "  s.site_id,\n" +
                        "  s.site_desc\n," +
                        "  l.schedule_prefix,\n"+
                        "  l.schedule_code,\n"+
                        "  l.schedule_exec,\n"+
                        "  sc.fcm_new_status,\n"+
                        "  sc.fcm_user_nick,\n"+
                        "  sc.schedule_erro_msg,\n" +
                        "  IFNULL(SUM(CASE WHEN df.value_extra like '"+CONST_NONCONFORMITY_LIKE_SEARCH+"'\n" +
                        "        THEN 1\n" +
                        "        ELSE 0\n" +
                        "   END),0) "+HAS_NONCONFORMITY_FIELD+"\n"+
                        "  FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+ " l,\n" +
                        GE_Custom_Form_DataDao.TABLE+ " d,\n " +
                        GE_Custom_Form_Data_FieldDao.TABLE+ " df\n " +
                        "  LEFT JOIN\n" +
                        MD_SiteDao.TABLE +" s ON\n" +
                        "               d.customer_code = s.customer_code\n" +
                        "               AND d.site_code = s.site_code\n    " +
                        "  LEFT JOIN\n" +
                        "       " + MD_Schedule_ExecDao.TABLE+ " sc ON\n " +
                        "      l.schedule_prefix = sc.schedule_prefix\n" +
                        "      AND l.schedule_code = sc.schedule_code\n" +
                        "      AND l.schedule_exec = sc.schedule_exec\n" +
                        "\n" +
                        "  WHERE\n" +
                        "      l.customer_code = d.customer_code\n" +
                        "      AND l.custom_form_type = d.custom_form_type\n" +
                        "      AND l.custom_form_code = d.custom_form_code\n" +
                        "      AND l.custom_form_version = d.custom_form_version\n" +
                        "      AND l.custom_form_data = d.custom_form_data\n" +
                        " \n" +
                        "      AND d.customer_code = df.customer_code\n" +
                        "      AND d.custom_form_type = df.custom_form_type\n" +
                        "      AND d.custom_form_code = df.custom_form_code\n" +
                        "      AND d.custom_form_version = df.custom_form_version\n" +
                        "      AND d.custom_form_data = df.custom_form_data\n" +
                        " \n" +
                        "      AND l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"'\n " +
                                statusFilter +
                        "  GROUP BY\n" +
                        "    d.customer_code,\n" +
                        "    d.custom_form_type,\n" +
                        "    d.custom_form_code,\n" +
                        "    d.custom_form_version,\n" +
                        "    d.custom_form_data\n" +
                        //nonConformityFilter é um HAVING, então sempre deve vir DEPOIS DO GROUP BY
                        nonConformityFilter +
                        "  ORDER BY \n" +
                        "    d.date_end desc,\n" +
                        "    l.custom_form_type,\n " +
                        "    l.custom_product_code,\n " +
                        "    l.serial_id, \n" +
                        "    l.custom_form_data \n" +
                        ";")
                .toString();
    }
}
