package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Product_BrandDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_Color_Sql_SS implements Specification {

    private String customer_code;
    private String product_code;
    private String brand_code;

    public MD_Brand_Color_Sql_SS(String customer_code, String product_code, String brand_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.brand_code = brand_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                                "   c.color_code "+ SearchableSpinner.ID +", " +
                                "   c.color_id , " +
                                "   c.color_desc "+ SearchableSpinner.DESCRIPTION +
                                " FROM " +
                                MD_Brand_ColorDao.TABLE +" c ,\n" +
                                MD_Product_BrandDao.TABLE +" pb \n" +
                                " WHERE " +
                                "   c.customer_code = pb.customer_code \n" +
                                "   and c.brand_code = pb.brand_code" +
                                "                                       " +
                                "   and c.customer_code = '"+ customer_code +"'" +
                                "   and pb.product_code = '"+ product_code +"'" +
                                "                                       " +
                                "   and c.brand_code = '"+ brand_code +"'" +
                                " ORDER BY " +
                                "      c.color_id,c.color_desc;")
                //.append(SearchableSpinner.ID + "#color_id#"+SearchableSpinner.DESCRIPTION )
                .toString();
    }
}
