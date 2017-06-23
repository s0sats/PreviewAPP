package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Segment {

    private long customer_code;
    private int segment_code;
    private String segment_id;
    private String segment_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(int segment_code) {
        this.segment_code = segment_code;
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
}
