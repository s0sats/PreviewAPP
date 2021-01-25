package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 23/01/17.
 *
 * LUCHE - 25/01/2021
 * Essa query não foi atualizada com os novos campos do site e quando usada para carregar um obj MD_SITE
 * estava gerando obj null.
 * Sua utilização foi substituida pela MD_Site_Sql_003 que faz um select *
 */
@Deprecated
public class MD_Site_Sql_001 implements Specification {

    private long s_customer_code;
    private String s_site_code;

    public MD_Site_Sql_001(long s_customer_code, String s_site_code) {
        this.s_customer_code = s_customer_code;
        this.s_site_code = s_site_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       s.customer_code,\n " +
                        "       s.site_code,\n " +
                        "       s.site_id,\n " +
                        "       s.site_id||' - '||s.site_desc site_desc,\n " +
                        "       s.io_control,\n " +
                        "       s.inbound_auto_create\n ," +
                        "       s.in_allow_new_item,\n" +
                        "       s.in_put_away_process,\n" +
                        "       s.in_zone_code_conf,\n" +
                        "       s.in_local_code_conf,\n" +
                        "       s.in_done_automatic,\n" +
                        "       s.out_allow_new_item,\n" +
                        "       s.out_picking_process,\n" +
                        "       s.out_zone_code_picking,\n" +
                        "       s.out_local_code_picking,\n" +
                        "       s.out_done_automatic \n" +
                        " FROM \n " +
                        MD_SiteDao.TABLE + " s\n" +
                        " WHERE \n" +
                        MD_SiteDao.CUSTOMER_CODE + " = '" + s_customer_code + "' \n" +
                        " AND " + MD_SiteDao.SITE_CODE + " = '" + s_site_code + "' \n")
                .toString();
    }

}
