package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class MD_Product_Category_HMAux_MD_CadProd_Lista_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_category_product_code;
    private String s_filtro;
    //
    private String s_aplicacar = "";

    public MD_Product_Category_HMAux_MD_CadProd_Lista_SqlSpecification(String s_customer_code, String s_category_product_code, String s_filtro) {
        this.s_customer_code = s_customer_code;
        this.s_category_product_code = s_category_product_code;
        this.s_filtro = s_filtro;
    }

    @Override
    public String toSqlQuery() {

        if (!s_filtro.trim().isEmpty()) {
            s_aplicacar = "and ( (descricao like '%" +
                    s_filtro +
                    "%') or (codigo like '%" +
                    s_filtro +
                    "%'))";
        }

        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT cp.customer_code, cp.struc_type as tipo, cp.category_code, cp.category_code_father, cp.category_desc, ifnull(pr.product_code, 0) as product_code, ifnull(pr.product_desc,'') as product_desc, ifnull(pr.active,1) as active, CASE WHEN cp.struc_type = 'NODE' THEN cp.category_desc ELSE pr.product_desc END as descricao, CASE WHEN cp.struc_type = 'NODE' THEN cp.category_code ELSE pr.product_code END as codigo, CASE WHEN cp.struc_type = 'NODE' THEN cp.category_code ELSE pr.product_id || ' - ' || pr.product_code END as codigo_descricao\n" +
                        "\n" +
                        "FROM md_product_categorys as cp \n" +
                        "\n" +
                        "LEFT JOIN md_products as pr ON cp.customer_code = pr.customer_code and cp.product_code = pr.product_code\n" +
                        "\n" +
                        "WHERE cp.customer_code = '" +
                        s_customer_code +
                        "' and cp.active = '1' and cp.category_code_father = '" +
                        s_category_product_code +
                        "' \n" +
                        s_aplicacar +
                        " \n" +
                        "ORDER BY cp.struc_type DESC;")
                .append("codigo#tipo#descricao#codigo_descricao")
                .toString();
    }


}
