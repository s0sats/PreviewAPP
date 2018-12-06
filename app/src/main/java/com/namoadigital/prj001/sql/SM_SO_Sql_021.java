package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 26/10/2017.
 *
 * Seleciona assinaturas de s.o pendente de downloads.
 *
 */

public class SM_SO_Sql_021 implements Specification {

    private long customer_code;

    public SM_SO_Sql_021(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   s.customer_code,\n" +
                        "   s.so_prefix,\n" +
                        "   s.so_code,\n" +
                        "   replace(s.client_approval_image_name,'.png','') client_approval_image_name,\n" +
                        "   s.client_approval_image_url\n" +
                        " FROM\n" +
                        SM_SODao.TABLE +" s\n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   AND s.client_type = '"+Constant.CLIENT_TYPE_CLIENT+"'\n" +
                        "   AND s.status = '"+Constant.SYS_STATUS_DONE +"'\n" +
                        "   AND s.client_approval_image_url is not null\n"+
                        "   AND s.client_approval_image_url_local is null\n"
                )
                .append(";")
                /*.append(
                        SM_SODao.CUSTOMER_CODE +"#" +
                        SM_SODao.SO_PREFIX +"#"+
                        SM_SODao.SO_CODE +"#"+
                        SM_SODao.CLIENT_APPROVAL_IMAGE_NAME + "#" +
                        SM_SODao.CLIENT_APPROVAL_IMAGE_URL
                )*/
                .toString();
    }
}
