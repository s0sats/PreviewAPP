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
    @SerializedName("site_exec_code")
    @Expose
    private Integer siteExecCode;
    @SerializedName("site_exec_id")
    @Expose
    private String siteExecId;
    @SerializedName("site_exec_desc")
    @Expose
    private String siteExecDesc;
    @SerializedName("mask_code")
    @Expose
    private Integer maskCode;
    @SerializedName("so_id_view")
    @Expose
    private String soIdView;
    @SerializedName("so_id_text")
    @Expose
    private String soIdText;
    @SerializedName("so_desc_view")
    @Expose
    private String soDescView;
    @SerializedName("so_desc_text")
    @Expose
    private String soDescText;
    @SerializedName("so_add_inf1_view")
    @Expose
    private String soAddInf1View;
    @SerializedName("so_add_inf1_text")
    @Expose
    private String soAddInf1Text;
    @SerializedName("so_add_inf2_view")
    @Expose
    private String soAddInf2View;
    @SerializedName("so_add_inf2_text")
    @Expose
    private String soAddInf2Text;
    @SerializedName("so_add_inf3_view")
    @Expose
    private String soAddInf3View;
    @SerializedName("so_add_inf3_text")
    @Expose
    private String soAddInf3Text;
    @SerializedName("so_file_view")
    @Expose
    private String soFileView;
    @SerializedName("so_file_text")
    @Expose
    private String soFileText;
    @SerializedName("so_priority_view")
    @Expose
    private String soPriorityView;
    @SerializedName("so_client_type_view")
    @Expose
    private String soClientTypeView;
    @SerializedName("so_client_so_id_view")
    @Expose
    private String soClientSoIdView;
    @SerializedName("so_client_so_id_text")
    @Expose
    private String soClientSoIdText;
    @SerializedName("so_add_inf1_tracking")
    @Expose
    private Integer soAddInf1Tracking;
    @SerializedName("so_add_inf2_tracking")
    @Expose
    private Integer soAddInf2Tracking;
    @SerializedName("so_add_inf3_tracking")
    @Expose
    private Integer soAddInf3Tracking;
    @SerializedName("so_add_inf4_view")
    @Expose
    private String soAddInf4View;
    @SerializedName("so_add_inf4_text")
    @Expose
    private String soAddInf4Text;
    @SerializedName("so_add_inf4_tracking")
    @Expose
    private Integer soAddInf4Tracking;
    @SerializedName("so_add_inf5_view")
    @Expose
    private String soAddInf5View;
    @SerializedName("so_add_inf5_text")
    @Expose
    private String soAddInf5Text;
    @SerializedName("so_add_inf5_tracking")
    @Expose
    private Integer soAddInf5Tracking;
    @SerializedName("so_add_inf6_view")
    @Expose
    private String soAddInf6View;
    @SerializedName("so_add_inf6_text")
    @Expose
    private String soAddInf6Text;
    @SerializedName("so_add_inf6_tracking")
    @Expose
    private Integer soAddInf6Tracking;


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

    public Integer getSiteExecCode() {
        return siteExecCode;
    }

    public void setSiteExecCode(Integer siteExecCode) {
        this.siteExecCode = siteExecCode;
    }

    public String getSiteExecId() {
        return siteExecId;
    }

    public void setSiteExecId(String siteExecId) {
        this.siteExecId = siteExecId;
    }

    public String getSiteExecDesc() {
        return siteExecDesc;
    }

    public void setSiteExecDesc(String siteExecDesc) {
        this.siteExecDesc = siteExecDesc;
    }

    public Integer getMaskCode() {
        return maskCode;
    }

    public void setMaskCode(Integer maskCode) {
        this.maskCode = maskCode;
    }

    public String getSoIdView() {
        return soIdView;
    }

    public void setSoIdView(String soIdView) {
        this.soIdView = soIdView;
    }

    public String getSoIdText() {
        return soIdText;
    }

    public void setSoIdText(String soIdText) {
        this.soIdText = soIdText;
    }

    public String getSoDescView() {
        return soDescView;
    }

    public void setSoDescView(String soDescView) {
        this.soDescView = soDescView;
    }

    public String getSoDescText() {
        return soDescText;
    }

    public void setSoDescText(String soDescText) {
        this.soDescText = soDescText;
    }

    public String getSoAddInf1View() {
        return soAddInf1View;
    }

    public void setSoAddInf1View(String soAddInf1View) {
        this.soAddInf1View = soAddInf1View;
    }

    public String getSoAddInf1Text() {
        return soAddInf1Text;
    }

    public void setSoAddInf1Text(String soAddInf1Text) {
        this.soAddInf1Text = soAddInf1Text;
    }

    public String getSoAddInf2View() {
        return soAddInf2View;
    }

    public void setSoAddInf2View(String soAddInf2View) {
        this.soAddInf2View = soAddInf2View;
    }

    public String getSoAddInf2Text() {
        return soAddInf2Text;
    }

    public void setSoAddInf2Text(String soAddInf2Text) {
        this.soAddInf2Text = soAddInf2Text;
    }

    public String getSoAddInf3View() {
        return soAddInf3View;
    }

    public void setSoAddInf3View(String soAddInf3View) {
        this.soAddInf3View = soAddInf3View;
    }

    public String getSoAddInf3Text() {
        return soAddInf3Text;
    }

    public void setSoAddInf3Text(String soAddInf3Text) {
        this.soAddInf3Text = soAddInf3Text;
    }

    public String getSoFileView() {
        return soFileView;
    }

    public void setSoFileView(String soFileView) {
        this.soFileView = soFileView;
    }

    public String getSoFileText() {
        return soFileText;
    }

    public void setSoFileText(String soFileText) {
        this.soFileText = soFileText;
    }

    public String getSoPriorityView() {
        return soPriorityView;
    }

    public void setSoPriorityView(String soPriorityView) {
        this.soPriorityView = soPriorityView;
    }

    public String getSoClientTypeView() {
        return soClientTypeView;
    }

    public void setSoClientTypeView(String soClientTypeView) {
        this.soClientTypeView = soClientTypeView;
    }

    public String getSoClientSoIdView() {
        return soClientSoIdView;
    }

    public void setSoClientSoIdView(String soClientSoIdView) {
        this.soClientSoIdView = soClientSoIdView;
    }

    public String getSoClientSoIdText() {
        return soClientSoIdText;
    }

    public void setSoClientSoIdText(String soClientSoIdText) {
        this.soClientSoIdText = soClientSoIdText;
    }

    public Integer getSoAddInf1Tracking() {
        return soAddInf1Tracking;
    }

    public void setSoAddInf1Tracking(Integer soAddInf1Tracking) {
        this.soAddInf1Tracking = soAddInf1Tracking;
    }

    public Integer getSoAddInf2Tracking() {
        return soAddInf2Tracking;
    }

    public void setSoAddInf2Tracking(Integer soAddInf2Tracking) {
        this.soAddInf2Tracking = soAddInf2Tracking;
    }

    public Integer getSoAddInf3Tracking() {
        return soAddInf3Tracking;
    }

    public void setSoAddInf3Tracking(Integer soAddInf3Tracking) {
        this.soAddInf3Tracking = soAddInf3Tracking;
    }

    public String getSoAddInf4View() {
        return soAddInf4View;
    }

    public void setSoAddInf4View(String soAddInf4View) {
        this.soAddInf4View = soAddInf4View;
    }

    public String getSoAddInf4Text() {
        return soAddInf4Text;
    }

    public void setSoAddInf4Text(String soAddInf4Text) {
        this.soAddInf4Text = soAddInf4Text;
    }

    public Integer getSoAddInf4Tracking() {
        return soAddInf4Tracking;
    }

    public void setSoAddInf4Tracking(Integer soAddInf4Tracking) {
        this.soAddInf4Tracking = soAddInf4Tracking;
    }

    public String getSoAddInf5View() {
        return soAddInf5View;
    }

    public void setSoAddInf5View(String soAddInf5View) {
        this.soAddInf5View = soAddInf5View;
    }

    public String getSoAddInf5Text() {
        return soAddInf5Text;
    }

    public void setSoAddInf5Text(String soAddInf5Text) {
        this.soAddInf5Text = soAddInf5Text;
    }

    public Integer getSoAddInf5Tracking() {
        return soAddInf5Tracking;
    }

    public void setSoAddInf5Tracking(Integer soAddInf5Tracking) {
        this.soAddInf5Tracking = soAddInf5Tracking;
    }

    public String getSoAddInf6View() {
        return soAddInf6View;
    }

    public void setSoAddInf6View(String soAddInf6View) {
        this.soAddInf6View = soAddInf6View;
    }

    public String getSoAddInf6Text() {
        return soAddInf6Text;
    }

    public void setSoAddInf6Text(String soAddInf6Text) {
        this.soAddInf6Text = soAddInf6Text;
    }

    public Integer getSoAddInf6Tracking() {
        return soAddInf6Tracking;
    }

    public void setSoAddInf6Tracking(Integer soAddInf6Tracking) {
        this.soAddInf6Tracking = soAddInf6Tracking;
    }
}
