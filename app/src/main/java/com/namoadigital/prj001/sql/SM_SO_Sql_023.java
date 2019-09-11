package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class SM_SO_Sql_023 implements Specification {

    private long customer_code;
    private String product_code;
    private String serial_id;
    private String filter = " ";
    private String fields = ToolBox_Inf.getColumnsToHmAux(SM_SODao.columns);

    public SM_SO_Sql_023(long customer_code, String product_code, String serial_id) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_id = serial_id;

        if(product_code != null && serial_id != null){
            filter += " AND s.product_code = '"+product_code+"' \n"+
                    " AND s.serial_id = '"+serial_id+"' \n";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*,\n" +
                        MD_BrandDao.BRAND_DESC+" ,\n" +
                        MD_Brand_ModelDao.MODEL_DESC+" ,\n" +
                        MD_Brand_ColorDao.COLOR_DESC+" ,\n" +
                        MD_ProductDao.PRODUCT_ICON_URL_LOCAL +" \n" +
                        " FROM\n" + SM_SODao.TABLE + " S\n" +
                        "  INNER JOIN\n" +
                        MD_Product_SerialDao.TABLE +" ps on ps.customer_code = s.customer_code\n" +
                        "                             and ps.product_code = s.product_code \n" +
                        "                             and ps.serial_code = s.serial_code\n" +
                        "  INNER JOIN\n" +
                        MD_ProductDao.TABLE +"        p on p.customer_code = s.customer_code\n" +
                        "                             and p.product_code = s.product_code \n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        filter +";").toString();
    }
}
