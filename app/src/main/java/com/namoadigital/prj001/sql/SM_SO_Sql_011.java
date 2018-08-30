package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 21/06/2017.
 */

public class SM_SO_Sql_011 implements Specification {

    private long customer_code;
    private String product_code;
    private String serial_id;
    private String filter = " ";
    private String fields = ToolBox_Inf.getColumnsToHmAux(SM_SODao.columns);

    public SM_SO_Sql_011(long customer_code, String product_code, String serial_id) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_id = serial_id;

        if(product_code != null && serial_id != null){
            filter += " AND s.product_code = '"+product_code+"' \n"+
                      " AND s.serial_id = '"+serial_id+"' \n";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        SM_SODao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                            filter +
                        "    AND s.status not in ('"+ Constant.SYS_STATUS_CANCELLED+"','"+ Constant.SYS_STATUS_DONE+"');")
                //.append(fields)
                .toString();
    }
}
