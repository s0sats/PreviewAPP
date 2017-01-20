package com.namoadigital.prj001.ui.act001;

import android.content.Context;

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

    void showPD();

    void call_Act002_Main(Context context);

    void call_Act003_Main(Context context);

}
