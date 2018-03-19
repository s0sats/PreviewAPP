package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 26/02/2018.
 * <p>
 * Seleciona todas as ap pendente de envio par ao server.
 */

public class GE_Custom_Form_Ap_Sql_006 implements Specification {

    public static final String RETURN_SQL_OBJ = "RETURN_SQL_OBJ";
    public static final String RETURN_SQL_HM_AUX = "RETURN_SQL_HM_AUX";

    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);

    public GE_Custom_Form_Ap_Sql_006(long customer_code,String sql_type_return) {
        this.customer_code = customer_code;
        //
        if(sql_type_return.equals(RETURN_SQL_OBJ)){
            HmAuxFields = "";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n " +
                        "   a.*\n  "  +
                        " FROM\n   " +
                        GE_Custom_Form_ApDao.TABLE + " a\n" +
                        " WHERE\n" +
                        "    a.customer_code = '"+customer_code+"'\n" +
                        //Alinha deaixo de ser  = 1, só esta <> 1
                        //para testes - VOLTAR AO CORRETO APOS TESTES
                        "    and a.upload_required = 1 \n")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
