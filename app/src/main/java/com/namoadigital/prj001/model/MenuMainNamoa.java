package com.namoadigital.prj001.model;

public class MenuMainNamoa {
    private String menu_id;//Id do app
    private String menu_code;//Constant, valor do server
    private String menu_desc;
    private String icon;
    private int badge1;
    private int badge2;

    public MenuMainNamoa(String menu_id, String menu_code, String menu_desc, String icon) {
        this.menu_id = menu_id;
        this.menu_code = menu_code;
        this.menu_desc = menu_desc;
        this.icon = icon;
        this.badge1 = 0;
        this.badge2 = 0 ;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }

    public String getMenu_desc() {
        return menu_desc;
    }

    public void setMenu_desc(String menu_desc) {
        this.menu_desc = menu_desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getBadge1() {
        return badge1;
    }

    public void setBadge1(int badge1) {
        this.badge1 = badge1;
    }

    public int getBadge2() {
        return badge2;
    }

    public void setBadge2(int badge2) {
        this.badge2 = badge2;
    }

    public int addInBadge1(int num){
        this.badge1 += num;
        return badge1;
    }

    public int addInBadge2(int num){
        this.badge2 += num;
        return badge2;
    }
}
