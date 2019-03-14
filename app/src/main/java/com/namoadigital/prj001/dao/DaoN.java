package com.namoadigital.prj001.dao;


import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.DaoObjReturn;

import java.util.List;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoN<T> {

    void addUpdate(T item, DaoObjReturn mDaoError);

    void addUpdate(List<T> items, boolean status, DaoObjReturn mDaoError);

    void addUpdate(String sQuery, DaoObjReturn mDaoError);

    void remove(String sQuery, DaoObjReturn mDaoError);

    T getByString(String sQuery);

    HMAux getByStringHM(String sQuery);

    List<T> query(String sQuery);

    List<HMAux> query_HM(String sQuery);



}
