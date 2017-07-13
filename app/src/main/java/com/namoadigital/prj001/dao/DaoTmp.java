package com.namoadigital.prj001.dao;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoTmp<T> extends Dao<T> {

    void addUpdateServer(T item);

    void addUpdateServer(Iterable<T> items, boolean status);

}
