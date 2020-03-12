package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/*
    BARRIONUEVO - 19-02-2020
    Query responsavel por listar os formularios finalizados com localizacao pendente.
    (1= para ha pendencia e 0 para nao ha pendencia)
*/

public class GE_Custom_Form_Data_Sql_006 implements Specification {
    private long s_customer_code;

    public GE_Custom_Form_Data_Sql_006(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT  d.* \n" +
                        " FROM\n" +
                        GE_Custom_Form_DataDao.TABLE +" d \n" +
                        " WHERE\n" +
                        "   d.customer_code = '"+s_customer_code+"'\n" +
                        "      AND d."+GE_Custom_Form_DataDao.LOCATION_PENDENCY  + " = 1"+
                        "      AND d."+GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS +
                                                       "  = '" + Constant.SYS_STATUS_FINALIZED+"' ;"
                ).toString();
    }
}
