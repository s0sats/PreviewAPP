package com.namoadigital.prj001.dao;

/**
 * Created by DANIEL.LUCHE on 07/04/2017.
 */

public interface DaoFormLocal<T> extends Dao<T> {

    void remove(Iterable<T> items);

}
