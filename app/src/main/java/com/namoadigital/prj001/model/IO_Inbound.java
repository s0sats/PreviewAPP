package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class IO_Inbound implements Serializable {
    private static final long serialVersionUID = 1063674396776438810L;

    @Expose
    @SerializedName("customer_code") private long customer_code;
    @Expose
    @SerializedName("inbound_prefix") private int inbound_prefix;
    @Expose
    @SerializedName("inbound_code") private int inbound_code;
    @Expose
    @SerializedName("inbound_id") private String inbound_id;
    @Expose
    @SerializedName("inbound_desc") private String inbound_desc;
    @Expose
    @SerializedName("scn") private int scn;
    @Expose
    @SerializedName("origin") private String origin;
    @Expose
    @SerializedName("invoice_number") private String invoice_number;
    @Expose
    @SerializedName("invoice_date") private String invoice_date;
    @Expose
    @SerializedName("eta_date") private String eta_date;
    @Expose
    @SerializedName("arrival_date") private String arrival_date;
    @Expose
    @SerializedName("from_type") private String from_type;
    @Expose
    @SerializedName("from_partner_code") private Integer from_partner_code;
    @Expose
    @SerializedName("from_partner_id") private String from_partner_id;
    @Expose
    @SerializedName("from_partner_desc") private String from_partner_desc;
    @Expose
    @SerializedName("from_site_code") private Integer from_site_code;
    @Expose
    @SerializedName("from_site_id") private String from_site_id;
    @Expose
    @SerializedName("from_site_desc") private String from_site_desc;
    @Expose
    @SerializedName("to_site_code") private int to_site_code;
    @Expose
    @SerializedName("carrier_code") private Integer carrier_code;
    @SerializedName("carrier_id") private String carrier_id;
    @SerializedName("carrier_desc") private String carrier_desc;
    @Expose
    @SerializedName("truck_number") private String truck_number;
    @Expose
    @SerializedName("driver") private String driver;
    @Expose
    @SerializedName("comments") private String comments;
    @Expose
    @SerializedName("status") private String status;
    @Expose
    @SerializedName("perc_done") private Double perc_done;
    @Expose
    @SerializedName("inbound_auto_seq") private int inbound_auto_seq;
    @Expose
    @SerializedName("modal_code") private Integer modal_code;
    @SerializedName("modal_id") private String modal_id;
    @SerializedName("modal_desc") private String modal_desc;
    @Expose
    @SerializedName("allow_new_item") private int allow_new_item;
    @Expose
    @SerializedName("zone_code_conf") private Integer zone_code_conf;
    @SerializedName("zone_id_conf") private String zone_id_conf;
    @SerializedName("zone_desc_conf") private String zone_desc_conf;
    @Expose
    @SerializedName("local_code_conf") private Integer local_code_conf;
    @SerializedName("local_id_conf") private String local_id_conf;
    @Expose
    @SerializedName("put_away_process") private int put_away_process;
    @Expose
    @SerializedName("done_automatic") private int done_automatic;
    @Expose
    @SerializedName("items") private ArrayList<IO_Inbound_Item> items = new ArrayList<>();
    @Expose
    @SerializedName("update_required") private int update_required;
    @Expose
    @SerializedName("sync_required") private int sync_required;
    @Expose
    @SerializedName("token") private String token;
    @Expose
    //CAMPO EXCLUSIVO PARA RECEBIMENTO DO WS PROCESS DOWNLOAD
    @SerializedName("serial") private ArrayList<MD_Product_Serial> serial = new ArrayList<>();
    @Expose
    //CAMPO EXCLUSIVO PARA RECEBIMENTO DO WS PROCESS DOWNLOAD
    @SerializedName("move") private ArrayList<IO_Move> move = new ArrayList<>();
    //Campos exclusivos para envio na criação da Inbound
    @Expose
    @SerializedName("outbound_prefix") private Integer outbound_prefix;
    @Expose
    @SerializedName("outbound_code") private Integer outbound_code;
    @Expose
    @SerializedName("transport_order") private String transport_order;

    //Metodo necessario para repassar a pk do cabeçalho para o item
    //Como esse metodo, exugamos o tamanho do json enviado pelo server
    public void setPK(){
        for (IO_Inbound_Item inboundItem:items){
            inboundItem.setPK(this);
        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getInbound_prefix() {
        return inbound_prefix;
    }

    public void setInbound_prefix(int inbound_prefix) {
        this.inbound_prefix = inbound_prefix;
    }

    public int getInbound_code() {
        return inbound_code;
    }

    public void setInbound_code(int inbound_code) {
        this.inbound_code = inbound_code;
    }

    public String getInbound_id() {
        return inbound_id;
    }

    public void setInbound_id(String inbound_id) {
        this.inbound_id = inbound_id;
    }

    public String getInbound_desc() {
        return inbound_desc;
    }

    public void setInbound_desc(String inbound_desc) {
        this.inbound_desc = inbound_desc;
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

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getFrom_type() {
        return from_type;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public Integer getFrom_partner_code() {
        return from_partner_code;
    }

    public void setFrom_partner_code(Integer from_partner_code) {
        this.from_partner_code = from_partner_code;
    }

    public String getFrom_partner_id() {
        return from_partner_id;
    }

    public void setFrom_partner_id(String from_partner_id) {
        this.from_partner_id = from_partner_id;
    }

    public String getFrom_partner_desc() {
        return from_partner_desc;
    }

    public void setFrom_partner_desc(String from_partner_desc) {
        this.from_partner_desc = from_partner_desc;
    }

    public Integer getFrom_site_code() {
        return from_site_code;
    }

    public void setFrom_site_code(Integer from_site_code) {
        this.from_site_code = from_site_code;
    }

    public String getFrom_site_id() {
        return from_site_id;
    }

    public void setFrom_site_id(String from_site_id) {
        this.from_site_id = from_site_id;
    }

    public String getFrom_site_desc() {
        return from_site_desc;
    }

    public void setFrom_site_desc(String from_site_desc) {
        this.from_site_desc = from_site_desc;
    }

    public int getTo_site_code() {
        return to_site_code;
    }

    public void setTo_site_code(int to_site_code) {
        this.to_site_code = to_site_code;
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

    public Double getPerc_done() {
        return perc_done;
    }

    public void setPerc_done(Double perc_done) {
        this.perc_done = perc_done;
    }

    public int getInbound_auto_seq() {
        return inbound_auto_seq;
    }

    public void setInbound_auto_seq(int inbound_auto_seq) {
        this.inbound_auto_seq = inbound_auto_seq;
    }

    public Integer getModal_code() {
        return modal_code;
    }

    public void setModal_code(Integer modal_code) {
        this.modal_code = modal_code;
    }

    public String getModal_id() {
        return modal_id;
    }

    public void setModal_id(String modal_id) {
        this.modal_id = modal_id;
    }

    public String getModal_desc() {
        return modal_desc;
    }

    public void setModal_desc(String modal_desc) {
        this.modal_desc = modal_desc;
    }

    public int getAllow_new_item() {
        return allow_new_item;
    }

    public void setAllow_new_item(int allow_new_item) {
        this.allow_new_item = allow_new_item;
    }

    public Integer getZone_code_conf() {
        return zone_code_conf;
    }

    public void setZone_code_conf(Integer zone_code_conf) {
        this.zone_code_conf = zone_code_conf;
    }

    public String getZone_id_conf() {
        return zone_id_conf;
    }

    public void setZone_id_conf(String zone_id_conf) {
        this.zone_id_conf = zone_id_conf;
    }

    public String getZone_desc_conf() {
        return zone_desc_conf;
    }

    public void setZone_desc_conf(String zone_desc_conf) {
        this.zone_desc_conf = zone_desc_conf;
    }

    public Integer getLocal_code_conf() {
        return local_code_conf;
    }

    public void setLocal_code_conf(Integer local_code_conf) {
        this.local_code_conf = local_code_conf;
    }

    public String getLocal_id_conf() {
        return local_id_conf;
    }

    public void setLocal_id_conf(String local_id_conf) {
        this.local_id_conf = local_id_conf;
    }

    public int getPut_away_process() {
        return put_away_process;
    }

    public void setPut_away_process(int put_away_process) {
        this.put_away_process = put_away_process;
    }

    public int getDone_automatic() {
        return done_automatic;
    }

    public void setDone_automatic(int done_automatic) {
        this.done_automatic = done_automatic;
    }

    public ArrayList<IO_Inbound_Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<IO_Inbound_Item> items) {
        this.items = items;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public int getSync_required() {
        return sync_required;
    }

    public void setSync_required(int sync_required) {
        this.sync_required = sync_required;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<MD_Product_Serial> getSerial() {
        return serial;
    }

    public void setSerial(ArrayList<MD_Product_Serial> serial) {
        this.serial = serial;
    }

    public ArrayList<IO_Move> getMove() {
        return move;
    }

    public void setMove(ArrayList<IO_Move> move) {
        this.move = move;
    }

    public Integer getOutbound_prefix() {
        return outbound_prefix;
    }

    public void setOutbound_prefix(Integer outbound_prefix) {
        this.outbound_prefix = outbound_prefix;
    }

    public Integer getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(Integer outbound_code) {
        this.outbound_code = outbound_code;
    }

    public String getTransport_order() {
        return transport_order;
    }

    public void setTransport_order(String transport_order) {
        this.transport_order = transport_order;
    }
}
