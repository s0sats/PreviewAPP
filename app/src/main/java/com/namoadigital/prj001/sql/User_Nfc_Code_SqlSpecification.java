package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class User_Nfc_Code_SqlSpecification implements Specification {

    private String s_Nfc_Code;

    public User_Nfc_Code_SqlSpecification(String s_Nfc_Code) {
        this.s_Nfc_Code = s_Nfc_Code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(UserDao.TABLE)
                .append(" where ")
                .append(UserDao.NFC_CODE)
                .append(" ='")
                .append(s_Nfc_Code)
                .append("'")
                .append(";")
                .toString();
    }
}
