package com.namoadigital.prj001.ui.act047;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act047_SO_Next_Orders_Adapter;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.databinding.Act047FilterDialogBinding;
import com.namoadigital.prj001.databinding.Act047SoNextOrdersDialogBinding;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.SO_Next_Orders_Obj;
import com.namoadigital.prj001.model.SmPriority;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WSSoStatusChange;
import com.namoadigital.prj001.service.WS_SO_Next_Orders;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.sql.MD_Partner_Sql_SS;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act047.model.NextOsFilter;
import com.namoadigital.prj001.ui.act047.model.TypeDeadlineFilter;
import com.namoadigital.prj001.ui.act047.model.TypePriorityFilter;
import com.namoadigital.prj001.ui.act047.model.TypeStatusFilter;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act047_Main extends Base_Activity implements Act047_Main_Contract.I_View {

    public static final String WS_PROCESS_SO_STATUS_CHANGE = "WS_PROCESS_SO_STATUS_CHANGE";
    private TextView tv_site;
    private TextView tv_qty;
    private TextView tv_zone;
    private TextView tv_empty_list;
    private TextView tv_filter;
    private ListView lv_services;
    private SwitchCompat sw_filter;
    private Act047_SO_Next_Orders_Adapter mAdapter;
    private String requestingAct = "";
    private Act047_Main_Contract.I_Presenter mPresenter;
    private String wsProcess = "";
    //Var tmp que armazena o item da lista clicado.
    private SO_Next_Orders_Obj wsTmpItem = null;
    private MKEditTextNM mketFilter;
    private String filterSerial;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act047_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT047
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("alert_error_order_list_ttl");
        transList.add("alert_error_order_list_msg");
        transList.add("alert_no_orders_found_ttl");
        transList.add("alert_no_orders_found_msg");
        transList.add("dialog_next_orders_search_ttl");
        transList.add("dialog_next_orders_search_msg");
        transList.add("qty_lbl");
        //
        transList.add("dialog_so_desc_lbl");
        transList.add("dialog_services_lbl");
        transList.add("dialog_so_comment_lbl");
        transList.add("dialog_so_details_ttl");
        //
        transList.add("dialog_so_download_ttl");
        transList.add("dialog_so_download_start");
        transList.add("dialog_serial_download_ttl");
        transList.add("dialog_serial_download_start");
        transList.add("alert_no_so_returned_ttl");
        transList.add("alert_no_so_returned_msg");
        transList.add("alert_so_download_param_error_ttl");
        transList.add("alert_so_download_param_error_msg");
        transList.add("alert_so_not_returned_ttl");
        transList.add("alert_so_not_returned_msg");
        transList.add("alert_serial_not_returned_ttl");
        transList.add("alert_serial_not_returned_msg");
        transList.add("alert_no_serial_returned_ttl");
        transList.add("alert_no_serial_returned_msg");
        transList.add("serial_hint");
        transList.add("serial_no_match_hint");
        //
        transList.add("dialog_so_serial_position_lbl");
        transList.add("dialog_so_site_lbl");
        transList.add("dialog_so_zone_lbl");
        transList.add("dialog_so_local_lbl");
        transList.add("dialog_so_created_by_lbl");
        transList.add("dialog_so_approved_by_lbl");
        //
        transList.add("filter_hint");
        //
        transList.add("dialog_so_add_info_lbl");
        //
        transList.add("empty_serial_list_lbl");
        transList.add("zone_filter_lbl");
        //
        transList.add("dialog_so_product_lbl");
        //
        transList.add("filter_dialog_ttl_lbl");
        transList.add("filter_dialog_status_lbl");
        transList.add("filter_dialog_deadline_lbl");
        transList.add("filter_dialog_deadline_without_lbl");
        transList.add("filter_dialog_deadline_expired_lbl");
        transList.add("filter_dialog_deadline_not_expired_lbl");
        transList.add("filter_dialog_orders_lbl");
        transList.add("filter_dialog_priority_deadline_lbl");
        transList.add("filter_dialog_priority_date_created_lbl");
        transList.add("filter_dialog_priority_lbl");
        transList.add("filter_dialog_priority_all_lbl");
        transList.add("filter_dialog_zone_lbl");
        transList.add("filter_dialog_partner_lbl");
        transList.add("filter_dialog_ok_lbl");
        transList.add("filter_dialog_cancel_lbl");
        transList.add("filter_option_invalid");

        //
        transList.add("progress_status_change_ttl");
        transList.add("progress_status_change_msg");
        //
        transList.add("dialog_block_service_lbl");
        transList.add("dialog_active_service_lbl");
        transList.add("dialog_ok_lbl");
        transList.add("dialog_close_lbl");
        //
        transList.add("status_block_success_toast");
        transList.add("status_active_success_toast");
        transList.add("status_change_error_toast");
        //


        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        //
        recoverIntentsInfo();

        //
        tv_zone = (TextView) findViewById(R.id.act047_tv_zone_val);
        //
        tv_filter = findViewById(R.id.act047_tv_zone_filter);
        //
        tv_empty_list = (TextView) findViewById(R.id.act047_list_empty);
        //
        mketFilter = findViewById(R.id.act047_mket_filter);
        //
        lv_services = (ListView) findViewById(R.id.act047_lv_services);
        //
        sw_filter = findViewById(R.id.act047_sw_filter);
        sw_filter.setOnCheckedChangeListener(null);
        sw_filter.setChecked(getSwitchState());
        sw_filter.setOnCheckedChangeListener(sw_filter_listener);
        //
        mPresenter = new Act047_Main_Presenter(
                context,
                this,
                requestingAct,
                hmAux_Trans
        );
        setLocationInfo();
        //
        setLabels();
        //
        mPresenter.executeNextOrdersSearch(getSwitchState());
    }

    private void setLabels() {
        mketFilter.setHint(hmAux_Trans.get("filter_hint"));
        tv_empty_list.setText(hmAux_Trans.get("empty_serial_list_lbl"));
        tv_filter.setText(hmAux_Trans.get("zone_filter_lbl"));
        if (!filterSerial.isEmpty()) mketFilter.setText(filterSerial);
    }

    private void checkIfContainsFilter() {
        String text = mketFilter.getText().toString();
        if (mAdapter != null && !text.isEmpty()) {
            mAdapter.getFilter().filter(text);
        }
    }

    private CompoundButton.OnCheckedChangeListener sw_filter_listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setSwitchState(isChecked);
            mPresenter.executeNextOrdersSearch(isChecked);
        }
    };


    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
            filterSerial = bundle.getString("FILTER_SERIAL", "");
        } else {
            requestingAct = Constant.ACT005;
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    /**
     * LUCHE - 16/01/2020
     * <p>
     * Metodo que reseta as variaveis relativas a chamada de WS e fecha o dialog.
     */
    @Override
    public void cleanWsTmpItem() {
        wsProcess = "";
        wsTmpItem = null;
        disableProgressDialog();
    }

    private void setLocationInfo() {
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        //
        tv_zone.setVisibility(View.GONE);
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)
                && mFooter.containsKey(Constant.FOOTER_ZONE)
                && !mFooter.get(Constant.FOOTER_ZONE).isEmpty()
        ) {
            tv_zone.setVisibility(View.VISIBLE);
            tv_zone.setText(mFooter.get(Constant.FOOTER_ZONE));
        }
        //
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void loadNextOrders(ArrayList<SO_Next_Orders_Obj> nextOrdersObjs) {
        //
        setTitleLanguage(" (" + nextOrdersObjs.size() + " / " + mPresenter.getOriginalListFromSoNextOrders().size() + ")");
        //
        setAdapter(nextOrdersObjs);
        //Checar se o usuario está usando o filtro
        checkIfContainsFilter();
    }

    private ArrayList<SO_Next_Orders_Obj> orderListItem;

    private void setAdapter(ArrayList<SO_Next_Orders_Obj> list) {
        changeVisibilityAdapter(list);
        orderListItem = list;
        if (mAdapter != null) {
            mAdapter.changeListByFilter(list);
            return;
        }

        mAdapter = new Act047_SO_Next_Orders_Adapter(
                getApplicationContext(),
                list,
                R.layout.act047_cell_new,
                this::changeVisibilityAdapter
        );
        lv_services.setAdapter(mAdapter);
    }


    private void changeVisibilityAdapter(ArrayList<SO_Next_Orders_Obj> list) {
        lv_services.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        tv_empty_list.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAlert(String ttl, String msg) {
        showAlert(ttl, msg, null);
    }

    @Override
    public void showAlert(String ttl, String msg, DialogInterface.OnClickListener listener) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                listener,
                0
        );
    }

    /**
     * LUCHE - 16/01/2020
     * <p>
     * Alterado metodo para verificar se o progressDialog ja esta instanciado e, caso esteja, atualiza
     * title, msg e exibe o dialog ao inves de criar uma nova instancia.
     * <p>
     * Teste feito para tentar resolver problemas que acontecem em algumas telas que tem chamadas de
     * ws encadeadas. Como cada chamada do enableProgressDialog, gera uma nova instancia do progressDialog,
     * era possivel empilhar dialogs e não conseguir fechar los ja que o se houvessem 2 aberto,
     * não existe mais referencia do primeiro tornando impossivel fechar o dialog.     *
     * <p>
     * O teste mostrou ser efetivo e talvez fosse interessando aplicar esse conceito direto na BaseACt
     *
     * @param title - Titulo
     * @param msg   - Msg
     */
    @Override
    public void showPD(String title, String msg) {
        if (progressDialog == null) {
            enableProgressDialog(
                    title,
                    msg,
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
        } else {
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
            //
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    @Override
    public void showNoConnecionMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_no_conection_ttl"),
                hmAux_Trans.get("alert_no_conection_msg"),
                (dialog, which) -> onBackPressed(),
                0
        );
    }


    @Override
    public void showErrorMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_error_order_list_ttl"),
                hmAux_Trans.get("alert_error_order_list_msg"),
                (dialog, which) -> dialog.dismiss(),
                0
        );
    }


    private String soChangeType;
    private AlertDialog detailDialog;
    private Act047SoNextOrdersDialogBinding bindingDetailDialog;
    private String tokenDialog = "";

    private void showDetailsDialog(final SO_Next_Orders_Obj item) {

        AlertDialog.Builder builder = null;
        if (detailDialog == null || !detailDialog.isShowing()) {
            builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            bindingDetailDialog = Act047SoNextOrdersDialogBinding.inflate(LayoutInflater.from(context));
        }
        //
        bindingDetailDialog.act047SoNextOrdersDialogTvTitle.setText((hmAux_Trans.get("dialog_so_details_ttl") + " " + item.getSo_prefix() + "." + item.getSo_code()));
        bindingDetailDialog.act047SoNextOrdersDialogTvStatus.setText(hmAux_Trans.get(item.getStatus()));
        bindingDetailDialog.act047SoNextOrdersDialogTvStatus.setTextColor(ToolBox_Inf.getStatusColorV2(context, item.getStatus()));

        if (item.getSo_desc() == null || item.getSo_desc().isEmpty()) {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoDesc.setVisibility(View.GONE);
        } else {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoDesc.setVisibility(View.VISIBLE);
            bindingDetailDialog.act047SoNextOrdersDialogTvSoDescLbl.setText(hmAux_Trans.get("dialog_so_desc_lbl"));
            bindingDetailDialog.act047SoNextOrdersDialogTvSoDescVal.setText(item.getSo_desc());
        }
        if (item.getComments() == null || item.getComments().isEmpty()) {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoComment.setVisibility(View.GONE);
        } else {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoComment.setVisibility(View.VISIBLE);
            bindingDetailDialog.act047SoNextOrdersDialogTvSoCommentLbl.setText(hmAux_Trans.get("dialog_so_comment_lbl"));
            bindingDetailDialog.act047SoNextOrdersDialogTvSoCommentVal.setText(item.getComments());
        }

        bindingDetailDialog.act047SoNextOrdersDialogTvServicesLbl.setText(hmAux_Trans.get("dialog_services_lbl"));
        bindingDetailDialog.act047SoNextOrdersDialogTvServicesVal.setText(item.getService());

        bindingDetailDialog.act047SoNextOrdersDialogMketSerialConfirm.setHint(hmAux_Trans.get("serial_hint"));
        bindingDetailDialog.act047SoNextOrdersDialogTvError.setText(hmAux_Trans.get("serial_no_match_hint"));

        bindingDetailDialog.act047SoNextOrdersDialogTvPositionLbl.setText(hmAux_Trans.get("dialog_so_serial_position_lbl"));
        if (item.getSerial_site_code() != null) {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoPosition.setVisibility(View.VISIBLE);
            if (item.getSerial_site_code().equals(ToolBox_Con.getPreference_Site_Code(context))) {
                bindingDetailDialog.act047SoNextOrdersDialogLlSoPositionSite.setVisibility(View.GONE);
                bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionSiteVal.setTextColor(getResources().getColor(R.color.font_normal));
                bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionZoneVal.setTextColor(getResources().getColor(R.color.font_normal));
                bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionLocalVal.setTextColor(getResources().getColor(R.color.font_normal));
            } else {
                bindingDetailDialog.act047SoNextOrdersDialogLlSoPositionSite.setVisibility(View.VISIBLE);
                bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionSiteVal.setTextColor(getResources().getColor(R.color.namoa_status_error));
                bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionZoneVal.setTextColor(getResources().getColor(R.color.namoa_status_error));
                bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionLocalVal.setTextColor(getResources().getColor(R.color.namoa_status_error));
            }
        } else {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoPosition.setVisibility(View.GONE);
            bindingDetailDialog.act047SoNextOrdersDialogLlSoPositionSite.setVisibility(View.GONE);
        }

        bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionSiteLbl.setText(hmAux_Trans.get("dialog_so_site_lbl"));
        bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionSiteVal.setText(item.getSerial_site_desc());
        bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionZoneLbl.setText(hmAux_Trans.get("dialog_so_zone_lbl"));
        bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionZoneVal.setText(item.getSerial_zone_desc());
        bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionLocalLbl.setText(hmAux_Trans.get("dialog_so_local_lbl"));
        bindingDetailDialog.act047SoNextOrdersDialogTvSoPositionLocalVal.setText(item.getSerial_local_desc());

        if (item.getLast_approval_budget_user() == null
                || item.getLast_approval_budget_user().isEmpty()) {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoApprovedBy.setVisibility(View.GONE);
        } else {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoApprovedBy.setVisibility(View.VISIBLE);
            bindingDetailDialog.act047SoNextOrdersDialogTvSoApprovedByLbl.setText(hmAux_Trans.get("dialog_so_approved_by_lbl"));
            bindingDetailDialog.act047SoNextOrdersDialogTvSoApprovedByVal.setText(item.getLast_approval_budget_user());
        }

        if (item.getCreate_user() == null
                || item.getCreate_user().isEmpty()) {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoCreatedBy.setVisibility(View.GONE);
        } else {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoCreatedBy.setVisibility(View.VISIBLE);
            bindingDetailDialog.act047SoNextOrdersDialogTvSoCreatedByLbl.setText(hmAux_Trans.get("dialog_so_created_by_lbl"));
            bindingDetailDialog.act047SoNextOrdersDialogTvSoCreatedByVal.setText(item.getCreate_user());
        }
        //LUCHE - 13/07/2021 - Add infos add da o.s
        String formatedSoAddInfo = getFormatedSoAddInfo(item);
        if (!formatedSoAddInfo.isEmpty()) {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoAddInfo.setVisibility(View.VISIBLE);
            bindingDetailDialog.act047SoNextOrdersDialogTvSoAddInfoLbl.setText(hmAux_Trans.get("dialog_so_add_info_lbl"));
            bindingDetailDialog.act047SoNextOrdersDialogTvSoAddInfoVal.setText(formatedSoAddInfo);
        } else {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoAddInfo.setVisibility(View.GONE);
        }


        if (!item.getProduct_id().isEmpty()) {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoProduct.setVisibility(View.VISIBLE);
            bindingDetailDialog.act047SoNextOrdersDialogTvSoProductLbl.setText(item.getProduct_desc());
            bindingDetailDialog.act047SoNextOrdersDialogTvSoProductVal.setText(item.getSerial_id());
        } else {
            bindingDetailDialog.act047SoNextOrdersDialogLlSoProduct.setVisibility(View.GONE);
        }

        //Config TIl e Mket do seria
        configSerialViews(bindingDetailDialog.act047SoNextOrdersDialogTvError, bindingDetailDialog.act047SoNextOrdersDialogMketSerialConfirm, item);
        hideSerialForByPassProfile(bindingDetailDialog.act047SoNextOrdersDialogMketSerialConfirm);
        //
        showMiddleButton(item.getStatus(), item.getEditUser());
        //
        bindingDetailDialog.soNextOrdersDialogCancel.setText(hmAux_Trans.get("dialog_close_lbl"));
        bindingDetailDialog.soNextOrdersDialogOk.setText(hmAux_Trans.get("dialog_ok_lbl"));
        //

        if (builder != null || detailDialog == null || !detailDialog.isShowing()) {
            builder.setView(bindingDetailDialog.getRoot());
            detailDialog = builder.create();
        }//
        if (!detailDialog.isShowing()) detailDialog.show();
        //Setado listner do botão positivo nesse momento, pois era necessaria a passagem do dialog
        //como parametro do metodo checkDialogFlow
        //
        //Listener para remover o mket_serial da lista de componentes
        detailDialog.setOnDismissListener(dialogInterface -> {
            tokenDialog = "";
            controls_sta.remove(bindingDetailDialog.act047SoNextOrdersDialogMketSerialConfirm);
        });

        bindingDetailDialog.soNextOrdersDialogCancel.setOnClickListener(v -> {
            detailDialog.dismiss();
        });

        bindingDetailDialog.soNextOrdersDialogOk.setOnClickListener(v -> {
            checkDialogFlow(detailDialog, bindingDetailDialog.act047SoNextOrdersDialogTvError, bindingDetailDialog.act047SoNextOrdersDialogMketSerialConfirm, item);
        });
        bindingDetailDialog.soNextOrdersDialogMiddleAction.setOnClickListener(v -> {
            if (tokenDialog == null || tokenDialog.isEmpty())
                tokenDialog = ToolBox_Inf.getToken(context);
            mPresenter.executeSoStatusChangeService(item, soChangeType, tokenDialog);
        });
    }


    private void processReturnSoChangeStatus(HMAux hmAux) {
        SO_Next_Orders_Obj item = (SO_Next_Orders_Obj) mAdapter.getItem(soPositionCardItemClicked);
        item.setStatus(hmAux.get("so_status"));
        item.setSoScn(Integer.parseInt(hmAux.get("scn_code")));
        mAdapter.notifyDataSetChanged();
        tokenDialog = "";
        changeStatusDialog(item);
        if (soChangeType.equals(WSSoStatusChange.WS_ACTION_SO_PROCESS)) {
            Toast.makeText(context, hmAux_Trans.get("status_block_success_toast"), Toast.LENGTH_LONG).show();
        } else if (soChangeType.equals(WSSoStatusChange.WS_ACTION_SO_STOP)) {
            Toast.makeText(context, hmAux_Trans.get("status_active_success_toast"), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, hmAux_Trans.get("status_change_error_toast"), Toast.LENGTH_LONG).show();
        }
        //
        mPresenter.updateLocalSo(Integer.parseInt(item.getSo_prefix()), Integer.parseInt(item.getSo_code()));
    }

    private void changeStatusDialog(SO_Next_Orders_Obj item) {
        if (detailDialog != null && detailDialog.isShowing()) {
            showDetailsDialog(item);
        }
    }

    private void showMiddleButton(String status, String editUser) {
        String user = ToolBox_Con.getPreference_User_Code(context);
        boolean profileExists = ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_SO, ConstantBaseApp.PROFILE_MENU_SO_PARAM_CHANGE_STATUS);
        MaterialButton button = bindingDetailDialog.soNextOrdersDialogMiddleAction;
        if (status.equals(ConstantBaseApp.SYS_STATUS_EDIT)) {
            if (editUser != null && !editUser.isEmpty() && editUser.equals(user)) {
                button.setVisibility(View.VISIBLE);
                button.setText(hmAux_Trans.get("dialog_active_service_lbl"));
                button.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.namoa_color_light_green));
                soChangeType = WSSoStatusChange.WS_ACTION_SO_PROCESS;
            } else {
                button.setVisibility(View.GONE);
            }
            return;
        }

        if (profileExists) {
            if (status.equals(ConstantBaseApp.SYS_STATUS_STOP)) {
                button.setText(hmAux_Trans.get("dialog_active_service_lbl"));
                button.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.namoa_color_light_green));
                soChangeType = WSSoStatusChange.WS_ACTION_SO_PROCESS;
            } else {
                button.setText(hmAux_Trans.get("dialog_block_service_lbl"));
                button.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.namoa_color_black));
                soChangeType = WSSoStatusChange.WS_ACTION_SO_STOP;
            }
            button.setVisibility(View.VISIBLE);
            return;
        }

        button.setVisibility(View.GONE);
    }

    /**
     * LUCHE - 13/07/2021
     * <p></p>
     * Metodo que formata a exibição das 6 infos add da o.s
     * A exibição deve seguir, caso exista:
     * idx : Valor \n
     *
     * @param ordersObj
     * @return
     */
    private String getFormatedSoAddInfo(SO_Next_Orders_Obj ordersObj) {
        String finalAddInfo = "";
        //
        finalAddInfo += getIdxAddInfoWithExists("1", ordersObj.getAdd_inf1());
        finalAddInfo += getIdxAddInfoWithExists("2", ordersObj.getAdd_inf2());
        finalAddInfo += getIdxAddInfoWithExists("3", ordersObj.getAdd_inf3());
        finalAddInfo += getIdxAddInfoWithExists("4", ordersObj.getAdd_inf4());
        finalAddInfo += getIdxAddInfoWithExists("5", ordersObj.getAdd_inf5());
        finalAddInfo += getIdxAddInfoWithExists("6", ordersObj.getAdd_inf6());
        //
        return !finalAddInfo.isEmpty() ? finalAddInfo.substring(0, finalAddInfo.length() - 1) : finalAddInfo;
    }

    /**
     * LUCHE - 13/07/2021
     * <p></p>
     * Metodo que avalia concatena o idx a info caso exista.
     *
     * @param idx
     * @param addInfo
     * @return
     */
    private String getIdxAddInfoWithExists(String idx, String addInfo) {
        if (addInfo != null && !addInfo.isEmpty()) {
            return idx + ": " + addInfo.trim() + "\n";
        }
        return "";
    }

    private void hideSerialForByPassProfile(MKEditTextNM mket_serial) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_BYPASS_SERIAL_VERIFICATION)) {
            mket_serial.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 16/01/2020
     * <p>
     * Metodo que define qual fluxo seguir apos a digitação do serial.
     * <p>
     * Se nenhum texto digitado, fecha o dialog
     * Se texto digitado diferente do serial, exibe tv com msg de erro
     * Se texto igual serial, verifica se o.s existe, e se existir, navega para act027.
     * Caso não exista, inicia a sequencia de download do serial e ana sequencia da o.s.
     * <p>
     * Dialog pode ser nulo e quando nulo, identifica que a chamada do metodo foi disparada pelos
     * leitores de Barcode ou OCR
     *
     * @param dialog      - Instancia do dialog para fecha-lo caso o campos serial seja vazio.
     * @param tv_error    - TextView que exibe o msg de erro caso o serial digitado seja diferente
     * @param mket_serial - Mket do serial
     * @param item        - Item da lista
     */
    private void checkDialogFlow(@Nullable AlertDialog dialog, TextView tv_error, MKEditTextNM mket_serial, SO_Next_Orders_Obj item) {
        String mketVal = mket_serial.getText().toString().trim();
        //LUCHE - 17/03/2021 - Aplicado profile que pula necessidade de digitação do serial.
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_BYPASS_SERIAL_VERIFICATION)) {
            callSoAction(item);
        } else {
            if (mketVal.length() > 0) {
                if (mketVal.equalsIgnoreCase(item.getSerial_id())) {
                    callSoAction(item);
                } else {
                    if (dialog == null) {
                        mket_serial.getText().clear();
                    }
                    //
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setError(hmAux_Trans.get("serial_no_match_hint"));
                }
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }
    }

    /**
     * LUCHE - 17/03/2021
     * Metodo que define a ação para abertura da O.S, se avança ou faz download antes de avançar.
     *
     * @param item
     */
    private void callSoAction(SO_Next_Orders_Obj item) {
        if (mPresenter.checkSoExits(item.getSo_prefix(), item.getSo_code())) {
            callAct027(
                    mPresenter.getAct027Bundle(
                            item.getSo_prefix(),
                            item.getSo_code()
                    )
            );
        } else {
            wsTmpItem = item;
            mPresenter.executeSerialDownload(item.getProduct_id(), item.getSerial_id());
        }
    }

    /**
     * LUCHE - 16/01/2020
     * <p>
     * Metodo que configura as views relativas ao serial e seus listeners.
     * <p>
     * Configura os tipos de leitura do mket
     *
     * @param tv_error    - TextView que exibe o msg de erro caso o serial digitado seja diferente
     * @param mket_serial - Mket do serial
     * @param item        - Item da lista
     */
    private void configSerialViews(final TextView tv_error, final MKEditTextNM mket_serial, final SO_Next_Orders_Obj item) {
        mket_serial.setmBARCODE(
                ToolBox_Inf.profileExists(
                        context,
                        Constant.PROFILE_MENU_PROFILE,
                        Constant.PROFILE_MENU_PROFILE_SERIAL_BARCODE
                )
        );
        //
        mket_serial.setmOCR(ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_PROFILE,
                Constant.PROFILE_MENU_PROFILE_SERIAL_OCR_MOSOLF
        ));
        mket_serial.setmNFC(false);
        //
        mket_serial.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
            @Override
            public void reportTextBySpecialist(String s) {
                checkDialogFlow(null, tv_error, mket_serial, item);
            }
        });
        //
        mket_serial.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                tv_error.setVisibility(View.GONE);
            }
        });

        //
        controls_sta.add(mket_serial);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT047;
        mAct_Title = Constant.ACT047 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private int soPositionCardItemClicked;
    private void initActions() {
        lv_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SO_Next_Orders_Obj item = (SO_Next_Orders_Obj) parent.getItemAtPosition(position);
                //
                soPositionCardItemClicked = position;
                //
                showDetailsDialog(item);
            }
        });

        lv_services.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                mketFilter.setEnabled(scrollState == SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        mketFilter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(s.trim());
                    if (lv_services.getVisibility() == View.VISIBLE)
                        lv_services.setSelectionFromTop(0, 0);
                }
            }
        });
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_SO_Next_Orders.class.getName())) {
            mPresenter.processNextOrderList(hmAux.get(WS_SO_Next_Orders.SO_NEXT_SERVICES));
            disableProgressDialog();
        } else if (wsProcess.equals(WS_Serial_Search.class.getName())) {
            //Não fecha o dialog, pois o mesmo será usado na sequencia para o download a S.O
            //em caso de erro, dialog pé fechado pelo metodo  cleanWsTmpItem();
            //disableProgressDialog();
            mPresenter.extractSearchResult(mLink, wsTmpItem);
        } else if (wsProcess.equals(WS_SO_Search.class.getName())) {
            mPresenter.processSoDownloadResult(hmAux, wsTmpItem.getSo_prefix(), wsTmpItem.getSo_code());
            cleanWsTmpItem();
        } else if (wsProcess.equals(WS_PROCESS_SO_STATUS_CHANGE)) {
            wsProcess = "";
            disableProgressDialog();
            processReturnSoChangeStatus(hmAux);
        }
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct027(Bundle bundle) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        if (wsProcess.equals(WS_Serial_Search.class.getName())
                || wsProcess.equals(WS_SO_Search.class.getName())) {
            cleanWsTmpItem();
        } else if (wsProcess.equals(WS_PROCESS_SO_STATUS_CHANGE)) {
            disableProgressDialog();
        } else {
            disableProgressDialog();
            onBackPressed();
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        if (wsProcess.equals(WS_Serial_Search.class.getName())
                || wsProcess.equals(WS_SO_Search.class.getName())
        ) {
            cleanWsTmpItem();
        } else if (wsProcess.equals(WS_PROCESS_SO_STATUS_CHANGE)) {
            disableProgressDialog();
        } else {
            disableProgressDialog();
            onBackPressed();
        }
    }


    //TRATA MSG SESSION NOT FOUND
    @Override
    protected void processLogin() {
        super.processLogin();
        //
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        //
        ToolBox_Inf.executeLogoffAndUpdateSoftware(context);
    }

    //Metodo chamado ao finalizar o download da atualização.
    @Override
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
        bundle.putString(Constant.WS_LOGOUT_USER_CODE, String.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Con.cleanPreferences(context);

        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, Menu.NONE, "");

        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_baseline_filter_alt_24));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 1) {
            showFilterDialog();
        }

        return true;
    }

    private boolean getSwitchState() {
        return ToolBox_Con.getBooleanPreferencesByKey(context, Constant.ACT047_SWITCH_STATE, true);
    }

    private void setSwitchState(boolean isChecked) {
        ToolBox_Con.setBooleanPreference(context, Constant.ACT047_SWITCH_STATE, isChecked);
    }


    @SuppressLint("ResourceType")
    private void showFilterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        Act047FilterDialogBinding binding = Act047FilterDialogBinding.inflate(LayoutInflater.from(context));
        NextOsFilter filter = mPresenter.getCheckboxFromPreference();
        List<TypeStatusFilter> status = filter.getStatusFilter();
        List<TypeDeadlineFilter> deadlineFilter = filter.getDeadlineFilter();
        TypePriorityFilter priorityFilter = filter.getPriorityFilter();
        MD_Site_Zone zone = new MD_Site_ZoneDao(context).getZone();
        MD_Site site = new MD_SiteDao(context).getSite();
        MD_PartnerDao partnerDao = new MD_PartnerDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        ArrayList<HMAux> partnerList = (ArrayList<HMAux>) partnerDao.query_HM(
                new MD_Partner_Sql_SS(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );

        binding.filterDialogTvTitle.setText(hmAux_Trans.get("filter_dialog_ttl_lbl"));
        binding.filterDialogTvStatusLbl.setText(hmAux_Trans.get("filter_dialog_status_lbl"));
        binding.filterDialogTvDeadlineLbl.setText(hmAux_Trans.get("filter_dialog_deadline_lbl"));
        binding.checkBoxWithoutDeadline.setText(hmAux_Trans.get("filter_dialog_deadline_without_lbl"));
        binding.checkboxExpiredDeadline.setText(hmAux_Trans.get("filter_dialog_deadline_expired_lbl"));
        binding.checkBoxNotExpiredDeadline.setText(hmAux_Trans.get("filter_dialog_deadline_not_expired_lbl"));
        binding.filterDialogTvOrdersLbl.setText(hmAux_Trans.get("filter_dialog_orders_lbl"));
        binding.radioPriorityDeadline.setText(hmAux_Trans.get("filter_dialog_priority_deadline_lbl"));
        binding.radioPriorityDateCreated.setText(hmAux_Trans.get("filter_dialog_priority_date_created_lbl"));

        binding.filterDialogTvPriorityLbl.setText(hmAux_Trans.get("filter_dialog_priority_lbl"));
        binding.radiobuttonAllPriority.setText(hmAux_Trans.get("filter_dialog_priority_all_lbl"));
        if (site != null && !site.getSite_desc().isEmpty()) {
            binding.filterDialogTvDescLbl.setText(site.getSite_desc());
            binding.filterDialogTvDescLbl.setVisibility(View.VISIBLE);
        } else {
            binding.filterDialogTvDescLbl.setVisibility(View.GONE);
        }
        if (getSwitchState() && zone != null && !zone.getZone_desc().isEmpty()) {
            binding.filterDialogTvZoneLbl.setText(hmAux_Trans.get("filter_dialog_zone_lbl") + ": " + zone.getZone_desc());
            binding.filterDialogTvZoneLbl.setVisibility(View.VISIBLE);
        } else {
            binding.filterDialogTvZoneLbl.setVisibility(View.GONE);
        }

        if (partnerList.isEmpty()) {
            binding.filterDialogTvPartnerLbl.setVisibility(View.GONE);
        } else {
            binding.filterDialogTvPartnerLbl.setVisibility(View.VISIBLE);
            binding.filterDialogTvPartnerLbl.setText(hmAux_Trans.get("filter_dialog_partner_lbl") + ": " + TextUtils.join(", ", filter.getPartnerList(partnerList)));
        }
        binding.filterDialogOk.setText(hmAux_Trans.get("filter_dialog_ok_lbl"));
        binding.filterDialogOk.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.drawable.button_theme_primary));
        binding.filterDialogCancel.setText(hmAux_Trans.get("filter_dialog_cancel_lbl"));
        onChangeStyleStatusFilter(binding);
        for (TypeStatusFilter item : status) {
            switch (item) {
                case EDIT:
                    binding.checkboxEdit.setChecked(true);
                    break;
                case APPROVAL_QUALITY:
                    binding.checkboxApprovalQuality.setChecked(true);
                    break;
                case PENDING_AND_PROCESS:
                    binding.checkboxPendingAndProcess.setChecked(true);
                    break;
                case STOP:
                    binding.checkboxStop.setChecked(true);
                    break;
                case APPROVAL_FINAL:
                    binding.checkboxApprovalFinal.setChecked(true);
                    break;
                case BUDGET:
                    binding.checkboxBudget.setChecked(true);
                    break;
            }
        }

        for (TypeDeadlineFilter item : deadlineFilter) {
            switch (item) {
                case NOT_EXPIRED:
                    binding.checkBoxNotExpiredDeadline.setChecked(true);
                    break;
                case EXPIRED:
                    binding.checkboxExpiredDeadline.setChecked(true);
                    break;
                case WITHOUT:
                    binding.checkBoxWithoutDeadline.setChecked(true);
                    break;
            }
        }

        switch (priorityFilter) {
            case DEADLINE:
                binding.radioPriorityDeadline.setChecked(true);
                break;
            case DATE_CREATED:
                binding.radioPriorityDateCreated.setChecked(true);
                break;
        }

        for (int i = 0; i < mPresenter.getListSmPriority().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            SmPriority item = mPresenter.getListSmPriority().get(i);
            radioButton.setId(View.generateViewId());
            radioButton.setText(item.getPriority_desc());
            radioButton.setTextColor(Color.parseColor(item.getPriority_color()));
            if (item.getPriority_desc().equals(priorityTypeFiltered)) {
                radioButton.setChecked(true);
            }
            RadioGroup.LayoutParams rdioLayout = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            binding.radioPriority.addView(radioButton, rdioLayout);
        }

        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        binding.filterDialogOk.setOnClickListener(v -> {
            List<TypeStatusFilter> typeStatusFilters = processSaveStatusFilterDialog(binding);
            List<TypeDeadlineFilter> deadlineFilterList = processSaveDeadlineFilterDialog(binding);
            TypePriorityFilter typePriorityFilter = processSavePriorityFilterDialog(binding);
            filter.setStatusFilter(typeStatusFilters);
            filter.setDeadlineFilter(deadlineFilterList);
            filter.setPriorityFilter(typePriorityFilter);
            filter.setPriorityTypeFilter(priorityTypeFiltered);
            if (mPresenter.saveFilterDialog(filter, getSwitchState())) {
                if (lv_services.getVisibility() == View.VISIBLE) lv_services.setSelection(0);
                dialog.dismiss();
            }
        });

        binding.filterDialogCancel.setOnClickListener(v -> dialog.dismiss());

        binding.checkboxEdit.setOnClickListener(v -> checkStateFromButtonFilter(binding));
        binding.checkboxBudget.setOnClickListener(v -> checkStateFromButtonFilter(binding));
        binding.checkboxStop.setOnClickListener(vv -> checkStateFromButtonFilter(binding));
        binding.checkboxPendingAndProcess.setOnClickListener(v -> checkStateFromButtonFilter(binding));
        binding.checkboxApprovalQuality.setOnClickListener(v -> checkStateFromButtonFilter(binding));
        binding.checkboxApprovalFinal.setOnClickListener(v -> checkStateFromButtonFilter(binding));
        binding.checkBoxNotExpiredDeadline.setOnClickListener(v -> checkStateFromButtonFilter(binding));
        binding.checkboxExpiredDeadline.setOnClickListener(v -> checkStateFromButtonFilter(binding));
        binding.checkBoxWithoutDeadline.setOnClickListener(v -> checkStateFromButtonFilter(binding));

        binding.radioPriority.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton radioButton = radioGroup.findViewById(i);
            if (radioButton != null) {
                if (radioButton.getId() == binding.radiobuttonAllPriority.getId()) {
                    priorityTypeFiltered = "";
                    return;
                }
                priorityTypeFiltered = radioButton.getText().toString();

            } else {
                Toast.makeText(context, hmAux_Trans.get("filter_option_invalid"), Toast.LENGTH_LONG).show();
            }
        });


    }

    String priorityTypeFiltered = "";

    //Caso o filtro "Todos os filtros" esteja ativado, desativa os outros filtros
    //Caso esteja desativado ativa os outros filtros
    private void onChangeStateStatusFilter(Act047FilterDialogBinding binding, boolean checked) {
        CheckBox[] checkboxes = {
                binding.checkboxEdit,
                binding.checkboxBudget,
                binding.checkboxStop,
                binding.checkboxPendingAndProcess,
                binding.checkboxApprovalQuality,
                binding.checkboxApprovalFinal,
        };

        for (CheckBox item : checkboxes) {
            item.setEnabled(!checked);
            item.setChecked(checked);
        }

    }


    private void onChangeStyleStatusFilter(Act047FilterDialogBinding binding) {

        TypeStatusFilter[] filters = {
                TypeStatusFilter.EDIT,
                TypeStatusFilter.BUDGET,
                TypeStatusFilter.APPROVAL_QUALITY,
                TypeStatusFilter.PENDING_AND_PROCESS,
                TypeStatusFilter.STOP,
                TypeStatusFilter.APPROVAL_FINAL,
        };

        for (TypeStatusFilter filter : filters) {
            switch (filter) {
                case EDIT:
                    binding.checkboxEdit.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_EDIT));
                    binding.checkboxEdit.setTextColor(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context, ConstantBaseApp.SYS_STATUS_EDIT)));
                    break;
                case BUDGET:
                    binding.checkboxBudget.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_WAITING_BUDGET));
                    binding.checkboxBudget.setTextColor(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context, ConstantBaseApp.SYS_STATUS_WAITING_BUDGET)));
                    break;
                case APPROVAL_QUALITY:
                    binding.checkboxApprovalQuality.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_WAITING_QUALITY));
                    binding.checkboxApprovalQuality.setTextColor(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context, ConstantBaseApp.SYS_STATUS_WAITING_QUALITY)));
                    break;
                case PENDING_AND_PROCESS:
                    String pendingText = hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_PENDING);
                    String processText = hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_PROCESS);

                    SpannableString spannableString = new SpannableString(pendingText + " / " + processText);

                    ForegroundColorSpan pendingColorSpan = new ForegroundColorSpan(ToolBox_Inf.getStatusColorV2(context, ConstantBaseApp.SYS_STATUS_PENDING));
                    spannableString.setSpan(pendingColorSpan, 0, pendingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ForegroundColorSpan processColorSpan = new ForegroundColorSpan(ToolBox_Inf.getStatusColorV2(context, ConstantBaseApp.SYS_STATUS_PROCESS));
                    spannableString.setSpan(processColorSpan, pendingText.length() + 3, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    binding.checkboxPendingAndProcess.setText(spannableString);
                    break;
                case STOP:
                    binding.checkboxStop.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_STOP));
                    binding.checkboxStop.setTextColor(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context, ConstantBaseApp.SYS_STATUS_STOP)));
                    break;
                case APPROVAL_FINAL:
                    binding.checkboxApprovalFinal.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_WAITING_CLIENT));
                    binding.checkboxApprovalFinal.setTextColor(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context, ConstantBaseApp.SYS_STATUS_WAITING_CLIENT)));
                    break;
            }
        }
    }


    private TypePriorityFilter processSavePriorityFilterDialog(
            Act047FilterDialogBinding binding
    ) {

        if (binding.radioPriorityDeadline.isChecked()) {
            return TypePriorityFilter.DEADLINE;
        }
        if (binding.radioPriorityDateCreated.isChecked()) {
            return TypePriorityFilter.DATE_CREATED;
        }
        return TypePriorityFilter.DEADLINE;
    }

    private List<TypeDeadlineFilter> processSaveDeadlineFilterDialog(
            Act047FilterDialogBinding binding
    ) {
        List<TypeDeadlineFilter> deadlineList = new ArrayList<>();

        CheckBox[] checkBoxes = {
                binding.checkBoxNotExpiredDeadline,
                binding.checkboxExpiredDeadline,
                binding.checkBoxWithoutDeadline,
        };

        TypeDeadlineFilter[] filters = {
                TypeDeadlineFilter.NOT_EXPIRED,
                TypeDeadlineFilter.EXPIRED,
                TypeDeadlineFilter.WITHOUT
        };

        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                deadlineList.add(filters[i]);
            }
        }

        return deadlineList;
    }

    private void checkStateFromButtonFilter(Act047FilterDialogBinding binding) {
        CheckBox[] checkboxesStatus = {
                binding.checkboxEdit,
                binding.checkboxBudget,
                binding.checkboxStop,
                binding.checkboxPendingAndProcess,
                binding.checkboxApprovalQuality,
                binding.checkboxApprovalFinal,
        };

        CheckBox[] checkboxesDeadline = {
                binding.checkBoxNotExpiredDeadline,
                binding.checkboxExpiredDeadline,
                binding.checkBoxWithoutDeadline,
        };


        boolean allCheckboxesUncheckedStatus = true;
        for (CheckBox checkbox : checkboxesStatus) {
            if (checkbox.isChecked()) {
                allCheckboxesUncheckedStatus = false;
                break;
            }
        }

        boolean allCheckboxesUncheckedDeadline = true;
        for (CheckBox checkbox : checkboxesDeadline) {
            if (checkbox.isChecked()) {
                allCheckboxesUncheckedDeadline = false;
                break;
            }
        }

        binding.filterDialogOk.setEnabled(!allCheckboxesUncheckedStatus && !allCheckboxesUncheckedDeadline);

    }

    private List<TypeStatusFilter> processSaveStatusFilterDialog(Act047FilterDialogBinding binding) {
        List<TypeStatusFilter> statusList = new ArrayList<>();

        //Tem que está ordenado como no layout
        CheckBox[] checkboxes = {
                binding.checkboxEdit,
                binding.checkboxBudget,
                binding.checkboxStop,
                binding.checkboxPendingAndProcess,
                binding.checkboxApprovalQuality,
                binding.checkboxApprovalFinal,
        };

        //Tem que está ordenado como no layout
        TypeStatusFilter[] filters = {
                TypeStatusFilter.EDIT,
                TypeStatusFilter.BUDGET,
                TypeStatusFilter.STOP,
                TypeStatusFilter.PENDING_AND_PROCESS,
                TypeStatusFilter.APPROVAL_QUALITY,
                TypeStatusFilter.APPROVAL_FINAL,
        };


        for (int i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].isChecked()) {
                statusList.add(filters[i]);
            }
        }

        return statusList;
    }

}

