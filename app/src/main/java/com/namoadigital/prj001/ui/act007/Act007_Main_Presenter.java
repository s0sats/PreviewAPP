package com.namoadigital.prj001.ui.act007;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act007_Main_Presenter {

    void setFile_name(String file_name);

    void executeSerialLog(MD_Product_Serial mdProductSerial);

    void getLog();

    void deleteLogFile();

    void processSoDownloadResult(HMAux so_download_result);

    void processDownloadClick(String process, String[] pk, boolean alreadyDownloaded);

    void executeNFormPDFDownload(String[] pk, String url);

    void executeNFormPDFGeneratation(String pk);

    void openPDF(String file_name);

    void openPDF(String[] pk);
}
