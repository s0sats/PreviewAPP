package com.namoadigital.prj001.dao;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 08/02/17.
 */

public interface DaoLocal <T> extends Dao<T> {

    void addUpdate(Iterable<HMAux> items);

}
