package com.namoadigital.prj001.ui.act061.frag_drawer;

import com.namoa_digital.namoa_library.util.HMAux;

public interface Act061_Frag_Drawer_Contract {
    interface I_View{

    }

    interface I_Presenter{
        HMAux getPercents(int inbound_prefix, int inbound_code);
    }
}
