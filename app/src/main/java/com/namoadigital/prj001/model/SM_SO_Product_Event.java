package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 30/10/2017.
 */

public class SM_SO_Product_Event {

    private long customer_code; //pk
    private int so_prefix; //pk
    private int so_code; //pk
    @Expose
    private int seq; //pk - server
    @Expose
    private int seq_tmp; //pk
    //
    @Expose
    private int product_code;
    @Expose
    private String product_id;
    @Expose
    private String product_desc;
    @Expose
    private String un;
    @Expose
    private int flag_apply;
    @Expose
    private int flag_inspection;
    @Expose
    private int flag_repair;
    @Expose
    private String qty_apply;
    @Expose
    private Integer sketch_code;
    private String sketch_name;
    private String sketch_url;
    private String sketch_url_local;
    @Expose
    private Integer sketch_lines;
    @Expose
    private Integer sketch_columns;
    @Expose
    private String sketch_color;
    @Expose
    private String comments;
    @Expose
    private String status;
    private String create_date;
    private int create_user;
    private String create_user_nick;
    @Expose
    private String done_date;
    @Expose
    private Integer done_user;
    @Expose
    private String done_user_nick;
    @Expose
    private int integrated;
    @Expose
    private ArrayList<SM_SO_Product_Event_File> file = new ArrayList<>();
    @Expose
    private ArrayList<SM_SO_Product_Event_Sketch> sketch = new ArrayList<>();

    public SM_SO_Product_Event() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.seq = -1;
    }

    public void setPK(SM_SO so) {
        this.customer_code = so.getCustomer_code();
        this.so_prefix = so.getSo_prefix();
        this.so_code = so.getSo_code();

        for (int i = 0; i < file.size() ; i++) {
            file.get(i).setPK(this);
        }

        for (int i = 0; i < sketch.size() ; i++) {
            sketch.get(i).setPK(this);

        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(int so_prefix) {
        this.so_prefix = so_prefix;
    }

    public int getSo_code() {
        return so_code;
    }

    public void setSo_code(int so_code) {
        this.so_code = so_code;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSeq_tmp() {
        return seq_tmp;
    }

    public void setSeq_tmp(int seq_tmp) {
        this.seq_tmp = seq_tmp;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public int getFlag_apply() {
        return flag_apply;
    }

    public void setFlag_apply(int flag_apply) {
        this.flag_apply = flag_apply;
    }

    public int getFlag_inspection() {
        return flag_inspection;
    }

    public void setFlag_inspection(int flag_inspection) {
        this.flag_inspection = flag_inspection;
    }

    public int getFlag_repair() {
        return flag_repair;
    }

    public void setFlag_repair(int flag_repair) {
        this.flag_repair = flag_repair;
    }

    public String getQty_apply() {
        return qty_apply;
    }

    public void setQty_apply(String qty_apply) {
        this.qty_apply = qty_apply;
    }

    public Integer getSketch_code() {
        return sketch_code;
    }

    public void setSketch_code(Integer sketch_code) {
        this.sketch_code = sketch_code;
    }

    public String getSketch_name() {
        return sketch_name;
    }

    public void setSketch_name(String sketch_name) {
        this.sketch_name = sketch_name;
    }

    public String getSketch_url() {
        return sketch_url;
    }

    public void setSketch_url(String sketch_url) {
        this.sketch_url = sketch_url;
    }

    public String getSketch_url_local() {
        return sketch_url_local;
    }

    public void setSketch_url_local(String sketch_url_local) {
        this.sketch_url_local = sketch_url_local;
    }

    public Integer getSketch_lines() {
        return sketch_lines;
    }

    public void setSketch_lines(Integer sketch_lines) {
        this.sketch_lines = sketch_lines;
    }

    public Integer getSketch_columns() {
        return sketch_columns;
    }

    public void setSketch_columns(Integer sketch_columns) {
        this.sketch_columns = sketch_columns;
    }

    public String getSketch_color() {
        return sketch_color;
    }

    public void setSketch_color(String sketch_color) {
        this.sketch_color = sketch_color;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getCreate_user() {
        return create_user;
    }

    public void setCreate_user(int create_user) {
        this.create_user = create_user;
    }

    public String getCreate_user_nick() {
        return create_user_nick;
    }

    public void setCreate_user_nick(String create_user_nick) {
        this.create_user_nick = create_user_nick;
    }

    public String getDone_date() {
        return done_date;
    }

    public void setDone_date(String done_date) {
        this.done_date = done_date;
    }

    public Integer getDone_user() {
        return done_user;
    }

    public void setDone_user(Integer done_user) {
        this.done_user = done_user;
    }

    public String getDone_user_nick() {
        return done_user_nick;
    }

    public void setDone_user_nick(String done_user_nick) {
        this.done_user_nick = done_user_nick;
    }

    public int getIntegrated() {
        return integrated;
    }

    public void setIntegrated(int integrated) {
        this.integrated = integrated;
    }

    public ArrayList<SM_SO_Product_Event_File> getFile() {
        return file;
    }

    public void setFile(ArrayList<SM_SO_Product_Event_File> file) {
        this.file = file;
    }

    public ArrayList<SM_SO_Product_Event_Sketch> getSketch() {
        return sketch;
    }

    public void setSketch(ArrayList<SM_SO_Product_Event_Sketch> sketch) {
        this.sketch = sketch;
    }
}
