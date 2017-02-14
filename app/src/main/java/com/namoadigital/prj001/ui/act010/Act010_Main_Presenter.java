package com.namoadigital.prj001.ui.act010;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act010_Main_Presenter {

    void setAdapterData(long product_code, int custom_form_type, String filter);

    void validateOpenForm(HMAux item);

    void onBackPressedClicked();

}
