package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 02/03/2018.
 *
 * Query que seleciona a qtd  so_express pendentes
 */

public class Sql_Act012_004 implements Specification {

    public static final String PENDING_QTY = "pending_qty";
    public static final String TYPE = "type";
    public static final String MODULE = "module";
    private long customer_code;
    private HMAux label_translation;

    public Sql_Act012_004(long customer_code, HMAux label_translation) {
        this.customer_code = customer_code;
        this.label_translation = label_translation;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append( "SELECT\n" +
                        "     count(1) "+PENDING_QTY+",\n " +
                        "    '"+label_translation.get(Act012_Main.LABEL_TRANS_OS_EXPRESS)+"' "+TYPE+" ,\n " +
                        "    '"+Constant.MODULE_SO_PACK_EXPRESS+"' "+MODULE+"\n " +
                        " FROM\n" +
                            SO_Pack_Express_LocalDao.TABLE +" s\n" +
                        " WHERE \n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and (s.ret_code is null or s.ret_code = '"+ Constant.SYS_STATUS_ERROR+"') \n" +
                        ";")
                .append(PENDING_QTY+"#"+TYPE+"#"+MODULE)
                .toString();
    }
}
