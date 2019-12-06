package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SO_Favorite_Item implements Serializable {
    private static final long serialVersionUID = -1138602148771599430L;
    //
    @SerializedName("customer_code")
    @Expose
    private Integer customerCode;
    @SerializedName("profile_code")
    @Expose
    private Integer profileCode;
    @SerializedName("favorite_code")
    @Expose
    private Integer favoriteCode;
    @SerializedName("favorite_desc")
    @Expose
    private String favoriteDesc;
    @SerializedName("favorite_color")
    @Expose
    private String favoriteColor;
    @SerializedName("favorite_font_color")
    @Expose
    private String favoriteFontColor;
    @SerializedName("contract_code")
    @Expose
    private Integer contractCode;
    @SerializedName("po_code")
    @Expose
    private Integer poCode;
    @SerializedName("pack_default")
    @Expose
    private String packDefault;
    @SerializedName("client_type")
    @Expose
    private String clientType;
    @SerializedName("client_code")
    @Expose
    private Integer clientCode;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("client_email")
    @Expose
    private String clientEmail;
    @SerializedName("client_phone")
    @Expose
    private String clientPhone;
    @SerializedName("pipeline_code")
    @Expose
    private Integer pipelineCode;
    @SerializedName("price_list_code")
    @Expose
    private Integer priceListCode;
    @SerializedName("pack_code")
    @Expose
    private Integer packCode;
    @SerializedName("pack_service_desc_full")
    @Expose
    private String packServiceDescFull;

    public SO_Favorite_Item(Integer customerCode, Integer profileCode, Integer favoriteCode, String favoriteDesc, String favoriteColor, String favoriteFontColor, Integer contractCode, String packDefault, String clientType, Integer clientCode, String clientId, String clientName, String clientEmail, String clientPhone) {
        this.customerCode = customerCode;
        this.profileCode = profileCode;
        this.favoriteCode = favoriteCode;
        this.favoriteDesc = favoriteDesc;
        this.favoriteColor = favoriteColor;
        this.favoriteFontColor = favoriteFontColor;
        this.contractCode = contractCode;
        this.packDefault = packDefault;
        this.clientType = clientType;
        this.clientCode = clientCode;
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }

    public SO_Favorite_Item(Integer customerCode, Integer profileCode, Integer favoriteCode, String favoriteDesc, String favoriteColor, String favoriteFontColor, Integer contractCode, Integer poCode, String packDefault, String clientType, Integer clientCode, String clientId, String clientName, String clientEmail, String clientPhone, Integer pipelineCode, Integer priceListCode, Integer packCode, String packServiceDescFull) {
        this.customerCode = customerCode;
        this.profileCode = profileCode;
        this.favoriteCode = favoriteCode;
        this.favoriteDesc = favoriteDesc;
        this.favoriteColor = favoriteColor;
        this.favoriteFontColor = favoriteFontColor;
        this.contractCode = contractCode;
        this.poCode = poCode;
        this.packDefault = packDefault;
        this.clientType = clientType;
        this.clientCode = clientCode;
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
        this.pipelineCode = pipelineCode;
        this.priceListCode = priceListCode;
        this.packCode = packCode;
        this.packServiceDescFull = packServiceDescFull;
    }

    public Integer getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Integer customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(Integer profileCode) {
        this.profileCode = profileCode;
    }

    public Integer getFavoriteCode() {
        return favoriteCode;
    }

    public void setFavoriteCode(Integer favoriteCode) {
        this.favoriteCode = favoriteCode;
    }

    public String getFavoriteDesc() {
        return favoriteDesc;
    }

    public void setFavoriteDesc(String favoriteDesc) {
        this.favoriteDesc = favoriteDesc;
    }

    public String getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public String getFavoriteFontColor() {
        return favoriteFontColor;
    }

    public void setFavoriteFontColor(String favoriteFontColor) {
        this.favoriteFontColor = favoriteFontColor;
    }

    public Integer getContractCode() {
        return contractCode;
    }

    public void setContractCode(Integer contractCode) {
        this.contractCode = contractCode;
    }

    public String getPackDefault() {
        return packDefault;
    }

    public void setPackDefault(String packDefault) {
        this.packDefault = packDefault;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Integer getClientCode() {
        return clientCode;
    }

    public void setClientCode(Integer clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public Integer getPoCode() {
        return poCode;
    }

    public void setPoCode(Integer poCode) {
        this.poCode = poCode;
    }

    public Integer getPipelineCode() {
        return pipelineCode;
    }

    public void setPipelineCode(Integer pipelineCode) {
        this.pipelineCode = pipelineCode;
    }

    public Integer getPriceListCode() {
        return priceListCode;
    }

    public void setPriceListCode(Integer priceListCode) {
        this.priceListCode = priceListCode;
    }

    public Integer getPackCode() {
        return packCode;
    }

    public void setPackCode(Integer packCode) {
        this.packCode = packCode;
    }

    public String getPackServiceDescFull() {
        return packServiceDescFull;
    }

    public void setPackServiceDescFull(String packServiceDescFull) {
        this.packServiceDescFull = packServiceDescFull;
    }
}
