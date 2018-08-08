package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Gallery_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task_File;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_008;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_009;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_006;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.sql.SM_SO_Sql_020;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.namoa_digital.namoa_library.util.ConstantBase.HMAUX_TRANS_LIB;
import static com.namoa_digital.namoa_library.util.ToolBox.reverseB;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Task extends BaseFragment {

    private boolean bStatus = false;

    private String user_code = "";

    private Context context;

    private Act028_Main mMain_new;

    private TextView tv_exec_task_tmp_lbl;
    private TextView tv_exec_task_tmp_value;
    //private TextView tv_status;

    //private TextView tv_task_code_lbl;
    //private TextView tv_task_code_value;

    //private TextView tv_service_lbl;
    private TextView tv_service_value;

    private TextView tv_additional_info_lbl;

    private ImageView iv_cancel_task;

    private ImageView iv_play_stop;
    private ImageView iv_save;

    private MKEditTextNM mk_comments;
    private ImageView iv_gallery;

    //private TextView tv_start_date_lbl;
    private MKEditTextNM mk_start_date;
    private MKEditTextNM mk_start_hour;

    //private TextView tv_end_date_lbl;
    private MKEditTextNM mk_end_date;
    private MKEditTextNM mk_end_hour;

    private TextView tv_stepped_txt_min_lbl;
    private TextView tv_stepped_txt_max_lbl;
    private TextView tv_stepped_txt_lbl;
    private SeekBar rb_stepped_perc;

    private float min = 0.0f;
    private float max = 100.0f;
    private float div = 10.0f;
    private float interval = 10.0f;

    //private ImageView iv_worker;
    //private TextView tv_qty_people_lbl;
    private MKEditTextNM mk_qty_people;

    private TextView tv_nick;
    private TextView tv_date;

    private String mErrorMSG;

    // Data
    private SM_SODao soDao;
    private SM_SO_Service_Exec_TaskDao sm_so_service_execDao;
    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;
    private SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao;

    private SM_SO_Service mService;
    private SM_SO_Service_Exec_Task mTask;

    private HashMap<String, String> data;

    private String full_status;

    private boolean sdAvoid = false;

    private boolean autoconf = false;

    public interface IAct028_Task {
        void exec_list_opc_update(String sFlag);
    }

    public void setData(HashMap<String, String> data) {
//        this.data = data;
//        //
//        tempValues.put("qty", data.get("qty_people"));
//        tempValues.put("perc", data.get("task_perc"));
//        tempValues.put("dts", data.get("start_date_local"));
//        tempValues.put("dte", data.get("end_date_local"));
//        tempValues.put("comments", data.get("comments"));
//        tempValues.put("img", "");
//        //
//        sdAvoid = true;
    }

    public void setmService(SM_SO_Service mService) {
        this.mService = mService;
    }

    public void setmTask(SM_SO_Service_Exec_Task mTask) {
        this.mTask = mTask;
        //
        this.mTask.setStart_date(ToolBox_Inf.convertDBToDeviceTMZ(mTask.getStart_date()));
        this.mTask.setEnd_date(ToolBox_Inf.convertDBToDeviceTMZ(mTask.getEnd_date()));
        //
        StringBuilder sFiles = new StringBuilder();

        boolean bFirst = true;

        for (SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file : mTask.getTask_file()) {

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
        //
        tempValues.put("comments", mTask.getComments());
        tempValues.put("img", sFiles.toString());
        tempValues.put("dts", mTask.getStart_date());
        tempValues.put("dte", mTask.getEnd_date());
        tempValues.put("perc", String.valueOf(mTask.getTask_perc()));
        tempValues.put("qty", String.valueOf(mTask.getQty_people()));
        //
        sdAvoid = true;
    }

    public void setFull_status(String full_status) {
        this.full_status = full_status;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act028_task_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

    public void updateTaskOnLeave() {
        loadScreenToData();

        try {

            if (mTask != null && mTask.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)
                    &&
                    String.valueOf(mTask.getTask_user()).equalsIgnoreCase(user_code)) {

                mTask.setComments(tempValues.get("comments"));
                mTask.setTask_file(recoverTaskFiles(mTask.getTask_file(), tempValues.get("img")));

                mTask.setStart_date(ToolBox.convertToDeviceTMZ2(tempValues.get("dts")));
                mTask.setEnd_date(ToolBox.convertToDeviceTMZ2(tempValues.get("dte")));

                mTask.setTask_perc(Integer.parseInt(tempValues.get("perc")));

                mTask.setQty_people(Integer.parseInt(tempValues.get("qty")));
                //

                //mTask.setPK(sm_so_service_exec);
                //
                sm_so_service_exec_task_fileDao.remove(
                        new SM_SO_Service_Exec_Task_File_Sql_009(
                                mTask.getCustomer_code(),
                                mTask.getSo_prefix(),
                                mTask.getSo_code(),
                                mTask.getPrice_list_code(),
                                mTask.getPack_code(),
                                mTask.getPack_seq(),
                                mTask.getCategory_price_code(),
                                mTask.getService_code(),
                                mTask.getService_seq(),
                                mTask.getExec_tmp(),
                                mTask.getTask_tmp()
                        ).toSqlQuery()
                );
                //
                sm_so_service_exec_taskDao.addUpdateTmp(mTask);
                //

                // Include Files to Upload
                uploadFiles(mTask);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTaskOnLeave() {
        if (mTask != null && String.valueOf(mTask.getTask_user()).equalsIgnoreCase(user_code)) {

            if (mService.getExec_type().equalsIgnoreCase(Constant.SO_SERVICE_TYPE_YES_NO)
                    && mTask.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)) {
                sm_so_service_execDao.remove(
                        new SM_SO_Service_Exec_Sql_005(
                                mTask.getCustomer_code(),
                                mTask.getSo_prefix(),
                                mTask.getSo_code(),
                                mTask.getPrice_list_code(),
                                mTask.getPack_code(),
                                mTask.getPack_seq(),
                                mTask.getCategory_price_code(),
                                mTask.getService_code(),
                                mTask.getService_seq(),
                                mTask.getExec_tmp()
                        ).toSqlQuery()
                );

                sm_so_service_exec_task_fileDao.remove(
                        new SM_SO_Service_Exec_Task_File_Sql_009(
                                mTask.getCustomer_code(),
                                mTask.getSo_prefix(),
                                mTask.getSo_code(),
                                mTask.getPrice_list_code(),
                                mTask.getPack_code(),
                                mTask.getPack_seq(),
                                mTask.getCategory_price_code(),
                                mTask.getService_code(),
                                mTask.getService_seq(),
                                mTask.getExec_tmp(),
                                mTask.getTask_tmp()
                        ).toSqlQuery()
                );

                sm_so_service_exec_taskDao.remove(
                        new SM_SO_Service_Exec_Task_Sql_006(
                                mTask.getCustomer_code(),
                                mTask.getSo_prefix(),
                                mTask.getSo_code(),
                                mTask.getPrice_list_code(),
                                mTask.getPack_code(),
                                mTask.getPack_seq(),
                                mTask.getCategory_price_code(),
                                mTask.getService_code(),
                                mTask.getService_seq(),
                                mTask.getExec_tmp(),
                                mTask.getTask_tmp()
                        ).toSqlQuery()
                );
                //Após apagar dados, volta update_required do cabeçalho
                //para o original(recebido via bundle)
                soDao.addUpdate(
                        new SM_SO_Sql_020(
                                mService.getCustomer_code(),
                                mService.getSo_prefix(),
                                mService.getSo_code(),
                                mMain_new.getOriginal_update_required()
                        ).toSqlQuery()
                );
            }
        }
    }

    private void iniVar(View view) {
        context = getActivity();

        mMain_new = (Act028_Main) getActivity();

        user_code = ToolBox_Con.getPreference_User_Code(getActivity());

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

        tv_exec_task_tmp_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_exec_task_tmp_lbl);
        tv_exec_task_tmp_value = (TextView) view.findViewById(R.id.act028_task_content_tv_exec_task_tmp_value);
        //tv_status = (TextView) view.findViewById(R.id.act028_task_content_tv_status);

        //tv_task_code_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_task_code_lbl);
        //tv_task_code_value = (TextView) view.findViewById(R.id.act028_task_content_tv_task_code_value);

        //tv_service_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_service_lbl);
        tv_service_value = (TextView) view.findViewById(R.id.act028_task_content_tv_service_value);

        tv_additional_info_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_addicional_info_lbl);

        iv_play_stop = (ImageView) view.findViewById(R.id.act028_task_content_iv_play_stop);
        iv_save = (ImageView) view.findViewById(R.id.act028_task_content_iv_save);

        iv_cancel_task = (ImageView) view.findViewById(R.id.act028_task_content_iv_cancel_task);

        mk_comments = (MKEditTextNM) view.findViewById(R.id.act028_task_content_mk_comments);
        iv_gallery = (ImageView) view.findViewById(R.id.act028_task_content_iv_gallery);

        //tv_start_date_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_start_date_lbl);
        mk_start_date = (MKEditTextNM) view.findViewById(R.id.act028_task_content_mk_start_date);
        mk_start_hour = (MKEditTextNM) view.findViewById(R.id.act028_task_content_mk_start_hour);

        //tv_end_date_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_end_date_lbl);
        mk_end_date = (MKEditTextNM) view.findViewById(R.id.act028_task_content_mk_end_date);
        mk_end_hour = (MKEditTextNM) view.findViewById(R.id.act028_task_content_mk_end_hour);

        tv_stepped_txt_min_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_stepped_txt_min_lbl);
        tv_stepped_txt_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_stepped_txt_lbl);
        tv_stepped_txt_max_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_stepped_txt_max_lbl);
        rb_stepped_perc = (SeekBar) view.findViewById(R.id.act028_task_content_rb_stepped_perc);

        tv_stepped_txt_min_lbl.setText(String.valueOf((int) min));
        tv_stepped_txt_max_lbl.setText(String.valueOf((int) max));
        rb_stepped_perc.setMax((int) div);
        rb_stepped_perc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_stepped_txt_lbl.setText(String.valueOf(progress * (int) interval + (int) min));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //tv_qty_people_lbl = (TextView) view.findViewById(R.id.act028_task_content_tv_qty_people_lbl);
        mk_qty_people = (MKEditTextNM) view.findViewById(R.id.act028_task_content_mk_qty_people);

        tv_nick = (TextView) view.findViewById(R.id.act028_task_content_tv_nick);
        tv_date = (TextView) view.findViewById(R.id.act028_task_content_tv_date);

        controls_iv.add(iv_gallery);
        controls_sta.add(mk_comments);
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (mTask != null) {

                if (mMain_new.hasExecutionProfile()) {

                    String task_code = String.valueOf(mTask.getTask_code());

                    tv_exec_task_tmp_lbl.setText(hmAux_Trans.get("exec_task_lbl"));
                    tv_exec_task_tmp_value.setText(
                            String.valueOf(mTask.getExec_tmp()) + " / " + String.valueOf(mTask.getTask_tmp()) +
                                    (task_code.equalsIgnoreCase("0") ? "" : " / " + String.valueOf(mTask.getTask_code()))
                    );
                    //tv_status.setText(hmAux_Trans.get(mTask.getStatus()));
                    //setExecStatusColor(tv_status, mTask.getStatus());
                    //ToolBox_Inf.setTaskStatusColor(context,tv_status, mTask.getStatus());

                    //tv_task_code_lbl.setText(hmAux_Trans.get("task_code_lbl"));
                    //tv_task_code_lbl.setText("Task Code");
                    //String task_code = String.valueOf(mTask.getTask_code());
                    //tv_task_code_value.setText(task_code.equalsIgnoreCase("0") ? "" : String.valueOf(mTask.getTask_code()));

                    //tv_service_lbl.setText(hmAux_Trans.get("service_lbl"));
                    //tv_service_lbl.setText("Servide");
                    tv_service_value.setText(mService.getService_id() + " - " + mService.getService_desc());

                    //mk_comments.setText(mTask.getComments());

//                StringBuilder sFiles = new StringBuilder();
//
//                boolean bFirst = true;
//
//                for (SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file : mTask.getTask_file()) {
//
//                    if (!sm_so_service_exec_task_file.getFile_name().isEmpty()) {
//
//                        if (bFirst) {
//                            sFiles.append(sm_so_service_exec_task_file.getFile_name());
//                            bFirst = false;
//                        } else {
//                            sFiles.append("#");
//                            sFiles.append(sm_so_service_exec_task_file.getFile_name());
//                        }
//
//                    }
//                }
//
                    //iv_gallery.setTag(sFiles.toString());

                    //iv_gallery.setTag(tempValues.get("img"));


                    tv_additional_info_lbl.setText(hmAux_Trans.get("additional_info_lbl"));

                    //tv_start_date_lbl.setText(hmAux_Trans.get("start_date_lbl"));
//                    mk_start_date.setMaskedText(ToolBox.reverseS(mTask.getStart_date()));
//                    mk_start_hour.setMaskedText(ToolBox.reverseSH(mTask.getStart_date()));
                    //
                    mk_start_date.setText(ToolBox.reverseS(mTask.getStart_date()));
                    mk_start_hour.setText(ToolBox.reverseSH(mTask.getStart_date()));


                    //tv_end_date_lbl.setText(hmAux_Trans.get("end_date_lbl"));
//                    mk_end_date.setMaskedText(ToolBox.reverseS(mTask.getEnd_date()));
//                    mk_end_hour.setMaskedText(ToolBox.reverseSH(mTask.getEnd_date()));

                    mk_end_date.setText(ToolBox.reverseS(mTask.getEnd_date()));
                    mk_end_hour.setText(ToolBox.reverseSH(mTask.getEnd_date()));

                    rb_stepped_perc.setProgress((int) ((ToolBox.convertSelector(String.valueOf(mTask.getTask_perc())) - min) / interval));

                    if (mTask.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED) ||
                            mService.getExec_type().equalsIgnoreCase(Constant.SO_SERVICE_TYPE_YES_NO)) {

                        tv_stepped_txt_lbl.setVisibility(View.GONE);
                        rb_stepped_perc.setVisibility(View.GONE);
                        tv_stepped_txt_min_lbl.setVisibility(View.GONE);
                        tv_stepped_txt_max_lbl.setVisibility(View.GONE);
                    }

                    //tv_qty_people_lbl.setText(hmAux_Trans.get("qty_people_lbl"));
                    mk_qty_people.setText(String.valueOf(mTask.getQty_people()));
                    mk_qty_people.setmMaxSize(5);

                    try {

                        if (widgetset) {
                            widgetset = false;
                        } else {
                            mk_comments.setText(tempValues.get("comments"));
                        }

                        //mk_comments.setText(tempValues.get("comments"));

//                        mk_start_date.setMaskedText(ToolBox.reverseS(tempValues.get("dts")));
//                        mk_start_hour.setMaskedText(ToolBox.reverseSH(tempValues.get("dts")));
//                        mk_end_date.setMaskedText(ToolBox.reverseS(tempValues.get("dte")));
//                        mk_end_hour.setMaskedText(ToolBox.reverseSH(tempValues.get("dte")));

                        mk_start_date.setText(ToolBox.reverseS(tempValues.get("dts")));
                        mk_start_hour.setText(ToolBox.reverseSH(tempValues.get("dts")));
                        mk_end_date.setText(ToolBox.reverseS(tempValues.get("dte")));
                        mk_end_hour.setText(ToolBox.reverseSH(tempValues.get("dte")));

                        rb_stepped_perc.setProgress((int) ((ToolBox.convertSelector(tempValues.get("perc")) - min) / interval));
                        mk_qty_people.setText(tempValues.get("qty"));

                        if (iv_gallery.getTag() == null) {
                            iv_gallery.setTag(tempValues.get("img"));
                        }

                        if (sdAvoid) {
                            sdAvoid = false;
                        } else {
                            iv_gallery.setTag(tempValues.get("img"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tv_nick.setText(mTask.getTask_user_nick());
                    tv_date.setText(mk_end_date.getText().toString().trim().length() != 0 ? mk_end_date.getText().toString().trim() : mk_start_date.getText().toString().trim());

                    upImgGallery();

                    processTaskStatus();

                } else {
                    /*
                     * Esse else aplica a inibição dos campos
                     * caso o usuario não possua profile de execução.
                     */

                    //
                    //Coloca dado na tela
                    //
                    String task_code = String.valueOf(mTask.getTask_code());

                    tv_exec_task_tmp_lbl.setText(hmAux_Trans.get("exec_task_lbl"));
                    tv_exec_task_tmp_value.setText(
                            String.valueOf(mTask.getExec_tmp()) + " / " + String.valueOf(mTask.getTask_tmp()) +
                                    (task_code.equalsIgnoreCase("0") ? "" : " / " + String.valueOf(mTask.getTask_code()))
                    );

                    tv_service_value.setText(mService.getService_id() + " - " + mService.getService_desc());
                    //
                    tv_additional_info_lbl.setText(hmAux_Trans.get("additional_info_lbl"));

                    //tv_start_date_lbl.setText(hmAux_Trans.get("start_date_lbl"));
//                    mk_start_date.setMaskedText(ToolBox.reverseS(mTask.getStart_date()));
//                    mk_start_hour.setMaskedText(ToolBox.reverseSH(mTask.getStart_date()));

                    mk_start_date.setText(ToolBox.reverseS(mTask.getStart_date()));
                    mk_start_hour.setText(ToolBox.reverseSH(mTask.getStart_date()));


                    //tv_end_date_lbl.setText(hmAux_Trans.get("end_date_lbl"));
//                    mk_end_date.setMaskedText(ToolBox.reverseS(mTask.getEnd_date()));
//                    mk_end_hour.setMaskedText(ToolBox.reverseSH(mTask.getEnd_date()));

                    mk_end_date.setText(ToolBox.reverseS(mTask.getEnd_date()));
                    mk_end_hour.setText(ToolBox.reverseSH(mTask.getEnd_date()));


                    mk_qty_people.setText(String.valueOf(mTask.getQty_people()));
                    mk_qty_people.setmMaxSize(5);

                    try {

                        if (widgetset) {
                            widgetset = false;
                        } else {
                            mk_comments.setText(tempValues.get("comments"));
                        }

//                        mk_start_date.setMaskedText(ToolBox.reverseS(tempValues.get("dts")));
//                        mk_start_hour.setMaskedText(ToolBox.reverseSH(tempValues.get("dts")));
//                        mk_end_date.setMaskedText(ToolBox.reverseS(tempValues.get("dte")));
//                        mk_end_hour.setMaskedText(ToolBox.reverseSH(tempValues.get("dte")));

                        mk_start_date.setText(ToolBox.reverseS(tempValues.get("dts")));
                        mk_start_hour.setText(ToolBox.reverseSH(tempValues.get("dts")));
                        mk_end_date.setText(ToolBox.reverseS(tempValues.get("dte")));
                        mk_end_hour.setText(ToolBox.reverseSH(tempValues.get("dte")));


                        rb_stepped_perc.setProgress((int) ((ToolBox.convertSelector(tempValues.get("perc")) - min) / interval));
                        mk_qty_people.setText(tempValues.get("qty"));

                        if (iv_gallery.getTag() == null) {
                            iv_gallery.setTag(tempValues.get("img"));
                        }
                        //
                        if (sdAvoid) {
                            sdAvoid = false;
                        } else {
                            iv_gallery.setTag(tempValues.get("img"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tv_nick.setText(mTask.getTask_user_nick());
                    tv_date.setText(mk_end_date.getText().toString().trim().length() != 0 ? mk_end_date.getText().toString().trim() : mk_start_date.getText().toString().trim());
                    //
                    //
                    // Desabilita tudo
                    //

                    //Btões de ação
                    iv_cancel_task.setVisibility(View.GONE);
                    iv_play_stop.setVisibility(View.GONE);
                    iv_save.setVisibility(View.GONE);

                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);
                    iv_gallery.setEnabled(true);
                    mk_comments.setEnabled(false);
                    //
                    rb_stepped_perc.setProgress((int) ((ToolBox.convertSelector(String.valueOf(mTask.getTask_perc())) - min) / interval));
                    tv_stepped_txt_lbl.setVisibility(View.GONE);
                    rb_stepped_perc.setVisibility(View.GONE);
                    tv_stepped_txt_min_lbl.setVisibility(View.GONE);
                    tv_stepped_txt_max_lbl.setVisibility(View.GONE);
                    //
                    if (mService.getExec_type().equalsIgnoreCase(Constant.SO_SERVICE_TYPE_START_STOP)) {
                        tv_stepped_txt_lbl.setVisibility(View.VISIBLE);
                        rb_stepped_perc.setVisibility(View.VISIBLE);
                        tv_stepped_txt_min_lbl.setVisibility(View.VISIBLE);
                        tv_stepped_txt_max_lbl.setVisibility(View.VISIBLE);
                    }
                    //
                    //Galeria
                    //
                    String files = null;
                    try {
                        files = (String) iv_gallery.getTag();
                    } catch (Exception e) {
                        ToolBox_Inf.registerException(getClass().getName(), e);
                    }
                    boolean turnOnGallery = files != null && files.length() > 0;

                    iv_gallery.setClickable(turnOnGallery);
                    iv_gallery.setEnabled(turnOnGallery);
                    //
                    if (turnOnGallery) {
                        //configura icone
                        iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_foto_marcada_ns));
                        //Redefine Listner para não deixar tirar nem editar foto
                        iv_gallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callCamera(false);
                            }
                        });
                    } else {
                        //configura icone
                        iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_foto_ns));
                        //Remove listner
                        iv_gallery.setOnClickListener(null);
                    }

                }
            }
        }
    }

    private void upImgGallery() {
        if (((String) iv_gallery.getTag()).equalsIgnoreCase("")) {
            iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_foto_ns));
        } else {
            iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_foto_marcada_ns));
        }

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCamera();
            }
        });
    }

    @Override
    public void loadScreenToData() {
        if (bStatus) {
            if (mMain_new.hasExecutionProfile()) {
                tempValues.put("comments", mk_comments.getText().toString());
                tempValues.put("img", (String) iv_gallery.getTag());
                String sDTS = reverseB(mk_start_date.getText().toString());

                tempValues.put("dts", sDTS + " " + mk_start_hour.getText().toString());

                String sDTE = reverseB(mk_end_date.getText().toString());

                tempValues.put("dte", sDTE + " " + mk_end_hour.getText().toString());

                tempValues.put("perc", String.valueOf(rb_stepped_perc.getProgress() * (int) interval + (int) min));

                tempValues.put("qty", mk_qty_people.getText().toString());
            }
        }
    }

    private void iniAction() {

        iv_cancel_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  06-08-2018 Verifica se site do servico é diferente do site logado. Não leva em consideracao
                 *  o site do cabecalho da S.O.
                 */
                if (ToolBox_Inf.hasServiceSiteRestriction(context, String.valueOf(mService.getSite_code()), hmAux_Trans)) {
                    return;
                }

                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_cancel_task_ttl"),
                        hmAux_Trans.get("alert_cancel_task_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTask.setStatus(Constant.SYS_STATUS_CANCELLED);
                                mTask.setComments(mk_comments.getText().toString().trim().length() > 0 ? mk_comments.getText().toString() : null);
                                mTask.setTask_file(recoverTaskFiles(mTask.getTask_file(), (String) iv_gallery.getTag()));

                                mTask.setStart_date(ToolBox.convertToDeviceTMZ2(reverseB(mk_start_date.getText().toString()) + " " + mk_start_hour.getText().toString()));
                                mTask.setEnd_date(reverseB(ToolBox.convertToDeviceTMZ2(mk_end_date.getText().toString()) + " " + mk_end_hour.getText().toString()));

                                mTask.setExec_time(Integer.parseInt(ToolBox.durationTimeValuesMinutes(mTask.getStart_date(), mTask.getEnd_date())));

                                mTask.setTask_perc(rb_stepped_perc.getProgress() * (int) interval + (int) min);

                                mTask.setQty_people(Integer.parseInt(mk_qty_people.getText().toString()));
                                //
                                sm_so_service_exec_task_fileDao.remove(
                                        new SM_SO_Service_Exec_Task_File_Sql_009(
                                                mTask.getCustomer_code(),
                                                mTask.getSo_prefix(),
                                                mTask.getSo_code(),
                                                mTask.getPrice_list_code(),
                                                mTask.getPack_code(),
                                                mTask.getPack_seq(),
                                                mTask.getCategory_price_code(),
                                                mTask.getService_code(),
                                                mTask.getService_seq(),
                                                mTask.getExec_tmp(),
                                                mTask.getTask_tmp()
                                        ).toSqlQuery()
                                );
                                //
                                sm_so_service_exec_taskDao.addUpdateTmp(mTask);

                                // Include Files to Upload
                                uploadFiles(mTask);

                                /**
                                 * Calling WebService
                                 */
                                SM_SO so = soDao.getByString(
                                        new SM_SO_Sql_001(
                                                ToolBox_Con.getPreference_Customer_Code(context),
                                                mTask.getSo_prefix(),
                                                mTask.getSo_code()
                                        ).toSqlQuery()
                                );

                                so.setUpdate_required(1);
                                soDao.addUpdate(so);
                                //
                                processStatusUpdateOffLine();
                                mMain_new.setmTaskCall(true);
                                mMain_new.executeSerialSave();
                                //callSoSave();
                            }
                        },
                        1,
                        false
                );
            }
        });
    }

    private void showInfo() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.act028_task_dialog_info, null);
//
//        /**
//         * Ini Vars
//         */
//
//        ListView lv_opt = (ListView) view.findViewById(R.id.act028_task_dialog_info_lv_opt);
//
////        String[] from = {NEW_OPT_LABEL};
////        //int[] to = {android.R.id.text1};
////        int[] to = {R.id.namoa_custom_cell_3_tv_item};
////
////
////        lv_opt.setAdapter(
////                new SimpleAdapter(
////                        context,
////                        getNewOpts(),
////                        //android.R.layout.simple_list_item_1,
////                        R.layout.namoa_custom_cell_3,
////                        from,
////                        to
////                )
////        );
//
//        /**
//         * Ini Action
//         */
//
////        lv_opt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                HMAux item = (HMAux) parent.getItemAtPosition(position);
////                mPresenter.defineFlow(item);
////
////            }
////        });
//
//        //builder.setTitle(hmAux_Trans.get("alert_new_opt_ttl"));
//        builder.setTitle("On Construction");
//        builder.setView(view);
//        builder.setCancelable(true);
//
//        builder.show();
    }

    private void processTaskStatus() {

        mMain_new.setMenuExit(
                HMAUX_TRANS_LIB.get("exit_shortcut_ttl"),
                HMAUX_TRANS_LIB.get("exit_shortcut_msg"),
                Constant.HM_ICON_NAMOA_SERVICES_TEXT
        );

        if (String.valueOf(mTask.getTask_user()).toUpperCase().equalsIgnoreCase(ToolBox_Con.getPreference_User_Code(getActivity()))) {

            if (full_status.equalsIgnoreCase("1")) {
                switch (mTask.getStatus().toUpperCase()) {
                    case Constant.SYS_STATUS_PROCESS:

                        if (mService.getExec_type().equalsIgnoreCase(Constant.SO_SERVICE_TYPE_YES_NO)) {
                            iv_cancel_task.setVisibility(View.GONE);
                            //
                            mMain_new.setMenuExit(
                                    HMAUX_TRANS_LIB.get("exit_shortcut_ttl"),
                                    hmAux_Trans.get("alert_task_lost_data_msg"),
                                    Constant.HM_ICON_NAMOA_SERVICES_TEXT
                            );
                        } else {
                            iv_cancel_task.setVisibility(View.VISIBLE);
                        }

                        cfgStatus(true);
                        break;

                    default:
                        iv_cancel_task.setVisibility(View.GONE);

                        cfgStatus(false);
                        break;
                }
            } else {
                iv_cancel_task.setVisibility(View.GONE);

                cfgStatus(false);
            }

            //Se a task for minha, porem há restrição de execução por causa do site,
            //bloqueia campos .
            if (ToolBox_Inf.hasServiceSiteRestriction(context, String.valueOf(mService.getSite_code()), hmAux_Trans)) {
                iv_cancel_task.setEnabled(false);
                //
                cfgStatus(false);
            }

        } else {

            iv_cancel_task.setVisibility(View.GONE);

            cfgStatus(false);
        }
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

    public void informTaskActiveClosed() {
        mTask.setStatus(Constant.SYS_STATUS_DONE);
        mTask.setComments(mk_comments.getText().toString().trim().length() > 0 ? mk_comments.getText().toString() : null);
        mTask.setTask_file(recoverTaskFiles(mTask.getTask_file(), (String) iv_gallery.getTag()));

        mTask.setStart_date(ToolBox.convertToDeviceTMZ2(reverseB(mk_start_date.getText().toString()) + " " + mk_start_hour.getText().toString()));
        mTask.setEnd_date(ToolBox.convertToDeviceTMZ2(reverseB(mk_end_date.getText().toString()) + " " + mk_end_hour.getText().toString()));

        mTask.setExec_time(Integer.parseInt(ToolBox.durationTimeValuesMinutes(mTask.getStart_date(), mTask.getEnd_date())));

        mTask.setTask_perc(rb_stepped_perc.getProgress() * (int) interval + (int) min);

        mTask.setQty_people(Integer.parseInt(mk_qty_people.getText().toString()));
        //
        sm_so_service_exec_task_fileDao.remove(
                new SM_SO_Service_Exec_Task_File_Sql_009(
                        mTask.getCustomer_code(),
                        mTask.getSo_prefix(),
                        mTask.getSo_code(),
                        mTask.getPrice_list_code(),
                        mTask.getPack_code(),
                        mTask.getPack_seq(),
                        mTask.getCategory_price_code(),
                        mTask.getService_code(),
                        mTask.getService_seq(),
                        mTask.getExec_tmp(),
                        mTask.getTask_tmp()
                ).toSqlQuery()
        );
        //
        sm_so_service_exec_taskDao.addUpdateTmp(mTask);
        //
        processTaskStatus();
        //
        // Include Files to Upload
        uploadFiles(mTask);

        /**
         * Calling WebService
         */
        soDao.getByString(
                new SM_SO_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mTask.getSo_prefix(),
                        mTask.getSo_code()
                ).toSqlQuery()
        );

        if (mTask.getTask_perc() == 100) {
            sm_so_service_execDao.addUpdate(
                    new SM_SO_Service_Exec_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            mTask.getSo_prefix(),
                            mTask.getSo_code(),
                            mTask.getPrice_list_code(),
                            mTask.getPack_code(),
                            mTask.getPack_seq(),
                            mTask.getCategory_price_code(),
                            mTask.getService_code(),
                            mTask.getService_seq(),
                            mTask.getExec_tmp()
                    ).toSqlQuery()
            );
        }
        //
        mMain_new.setMTASK_STATUS(Act028_Main.CREATE_SAVE);
        //
        processStatusUpdateOffLine();
        mMain_new.setmTaskCall(true);
        mMain_new.executeSerialSave();
        //callSoSave();
    }

//    private void callSoSave() {
//
//        // processStatusUpdateOffLine();
//
//        if (ToolBox_Con.isOnline(context)) {
//
//            baInfra.enableProgressDialog(
//                    hmAux_Trans.get("alert_task_title"),
//                    hmAux_Trans.get("alert_task_msg"),
//                    hmAux_Trans.get("sys_alert_btn_cancel"),
//                    hmAux_Trans.get("sys_alert_btn_ok")
//            );
//            //
//            Intent mIntent = new Intent(context, WBR_SO_Save.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);
//
//            mIntent.putExtras(bundle);
//            ////
//            //context.sendBroadcast(mIntent);
//        } else {
//            mMain_new.offLineProcess();
//        }
//    }

    private void processStatusUpdateOffLine() {

        sm_so_service_exec_taskDao.updateStatusOffLine(mTask);

    }

    public void informTaskError(String msg) {

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("task_title_error"),
                msg,
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
                    data.add(createFileTask(mTask, sTask_files[i]));
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

        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void setExecStatusColor(TextView tv_status, String status) {
        /*
         * Tratativa de cor por Status
         * */
        switch (status) {
            case Constant.SYS_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SYS_STATUS_PROCESS:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
            case Constant.SYS_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SYS_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            case Constant.SYS_STATUS_NOT_EXECUTED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_purple_3));
                break;
            case Constant.SYS_STATUS_INCONSISTENT:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
                break;
            default:
                break;
        }
    }

    private void callCamera() {
        sdAvoid = true;

        Intent mIntent = new Intent(context, Gallery_Activity.class);
        mIntent.putExtra(ConstantBase.PID, iv_gallery.getId());
        mIntent.putExtra(ConstantBase.PTYPE, 10);
        mIntent.putExtra(ConstantBase.PPATH, (String) iv_gallery.getTag());
        mIntent.putExtra(ConstantBase.MPRE, "p");

        if (mTask.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)) {
            mIntent.putExtra(ConstantBase.PENABLED, true);
        } else {
            mIntent.putExtra(ConstantBase.PENABLED, false);
        }

        mIntent.putExtra(ConstantBase.MAXIMAGES, 4);
        //
        context.startActivity(mIntent);
    }

    private void callCamera(boolean pEnabled) {
        sdAvoid = true;

        Intent mIntent = new Intent(context, Gallery_Activity.class);
        mIntent.putExtra(ConstantBase.PID, iv_gallery.getId());
        mIntent.putExtra(ConstantBase.PTYPE, 10);
        mIntent.putExtra(ConstantBase.PPATH, (String) iv_gallery.getTag());
        mIntent.putExtra(ConstantBase.MPRE, "p");
        //
        mIntent.putExtra(ConstantBase.PENABLED, pEnabled);
        //
        mIntent.putExtra(ConstantBase.MAXIMAGES, 4);
        //
        context.startActivity(mIntent);
    }

    private void cfgStatus(boolean mEnabled) {

        String mImgPath = (String) iv_gallery.getTag();

        if (mEnabled) {
            switch (mTask.getStatus()) {
                case Constant.SYS_STATUS_PENDING:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(true);
                    iv_save.setEnabled(true);
                    rb_stepped_perc.setEnabled(false);
                    iv_gallery.setEnabled(false);
                    mk_comments.setEnabled(false);
                    break;
                case Constant.SYS_STATUS_INCONSISTENT:
                case Constant.SYS_STATUS_CANCELLED:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);

                    if (mImgPath.trim().length() != 0) {
                        iv_gallery.setEnabled(true);
                    } else {
                        iv_gallery.setEnabled(false);
                    }

                    mk_comments.setEnabled(false);
                    break;
                case Constant.SYS_STATUS_DONE:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);

                    if (mImgPath.trim().length() != 0) {
                        iv_gallery.setEnabled(true);
                    } else {
                        iv_gallery.setEnabled(false);
                    }

                    mk_comments.setEnabled(false);
                    break;

                case Constant.SYS_STATUS_NOT_EXECUTED:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);

                    if (mImgPath.trim().length() != 0) {
                        iv_gallery.setEnabled(true);
                    } else {
                        iv_gallery.setEnabled(false);
                    }

                    mk_comments.setEnabled(false);
                    break;
                case Constant.SYS_STATUS_PROCESS:
                    mk_qty_people.setEnabled(true);
                    mk_start_date.setEnabled(true);
                    mk_start_hour.setEnabled(true);
                    mk_end_date.setEnabled(true);
                    mk_end_hour.setEnabled(true);

                    iv_play_stop.setEnabled(true);
                    iv_play_stop.setOnClickListener(play_stop_listener);

                    iv_save.setEnabled(true);
                    iv_save.setOnClickListener(save_listener);

                    rb_stepped_perc.setEnabled(true);
                    iv_gallery.setEnabled(true);
                    mk_comments.setEnabled(true);
                    break;
                default:
                    break;
            }
        } else {
            switch (mTask.getStatus()) {
                case Constant.SYS_STATUS_PENDING:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);
                    iv_gallery.setEnabled(false);
                    mk_comments.setEnabled(false);
                    break;
                case Constant.SYS_STATUS_INCONSISTENT:
                case Constant.SYS_STATUS_CANCELLED:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);

                    if (mImgPath.trim().length() != 0) {
                        iv_gallery.setEnabled(true);
                    } else {
                        iv_gallery.setEnabled(false);
                    }

                    mk_comments.setEnabled(false);
                    break;
                case Constant.SYS_STATUS_DONE:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);

                    if (mImgPath.trim().length() != 0) {
                        iv_gallery.setEnabled(true);
                    } else {
                        iv_gallery.setEnabled(false);
                    }

                    mk_comments.setEnabled(false);
                    break;
                case Constant.SYS_STATUS_NOT_EXECUTED:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);

                    if (mImgPath.trim().length() != 0) {
                        iv_gallery.setEnabled(true);
                    } else {
                        iv_gallery.setEnabled(false);
                    }

                    mk_comments.setEnabled(false);
                    break;
                case Constant.SYS_STATUS_PROCESS:
                    mk_qty_people.setEnabled(false);
                    mk_start_date.setEnabled(false);
                    mk_start_hour.setEnabled(false);
                    mk_end_date.setEnabled(false);
                    mk_end_hour.setEnabled(false);

                    iv_play_stop.setEnabled(false);
                    iv_save.setEnabled(false);
                    rb_stepped_perc.setEnabled(false);
                    iv_gallery.setEnabled(false);
                    mk_comments.setEnabled(false);
                    break;
                default:
                    break;
            }
        }

        if (mService.getExec_type().equalsIgnoreCase(Constant.SO_SERVICE_TYPE_START_STOP)) {
            iv_play_stop.setVisibility(View.VISIBLE);
            iv_save.setVisibility(View.GONE);
        } else {
            iv_play_stop.setVisibility(View.GONE);
            iv_save.setVisibility(View.VISIBLE);
            //
            rb_stepped_perc.setEnabled(false);
            rb_stepped_perc.setProgress(100);
        }

        if (String.valueOf(mTask.getTask_user()).equalsIgnoreCase(user_code)) {
            if (!mTask.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)) {
                iv_play_stop.setVisibility(View.GONE);
                iv_save.setVisibility(View.GONE);
            }
        } else {
            iv_play_stop.setVisibility(View.GONE);
            iv_save.setVisibility(View.GONE);
        }

        mk_comments.setTextColor(0xFF000000);
    }

    private View.OnClickListener play_stop_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /**
             *  06-08-2018 Verifica se site do servico é diferente do site logado. Não leva em consideracao
             *  o site do cabecalho da S.O.
             */
            if (ToolBox_Inf.hasServiceSiteRestriction(context, String.valueOf(mService.getSite_code()), hmAux_Trans)) {
                return;
            }

            showTechDialog();
        }
    };

    private View.OnClickListener save_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /**
             *  06-08-2018 Verifica se site do servico é diferente do site logado. Não leva em consideracao
             *  o site do cabecalho da S.O.
             */
            if (ToolBox_Inf.hasServiceSiteRestriction(context, String.valueOf(mService.getSite_code()), hmAux_Trans)) {
                return;
            }

            if (isValid()) {
                informTaskActiveClosed();
            } else {
                informTaskError(mErrorMSG);
            }
        }
    };

    public boolean isValid() {

        if (mk_end_date.getText().toString().equalsIgnoreCase("") || mk_end_hour.getText().toString().equalsIgnoreCase("")) {
            String sDT = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z");
            //
            autoconf = true;
            //
//            mk_end_date.setMaskedText(ToolBox.reverseS(sDT));
//            mk_end_hour.setMaskedText(ToolBox.reverseSH(sDT));

            mk_end_date.setText(ToolBox.reverseS(sDT));
            mk_end_hour.setText(ToolBox.reverseSH(sDT));
        }

        mErrorMSG = "";

        if (!mk_qty_people.isValid() || mk_qty_people.getText().toString().equalsIgnoreCase("0")) {
            mErrorMSG = HMAUX_TRANS_LIB.get("msg_qty_people_error");

            return false;
        }

        if (!mk_start_date.isValid()) {
            mErrorMSG = HMAUX_TRANS_LIB.get("msg_start_date_error");

            return false;
        }

        if (!mk_end_date.isValid() || mk_end_date.getText().toString().isEmpty()) {
            mErrorMSG = HMAUX_TRANS_LIB.get("msg_end_date_error");

            return false;
        }

        if (!mk_start_hour.isValid()) {
            mErrorMSG = HMAUX_TRANS_LIB.get("msg_start_hour_error");

            return false;
        }

        if (!mk_end_hour.isValid() || mk_end_hour.getText().toString().isEmpty()) {
            mErrorMSG = HMAUX_TRANS_LIB.get("msg_end_hour_error");

            return false;
        }

        String sDTS = ToolBox.reverseB(mk_start_date.getText().toString()) + " " + mk_start_hour.getText().toString();

        String sDTE = reverseB(mk_end_date.getText().toString()) + " " + mk_end_hour.getText().toString();


        if (!ToolBox.isValidDateIntervalT(sDTS, sDTE)) {
            mErrorMSG = HMAUX_TRANS_LIB.get("msg_task_interval_or_future_date_error");

            return false;
        }


        int perc = rb_stepped_perc.getProgress() * (int) interval + (int) min;


        if (perc <= 0) {
            mErrorMSG = HMAUX_TRANS_LIB.get("msg_value_error");

            return false;
        }

        mErrorMSG = "";

        return true;
    }

    private void showTechDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.taskcontrol_dialog, null);

        final TextView tv_title = (TextView) mView.findViewById(R.id.taskcontrol_dialog_tv_title);
        final Button btnCancel = (Button) mView.findViewById(R.id.taskcontrol_dialog_btn_cancel);
        final Button btnSaveParcially = (Button) mView.findViewById(R.id.taskcontrol_dialog_btn_save_parcial);
        final Button btnSaveFull = (Button) mView.findViewById(R.id.taskcontrol_dialog_btn_save_full);

        dialog.setView(mView);

        tv_title.setText(HMAUX_TRANS_LIB.get("task_dialog_title"));
        btnCancel.setText(HMAUX_TRANS_LIB.get("task_dialog_cancel"));
        btnSaveParcially.setText(HMAUX_TRANS_LIB.get("task_dialog_save_parcially"));
        btnSaveFull.setText(HMAUX_TRANS_LIB.get("task_dialog_save_full"));

        final AlertDialog alert = dialog.create();
        alert.setCancelable(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        btnSaveParcially.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int perc = rb_stepped_perc.getProgress() * (int) interval + (int) min;

                if (isValid() && perc < 100) {
                    informTaskActiveClosed();

                    alert.dismiss();

                    autoconf = false;

                } else {

                    if (autoconf) {
                        autoconf = false;
                        mk_end_date.setText("");
                        mk_end_hour.setText("");
                    }

                    if (perc >= 100) {
                        informTaskError(HMAUX_TRANS_LIB.get("msg_parcially_100_error"));
                    } else {
                        informTaskError(mErrorMSG);
                    }

                    alert.dismiss();

                }
            }
        });

        btnSaveFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int perc = rb_stepped_perc.getProgress() * (int) interval + (int) min;

                if (isValid() && perc == 100) {
                    informTaskActiveClosed();

                    alert.dismiss();

                    autoconf = false;

                } else {

                    if (autoconf) {
                        autoconf = false;
                        mk_end_date.setText("");
                        mk_end_hour.setText("");
                    }

                    if (perc != 100) {
                        informTaskError(HMAUX_TRANS_LIB.get("msg_full_100_error"));
                    } else {
                        informTaskError(mErrorMSG);
                    }

                    alert.dismiss();

                }
            }
        });

        alert.show();

    }

}