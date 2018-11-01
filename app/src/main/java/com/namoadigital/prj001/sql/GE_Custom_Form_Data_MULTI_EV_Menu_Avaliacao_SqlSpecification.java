package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Data_MULTI_EV_Menu_Avaliacao_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_translate_code;

    public GE_Custom_Form_Data_MULTI_EV_Menu_Avaliacao_SqlSpecification(String s_customer_code, String s_translate_code) {
        this.s_customer_code = s_customer_code;
        this.s_translate_code = s_translate_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT D.CUSTOMER_CODE || '|' || D.CUSTOM_FORM_TYPE || '|' || D.CUSTOM_FORM_CODE || '|' || D.CUSTOM_FORM_VERSION  || '|' || D.custom_form_data cf_data, D.date_start cf_date_start, D.custom_form_status cf_status,\n" +
                        "       DTA.TXT_VALUE type_desc,\n" +
                        "       DTB.TXT_VALUE form_desc,\n" +
                        "       D.SERIAL_ID serial,\n" +
                        "       PRO.PRODUCT_ID produto\n" +
                        "FROM GE_CUSTOM_FORM_DATAS D,\n" +
                        "     (SELECT TT.TXT_CODE,\n" +
                        "             TT.TXT_VALUE\n" +
                        "      FROM EV_MODULE_RESS R,\n" +
                        "           EV_MODULE_RES_TXTS T,\n" +
                        "           EV_MODULE_RES_TXT_TRANSS TT\n" +
                        "      WHERE R.MODULE_CODE = T.MODULE_CODE\n" +
                        "            AND R.RESOURCE_CODE = T.RESOURCE_CODE           \n" +
                        "            AND T.MODULE_CODE = TT.MODULE_CODE\n" +
                        "            AND T.RESOURCE_CODE = TT.RESOURCE_CODE\n" +
                        "            AND T.TXT_CODE = TT.TXT_CODE\n" +
                        "           \n" +
                        "            AND R.MODULE_CODE = 'CUST_FORM'\n" +
                        "            AND R.RESOURCE_NAME = 'GE_CUSTOM_FORM_TYPE'\n" +
                        "           \n")
                .append("            AND TT.TRANSLATE_CODE = '").append(s_translate_code).append("') DTA,\n")
                .append("     (SELECT R.RESOURCE_NAME TXT_CODE,\n" +
                        "             TT.TXT_VALUE\n" +
                        "      FROM EV_MODULE_RESS R,\n" +
                        "           EV_MODULE_RES_TXTS T,\n" +
                        "           EV_MODULE_RES_TXT_TRANSS TT\n" +
                        "      WHERE R.MODULE_CODE = T.MODULE_CODE\n" +
                        "            AND R.RESOURCE_CODE = T.RESOURCE_CODE\n" +
                        "           \n" +
                        "            AND T.MODULE_CODE = TT.MODULE_CODE\n" +
                        "            AND T.RESOURCE_CODE = TT.RESOURCE_CODE\n" +
                        "            AND T.TXT_CODE = TT.TXT_CODE\n" +
                        "           \n" +
                        "            AND R.MODULE_CODE = 'CUST_FORM'\n" +
                        "           \n" +
                        "            AND T.TXT_CODE = 'TITLE'\n")
                .append("            AND TT.TRANSLATE_CODE = '").append(s_translate_code).append("') DTB,\n")
                .append("     MD_PRODUCTS PRO\n")
                .append("\t\t\t\n" +
                        "\tWHERE D.CUSTOMER_CODE || '|' || D.CUSTOM_FORM_TYPE = DTA.TXT_CODE\n" +
                        "\tAND D.CUSTOMER_CODE || '|' || D.CUSTOM_FORM_TYPE || '|' || D.CUSTOM_FORM_CODE || '|' || D.CUSTOM_FORM_VERSION = DTB.TXT_CODE\n")
                .append("\tAND D.CUSTOMER_CODE = '").append(s_customer_code).append("'\n")
                .append("\tAND D.CUSTOMER_CODE = PRO.CUSTOMER_CODE\n")
                .append("\tAND D.PRODUCT_CODE = PRO.PRODUCT_CODE\n")
                .append(";")
                //.append("cf_data#cf_date_start#cf_status#type_desc#form_desc#serial#produto")
                .toString();
    }
}
