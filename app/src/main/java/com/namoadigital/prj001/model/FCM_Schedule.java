package com.namoadigital.prj001.model;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 24/03/2020
 *
 * Classe que representa os dados enviados no msg_long dos FCM do agendamento.
 *
 */

public class FCM_Schedule {
    private String customer_code = "-1";
    private String schedule_prefix = "-1";
    private String schedule_code = "-1";
    @Nullable
    private String schedule_exec = "-1";
    private String schedule_msg_type = null;
    private FCM_Schedule_Msg_long schedule_msg_long = new FCM_Schedule_Msg_long();

    public FCM_Schedule(FCMMessage fcmMessage) {
        if(fcmMessage != null){
            this.schedule_msg_type = fcmMessage.getTitle().trim();
            extracSchedulePkFromShortMsg(fcmMessage.getMsg_short());
            extracSchedulesInfoFromLongMsg(fcmMessage.getMsg_long());
        }
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getSchedule_prefix() {
        return schedule_prefix;
    }

    public void setSchedule_prefix(String schedule_prefix) {
        this.schedule_prefix = schedule_prefix;
    }

    public String getSchedule_code() {
        return schedule_code;
    }

    public void setSchedule_code(String schedule_code) {
        this.schedule_code = schedule_code;
    }

    @Nullable
    public String getSchedule_exec() {
        return schedule_exec;
    }

    public void setSchedule_exec(@Nullable String schedule_exec) {
        this.schedule_exec = schedule_exec;
    }

    public String getSchedule_msg_type() {
        return schedule_msg_type;
    }

    public void setSchedule_msg_type(String schedule_msg_type) {
        this.schedule_msg_type = schedule_msg_type;
    }

    public FCM_Schedule_Msg_long getSchedule_msg_long() {
        return schedule_msg_long;
    }

    public void setSchedule_msg_long(FCM_Schedule_Msg_long schedule_msg_long) {
        this.schedule_msg_long = schedule_msg_long;
    }

    /**
     * LUCHE - 26/03/2020
     * Metodo que retorna o status correto baseado no tipo do update
     * @return - Status ou null se schedule_msg_long ainda não definida
     */
    @Nullable
    public String getStatus(){
        if(schedule_msg_long != null){
            String sStatus = schedule_msg_type.equals(ConstantBaseApp.FCM_ACTION_SCHEDULE_UPDATE)
                    ? schedule_msg_long.getSchedule_status()
                    : schedule_msg_long.getExec_status();
            //
            return  sStatus != null && sStatus.equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PROCESS) ? ConstantBaseApp.SYS_STATUS_CANCELLED : sStatus;
        }
        //
        return null;
    }

    /**
     * LUCHE - 24/03/2020
     * Metodo que extrai a pk da short_msg e seta no obj
     * @param short_msg Short MSg do FCM
     */
    private void extracSchedulePkFromShortMsg(String short_msg){
        if(isScheduleRightPK(short_msg)){
            String[] rawPk = short_msg.replace(".","#").split("#");
            this.customer_code = rawPk[0];
            this.schedule_prefix = rawPk[1];
            this.schedule_code = rawPk[2];
            this.schedule_exec = rawPk.length == 4 ? rawPk[3] : "-1";
        }
    }

    /**
     * LUCHE - 24/03/2020
     * <p></p>
     * Metodo que verifica se existe ocorrencia da pk no shortMsg baseado no tipo de msg
     * @param short_msg ShortMSg enviado no FCM
     * @return True se existir "." e se qtd de pontos for 3 para schedule e 4 para schedule_exec
     */
    private boolean isScheduleRightPK(String short_msg) {
        int matchesByType =  schedule_msg_type.equalsIgnoreCase(ConstantBaseApp.FCM_ACTION_SCHEDULE_UPDATE) ? 2 : 3;
        return short_msg.contains(".") && ((short_msg.length() - short_msg.replace(".", "").length()) == matchesByType);
    }

    private void extracSchedulesInfoFromLongMsg(String msg_long) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        this.schedule_msg_long = gson.fromJson(msg_long, FCM_Schedule_Msg_long.class);
    }

    public boolean isValid(){
        return !customer_code.equalsIgnoreCase("-1")
               && !schedule_prefix.equalsIgnoreCase("-1")
               && !schedule_code.equalsIgnoreCase("-1")
               && ( schedule_msg_type.equalsIgnoreCase(ConstantBaseApp.FCM_ACTION_SCHEDULE_UPDATE)
                    || !schedule_exec.equalsIgnoreCase("-1"));
    }

    public class FCM_Schedule_Msg_long {
        @Nullable
        private String schedule_desc = null;
        @Nullable
        private String date_start  = null;
        @Nullable
        private String date_end  = null;
        @Nullable
        private String schedule_status  = null;
        @Nullable
        private String exec_status  = null;
        @Nullable
        private String comments  = null;
        @Nullable
        private String user_nick  = null;

        @Nullable
        public String getSchedule_desc() {
            return schedule_desc;
        }

        public void setSchedule_desc(@Nullable String schedule_desc) {
            this.schedule_desc = schedule_desc;
        }

        @Nullable
        public String getDate_start() {
            return date_start;
        }

        public void setDate_start(@Nullable String date_start) {
            this.date_start = date_start;
        }

        @Nullable
        public String getDate_end() {
            return date_end;
        }

        public void setDate_end(@Nullable String date_end) {
            this.date_end = date_end;
        }

        @Nullable
        public String getSchedule_status() {
            return schedule_status;
        }

        public void setSchedule_status(@Nullable String schedule_status) {
            this.schedule_status = schedule_status;
        }

        @Nullable
        public String getExec_status() {
            return exec_status;
        }

        public void setExec_status(@Nullable String exec_status) {
            this.exec_status = exec_status;
        }

        @Nullable
        public String getComments() {
            return comments;
        }

        public void setComments(@Nullable String comments) {
            this.comments = comments;
        }

        @Nullable
        public String getUser_nick() {
            return user_nick;
        }

        public void setUser_nick(@Nullable String user_nick) {
            this.user_nick = user_nick;
        }
    }


}
