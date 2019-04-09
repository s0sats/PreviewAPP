package com.namoadigital.prj001.model;

import java.util.List;

public class T_IO_Move_Save_Env extends Main_Header_Env {
    private String token;
    private List<IO_Move_Save_Record>  move;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<IO_Move_Save_Record> getMove() {
        return move;
    }

    public void setMove(List<IO_Move_Save_Record> move) {
        this.move = move;
    }
}
