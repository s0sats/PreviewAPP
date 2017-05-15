package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 14/02/2017.
 */

public class GE_Custom_Form_Field_Local_Sql_003 implements Specification {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;

    //
    public GE_Custom_Form_Field_Local_Sql_003(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                                "    fl.customer_code,  " +
                                "    fl.custom_form_type,  " +
                                "    fl.custom_form_code,  " +
                                "    fl.custom_form_version,  " +
                                "    fl.custom_form_seq,    " +
                                "    fl.custom_form_data_type,  " +
                                "    fl.custom_form_data_size,  " +
                                "    fl.custom_form_data_mask,  " +
                                "    fl.custom_form_data_content,  " +
                                "    fl.custom_form_local_link,  " +
                                "    fl.custom_form_order,  " +
                                "    fl.page,  " +
                                "    fl.required" +
                                " FROM " +
                                GE_Custom_Form_Field_LocalDao.TABLE + " fl " +
                                " WHERE " +
                                "   fl.customer_code = '" + customer_code + "' " +
                                "   and fl.custom_form_type = '" + custom_form_type + "' " +
                                "   and fl.custom_form_code = '" + custom_form_code + "' " +
                                "   and fl.custom_form_version = '" + custom_form_version + "' " +
                                "    " +
                                "   and fl.custom_form_data_type = 'PICTURE' " +
                                "   and fl.custom_form_local_link = ''" +
                                " " +
                                " UNION ALL" +
                                " SELECT " +
                                "    fl.customer_code,  " +
                                "    fl.custom_form_type,  " +
                                "    fl.custom_form_code,  " +
                                "    fl.custom_form_version,  " +
                                "    fl.custom_form_seq,    " +
                                "    fl.custom_form_data_type,  " +
                                "    fl.custom_form_data_size,  " +
                                "    fl.custom_form_data_mask,  " +
                                "    fl.custom_form_data_content,  " +
                                "    fl.custom_form_local_link,  " +
                                "    fl.custom_form_order,  " +
                                "    fl.page,  " +
                                "    fl.required" +
                                " FROM " +
                                GE_Custom_Form_FieldDao.TABLE + " fl " +
                                " WHERE " +
                                "   fl.customer_code = '" + customer_code + "' " +
                                "   and fl.custom_form_type = '" + custom_form_type + "' " +
                                "   and fl.custom_form_code = '" + custom_form_code + "' " +
                                "   and fl.custom_form_version = '" + custom_form_version + "' " +
                                "    " +
                                "   and fl.custom_form_data_type = 'PICTURE' " +
                                "   and fl.custom_form_local_link = ''" +
                                " " +
                                ";customer_code#custom_form_type")
                .toString();
    }
}
