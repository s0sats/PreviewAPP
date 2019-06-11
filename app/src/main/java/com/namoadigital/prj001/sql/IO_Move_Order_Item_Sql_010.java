package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class IO_Move_Order_Item_Sql_010 implements Specification{

    private long customer_code;
    private String product_id;
    private String serial_id;
    private String tracking;
    private String site_code;

    public IO_Move_Order_Item_Sql_010(long customer_code, String site_code, String product_id, String serial_id, String tracking) {
        this.customer_code = customer_code;
        this.product_id = product_id.trim().length() > 0 ? product_id : "null";
        this.serial_id = serial_id.trim().length() > 0 ? serial_id : "null";
        this.tracking = tracking.trim().length() > 0 ? tracking : "null";
        this.site_code = site_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   DISTINCT " +
                        "       s.*,  m.status " + IO_Inbound_ItemDao.STATUS + "\n" +
                        " FROM\n" +
                        "     " + MD_ProductDao.TABLE + " p\n" +
                        " INNER JOIN\n" +
                        "     " + MD_Product_SerialDao.TABLE + " s on p.customer_code = s.customer_code\n" +
                        "                             and p.product_code = s.product_code\n" +
                        "                             and ( p.site_restriction = '0' \n" +
                        "                                   or (p.site_restriction = '1' AND p.allow_new_serial_cl = '1')\n" +
                        "                                   or (p.site_restriction = '1' AND p.allow_new_serial_cl = '0' AND (s.site_code is null OR s.site_code = '" + site_code + "'))\n" +
                        "                                 )\n" +
                        "INNER JOIN\n" +
                        "     io_inbound_item m                      on m.customer_code = s.customer_code\n" +
                        "                                   and m.product_code = s.product_code\n" +
                        "                                   and m.serial_code = s.serial_code      \n" +
                        "                                   and (m.conf_date = '' or m.conf_date is null)      \n" +
                        "                                   and m.status = '" + ConstantBaseApp.SYS_STATUS_PENDING+ "'\n"+
                        " LEFT JOIN\n" +
                        "     " + MD_Product_Serial_TrackingDao.TABLE + " t on t.customer_code = s.customer_code\n" +
                        "                                   and t.product_code = s.product_code\n" +
                        "                                   and t.serial_code = s.serial_code        \n" +
                        " WHERE\n" +
                        "     p.customer_code = '" + customer_code + "'\n" +
                        "     and (s.site_code = '" + site_code + "' or s.site_code is null)\n"+
                        "     and ( '" + product_id + "' is null or p.product_id = '" + product_id + "')\n" +
                        "     and ( '" + serial_id + "' is null or s.serial_id like '%" + serial_id + "%')\n" +
                        "     and ( '" + tracking + "' is null  or t.tracking = '" + tracking + "')\n" +
                        "     \n" +
                        " ORDER BY\n" +

                        "     CASE\n" +
                        "         WHEN s.site_code is not null and s.site_code = '" + site_code + "'  THEN '1'\n" +
                        "   	  WHEN s.site_code is not null and s.site_code != '" + site_code + "'  THEN '2'\n" +
                        "   	  WHEN s.site_code is null THEN '3'\n" +
                        "     END,\n" +

                        "     p.product_id,\n" +
                        "     s.serial_id,\n" +
                        "     t.tracking\n" +
                        ";"
                ).toString()
                .replace("'%null%'", "null").replace("'null'", "null");
    }
}
