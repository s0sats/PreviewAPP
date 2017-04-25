package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 */

public class Sql_Act016_001 implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  t.schedule_date_start "+CalendarView.DT+" ,\n" +
                        "  sum(t.delayed_count) "+CalendarView.DELAYED_COUNT+ ",\n" +
                        "  sum(t.inprocessing_count) "+CalendarView.INPROCESSING_COUNT+ ",\n" +
                        "  sum(t.scheduled_count) "+CalendarView.SCHEDULED_COUNT+ ",\n" +
                        "  sum(t.finalized_count) "+CalendarView.FINALIZED_COUNT+ ",\n" +
                        "  sum(t.sent_count) "+CalendarView.SENT_COUNT+ "\n " +
                        " FROM(\n" +
                        "  SELECT\n" +
                        "      strftime('%Y-%m-%d',l.schedule_date_start_format,'localtime') schedule_date_start,\n" +
                        "      (l.schedule_date_start_format_ms < (strftime('%s', 'now')  * 1000 ) and l.custom_form_status = '"+ Constant.CUSTOM_FORM_STATUS_SCHEDULED+"' ) delayed_count,\n" +
                        "      (l.custom_form_status = '"+ Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"') inprocessing_count,\n" +
                        "      (l.schedule_date_start_format_ms >= (strftime('%s', 'now')  * 1000 ) AND l.custom_form_status = '"+ Constant.CUSTOM_FORM_STATUS_SCHEDULED+"') scheduled_count,    \n" +
                        "      (l.custom_form_status = '"+ Constant.CUSTOM_FORM_STATUS_FINALIZED+"') finalized_count,\n" +
                        "      (l.custom_form_status = '"+ Constant.CUSTOM_FORM_STATUS_SENT+"') sent_count\n" +
                        "     \n" +
                        "  FROM "+ GE_Custom_Form_LocalDao.TABLE+" l\n" +
                        "  \n" +
                        "  WHERE \n" +
                        "        l.customer_code= '1'     \n" +
                        "        AND l.custom_form_data_serv is not null) T\n" +
                        "      \n" +
                        " GROUP BY\n" +
                        "  schedule_date_start;" +
                        CalendarView.DT
                        +"#"+ CalendarView.DELAYED_COUNT
                        +"#"+ CalendarView.INPROCESSING_COUNT
                        +"#"+ CalendarView.SCHEDULED_COUNT
                        +"#"+ CalendarView.FINALIZED_COUNT
                        +"#"+ CalendarView.SENT_COUNT
                        +"#cur_data#cur_mili"
                )
                .toString();

    }
}
