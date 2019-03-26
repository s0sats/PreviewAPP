package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_Category_PriceDao;
import com.namoadigital.prj001.dao.MD_Product_Category_PriceDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Category_Price_Sql_SS implements Specification {
    private String customer_code;
    private String product_code;

    public MD_Category_Price_Sql_SS(String customer_code, String product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                                "  c.category_price_code " + SearchableSpinner.CODE +", " +
                                "  c.category_price_id  " + SearchableSpinner.ID +", " +
                                "  c.category_price_desc " + SearchableSpinner.DESCRIPTION +
                                " FROM " +
                                MD_Category_PriceDao.TABLE +" c ,\n" +
                                MD_Product_Category_PriceDao.TABLE +" pc \n" +
                                " WHERE " +
                                "   c.customer_code = pc.customer_code \n" +
                                "   and c.category_price_code = pc.category_price_code" +
                                "                                       " +
                                "   and c.customer_code = '"+ customer_code +"'" +
                                "   and pc.product_code = '"+ product_code +"'" +

                                " ORDER BY " +
                                "     c.category_price_id,c.category_price_desc;")
                //.append(SearchableSpinner.ID + "#category_price_id#"+SearchableSpinner.DESCRIPTION )
                .toString();
    }
}
