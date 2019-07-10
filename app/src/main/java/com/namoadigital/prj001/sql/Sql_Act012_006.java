package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.util.Constant;

public class Sql_Act012_006 implements Specification {
    public static final String PENDING_QTY = "pending_qty";
    public static final String TYPE = "type";
    public static final String MODULE = "module";
    private long customer_code;
    private HMAux label_translation;

    public Sql_Act012_006(long customer_code, HMAux label_translation) {
        this.customer_code = customer_code;
        this.label_translation = label_translation;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append( "SELECT\n" +
                        "     count(1) "+PENDING_QTY+",\n " +
                        "    '"+label_translation.get(Act012_Main.LABEL_TRANS_IO_MOVE)+"' "+TYPE+" \n " +
                        " FROM\n" +
                        IO_MoveDao.TABLE +" s\n" +
                        " WHERE \n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.status = '"+ Constant.SYS_STATUS_PENDING + '\'' )
                .toString();
    }
}
