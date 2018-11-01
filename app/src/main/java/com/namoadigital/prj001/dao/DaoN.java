package com.namoadigital.prj001.dao;


import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.ErrorCfg;

import java.util.List;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoN<T> {

    void addUpdate(T item, ErrorCfg mError);

    void addUpdate(List<T> items, boolean status, ErrorCfg mError);

    void addUpdate(String sQuery, ErrorCfg mError);

    void remove(String sQuery, ErrorCfg mError);

    T getByString(String sQuery, ErrorCfg mError);

    HMAux getByStringHM(String sQuery, ErrorCfg mError);

    List<T> query(String sQuery, ErrorCfg mError);

    List<HMAux> query_HM(String sQuery, ErrorCfg mError);



}
