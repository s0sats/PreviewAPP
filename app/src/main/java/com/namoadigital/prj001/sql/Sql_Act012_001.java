package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 *
 * Query que seleciona a qtd N-Forms pendentes
 */

public class Sql_Act012_001 implements Specification {

    public static final String PENDING_QTY = "pending_qty";
    public static final String TYPE = "type";
    public static final String MODULE = "module";
    //
    private long s_customer_code;
    private HMAux label_translation;

    public Sql_Act012_001(long s_customer_code, HMAux label_translation) {
        this.s_customer_code = s_customer_code;
        this.label_translation = label_translation;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     count(1) "+PENDING_QTY+",\n " +
                        "    '"+label_translation.get(Act012_Main.LABEL_TRANS_CHECKLIST)+"' "+TYPE+" ,\n " +
                        "    '"+Constant.MODULE_CHECKLIST+"' "+MODULE+"\n " +
                        " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+" l\n " +
                        " WHERE\n" +
                        "   l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' " +
                        "   AND l."+GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS+"" +
                        "    in('"+ Constant.CUSTOM_FORM_STATUS_IN_PROCESSING+"'" +
                        //" ,'"+Constant.CUSTOM_FORM_STATUS_FINALIZED+"' " +
                        ");")
                .append(PENDING_QTY+"#"+TYPE+"#"+MODULE)
                .toString();
    }
}
