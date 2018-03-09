package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 05/03/2018.
 * <p>
 * Set flags sync_required e upload_required para 0
 * Atualiza SCN e campo last_update
 *
 */

public class GE_Custom_Form_Ap_Sql_009 implements Specification {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private long custom_form_data;
    private int ap_code;
    private int ap_scn;
    private String last_update = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");

    public GE_Custom_Form_Ap_Sql_009(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, long custom_form_data, int ap_code, int ap_scn) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
        this.ap_code = ap_code;
        this.ap_scn = ap_scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ GE_Custom_Form_ApDao.TABLE+" SET\n" +
                        "      ap_scn = '"+ap_scn+"',\n" +
                        "      sync_required = 0,\n" +
                        "      upload_required = 0," +
                        "      last_update = '"+last_update+"' \n" +
                        " WHERE\n" +
                        "       customer_code = '"+customer_code+"'\n" +
                        "       and custom_form_type = '"+custom_form_type+"'\n" +
                        "       and custom_form_code = '"+custom_form_code+"'\n" +
                        "       and custom_form_version = '"+custom_form_version+"'\n" +
                        "       and custom_form_data = '"+custom_form_data+"'\n" +
                        "       and ap_code = '"+ap_code+"'\n")
                .append(";")
                .toString();
    }
}
