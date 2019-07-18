package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/** L.BARRIONUEVO
  *  QUery responsavel por listar os seriais com status de MOVE de forma offline na funcao
  *  "Verificar Existencia"
  *
  *  LUCHE - 17/07/019
  *  Modificado query para listar seriais pelo parametros enviados e que NÃO POSSUAM VINCULO NENHUM
  *  com as telas de IO, sendo assim, tem apenas opção de MOVE
 */
public class IO_Move_Order_Item_Sql_012 implements Specification {

    private long customer_code;
    private String site_code;
    private String product_id;
    private String serial_id;
    private String tracking;

    private String mOption_Site = "";

    public IO_Move_Order_Item_Sql_012(long customer_code, String site_code, String product_id, String serial_id, String tracking) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.product_id = product_id.trim().length() > 0 ? product_id : "null";
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
        return sb.append(" SELECT\n" +
                        "   DISTINCT " +
                        "       s.*\n" +
                        " FROM\n" +
                        "     " + MD_ProductDao.TABLE + " p\n" +
                        " INNER JOIN\n" +
                        "     " + MD_Product_SerialDao.TABLE + " s on p.customer_code = s.customer_code\n" +
                        "                                             and p.product_code = s.product_code\n" +
                        "                                             and ( p.site_restriction = '0' \n" +
                        "                                                   or (p.site_restriction = '1' AND p.allow_new_serial_cl = '1')\n" +
                        "                                                       or (p.site_restriction = '1' AND p.allow_new_serial_cl = '0' AND (s.site_code is null OR s.site_code = '" + site_code + "'))\n" +
                        "                                                 )\n" +
                        " LEFT JOIN\n" +
                        "     " + MD_Product_Serial_TrackingDao.TABLE + " t on t.customer_code = s.customer_code\n" +
                        "                                   and t.product_code = s.product_code\n" +
                        "                                   and t.serial_code = s.serial_code        \n" +
                        " WHERE\n" +
                        "     p.customer_code = '" + customer_code + "'\n" +

                        mOption_Site +

                        "     and ( '" + product_id + "' is null or p.product_id = '" + product_id + "')\n" +
                        "     and ( '" + serial_id + "' is null or s.serial_id like '%" + serial_id + "%')\n" +
                        "     and ( '" + tracking + "' is null  or t.tracking = '" + tracking + "')\n" +
                        "     and s.product_io_control = 1\n  " +
                        "     and s.site_io_control = 1\n  " +
                        "     and NOT EXISTS ( SELECT 1 \n" +
                        "                      FROM "+ IO_Inbound_ItemDao.TABLE+" i\n" +
                        "                      WHERE \n" +
                        "                           i.customer_code = s.customer_code\n" +
                        "                           and i.product_code = s.product_code\n" +
                        "                           and i.serial_code = s.serial_code\n" +
                        "                           and i.status not in('"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"',\n" +
                        "                                               '"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"',\n" +
                        "                                               '"+ ConstantBaseApp.SYS_STATUS_DONE +"'\n" +
                        "                                               )\n " +
                        "                     )\n" +
                        "     and NOT EXISTS ( SELECT 1 \n" +
                        "                      FROM "+ IO_MoveDao.TABLE+" m\n" +
                        "                      WHERE \n" +
                        "                           m.customer_code = s.customer_code\n" +
                        "                           and m.product_code = s.product_code\n" +
                        "                           and m.serial_code = s.serial_code\n" +
                        "                           and m.status not in('"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"',\n" +
                        "                                               '"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"',\n" +
                        "                                               '"+ ConstantBaseApp.SYS_STATUS_DONE +"'\n" +
                        "                                               )\n " +
                        "                     )\n" +
                        "     and NOT EXISTS ( SELECT 1 \n" +
                        "                      FROM "+ IO_Outbound_ItemDao.TABLE+" o\n" +
                        "                      WHERE \n" +
                        "                           o.customer_code = s.customer_code\n" +
                        "                           and o.product_code = s.product_code\n" +
                        "                           and o.serial_code = s.serial_code\n" +
                        "                           and o.status not in('"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"',\n" +
                        "                                               '"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"',\n" +
                        "                                               '"+ ConstantBaseApp.SYS_STATUS_DONE +"'\n" +
                        "                                               )\n " +
                        "                     )\n" +
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
