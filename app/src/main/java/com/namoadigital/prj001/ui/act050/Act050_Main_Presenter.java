package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SO_Creation_Obj;
import com.namoadigital.prj001.receiver.WBR_SO_Client_List;
import com.namoadigital.prj001.receiver.WBR_SO_Creation_Save;
import com.namoadigital.prj001.receiver.WBR_SO_Favorite_List;
import com.namoadigital.prj001.service.WS_SO_Client_List;
import com.namoadigital.prj001.service.WS_SO_Creation_Save;
import com.namoadigital.prj001.service.WS_SO_Favorite_List;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act050_Main_Presenter implements Act050_Main_Contract.I_Presenter {

    private Context context;
    private Act050_Main mView;
    private HMAux hmAux_Trans;

    public Act050_Main_Presenter(Context context, Act050_Main mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public boolean getProductSerial(long productCode, long serialCode) {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        MD_Product_Serial mdProductSerial =  serialDao.getByString(
                new MD_Product_Serial_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        productCode,
                        (int) serialCode
                ).toSqlQuery()
        );
        //
        if(mdProductSerial != null && mdProductSerial.getCustomer_code() > 0){
            mView.setProductSerial(mdProductSerial);
            return true;
        }
        //
        return false;
    }

    @Override
    public void getFavoriteList(long productCode, long serialCode, int categoryPriceCode, int segmentCode, MD_Product_Serial mdProductSerial) {

        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_SO_Favorite_List.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_searching_favorite_ttl"),
                    hmAux_Trans.get("dialog_searching_favorite_msg")
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
            showNoConnectionDialog(context, mdProductSerial);
        }
    }

    @Override
    public void executeWsSoClient() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_SO_Client_List.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_loading_client_list_ttl"),
                    hmAux_Trans.get("dialog_loading_client_list_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Client_List.class);
            Bundle bundle = new Bundle();
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void executeWsSoCreation(SO_Creation_Obj mSoCreation) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_SO_Creation_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_so_creating_ttl"),
                    hmAux_Trans.get("dialog_so_creating_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Creation_Save.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(WS_SO_Creation_Save.SO_CREATION_OBJ_KEY,mSoCreation);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processSoCreationRet(final HMAux hmAuxRet) {
        if(hmAuxRet.hasConsistentValue(SM_SODao.SO_PREFIX)
           && hmAuxRet.hasConsistentValue(SM_SODao.SO_CODE)
           && !hmAuxRet.get(SM_SODao.SO_PREFIX).equals("0")
           && !hmAuxRet.get(SM_SODao.SO_CODE).equals("0")
        ){
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_creation_return_ttl"),
                    hmAux_Trans.get("alert_so_creation_return_success"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = new Bundle();
                            //
                            bundle.putString(SM_SODao.SO_PREFIX,hmAuxRet.get(SM_SODao.SO_PREFIX));
                            bundle.putString(SM_SODao.SO_CODE,hmAuxRet.get(SM_SODao.SO_CODE));
                            //
                            mView.callAct027(context,bundle);
                        }
                    },
                    0
            );
        }else{
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_creation_return_ttl"),
                    hmAux_Trans.get("alert_so_creation_return_error") +"\n"+
                         hmAuxRet.get(WS_SO_Creation_Save.SO_CREATION_MSG_KEY) ,
                    null,
                    0
            );
        }
    }

    @Override
    public void onBackPressedClicked(final FragmentManager fm, final MD_Product_Serial mdProductSerial, final boolean isEmptyList) {
        int count = fm.getBackStackEntryCount();
        //
        switch (count){
            case 0:
                mView.callAct005(context);
            case 1:
                callAct023(mdProductSerial);
                break;
            case 2:
                //Se o voltar foi chamada do fragmento de parametros,
                //Informa que os dados serão perdidos caso ele continuar.
                ToolBox.alertMSG_YES_NO(
                        context,
                        hmAux_Trans.get("alert_leave_so_creation_ttl"),
                        hmAux_Trans.get("alert_discard_so_creation_confirm"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.clearOSCreationData();
                                if(isEmptyList){
                                    callAct023(mdProductSerial);
                                }else{
                                    fm.popBackStack();
                                }
                            }
                        },
                        1
                );
                break;
            default:
                fm.popBackStack();
        }
    }

    private void callAct023(MD_Product_Serial mdProductSerial) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.MODULE_SO_SEARCH_SERIAL);
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(mdProductSerial.getProduct_code()));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, mdProductSerial.getSerial_id());
        //O serial já foi criado nas etapas anteriores por isso o parametro é falso
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, false);

        //
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, mdProductSerial);
        //
        mView.callAct026(context,bundle);
    }

    public void showNoConnectionDialog(Context act_context, final MD_Product_Serial mdProductSerial) {
        //Chama caixa de dialogo
        ToolBox.alertMSG(
                act_context,
                hmAux_Trans.get("alert_no_conection_ttl"),
                hmAux_Trans.get("alert_no_conection_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callAct023(mdProductSerial);
                    }
                },
                0
        );
    }
}
