package com.namoadigital.prj001.model;

public class SO_Favorite_Request {
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
}
