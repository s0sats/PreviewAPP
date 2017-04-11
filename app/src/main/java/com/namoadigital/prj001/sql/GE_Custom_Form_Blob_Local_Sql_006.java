package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 10/04/2017.
 */

/**
 *
 * QUERY QUE DELETA TODOS OS REGISTROS DE PDF DE UM DETERMINADO FORMULÁRIO.
 *
 */

public class GE_Custom_Form_Blob_Local_Sql_006 implements Specification {

    private String s_customer_code;
    private String s_form_type;
    private String s_form_code;
    private String s_form_version;

    public GE_Custom_Form_Blob_Local_Sql_006(String s_customer_code, String s_form_type, String s_form_code, String s_form_version) {
        this.s_customer_code = s_customer_code;
        this.s_form_type = s_form_type;
        this.s_form_code = s_form_code;
        this.s_form_version = s_form_version;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(
                " DELETE " +
                " FROM " +
                GE_Custom_Form_Blob_LocalDao.TABLE +
                " WHERE " +
                GE_Custom_Form_Blob_LocalDao.CUSTOMER_CODE + " = '" + s_customer_code + "' " +
                "     AND " + GE_Custom_Form_Blob_LocalDao.CUSTOM_FORM_TYPE + " = '" + s_form_type + "' " +
                "     AND " + GE_Custom_Form_Blob_LocalDao.CUSTOM_FORM_CODE + " = '" + s_form_code + "' " +
                "     AND " + GE_Custom_Form_Blob_LocalDao.CUSTOM_FORM_VERSION + " = '" + s_form_version + "' ")
                .toString();
    }
}
