package com.namoadigital.prj001.dao;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoSOFullDelete<T> extends Dao<T> {

    void removeFull(T item);

}
