package com.namoadigital.prj001.dao;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoTmp<T> extends Dao<T> {

    void addUpdateTmp(T item);

    void addUpdateTmp(Iterable<T> items, boolean status);

}
