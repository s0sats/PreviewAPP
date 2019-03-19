package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class IO_Outbound {

    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private String outbound_desc;
    private String outbound_id;
    private int scn;
    private String origin;
    private String invoice_number;
    private String invoice_date;
    private String eta_date;
    private String departure_date;
    private String loading_date;
    private int from_site_code;
    private String to_type;
    private Integer to_partner_code;
    private String to_partner_id;
    private String to_partner_desc;
    private Integer to_site_code;
    private String to_site_id;
    private String to_site_desc;
    private Integer carrier_code;
    private String carrier_id;
    private String carrier_desc;
    private String truck_number;
    private String driver;
    private String comments;
    private String status;
    private Integer modal_code;
    private int allow_new_item;
    private Integer zone_code_picking;
    private Integer local_code_picking;
    private int picking_process;
    private int done_automatic;
    private ArrayList<IO_Outbound_Item> items = new ArrayList<>();

    //Metodo necessario para repassar a pk do cabeçalho para o item
    //Como esse metodo, exugamos o tamanho do json enviado pelo server
    public void setPK(){
        for (IO_Outbound_Item ioOutboundItem:items){
            ioOutboundItem.setPK(this);
        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getOutbound_prefix() {
        return outbound_prefix;
    }

    public void setOutbound_prefix(int outbound_prefix) {
        this.outbound_prefix = outbound_prefix;
    }

    public int getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(int outbound_code) {
        this.outbound_code = outbound_code;
    }

    public String getOutbound_desc() {
        return outbound_desc;
    }

    public void setOutbound_desc(String outbound_desc) {
        this.outbound_desc = outbound_desc;
    }

    public String getOutbound_id() {
        return outbound_id;
    }

    public void setOutbound_id(String outbound_id) {
        this.outbound_id = outbound_id;
    }

    public int getScn() {
        return scn;
    }

    public void setScn(int scn) {
        this.scn = scn;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getEta_date() {
        return eta_date;
    }

    public void setEta_date(String eta_date) {
        this.eta_date = eta_date;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getLoading_date() {
        return loading_date;
    }

    public void setLoading_date(String loading_date) {
        this.loading_date = loading_date;
    }

    public int getFrom_site_code() {
        return from_site_code;
    }

    public void setFrom_site_code(int from_site_code) {
        this.from_site_code = from_site_code;
    }

    public String getTo_type() {
        return to_type;
    }

    public void setTo_type(String to_type) {
        this.to_type = to_type;
    }

    public Integer getTo_partner_code() {
        return to_partner_code;
    }

    public void setTo_partner_code(Integer to_partner_code) {
        this.to_partner_code = to_partner_code;
    }

    public String getTo_partner_id() {
        return to_partner_id;
    }

    public void setTo_partner_id(String to_partner_id) {
        this.to_partner_id = to_partner_id;
    }

    public String getTo_partner_desc() {
        return to_partner_desc;
    }

    public void setTo_partner_desc(String to_partner_desc) {
        this.to_partner_desc = to_partner_desc;
    }

    public Integer getTo_site_code() {
        return to_site_code;
    }

    public void setTo_site_code(Integer to_site_code) {
        this.to_site_code = to_site_code;
    }

    public String getTo_site_id() {
        return to_site_id;
    }

    public void setTo_site_id(String to_site_id) {
        this.to_site_id = to_site_id;
    }

    public String getTo_site_desc() {
        return to_site_desc;
    }

    public void setTo_site_desc(String to_site_desc) {
        this.to_site_desc = to_site_desc;
    }

    public Integer getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(Integer carrier_code) {
        this.carrier_code = carrier_code;
    }

    public String getCarrier_id() {
        return carrier_id;
    }

    public void setCarrier_id(String carrier_id) {
        this.carrier_id = carrier_id;
    }

    public String getCarrier_desc() {
        return carrier_desc;
    }

    public void setCarrier_desc(String carrier_desc) {
        this.carrier_desc = carrier_desc;
    }

    public String getTruck_number() {
        return truck_number;
    }

    public void setTruck_number(String truck_number) {
        this.truck_number = truck_number;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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

    public Integer getModal_code() {
        return modal_code;
    }

    public void setModal_code(Integer modal_code) {
        this.modal_code = modal_code;
    }

    public int getAllow_new_item() {
        return allow_new_item;
    }

    public void setAllow_new_item(int allow_new_item) {
        this.allow_new_item = allow_new_item;
    }

    public Integer getZone_code_picking() {
        return zone_code_picking;
    }

    public void setZone_code_picking(Integer zone_code_picking) {
        this.zone_code_picking = zone_code_picking;
    }

    public Integer getLocal_code_picking() {
        return local_code_picking;
    }

    public void setLocal_code_picking(Integer local_code_picking) {
        this.local_code_picking = local_code_picking;
    }

    public int getPicking_process() {
        return picking_process;
    }

    public void setPicking_process(int picking_process) {
        this.picking_process = picking_process;
    }

    public int getDone_automatic() {
        return done_automatic;
    }

    public void setDone_automatic(int done_automatic) {
        this.done_automatic = done_automatic;
    }

    public ArrayList<IO_Outbound_Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<IO_Outbound_Item> items) {
        this.items = items;
    }
}
