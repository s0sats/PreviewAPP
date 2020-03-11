package com.namoadigital.prj001.model;

public class DaoObjReturn {

    public static final String DAO_ERROR_EXCEPTION = "DAO_ERROR_EXCEPTION";

    public static final String SELECT = "SELECT";
    public static final String INSERT = "INSERT";
    public static final String INSERT_OR_UPDATE = "INSERT_OR_UPDATE";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";

    public String DELETE_ERROR_0_ROWS_AFFECTED = "delete in table "+this.table+" return 0 affected rows";

    private boolean error;
    private String action;
    private String code;
    private String description;
    private String rawMessage;
    private long actionReturn;//Qtd de linhas afetadas
    private String table;

    public DaoObjReturn() {
        initializeProperties();
    }

    private void initializeProperties() {
        this.error = false;
        this.action = "";
        this.code = "";
        this.description = "";
        this.rawMessage = "";
        this.actionReturn = -2;//Default menos 2 pois o retorno de dado não inserido é -1
        this.table = "";
    }

    public DaoObjReturn(String table) {
        initializeProperties();
        this.table = table;
    }

    public DaoObjReturn(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public void copyError(DaoObjReturn DaoError){
        if (DaoError == null) {
            return;
        }
        //
        this.error = DaoError.hasError();
        this.action = DaoError.getAction();
        this.actionReturn = DaoError.getActionReturn();
        this.code = DaoError.getCode();
        this.description = DaoError.getDescription();
        this.rawMessage = DaoError.getRawMessage();
        this.table = DaoError.getTable();
    }

    public void clearError(){
        this.error = false;
        this.action = "";
        this.actionReturn = -2;
        this.code = "";
        this.description = "";
        this.rawMessage = "";
        this.table = "";
    }

    public String getErrorMsg(){
        return "\nError: " + DAO_ERROR_EXCEPTION +
                (action.length() > 0 ? "\n Trying to: " + action: "" ) +
                "\nShort Desc: " + getCode() + " - " + getDescription() +
                "\nFull Desc: "+getRawMessage();
    }

    public boolean hasError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getActionReturn() {
        return actionReturn;
    }

    public void setActionReturn(long actionReturn) {
        this.actionReturn = actionReturn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
