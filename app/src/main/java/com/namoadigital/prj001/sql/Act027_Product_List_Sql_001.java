package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_SketchDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 01/11/2017.
 * Retorna lista de eventos da S.O com informações de se te sketch preenchido
 * e qtd de fotos.
 */

public class Act027_Product_List_Sql_001 implements Specification {
    public static final String SKETCH_SELECTED = "sketch_selected";
    public static final String FILE_QTY = "file_qty";

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private String product_filter;
    private String HmAuxFields  = ToolBox_Inf.getColumnsToHmAux(SM_SO_Product_EventDao.columns);

    public Act027_Product_List_Sql_001(long customer_code, int so_prefix, int so_code, String product_filter) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.product_filter = product_filter.trim().equals("") ? "null" : product_filter.trim();
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();
        return
                sb
                .append(" SELECT\n" +
                        "  s.*,\n" +
                        "  (SELECT\n" +
                        "     IFNULL(count(1),0)\n" +
                        "    FROM \n" +
                        "      "+ SM_SO_Product_Event_SketchDao.TABLE+" ss\n" +
                        "    WHERE\n" +
                        "      ss.customer_code = s.customer_code\n" +
                        "      and ss.so_prefix = s.so_prefix\n" +
                        "      and ss.so_code = s.so_code\n" +
                        "      and ss.seq_tmp = s.seq_tmp \n" +
                        "  ) "+SKETCH_SELECTED+" ,\n" +
                        "   (SELECT\n" +
                        "     IFNULL(count(1),0)\n" +
                        "    FROM \n" +
                        "      "+ SM_SO_Product_Event_FileDao.TABLE+" f\n" +
                        "    WHERE\n" +
                        "      f.customer_code = s.customer_code\n" +
                        "      and f.so_prefix = s.so_prefix\n" +
                        "      and f.so_code = s.so_code\n" +
                        "      and f.seq_tmp = s.seq_tmp \n" +
                        "  ) "+FILE_QTY+",\n" +
                        " (CASE WHEN s.status = '"+ Constant.SO_STATUS_INCONSISTENT+"' \n" +
                        "           THEN '999'\n" +
                        "      WHEN s.status = '"+ Constant.SO_STATUS_CANCELLED+"'\n" +
                        "           THEN '998'\n" +
                        "           ELSE '0'             \n" +
                        " END) status_order \n" +
                        " FROM\n" +
                        "  "+ SM_SO_Product_EventDao.TABLE+" s\n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.so_prefix = '"+so_prefix+"'\n" +
                      //  "   and s.so_code = '"+so_code+"'" +
                        "   and ('"+product_filter+"' is null \n " +
                        "          or s.product_id like '%"+product_filter+"%' \n" +
                        "          or s.product_desc like '%"+product_filter+"%' \n" +
                        "        )\n" +
                        " ORDER BY " +
                        "   status_order")
                .append(";")
                .append(HmAuxFields+"#"+SKETCH_SELECTED+"#"+FILE_QTY)
                .toString()
                .replace("'%null%'","null").replace("'null'","null");
    }
}
