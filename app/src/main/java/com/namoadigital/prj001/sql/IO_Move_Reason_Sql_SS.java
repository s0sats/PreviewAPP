package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.IO_Move_ReasonDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Reason_Sql_SS implements Specification {

    private long customer_code;

    public IO_Move_Reason_Sql_SS(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  f.customer_code , " +
                        "  f.reason_code "+ SearchableSpinner.CODE +", " +
                        "  f.reason_id  " + SearchableSpinner.ID +", " +
                        "  f.reason_desc "+ SearchableSpinner.DESCRIPTION +
                        " FROM\n" +
                        IO_Move_ReasonDao.TABLE +"  f\n" +
                        " WHERE customer_code=" + customer_code
                )
                .toString();
    }
}
