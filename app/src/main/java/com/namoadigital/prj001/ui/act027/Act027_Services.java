package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act027_Services_Adapter;
import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.sql.MD_Partner_Sql_SS;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.sql.SM_SO_Sql_016;
import com.namoadigital.prj001.sql.SM_SO_Sql_023;
import com.namoadigital.prj001.sql.Sql_Act027_002;
import com.namoadigital.prj001.sql.Sql_Act027_003;
import com.namoadigital.prj001.sql.Sql_Act027_004;
import com.namoadigital.prj001.sql.Sql_Act027_005;
import com.namoadigital.prj001.sql.Sql_Act027_006;
import com.namoadigital.prj001.ui.act028.Act028_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Services extends BaseFragment {

    private boolean bStatus;

    private Context context;
    private TextView tv_filter_lbl;
    private Switch sw_filter;
    private ListView lv_services;
    private Act027_Services_Adapter adp;
    private SM_SO_ServiceDao sm_so_serviceDao;
    private SM_SO_Service_ExecDao sm_so_service_execDao;
    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;
    private SM_SO mSm_so;
    private HMAux partnerAux = new HMAux();
    private String lastServiceUpdated = "";
    private int original_update_required;
    private Act027_Main mMain;
    private ImageView iv_product_serial_id;
    private TextView tv_product_serial_id;
    private TextView tv_product_serial_infos;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public interface IAct027_Services {
        void onServiceSelected(HMAux sService);
    }

    private IAct027_Services delegate;
    private OnRecoveryFragmentState recoveryDelegate;

    public void setOnServiceSelectedListener(IAct027_Services delegate) {
        this.delegate = delegate;
    }

    public void setLastServiceUpdated(String lastServiceUpdated) {
        this.lastServiceUpdated = lastServiceUpdated;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bStatus = true;

        View view = inflater.inflate(R.layout.act027_services_content, container, false);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        recoveryDelegate = (OnRecoveryFragmentState) context;
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

    private void iniVar(View view) {
        context = getActivity();
        //
        mMain = (Act027_Main) getActivity();
        //
        sm_so_serviceDao = new SM_SO_ServiceDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_execDao = new SM_SO_Service_ExecDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        tv_filter_lbl = (TextView) view.findViewById(R.id.act027_services_content_tv_filter_lbl);
        iv_product_serial_id =  view.findViewById(R.id.iv_product_serial_id);
        tv_product_serial_id = view.findViewById(R.id.tv_product_serial_id);
        tv_product_serial_infos = view.findViewById(R.id.tv_product_serial_infos);
        sw_filter = (Switch) view.findViewById(R.id.act027_services_content_sw_filter);
        lv_services = (ListView) view.findViewById(R.id.act027_services_content_lv_services);
    }

    private void iniAction() {

        sw_filter.setOnCheckedChangeListener(sw_filter_listener);
    }

    private CompoundButton.OnCheckedChangeListener sw_filter_listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setServiceAdapter(isChecked);
        }
    };

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null
                    && hmAux_Trans != null) {
                //
                original_update_required = mSm_so.getUpdate_required();
                //
                tv_filter_lbl.setText(hmAux_Trans.get("filter_lbl"));
                //
                //sw_filter.setChecked(true);
                //
                if ((!mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING) &&
                        !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)) ||
                        !mMain.hasExecutionProfile()
                ) {
                    sw_filter.setOnCheckedChangeListener(null);
                    sw_filter.setChecked(false);
                    sw_filter.setEnabled(false);
                    //
                    if (mMain.hasExecutionProfile()) {
                        sw_filter.setEnabled(true);
                        sw_filter.setOnCheckedChangeListener(sw_filter_listener);
                    }

                }
                //
                setServiceAdapter(sw_filter.isChecked());
                setSerialINfo();
            } else {
                recoveryDelegate.callAct005();
            }
        }
    }

    private void setSerialINfo() {

        HMAux product_serial_content = sm_so_serviceDao.getByStringHM(
                new SM_SO_Sql_023(
                        mSm_so.getCustomer_code(),
                        String.valueOf(mSm_so.getProduct_code()),
                        mSm_so.getSerial_id()
                ).toSqlQuery()
        );

        try {
            if (product_serial_content.hasConsistentValue(SM_SODao.SERIAL_ID)) {
                tv_product_serial_id.setText(product_serial_content.get(SM_SODao.SERIAL_ID));
            }
            String serial_bmd = "";
            if (product_serial_content.hasConsistentValue(MD_BrandDao.BRAND_DESC)
                    && !product_serial_content.get(MD_BrandDao.BRAND_DESC).isEmpty()) {
                serial_bmd = product_serial_content.get(MD_BrandDao.BRAND_DESC);
            }
            if (product_serial_content.hasConsistentValue(MD_Brand_ModelDao.MODEL_DESC)
                    && !product_serial_content.get(MD_Brand_ModelDao.MODEL_DESC).isEmpty()) {
                if(serial_bmd.isEmpty()){
                    serial_bmd = product_serial_content.get(MD_Brand_ModelDao.MODEL_DESC);
                }else {
                    serial_bmd = serial_bmd + " | " + product_serial_content.get(MD_Brand_ModelDao.MODEL_DESC);
                }
            }
            if (product_serial_content.hasConsistentValue(MD_Brand_ColorDao.COLOR_DESC)
                    && !product_serial_content.get(MD_Brand_ColorDao.COLOR_DESC).isEmpty()) {
                if(serial_bmd.isEmpty()){
                    serial_bmd = product_serial_content.get(MD_Brand_ColorDao.COLOR_DESC);
                }else {
                    serial_bmd = serial_bmd + " | " + product_serial_content.get(MD_Brand_ColorDao.COLOR_DESC);
                }
            }
            tv_product_serial_infos.setText(serial_bmd);
            tv_product_serial_infos.setVisibility(View.VISIBLE);
            if(serial_bmd.isEmpty()){
                tv_product_serial_infos.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            tv_product_serial_infos.setText("");
            tv_product_serial_infos.setVisibility(View.GONE);
        }
//        Glide.with(context)
//                .load("url")
//                .into(iv_product_serial_id);
    }

    public void setServiceAdapter(boolean isChecked) {

        if(mSm_so == null){
            recoveryDelegate.callAct005();
        }else {
            adp = new Act027_Services_Adapter(
                    getActivity(),
                    R.layout.act027_services_content_adapter_cell,
                    sm_so_serviceDao.query_HM(
                                /*new SM_SO_Service_Sql_003(
                                        mSm_so.getCustomer_code(),
                                        mSm_so.getSo_prefix(),
                                        mSm_so.getSo_code()
                                ).toSqlQuery()*/
                                /*new Sql_Act027_001(
                                        mSm_so.getCustomer_code(),
                                        mSm_so.getSo_prefix(),
                                        mSm_so.getSo_code()
                                ).toSqlQuery()*/
                            new Sql_Act027_002(
                                    mSm_so.getCustomer_code(),
                                    mSm_so.getSo_prefix(),
                                    mSm_so.getSo_code(),
                                    ToolBox_Con.getPreference_User_Code(context),
                                    ToolBox_Con.getPreference_Site_Code(context),
                                    ToolBox_Con.getPreference_Zone_Code(context),
                                    isChecked//sw_filter != null && sw_filter.isChecked()
                            ).toSqlQuery()
                    ),
                    mMain.hasExecutionProfile()
            );
            //
            adp.setOnServiceSelectedListener(new Act027_Services_Adapter.IAct027_Services_Adapter() {
                @Override
                public void serviceSelected(HMAux sData, String selection_type) {

                    HMAux sService = sm_so_serviceDao.getByStringHM(
                            new SM_SO_Service_Sql_004(
                                    Long.parseLong(sData.get("customer_code")),
                                    Integer.parseInt(sData.get("so_prefix")),
                                    Integer.parseInt(sData.get("so_code")),
                                    Integer.parseInt(sData.get("price_list_code")),
                                    Integer.parseInt(sData.get("pack_code")),
                                    Integer.parseInt(sData.get("pack_seq")),
                                    Integer.parseInt(sData.get("category_price_code")),
                                    Integer.parseInt(sData.get("service_code")),
                                    Integer.parseInt(sData.get("service_seq"))
                            ).toSqlQuery()
                    );
                    //Tratativa para o nova ação do btn express é que igual ao btn normal...
                    //Confuso ?! kkkk Senta e chora
                    if (selection_type.equals(Act027_Main.SELECTION_EXPRESS) &&
                            sData.get(Sql_Act027_002.YES_NO_ICON).equals("0") &&
                            sData.get(Sql_Act027_002.START_STOP_ACTION).equals(Sql_Act027_002.ACTION_NONE)
                    ) {
                        selection_type = Act027_Main.SELECTION_NORMAL;
                    }

                    switch (selection_type) {
                        case Act027_Main.SELECTION_EXPRESS:
                            serviceExpress(sData);
                            break;
                        case Act027_Main.SELECTION_NORMAL:
                            if (delegate != null) {
                                delegate.onServiceSelected(sService);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
            //
            lv_services.setAdapter(adp);
            //
            if (adp.getCount() == 0) {
                mMain.openDrawerInternally();
            }

            //Se possui var indicando qual seriviço foi alterado,
            //Aplica "auto scroll"
            if (!lastServiceUpdated.equals("")) {
                int idx = adp.getPositionByPk(lastServiceUpdated);
                //
                if (idx > -1) {
                    lv_services.setSelection(idx);
                }
                lastServiceUpdated = "";
            }
        }

    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }

    private void serviceExpress(final HMAux item) {

        if (item.get(SM_SO_ServiceDao.EXEC_TYPE).equals(Constant.SO_SERVICE_TYPE_START_STOP)) {

            if (item.get(Sql_Act027_002.START_STOP_ACTION).equals(Sql_Act027_002.ACTION_PLAY)) {
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_start_task_confirm_ttl"),
                        hmAux_Trans.get("alert_start_task_confirm_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (item.get(Sql_Act027_002.PARTNER_RESTRICTION).equals("-1")) {
                                    showPartnerDialog(item);
                                } else {
                                    createExecTask(item);
                                }
                            }
                        },
                        1
                );
            } else {

                HMAux execTaskAux = sm_so_service_exec_taskDao.getByStringHM(
                        new Sql_Act027_004(
                                item.get(SM_SO_ServiceDao.CUSTOMER_CODE),
                                item.get(SM_SO_ServiceDao.SO_PREFIX),
                                item.get(SM_SO_ServiceDao.SO_CODE),
                                item.get(SM_SO_ServiceDao.PRICE_LIST_CODE),
                                item.get(SM_SO_ServiceDao.PACK_CODE),
                                item.get(SM_SO_ServiceDao.PACK_SEQ),
                                item.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE),
                                item.get(SM_SO_ServiceDao.SERVICE_CODE),
                                item.get(SM_SO_ServiceDao.SERVICE_SEQ),
                                ToolBox_Con.getPreference_User_Code(context)

                        ).toSqlQuery()
                );
                //
                if (execTaskAux != null) {
                    sendToTask(item, execTaskAux.get(SM_SO_Service_Exec_TaskDao.EXEC_TMP), execTaskAux.get(SM_SO_Service_Exec_TaskDao.TASK_TMP));
                } else {
                    Log.d("SHORTCUT_STOP", "Exec_tmp e task_tmp não encontrado.");
                }
            }

        } else {
            if (item.get(Sql_Act027_002.PARTNER_RESTRICTION).equals("-1")) {
                showPartnerDialog(item);
            } else {
                createExecTask(item);
            }
        }
    }

    private void sendToTask(HMAux sService, String exec_tmp, String task_tmp) {

        Bundle bundle = new Bundle();
        bundle.putString(SM_SODao.SO_PREFIX, sService.get(SM_SO_Service_Exec_TaskDao.SO_PREFIX));
        bundle.putString(SM_SODao.SO_CODE, sService.get(SM_SO_Service_Exec_TaskDao.SO_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE, sService.get(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.PACK_CODE, sService.get(SM_SO_Service_Exec_TaskDao.PACK_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.PACK_SEQ, sService.get(SM_SO_Service_Exec_TaskDao.PACK_SEQ));
        bundle.putString(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE, sService.get(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.SERVICE_CODE, sService.get(SM_SO_Service_Exec_TaskDao.SERVICE_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ, sService.get(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ));
        bundle.putString(SM_SO_Service_Exec_TaskDao.EXEC_TMP, exec_tmp);
        bundle.putString(SM_SO_Service_Exec_TaskDao.TASK_TMP, task_tmp);
        bundle.putInt(Constant.ACT027_ORIGINAL_UPDATE_REQUIRED, original_update_required);

        bundle.putSerializable("data", sService);

        bundle.putBoolean(Constant.ACT027_IS_SHORTCUT, true);

        Intent mIntent = new Intent(context, Act028_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);

        getActivity().finish();

    }

    private void createExecTask(HMAux item) {
        //Seta pk do serviço para q a lista seja recarregada no Serviço clicado
        lastServiceUpdated =
                item.get(SM_SO_ServiceDao.CUSTOMER_CODE) + "|" +
                        item.get(SM_SO_ServiceDao.SO_PREFIX) + "|" +
                        item.get(SM_SO_ServiceDao.SO_CODE) + "|" +
                        item.get(SM_SO_ServiceDao.PRICE_LIST_CODE) + "|" +
                        item.get(SM_SO_ServiceDao.PACK_CODE) + "|" +
                        item.get(SM_SO_ServiceDao.PACK_SEQ) + "|" +
                        item.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE) + "|" +
                        item.get(SM_SO_ServiceDao.SERVICE_CODE) + "|" +
                        item.get(SM_SO_ServiceDao.SERVICE_SEQ);
        //
        //Monta o obj serviço
        SM_SO_Service sm_so_service = sm_so_serviceDao.getByString(
                new SM_SO_Service_Sql_001(
                        Long.parseLong(item.get(SM_SO_ServiceDao.CUSTOMER_CODE)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.SO_PREFIX)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.SO_CODE)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.PRICE_LIST_CODE)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.PACK_CODE)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.PACK_SEQ)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.SERVICE_CODE)),
                        Integer.parseInt(item.get(SM_SO_ServiceDao.SERVICE_SEQ))
                        //
                ).toSqlQuery()
        );
        //
        if (sm_so_service.getExec_type().equals(Constant.SO_SERVICE_TYPE_YES_NO)) {
            SM_SO_Service_Exec serviceExec = createExec(sm_so_service);
            //
            SM_SO_Service_Exec_Task serviceExecTask = createTask(serviceExec);
            //
            sendToTask(item, String.valueOf(serviceExecTask.getExec_tmp()), String.valueOf(serviceExecTask.getTask_tmp()));

        } else {
            //Action_Start
            SM_SO_Service_Exec execAux = sm_so_service_execDao.getByString(
                    new Sql_Act027_003(
                            item.get(SM_SO_ServiceDao.CUSTOMER_CODE),
                            item.get(SM_SO_ServiceDao.SO_PREFIX),
                            item.get(SM_SO_ServiceDao.SO_CODE),
                            item.get(SM_SO_ServiceDao.PRICE_LIST_CODE),
                            item.get(SM_SO_ServiceDao.PACK_CODE),
                            item.get(SM_SO_ServiceDao.PACK_SEQ),
                            item.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE),
                            item.get(SM_SO_ServiceDao.SERVICE_CODE),
                            item.get(SM_SO_ServiceDao.SERVICE_SEQ)
                    ).toSqlQuery()
            );
            //Se null , não existe exec criada.
            if (execAux == null) {
                SM_SO_Service_Exec serviceExec = createExec(sm_so_service);
                //
                createTask(serviceExec);
            } else {
                /*
                Verificação de parceiro.
                    Mesmo a execução ja estando criado, é necessário verificar se existe parceiro
                    vinculada a ela, pois caso a unica task existente tenha sido cancelada e não
                    há parceiro definido no serviço, o parceiro da execução é setado para null.
                 */
                if (execAux.getPartner_code() == null && partnerAux != null && partnerAux.size() > 0) {
                    try {

                        execAux.setPartner_code(Integer.valueOf(partnerAux.get(SearchableSpinner.CODE)));
                        execAux.setPartner_id(partnerAux.get(SearchableSpinner.ID));
                        execAux.setPartner_desc(partnerAux.get(SearchableSpinner.DESCRIPTION));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Atualiza parceiro na exec
                    sm_so_service_execDao.addUpdateTmp(execAux);
                }
                //
                HMAux execTaskAux = sm_so_service_exec_taskDao.getByStringHM(
                        new Sql_Act027_005(
                                item.get(SM_SO_ServiceDao.CUSTOMER_CODE),
                                item.get(SM_SO_ServiceDao.SO_PREFIX),
                                item.get(SM_SO_ServiceDao.SO_CODE),
                                item.get(SM_SO_ServiceDao.PRICE_LIST_CODE),
                                item.get(SM_SO_ServiceDao.PACK_CODE),
                                item.get(SM_SO_ServiceDao.PACK_SEQ),
                                item.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE),
                                item.get(SM_SO_ServiceDao.SERVICE_CODE),
                                item.get(SM_SO_ServiceDao.SERVICE_SEQ)
                        ).toSqlQuery()
                );

                createTask(execAux, execTaskAux == null ? null : execTaskAux.get(SM_SO_Service_Exec_TaskDao.TASK_PERC));
            }
            //
            if (ToolBox_Con.isOnline(context)) {
                Act027_Main mMain = (Act027_Main) getActivity();
                //Seta flag de somente save sem sincronismo.
                mMain.setOnly_save(true);
                //
                mMain.executeSerialSave(true);
                //mMain.executeSoSave();
            } else {
                Act027_Main mMain = (Act027_Main) getActivity();
                mMain.refreshUI();
            }
        }


    }

    private SM_SO_Service_Exec_Task createTask(SM_SO_Service_Exec serviceExec, String task_perc) {
        /*
         * Cria Task
         */
        //Pega proximo task_seq_oper
        HMAux taskSeqOperAux =
                sm_so_service_exec_taskDao.getByStringHM(
                        new Sql_Act027_006(
                                serviceExec.getCustomer_code(),
                                serviceExec.getSo_prefix(),
                                serviceExec.getSo_code(),
                                serviceExec.getPrice_list_code(),
                                serviceExec.getPack_code(),
                                serviceExec.getPack_seq(),
                                serviceExec.getCategory_price_code(),
                                serviceExec.getService_code(),
                                serviceExec.getService_seq()
                        ).toSqlQuery()
                );

        int next_task_seq_oper = taskSeqOperAux != null ? Integer.parseInt(taskSeqOperAux.get(Sql_Act027_006.NEXT_TASK_SEQ_OPER)) : 1;
        //
        SM_SO_Service_Exec_Task newTask = new SM_SO_Service_Exec_Task();
        //
        newTask.setTask_code(0);
        newTask.setTask_perc(task_perc == null ? 0 : Integer.parseInt(task_perc));
        newTask.setQty_people(1);
        newTask.setTask_seq_oper(next_task_seq_oper);
        newTask.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        newTask.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        newTask.setStatus(Constant.SYS_STATUS_PROCESS);
        newTask.setStart_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"));
        newTask.setEnd_date("");
        //newTask.setComments(null);

        newTask.setPK(serviceExec);

        long nTaskTemp = Long.parseLong(sm_so_service_exec_taskDao.getByStringHM(
                new SM_SO_Service_Exec_Task_Sql_004(
                        newTask.getCustomer_code(),
                        newTask.getSo_prefix(),
                        newTask.getSo_code(),
                        newTask.getPrice_list_code(),
                        newTask.getPack_code(),
                        newTask.getPack_seq(),
                        newTask.getCategory_price_code(),
                        newTask.getService_code(),
                        newTask.getService_seq(),
                        newTask.getExec_tmp()

                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_Sql_004.NEXT_TMP));

        newTask.setTask_tmp(nTaskTemp);
        sm_so_service_exec_taskDao.addUpdateTmp(newTask);
        //Seta S.O para update required.
        sm_so_serviceDao.addUpdate(
                new SM_SO_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code()
                ).toSqlQuery()
        );

        return newTask;
    }

    private SM_SO_Service_Exec createExec(SM_SO_Service sm_so_service) {

        /*
         * Cria Exec
         */
        SM_SO_Service_Exec newExec = new SM_SO_Service_Exec();
        //
        newExec.setExec_code(0);

        newExec.setPK(sm_so_service);

        long nExecTemp = Long.parseLong(sm_so_service_execDao.getByStringHM(
                new SM_SO_Service_Exec_Sql_003(
                        sm_so_service.getCustomer_code(),
                        sm_so_service.getSo_prefix(),
                        sm_so_service.getSo_code(),
                        sm_so_service.getPrice_list_code(),
                        sm_so_service.getPack_code(),
                        sm_so_service.getPack_seq(),
                        sm_so_service.getCategory_price_code(),
                        sm_so_service.getService_code(),
                        sm_so_service.getService_seq()

                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Sql_003.NEXT_TMP));
        //
        newExec.setExec_tmp(nExecTemp);
        newExec.setStatus(Constant.SYS_STATUS_PROCESS);
        //
        if (sm_so_service.getPartner_code() == null) {
            try {
                partnerAux.size();
                newExec.setPartner_code(Integer.valueOf(partnerAux.get(SearchableSpinner.CODE)));
                newExec.setPartner_id(partnerAux.get(SearchableSpinner.ID));
                newExec.setPartner_desc(partnerAux.get(SearchableSpinner.DESCRIPTION));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newExec.setPartner_code(sm_so_service.getPartner_code());
            newExec.setPartner_id(sm_so_service.getPartner_id());
            newExec.setPartner_desc(sm_so_service.getPartner_desc());
        }
        //Insere execução
        sm_so_service_execDao.addUpdateTmp(newExec);
        //Seta S.O para update required.
        sm_so_serviceDao.addUpdate(
                new SM_SO_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code()
                ).toSqlQuery()
        );

        return newExec;
    }

    private SM_SO_Service_Exec_Task createTask(SM_SO_Service_Exec serviceExec) {
        return createTask(serviceExec, null);
    }

    private void showPartnerDialog(final HMAux item) {

        MD_PartnerDao md_partnerDao = new MD_PartnerDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        final ArrayList<HMAux> partners = (ArrayList<HMAux>) md_partnerDao.query_HM(

                new MD_Partner_Sql_SS(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        if (partners == null || partners.size() == 0) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_partner_selection_ttl"),
                    hmAux_Trans.get("alert_no_partner_found_msg"),
                    null,
                    0
            );
        } else if (partners.size() == 1) {
            partnerAux.putAll(partners.get(0));
            // Chama criação da execTask
            createExecTask(item);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act028_dialog_new_partner_opt, null);

            SearchableSpinner ss_partner = (SearchableSpinner) view.findViewById(R.id.act028_dialog_new_partner_opt_ss_partner);

            ss_partner.setmLabel(hmAux_Trans.get("partner_selection_ttl"));
            ss_partner.setmTitle(hmAux_Trans.get("partner_search_lbl"));


            if (partners.size() > 0) {
                HMAux hmAux = new HMAux();
                hmAux.put(SearchableSpinner.CODE, "0");
                hmAux.put(SearchableSpinner.ID, "0");
                hmAux.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get("select_partner_lbl"));

                ss_partner.setmValue(hmAux);
            }

            ss_partner.setmOption(partners);

            builder.setView(view);
            builder.setCancelable(true);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (partnerAux.size() == 0) {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_partner_selection_ttl"),
                                hmAux_Trans.get("alert_no_partner_selected_msg"),
                                null,
                                0
                        );
                    } else {
                        createExecTask(item);
                    }
                }
            });

            final AlertDialog show = builder.show();

            ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
                @Override
                public void onItemPreSelected(HMAux hmAux) {

                }

                @Override
                public void onItemPostSelected(HMAux hmAux) {
                    partnerAux.clear();

                    partnerAux.putAll(hmAux);

                    show.dismiss();
                }
            });
        }
    }

}
