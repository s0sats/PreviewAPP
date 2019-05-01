package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Inbound_Item_Sql_010 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private int inbound_item;
    private int update_required;
    private String save_date;


    public IO_Inbound_Item_Sql_010(long customer_code, int inbound_prefix, int inbound_code, int inbound_item, int update_required, String save_date) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.inbound_item = inbound_item;
        this.update_required = update_required;
        this.save_date = save_date;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_Inbound_ItemDao.TABLE + "  SET\n" +
                        "   update_required = '"+update_required+"',\n" +
                        "   save_date = '" +save_date +"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and inbound_code = '"+inbound_code+"'\n" +
                        "   and inbound_item = '"+inbound_item+"'\n" +
                        "; \n"
                )
                .toString();
    }
}
