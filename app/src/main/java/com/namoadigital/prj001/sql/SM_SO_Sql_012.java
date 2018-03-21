package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 21/06/2017.
 * <p>
 * Seta update required pra 1
 */

public class SM_SO_Sql_012 implements Specification {

    private int approval_required;
    private long customer_code;
    private int so_prefix;
    private int so_code;
    private Integer client_approval_user;

    private String client_approval_date;
    private String client_name;
    private String client_approval_image_name;
    private String client_approval_type_sig;
    private String approval_type;

    private Integer quality_approval_user;
    private String quality_approval_user_nick;
    private String quality_approval_date;

    public SM_SO_Sql_012(int approval_required, long customer_code, int so_prefix, int so_code, Integer client_approval_user, String client_approval_date, String client_name, String client_approval_image_name, String client_approval_type_sig, String approval_type, Integer quality_approval_user, String quality_approval_user_nick, String quality_approval_date) {
        this.approval_required = approval_required;
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.client_approval_user = client_approval_user;

        this.client_approval_date = client_approval_date;
        this.client_name = client_name;
        this.client_approval_image_name = client_approval_image_name;
        this.client_approval_type_sig = client_approval_type_sig;
        this.approval_type = approval_type;

        this.quality_approval_user = quality_approval_user;
        this.quality_approval_user_nick = quality_approval_user_nick;
        this.quality_approval_date = quality_approval_date;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        StringBuilder sbStatus = new StringBuilder();

        if (approval_required == 1 || approval_required == 2) {
            sbStatus.append(Constant.SYS_STATUS_WAITING_SYNC);
        } else {
            //sbStatus.append(Constant.SYS_STATUS_WAITING_CLIENT);
            sbStatus.append(approval_type);
        }

        return sb
                .append(" UPDATE " + SM_SODao.TABLE + "\n" +
                        "  set approval_required = '" + approval_required + "',\n" +
                        "  status ='" + sbStatus.toString() + "',\n" +
                        "  client_approval_user = '" + client_approval_user + "',\n" +

                        "  client_approval_date = '" + client_approval_date + "',\n" +
                        "  client_name = '" + client_name + "',\n" +
                        "  client_approval_image_name = '" + client_approval_image_name + "',\n" +
                        "  client_approval_type_sig = '" + client_approval_type_sig + "',\n" +

                        "  quality_approval_user = '" + quality_approval_user + "',\n" +
                        "  quality_approval_user_nick = '" + quality_approval_user_nick + "',\n" +
                        "  quality_approval_date = '" + quality_approval_date + "'\n" +

                        " WHERE\n" +
                        "  customer_code = '" + customer_code + "'\n" +
                        "  and so_prefix = '" + so_prefix + "'\n" +
                        "  and so_code = '" + so_code + "'")
                .toString().replace("\'null\'", "NULL");
    }
}
