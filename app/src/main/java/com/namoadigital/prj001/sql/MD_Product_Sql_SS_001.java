package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 20/02/2017.
 */

public class MD_Product_Sql_SS_001 implements Specification {

    private long customer_code;
    private long product_code;

    public MD_Product_Sql_SS_001(long customer_code, long product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    public MD_Product_Sql_SS_001(long customer_code, String product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code == null ? -1 : Long.parseLong(product_code);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "    p.product_code " + SearchableSpinner.ID + ",\n"+
                        "    p.product_id ||' - '|| p.product_desc " + SearchableSpinner.DESCRIPTION + "\n," +
                        "    p.product_code, \n"+
                        "    p.product_id, \n"+
                        "    p.product_desc, \n"+
                        "    CASE WHEN p.product_code = '"+product_code+"'\n" +
                        "         THEN 0 ELSE 1 \n" +
                        "    END prod_order \n"+
                        "  FROM \n" +
                        MD_ProductDao.TABLE +" p \n"+
                        "  WHERE \n" +
                        " p."+MD_ProductDao.CUSTOMER_CODE + " = '"+ customer_code+"' \n" +
                        " AND (p." + MD_ProductDao.REQUIRE_SERIAL + " = '0' OR\n" +
                        "      p."+ MD_ProductDao.PRODUCT_CODE +" = '"+product_code+"')\n" +
                        "  ORDER BY \n" +
                        "     prod_order,\n" +
                        "     p.product_id,\n" +
                        "     p.product_desc \n")
                .append(";")
                .append(SearchableSpinner.ID + "#product_code#product_id#product_desc#"+SearchableSpinner.DESCRIPTION)
                .toString();
    }
}
