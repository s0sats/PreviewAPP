package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 23/02/17.
 */

public class WS_Cleaning_Sql_008 implements Specification {

    private String s_date;

    public WS_Cleaning_Sql_008(String s_date) {
        this.s_date = s_date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" delete from so_pack_expresss_local where Date(log_date) <")
                .append("Date('")
                .append(s_date)
                .append("') \n" +
                        "   AND ( " + SO_Pack_Express_LocalDao.STATUS + "" +
                        "    = '" + Constant.SYS_STATUS_SENT + "' ")
                .append(")\n")
                .append(";")
                .toString();
    }
}
