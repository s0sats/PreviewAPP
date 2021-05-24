package com.namoadigital.prj001.ui.act010;

import android.text.SpannableString;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act010_Main_Presenter {

    void setAdapterData(long product_code, int tagCode, String filter);

    void validateOpenForm(HMAux item);

    void onBackPressedClicked();

    void validateGPSResource(HMAux item);

    SpannableString getTagLblText(String tag_lbl, String tag_desc);
}
