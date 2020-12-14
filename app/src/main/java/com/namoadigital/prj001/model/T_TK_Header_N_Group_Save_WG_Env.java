package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Header_N_Group_Save_WG_Env extends Main_Header_Env {
    private String token;
    private ArrayList<T_TK_Header_N_Group_Save_WG_Ticket> ticket = new ArrayList<>();

    public T_TK_Header_N_Group_Save_WG_Env(String app_code, String app_version, String app_type, String session_app, String token, T_TK_Header_N_Group_Save_WG_Ticket ticket) {
        super(app_code, app_version, app_type, session_app);
        this.token = token;
        this.ticket.add(ticket);
    }

    public ArrayList<T_TK_Header_N_Group_Save_WG_Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<T_TK_Header_N_Group_Save_WG_Ticket> ticket) {
        this.ticket = ticket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class T_TK_Header_N_Group_Save_WG_Ticket{
        private long customer_code;
        private int ticket_prefix;
        private int ticket_code;
        private int scn;
        private ArrayList<T_TK_Header_N_Group_Save_WG_Step> step;

        public T_TK_Header_N_Group_Save_WG_Ticket() {
        }

        public T_TK_Header_N_Group_Save_WG_Ticket(long customer_code, int ticket_prefix, int ticket_code, int scn, ArrayList<T_TK_Header_N_Group_Save_WG_Step> step) {
            this.customer_code = customer_code;
            this.ticket_prefix = ticket_prefix;
            this.ticket_code = ticket_code;
            this.scn = scn;
            this.step = step;
        }

        public long getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(long customer_code) {
            this.customer_code = customer_code;
        }

        public int getTicket_prefix() {
            return ticket_prefix;
        }

        public void setTicket_prefix(int ticket_prefix) {
            this.ticket_prefix = ticket_prefix;
        }

        public int getTicket_code() {
            return ticket_code;
        }

        public void setTicket_code(int ticket_code) {
            this.ticket_code = ticket_code;
        }

        public int getScn() {
            return scn;
        }

        public void setScn(int scn) {
            this.scn = scn;
        }

        public ArrayList<T_TK_Header_N_Group_Save_WG_Step> getStep() {
            return step;
        }

        public void setStep(ArrayList<T_TK_Header_N_Group_Save_WG_Step> step) {
            this.step = step;
        }
    }

    public static class T_TK_Header_N_Group_Save_WG_Step{
        private int step_code;
        private int step_group_code;

        public T_TK_Header_N_Group_Save_WG_Step() {
        }

        public T_TK_Header_N_Group_Save_WG_Step(int step_code, int step_group_code) {
            this.step_code = step_code;
            this.step_group_code = step_group_code;
        }

        public int getStep_code() {
            return step_code;
        }

        public void setStep_code(int step_code) {
            this.step_code = step_code;
        }

        public int getStep_group_code() {
            return step_group_code;
        }

        public void setStep_group_code(int step_group_code) {
            this.step_group_code = step_group_code;
        }
    }

}
