package com.namoadigital.prj001.ui.act006;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main_Presenter_Impl implements Act006_Main_Presenter {

    private Context context;
    private Act006_Main_View mView;

    public Act006_Main_Presenter_Impl(Context context, Act006_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void getAllOpcs() {
        ArrayList<HMAux> data = new ArrayList<>();
        //
        for (int i = 0; i < opcs.length; i++) {
            HMAux item = new HMAux();
            //
            item.put(HMAux.ID, String.valueOf(i + 1));
            item.put(HMAux.TEXTO_01, Constant.ACT006 + "_lbl_" + opcs[i]);
            item.put(HMAux.TEXTO_02, opcs[i]);
            //
            data.add(item);
        }

        mView.loadCheckListOpcs(data);
    }

    String[] opcs = {
            "new",
          //  "barcode",
            "checklist"
    };
}
