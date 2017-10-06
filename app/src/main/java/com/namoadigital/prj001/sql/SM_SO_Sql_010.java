package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 03/08/2017.
 *
 * Atualiza SCN e , dependendo dos parametos, o update_required.
 *
 *
 */

public class SM_SO_Sql_010 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int so_scn;
    private boolean update_required_changes;
    private int update_required;
    private String update_required_val ="";

    public SM_SO_Sql_010(long customer_code, int so_prefix, int so_code, int so_scn, boolean update_required_changes, int update_required) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.so_scn = so_scn;
        this.update_required_changes = update_required_changes;
        this.update_required = update_required;
        //
        if(this.update_required_changes){
            this.update_required_val =   "   update_required = '"+this.update_required +"'\n,";
        }



    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ SM_SODao.TABLE+" set\n" +
                        update_required_val +
                        "   so_scn = '"+so_scn+"'\n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and so_prefix = '"+so_prefix+"'\n" +
                        "  and so_code = '"+so_code+"'")
                .toString();
    }
}
