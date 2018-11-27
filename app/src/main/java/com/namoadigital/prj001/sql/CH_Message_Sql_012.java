package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by neomatrix on 12/20/17.
 *
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 */

public class CH_Message_Sql_012 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);

    private ArrayList<HMAux> messages;
    private String deviceGMT = ToolBox_Inf.getDeviceGMT(false);
    public CH_Message_Sql_012(ArrayList<HMAux> messages) {
        this.messages = messages;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(" SELECT \n" +
                        "    m.*, \n" +
                        //"strftime('%Y-%m-%d %H:%M:%S',msg_date,'localtime') as msg_date_zone " +
                        "strftime('%Y-%m-%d %H:%M:%S',msg_date,'"+deviceGMT+"') as msg_date_zone \n" +
                        " FROM \n" +
                        CH_MessageDao.TABLE + " m \n" +
                        " WHERE\n" +
                        " (1 != 1) \n");

        for (HMAux message : messages) {
            sb
                    .append(" or ( (tmp = '" + message.get("tmp") + "')  )\n");
        }

        sb
                .append(" ORDER BY \n" +
                        " case when msg_pk is null \n" +
                        "      then 1 \n" +
                        "      else 0 \n" +
                        " end,\n" +
                        " msg_pk,\n" +
                        " msg_prefix,\n" +
                        " tmp")
                .append(";");
                //.append(HmAuxFields+"#msg_date_zone");

        return sb.toString();
    }
}
