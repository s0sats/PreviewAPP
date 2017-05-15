package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 12/05/2017.
 */

public class Sql_Act008_001 implements Specification {

    public static final String FORM_OPERATION_PROFILE = "form_operation_profile";

    private long customer_code;
    private long product_code;
    private long operation_code;

    public Sql_Act008_001(long customer_code, long product_code, long operation_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.operation_code = operation_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT\n " +
                        "  count(1) "+ FORM_OPERATION_PROFILE +"\n" +
                        " FROM\n " +
                        GE_Custom_Form_ProductDao.TABLE +" p,\n " +
                        GE_Custom_Form_OperationDao.TABLE +" o\n " +
                        " WHERE\n " +
                        "  p.customer_code = o.customer_code\n" +
                        "  and p.custom_form_type = o.custom_form_type\n" +
                        "  and p.custom_form_code = o.custom_form_code\n" +
                        "  and p.custom_form_version = o.custom_form_version\n" +
                        "  and p.customer_code = '"+customer_code+"' \n" +
                        "  and p.product_code = '"+product_code+"'\n" +
                        "  and o.operation_code = '"+operation_code+"'\n")
                .append(";")
                .append(FORM_OPERATION_PROFILE)
                .toString();
    }
}
