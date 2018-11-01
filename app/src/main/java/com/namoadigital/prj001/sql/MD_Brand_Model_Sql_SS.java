package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_Product_BrandDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_Model_Sql_SS implements Specification {

    private String customer_code;
    private String product_code;
    private String brand_code;

    public MD_Brand_Model_Sql_SS(String customer_code, String product_code, String brand_code) {
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
                                "   m.model_code "+ SearchableSpinner.ID +", " +
                                "   m.model_id , " +
                                "   m.model_desc "+ SearchableSpinner.DESCRIPTION +
                                " FROM " +
                                MD_Brand_ModelDao.TABLE +" m ,\n" +
                                MD_Product_BrandDao.TABLE +" pb \n" +
                                " WHERE " +
                                "   m.customer_code = pb.customer_code \n" +
                                "   and m.brand_code = pb.brand_code" +
                                "                                       " +
                                "   and m.customer_code = '"+ customer_code +"'" +
                                "   and pb.product_code = '"+ product_code +"'" +
                                "                                       " +
                                "   and m.brand_code = '"+ brand_code +"'" +
                                " ORDER BY " +
                                "      m.model_id,m.model_desc;")
                //.append(SearchableSpinner.ID + "#model_id#"+SearchableSpinner.DESCRIPTION )
                .toString();
    }
}
