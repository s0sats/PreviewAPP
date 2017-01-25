package com.namoadigital.prj001.dao;

import java.util.List;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoProduct<T> extends Dao<T> {

    //List<T> query_Custom(String sQuery);

    List<Long> query_Custom_Product_Code(String sQuery);

}
