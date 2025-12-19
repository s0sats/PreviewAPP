package com.namoadigital.prj001.sql.material

import android.util.Log
import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao
import com.namoadigital.prj001.database.Specification

class GetProductListSql(
    private val s_customer_code: String?,
    private val s_group_code: String?,
    private val s_filter: String?,
    private val iType: Int
) : Specification {

    var affinityCounterQuery = "0 SCORE,"
    var affinityFilter = ""

    init{
        val charFilter = if(s_filter == "null") null else s_filter
        var searchWords = charFilter?.split("\\s+".toRegex(), limit = 10)
        Log.d("searchWords", "$searchWords")
        searchWords?.let {
            if (it.size > 0) {
                affinityCounterQuery = getAffinityCounterQuery(it)
                affinityFilter = getAffinityFilter(it)
            }
        }
    }

    private fun getAffinityCounterQuery(searchWords: List<String>): String {
        return """ case when lower(p.product_desc) GLOB lower('*$s_filter*')
                   then 999
                   else ${getWordsCriteria(searchWords)}
                    end SCORE,
        """.trimIndent()
    }

    private fun getWordsCriteria(searchWords: List<String>): String{
        return searchWords
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(" +\n") { term ->
                "IIF(lower(p.product_desc) GLOB lower('*$term*'), 1, 0)"
            }
    }

    private fun getAffinityFilter(searchWords: List<String>): String {

        return searchWords
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(" \n") { term ->
                "or lower(p.product_desc) GLOB lower('*$term*') \n  OR lower(p.product_id) GLOB lower('*$term*') "
            }
    }

    override fun toSqlQuery(): String {
        val sb = StringBuilder()

        if (iType == 0) {
            sb
                .append(
                    " SELECT\n" +
                            "    p.product_code code,\n" +
                            "    p.product_id id,\n" +
                            "    p.product_desc desc,\n" +
                            "    p.product_desc full_desc,\n" +
                            """
                             $affinityCounterQuery   
                            """.trimIndent() +
                            "    'product' type \n" +
                            " FROM\n" +
                            "    md_products p\n" +
                            " WHERE p.customer_code= " + s_customer_code + " \n" +
                            "   AND ( '" + s_filter + "' IS NULL\n" +
                            """
                                $affinityFilter   
                            """.trimIndent() +
                            "          ) \n" +
                            "      AND p.has_group = 0" +
                            "  ORDER BY \n" +
                            "     SCORE DESC," +
                            "     trim(p.product_desc);"
                ) //.append("product_code#product_id#product_desc#full_product_desc#type")
                .toString()
        } else {
            sb
                .append(
                    "     SELECT\n" +
                            "        p.product_code code,\n" +
                            "        p.product_id id,\n" +
                            "        p.product_desc desc,\n" +
                            "        p.product_desc full_desc,\n" +
                            """
                             $affinityCounterQuery   
                            """.trimIndent() +
                            "        'product' type \n" +
                            "     FROM\n " +
                            "        md_products p \n" +
                            "     WHERE\n " +
                            "            p.customer_code= " + s_customer_code + "   \n" +
                            "   AND ( '" + s_filter + "' IS NULL\n" +
                            """
                             $affinityFilter   
                            """.trimIndent() +
                            "          ) \n" +
                            "      AND p.has_group = 1" +
                            "   AND exists " +
                            "   (\n" +
                            "    select 1 \n" +
                            "      from " +
                            "     " + MD_Product_Group_ProductDao.TABLE + " as pgp \n" +
                            "     where p.customer_code = pgp.customer_code \n" +
                            "       and p.product_code = pgp.product_code\n" +
                            "       and pgp.group_code =" + s_group_code + "\n" +
                            " )" +
                            "     ORDER BY\n" +
                            "     SCORE DESC," +
                            "     trim(p.product_desc);"
                ) //.append("product_code#product_id#product_desc#full_product_desc#type")
                .toString()

        }

        return sb.toString().replace("'%null%'", "null")
            .replace("'null'", "null")
            .replace("'*null*'", "null")
    }
}
