package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 25/05/2017.
 */

public class Sql_Act020_001 implements Specification {

    private long customer_code;
    private long product_code;
    private String serial_id;


    public Sql_Act020_001(long customer_code, long product_code, String serial_id) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_id = serial_id;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append(" SELECT \n" +
                        "       l.* \n" +
                        " FROM \n" +
                            GE_Custom_Form_LocalDao.TABLE +" l \n" +
                        " LEFT JOIN \n"+
                            GE_Custom_Form_DataDao.TABLE +" d \n" +
                        "      ON l.customer_code = d.customer_code\n" +
                        "         and l.custom_form_type = d.custom_form_type\n" +
                        "         and l.custom_form_code = d.custom_form_code\n" +
                        "         and l.custom_form_version = d.custom_form_version\n" +
                        "         and l.custom_form_data = d.custom_form_data" +
                        " WHERE \n" +
                        "    l.customer_code = '"+customer_code+"' \n" +
                        "    and l.custom_product_code = '"+product_code+"' \n" +
                        "    and l.serial_id = '"+serial_id+"'  \n" +
                        "    and l.custom_form_status = '"+Constant.SYS_STATUS_IN_PROCESSING+"' \n" +
                        " ORDER BY \n" +
                        "    d.date_start asc")
                .toString();
    }
}
