package com.namoadigital.prj001.dao;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoTmpStatus<T> extends DaoTmp<T> {

    void addUpdateTmp(T item);

    void addUpdateTmp(Iterable<T> items, boolean status);

    void updateStatusOffLine(T item);

}
