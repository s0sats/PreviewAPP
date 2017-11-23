package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/10/2017.
 *
 * Atualiza campo de url local do croqui do produto
 *
 */

public class MD_Product_Sql_005 implements Specification {
    private long customer_code;
    private String product_code;
    private String sketch_url_local;

    public MD_Product_Sql_005(long customer_code, String product_code, String sketch_url_local) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.sketch_url_local = sketch_url_local;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ MD_ProductDao.TABLE +" SET\n" +
                        "   sketch_url_local = '"+sketch_url_local+"'\n" +
                        " WHERE\n" +
                        "   "+MD_ProductDao.CUSTOMER_CODE+" = '"+customer_code+"'\n" +
                        "   and "+MD_ProductDao.PRODUCT_CODE+" = '"+product_code+"'")
                .toString();

    }
}
