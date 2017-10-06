package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 12/09/2017.
 */

public class MD_Product_Serial_Tracking_Sql_002 implements Specification {

    private long customer_code;
    private long product_code;
    private long serial_tmp;

    public MD_Product_Serial_Tracking_Sql_002(long customer_code, long product_code, long serial_tmp) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_tmp = serial_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" DELETE\n" +
                        " FROM\n" +
                        MD_Product_Serial_TrackingDao.TABLE +" \n" +
                        " WHERE \n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and product_code = '"+product_code+"'\n" +
                        "  and serial_tmp = '"+serial_tmp+"'")
                .toString() ;
    }
}
