package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 21/06/2017.
 */

public class SM_SO_Sql_016 implements Specification {

    private long customer_code;
    private String product_code;
    private String serial_id;
    private String filter = " ";
    private String fields = ToolBox_Inf.getColumnsToHmAux(SM_SODao.columns);

    public SM_SO_Sql_016(long customer_code, String product_code, String serial_id) {
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
                        "      S.*\n," +
                        "      '"+Constant.PARAM_KEY_TYPE_SO+"' "+Constant.PARAM_KEY_TYPE+",\n"+
                        MD_BrandDao.BRAND_DESC+" ,\n" +
                        MD_Brand_ModelDao.MODEL_DESC+" ,\n" +
                        MD_Brand_ColorDao.COLOR_DESC+" \n" +                        " FROM\n" +
                        SM_SODao.TABLE + " S\n" +
                        "  LEFT JOIN\n" +
                        MD_Product_SerialDao.TABLE +" ps on ps.customer_code = s.customer_code\n" +
                        "                             and ps.product_code = s.product_code \n" +
                        "                             and ps.serial_code = s.serial_code\n" +
                        " LEFT JOIN\n" +
                        MD_BrandDao.TABLE +" b on ps.customer_code = b.customer_code\n" +
                        "                    and ps.brand_code = b.brand_code\n" +
                        " LEFT JOIN\n" +
                        MD_Brand_ModelDao.TABLE +" m on ps.customer_code = m.customer_code\n" +
                        "                       and ps.brand_code = m.brand_code\n" +
                        "                       and ps.model_code = m.model_code\n" +
                        " LEFT JOIN\n" +
                        MD_Brand_ColorDao.TABLE +" c on ps.customer_code = c.customer_code\n" +
                        "                       and ps.brand_code = c.brand_code\n" +
                        "                       and ps.color_code = c.color_code\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                            filter +
                        //"    AND s.status in ('"+ Constant.SYS_STATUS_CANCELLED+"','" + Constant.SYS_STATUS_WAITING_QUALITY +"','"+ Constant.SYS_STATUS_DONE+"');")
                        "    AND s.status in ('"+ Constant.SYS_STATUS_CANCELLED+"','"+ Constant.SYS_STATUS_DONE+"');")
                .append(fields+"#"+
                        Constant.PARAM_KEY_TYPE+"#"+
                        MD_BrandDao.BRAND_DESC+"#"+
                        MD_Brand_ModelDao.MODEL_DESC+"#"+
                        MD_Brand_ColorDao.COLOR_DESC
                )
                .toString();
    }
}
