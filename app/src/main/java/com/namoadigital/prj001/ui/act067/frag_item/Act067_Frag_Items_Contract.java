package com.namoadigital.prj001.ui.act067.frag_item;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

public interface Act067_Frag_Items_Contract {

    interface I_View{

    }

    interface I_Presenter{

        ArrayList<HMAux> getItemList(int inboundPrefix, int inboundCode);
    }
}
