package com.namoadigital.prj001.util;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_FileDao;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.CH_File;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.sql.CH_File_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_006;
import com.namoadigital.prj001.sql.GE_File_Sql_001;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_010;
import com.namoadigital.prj001.sql.Sql_Act005_002;
import com.namoadigital.prj001.sql.Sql_Act005_007;
import com.namoadigital.prj001.sql.Sql_Act005_008;
import com.namoadigital.prj001.sql.Sql_Act021_003;
import com.namoadigital.prj001.sql.Sql_Chat_Notification_Helper_001;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * BARRIONUEVO - 12/03/2020
 * <p></p>
 *
 * Classe responsavel pela noticacao de pendencias.
 * Locais de instacia:
 *     ACT005
 *     WS_AP_Save
 *     WS_IO_Blind_Move_Save
 *     WS_IO_Inbound_Item_Save
 *     WS_IO_Move_Save
 *     WS_IO_Outbound_Item_Save
 *     WS_Save
 *     WS_Serial_Save
 *     WS_SO_Approval
 *     WS_SO_Pack_Express_Local
 *     WS_SO_Save
 *     WS_TK_Ticket_Save
 */
public class NotificationHelper {
    public static final int LOCATION_NOTIFICATION_ID = 9999;
    Context context;
    HMAux hmAux_Trans;
    RemoteViews notificationLayoutExpanded;
    int moduleShowCount;
    public NotificationHelper(Context context, HMAux hmAux_Trans) {
        this.context = context;
        this.hmAux_Trans = hmAux_Trans;
        this.notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.namoa_notification_large);
        this.moduleShowCount =0;
    }

    public void call_Notification() {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.namoa_notification_small);


        int locationPendencies = getLocationPendencies();
        //int fileUploadPendencies = getFileUploadPendencies();
        int fileUploadPendencies = getTotalUploadPendencies();
        int updatePendenciesCount = getUpdatePendencies(locationPendencies,fileUploadPendencies );
        String formatLocationPendencies = String.valueOf(locationPendencies);
        String formatFileUploadPendencies = String.valueOf(fileUploadPendencies);
        String updatePendencies = String.valueOf(updatePendenciesCount);
        notificationLayout.setImageViewResource(R.id.iv_location_placeholder, R.drawable.ic_pin_drop_black_24dp);
        notificationLayout.setImageViewResource(R.id.iv_file_upload_placeholder, R.drawable.ic_photo_size_select_actual_black_24dp);
        notificationLayout.setImageViewResource(R.id.iv_update_required_placeholder, R.drawable.ic_mail_black_24dp);
        notificationLayout.setTextViewText(R.id.tv_location_val, formatLocationPendencies);
        notificationLayout.setTextViewText(R.id.tv_file_upload_val, formatFileUploadPendencies);
        notificationLayout.setTextViewText(R.id.tv_update_required_val, updatePendencies);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.namoa_logo_white24x24);
        builder.setOngoing(true);
        builder.setContentTitle(context.getString(R.string.title_notification_generic));
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        builder.setCustomContentView(notificationLayout);
        builder.setCustomBigContentView(notificationLayoutExpanded);

//        RemoteViews view = new RemoteViews(context.context.getPackageName(), R.layout.sv_resume_notification);
        String gps_searching_location = hmAux_Trans.get("gps_searching_location");
        //
        int versao = Build.VERSION.SDK_INT;
        //
        int totalPendency = locationPendencies + fileUploadPendencies + updatePendenciesCount;
        //
        if (totalPendency > 0) {
            if (versao >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                nm.notify(LOCATION_NOTIFICATION_ID, builder.build());
            } else {
                nm.notify(LOCATION_NOTIFICATION_ID, builder.getNotification());
            }
        } else {
            ToolBox_Inf.cancelNotification(context, LOCATION_NOTIFICATION_ID);
        }
    }


    private int getUpdatePendencies(int locationPendencies, int fileUploadPendencies) {

        String qty = setFormNotification();
        String qtySO = setSoNotification();
        String qtyAP = setFormApNotification();
        String qtySerial = setSerialNotification();
        String qtySO_Express = setSoExpressNotification();

        String qtyAssets = "0";
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_IO, null)) {

            moduleShowCount++;
            qtyAssets = ToolBox_Inf.handleAssetsWaitingSync(context, ToolBox_Con.getPreference_Customer_Code(context));
            notificationLayoutExpanded.setViewVisibility(R.id.tv_assets, View.VISIBLE);
        }else{
            notificationLayoutExpanded.setViewVisibility(R.id.tv_assets, View.GONE);
        }
        String qtyTicket = "0";
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET, null)) {
            Log.i("TESTE", "PROFILE_MENU_TICKET");
            moduleShowCount++;
            qtyTicket = ToolBox_Inf.handleTicketUpdateRequired(context, ToolBox_Con.getPreference_Customer_Code(context));
            notificationLayoutExpanded.setViewVisibility(R.id.tv_ticket, View.VISIBLE);
        }else{
            notificationLayoutExpanded.setViewVisibility(R.id.tv_ticket, View.GONE);
        }

        notificationLayoutExpanded.setTextViewText(R.id.tv_form, hmAux_Trans.get("sys_notification_pendency_form_lbl") + " " + qty);
        notificationLayoutExpanded.setTextViewText(R.id.tv_form_ap, hmAux_Trans.get("sys_notification_pendency_form_ap_lbl") + " " + qtyAP);
        notificationLayoutExpanded.setTextViewText(R.id.tv_serial, hmAux_Trans.get("sys_notification_pendency_serial_lbl") + " " + qtySerial);
        notificationLayoutExpanded.setTextViewText(R.id.tv_assets, hmAux_Trans.get("sys_notification_pendency_assets_lbl") + " " + qtyAssets);
        notificationLayoutExpanded.setTextViewText(R.id.tv_services, hmAux_Trans.get("sys_notification_pendency_services_lbl") + " " + qtySO);
        notificationLayoutExpanded.setTextViewText(R.id.tv_express_services, hmAux_Trans.get("sys_notification_pendency_express_services_lbl") + " " + qtySO_Express);
        notificationLayoutExpanded.setTextViewText(R.id.tv_ticket, hmAux_Trans.get("sys_notification_pendency_ticket_lbl") + " " + qtyTicket);
        notificationLayoutExpanded.setViewVisibility(R.id.hdr_notification, View.VISIBLE);
        notificationLayoutExpanded.setImageViewResource(R.id.iv_location_placeholder, R.drawable.ic_pin_drop_black_24dp);
        notificationLayoutExpanded.setImageViewResource(R.id.iv_file_upload_placeholder, R.drawable.ic_photo_size_select_actual_black_24dp);
        notificationLayoutExpanded.setImageViewResource(R.id.iv_update_required_placeholder, R.drawable.ic_mail_black_24dp);

        int totalPendency = ToolBox_Inf.convertStringToInt(qty);
        totalPendency += ToolBox_Inf.convertStringToInt(qtyAP);
        totalPendency += ToolBox_Inf.convertStringToInt(qtyAssets);
        totalPendency += ToolBox_Inf.convertStringToInt(qtySerial);
        totalPendency += ToolBox_Inf.convertStringToInt(qtySO);
        totalPendency += ToolBox_Inf.convertStringToInt(qtySO_Express);
        totalPendency += ToolBox_Inf.convertStringToInt(qtyTicket);
        Log.i("TESTE", "moduleShowCount=" +moduleShowCount);
        if(moduleShowCount > 2) {
            notificationLayoutExpanded.setViewVisibility(R.id.hdr_notification, View.GONE);
        }else {
            notificationLayoutExpanded.setViewVisibility(R.id.hdr_notification, View.VISIBLE);
        }
        notificationLayoutExpanded.setTextViewText(R.id.tv_location_val, String.valueOf(locationPendencies));
        notificationLayoutExpanded.setTextViewText(R.id.tv_file_upload_val, String.valueOf(fileUploadPendencies));
        notificationLayoutExpanded.setTextViewText(R.id.tv_update_required_val, String.valueOf(totalPendency));
        return totalPendency;
    }

    private String setSoExpressNotification() {
        String qtySO_Express="0";
        SO_Pack_Express_LocalDao soPackExpressLocalDao = new SO_Pack_Express_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, ConstantBaseApp.PROFILE_MENU_SO_EXPRESS)) {
            Log.i("TESTE", "PROFILE_PRJ001_SO");
            notificationLayoutExpanded.setViewVisibility(R.id.tv_express_services, View.VISIBLE);
            try {
                qtySO_Express = soPackExpressLocalDao.getByStringHM(
                        new SO_Pack_Express_Local_Sql_010(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                ).get(SO_Pack_Express_Local_Sql_010.BADGE_IN_NEW_QTY);
            } catch (Exception e) {
                qtySO_Express = "0";
            }
        } else {
            notificationLayoutExpanded.setViewVisibility(R.id.tv_express_services, View.GONE);
        }
        return qtySO_Express;
    }

    private String setSerialNotification() {
        String qtySerial="0";

        MD_ProductDao mdProductDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, null)) {
            Log.i("TESTE", "PROFILE_PRJ001_PRODUCT_SERIAL");
            moduleShowCount++;
            notificationLayoutExpanded.setViewVisibility(R.id.tv_serial, View.VISIBLE);
            try {
                qtySerial = mdProductDao.getByStringHM(
                        new Sql_Act005_008(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                ).get(Sql_Act005_008.BADGE_TO_SEND_QTY);
            } catch (Exception e) {
                qtySerial = "0";
            }
        }else{
            notificationLayoutExpanded.setViewVisibility(R.id.tv_serial, View.GONE);
        }

        return qtySerial;
    }

    private String setFormApNotification() {
        String qtyAP="0";
        GE_Custom_Form_ApDao customFormApDao = new GE_Custom_Form_ApDao(context);
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_AP, null)) {
            Log.i("TESTE", "PROFILE_PRJ001_AP");
            moduleShowCount++;
            notificationLayoutExpanded.setViewVisibility(R.id.tv_form_ap, View.VISIBLE);
            try {
                qtyAP = customFormApDao.getByStringHM(
                        new Sql_Act005_007(
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                        ).toSqlQuery()
                ).get(Sql_Act005_007.BADGE_TO_SEND_QTY);
            } catch (Exception e) {
                qtyAP = "0";
            }
        } else {
            notificationLayoutExpanded.setViewVisibility(R.id.tv_form_ap, View.GONE);
        }
        return qtyAP;
    }

    private String setFormNotification() {
        String qty = "0";

        GE_Custom_Form_LocalDao customFormLocalDao = new GE_Custom_Form_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_CHECKLIST, null)) {
            Log.i("TESTE", "PROFILE_PRJ001_CHECKLIST");
            moduleShowCount++;
            notificationLayoutExpanded.setViewVisibility(R.id.tv_form, View.VISIBLE);
            try {
                qty = customFormLocalDao.getByStringHM(
                        new Sql_Act005_002(
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                        ).toSqlQuery()
                ).get(Sql_Act005_002.BADGE_WAITING_SYNC_QTY);
            } catch (Exception e) {
                qty = "0";
            }
        } else {
            notificationLayoutExpanded.setViewVisibility(R.id.tv_form, View.GONE);
        }
        return qty;
    }

    private String setSoNotification() {
        String qtySO = "0";

        SM_SODao soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
            Log.i("TESTE", "PROFILE_PRJ001_SO");
            notificationLayoutExpanded.setViewVisibility(R.id.tv_services, View.VISIBLE);
            moduleShowCount++;
            try {
                qtySO = soDao.getByStringHM(
                        new Sql_Act021_003(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                ).get(Sql_Act021_003.UPDATE_APPROVAL_REQUIRED_QTY);
            } catch (Exception e) {
                qtySO = "0";
            }
        } else {
            notificationLayoutExpanded.setViewVisibility(R.id.tv_services, View.GONE);
        }
        return qtySO;
    }

    private int getFileUploadPendencies() {
        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        ArrayList<GE_File> geFiles = (ArrayList<GE_File>) geFileDao.query(
                new GE_File_Sql_001().toSqlQuery()
        );
        if (geFiles != null) {
            return geFiles.size();
        }
        return 0;
    }

    /**
     * LUCHE - 02/04/2020
     * Criado metodo que retorna a qtd de fotos pendentes de envio do chat.
     * Verifica a tabela ch_files e tb msg, buscando imagen sem pk do server.(antes de ir para a ch_files)
     * @return Qtd de foto pendentes de envio do chat
     */
    private int getChatUploadPendencies(){
        int qtdImgs = 0;
        try {
            CH_FileDao chFileDao = new CH_FileDao(
                context
            );
            CH_MessageDao chMessageDao = new CH_MessageDao(
                context
            );
            //Lista imgs de msgs que JA POSSUEM prefixo e code, mas que não foram envadas
            ArrayList<CH_File> chFiles = (ArrayList<CH_File>) chFileDao.query(
                new CH_File_Sql_001().toSqlQuery()
            );
            //
            if (chFiles != null) {
                qtdImgs += chFiles.size();
            }
            //Lista imgs de msgs que NÃO POSSUEM prefixo e code, mas que não foram enviadas
            List<CH_Message> chImgMsgs = chMessageDao.query(
                new Sql_Chat_Notification_Helper_001(
                    ToolBox_Con.getPreference_User_Code(context)
                ).toSqlQuery()
            );
            //
            if (chImgMsgs != null) {
                qtdImgs += chImgMsgs.size();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //
        return qtdImgs;
    }

    /**
     * LUCHE - 02/04/2020
     * Metodo que soma as imagens pendente de envio do banco c_customer e chat
     * @return Qtd total de imgs pendentes de envio.
     */
    private int getTotalUploadPendencies(){
        return getFileUploadPendencies() + getChatUploadPendencies();
    }

    private int getLocationPendencies() {
        GE_Custom_Form_DataDao ge_custom_form_dataDao = new GE_Custom_Form_DataDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);

        List<GE_Custom_Form_Data> formDataList = getFormDataList(ToolBox_Con.getPreference_Customer_Code(context), ge_custom_form_dataDao);
        if (formDataList != null) {
            return formDataList.size();
        }
        return 0;
    }

    private List<GE_Custom_Form_Data> getFormDataList(long customer_code, GE_Custom_Form_DataDao ge_custom_form_dataDao) {
        return ge_custom_form_dataDao.query(
                new GE_Custom_Form_Data_Sql_006(customer_code).toSqlQuery()
        );
    }
}
