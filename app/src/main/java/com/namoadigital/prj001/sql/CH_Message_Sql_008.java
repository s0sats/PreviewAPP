package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 06/12/2017.
 * <p>
 * Seleciona uma unica msg usando o tmp
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 *
 */

public class CH_Message_Sql_008 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);
    private String room_code;
    private String deviceGMT = ToolBox_Inf.getDeviceGMT(false);

    public CH_Message_Sql_008(String room_code) {
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "    m.*, \n" +
                        "strftime('%Y-%m-%d %H:%M:%S',msg_date,'"+deviceGMT+"') as msg_date_zone " +
                        " FROM \n" +
                        CH_MessageDao.TABLE + " m \n" +
                        " WHERE \n" +
                        "   m.room_code = '" + room_code + "' \n" +
                        "   and m.status_update = '1' \n" +
                        " ORDER BY\n" +
                        "   m.msg_pk asc\n")
                .append(";")
                //.append(HmAuxFields+"#msg_date_zone")
                .toString();
    }
}
