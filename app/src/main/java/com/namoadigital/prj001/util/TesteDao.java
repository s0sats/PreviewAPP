package com.namoadigital.prj001.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.ui.act027.Act027_Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by neomatrix on 07/07/17.
 */

public class TesteDao {


    public static void Test_SM_SO_Daos(Activity act, Context context) {

//        SM_SODao sm_soDao = new SM_SODao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        SM_SO_FileDao sm_so_fileDao = new SM_SO_FileDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        SM_SO_PackDao sm_so_packDao = new SM_SO_PackDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        SM_SO_ServiceDao sm_so_serviceDao = new SM_SO_ServiceDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        SM_SO_Service_ExecDao sm_so_service_execDao = new SM_SO_Service_ExecDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
//        Gson gsonNoExpose = new GsonBuilder().serializeNulls().create();
//
//
//        SM_Rec rec = gsonNoExpose.fromJson(
//                readLog(R.raw.os_list_namoa, context),
//                SM_Rec.class
//        );
//
//        for (int i = 0; i < rec.getSo().size(); i++) {
//            rec.getSo().get(i).setPK();
//        }
//
//        sm_soDao.addUpdate(rec.getSo(), false);

        //sm_soDao.addUpdate(rec.getSo().get(0));

        //sm_soDao.addUpdate(rec.getSo(), false);

//        int i = 10;


//        SM_SO sSO = sm_soDao.getByString(
//
//                new SM_SO_Sql_001(
//                        rec.getSo().get(0).getCustomer_code(),
//                        rec.getSo().get(0).getSo_prefix(),
//                        rec.getSo().get(0).getSo_code()
//                ).toSqlQuery()
//        );
//
        HMAux hmAux = new HMAux();
        hmAux.put(SM_SODao.SO_PREFIX, "2017");
        hmAux.put(SM_SODao.SO_CODE, "30");

        callAct027(act, context, hmAux);

    }

    public static void callAct027(Activity act, Context context, HMAux so) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(SM_SODao.SO_PREFIX, so.get(SM_SODao.SO_PREFIX));
        bundle.putString(SM_SODao.SO_CODE, so.get(SM_SODao.SO_CODE));
        mIntent.putExtras(bundle);

        act.startActivity(mIntent);
        act.finish();
    }

    public static String readLog(int iID, Context context) {
        StringBuilder contents = new StringBuilder();

        try {

            InputStream inputStream = context.getResources().openRawResource(
                    iID);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader input = new BufferedReader(inputreader);

            try {
                String line = null;

                while ((line = input.readLine()) != null) {
                    contents.append(line);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }

}
