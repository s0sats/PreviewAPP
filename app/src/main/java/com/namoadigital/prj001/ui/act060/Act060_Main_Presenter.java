package com.namoadigital.prj001.ui.act060;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.IO_Conf_TrackingDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Conf_Tracking;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Item_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.IO_Inbound_Item_Sql_006;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

import static com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create.DATE_FORMAT_MKDATE;

public class Act060_Main_Presenter implements Act060_Main_Contract.I_Presenter  {
    Context context;
    Act060_Main mView;
    HMAux hmAux_trans;

    public Act060_Main_Presenter(Context context, Act060_Main mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
    }


}
