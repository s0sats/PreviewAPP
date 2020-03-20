package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Act027_Product_List_Sql_002 implements Specification {

    public static final String SKETCH_SELECTED = "sketch_selected";
    public static final String FILE_QTY = "file_qty";

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public Act027_Product_List_Sql_002(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();
        return sb.append(" SELECT\n" +
                                "  s.*\n" +
                                " FROM\n" +
                                "  "+ SM_SO_Product_EventDao.TABLE+" s\n" +
                                " WHERE\n" +
                                "   s.customer_code = '"+customer_code+"'\n" +
                                "   and s.so_prefix = '"+so_prefix+"'\n" +
                                "   and s.so_code = '"+so_code+"'" +"\n" +
                                "   and s.status = '" + ConstantBaseApp.SYS_STATUS_PENDING + "'")
                        //.append(HmAuxFields+"#"+SKETCH_SELECTED+"#"+FILE_QTY)
                        .toString();
    }
}
