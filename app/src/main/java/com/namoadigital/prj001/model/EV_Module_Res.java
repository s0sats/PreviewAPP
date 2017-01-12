package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 5/9/16.
 */
public class EV_Module_Res {

    //chave
    private String module_code;
    //chave
    private int resource_code;
    private String resource_name;

    public String getModule_code() {
        return module_code;
    }

    public void setModule_code(String module_code) {
        this.module_code = module_code;
    }

    public int getResource_code() {
        return resource_code;
    }

    public void setResource_code(int resource_code) {
        this.resource_code = resource_code;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }
}
