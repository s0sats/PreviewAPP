package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;


/*
    12/09/2019 BARRIONUEVO
    Query que busca icones dos produtos para download.
*/
public class MD_Product_Sql_007 implements Specification {
    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(MD_ProductDao.columns);

    public MD_Product_Sql_007(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        MD_ProductDao.PRODUCT_ICON_URL + ", " +
                        MD_ProductDao.PRODUCT_ICON_NAME + " " +
                        " FROM\n" +
                        "      "+ MD_ProductDao.TABLE +" p\n" +
                        " WHERE\n" +
                        "   p."+MD_ProductDao.CUSTOMER_CODE+" = '"+customer_code+"'\n" +
                        "   and p."+MD_ProductDao.PRODUCT_ICON_URL+" is not null\n" +
                        "   and p."+MD_ProductDao.PRODUCT_ICON_NAME+" is not null")
                .append(";")
                //.append(HmAuxFields+"#"+PROD_FILE_LOCAL_NAME)
                .toString();

    }
}
