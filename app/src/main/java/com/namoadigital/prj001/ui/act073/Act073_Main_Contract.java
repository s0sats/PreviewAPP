package com.namoadigital.prj001.ui.act073;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit_Presenter;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit_View;

public interface Act073_Main_Contract {

    interface I_View extends Frg_Serial_Edit_View {

        void setProductValues(MD_Product md_product);

        void setWsProcess(String wsProcess);

        void showPD(String title , String msg);

        void showAlert(String ttl, String msg);

        void callAct068();
    }

    interface I_Presenter extends Frg_Serial_Edit_Presenter {
        void defineBackFlow();

        void onBackPressedClicked();

        void executeSerialSearch(Long product_code, String serial_id);

        void saveSerialInfo(MD_Product_Serial md_product_serial);

        void executeTicketDownload(long productCode, long serialCode, String serialId);
    }
}
