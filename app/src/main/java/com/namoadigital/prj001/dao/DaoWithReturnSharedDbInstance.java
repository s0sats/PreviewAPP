package com.namoadigital.prj001.dao;


import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.namoadigital.prj001.model.DaoObjReturn;

import java.util.List;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface DaoWithReturnSharedDbInstance<T> {

    DaoObjReturn addUpdate(T item, SQLiteDatabase dbInstance);

    DaoObjReturn addUpdate(List<T> items, boolean status, SQLiteDatabase dbInstance);

    DaoObjReturn remove(T item, @Nullable SQLiteDatabase dbInstance);

}
