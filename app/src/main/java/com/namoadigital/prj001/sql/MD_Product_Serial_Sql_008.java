package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 13/11/2017.
 *
 *
 * Query que seleciona todos do serial e relacionais
 * de um unico serial, diferentemente da 001, se baseia pelo serial_code.
 *
 * Retorna HMAux
 *
 */

public class MD_Product_Serial_Sql_008 implements Specification {

    private long customer_code;
    private long product_code;
    private int serial_code;

    public MD_Product_Serial_Sql_008(long customer_code, long product_code, int serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       p.customer_code,\n" +
                        "       p.product_code,\n" +
                        "       p.product_id,\n" +
                        "       p.product_desc,\n" +
                        "       ps.serial_code,\n" +
                        "       ps.serial_code,\n" +
                        "       s.site_code,\n" +
                        "       s.site_id,\n" +
                        "       s.site_desc,\n" +
                        "       z.zone_code,\n" +
                        "       z.zone_id,\n" +
                        "       z.zone_desc,\n" +
                        "       l.local_code,\n" +
                        "       l.local_id,\n" +
                        "       so.site_code site_code_owner,\n" +
                        "       so.site_id site_id_owner,\n" +
                        "       so.site_desc site_desc_owner,\n" +
                        "       b.brand_code,\n" +
                        "       b.brand_id,\n" +
                        "       b.brand_desc,\n" +
                        "       m.model_code,\n" +
                        "       m.model_id,\n" +
                        "       m.model_desc,\n" +
                        "       c.color_code,\n" +
                        "       c.color_id,\n" +
                        "       c.color_desc,\n" +
                        "       sg.segment_code,\n" +
                        "       sg.segment_id,\n" +
                        "       sg.segment_desc,\n" +
                        "       cp.category_price_code,\n" +
                        "       cp.category_price_id,\n" +
                        "       cp.category_price_desc,\n" +
                        "       ps.add_inf1,\n" +
                        "       ps.add_inf2,\n" +
                        "       ps.add_inf3\n" +
                        "           \n" +
                        " FROM\n" +
                        "     md_product_serials ps\n" +
                        " INNER JOIN\n" +
                        "    md_products p on ps.customer_code = p.customer_code and ps.product_code = p.product_code   \n" +
                        " LEFT JOIN\n" +
                        "    md_sites s on ps.customer_code = s.customer_code and ps.site_code = s.site_code\n" +
                        " LEFT JOIN\n" +
                        "    md_site_zones z on ps.customer_code = z.customer_code and ps.site_code = z.site_code and ps.zone_code = z.zone_code\n" +
                        " LEFT JOIN \n" +
                        "     md_site_zone_locals l on ps.customer_code = l.customer_code and ps.site_code = l.site_code and ps.zone_code = l.zone_code and ps.local_code = l.local_code\n" +
                        " LEFT JOIN\n" +
                        "    md_sites so on ps.customer_code = so.customer_code and ps.site_code_owner = so.site_code\n" +
                        " LEFT JOIN         \n" +
                        "     md_brands b on ps.customer_code = b.customer_code and ps.brand_code = b.brand_code\n" +
                        " LEFT JOIN             \n" +
                        "     md_brand_models m on ps.customer_code = m.customer_code  and ps.brand_code = m.brand_code  and ps.model_code = m.model_code\n" +
                        " LEFT JOIN                 \n" +
                        "     md_brand_colors c on ps.customer_code = c.customer_code and ps.brand_code = c.brand_code and ps.color_code = c.color_code\n" +
                        " LEFT JOIN      \n" +
                        "     md_segments sg on ps.customer_code = sg.customer_code and ps.segment_code = sg.segment_code\n" +
                        " LEFT JOIN          \n" +
                        "     md_category_prices cp on ps.customer_code = cp.customer_code and ps.category_price_code = cp.category_price_code    \n" +
                        " \n" +
                        " WHERE\n" +
                        "    ps.customer_code = '"+customer_code+"'\n" +
                        "    and ps.product_code = '"+product_code+"'\n" +
                        "    and ps.serial_code = '"+ serial_code +"';")
                .append("customer_code#product_code#product_id#product_desc#serial_code#serial_code#site_code#site_id#site_desc#zone_code#zone_id#zone_desc#local_code#local_id#site_code_owner#site_id_owner#site_desc_owner#brand_code#brand_id#brand_desc#model_code#model_id#model_desc#color_code#color_id#color_desc#segment_code#segment_id#segment_desc#category_price_code#category_price_id#category_price_desc#add_inf1#add_inf2#add_inf3")
                .toString();
    }
}
