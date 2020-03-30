package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 17/01/2017.
 *
 * LUCHE - 26/03/2020
 *
 * Modificado query para filtrar APENAS os parceiros operacionais.
 * Essa query até esa data é usada somente para o modulo de O.S, e nele faz sentido.
 *
 * Se for usar essa query em outro processo, analisar o impacto da mudança.
 */

public class MD_Partner_Sql_SS implements Specification {

    private long s_customer_code;

    public MD_Partner_Sql_SS(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        // customer_code, partner_code, partner_id as id, partner_desc as description

        return sb
                .append("SELECT \n" +
                        "   customer_code, \n" +
                        "   partner_code "+ SearchableSpinner.CODE +",\n " +
                        "   partner_id "+ SearchableSpinner.ID +",\n" +
                        "   partner_desc "+ SearchableSpinner.DESCRIPTION +" \n" +
                        "FROM \n")
                .append(MD_PartnerDao.TABLE).append(" \n")
                .append(" WHERE \n" +
                        "   " + MD_PartnerDao.CUSTOMER_CODE + " = '" + s_customer_code + "' \n" +
                        "   AND " + MD_PartnerDao.OPERATIONAL + " = '1' \n" +
                        " ORDER BY \n" +
                        "      partner_desc \n")
                .append(";")
                //.append("customer_code#partner_id#id#description")
                .toString();
    }
}
