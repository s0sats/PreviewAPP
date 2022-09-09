package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Created by neomatrix on 3/22/18.
 */

public class SO_Pack_Express implements Serializable {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("site_code") private long site_code;
    @SerializedName("operation_code") private long operation_code;
    @SerializedName("product_code") private long product_code;
    @SerializedName("express_code") private String express_code;
    @SerializedName("pack_desc") private String pack_desc;
    @SerializedName("category_price_code") private int category_price_code;
    @SerializedName("contract_code") private int contract_code;
    @SerializedName("segment_code") private int segment_code;
    @SerializedName("price_list_code") private int price_list_code;
    @SerializedName("pack_code") private int pack_code;
    @SerializedName("add_pack_service") private int add_pack_service;
    @SerializedName("price") private float price;
    @SerializedName("billing_add_inf1_view") private String billing_add_inf1_view;
    @Nullable
    @SerializedName("billing_add_inf1_text") private String billing_add_inf1_text;
    @SerializedName("billing_add_inf1_tracking") private int billing_add_inf1_tracking;
    @SerializedName("billing_add_inf2_view") private String billing_add_inf2_view;
    @Nullable
    @SerializedName("billing_add_inf2_text") private String billing_add_inf2_text;
    @SerializedName("billing_add_inf2_tracking") private int billing_add_inf2_tracking;
    @SerializedName("billing_add_inf3_view") private String billing_add_inf3_view;
    @Nullable
    @SerializedName("billing_add_inf3_text") private String billing_add_inf3_text;
    @SerializedName("billing_add_inf3_tracking") private int billing_add_inf3_tracking;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getSite_code() {
        return site_code;
    }

    public void setSite_code(long site_code) {
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

    public String getExpress_code() {
        return express_code;
    }

    public void setExpress_code(String express_code) {
        this.express_code = express_code;
    }

    public String getPack_desc() {
        return pack_desc;
    }

    public void setPack_desc(String pack_desc) {
        this.pack_desc = pack_desc;
    }

    public int getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(int category_price_code) {
        this.category_price_code = category_price_code;
    }

    public int getContract_code() {
        return contract_code;
    }

    public void setContract_code(int contract_code) {
        this.contract_code = contract_code;
    }

    public int getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(int segment_code) {
        this.segment_code = segment_code;
    }

    public int getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(int price_list_code) {
        this.price_list_code = price_list_code;
    }

    public int getPack_code() {
        return pack_code;
    }

    public void setPack_code(int pack_code) {
        this.pack_code = pack_code;
    }

    public int getAdd_pack_service() {
        return add_pack_service;
    }

    public void setAdd_pack_service(int add_pack_service) {
        this.add_pack_service = add_pack_service;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBilling_add_inf1_view() {
        return billing_add_inf1_view;
    }

    public void setBilling_add_inf1_view(String billing_add_inf1_view) {
        this.billing_add_inf1_view = billing_add_inf1_view;
    }

    @Nullable
    public String getBilling_add_inf1_text() {
        return billing_add_inf1_text;
    }

    public void setBilling_add_inf1_text(@Nullable String billing_add_inf1_text) {
        this.billing_add_inf1_text = billing_add_inf1_text;
    }

    public String getBilling_add_inf2_view() {
        return billing_add_inf2_view;
    }

    public void setBilling_add_inf2_view(String billing_add_inf2_view) {
        this.billing_add_inf2_view = billing_add_inf2_view;
    }

    @Nullable
    public String getBilling_add_inf2_text() {
        return billing_add_inf2_text;
    }

    public void setBilling_add_inf2_text(@Nullable String billing_add_inf2_text) {
        this.billing_add_inf2_text = billing_add_inf2_text;
    }

    public String getBilling_add_inf3_view() {
        return billing_add_inf3_view;
    }

    public void setBilling_add_inf3_view(String billing_add_inf3_view) {
        this.billing_add_inf3_view = billing_add_inf3_view;
    }

    @Nullable
    public String getBilling_add_inf3_text() {
        return billing_add_inf3_text;
    }

    public void setBilling_add_inf3_text(@Nullable String billing_add_inf3_text) {
        this.billing_add_inf3_text = billing_add_inf3_text;
    }

    public int getBilling_add_inf1_tracking() {
        return billing_add_inf1_tracking;
    }

    public void setBilling_add_inf1_tracking(int billing_add_inf1_tracking) {
        this.billing_add_inf1_tracking = billing_add_inf1_tracking;
    }

    public int getBilling_add_inf2_tracking() {
        return billing_add_inf2_tracking;
    }

    public void setBilling_add_inf2_tracking(int billing_add_inf2_tracking) {
        this.billing_add_inf2_tracking = billing_add_inf2_tracking;
    }

    public int getBilling_add_inf3_tracking() {
        return billing_add_inf3_tracking;
    }

    public void setBilling_add_inf3_tracking(int billing_add_inf3_tracking) {
        this.billing_add_inf3_tracking = billing_add_inf3_tracking;
    }
}
