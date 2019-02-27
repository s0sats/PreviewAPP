package com.namoadigital.prj001.ui.act043;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.ArrayList;

public interface onSmSoRequestObject {
    SM_SO getSmSo();
    HMAux getHMAux_Trans();
    void callAct005();
}
