package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.receiver.WBR_SO_Favorite_List;
import com.namoadigital.prj001.service.WS_SO_Favorite_List;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act050_Main_Presenter implements Act050_Main_Contract.I_Presenter {

    Act050_Main_Contract.I_Frag_Favorite mView;
    private HMAux hmAux_trans;

    public Act050_Main_Presenter(Act050_Main_Contract.I_Frag_Favorite mView, HMAux hmAux_trans) {
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
    }

    @Override
    public void getFavoriteList(Context context, long productCode, long serialCode, int categoryPriceCode, int segmentCode) {

        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_SO_Favorite_List.class.getName());

            mView.showPD(
                    "De Conhecimento",
                    "Massagem"
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Favorite_List.class);
            Bundle bundle = new Bundle();
            //

            bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putLong(Constant.LOGIN_OPERATION_CODE, ToolBox_Con.getPreference_Operation_Code(context));
            bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, productCode);
            bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, serialCode);
            bundle.putInt(SM_SO_ServiceDao.CATEGORY_PRICE_CODE, categoryPriceCode);
            bundle.putInt(MD_SegmentDao.SEGMENT_CODE, segmentCode);

            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showNoConnecionMsg();
        }
//        mView.populatedFavoritesList();
    }
}
