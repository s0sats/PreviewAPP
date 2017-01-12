package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 5/9/16.
 */
public class EV_Module_Res_Txt_Trans {

    //chave
    private String module_code;
    //chave
    private int resource_code;
    //chave
    private String txt_code;
    //chave
    private int translate_code;
    private String txt_value;

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

    public String getTxt_code() {
        return txt_code;
    }

    public void setTxt_code(String txt_code) {
        this.txt_code = txt_code;
    }

    public int getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(int translate_code) {
        this.translate_code = translate_code;
    }

    public String getTxt_value() {
        return txt_value;
    }

    public void setTxt_value(String txt_value) {
        this.txt_value = txt_value;
    }
}
