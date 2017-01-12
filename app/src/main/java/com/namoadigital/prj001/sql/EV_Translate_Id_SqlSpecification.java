package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_TranslateDao;
import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.model.EV_Translate;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_Translate_Id_SqlSpecification implements Specification {

    private String s_EV_Translate_Code;

    public EV_Translate_Id_SqlSpecification(String s_EV_Translate_Code) {
        this.s_EV_Translate_Code = s_EV_Translate_Code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(EV_TranslateDao.TABLE)
                .append(" where ")
                .append(EV_TranslateDao.TRANSLATE_CODE)
                .append(" ='")
                .append(s_EV_Translate_Code)
                .append("'")
                .append(";")
                .toString();
    }
}
