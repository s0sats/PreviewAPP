package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SO_Favorite_Response implements Serializable {
    private static final long serialVersionUID = -8556367998671062222L;

    @SerializedName("app")
    @Expose
    private String app;
    @SerializedName("validation")
    @Expose
    private String validation;
    private String link_url;
    private String error_msg;
    @SerializedName("contract")
    @Expose
    private List<SO_Favorite_Contract> contract;
    @SerializedName("favorite")
    @Expose
    private List<SO_Favorite_Item> favorite;
    @SerializedName("priority")
    @Expose
    private List<SO_Favorite_Priority> priority;
    @SerializedName("pipeline")
    @Expose
    private List<SO_Favorite_Pipeline> pipeline;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public List<SO_Favorite_Contract> getContract() {
        return contract;
    }

    public void setContract(List<SO_Favorite_Contract> contract) {
        this.contract = contract;
    }

    public List<SO_Favorite_Item> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<SO_Favorite_Item> favorite) {
        this.favorite = favorite;
    }

    public List<SO_Favorite_Priority> getPriority() {
        return priority;
    }

    public void setPriority(List<SO_Favorite_Priority> priority) {
        this.priority = priority;
    }

    public List<SO_Favorite_Pipeline> getPipeline() {
        return pipeline;
    }

    public void setPipeline(List<SO_Favorite_Pipeline> pipeline) {
        this.pipeline = pipeline;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
