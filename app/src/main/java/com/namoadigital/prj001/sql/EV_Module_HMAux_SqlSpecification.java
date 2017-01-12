package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_ModuleDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/21/16.
 */

public class EV_Module_HMAux_SqlSpecification implements Specification {

    private String s_module_code;

    public EV_Module_HMAux_SqlSpecification(String s_module_code) {
        this.s_module_code = s_module_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" module_code, module_name from ")
                .append(EV_ModuleDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(EV_ModuleDao.MODULE_CODE)
                .append(" ='")
                .append(s_module_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(";")
                .append("module_code#module_name")
                .toString();
    }
}
