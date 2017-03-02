package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by DANIEL.LUCHE on 17/02/2017.
 */

public class Sync_Checklist_Sql_003 implements Specification {

    private String customer_code;
    private String date_now;

    public Sync_Checklist_Sql_003(String customer_code) {
        this.customer_code = customer_code;

        Calendar cDate =  Calendar.getInstance();
        cDate.add(Calendar.DATE,-31);
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
        this.date_now = dateFormat.format(cDate.getTime());

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE FROM " +
                        "   sync_checklist  " +
                        " WHERE " +
                        "   customer_code = '"+customer_code+"'" +
                        "   and Date(last_update) <= Date('"+date_now+"')")
                .toString();
    }
}
