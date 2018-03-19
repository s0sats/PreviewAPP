package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 15/03/2018.
 * <p>
 * Seleciona todos os Ap sem ap_who ou com ap_who != do meu usr
 * e status != de done e cancelled
 */

public class GE_Custom_Form_Ap_Sql_011 implements Specification {

    private long customer_code;
    private String user_code;

    public GE_Custom_Form_Ap_Sql_011(long customer_code, String user_code) {
        this.customer_code = customer_code;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   a.*    \n" +
                        " FROM \n" +
                        GE_Custom_Form_ApDao.TABLE + " a\n" +
                        " WHERE\n" +
                        "   a.customer_code = '"+customer_code+"'\n" +
                        "   and (a.ap_who is null or a.ap_who <> '"+user_code+"')\n" +
                        "   and a.ap_status not in ('"+ Constant.SYS_STATUS_DONE+"','"+ Constant.SYS_STATUS_CANCELLED+"')")
                .append(";")
                .toString();
    }
}
