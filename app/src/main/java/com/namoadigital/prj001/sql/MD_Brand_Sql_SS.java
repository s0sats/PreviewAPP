package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Product_BrandDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_Sql_SS implements Specification {

    private String customer_code;
    private String product_code;

    public MD_Brand_Sql_SS(String customer_code, String product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                                "   b.brand_code "+ SearchableSpinner.ID +", " +
                                "   b.brand_id , " +
                                "   b.brand_desc "+ SearchableSpinner.DESCRIPTION +
                                " FROM " +
                                MD_BrandDao.TABLE +" b ,\n" +
                                MD_Product_BrandDao.TABLE +" pb \n" +
                                " WHERE " +
                                "   b.customer_code = pb.customer_code \n" +
                                "   and b.brand_code = pb.brand_code" +
                                "                                       " +
                                "   and b.customer_code = '"+ customer_code +"'" +
                                "   and  pb.product_code = '"+ product_code +"'" +
                                " ORDER BY " +
                                "      brand_id,brand_desc;")
                .append(SearchableSpinner.ID + "#brand_id#"+SearchableSpinner.DESCRIPTION )
                .toString();
    }
}
