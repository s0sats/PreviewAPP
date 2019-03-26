package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_Product_SegmentDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Segment_Sql_SS implements Specification {
    private String customer_code;
    private String product_code;

    public MD_Segment_Sql_SS(String customer_code, String product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                                "  s.segment_code " + SearchableSpinner.CODE +", " +
                                "  s.segment_id " + SearchableSpinner.ID +", " +
                                "  s.segment_desc "+ SearchableSpinner.DESCRIPTION +
                                " FROM " +
                                MD_SegmentDao.TABLE +" s ,\n" +
                                MD_Product_SegmentDao.TABLE +" ps \n" +
                                " WHERE " +
                                "   s.customer_code = ps.customer_code \n" +
                                "   and s.segment_code = ps.segment_code" +
                                "                                       " +
                                "   and s.customer_code = '"+ customer_code +"'" +
                                "   and ps.product_code = '"+ product_code +"'" +

                                " ORDER BY " +
                                "     s.segment_id,s.segment_desc;")
                //.append(SearchableSpinner.ID + "#segment_id#"+SearchableSpinner.DESCRIPTION )
                .toString();
    }
}
