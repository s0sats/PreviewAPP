package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class GE_Custom_Form_Blob_Sql_002 implements Specification {

    public GE_Custom_Form_Blob_Sql_002() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(
                " SELECT " +
                        " * " +
                        ", (\"PDF\" || \"_\" || customer_code || \"_\" || custom_form_type || \"_\" || custom_form_code || \"_\" || custom_form_version || \"_\" || blob_code) as custom_name\n" +
                        " FROM " +
                        GE_Custom_Form_BlobDao.TABLE +
                        " WHERE " +
                        GE_Custom_Form_BlobDao.BLOB_URL_LOCAL + "= '' ")
                .append(";customer_code#custom_form_type#custom_form_code#custom_form_version#blob_code#blob_url#custom_name")
                .toString();
    }
}
