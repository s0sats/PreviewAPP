package com.namoadigital.prj001.ui.act024;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.SM_SO;

import java.util.ArrayList;

/**
 * Created by d.luche on 28/06/2017.
 */

public interface Act024_Main_Presenter {

    void getSoHeaderList(String so_list);

    void downloadSingleSo(SM_SO so);

    void downloadMultSo(ArrayList<HMAux> download_list);

    void executeSODownload(String product_code ,String serial_id , String so_list);

    void startDownloadWorkers();

    void onBackPressedClicked();

}
