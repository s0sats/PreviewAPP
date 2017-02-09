package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 07/02/2017.
 */

public class GE_Custom_Form_Type_Sql_001 implements Specification {

    public static final String CUSTOM_FORM_TYPE_DESC_FULL = "custom_form_type_desc_full";

    private long s_customer_code;
    private long s_product_code;
    private String s_translate_code;

    public GE_Custom_Form_Type_Sql_001(long s_customer_code, long s_product_code, String s_translate_code) {
        this.s_customer_code = s_customer_code;
        this.s_product_code = s_product_code;
        this.s_translate_code = s_translate_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "    DISTINCT T."+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+", " +
                        "    ( " +
                        "     SELECT " +
                        "        ts."+EV_Module_Res_Txt_TransDao.TXT_VALUE +
                        "     FROM " +
                                EV_Module_ResDao.TABLE +" mr, " +
                                EV_Module_Res_Txt_TransDao.TABLE +" ts " +
                        "     WHERE " +
                        "          ts."+EV_Module_Res_Txt_TransDao.MODULE_CODE+" = mr."+EV_Module_ResDao.MODULE_CODE+" " +
                        "          and ts."+EV_Module_Res_Txt_TransDao.RESOURCE_CODE+" = mr."+EV_Module_ResDao.RESOURCE_CODE+" " +
                        "          and ts."+EV_Module_Res_Txt_TransDao.TXT_CODE+" = t.customer_code || '|' || t.custom_form_type     " +
                        "           " +
                        "          and ts."+EV_Module_Res_Txt_TransDao.MODULE_CODE+" = 'CUST_FORM'       " +
                        "          and ts."+EV_Module_Res_Txt_TransDao.TRANSLATE_CODE+" = '"+s_translate_code+"' " +
                        "    ) "+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC+" "+
                        "     " +
                        " FROM " +
                        GE_Custom_Form_TypeDao.TABLE + " t, " +
                        GE_Custom_Form_ProductDao.TABLE + " p " +
                        " WHERE " +
                        "   t."+GE_Custom_Form_TypeDao.CUSTOMER_CODE+" = p."+GE_Custom_Form_ProductDao.CUSTOMER_CODE+" " +
                        "   AND t."+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+" = p."+GE_Custom_Form_ProductDao.CUSTOM_FORM_TYPE+" " +
                        "   " +
                        "   AND p."+GE_Custom_Form_ProductDao.CUSTOMER_CODE+" = '" + s_customer_code + "' " +
                        "   AND p."+GE_Custom_Form_ProductDao.PRODUCT_CODE +" = '" + s_product_code + "' " +
                        " order by " +
                        "   t."+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+" ;" +
                        GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE+"#"+GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC)
                .toString();

    }
}
