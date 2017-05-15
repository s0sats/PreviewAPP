package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class Sql_Act011_002 implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;
    private String s_translate_code;
    private String s_form_data;

    public Sql_Act011_002(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_translate_code, String s_form_data) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
        this.s_translate_code = s_translate_code;
        this.s_form_data = s_form_data;
    }

    @Override
    public String toSqlQuery() {

        String opc = s_customer_code + "|" + s_formtype_code + "|" + s_form_code + "|" + s_formversion_code;

        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT *, ")
                .append(s_form_data)
                .append("  CUSTOM_FORM_DATA ")
                .append("  FROM GE_CUSTOM_FORM_FIELDS GE\n" +
                        "INNER JOIN (SELECT RESOURCE_CODE,\n" +
                        "                    TXT_CODE,\n" +
                        "                    TXT_VALUE,\n" +
                        "                    IFNULL(COMMENT, '') COMMENT\n" +
                        "               FROM (SELECT *,\n" +
                        "                            (SELECT TXT_VALUE\n" +
                        "                               FROM EV_MODULE_RES_TXT_TRANSS TS\n" +
                        "                              WHERE TR.MODULE_CODE = TS.MODULE_CODE\n" +
                        "                                AND TR.RESOURCE_CODE = TS.RESOURCE_CODE\n" +
                        "                                   --AND TR.TXT_CODE = TS.TXT_CODE\n" +
                        "                                AND TR.TRANSLATE_CODE = TS.TRANSLATE_CODE\n" +
                        "                                AND TS.TXT_CODE = TR.TXT_CODE || '_COMMENT') COMMENT\n" +
                        "                    \n" +
                        "                       FROM EV_MODULE_RES_TXT_TRANSS AS TR\n" +
                        "                      WHERE TR.RESOURCE_CODE IN\n" +
                        "                            (\n" +
                        "                            \n" +
                        "                             SELECT MR.RESOURCE_CODE\n" +
                        "                               FROM EV_MODULE_RESS AS MR\n" +
                        "                              WHERE MR.RESOURCE_NAME = '")
                .append(opc)
                .append("'\n" +
                        "                                AND MR.MODULE_CODE = 'CUST_FORM'\n" +
                        "                            \n" +
                        "                             )\n" +
                        "                        AND TR.TRANSLATE_CODE = '")
                .append(s_translate_code)
                .append("'\n" +
                        "                        AND TR.MODULE_CODE = 'CUST_FORM'\n" +
                        "                        AND TR.TXT_CODE NOT LIKE '%_COMMENT'\n" +
                        "                        AND TR.TXT_CODE <> 'TITLE') AS RESULTADO)\n" +
                        "    ON GE.CUSTOM_FORM_SEQ = TXT_CODE\n" +
                        "   AND GE.CUSTOMER_CODE || '|' || GE.CUSTOM_FORM_TYPE || '|' ||\n" +
                        "       GE.CUSTOM_FORM_CODE || '|' || GE.CUSTOM_FORM_VERSION = '")
                .append(opc)
                .append("'\n" +
                        "ORDER BY GE.CUSTOM_FORM_ORDER")
                .append(";")
                .append("CUSTOMER_CODE#CUSTOM_FORM_TYPE#CUSTOM_FORM_CODE#CUSTOM_FORM_VERSION#CUSTOM_FORM_DATA#CUSTOM_FORM_DATA_SERV#CUSTOM_FORM_SEQ#CUSTOM_FORM_DATA_TYPE#CUSTOM_FORM_DATA_SIZE#CUSTOM_FORM_DATA_MASK#CUSTOM_FORM_DATA_CONTENT#CUSTOM_FORM_LOCAL_LINK#CUSTOM_FORM_ORDER#PAGE#REQUIRED#AUTOMATIC#COMMENT#TXT_VALUE;")
                .toString();
    }
}
