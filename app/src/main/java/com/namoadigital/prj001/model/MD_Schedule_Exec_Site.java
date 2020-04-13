package com.namoadigital.prj001.model;

/**
 * LUCHE - 13/04/2020
 *
 * USADO SOMENTE NO PROCESSAMENTO DO AGENDAMENTO NÃO POSSUI TABLE, É UMA LISTA TMP
 */

public class MD_Schedule_Exec_Site {

    private long customer_code;
    private int site_code;
    private String site_id;
    private String site_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }
}
