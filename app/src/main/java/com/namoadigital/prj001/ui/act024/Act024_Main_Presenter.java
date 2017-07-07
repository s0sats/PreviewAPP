package com.namoadigital.prj001.ui.act024;

import com.namoadigital.prj001.model.SM_SO;

import java.util.ArrayList;

/**
 * Created by d.luche on 28/06/2017.
 */

public interface Act024_Main_Presenter {

    void getSoHeaderList(String so_list);

    void downloadSingleSo(SM_SO so);

    void downloadMultSo(ArrayList<SM_SO> download_list);

    void onBackPressedClicked();

}
