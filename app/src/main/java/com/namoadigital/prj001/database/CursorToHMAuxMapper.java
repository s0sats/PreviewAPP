package com.namoadigital.prj001.database;

import android.database.Cursor;

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

        String cFF[] = fields.split("#");

        if (cFF != null) {
            for (int i = 0; i < cFF.length; i++) {
                String sAux = cFF[i];
                //
                hmAux.put(
                        sAux,
                        cursor.getString(cursor.getColumnIndex(cFF[i]))
                );
            }

            return hmAux;

        } else {

            return null;
        }


    }
}
