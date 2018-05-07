package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 04/05/2017.
 *
 * Seleciona croquis que devem ser baixados.
 *
 */

public class MD_All_Product_Sql_004 implements Specification {
    public static final String PROD_FILE_LOCAL_NAME = "prod_file_local_name";
    public static final String PROD_FILE_LOCAL_PREFIX = "prod_";

    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(MD_All_ProductDao.columns);

    public MD_All_Product_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   p.*,\n" +
                        "   '"+PROD_FILE_LOCAL_PREFIX+"'||p.customer_code||\"_\"||p.product_code||\"_\"||p.sketch_code "+PROD_FILE_LOCAL_NAME+"\n" +
                        " FROM\n" +
                        "      "+ MD_All_ProductDao.TABLE +" p\n" +
                        " WHERE\n" +
                        "   p."+MD_All_ProductDao.CUSTOMER_CODE+" = '"+customer_code+"'\n" +
                        "   and p."+MD_All_ProductDao.SKETCH_URL+" is not null\n" +
                        "   and p."+MD_All_ProductDao.SKETCH_URL_LOCAL+" = ''")
                .append(";")
                .append(HmAuxFields+"#"+PROD_FILE_LOCAL_NAME)
                .toString();

    }
}
