package com.namoadigital.prj001.ui.act001;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface Act001_Main_View {

    void updatePD(String type, String sMessage);

    void showAlertMsg(String title, String message);
    //
    // VERIFICAR NECESSIDADE DA INCLUSÃO DO METODO
    //showSoftKeyboard NA TOOLBOX DA LIBRARY OU DO PROJETO.
    void fieldFocus(int index);

}
