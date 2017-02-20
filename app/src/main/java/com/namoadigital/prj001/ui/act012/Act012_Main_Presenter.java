package com.namoadigital.prj001.ui.act012;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act012_Main_Presenter {

    void getPendencies(HMAux label_translation);

    void checkPendenciesFlow(HMAux item);

    void onBackPressedClicked();
}
