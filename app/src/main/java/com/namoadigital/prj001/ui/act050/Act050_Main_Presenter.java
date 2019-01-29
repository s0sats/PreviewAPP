package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_SO_Favorite_List;
import com.namoadigital.prj001.service.WS_SO_Favorite_List;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

public class Act050_Main_Presenter implements Act050_Main_Contract.I_Presenter {

    Act050_Main_Contract.I_Frag_Favorite mView;
    private HMAux hmAux_trans;

    public Act050_Main_Presenter(Act050_Main_Contract.I_Frag_Favorite mView, HMAux hmAux_trans) {
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
    }

    @Override
    public void getFavoriteList(Context context, long productCode, long serialCode) {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
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
            MD_Product_Serial md_product_serials = serialDao.getByString(
                    new MD_Product_Serial_Sql_009(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            productCode,
                            (int) serialCode).toSqlQuery()
            );

            if(md_product_serials != null ) {
                bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
                bundle.putLong(Constant.LOGIN_OPERATION_CODE, ToolBox_Con.getPreference_Operation_Code(context));
                bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, productCode);
                bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, serialCode);
                bundle.putInt(SM_SO_ServiceDao.CATEGORY_PRICE_CODE, md_product_serials.getCategory_price_code());
                bundle.putInt(MD_SegmentDao.SEGMENT_CODE, md_product_serials.getSegment_code());
            }
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
