package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 23/02/17.
 */

public class WS_Cleaning_Sql_003 implements Specification {

    private String s_date;

    public WS_Cleaning_Sql_003(String s_date) {
        this.s_date = s_date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("select * FROM sm_sos s where Date(s.log_date) <")
                .append("Date('")
                .append(s_date)
                .append("') \n" +
                        "   AND ( s." + SM_SODao.STATUS + "" +
                        "    = '" + Constant.SO_STATUS_CANCELLED + "' " +
                        "   OR  s." + SM_SODao.STATUS + "" +
                        "    = '" + Constant.SO_STATUS_DONE + "' ")
                .append(")\n")
                .append(";")
                .toString();
    }
}
