package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 26/02/2018.
 * <p>
 * Selecion todas as infos de um ap.
 */

public class GE_Custom_Form_Ap_Sql_005 implements Specification {

    public static final String RETURN_SQL_OBJ = "RETURN_SQL_OBJ";
    public static final String RETURN_SQL_HM_AUX = "RETURN_SQL_HM_AUX";

    private String customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String custom_form_data;
    private String ap_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);

    public GE_Custom_Form_Ap_Sql_005(String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String ap_code, String sql_type_return) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
        this.ap_code = ap_code;
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
                        "    and a.custom_form_type = '"+custom_form_type+"'\n" +
                        "    and a.custom_form_code = '"+custom_form_code+"'\n" +
                        "    and a.custom_form_version = '"+custom_form_version+"'\n" +
                        "    and a.custom_form_data = '"+custom_form_data+"'\n" +
                        "    and a.ap_code = '"+ap_code+"'\n")
                .append(";")
                //.append(HmAuxFields)
                .toString();
    }
}
