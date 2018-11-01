package com.namoadigital.prj001.database;

import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 09/01/17.
 */

public class CursorToHMAuxMapper implements Mapper<Cursor, HMAux> {

    private String fields;

    public CursorToHMAuxMapper(String fields) {
        this.fields = fields;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    @Override
    public HMAux map(Cursor cursor) {
        HMAux hmAux = new HMAux();

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            hmAux.put(
                    cursor.getColumnName(i),
                    cursor.isNull(cursor.getColumnIndex(cursor.getColumnName(i))) ? "" : cursor.getString(i)
            );
        }

        if (hmAux.size() > 0) {
            return hmAux;
        } else {
            return null;
        }
    }

    public static HMAux mapN(Cursor cursor) {

        HMAux hmAux = new HMAux();

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            hmAux.put(
                    cursor.getColumnName(i),
                    cursor.isNull(cursor.getColumnIndex(cursor.getColumnName(i))) ? "" : cursor.getString(i)
            );
        }

        if (hmAux.size() > 0) {
            return hmAux;
        } else {
            return null;
        }
    }
}
