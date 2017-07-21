package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.TaskControl;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task_File;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.HashMap;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Task extends BaseFragment {

    private Context context;

    private TextView tv_service_id_label;
    private TextView tv_service_id_value;

    private TextView tv_service_desc_label;
    private TextView tv_service_desc_value;

    private TextView tv_task_tmp_label;
    private TextView tv_task_tmp_value;

    private TaskControl taskControl;

    private Button btn_cancel_task;

    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;
    private SM_SO_Service_Exec_Task sm_so_service_exec_task;

    private HashMap<String, String> data;

    public void setData(HashMap<String, String> data) {
        this.data = data;
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
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    private void iniVar(View view) {
        context = getActivity();

        sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
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

        tv_service_id_label = (TextView) view.findViewById(R.id.act028_task_content_tv_service_id_label);
        tv_service_id_value = (TextView) view.findViewById(R.id.act028_task_content_tv_service_id_value);

        tv_service_desc_label = (TextView) view.findViewById(R.id.act028_task_content_tv_desc_label);
        tv_service_desc_value = (TextView) view.findViewById(R.id.act028_task_content_tv_desc_value);

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

                tv_service_id_label.setText("Service ID");
                tv_service_id_value.setText(data.get("service_id"));

                tv_service_desc_label.setText("Service Description");
                tv_service_desc_value.setText(data.get("service_desc"));

                tv_task_tmp_label.setText("Task TMP");
                tv_task_tmp_value.setText(data.get("task_tmp"));


                taskControl.setmLabel("Task Lavel");
                taskControl.setmValue(data.get("task_perc"), false);
                taskControl.setmPerc(data.get("task_perc"));
                taskControl.setmQty_People_label("Qty People");
                taskControl.setmQty_People(data.get("qty_people"), false);
                taskControl.setmDtStart(data.get("start_date"));
                taskControl.setmDtEnd(data.get("end_date"));
                taskControl.setmComments(data.get("comments"));
                taskControl.setmMaxImages(5);
                taskControl.setmType(data.get("exec_type"));

                StringBuilder sFiles = new StringBuilder();

                boolean bFirst = true;

                for (SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file : sm_so_service_exec_task.getTask_file()) {

                    if (!sm_so_service_exec_task_file.getFile_url_local().isEmpty()) {

                        if (bFirst) {
                            sFiles.append(sm_so_service_exec_task_file.getFile_url_local());
                            bFirst = false;
                        } else {
                            sFiles.append("#");
                            sFiles.append(sm_so_service_exec_task_file.getFile_url_local());
                        }

                    }

                }

                taskControl.setmImgPath(sFiles.toString());

                switch (data.get("exec_status").toUpperCase()) {
                    case "PENDING":
                        processTaskStatus();
                        break;
                    case "PROCESS":
                        processTaskStatus();
                        break;
                    default:
                        taskControl.setmEnabled(false);
                        break;
                }

            }
        } catch (Exception e) {
        }

    }

    private void processTaskStatus() {

        if (data.get("task_user").toUpperCase().equalsIgnoreCase(ToolBox_Con.getPreference_User_Code(getActivity()))) {

            switch (data.get("task_status").toUpperCase()) {
                case "PROCESS":

                    btn_cancel_task.setVisibility(View.VISIBLE);

                    taskControl.setmStatus("EXEC");
                    taskControl.setmEnabled(true);
                    break;
                default:
                    taskControl.setmStatus(data.get("task_status").toUpperCase());
                    taskControl.setmEnabled(false);
                    break;
            }

        } else {

            btn_cancel_task.setVisibility(View.GONE);

            taskControl.setmStatus(data.get("task_status").toUpperCase());
            taskControl.setmEnabled(false);
        }
    }

}
