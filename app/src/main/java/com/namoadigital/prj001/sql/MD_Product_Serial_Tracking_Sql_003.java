package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 12/09/2017.
 */

public class MD_Product_Serial_Tracking_Sql_003 implements Specification {

    private long customer_code;
    private long product_code;
    private long serial_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(MD_Product_Serial_TrackingDao.columns);

    public MD_Product_Serial_Tracking_Sql_003(long customer_code, long product_code, long serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   t.*\n" +
                        " FROM\n" +
                        MD_Product_Serial_TrackingDao.TABLE +" t\n" +
                        " WHERE \n" +
                        "  t.customer_code = '"+customer_code+"'\n" +
                        "  and t.product_code = '"+product_code+"'\n" +
                        "  and t.serial_code = '"+serial_code+"'")
                .append(";")
                //.append(HmAuxFields)
                .toString() ;
    }
}
