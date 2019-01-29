package com.namoadigital.prj001.model;

public class SO_Favorite_Request  extends  Main_Header_Env{
    String site_code;
	long operation_code;
	long product_code;
	long serial_code;
	int category_price_code;
	int segment_code;

	public SO_Favorite_Request(String siteCode, long operation_code, long product_code, long serial_code, int category_price_code, int segment_code) {
		this.site_code = siteCode;
		this.operation_code = operation_code;
		this.product_code = product_code;
		this.serial_code = serial_code;
		this.category_price_code = category_price_code;
		this.segment_code = segment_code;
	}

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public long getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(long operation_code) {
        this.operation_code = operation_code;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public long getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(long serial_code) {
        this.serial_code = serial_code;
    }

    public int getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(int category_price_code) {
        this.category_price_code = category_price_code;
    }

    public int getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(int segment_code) {
        this.segment_code = segment_code;
    }
}
