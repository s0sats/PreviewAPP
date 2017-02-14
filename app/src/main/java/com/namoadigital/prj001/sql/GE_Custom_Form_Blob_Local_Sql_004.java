package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 14/02/2017.
 */

public class GE_Custom_Form_Blob_Local_Sql_004 implements Specification {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;

    public GE_Custom_Form_Blob_Local_Sql_004(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return sb
                .append(" SELECT " +
                        "   bl.* " +
                        " FROM " +
                        GE_Custom_Form_Blob_LocalDao.TABLE + "  bl " +
                        " WHERE " +
                        "   bl.customer_code = '"+customer_code+"' " +
                        "   and bl.custom_form_type = '"+custom_form_type+"' " +
                        "   and bl.custom_form_code = '"+custom_form_code+"' " +
                        "   and bl.custom_form_version = '"+custom_form_code+"' " +
                        "    " +
                        "   and bl.blob_url_local = ''" +
                        " " +
                        " UNION " +
                        " " +
                        " SELECT " +
                        "  bl.* " +
                        " FROM " +
                        GE_Custom_Form_BlobDao.TABLE + "  bl " +
                        " WHERE " +
                        "   bl.customer_code = '"+customer_code+"' " +
                        "   and bl.custom_form_type = '"+custom_form_type+"' " +
                        "   and bl.custom_form_code = '"+custom_form_code+"' " +
                        "   and bl.custom_form_version = '"+custom_form_code+"' " +
                        "    " +
                        "   and bl.blob_url_local = ''")
                .toString();
    }
}
