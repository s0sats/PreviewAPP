package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Zone_Local_Sql_SS implements Specification {
    private String customer_code;
    private String site_code;
    private String zone_code;
    private String site_code_restriction;

    public MD_Site_Zone_Local_Sql_SS(String customer_code, String site_code, String zone_code) {
        this.customer_code = customer_code;
//        this.site_code = site_code.length() > 0 ? site_code : null;
//        this.zone_code = zone_code.length() > 0 ? zone_code : null;
        this.site_code = site_code;
        this.zone_code = zone_code;
        this.site_code_restriction = "";
    }

    public MD_Site_Zone_Local_Sql_SS(String customer_code, String site_code, String zone_code,String site_code_restriction) {
        this.customer_code = customer_code;
//        this.site_code = site_code.length() > 0 ? site_code : null;
//        this.zone_code = zone_code.length() > 0 ? zone_code : null;
        this.site_code = site_code;
        this.zone_code = zone_code;
        this.site_code_restriction = site_code_restriction;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "   l.local_code "+ SearchableSpinner.CODE +",\n " +
                        "   l.local_id "+ SearchableSpinner.DESCRIPTION +",\n " +
                        "   s.site_code,\n" +
                        "   s.site_desc,\n" +
                        "   z.zone_code,\n" +
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
                        "  and ('" + site_code  + "' IS NULL \n"+
                        "        or (l.site_code = '"+ site_code +"' \n"+
                        "            and ( '"+ zone_code  + "' IS NULL OR l.zone_code= '"+ zone_code +"')" +
                        "           )\n" +
                        "       ) \n" +
                        " and ( length('"+site_code_restriction+"') = 0 OR l.site_code in ("+site_code_restriction+")  ) \n"+
                        " ORDER BY\n" +
                        "      l.local_id;")
                //.append(SearchableSpinner.ID + "#"+SearchableSpinner.DESCRIPTION+"#site_code#site_desc#zone_code#zone_desc")
                .toString().replace("'%null%'","null").replace("'null'","null");
    }
}
