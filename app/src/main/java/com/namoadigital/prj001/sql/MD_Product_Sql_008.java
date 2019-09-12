package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;


/*
    12/09/2019 BARRIONUEVO
    Query que cadastra os icones dos produtos.
*/
public class MD_Product_Sql_008 implements Specification {
    private long customer_code;
    private String product_code;
    private String product_icon_url_local;

    public MD_Product_Sql_008(long customer_code, String product_code, String product_icon_url_local) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.product_icon_url_local = product_icon_url_local;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ MD_ProductDao.TABLE +" SET\n" +
                        "   product_icon_url_local = '"+product_icon_url_local+"'\n" +
                        " WHERE\n" +
                        "   "+MD_ProductDao.CUSTOMER_CODE+" = '"+customer_code+"'\n" +
                        "   and "+MD_ProductDao.PRODUCT_CODE+" = '"+product_code+"'")
                .toString();

    }
}
