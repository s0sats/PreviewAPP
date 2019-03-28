package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/03/2019.
 *
 * Criado query seleciona locais baseado no site / zone, mas sem regras de site restriction etc
 */

public class MD_Site_Zone_Local_Sql_SS_002 implements Specification {
    private String customer_code;
    private String site_code;
    private String zone_code;

    public MD_Site_Zone_Local_Sql_SS_002(String customer_code, String site_code, String zone_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.zone_code = zone_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "   l.local_code "+ SearchableSpinner.CODE +",\n " +
                        "   l.local_id "+ SearchableSpinner.ID +",\n " +
                        "   l.local_id "+ SearchableSpinner.DESCRIPTION +",\n " +
                        "   s.site_code,\n" +
                        "   s.site_desc,\n" +
                        "   z.zone_code,\n" +
                        "   z.zone_id,\n" +
                        "   z.zone_desc \n" +
                        " FROM \n" +
                        "       "+MD_Site_Zone_LocalDao.TABLE + " l,\n"+
                        "       "+MD_SiteDao.TABLE + " s,\n"+
                        "       "+MD_Site_ZoneDao.TABLE + " z\n"+
                        " WHERE \n" +
                        "  l.customer_code = s.customer_code\n" +
                        "  and l.site_code  = s.site_code\n" +
                        "  \n" +
                        "  and l.customer_code = z.customer_code\n" +
                        "  and l.site_code = z.site_code\n" +
                        "  and l.zone_code = z.zone_code" +
                        "  and l.customer_code = '"+ customer_code +"' \n" +
                        "  and l.site_code = '"+ site_code +"' \n" +
                        "  and ("+zone_code+" is null OR l.zone_code = '"+ zone_code +"') \n" +
                        " ORDER BY\n" +
                        "      l.local_id;")
                //.append(SearchableSpinner.ID + "#"+SearchableSpinner.DESCRIPTION+"#site_code#site_desc#zone_code#zone_desc")
                .toString();
    }
}
