package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 */

public class GE_Custom_Form_Local_Sql_013 implements Specification {

    private long dt_start;
    private long dt_end;

    public GE_Custom_Form_Local_Sql_013(long dt_start) {
        this.dt_start = dt_start;
        this.dt_end = dt_start + (60 * 60 * 1000);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   'future' as type,  count (1) as total\n" +
                        " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE + "\n" +
                        " WHERE\n" +
                        "   custom_form_status = 'SCHEDULE' and schedule_date_start_format_ms >= '" +
                        String.valueOf(dt_start) + "' and schedule_date_start_format_ms <= '" +
                        String.valueOf(dt_end) +
                        "'\n" +
                        " " +
                        " union\n" +
                        " " +
                        "  SELECT\n" +
                        "   'late' as type,  count (1) as total\n" +
                        " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE + "\n" +
                        " WHERE\n" +
                        "   custom_form_status = 'SCHEDULE' and schedule_date_start_format_ms < '" +
                        String.valueOf(dt_start) +
                        "'\n" +
                        " ")
                .append(";")
                //.append("type#total")
                .toString();
    }

    /**
     *
     *  select 'FUTURE',  count (1) as total from ge_custom_forms_local where custom_form_status = 'SCHEDULE' and schedule_date_start_format_ms >=  '1492225200000' and schedule_date_start_format_ms <= 1492225200000 + ( 60 * 60 * 1000)

     union

     select 'LATE',  count (1) as total from ge_custom_forms_local where custom_form_status = 'SCHEDULE' and schedule_date_start_format_ms <  '1493050061729';
     *
     *
     */
}
