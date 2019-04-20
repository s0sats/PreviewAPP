package com.namoadigital.prj001.ui.act052;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.IO_Blind_Move;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.receiver.WBR_IO_Serial_Process_Download;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Download;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act052_Main_Presenter implements Act052_Main_Contract.I_Presenter {
    private Context context;
    private Act052_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_Serial_Process_Record record;

    public Act052_Main_Presenter(Context context, Act052_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct051();
    }

    @Override
    public void defineIOSerialFlow(HMAux hmAuxRet) {
        if(hmAuxRet.hasConsistentValue(Constant.HMAUX_PROCESS_KEY)) {
            String processType = hmAuxRet.get(Constant.HMAUX_PROCESS_KEY);
            //
            Bundle bundle = new Bundle();

            switch (processType) {
                case ConstantBaseApp.IO_PROCESS_IN_CONF:
                    Toast.makeText(context, "IN_CONF", Toast.LENGTH_SHORT).show();
                    break;
                case ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY:
                    Toast.makeText(context, "IN_PUT_AWAY", Toast.LENGTH_SHORT).show();
                    //callact058
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                    bundle.putString(IO_MoveDao.MOVE_PREFIX, hmAuxRet.get(Constant.HMAUX_PREFIX_KEY));
                    bundle.putString(IO_MoveDao.MOVE_CODE, hmAuxRet.get(Constant.HMAUX_CODE_KEY));
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                    mView.callAct058(bundle);
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE:

                    bundle.putInt(MD_Product_SerialDao.PRODUCT_CODE, Integer.parseInt(hmAuxRet.get(MD_Product_SerialDao.PRODUCT_CODE)));
                    bundle.putInt(MD_Product_SerialDao.SERIAL_CODE, Integer.parseInt(hmAuxRet.get(MD_Product_SerialDao.SERIAL_CODE)));
                    if(hmAuxRet.get(Constant.HMAUX_PLANNED_ZONE_CODE_KEY).isEmpty()) {
                        bundle.putInt(IO_Blind_MoveDao.ZONE_CODE,-1 );
                    }else{
                        bundle.putInt(IO_Blind_MoveDao.ZONE_CODE, Integer.parseInt(hmAuxRet.get(Constant.HMAUX_PLANNED_ZONE_CODE_KEY)));
                    }

                    if(hmAuxRet.get(Constant.HMAUX_PLANNED_LOCAL_CODE_KEY).isEmpty()) {
                        bundle.putInt(IO_Blind_MoveDao.LOCAL_CODE,-1 );
                    }else{
                        bundle.putInt(IO_Blind_MoveDao.LOCAL_CODE, Integer.parseInt(hmAuxRet.get(Constant.HMAUX_PLANNED_LOCAL_CODE_KEY)));
                    }

                    if(hmAuxRet.get(Constant.HMAUX_PLANNED_CLASS_CODE_KEY).isEmpty()) {
                        bundle.putInt(IO_Blind_MoveDao.CLASS_CODE,-1 );
                    }else{
                        bundle.putInt(IO_Blind_MoveDao.CLASS_CODE, Integer.parseInt(hmAuxRet.get(Constant.HMAUX_PLANNED_CLASS_CODE_KEY)));
                    }

                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,Constant.ACT052);
                    mView.callAct058(bundle);
                    //callact058
                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
                    Toast.makeText(context, "OUT_PICKING", Toast.LENGTH_SHORT).show();
                    //callact058
                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_CONF:
                    Toast.makeText(context, "OUT_CONF", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void executeWsProcessDownload(IO_Serial_Process_Record data) {
        if(ToolBox_Con.isOnline(context)){
            mView.setWsProcess(WS_IO_Serial_Process_Download.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_process_download_ttl"),
                    hmAux_Trans.get("dialog_process_download_starting_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Serial_Process_Download.class);
            Bundle bundle = new Bundle();

            bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(data.getProduct_code()));
            bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(data.getSerial_code()));
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public MD_Product getMd_product(String mProduct_id) {
        MD_ProductDao mdProductDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        return mdProductDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        "",
                        mProduct_id
                ).toSqlQuery()
        );
    }

    @Override
    public void createNewSerialFlow(MD_Product_Serial productSerial) {
        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID,productSerial.getSerial_id());
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT052);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
        //
        mView.callAct053(bundle);
    }

    /**
     * Verifica se site logado permite Inbound_auto_create
     * @return Inbound_auto_create
     */
    @Override
    public boolean isSiteInboundAutoCreation() {
        MD_SiteDao mdSiteDao = new MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        MD_Site mdSite = mdSiteDao.getByString(
                new MD_Site_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );
        //
        if(mdSite != null && mdSite.getInbound_auto_create() == 1){
            return true;
        }
        return false;
    }
}
