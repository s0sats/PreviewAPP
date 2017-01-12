package com.namoadigital.prj001.dao;

import java.util.List;


/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoI<T> extends Dao<T> {

    List<T> query_Custom(String sQuery);

}
