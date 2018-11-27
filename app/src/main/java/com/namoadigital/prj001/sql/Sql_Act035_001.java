package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 23/02/17.
 *
 *
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 */

public class Sql_Act035_001 implements Specification {

    private String room_code;
    private String offSet;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);
    private String deviceGMT = ToolBox_Inf.getDeviceGMT(false);

    public Sql_Act035_001(String room_code, String offSet) {
        this.room_code = room_code;
        this.offSet = offSet;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();



        /*

select msg_pk, msg_prefix, msg_code, tmp from ch_messages
where room_code = '2017.J'
order by case when msg_pk is null then 1 else 0 end, msg_pk, msg_prefix , tmp limit 50 offset (select count(*) from ch_messages where (room_code = '2017.J' ))-50


         */

        return sb
                .append(" SELECT\n" +
                        "      *\n," +
                        "strftime('%Y-%m-%d %H:%M:%S',msg_date,'"+deviceGMT+"') as msg_date_zone \n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE + " \n" +
                        " WHERE\n" +
                        "    room_code =  '" + room_code + "'\n" +
                        " ORDER BY \n" +
                        "   case when msg_pk is null \n" +
                        "       then 1 \n" +
                        "       else 0 \n" +
                        "   end, \n" +
                        "   msg_pk,\n" +
                        "   tmp limit " + offSet + " offset (select count(*) from ch_messages where (room_code = '" + room_code + "' ))-" + offSet)
                .append(";")
                //.append(HmAuxFields+"#msg_date_zone")
                .toString();
    }
}
