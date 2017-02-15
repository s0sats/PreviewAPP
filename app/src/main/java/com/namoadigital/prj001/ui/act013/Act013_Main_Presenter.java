package com.namoadigital.prj001.ui.act013;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act013_Main_Presenter {

    void getPendencies();

    void addFormInfoToBundle(HMAux item);

    void validateOpenForm(HMAux item);

    void onBackPressedClicked();
}
