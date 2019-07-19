package com.namoadigital.prj001.ui.act052;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;

public interface Act052_Main_Contract {

    interface I_Presenter{

        void onBackPressedClicked();

        void defineIOSerialFlow(HMAux hmAuxRet);

        void executeWsProcessDownload(IO_Serial_Process_Record data);

        void createNewSerialFlow(String mProduct_id, String mSerial_id);

        void editNonLocationSerial(IO_Serial_Process_Record record);

        boolean isSiteInboundAutoCreation();

        boolean hasCreateSerialPermission(String mProduct_id, String mSerial_id, boolean serial_jump);

        void callBlindMove(String mProduct_id, String mSerial_id);

        void processListItem(IO_Serial_Process_Record data);
    }

    interface I_View{

        void setWsProcess(String wsProcess);

        void showPD(String title, String msg);

        void callAct051();

        void callAct053(Bundle bundle);

        void callAct058(Bundle bundle);

        void callAct061(Bundle bundle);

        void callAct064(Bundle bundle);

        void callAct059(Bundle bundle);

        void callAct067(Bundle bundle);

        void showAlert(String ttl, String msg, @Nullable DialogInterface.OnClickListener listener) ;

        boolean getSerialJump();
    }
}
