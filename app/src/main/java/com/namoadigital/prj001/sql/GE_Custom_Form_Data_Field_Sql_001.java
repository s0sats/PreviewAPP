package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 13/02/2017.
 */

public class GE_Custom_Form_Data_Field_Sql_001 implements Specification {

    private long s_customer_code;

    public GE_Custom_Form_Data_Field_Sql_001(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        "  SELECT " +
                        "   df.* " +
                        "  FROM " +
                        GE_Custom_Form_Data_FieldDao.TABLE +" df," +
                        GE_Custom_Form_DataDao.TABLE +" f " +
                        " " +
                        "  WHERE " +
                        "       df.customer_code = f.customer_code " +
                        "       and df.custom_form_type = f.custom_form_type  " +
                        "       and df.custom_form_code = f.custom_form_code " +
                        "       and df.custom_form_version = f.custom_form_version " +
                        "       and df.custom_form_data = f.custom_form_data " +
                        "        " +
                        "       and f.customer_code = '"+s_customer_code+"' " +
                        "       and f.custom_form_status = '"+ Constant.CUSTOM_FORM_STATUS_FINALIZED+"' " +
                        "       AND f."+GE_Custom_Form_DataDao.TOKEN+" != '' " +
                        "        " +
                        "  ORDER BY " +
                        "   df.customer_code, " +
                        "   df.custom_form_type, " +
                        "   df.custom_form_code, " +
                        "   df.custom_form_version, " +
                        "   df.custom_form_data;"
                )
                .toString();
    }
}
