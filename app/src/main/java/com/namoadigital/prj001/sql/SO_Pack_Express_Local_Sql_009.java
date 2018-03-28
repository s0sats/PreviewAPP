package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 03/08/2017.
 * <p>
 * Seleciona todos as so_express pendentes
 */

public class SO_Pack_Express_Local_Sql_009 implements Specification {
    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SO_Pack_Express_LocalDao.columns);

    public SO_Pack_Express_Local_Sql_009(long customer_code, String sql_type_return) {
        this.customer_code = customer_code;
        //
        if(sql_type_return.equals(Constant.RETURN_SQL_OBJ)){
            HmAuxFields = "";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
//                        "   "+SO_Pack_Express_LocalDao.CUSTOMER_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SITE_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.OPERATION_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.PRODUCT_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.EXPRESS_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.EXPRESS_TMP+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SERIAL_ID+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.PARTNER_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SO_PREFIX+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SO_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SO_ID+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SO_DESC+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SO_STATUS+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.CONTRACT_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.CONTRACT_DESC+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.PRIORITY_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.PRIORITY_DESC+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SITE_ID+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SITE_DESC+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.OPERATION_ID+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.OPERATION_DESC+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.PRODUCT_ID+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.PRODUCT_DESC+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SERIAL_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SEGMENT_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SEGMENT_ID+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.SEGMENT_DESC+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.RET_CODE+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.RET_MSG+",\n" +
//                        "   "+SO_Pack_Express_LocalDao.STATUS+",\n" +
                        "   s.*, \n"+
                        "   '"+Constant.PARAM_KEY_TYPE_SO_EXPRESS+"' "+Constant.PARAM_KEY_TYPE+"\n," +
                        "   '' "+ SM_SODao.CLIENT_TYPE+",\n" +
                        "   '' "+ SM_SODao.DEADLINE+"\n" +
                        " FROM\n" +
                        SO_Pack_Express_LocalDao.TABLE +" s\n" +
                        " WHERE \n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and (s.ret_code is null or s.ret_code = '"+ Constant.SYS_STATUS_ERROR+"') \n" +
                        " ORDER BY \n" +
                        "       s.express_tmp asc\n")
                .append(
                        ";" +HmAuxFields+"#"
                            +Constant.PARAM_KEY_TYPE+"#"
                            +SM_SODao.CLIENT_TYPE+"#"
                            +SM_SODao.DEADLINE
                )
                .toString();
    }
}
