package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 24/05/2021
 * Criado query que seleciona os form do tema selecionado.
 *
 * LUCHE - 06/07/2021
 *  * Modificado query, add o param de block_spontaneous na query. Para forms avulsos, somente os fomrs
 *  * block_spontaneous = 0 devem ser considerados, para o outros processo, qualquer um
 */

public class Sql_Act010_001 implements Specification {
    public static final String CUSTOM_FORM_PK = "CUSTOM_FORM_PK";

    private long s_customer_code;
    private int s_tag_code;
    private String s_translate_code;
    private String s_product_code;
    private long s_operation_code;
    private String s_site_code;
    private String s_serial_id;
    private Integer blockSpontaneous;

    public Sql_Act010_001(long s_customer_code, int s_tag_code, String s_translate_code, String s_product_code, long s_operation_code, String s_site_code, String s_serial_id, Integer blockSpontaneous) {
        this.s_customer_code = s_customer_code;
        this.s_tag_code = s_tag_code;
        this.s_translate_code = s_translate_code;
        this.s_product_code = s_product_code;
        this.s_operation_code = s_operation_code;
        this.s_site_code = s_site_code;
        this.s_serial_id = s_serial_id.trim().length() != 0 ? s_serial_id.trim()  : "null";
        this.blockSpontaneous = blockSpontaneous;
    }

    @Override
    public String toSqlQuery() {
            StringBuilder sb =  new StringBuilder();

            return  sb.append(
                " SELECT\n" +
                "      cf."+GE_Custom_FormDao.CUSTOMER_CODE+",\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_TYPE+",\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_CODE+",\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_VERSION+",\n" +
                "      cf."+GE_Custom_FormDao.REQUIRE_LOCATION+",\n" +
                "      (SELECT txt_value\n" +
                "       FROM "+ EV_Module_Res_Txt_TransDao.TABLE+" tr,\n" +
                "            "+ EV_Module_Res_TxtDao.TABLE+" ts,\n" +
                "            "+ EV_Module_ResDao.TABLE+" rs\n" +
                "       WHERE \n" +
                "         rs."+EV_Module_ResDao.MODULE_CODE+" = ts."+EV_Module_Res_TxtDao.MODULE_CODE+"\n" +
                "         AND rs."+EV_Module_ResDao.RESOURCE_CODE+" = ts."+EV_Module_Res_TxtDao.RESOURCE_CODE+"\n" +
                "         \n" +
                "         AND ts."+EV_Module_Res_TxtDao.MODULE_CODE+" = tr."+EV_Module_Res_Txt_TransDao.MODULE_CODE+"\n" +
                "         AND ts."+EV_Module_Res_TxtDao.RESOURCE_CODE+" = tr."+EV_Module_Res_Txt_TransDao.RESOURCE_CODE+"\n" +
                "         AND ts."+EV_Module_Res_TxtDao.TXT_CODE+" = tr."+EV_Module_Res_Txt_TransDao.TXT_CODE+"\n" +
                "       \n" +
                "         AND rs."+EV_Module_ResDao.MODULE_CODE+" = 'CUST_FORM'\n" +
                "         AND rs."+EV_Module_ResDao.RESOURCE_NAME+" = cf."+GE_Custom_FormDao.CUSTOMER_CODE+"||'|'||cf."+GE_Custom_FormDao.CUSTOM_FORM_TYPE+"||'|'||cf."+GE_Custom_FormDao.CUSTOM_FORM_CODE+"||'|'||cf."+GE_Custom_FormDao.CUSTOM_FORM_VERSION+" \n" +
                "         AND tr."+EV_Module_Res_Txt_TransDao.TRANSLATE_CODE+" = '" + s_translate_code +"'\n" +
                "         AND tr."+EV_Module_Res_TxtDao.TXT_CODE+" = 'TITLE') "+GE_Custom_FormDao.CUSTOM_FORM_DESC+",\n" +
                "       cf."+GE_Custom_FormDao.CUSTOM_FORM_TYPE+"||'.'||" +
                "       cf."+GE_Custom_FormDao.CUSTOM_FORM_CODE+"||'.'||" +
                "       cf."+GE_Custom_FormDao.CUSTOM_FORM_VERSION+" " + CUSTOM_FORM_PK+"\n" +
                "    FROM\n" +
                "       " +     GE_Custom_FormDao.TABLE +" CF \n" +
                "    LEFT JOIN\n" +
                "       "+     GE_Custom_Form_ProductDao.TABLE +" p on p.customer_code = cf.customer_code\n" +
                "                             and p.custom_form_type = cf.custom_form_type\n" +
                "                             and p.custom_form_code = cf.custom_form_code\n" +
                "                             and p.custom_form_version = cf.custom_form_version  \n" +
                "                             and p.product_code = '"+s_product_code+"'\n" +
                "    LEFT JOIN\n" +
                "       "+     GE_Custom_Form_OperationDao.TABLE +"  o on o.customer_code = cf.customer_code\n" +
                "                             and o.custom_form_type = cf.custom_form_type\n" +
                "                             and o.custom_form_code = cf.custom_form_code\n" +
                "                             and o.custom_form_version = cf.custom_form_version     \n" +
                "                             and o.operation_code = '"+s_operation_code+"' \n "+
                "    LEFT JOIN\n" +
                "       "+     GE_Custom_Form_SiteDao.TABLE +" s on s.customer_code = cf.customer_code\n" +
                "                               and s.custom_form_type = cf.custom_form_type\n" +
                "                               and s.custom_form_code = cf.custom_form_code\n" +
                "                               and s.custom_form_version = cf.custom_form_version \n " +
                "                               and s.site_code = '"+s_site_code+"' \n"+
                "    WHERE\n" +
                "      cf."+GE_Custom_FormDao.CUSTOMER_CODE+" = '" + s_customer_code + "'\n" +
                "      AND cf."+GE_Custom_FormDao.TAG_OPERATIONAL_CODE+" = '" + s_tag_code +"'\n" +
                "      AND (" + blockSpontaneous +" is null OR cf."+GE_Custom_FormDao.BLOCK_SPONTANEOUS+" = '" + blockSpontaneous +"')\n" +
                "      AND (cf.all_product = 1 OR p.product_code = '"+s_product_code+"')\n" +
                "      AND (cf.all_operation = 1 OR o.operation_code = '"+s_operation_code+"') \n" +
                "      AND (cf.all_site = 1 OR s.site_code = '"+s_site_code+"')\n"+
                "      AND ( '"+s_serial_id+"' IS NOT NULL OR cf.require_serial_done = 0)\n"+
                "    \n" +
                "    ORDER BY\n" +
                "      upper(" + GE_Custom_FormDao.CUSTOM_FORM_DESC + ") \n;"
            )
                //GE_Custom_FormDao.CUSTOMER_CODE+"#"+GE_Custom_FormDao.CUSTOM_FORM_TYPE+"#"+GE_Custom_FormDao.CUSTOM_FORM_CODE+"#"+GE_Custom_FormDao.CUSTOM_FORM_VERSION+"#"+GE_Custom_FormDao.CUSTOM_FORM_DESC)
                .toString()
                .replace("'null'","null");

        /*return  sb.append(
                " SELECT\n" +
                "      cf."+GE_Custom_FormDao.CUSTOMER_CODE+",\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_TYPE+",\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_CODE+",\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_VERSION+",\n" +
                "      (SELECT txt_value\n" +
                "       FROM "+ EV_Module_Res_Txt_TransDao.TABLE+" tr,\n" +
                "            "+ EV_Module_Res_TxtDao.TABLE+" ts,\n" +
                "            "+ EV_Module_ResDao.TABLE+" rs\n" +
                "       WHERE \n" +
                "         rs."+EV_Module_ResDao.MODULE_CODE+" = ts."+EV_Module_Res_TxtDao.MODULE_CODE+"\n" +
                "         AND rs."+EV_Module_ResDao.RESOURCE_CODE+" = ts."+EV_Module_Res_TxtDao.RESOURCE_CODE+"\n" +
                "         \n" +
                "         AND ts."+EV_Module_Res_TxtDao.MODULE_CODE+" = tr."+EV_Module_Res_Txt_TransDao.MODULE_CODE+"\n" +
                "         AND ts."+EV_Module_Res_TxtDao.RESOURCE_CODE+" = tr."+EV_Module_Res_Txt_TransDao.RESOURCE_CODE+"\n" +
                "         AND ts."+EV_Module_Res_TxtDao.TXT_CODE+" = tr."+EV_Module_Res_Txt_TransDao.TXT_CODE+"\n" +
                "       \n" +
                "         AND rs."+EV_Module_ResDao.MODULE_CODE+" = 'CUST_FORM'\n" +
                "         AND rs."+EV_Module_ResDao.RESOURCE_NAME+" = cf."+GE_Custom_FormDao.CUSTOMER_CODE+"||'|'||cf."+GE_Custom_FormDao.CUSTOM_FORM_TYPE+"||'|'||cf."+GE_Custom_FormDao.CUSTOM_FORM_CODE+"||'|'||cf."+GE_Custom_FormDao.CUSTOM_FORM_VERSION+" \n" +
                "         AND tr."+EV_Module_Res_Txt_TransDao.TRANSLATE_CODE+" = '" + s_translate_code +"'\n" +
                "         AND tr."+EV_Module_Res_TxtDao.TXT_CODE+" = 'TITLE') "+GE_Custom_FormDao.CUSTOM_FORM_DESC+"\n" +
                "    FROM\n" +
                     GE_Custom_FormDao.TABLE +" CF \n," +
                     GE_Custom_Form_ProductDao.TABLE +" p \n" +
                "    WHERE\n" +
                "      cf."+GE_Custom_FormDao.CUSTOMER_CODE+" = p." + GE_Custom_Form_ProductDao.CUSTOMER_CODE + "\n " +
                "      AND cf."+GE_Custom_FormDao.CUSTOM_FORM_TYPE+" = p." + GE_Custom_Form_ProductDao.CUSTOM_FORM_TYPE + "\n " +
                "      AND cf."+GE_Custom_FormDao.CUSTOM_FORM_CODE+" = p." + GE_Custom_Form_ProductDao.CUSTOM_FORM_CODE + "\n " +
                "      AND cf."+GE_Custom_FormDao.CUSTOM_FORM_VERSION+" = p." + GE_Custom_Form_ProductDao.CUSTOM_FORM_VERSION + "\n " +

                "      AND cf."+GE_Custom_FormDao.CUSTOMER_CODE+" = '" + s_customer_code + "'\n" +
                "      AND cf."+GE_Custom_FormDao.CUSTOM_FORM_TYPE+" = '" + s_form_type_code +"'\n" +
                "      AND p."+ GE_Custom_Form_ProductDao.PRODUCT_CODE+" = '" + product_code +"'\n" +
                "      AND EXISTS(SELECT\n" +
                "                     1\n" +
                "               FROM\n" +
                                    GE_Custom_Form_OperationDao.TABLE +" o\n" +
                "               WHERE\n" +
                "                 o.customer_code = p.customer_code\n" +
                "                 and o.custom_form_type = p.custom_form_type\n" +
                "                 and o.custom_form_code = p.custom_form_code\n" +
                "                 and o.custom_form_version = p.custom_form_version\n" +
                "                 and o.operation_code = '"+s_operation_code+"'\n" +
                "                )  " +
                "    \n" +
                "    ORDER BY\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_CODE+",\n" +
                "      cf."+GE_Custom_FormDao.CUSTOM_FORM_VERSION+";" +
                GE_Custom_FormDao.CUSTOMER_CODE+"#"+GE_Custom_FormDao.CUSTOM_FORM_TYPE+"#"+GE_Custom_FormDao.CUSTOM_FORM_CODE+"#"+GE_Custom_FormDao.CUSTOM_FORM_VERSION+"#"+GE_Custom_FormDao.CUSTOM_FORM_DESC)
                .toString();*/

    }
}
