package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Serial_Process_Download_Move {

    private Long customer_code;
    private Integer move_prefix;
    private Integer move_code;
    private Long product_code;
    private Integer serial_code;
    private Integer site_code;
    private Integer from_zone_code;
    private Integer from_local_code;
    private Integer from_class_code;
    private Integer planned_zone_code;
    private Integer planned_local_code;
    private Integer planned_class_code;
    private Integer to_zone_code;
    private Integer to_local_code;
    private Integer to_class_code;
    private String move_type;
    private Integer reason_code;
    private Integer inbound_prefix;
    private Integer inbound_code;
    private Integer inbound_item;
    private Integer outbound_prefix;
    private Integer outbound_code;
    private Integer outbound_item;
    private String done_date;
    private Integer done_user;
    private String done_user_nick;
    private String status;
    private ArrayList<MD_Product_Serial> serial;

    public Long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(Long customer_code) {
        this.customer_code = customer_code;
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

    public Long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(Long product_code) {
        this.product_code = product_code;
    }

    public Integer getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(Integer serial_code) {
        this.serial_code = serial_code;
    }

    public Integer getSite_code() {
        return site_code;
    }

    public void setSite_code(Integer site_code) {
        this.site_code = site_code;
    }

    public Integer getFrom_zone_code() {
        return from_zone_code;
    }

    public void setFrom_zone_code(Integer from_zone_code) {
        this.from_zone_code = from_zone_code;
    }

    public Integer getFrom_local_code() {
        return from_local_code;
    }

    public void setFrom_local_code(Integer from_local_code) {
        this.from_local_code = from_local_code;
    }

    public Integer getFrom_class_code() {
        return from_class_code;
    }

    public void setFrom_class_code(Integer from_class_code) {
        this.from_class_code = from_class_code;
    }

    public Integer getPlanned_zone_code() {
        return planned_zone_code;
    }

    public void setPlanned_zone_code(Integer planned_zone_code) {
        this.planned_zone_code = planned_zone_code;
    }

    public Integer getPlanned_local_code() {
        return planned_local_code;
    }

    public void setPlanned_local_code(Integer planned_local_code) {
        this.planned_local_code = planned_local_code;
    }

    public Integer getPlanned_class_code() {
        return planned_class_code;
    }

    public void setPlanned_class_code(Integer planned_class_code) {
        this.planned_class_code = planned_class_code;
    }

    public Integer getTo_zone_code() {
        return to_zone_code;
    }

    public void setTo_zone_code(Integer to_zone_code) {
        this.to_zone_code = to_zone_code;
    }

    public Integer getTo_local_code() {
        return to_local_code;
    }

    public void setTo_local_code(Integer to_local_code) {
        this.to_local_code = to_local_code;
    }

    public Integer getTo_class_code() {
        return to_class_code;
    }

    public void setTo_class_code(Integer to_class_code) {
        this.to_class_code = to_class_code;
    }

    public String getMove_type() {
        return move_type;
    }

    public void setMove_type(String move_type) {
        this.move_type = move_type;
    }

    public Integer getReason_code() {
        return reason_code;
    }

    public void setReason_code(Integer reason_code) {
        this.reason_code = reason_code;
    }

    public Integer getInbound_prefix() {
        return inbound_prefix;
    }

    public void setInbound_prefix(Integer inbound_prefix) {
        this.inbound_prefix = inbound_prefix;
    }

    public Integer getInbound_code() {
        return inbound_code;
    }

    public void setInbound_code(Integer inbound_code) {
        this.inbound_code = inbound_code;
    }

    public Integer getInbound_item() {
        return inbound_item;
    }

    public void setInbound_item(Integer inbound_item) {
        this.inbound_item = inbound_item;
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

    public Integer getOutbound_item() {
        return outbound_item;
    }

    public void setOutbound_item(Integer outbound_item) {
        this.outbound_item = outbound_item;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MD_Product_Serial> getSerial() {
        return serial;
    }

    public void setSerial(ArrayList<MD_Product_Serial> serial) {
        this.serial = serial;
    }

    /**
     *
     * Gera obj IO_Move , baseado no obj move da response.
     * @param downloadMove - Obj de response
     * @return IO_Move: Se Ok, obj valido, se erro, obj null.
     *
     */
    public static IO_Move getIO_MoveObj(T_IO_Serial_Process_Download_Move downloadMove){
        IO_Move io_move = new IO_Move();
        //
        try {
            io_move.setCustomer_code(downloadMove.getCustomer_code());
            io_move.setMove_prefix(downloadMove.getMove_prefix());
            io_move.setMove_code(downloadMove.getMove_code());
            io_move.setProduct_code(downloadMove.getProduct_code());
            io_move.setSerial_code(downloadMove.getSerial_code());
            io_move.setSite_code(downloadMove.getSite_code());
            io_move.setFrom_zone_code(downloadMove.getFrom_zone_code());
            io_move.setFrom_local_code(downloadMove.getFrom_local_code());
            io_move.setFrom_class_code(downloadMove.getFrom_class_code());
            io_move.setPlanned_zone_code(downloadMove.getPlanned_zone_code());
            io_move.setPlanned_local_code(downloadMove.getPlanned_local_code());
            io_move.setPlanned_class_code(downloadMove.getPlanned_class_code());
            io_move.setTo_zone_code(downloadMove.getTo_zone_code());
            io_move.setTo_local_code(downloadMove.getTo_local_code());
            io_move.setTo_class_code(downloadMove.getTo_class_code());
            io_move.setMove_type(downloadMove.getMove_type());
            io_move.setReason_code(downloadMove.getReason_code());
            io_move.setInbound_prefix(downloadMove.getInbound_prefix());
            io_move.setInbound_code(downloadMove.getInbound_code());
            io_move.setInbound_item(downloadMove.getInbound_item());
            io_move.setOutbound_prefix(downloadMove.getOutbound_prefix());
            io_move.setOutbound_code(downloadMove.getOutbound_code());
            io_move.setOutbound_item(downloadMove.getOutbound_item());
            io_move.setDone_date(downloadMove.getDone_date());
            io_move.setDone_user(downloadMove.getDone_user());
            io_move.setDone_user_nick(downloadMove.getDone_user_nick());
            io_move.setStatus(downloadMove.getStatus());
        }catch (Exception e){
            io_move = null;
        }
        //
        return io_move;
    }
    public static IO_Blind_Move getIO_Blind_MoveObj(T_IO_Serial_Process_Download_Move downloadMove){
        IO_Blind_Move io_blind_move = new IO_Blind_Move();

        io_blind_move.setCustomer_code(downloadMove.getCustomer_code());
        io_blind_move.setBlind_prefix(downloadMove.getMove_prefix());
        io_blind_move.setBlind_code(downloadMove.getMove_code());
        io_blind_move.setProduct_code(downloadMove.getProduct_code());
        io_blind_move.setSerial_code(downloadMove.getSerial_code());

        io_blind_move.setSite_code(downloadMove.getSite_code());
        io_blind_move.setReason_code(downloadMove.getReason_code());
        io_blind_move.setStatus(downloadMove.getStatus());
        io_blind_move.setLocal_code(downloadMove.getPlanned_local_code());
        io_blind_move.setZone_code(downloadMove.getPlanned_zone_code());
        io_blind_move.setClass_code(downloadMove.getPlanned_class_code());

        return io_blind_move;
    }
}
