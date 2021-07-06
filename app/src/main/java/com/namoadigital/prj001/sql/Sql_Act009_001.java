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
 * LUCHE - 06/07/2021
 * Modificado query, add o param de block_spontaneous na query. Para forms avulsos, somente os fomrs
 * block_spontaneous = 0 devem ser considerados, para o outros processo, qualquer um
 */

public class Sql_Act009_001 implements Specification {

    private long s_customer_code;
    private long s_product_code;
    private String s_translate_code;
    private long s_operation_code;
    private String s_site_code;
    private String s_serial_id;
    private Integer block_spontaneous;

    public Sql_Act009_001(long s_customer_code, long s_product_code, String s_translate_code, long s_operation_code, String s_site_code, String s_serial_id, Integer block_spontaneous) {
        this.s_customer_code = s_customer_code;
        this.s_product_code = s_product_code;
        this.s_translate_code = s_translate_code;
        this.s_operation_code = s_operation_code;
        this.s_site_code = s_site_code;
        this.s_serial_id = s_serial_id.trim().length() != 0 ? s_serial_id.trim()  : "null";
        this.block_spontaneous = block_spontaneous;
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
                        "   AND ("+block_spontaneous+" is null OR f.block_spontaneous = '"+block_spontaneous+"')\n"+
                        "   AND (f.all_product = 1 OR p.product_code = '"+s_product_code+"')\n" +
                        "   AND (f.all_operation = 1 OR o.operation_code = '"+s_operation_code+"') \n" +
                        "   AND (f.all_site = 1 OR s.site_code = '"+s_site_code+"')\n"+
                        "   AND ( '"+s_serial_id+"' IS NOT NULL OR f.require_serial_done = 0)\n"+
                        " ORDER BY \n" +
                        "   upper(" + MdTagDao.TAG_DESC+ ") \n"
                )
                .append(";")
                .toString()
                .replace("'null'","null");

    }
}
