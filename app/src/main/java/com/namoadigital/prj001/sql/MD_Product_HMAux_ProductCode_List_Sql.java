package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 24/01/2017.
 *
 * Carrega lista dos codigos dos produtos.
 *
 */

public class MD_Product_HMAux_ProductCode_List_Sql implements Specification {

    private String s_customer_code;

    public MD_Product_HMAux_ProductCode_List_Sql(String s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select product_code  from ")
                .append(MD_ProductDao.TABLE)
                .append(" where  " +
                        MD_ProductDao.CUSTOMER_CODE +" = '" +s_customer_code+"' ")
                .append(" order by ")
                .append(" product_code ")
                .append(";")
                .append("product_code")
                .toString();
    }
}
