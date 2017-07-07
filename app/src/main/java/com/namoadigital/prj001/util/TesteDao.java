package com.namoadigital.prj001.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.model.SM_Rec;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by neomatrix on 07/07/17.
 */

public class TesteDao {


    public static void Test_SM_SO_Daos(Context context) {

        SM_SODao sm_soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO_FileDao sm_so_fileDao = new SM_SO_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO_PackDao sm_so_packDao = new SM_SO_PackDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO_ServiceDao sm_so_serviceDao = new SM_SO_ServiceDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO_Service_ExecDao sm_so_service_execDao = new SM_SO_Service_ExecDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        Gson gsonNoExpose = new GsonBuilder().serializeNulls().create();


        SM_Rec rec = gsonNoExpose.fromJson(
                readLog(R.raw.os_list_namoa, context),
                SM_Rec.class
        );

        rec.getSo().get(0).setPK();

        //sm_soDao.addUpdate(rec.getSo().get(0));

        //sm_soDao.addUpdate(rec.getSo(), false);

        SM_SO sSO = sm_soDao.getByString(

                new SM_SO_Sql_001(
                        rec.getSo().get(0).getCustomer_code(),
                        rec.getSo().get(0).getSo_prefix(),
                        rec.getSo().get(0).getSo_code()
                ).toSqlQuery()
        );

        //


//        ArrayList<SM_SO_Service> hugo = (ArrayList<SM_SO_Service>) sm_so_serviceDao.query(new SM_SO_Service_Sql_002(
//                1,
//                2017,
//                9,
//                1,
//                2,
//                1
//        ).toSqlQuery());
//
//        int tamanho = hugo.size();


        int i = 10;

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
