package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
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
        cDate.add(Calendar.DATE,-11);
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
        this.date_now = dateFormat.format(cDate.getTime());

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE FROM \n" +
                        Sync_ChecklistDao.TABLE +"  \n" +
                        " WHERE \n" +
                        "   customer_code = '"+customer_code+"' \n" +
                        "   and Date(last_update) <= Date('"+date_now+"')\n" +
                        "   and product_code in (SELECT s.product_code\n" +
                        "                        FROM "+Sync_ChecklistDao.TABLE +" s\n" +
                        "                        LEFT JOIN "+ MD_ProductDao.TABLE +" p on p.customer_code = s.customer_code \n" +
                        "                                               and p.product_code = s.product_code \n" +
                        "                        WHERE  \n" +
                        "                        p.flag_offline <> 1\n" +
                        "                        --or p.product_code is null                                            \n" +
                        "                       )")
                .toString();
    }
}
