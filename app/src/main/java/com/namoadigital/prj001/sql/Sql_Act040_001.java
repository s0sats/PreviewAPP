package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 08/1082018.
 * <p>
 * Query da act040 que seleciona os seriais locais usando os parametros de product, serial e tracking
 */

public class Sql_Act040_001 implements Specification {

    private long customer_code;
    private String site_code;
    private String product_code;
    private String serial_id;
    private String tracking;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(MD_Product_SerialDao.columns);

    private String mOption_Site = "";

    public Sql_Act040_001(long customer_code, String site_code, String product_code, String serial_id, String tracking) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.product_code = product_code.trim().length() > 0 ? product_code : "null";
        this.serial_id = serial_id.trim().length() > 0 ? serial_id : "null";
        this.tracking = tracking.trim().length() > 0 ? tracking : "null";
        //
        if (tracking != null && !tracking.isEmpty()) {
            mOption_Site = "     and s.site_code = '" + site_code + "'\n";
        } else {
            mOption_Site = "     \n";
        }
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();
        return
                sb
                        .append(" SELECT\n" +
                                "   DISTINCT " +
                                "       s.*\n" +
                                " FROM\n" +
                                "     " + MD_ProductDao.TABLE + " p\n" +
                                " INNER JOIN\n" +
                                "     " + MD_Product_SerialDao.TABLE + " s on p.customer_code = s.customer_code\n" +
                                "                             and p.product_code = s.product_code\n" +
                                "                             and ( p.site_restriction = '0' \n" +
                                "                                   or (p.site_restriction = '1' AND p.allow_new_serial_cl = '1')\n" +
                                "                                   or (p.site_restriction = '1' AND p.allow_new_serial_cl = '0' AND (s.site_code is null OR s.site_code = '" + site_code + "'))\n" +
                                "                                 )\n" +
                                " LEFT JOIN\n" +
                                "     " + MD_Product_Serial_TrackingDao.TABLE + " t on t.customer_code = s.customer_code\n" +
                                "                                   and t.product_code = s.product_code\n" +
                                "                                   and t.serial_code = s.serial_code        \n" +
                                " WHERE\n" +
                                "     p.customer_code = '" + customer_code + "'\n" +

                                mOption_Site +

                                "     and ( '" + product_code + "' is null or p.product_code = '" + product_code + "')\n" +
                                "     and ( '" + serial_id + "' is null or s.serial_id like '%" + serial_id + "%')\n" +
                                "     and ( '" + tracking + "' is null  or t.tracking = '" + tracking + "')\n" +
                                "     \n" +
                                " ORDER BY\n" +

                                "     CASE\n" +
                                "         WHEN s.site_code is not null and s.site_code = '" + site_code + "'  THEN '1'\n" +
                                "   	  WHEN s.site_code is not null and s.site_code != '" + site_code + "'  THEN '2'\n" +
                                "   	  WHEN s.site_code is null THEN '3'\n" +
                                "     END,\n" +

                                "     p.product_code,\n" +
                                "     s.serial_id,\n" +
                                "     t.tracking\n" +
                                ";"
                        ).toString()
                        .replace("'%null%'", "null").replace("'null'", "null");

    }
}
