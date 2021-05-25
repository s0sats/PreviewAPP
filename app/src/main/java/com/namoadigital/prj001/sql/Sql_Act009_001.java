package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_SiteDao;
import com.namoadigital.prj001.dao.MdTagDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE - 24/05/2021
 * Substituido o join com a form type pela md_tag, pois agora a listagem será por tag e não por tipo
 */

public class Sql_Act009_001 implements Specification {

    private long s_customer_code;
    private long s_product_code;
    private String s_translate_code;
    private long s_operation_code;
    private String s_site_code;
    private String s_serial_id;

    public Sql_Act009_001(long s_customer_code, long s_product_code, String s_translate_code, long s_operation_code, String s_site_code, String s_serial_id) {
        this.s_customer_code = s_customer_code;
        this.s_product_code = s_product_code;
        this.s_translate_code = s_translate_code;
        this.s_operation_code = s_operation_code;
        this.s_site_code = s_site_code;
        this.s_serial_id = s_serial_id.trim().length() != 0 ? s_serial_id.trim()  : "null";
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT \n" +
                        "    DISTINCT " +
                        "       T."+MdTagDao.TAG_CODE+", \n" +
                        "       T."+MdTagDao.TAG_DESC+" \n" +
                        " FROM \n" +
                        "       "+ MdTagDao.TABLE + " t, \n" +
                        "       "+ GE_Custom_FormDao.TABLE + " f \n" +
                        " LEFT JOIN\n" +
                        "       "+ GE_Custom_Form_ProductDao.TABLE +" p on p.customer_code = f.customer_code\n" +
                        "                             and p.custom_form_type = f.custom_form_type\n" +
                        "                             and p.custom_form_code = f.custom_form_code\n" +
                        "                             and p.custom_form_version = f.custom_form_version\n" +
                        "                             and p.product_code = '"+s_product_code+"'\n" +
                        " LEFT JOIN\n" +
                        "       "+ GE_Custom_Form_OperationDao.TABLE +" o on o.customer_code = f.customer_code\n" +
                        "                               and o.custom_form_type = f.custom_form_type\n" +
                        "                               and o.custom_form_code = f.custom_form_code\n" +
                        "                               and o.custom_form_version = f.custom_form_version \n "+
                        "                               and o.operation_code = '"+s_operation_code+"' \n "+
                        " LEFT JOIN\n" +
                        "    "+ GE_Custom_Form_SiteDao.TABLE +" s on s.customer_code = f.customer_code\n" +
                        "                               and s.custom_form_type = f.custom_form_type\n" +
                        "                               and s.custom_form_code = f.custom_form_code\n" +
                        "                               and s.custom_form_version = f.custom_form_version \n"+
                        "                               and s.site_code = '"+s_site_code+"' \n"+
                        " WHERE    \n" +
                        "   t.customer_code = f.customer_code \n" +
                        "   AND t.tag_code = f.tag_operational_code\n"+
                        "\n"+
                        "   AND t.customer_code = '"+s_customer_code+"'\n"+
                        "   AND (f.all_product = 1 OR p.product_code = '"+s_product_code+"')\n" +
                        "   AND (f.all_operation = 1 OR o.operation_code = '"+s_operation_code+"') \n" +
                        "   AND (f.all_site = 1 OR s.site_code = '"+s_site_code+"')\n"+
                        "   AND ( '"+s_serial_id+"' IS NOT NULL OR f.require_serial_done = 0)\n"+
                        " ORDER BY \n" +
                        "   upper(" + MdTagDao.TAG_DESC+ ") \n"
                )
                .append(";")
                //.append(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+"#"+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC)
                .toString()
                .replace("'null'","null");

        /*return sb
                .append(
                        " SELECT \n" +
                        "    DISTINCT T."+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+", \n" +
                        "    ( \n" +
                        "     SELECT \n" +
                        "        ts."+EV_Module_Res_Txt_TransDao.TXT_VALUE +"\n"+
                        "     FROM \n" +
                                EV_Module_ResDao.TABLE +" mr, \n" +
                                EV_Module_Res_Txt_TransDao.TABLE +" ts \n" +
                        "     WHERE\n " +
                        "          ts."+EV_Module_Res_Txt_TransDao.MODULE_CODE+" = mr."+EV_Module_ResDao.MODULE_CODE+" \n" +
                        "          and ts."+EV_Module_Res_Txt_TransDao.RESOURCE_CODE+" = mr."+EV_Module_ResDao.RESOURCE_CODE+" \n" +
                        "          and ts."+EV_Module_Res_Txt_TransDao.TXT_CODE+" = t.customer_code || '|' || t.custom_form_type    \n " +
                        "           \n" +
                        "          and ts."+EV_Module_Res_Txt_TransDao.MODULE_CODE+" = 'CUST_FORM'       \n" +
                        "          and ts."+EV_Module_Res_Txt_TransDao.TRANSLATE_CODE+" = '"+s_translate_code+"' \n" +
                        "    ) "+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC+" \n"+
                        "     \n" +
                        " FROM \n" +
                        GE_Custom_Form_TypeDao.TABLE + " t, \n" +
                        GE_Custom_Form_ProductDao.TABLE + " p \n" +
                        " WHERE \n" +
                        "   t."+GE_Custom_Form_TypeDao.CUSTOMER_CODE+" = p."+GE_Custom_Form_ProductDao.CUSTOMER_CODE+" \n" +
                        "   AND t."+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+" = p."+GE_Custom_Form_ProductDao.CUSTOM_FORM_TYPE+" \n" +
                        "   \n" +
                        "   AND p."+GE_Custom_Form_ProductDao.CUSTOMER_CODE+" = '" + s_customer_code + "' \n" +
                        "   AND p."+GE_Custom_Form_ProductDao.PRODUCT_CODE +" = '" + s_product_code + "' \n" +
                        "   AND EXISTS(SELECT\n" +
                        "                     1\n" +
                        "               FROM\n" +
                                            GE_Custom_Form_OperationDao.TABLE +" o\n" +
                        "               WHERE\n" +
                        "                 o.customer_code = p.customer_code\n" +
                        "                 and o.custom_form_type = p.custom_form_type\n" +
                        "                 and o.custom_form_code = p.custom_form_code\n" +
                        "                 and o.custom_form_version = p.custom_form_version\n" +
                        "                 and o.operation_code = '"+s_operation_code+"'\n" +
                        "                )  \n" +
                        " order by \n" +
                        "   t."+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE)
                        .append(";")
                        .append(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+"#"+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC)
                .toString();*/

    }
}
