package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by neomatrix on 8/9/16.
 */

public class MD_Product_Serial implements Serializable {
    private static final long serialVersionUID = 4954240719813141624L;

    @Expose
    private long customer_code;
    @Expose
    private long product_code;
    private String product_id;
    private String product_desc;
    @Expose
    private long serial_code;
    @Expose
    private long serial_tmp;
    @Expose
    private String serial_id;
    @Expose
    private Integer site_code;
    private String site_id;
    private String site_desc;
    @Expose
    private Integer zone_code;
    private String zone_id;
    private String zone_desc;
    @Expose
    private Integer local_code;
    private String local_id;
    @Expose
    private Integer site_code_owner;
    @Expose
    private Integer brand_code;
    private String brand_id;
    private String brand_desc;
    @Expose
    private Integer model_code;
    private String model_id;
    private String model_desc;
    @Expose
    private Integer color_code;
    private String color_id;
    private String color_desc;
    @Expose
    private Integer segment_code;
    private String segment_id;
    private String segment_desc;
    @Expose
    private Integer category_price_code;
    private String category_price_id;
    private String category_price_desc;
    @Expose
    private String add_inf1;
    @Expose
    private String add_inf2;
    @Expose
    private String add_inf3;
    @Expose
    private int update_required;
    //SOMENTE PARA ENVIO NO WS
    @Expose
    private Integer only_position;
    @Expose
    private ArrayList<MD_Product_Serial_Tracking> tracking_list = new ArrayList<>();
    private int flag_offline;
    private int sync_process;
    @Expose
    private Integer class_code;
    private String class_id;
    private String class_type;
    private String class_color;
    private Integer class_available;
    private Integer inbound_code;
    private String inbound_id;
    private String inbound_conf_date;
    private Integer move_prefix;
    private Integer move_code;
    private Integer move_group_code;
    private Integer outbound_code;
    private String outbound_id;
    private Integer product_io_control;
    private Integer local_control;
    private Integer site_io_control;
    private Integer inbound_auto_create;
    private Integer site_restriction;
    @Expose
    private String edit_mode;
    @Expose
    private String profile;
    @Expose
    private String log_date;

    /*
    * Add contrutor com inicialização de -1 no serial temp para diferenciar dos seriais criados
    * via app.
    */
    public MD_Product_Serial() {
        this.serial_tmp = 0;
    }

    public void setPk(){
        for (int i = 0; i < tracking_list.size(); i++) {
            tracking_list.get(i).setPk(this);
        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
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

    public long getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(long serial_code) {
        this.serial_code = serial_code;
    }

    public long getSerial_tmp() {
        return serial_tmp;
    }

    public void setSerial_tmp(long serial_tmp) {
        this.serial_tmp = serial_tmp;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public Integer getSite_code() {
        return site_code;
    }

    public void setSite_code(Integer site_code) {
        this.site_code = site_code;
    }

    public Integer getZone_code() {
        return zone_code;
    }

    public void setZone_code(Integer zone_code) {
        this.zone_code = zone_code;
    }

    public Integer getLocal_code() {
        return local_code;
    }

    public void setLocal_code(Integer local_code) {
        this.local_code = local_code;
    }

    public Integer getSite_code_owner() {
        return site_code_owner;
    }

    public void setSite_code_owner(Integer site_code_owner) {
        this.site_code_owner = site_code_owner;
    }

    public Integer getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(Integer brand_code) {
        this.brand_code = brand_code;
    }

    public Integer getModel_code() {
        return model_code;
    }

    public void setModel_code(Integer model_code) {
        this.model_code = model_code;
    }

    public Integer getColor_code() {
        return color_code;
    }

    public void setColor_code(Integer color_code) {
        this.color_code = color_code;
    }

    public Integer getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(Integer segment_code) {
        this.segment_code = segment_code;
    }

    public Integer getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(Integer category_price_code) {
        this.category_price_code = category_price_code;
    }

    public String getAdd_inf1() {
        return add_inf1;
    }

    public void setAdd_inf1(String add_inf1) {
        this.add_inf1 = add_inf1;
    }

    public String getAdd_inf2() {
        return add_inf2;
    }

    public void setAdd_inf2(String add_inf2) {
        this.add_inf2 = add_inf2;
    }

    public String getAdd_inf3() {
        return add_inf3;
    }

    public void setAdd_inf3(String add_inf3) {
        this.add_inf3 = add_inf3;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public Integer getOnly_position() {
        return only_position;
    }

    public void setOnly_position(Integer only_position) {
        this.only_position = only_position;
    }

    public ArrayList<MD_Product_Serial_Tracking> getTracking_list() {
        return tracking_list;
    }

    public void setTracking_list(ArrayList<MD_Product_Serial_Tracking> tracking_list) {
        this.tracking_list = tracking_list;
    }

    public int getFlag_offline() {
        return flag_offline;
    }

    public void setFlag_offline(int flag_offline) {
        this.flag_offline = flag_offline;
    }

    public int getSync_process() {
        return sync_process;
    }

    public void setSync_process(int sync_process) {
        this.sync_process = sync_process;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getZone_desc() {
        return zone_desc;
    }

    public void setZone_desc(String zone_desc) {
        this.zone_desc = zone_desc;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_desc() {
        return brand_desc;
    }

    public void setBrand_desc(String brand_desc) {
        this.brand_desc = brand_desc;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getModel_desc() {
        return model_desc;
    }

    public void setModel_desc(String model_desc) {
        this.model_desc = model_desc;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getColor_desc() {
        return color_desc;
    }

    public void setColor_desc(String color_desc) {
        this.color_desc = color_desc;
    }

    public String getSegment_id() {
        return segment_id;
    }

    public void setSegment_id(String segment_id) {
        this.segment_id = segment_id;
    }

    public String getSegment_desc() {
        return segment_desc;
    }

    public void setSegment_desc(String segment_desc) {
        this.segment_desc = segment_desc;
    }

    public String getCategory_price_id() {
        return category_price_id;
    }

    public void setCategory_price_id(String category_price_id) {
        this.category_price_id = category_price_id;
    }

    public String getCategory_price_desc() {
        return category_price_desc;
    }

    public void setCategory_price_desc(String category_price_desc) {
        this.category_price_desc = category_price_desc;
    }

    public Integer getClass_code() {
        return class_code;
    }

    public void setClass_code(Integer class_code) {
        this.class_code = class_code;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_type() {
        return class_type;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
    }

    public String getClass_color() {
        return class_color;
    }

    public void setClass_color(String class_color) {
        this.class_color = class_color;
    }

    public Integer getClass_available() {
        return class_available;
    }

    public void setClass_available(Integer class_available) {
        this.class_available = class_available;
    }

    public Integer getInbound_code() {
        return inbound_code;
    }

    public void setInbound_code(Integer inbound_code) {
        this.inbound_code = inbound_code;
    }

    public String getInbound_id() {
        return inbound_id;
    }

    public void setInbound_id(String inbound_id) {
        this.inbound_id = inbound_id;
    }

    public String getInbound_conf_date() {
        return inbound_conf_date;
    }

    public void setInbound_conf_date(String inbound_conf_date) {
        this.inbound_conf_date = inbound_conf_date;
    }

    public Integer getMove_prefix() {
        return move_prefix;
    }

    public void setMove_prefix(Integer move_prefix) {
        this.move_prefix = move_prefix;
    }

    public Integer getMove_code() {
        return move_code;
    }

    public void setMove_code(Integer move_code) {
        this.move_code = move_code;
    }

    public Integer getMove_group_code() {
        return move_group_code;
    }

    public void setMove_group_code(Integer move_group_code) {
        this.move_group_code = move_group_code;
    }

    public Integer getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(Integer outbound_code) {
        this.outbound_code = outbound_code;
    }

    public String getOutbound_id() {
        return outbound_id;
    }

    public void setOutbound_id(String outbound_id) {
        this.outbound_id = outbound_id;
    }

    public Integer getProduct_io_control() {
        return product_io_control;
    }

    public void setProduct_io_control(Integer product_io_control) {
        this.product_io_control = product_io_control;
    }

    public Integer getLocal_control() {
        return local_control;
    }

    public void setLocal_control(Integer local_control) {
        this.local_control = local_control;
    }

    public Integer getSite_io_control() {
        return site_io_control;
    }

    public void setSite_io_control(Integer site_io_control) {
        this.site_io_control = site_io_control;
    }

    public Integer getInbound_auto_create() {
        return inbound_auto_create;
    }

    public void setInbound_auto_create(Integer inbound_auto_create) {
        this.inbound_auto_create = inbound_auto_create;
    }

    public Integer getSite_restriction() {
        return site_restriction;
    }

    public void setSite_restriction(Integer site_restriction) {
        this.site_restriction = site_restriction;
    }

    public String getEdit_mode() {
        return edit_mode;
    }

    public void setEdit_mode(String edit_mode) {
        this.edit_mode = edit_mode;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    /**
     * Monta Obj TProduct_Serial baseado nas informações da classe.
     * @return
     */
    public TProduct_Serial getTProductSerial(){
        TProduct_Serial tProduct_serial = new TProduct_Serial();

        tProduct_serial.setCustomer_code(this.customer_code);
        tProduct_serial.setProduct_code(this.product_code);
        tProduct_serial.setSerial_code(this.serial_code);
        tProduct_serial.setSerial_id(this.serial_id);
        tProduct_serial.setSite_code(this.site_code);
        tProduct_serial.setZone_code(this.zone_code);
        tProduct_serial.setLocal_code(this.local_code);
        tProduct_serial.setSite_code_owner(this.site_code_owner);
        tProduct_serial.setBrand_code(this.brand_code);
        tProduct_serial.setModel_code(this.model_code);
        tProduct_serial.setColor_code(this.color_code);
        tProduct_serial.setSegment_code(this.segment_code);
        tProduct_serial.setCategory_price_code(this.category_price_code);
        tProduct_serial.setAdd_inf1(this.add_inf1);
        tProduct_serial.setAdd_inf2(this.add_inf2);
        tProduct_serial.setAdd_inf3(this.add_inf3);
        tProduct_serial.setTracking_list(this.tracking_list);

        return tProduct_serial;
    }
}
