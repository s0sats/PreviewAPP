package com.namoadigital.prj001.ui.act070.model;

public class StepNotExecuted extends BaseStep{
    private String justifyLbl;
    private String justify;
    private String comment;
    private String userId;
    private String userCode;
    private String photoUrl;
    private String photoName;
    private String date;
    //
    public StepNotExecuted(String justifyLbl,String justify, String comment, String userId, String userCode, String photoName, String photoUrl, String date) {
        this.justifyLbl = justifyLbl;
        this.justify = justify;
        this.comment = comment;
        this.userId = userId;
        this.userCode = userCode;
        this.photoName = photoName;
        this.photoUrl = photoUrl;
        this.date = date;
    }
    //

    public String getJustifyLbl() {
        return justifyLbl;
    }

    public void setJustifyLbl(String justifyLbl) {
        this.justifyLbl = justifyLbl;
    }

    public String getJustify() {
        return justify;
    }

    public void setJustify(String justify) {
        this.justify = justify;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
