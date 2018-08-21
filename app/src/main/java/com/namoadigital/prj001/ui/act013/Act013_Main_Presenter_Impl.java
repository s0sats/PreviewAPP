package com.namoadigital.prj001.ui.act013;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act013_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act013_Main_Presenter_Impl implements Act013_Main_Presenter {

    private Context context;
    private Act013_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans;
    //
    public Act013_Main_Presenter_Impl(Context context, Act013_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getPendencies(boolean filter_in_processing ,boolean filter_finalized,boolean filter_scheduled) {

        List<HMAux> pendencies =
                customFormLocalDao.query_HM(
                        new Sql_Act013_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                filter_in_processing,
                                filter_finalized,
                                filter_scheduled,
                                context
                        ).toSqlQuery()
                );

        mView.loadPendencies(pendencies);

    }

    @Override
    public void addFormInfoToBundle(final HMAux item) {
        final Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        //bundle.putString(Constant.ACT007_PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        //bundle.putString(Constant.ACT008_PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        //bundle.putString(Constant.ACT008_SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        //bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        //bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        //bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));
        //bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));
        // DIFERENTE VERIFICAR
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        //bundle.putString(Constant.ACT013_CUSTOM_FORM_DATA,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        //mView.callAct011(context,bundle);
        //17/08/18
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, item.get(GE_Custom_Form_LocalDao.SITE_CODE));
        //
        if (!item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SCHEDULE)) {
            mView.callAct011(context, bundle);
        } else if (!item.get(GE_Custom_Form_LocalDao.SERIAL_ID).isEmpty()) {
            mView.callAct008(context, bundle);
        } else {
            if (item.get(GE_Custom_Form_LocalDao.REQUIRE_SERIAL).equals("0")
                    && item.get(GE_Custom_Form_LocalDao.ALLOW_NEW_SERIAL_CL).equals("1")
                    ) {
                //16/08/18
                //Se o form agendado requer aprovação via serial, joga user para act008
                //
                if (item.get(GE_Custom_Form_LocalDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("1")) {
                    bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
                    //
                    mView.callAct008(context, bundle);
                } else {
                    ToolBox.alertMSG_YES_NO(
                            context,
                            hmAux_Trans.get("alert_define_serial_ttl"),
                            hmAux_Trans.get("alert_define_serial_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
                                    //
                                    mView.callAct008(context, bundle);
                                }
                            },
                            2,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.callAct011(context, bundle);
                                }
                            }
                    );
                }
            } else {
                mView.callAct011(context, bundle);
            }
        }
    }

    @Override
    public void validateOpenForm(HMAux item) {
        //
        if(ToolBox_Inf.checkFormIsReady(
                context,
                Long.parseLong(item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION))
                )
         ){
            if(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equals(Constant.SYS_STATUS_SCHEDULE)) {
                if (isAnyFormInProcessing(item)) {
                      mView.showMsg(Act013_Main.FORM_IN_PROCESSING, item);
                } else {
                      mView.showMsg(Act013_Main.START_FORM, item);
                }
            }else{
                addFormInfoToBundle(item);
            }
        }else{
            mView.alertFormNotReady();
        }
    }

    public boolean isAnyFormInProcessing(HMAux item) {

        GE_Custom_Form_Local customFormLocal =
                customFormLocalDao.getByString(
                        new GE_Custom_Form_Local_Sql_003(
                                item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                                "0",
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                                item.get(GE_Custom_Form_LocalDao.SERIAL_ID)
                        ).toSqlQuery()
                );

        if(customFormLocal != null){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act){
            case Constant.ACT006:
                mView.callAct006(context);
                break;
            default:
                mView.callAct012(context);
        }
    }
}
