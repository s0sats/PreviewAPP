package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by DANIEL.LUCHE on 07/03/2017.
 */

public class WS_DownLoad_Customer_Logo extends IntentService {

    public WS_DownLoad_Customer_Logo() {
        super("WS_DownLoad_Customer_Logo");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            if(!ToolBox_Inf.isDownloadRunning()){
                //Log.v("WS_Customer_Logo","true");
                //WBR_DownLoad_Customer_Logo.IS_RUNNING = true;
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }

            WBR_DownLoad_Customer_Logo.IS_RUNNING = true;

            Bundle bundle = intent.getExtras();

            EV_User_CustomerDao userCustomerDao =
                    new EV_User_CustomerDao(
                            getApplicationContext(),
                            Constant.DB_FULL_BASE,
                            Constant.DB_VERSION_BASE
                    );

            EV_User_Customer userCustomer =
                    userCustomerDao.getByString(
                            new EV_User_Customer_Sql_002(
                                    ToolBox_Con.getPreference_User_Code(getApplicationContext()),
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()))
                            ).toSqlQuery()
                    );
            String logo_prefix = "logo_c_" + userCustomer.getCustomer_code();


                if (!ToolBox_Inf.verifyDownloadFileInf(logo_prefix + ".png",Constant.IMG_PATH)) {

                    ToolBox_Inf.deleteDownloadFileInf(logo_prefix + ".tmp",Constant.IMG_PATH);
                    //
                    ToolBox_Inf.downloadImagePDF(
                            userCustomer.getLogo_url(),
                            Constant.IMG_PATH + "/" + logo_prefix + ".tmp"
                    );

                    //Verfica se downlaod esta ok
                    if(ToolBox_Inf.verifyImgIntegrity(Constant.IMG_PATH,logo_prefix + ".tmp")){
                        //Extensão sempre .png ,
                        //pois no android le a imagens independente da extensão
                        ToolBox_Inf.renameDownloadFileInfV2(Constant.IMG_PATH,logo_prefix,"",".png");
                    }else{
                      throw new Exception("ImgIntegrity");
                    }
                }

        } catch (Exception e) {
            String results = e.toString();
            ToolBox_Inf.registerException(getClass().getName(),e);
        }finally {
            WBR_DownLoad_Customer_Logo.IS_RUNNING = false;
            WBR_DownLoad_Customer_Logo.completeWakefulIntent(intent);
            //Log.v("WS_Customer_Logo","false");
            if(!ToolBox_Inf.isDownloadRunning()){
                ToolBox_Inf.cancelNotification(getApplicationContext(),Constant.NOTIFICATION_DOWNLOAD);
            }
        }
    }

    private boolean verifyDownloadLogo(final String prefix) {
        File fileList = new File(Constant.CACHE_PATH);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(prefix) && !filename.endsWith(".tmp")) {
                    return true;
                }
                return false;
            }
        });
        //
        if (files != null) {
           return false;
        }
        //
        return true;
    }

}
