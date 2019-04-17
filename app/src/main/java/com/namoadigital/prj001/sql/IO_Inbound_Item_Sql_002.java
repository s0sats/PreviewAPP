package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 13/04/2019
 *
 * query que seleciona os dados necessarios para exibição dalista de itens da inbound
 * no fragmento de lista de inbound
 *
 */

public class IO_Inbound_Item_Sql_002 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public IO_Inbound_Item_Sql_002(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("   SELECT\n" +
                        "       t.*,\n" +
                        "       i.put_away_process,\n" +
                        "       s.product_id || \" - \" || s.product_desc product_desc,\n" +
                        "       s.serial_id,\n" +
                        "       s.brand_desc,\n" +
                        "       s.model_desc,\n" +
                        "       s.color_desc," +
                        //"       z.zone_id || \" - \" || z.zone_desc "+IO_Inbound_ItemDao.PLANNED_ZONE_ID+",  \n" +
                        "       z.zone_id "+IO_Inbound_ItemDao.PLANNED_ZONE_ID +",  \n" +
                        "       l.local_id "+IO_Inbound_ItemDao.PLANNED_LOCAL_ID+"\n" +
                        "    FROM\n" +
                            IO_InboundDao.TABLE +" i,\n" +
                            IO_Inbound_ItemDao.TABLE +" t\n" +
                        "  LEFT JOIN \n" +
                            MD_Product_SerialDao.TABLE + " s on s.customer_code = t.customer_code\n" +
                        "                             and s.product_code = t.product_code\n" +
                        "                             and s.serial_code = t.serial_code \n " +
                        "   LEFT JOIN\n" +
                            MD_Site_ZoneDao.TABLE +" z on z.customer_code = t.customer_code\n" +
                        "                       and z.site_code = t.site_code\n" +
                        "                       and z.zone_code = t.planned_zone_code \n" +
                        "                       \n" +
                        "   LEFT JOIN\n" +
                            MD_Site_Zone_LocalDao.TABLE +" l on l.customer_code = t.customer_code\n" +
                        "                       and l.site_code = t.site_code\n" +
                        "                       and l.zone_code = t.planned_zone_code\n" +
                        "                       and l.local_code = t.local_code \n" +
                        "   WHERE\n" +
                        "     i.customer_code = t.customer_code\n" +
                        "     and i.inbound_prefix = t.inbound_prefix\n" +
                        "     and i.inbound_code = t.inbound_code\n" +
                        "     \n" +
                        "     and i.customer_code = '"+customer_code+"' \n" +
                        "     and i.inbound_prefix = '"+inbound_prefix+"'\n" +
                        "     and i.inbound_code = '"+inbound_code+"'\n" +
                        "   ORDER BY\n" +
                        "     t.inbound_item\n")
                .toString();
    }
}
