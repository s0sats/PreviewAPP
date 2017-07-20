package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Task_Adapter;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.HashMap;

/**
 * Created by neomatrix on 18/07/17.
 */

public class Act028_Task_List extends BaseFragment {

    private Context context;

    private transient ListView lv_tasks;
    private transient Button btn_new_task;

    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;
    private SM_SO_Service_Exec sm_so_service_exec;

    public interface IAct028_Task_List {
        void menuTaksSelected(HashMap<String, String> data);
    }

    private IAct028_Task_List delegate;

    public void setOnTaskSelected(IAct028_Task_List delegate) {
        this.delegate = delegate;
    }

    public void setSm_so_service_exec(SM_SO_Service_Exec sm_so_service_exec) {
        this.sm_so_service_exec = sm_so_service_exec;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act028_task_list_content, container, false);
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

        lv_tasks = (ListView) view.findViewById(R.id.act028_task_list_content_lv_tasks);
        btn_new_task = (Button) view.findViewById(R.id.act028_task_list_content_btn_new_task);

        setHMAuxScreen();
    }

    public void setHMAuxScreen() {

        try {

            if (sm_so_service_exec != null) {
                lv_tasks.setAdapter(

                        new Act028_Task_Adapter(
                                context,
                                R.layout.act028_task_content_cell_01,
                                sm_so_service_exec_taskDao.query_HM(
                                        new SM_SO_Service_Exec_Task_Sql_003(
                                                sm_so_service_exec.getCustomer_code(),
                                                sm_so_service_exec.getSo_prefix(),
                                                sm_so_service_exec.getSo_code(),
                                                sm_so_service_exec.getPrice_list_code(),
                                                sm_so_service_exec.getPack_code(),
                                                sm_so_service_exec.getPack_seq(),
                                                sm_so_service_exec.getCategory_price_code(),
                                                sm_so_service_exec.getService_code(),
                                                sm_so_service_exec.getService_seq(),
                                                sm_so_service_exec.getExec_tmp()
                                        ).toSqlQuery()
                                )
                        )
                );

            }

            switch (sm_so_service_exec.getStatus().toUpperCase()) {
                case "PENDING":
                    btn_new_task.setVisibility(View.VISIBLE);
                    break;
                case "PROCESS":
                    btn_new_task.setVisibility(View.VISIBLE);
                    break;
                default:
                    btn_new_task.setVisibility(View.GONE);
                    break;
            }


        } catch (Exception e) {
            String error_s = e.toString();
        }
    }

    private void iniAction() {

        lv_tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hmAux = (HashMap<String, String>) parent.getItemAtPosition(position);
                hmAux.put("exec_status", sm_so_service_exec.getStatus());

                if (delegate != null) {
                    delegate.menuTaksSelected(hmAux);
                }

            }
        });


    }
}
