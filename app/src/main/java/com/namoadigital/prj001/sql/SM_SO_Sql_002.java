package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 21/06/2017.
 */

public class SM_SO_Sql_002 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public SM_SO_Sql_002(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        SM_SODao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        "    AND S.so_prefix =  '" + so_prefix + "'\n" +
                        "    AND S.so_code =    '" + so_code + "' ")
                .append(";")
                .append("customer_code#so_prefix#so_code#so_id#so_scn#so_desc#product_code#product_id#product_desc#serial_code#serial_id#category_price_code#category_price_id#category_price_desc#segment_code#segment_id#segment_desc#site_code#site_id#site_desc#operation_code#operation_id#operation_desc#contract_code#contract_desc#contract_po_erp#contract_po_client1#contract_po_client2#priority_code#priority_desc#status#quality_approval_user#quality_approval_date#comments#so_father_prefix#so_father_code#deadline#origin#client_type#client_user#client_code#client_id#client_name#client_email#client_phone#client_approval_image#client_approval_image_name#client_approval_image_url#client_approval_date#client_approval_flag#origin_change#started_flag#edit_origin#edit_user#total_qty_service#total_price")
                .toString();
    }
}
