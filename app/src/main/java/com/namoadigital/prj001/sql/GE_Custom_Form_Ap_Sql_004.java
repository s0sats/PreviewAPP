package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 26/02/2018.
 * <p>
 * Selecion todos ap pendente de sincronismo.
 */

public class GE_Custom_Form_Ap_Sql_004 implements Specification {

    private long customer_code;

    public GE_Custom_Form_Ap_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n " +
                        "   a.customer_code,\n" +
                        "   a.custom_form_type,\n" +
                        "   a.custom_form_code,\n" +
                        "   a.custom_form_version,\n" +
                        "   a.custom_form_data,\n" +
                        "   a.ap_code,\n" +
                        "   a.ap_description,\n" +
                        "   a.ap_status,\n" +
                        "   a.ap_comments,\n" +
                        "   a.ap_what,\n" +
                        "   a.ap_when,\n" +
                        "   a.ap_where,\n" +
                        "   a.ap_why,\n" +
                        "   a.ap_who,\n" +
                        "   a.ap_how,\n" +
                        "   a.ap_how_much,\n" +
                        "   a.department_code,\n" +
                        "   a.ap_scn\n" +
                        " FROM\n   " +
                        GE_Custom_Form_ApDao.TABLE + " a\n" +
                        " WHERE \n" +
                        "   a.customer_code = '"+customer_code+"'\n" +
                        "   and a.ap_status not in('"+ Constant.SYS_STATUS_DONE+"','"+ Constant.SYS_STATUS_CANCELLED+"') \n")
                .append(";")
                .append(GE_Custom_Form_ApDao.CUSTOMER_CODE+"#"+
                        GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE+"#"+
                        GE_Custom_Form_ApDao.CUSTOM_FORM_CODE+"#"+
                        GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION+"#"+
                        GE_Custom_Form_ApDao.CUSTOM_FORM_DATA+"#"+
                        GE_Custom_Form_ApDao.AP_CODE+"#"+
                        GE_Custom_Form_ApDao.AP_DESCRIPTION+"#"+
                        GE_Custom_Form_ApDao.AP_STATUS+"#"+
                        GE_Custom_Form_ApDao.AP_COMMENTS+"#"+
                        GE_Custom_Form_ApDao.AP_WHAT+"#"+
                        GE_Custom_Form_ApDao.AP_WHEN+"#"+
                        GE_Custom_Form_ApDao.AP_WHERE+"#"+
                        GE_Custom_Form_ApDao.AP_WHY+"#"+
                        GE_Custom_Form_ApDao.AP_WHO+"#"+
                        GE_Custom_Form_ApDao.AP_HOW+"#"+
                        GE_Custom_Form_ApDao.AP_HOW_MUCH+"#"+
                        GE_Custom_Form_ApDao.DEPARTMENT_CODE+"#"+
                        GE_Custom_Form_ApDao.AP_SCN
                )
                .toString();
    }
}
