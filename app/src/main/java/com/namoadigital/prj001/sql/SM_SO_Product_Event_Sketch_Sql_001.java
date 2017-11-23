package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_Event_SketchDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 31/10/2017.
 *
 * Seleciona os vetores marcados no croqui de um evento
 */

public class SM_SO_Product_Event_Sketch_Sql_001 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int seq_tmp;


    public SM_SO_Product_Event_Sketch_Sql_001(long customer_code, int so_prefix, int so_code, int seq_tmp) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.seq_tmp = seq_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  s.*\n" +
                        " FROM\n" +
                        SM_SO_Product_Event_SketchDao.TABLE +"  s\n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.so_prefix = '"+so_prefix+"'\n" +
                        "   and s.so_code = '"+so_code+"'\n" +
                        "   and s.seq_tmp = '"+seq_tmp+"'\n")
                .toString();
    }
}
