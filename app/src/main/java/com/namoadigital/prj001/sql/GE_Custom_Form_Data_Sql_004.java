package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 20/10/2017.
 */

/**
 *  Seleciona forms em andamento e um determinado produto serail
 */

public class GE_Custom_Form_Data_Sql_004 implements Specification {

    private String customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String product_code;
    private String serial_id;

    public GE_Custom_Form_Data_Sql_004(String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String product_code, String serial_id) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.product_code = product_code;
        this.serial_id = serial_id;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   d.* \n" +
                        " FROM\n" +
                        GE_Custom_Form_DataDao.TABLE +" d \n" +
                        " WHERE\n" +
                        "   d.customer_code = '"+customer_code+"'\n" +
                        "   AND d.custom_form_type = '"+custom_form_type+"'\n" +
                        "   AND d.custom_form_code = '"+custom_form_code+"'\n" +
                        "   AND d.custom_form_version = '"+custom_form_version+"'\n" +
                        "   AND d.product_code = '"+product_code+"'\n" +
                        "   AND d.serial_id = '"+serial_id+"'\n" +
                        "   AND d.custom_form_status = '"+ Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"';")
                .toString();
    }
}
