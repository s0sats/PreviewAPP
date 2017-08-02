package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.TaskControl;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task_File;
import com.namoadigital.prj001.receiver.WBR_SO_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_008;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Task extends BaseFragment implements TaskControl.ITaskControl {

    private Context context;

    private TextView tv_exec_tmp_label;
    private TextView tv_exec_tmp_value;

    private TextView tv_task_tmp_label;
    private TextView tv_task_tmp_value;

    private TaskControl taskControl;

    private Button btn_cancel_task;

    private SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao;

    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;
    private SM_SO_Service_Exec_Task sm_so_service_exec_task;

    private SM_SO_Service_Exec_TaskDao sm_so_service_execDao;

    private SM_SODao soDao;

    private HashMap<String, String> data;

    private boolean sdAvoid = false;

    public interface IAct028_Task {
        void exec_list_opc_update();
    }

    private IAct028_Task delegate;

    public void setOnExec_List_Opc_Update(IAct028_Task delegate) {
        this.delegate = delegate;
    }

    public void changeData(HMAux newData) {
        if (data != null) {
            data.put("exec_code", newData.get("exec_code"));
            data.put("task_code", newData.get("exec_task"));
        }
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
        //
        tempValues.put("qty", data.get("qty_people"));
        tempValues.put("perc", data.get("task_perc"));
        tempValues.put("dts", data.get("start_date"));
        tempValues.put("dte", data.get("end_date"));
        tempValues.put("comments", data.get("comments"));
        tempValues.put("img", "");
        //
        sdAvoid = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act028_task_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        tempValues.put("qty", taskControl.getmQty_People());
        tempValues.put("perc", taskControl.getmPerc());
        tempValues.put("dts", taskControl.getmDtStart());
        tempValues.put("dte", taskControl.getmDtEnd());
        tempValues.put("comments", taskControl.getmComments());
        tempValues.put("img", taskControl.getmImgPath());
    }

    @Override
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    public void updateTaskOnLeave() {
        try {
            if (sm_so_service_exec_task != null && sm_so_service_exec_task.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PROCESS)) {
                sm_so_service_exec_task.setQty_people(Integer.parseInt(taskControl.getmQty_People()));
                sm_so_service_exec_task.setTask_perc(Integer.parseInt(taskControl.getmValue()));
                sm_so_service_exec_task.setStart_date(ToolBox.convertToDeviceTMZ2(taskControl.getmDtStart()));
                sm_so_service_exec_task.setEnd_date(ToolBox.convertToDeviceTMZ2(taskControl.getmDtEnd()));
                sm_so_service_exec_task.setComments(taskControl.getmComments());
                sm_so_service_exec_task.setTask_file(recoverTaskFiles(sm_so_service_exec_task.getTask_file(), taskControl.getmImgPath()));
                //
                //sm_so_service_exec_task.setPK(sm_so_service_exec);
                //
                sm_so_service_exec_taskDao.addUpdateTmp(sm_so_service_exec_task);

                // Include Files to Upload
                uploadFiles(sm_so_service_exec_task);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void iniVar(View view) {
        context = getActivity();

        soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_execDao = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

//        sm_so_service_exec_task = sm_so_service_exec_taskDao.getByString(
//
//                new SM_SO_Service_Exec_Task_Sql_005(
//                        Long.parseLong(data.get("customer_code")),
//                        Integer.parseInt(data.get("so_prefix")),
//                        Integer.parseInt(data.get("so_code")),
//                        Integer.parseInt(data.get("price_list_code")),
//                        Integer.parseInt(data.get("pack_code")),
//                        Integer.parseInt(data.get("pack_seq")),
//                        Integer.parseInt(data.get("category_price_code")),
//                        Integer.parseInt(data.get("service_code")),
//                        Integer.parseInt(data.get("service_seq")),
//                        Integer.parseInt(data.get("exec_tmp")),
//                        Integer.parseInt(data.get("task_tmp"))
//                ).toSqlQuery()
//
//        );

        tv_exec_tmp_label = (TextView) view.findViewById(R.id.act028_task_content_tv_exec_tmp_label);
        tv_exec_tmp_value = (TextView) view.findViewById(R.id.act028_task_content_tv_exec_tmp_value);

        tv_task_tmp_label = (TextView) view.findViewById(R.id.act028_task_content_tv_task_tmp_label);
        tv_task_tmp_value = (TextView) view.findViewById(R.id.act028_task_content_tv_task_tmp_value);

        taskControl = (TaskControl) view.findViewById(R.id.act028_task_content_tc_task_value);

        btn_cancel_task = (Button) view.findViewById(R.id.act028_task_content_btn_cancel_task);

        setHMAuxScreen();

        controls_task.add(taskControl);
    }

    private void iniAction() {

        btn_cancel_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToolBox.alertMSG(
                        context,
                        "Cancelamento de Task",
                        "Deseja cancelar a Task?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sm_so_service_exec_task.setStatus(Constant.SO_STATUS_CANCELLED);
                                sm_so_service_exec_task.setQty_people(Integer.parseInt(taskControl.getmQty_People()));
                                sm_so_service_exec_task.setTask_perc(Integer.parseInt(taskControl.getmValue()));
                                sm_so_service_exec_task.setStart_date(ToolBox.convertToDeviceTMZ2(ToolBox.convertToDeviceTMZ2(taskControl.getmDtStart())));
                                sm_so_service_exec_task.setEnd_date(ToolBox.convertToDeviceTMZ2(ToolBox.convertToDeviceTMZ2(taskControl.getmDtEnd())));
                                sm_so_service_exec_task.setComments(taskControl.getmComments());
                                sm_so_service_exec_task.setTask_file(recoverTaskFiles(sm_so_service_exec_task.getTask_file(), taskControl.getmImgPath()));
                                //
                                //sm_so_service_exec_task.setPK(sm_so_service_exec);
                                //
                                sm_so_service_exec_taskDao.addUpdateTmp(sm_so_service_exec_task);

                                // Include Files to Upload
                                uploadFiles(sm_so_service_exec_task);

                                /**
                                 * Calling WebService
                                 */
                                SM_SO so = soDao.getByString(
                                        new SM_SO_Sql_001(
                                                ToolBox_Con.getPreference_Customer_Code(context),
                                                sm_so_service_exec_task.getSo_prefix(),
                                                sm_so_service_exec_task.getSo_code()
                                        ).toSqlQuery()
                                );

                                so.setUpdate_required(1);
                                soDao.addUpdate(so);

                                callSoSave(sm_so_service_exec_task.getSo_prefix(), sm_so_service_exec_task.getSo_code());
                            }
                        },
                        1,
                        false
                );

//                sm_so_service_exec_task.setStatus(Constant.SO_STATUS_CANCELLED);
//                sm_so_service_exec_task.setQty_people(Integer.parseInt(taskControl.getmQty_People()));
//                sm_so_service_exec_task.setTask_perc(Integer.parseInt(taskControl.getmValue()));
//                sm_so_service_exec_task.setStart_date(ToolBox.convertToDeviceTMZ2(ToolBox.convertToDeviceTMZ2(taskControl.getmDtStart())));
//                sm_so_service_exec_task.setEnd_date(ToolBox.convertToDeviceTMZ2(ToolBox.convertToDeviceTMZ2(taskControl.getmDtEnd())));
//                sm_so_service_exec_task.setComments(taskControl.getmComments());
//                //
//                //sm_so_service_exec_task.setPK(sm_so_service_exec);
//                //
//                sm_so_service_exec_taskDao.addUpdateTmp(sm_so_service_exec_task);
//
//                /**
//                 * Calling WebService
//                 */
//                SM_SO so = soDao.getByString(
//                        new SM_SO_Sql_001(
//                                ToolBox_Con.getPreference_Customer_Code(context),
//                                sm_so_service_exec_task.getSo_prefix(),
//                                sm_so_service_exec_task.getSo_code()
//                        ).toSqlQuery()
//                );
//
//                so.setUpdate_required(1);
//                soDao.addUpdate(so);
//
//                callSoSave(sm_so_service_exec_task.getSo_prefix(), sm_so_service_exec_task.getSo_code());
            }
        });

    }

    public void setHMAuxScreen() {
        try {
            if (data != null) {

                sm_so_service_exec_task = sm_so_service_exec_taskDao.getByString(

                        new SM_SO_Service_Exec_Task_Sql_005(
                                Long.parseLong(data.get("customer_code")),
                                Integer.parseInt(data.get("so_prefix")),
                                Integer.parseInt(data.get("so_code")),
                                Integer.parseInt(data.get("price_list_code")),
                                Integer.parseInt(data.get("pack_code")),
                                Integer.parseInt(data.get("pack_seq")),
                                Integer.parseInt(data.get("category_price_code")),
                                Integer.parseInt(data.get("service_code")),
                                Integer.parseInt(data.get("service_seq")),
                                Long.parseLong(data.get("exec_tmp")),
                                Long.parseLong(data.get("task_tmp"))
                        ).toSqlQuery()

                );

                tv_exec_tmp_label.setText("Exec TMP");
                tv_exec_tmp_value.setText(data.get("exec_tmp"));

                tv_task_tmp_label.setText("Task TMP");
                tv_task_tmp_value.setText(data.get("task_tmp"));

                btn_cancel_task.setText(hmAux_Trans.get("btn_cancel_task"));

                taskControl.setmLabel("Task Lavel");
                taskControl.setmValue(data.get("task_perc"), false);
                taskControl.setmPerc(data.get("task_perc"));
                taskControl.setmQty_People_label("Qty People");
                taskControl.setmQty_People(data.get("qty_people"), false);
                taskControl.setmDtStart(data.get("start_date"));
                taskControl.setmDtEnd(data.get("end_date"));
                taskControl.setmComments(data.get("comments"));
                taskControl.setmMaxImages(4);
                taskControl.setmType(data.get("exec_type"));
                taskControl.setOnInformTaskStatusListener(this);

                StringBuilder sFiles = new StringBuilder();

                boolean bFirst = true;

                for (SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file : sm_so_service_exec_task.getTask_file()) {

                    if (!sm_so_service_exec_task_file.getFile_name().isEmpty()) {

                        if (bFirst) {
                            sFiles.append(sm_so_service_exec_task_file.getFile_name());
                            bFirst = false;
                        } else {
                            sFiles.append("#");
                            sFiles.append(sm_so_service_exec_task_file.getFile_name());
                        }

                    }
                }

                taskControl.setmImgPath(sFiles.toString());

                try {

                    taskControl.setmValue(tempValues.get("perc"));
                    taskControl.setmPerc(tempValues.get("perc"));
                    taskControl.setmQty_People(tempValues.get("qty"));
                    taskControl.setmDtStart(tempValues.get("dts"));
                    taskControl.setmDtEnd(tempValues.get("dte"));
                    taskControl.setmComments(tempValues.get("comments"));

                    if (sdAvoid) {
                        sdAvoid = false;
                    } else {
                        taskControl.setmImgPath(tempValues.get("img"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                processTaskStatus();

//                switch (data.get("exec_status").toUpperCase()) {
//                    case "PENDING":
//                        processTaskStatus();
//                        break;
//                    case "PROCESS":
//                        processTaskStatus();
//                        break;
//                    default:
//                        taskControl.setmEnabled(false);
//                        break;
//                }

            }
        } catch (Exception e) {
        }

    }

    private void processTaskStatus() {

        if (data.get("task_user").toUpperCase().equalsIgnoreCase(ToolBox_Con.getPreference_User_Code(getActivity()))) {

            if (data.get("full_status").equalsIgnoreCase("1")) {

                switch (data.get("task_status").toUpperCase()) {
                    case Constant.SO_STATUS_PROCESS:

                        btn_cancel_task.setVisibility(View.VISIBLE);

                        taskControl.setmStatus("EXEC");
                        taskControl.setmEnabled(true);
                        break;

                    default:
                        btn_cancel_task.setVisibility(View.GONE);

                        taskControl.setmStatus(data.get("task_status").toUpperCase());
                        taskControl.setmEnabled(false);
                        break;
                }
            } else {
                btn_cancel_task.setVisibility(View.GONE);

                taskControl.setmStatus(data.get("task_status").toUpperCase());
                taskControl.setmEnabled(false);

            }

        } else {

            btn_cancel_task.setVisibility(View.GONE);

            taskControl.setmStatus(data.get("task_status").toUpperCase());
            taskControl.setmEnabled(false);
        }
    }

    private void saveTask() {
        sm_so_service_exec_task.setStatus(Constant.SO_STATUS_DONE);
        sm_so_service_exec_task.setQty_people(Integer.parseInt(taskControl.getmQty_People()));
        sm_so_service_exec_task.setTask_perc(Integer.parseInt(taskControl.getmValue()));
        sm_so_service_exec_task.setStart_date(ToolBox.convertToDeviceTMZ2(taskControl.getmDtStart()));
        sm_so_service_exec_task.setEnd_date(ToolBox.convertToDeviceTMZ2(taskControl.getmDtEnd()));
        sm_so_service_exec_task.setComments(taskControl.getmComments());
        sm_so_service_exec_task.setTask_file(recoverTaskFiles(sm_so_service_exec_task.getTask_file(), taskControl.getmImgPath()));
        //
        //sm_so_service_exec_task.setPK(sm_so_service_exec);
        //
        sm_so_service_exec_taskDao.addUpdateTmp(sm_so_service_exec_task);

        // Include Files to Upload
        uploadFiles(sm_so_service_exec_task);

        /**
         * Calling WebService
         */
        SM_SO so = soDao.getByString(
                new SM_SO_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        sm_so_service_exec_task.getSo_prefix(),
                        sm_so_service_exec_task.getSo_code()
                ).toSqlQuery()
        );

        so.setUpdate_required(1);
        soDao.addUpdate(so);

        if (sm_so_service_exec_task.getTask_perc() == 100) {
            sm_so_service_execDao.addUpdate(
                    new SM_SO_Service_Exec_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            sm_so_service_exec_task.getSo_prefix(),
                            sm_so_service_exec_task.getSo_code(),
                            sm_so_service_exec_task.getPrice_list_code(),
                            sm_so_service_exec_task.getPack_code(),
                            sm_so_service_exec_task.getPack_seq(),
                            sm_so_service_exec_task.getCategory_price_code(),
                            sm_so_service_exec_task.getService_code(),
                            sm_so_service_exec_task.getService_seq(),
                            sm_so_service_exec_task.getExec_tmp()
                    ).toSqlQuery()
            );

            if (delegate != null) {
                delegate.exec_list_opc_update();
            }
        }

        callSoSave(sm_so_service_exec_task.getSo_prefix(), sm_so_service_exec_task.getSo_code());
    }

    private void uploadFiles(SM_SO_Service_Exec_Task sm_so_service_exec_task) {
        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        ArrayList<GE_File> geFiles = new ArrayList<>();

        for (int i = 0; i < sm_so_service_exec_task.getTask_file().size(); i++) {
            String sFile_v = sm_so_service_exec_task.getTask_file().get(i).getFile_name();

            if (sFile_v.endsWith(".jpg")) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_v);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_v.replace(".jpg", ""));
                    geFile.setFile_path(sFile_v);
                    geFile.setFile_status("OPENED");
                    geFile.setFile_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss"));

                    geFiles.add(geFile);
                }
            }
        }

        geFileDao.addUpdate(geFiles, false);
    }

    @Override
    public void informTaskStatus(String s, String s1) {

    }

    @Override
    public void informTaskActiveClosed(int i, String s) {

        String tts = taskControl.getmDtStart();
        String tte = taskControl.getmDtEnd();


        String st = ToolBox.convertToDeviceTMZ2(taskControl.getmDtStart());

        sm_so_service_exec_task.setStatus(Constant.SO_STATUS_DONE);
        sm_so_service_exec_task.setQty_people(Integer.parseInt(taskControl.getmQty_People()));
        sm_so_service_exec_task.setTask_perc(Integer.parseInt(taskControl.getmValue()));
        sm_so_service_exec_task.setStart_date(ToolBox.convertToDeviceTMZ2(taskControl.getmDtStart()));
        sm_so_service_exec_task.setEnd_date(ToolBox.convertToDeviceTMZ2(taskControl.getmDtEnd()));
        sm_so_service_exec_task.setComments(taskControl.getmComments());
        sm_so_service_exec_task.setTask_file(recoverTaskFiles(sm_so_service_exec_task.getTask_file(), taskControl.getmImgPath()));
        //
        //sm_so_service_exec_task.setPK(sm_so_service_exec);
        //
        sm_so_service_exec_taskDao.addUpdateTmp(sm_so_service_exec_task);

        // Include Files to Upload
        uploadFiles(sm_so_service_exec_task);

        /**
         * Calling WebService
         */
        SM_SO so = soDao.getByString(
                new SM_SO_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        sm_so_service_exec_task.getSo_prefix(),
                        sm_so_service_exec_task.getSo_code()
                ).toSqlQuery()
        );

        so.setUpdate_required(1);
        soDao.addUpdate(so);

        if (sm_so_service_exec_task.getTask_perc() == 100) {
            sm_so_service_execDao.addUpdate(
                    new SM_SO_Service_Exec_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            sm_so_service_exec_task.getSo_prefix(),
                            sm_so_service_exec_task.getSo_code(),
                            sm_so_service_exec_task.getPrice_list_code(),
                            sm_so_service_exec_task.getPack_code(),
                            sm_so_service_exec_task.getPack_seq(),
                            sm_so_service_exec_task.getCategory_price_code(),
                            sm_so_service_exec_task.getService_code(),
                            sm_so_service_exec_task.getService_seq(),
                            sm_so_service_exec_task.getExec_tmp()
                    ).toSqlQuery()
            );

            if (delegate != null) {
                delegate.exec_list_opc_update();
            }
        }

        //
        callSoSave(sm_so_service_exec_task.getSo_prefix(), sm_so_service_exec_task.getSo_code());
    }

    private void callSoSave(int prefix, int code) {

        if (ToolBox_Con.isOnline(context)) {

            baInfra.enableProgressDialog(
                    hmAux_Trans.get("alert_task_title"),
                    hmAux_Trans.get("alert_task_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Serial_Save.class);
            Bundle bundle = new Bundle();
            bundle.putLong(Constant.WS_SO_SERIAL_SAVE_PRODUCT_CODE, -1L);
            bundle.putString(Constant.WS_SO_SERIAL_SAVE_SERIAL_ID, "");
            bundle.putInt(Constant.WS_SO_SERIAL_SAVE_SO_PREFIX, prefix);
            bundle.putInt(Constant.WS_SO_SERIAL_SAVE_SO_CODE, code);

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
            activateUpload(context);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void informTaskError(int i, String s) {

//        ToolBox.alertMSG(
//                context,
//                hmAux_Trans.get("task_title_error") != null ? hmAux_Trans.get("task_title_error") : s,
//                s,
//                null,
//                -1
//        );

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("task_title_error"),
                s,
                null,
                -1
        );
    }

    private ArrayList<SM_SO_Service_Exec_Task_File> recoverTaskFiles(ArrayList<SM_SO_Service_Exec_Task_File> task_files, String sFiles) {
        ArrayList<SM_SO_Service_Exec_Task_File> data = new ArrayList<>();
        //
        if (!sFiles.isEmpty()) {
            String[] sTask_files = sFiles.split("#");

            for (int i = 0; i < task_files.size(); i++) {
                if (ToolBox_Inf.verifyFileExists(task_files.get(i).getFile_name())) {
                    data.add(task_files.get(i));
                } else {
                    sm_so_service_exec_task_fileDao.remove(
                            new SM_SO_Service_Exec_Task_File_Sql_008(
                                    task_files.get(i).getCustomer_code(),
                                    task_files.get(i).getSo_prefix(),
                                    task_files.get(i).getSo_code(),
                                    task_files.get(i).getPrice_list_code(),
                                    task_files.get(i).getPack_code(),
                                    task_files.get(i).getPack_seq(),
                                    task_files.get(i).getCategory_price_code(),
                                    task_files.get(i).getService_code(),
                                    task_files.get(i).getService_seq(),
                                    task_files.get(i).getExec_tmp(),
                                    task_files.get(i).getTask_tmp(),
                                    task_files.get(i).getFile_tmp()
                            ).toSqlQuery()
                    );
                }
            }

            for (int i = 0; i < sTask_files.length; i++) {

                boolean bExists = false;

                for (int j = 0; j < task_files.size(); j++) {
                    if (sTask_files[i].equalsIgnoreCase(task_files.get(j).getFile_name())) {
                        bExists = true;

                        break;
                    }

                }

                if (!bExists) {
                    data.add(createFileTask(sm_so_service_exec_task, sTask_files[i]));
                }
            }

        }
        //
        return data;
    }

    private SM_SO_Service_Exec_Task_File createFileTask(SM_SO_Service_Exec_Task sm_so_service_exec_task, String sTask_file) {
        SM_SO_Service_Exec_Task_File task_file = new SM_SO_Service_Exec_Task_File();
        //
        task_file.setFile_code(0);

        long nTask_fileTemp = Long.parseLong(sm_so_service_exec_task_fileDao.getByStringHM(
                new SM_SO_Service_Exec_Task_File_Sql_005(
                        sm_so_service_exec_task.getCustomer_code(),
                        sm_so_service_exec_task.getSo_prefix(),
                        sm_so_service_exec_task.getSo_code(),
                        sm_so_service_exec_task.getPrice_list_code(),
                        sm_so_service_exec_task.getPack_code(),
                        sm_so_service_exec_task.getPack_seq(),
                        sm_so_service_exec_task.getCategory_price_code(),
                        sm_so_service_exec_task.getService_code(),
                        sm_so_service_exec_task.getService_seq(),
                        sm_so_service_exec_task.getExec_tmp(),
                        sm_so_service_exec_task.getTask_tmp()
                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP));

        task_file.setFile_tmp(nTask_fileTemp);

        task_file.setPK(sm_so_service_exec_task);
        task_file.setFile_name(sTask_file);
        task_file.setFile_url("");
        task_file.setFile_url_local(sTask_file);
        //
        sm_so_service_exec_task_fileDao.addUpdateTmp(task_file);
        //
        return task_file;
    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }


}
