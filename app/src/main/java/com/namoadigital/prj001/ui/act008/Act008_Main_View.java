package com.namoadigital.prj001.ui.act008;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_View {

    void setCheckboxValues(int required, int allow_new);

    void fieldFocus();

    void showPD(String wsProcess);

    void showAlertDialog(String title, String msg);

}
