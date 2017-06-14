package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 14/06/2017.
 */

public class SM_Env {

    @Expose
    private SM_SO so;

    public SM_SO getSo() {
        return so;
    }

    public void setSo(SM_SO so) {
        this.so = so;
    }
}
