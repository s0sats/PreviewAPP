package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.TaskControl;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
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

    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;

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

        tv_service_id_label = (TextView) view.findViewById(R.id.act028_task_content_tv_service_id_label);
        tv_service_id_value = (TextView) view.findViewById(R.id.act028_task_content_tv_service_id_value);

        tv_service_desc_label = (TextView) view.findViewById(R.id.act028_task_content_tv_desc_label);
        tv_service_desc_value = (TextView) view.findViewById(R.id.act028_task_content_tv_desc_value);

        tv_task_tmp_label = (TextView) view.findViewById(R.id.act028_task_content_tv_task_tmp_label);
        tv_task_tmp_value = (TextView) view.findViewById(R.id.act028_task_content_tv_task_tmp_value);

        taskControl = (TaskControl) view.findViewById(R.id.act028_task_content_tc_task_value);

        setHMAuxScreen();
    }

    private void iniAction() {

    }

    public void setHMAuxScreen() {

        try {
            if (data != null) {
                tv_service_id_label.setText("Service ID");
                tv_service_id_value.setText(data.get("service_id"));

                tv_service_desc_label.setText("Service Description");
                tv_service_desc_value.setText(data.get("service_desc"));

                tv_task_tmp_label.setText("Task TMP");
                tv_task_tmp_value.setText(data.get("task_tmp"));

                taskControl.setmLabel("Task Lavel");
                //taskControl.setmStatus(data.get("task_status"));
                taskControl.setmStatus("EXEC");
                taskControl.setmValue("30");
                //taskControl.setmQty_People_label("Qty People");
                //taskControl.setmQty_People(data.get("qty_people"));
                //taskControl.setmQty_People("20");
                //taskControl.setmEnabled(true);

            }
        } catch (Exception e) {
        }

    }

}
