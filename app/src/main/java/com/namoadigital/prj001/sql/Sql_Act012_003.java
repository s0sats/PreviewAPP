package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 02/03/2018.
 *
 * Query que seleciona a qtd  FormAp pendentes
 */

public class Sql_Act012_003 implements Specification {

    public static final String PENDING_QTY = "pending_qty";
    public static final String TYPE = "type";
    public static final String MODULE = "module";
    private long s_customer_code;
    private HMAux label_translation;

    public Sql_Act012_003(long s_customer_code, HMAux label_translation) {
        this.s_customer_code = s_customer_code;
        this.label_translation = label_translation;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append( "SELECT\n" +
                        "     count(1) "+PENDING_QTY+",\n " +
                        "    '"+label_translation.get(Act012_Main.LABEL_TRANS_FORM_AP)+"' "+TYPE+" ,\n " +
                        "    '"+Constant.MODULE_FORM_AP+"' "+MODULE+"\n " +
                        " FROM\n" +
                        GE_Custom_Form_ApDao.TABLE + " a\n" +
                        " WHERE\n" +
                        "    a."+GE_Custom_Form_ApDao.CUSTOMER_CODE+" =  '" + s_customer_code + "'\n" +
                        "    AND a."+GE_Custom_Form_ApDao.AP_STATUS+" not in ('"+ Constant.SYS_STATUS_CANCELLED+"','"+ Constant.SYS_STATUS_DONE+"')" +
                        ";")
                //.append(PENDING_QTY+"#"+TYPE+"#"+MODULE)
                .toString();
    }
}
