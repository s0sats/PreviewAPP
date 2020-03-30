package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 13/02/2017.
 */

public class GE_Custom_Form_Data_Sql_001 implements Specification {

    private long s_customer_code;
    private int s_pending;

    public GE_Custom_Form_Data_Sql_001(long s_customer_code, int s_pending) {
        this.s_customer_code = s_customer_code;
        this.s_pending = s_pending;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        String tokenFilter = "";
        //Baseado no parameto, define se busca novos token
        // ou token pendente
        if(s_pending == 1){
            tokenFilter =
                    "       AND F."+GE_Custom_Form_DataDao.TOKEN+" != ''";
        }else{
            tokenFilter =
                    "       AND F."+GE_Custom_Form_DataDao.TOKEN+" = ''";
        }
        
        return sb
                .append(
                        " SELECT " +
                        "     F.*  " +
                        "  FROM " +
                        GE_Custom_Form_DataDao.TABLE +" F " +
                        "  WHERE " +
                        "      F."+GE_Custom_Form_DataDao.CUSTOMER_CODE+" = '"+s_customer_code+"'"+
                        "      AND F."+GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS+ "  = '" + Constant.SYS_STATUS_WAITING_SYNC+"' " +
                                tokenFilter +
                                "AND F."+GE_Custom_Form_DataDao.LOCATION_PENDENCY  + " = 0"+
                        "  ORDER BY " +
                        "      F.customer_code,  " +
                        "      F.custom_form_type,  " +
                        "      F.custom_form_code,  " +
                        "      F.custom_form_version,  " +
                        "      F.custom_form_data ;"
                )
                .toString();
    }
}
