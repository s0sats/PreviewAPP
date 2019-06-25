package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class IO_Outbound_Item_Sql_003 implements Specification {

    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public IO_Outbound_Item_Sql_003(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("   SELECT\n" +
                        "       t.*,\n" +
                        "       i.picking_process,\n" +
                        "       s.product_id || \" - \" || s.product_desc product_desc,\n" +
                        "       s.serial_id, \n" +
                        "       s.brand_desc, \n" +
                        "       s.model_desc, \n" +
                        "       s.color_desc," +
                        //"       z.zone_id || \" - \" || z.zone_desc "+IO_Outbound_ItemDao.PLANNED_ZONE_ID+",  \n" +
                        "       z.zone_id "+ IO_OutboundDao.ZONE_ID_PICKING +",  \n" +
                        "       l.local_id "+IO_OutboundDao.LOCAL_ID_PICKING +"\n," +
                        "       z2.zone_desc "+ IO_MoveDao.TO_ZONE_DESC +",  \n" +
                        "       l2.local_id "+IO_MoveDao.TO_LOCAL_ID+" \n" +
                        "    FROM\n" +
                        "               "+ IO_OutboundDao.TABLE +" i,\n" +
                        "               "+ IO_Outbound_ItemDao.TABLE +" t\n" +
                        "  LEFT JOIN \n" +
                        "               "+    MD_Product_SerialDao.TABLE + " s on s.customer_code = t.customer_code\n" +
                        "                             and s.product_code = t.product_code\n" +
                        "                             and s.serial_code = t.serial_code \n " +
                        "   LEFT JOIN\n" +
                        "               "+   MD_Site_ZoneDao.TABLE +" z on z.customer_code = t.customer_code\n" +
                        "                       and z.site_code = i.from_site_code\n" +
                        "                       and z.zone_code = i.zone_code_picking \n" +
                        "                       \n" +
                        "   LEFT JOIN\n" +
                        "               "+    MD_Site_Zone_LocalDao.TABLE +" l on l.customer_code = t.customer_code\n" +
                        "                       and l.site_code = i.from_site_code\n" +
                        "                       and l.zone_code = i.zone_code_picking\n" +
                        "                       and l.local_code = i.local_code_picking \n" +
                        "   LEFT JOIN\n" +
                        "               "+   IO_MoveDao.TABLE +" m on m.customer_code = t.customer_code\n" +
                        "                       and m.outbound_prefix = t.outbound_prefix\n" +
                        "                       and m.outbound_code = t.outbound_code\n" +
                        "                       and m.outbound_item = t.outbound_item \n" +
                        "                       and m.status in('"+ ConstantBaseApp.SYS_STATUS_DONE +"','"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') \n" +
                        "   LEFT JOIN\n" +
                        "               "+MD_Site_ZoneDao.TABLE +" z2 on z2.customer_code = m.customer_code\n" +
                        "                       and z2.site_code = m.site_code\n" +
                        "                       and z2.zone_code = m.to_zone_code \n" +
                        "                       \n" +
                        "   LEFT JOIN\n" +
                        "               "+MD_Site_Zone_LocalDao.TABLE +" l2 on l2.customer_code = m.customer_code\n" +
                        "                       and l2.site_code = m.site_code\n" +
                        "                       and l2.zone_code = m.to_zone_code\n" +
                        "                       and l2.local_code = m.to_local_code \n" +
                        "\n" +
                        "   WHERE\n" +
                        "     i.customer_code = t.customer_code\n" +
                        "     and i.outbound_prefix = t.outbound_prefix\n" +
                        "     and i.outbound_code = t.outbound_code\n" +
                        "     \n" +
                        "     and i.customer_code = '"+customer_code+"' \n" +
                        "     and i.outbound_prefix = '"+outbound_prefix+"'\n" +
                        "     and i.outbound_code = '"+outbound_code+"'\n" +
                        "   ORDER BY\n" +
                        "     t.outbound_item\n")
                .toString();
    }
}
