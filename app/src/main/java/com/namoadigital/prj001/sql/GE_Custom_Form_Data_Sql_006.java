package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/*
    BARRIONUEVO - 19-02-2020
    Query responsavel por atualizar os formularios finalizados com localizacao pendente.
    (1= para ha pendencia e 0 para nao ha pendencia)
*/

public class GE_Custom_Form_Data_Sql_006 implements Specification {
    private long s_customer_code;
    private String s_location_type;
    private String s_location_lat;
    private String s_location_lng;

    public GE_Custom_Form_Data_Sql_006(long s_customer_code, String s_location_type, String s_location_lat, String s_location_lng) {
        this.s_customer_code = s_customer_code;
        this.s_location_type = s_location_type;
        this.s_location_lat = s_location_lat;
        this.s_location_lng = s_location_lng;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " +GE_Custom_Form_DataDao.TABLE +" SET \n" +
                        GE_Custom_Form_DataDao.LOCATION_LAT+" = '"+s_location_lat + "'\n ," +
                        GE_Custom_Form_DataDao.LOCATION_LNG+" = '"+s_location_lng + "'\n ," +
                        GE_Custom_Form_DataDao.LOCATION_TYPE+" = '"+s_location_type + "'\n," +
                        GE_Custom_Form_DataDao.LOCATION_PENDENCY+" = 0" +
                        " WHERE\n" +
                        "      "+GE_Custom_Form_DataDao.CUSTOMER_CODE+" = '"+s_customer_code + "'" +
                        "      AND "+GE_Custom_Form_DataDao.LOCATION_PENDENCY  + " = 1"+
                        "      AND "+GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS +
                                        "  = '" + Constant.SYS_STATUS_FINALIZED+"' ;"
//                        tokenFilter + ";"
                ).toString();
    }
}
