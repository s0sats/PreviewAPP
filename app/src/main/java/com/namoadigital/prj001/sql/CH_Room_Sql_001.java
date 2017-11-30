package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_001 implements Specification {

    private Long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);

    public CH_Room_Sql_001(Long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   r.*\n" +
                        " FROM\n" +
                            CH_RoomDao.TABLE +" r\n" +
                        " WHERE\n" +
                        "   r.customer_code = 1\n" +
                        "   or r.customer_code is null\n" +
                        " ORDER BY\n" +
                        "   r.room_desc")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
