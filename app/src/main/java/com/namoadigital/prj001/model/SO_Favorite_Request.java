package com.namoadigital.prj001.model;

public class SO_Favorite_Request  extends  Main_Header_Env{
    String siteCode;
	long operationCode;
	long productCode;
	long serialCode;
	int categoryPriceCode;
	int segmentCode;

	public SO_Favorite_Request(String siteCode, long operationCode, long productCode, long serialCode, int categoryPriceCode, int segmentCode) {
		this.siteCode = siteCode;
		this.operationCode = operationCode;
		this.productCode = productCode;
		this.serialCode = serialCode;
		this.categoryPriceCode = categoryPriceCode;
		this.segmentCode = segmentCode;
	}

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public long getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(long operationCode) {
        this.operationCode = operationCode;
    }

    public long getProductCode() {
        return productCode;
    }

    public void setProductCode(long productCode) {
        this.productCode = productCode;
    }

    public long getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(long serialCode) {
        this.serialCode = serialCode;
    }

    public int getCategoryPriceCode() {
        return categoryPriceCode;
    }

    public void setCategoryPriceCode(int categoryPriceCode) {
        this.categoryPriceCode = categoryPriceCode;
    }

    public int getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(int segmentCode) {
        this.segmentCode = segmentCode;
    }
}
