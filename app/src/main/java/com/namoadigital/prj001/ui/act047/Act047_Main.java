package com.namoadigital.prj001.ui.act047;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act047_SO_Next_Orders_Adapter;
import com.namoadigital.prj001.databinding.Act047SoNextOrdersDialogBinding;
import com.namoadigital.prj001.model.SO_Next_Orders_Obj;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_SO_Next_Orders;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act047_Main extends Base_Activity implements Act047_Main_Contract.I_View {

    private TextView tv_site;
    private TextView tv_qty;
    private TextView tv_zone;
    private ListView lv_services;
    private Act047_SO_Next_Orders_Adapter mAdapter;
    private String requestingAct = "";
    private Act047_Main_Contract.I_Presenter mPresenter;
    private String wsProcess ="";
    //Var tmp que armazena o item da lista clicado.
    private SO_Next_Orders_Obj wsTmpItem = null;
    private MKEditTextNM mketFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act047_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        transList.add("alert_empty_order_list_ttl");
        transList.add("alert_empty_order_list_msg");
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
        tv_site = (TextView) findViewById(R.id.act047_tv_site_val);
        //
        tv_qty = (TextView) findViewById(R.id.act047_tv_qty_orders);
        //
        tv_zone = (TextView) findViewById(R.id.act047_tv_zone_val);
        //
        mketFilter = findViewById(R.id.act047_mket_filter);
        //
        lv_services = (ListView) findViewById(R.id.act047_lv_services);
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
        mPresenter.executeNextOrdersSearch();
    }

    private void setLabels() {
        mketFilter.setHint(hmAux_Trans.get("filter_hint"));
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
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
     *
     * Metodo que reseta as variaveis relativas a chamada de WS e fecha o dialog.
     */
    @Override
    public void cleanWsTmpItem() {
       wsProcess ="";
       wsTmpItem = null;
       disableProgressDialog();
    }

    private void setLocationInfo() {
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        //
        tv_site.setText(mFooter.get(Constant.FOOTER_SITE));
        //
        tv_zone.setVisibility(View.GONE);
        if( ToolBox_Inf.profileExists(context,Constant.PROFILE_PRJ001_SO,null)
            && mFooter.containsKey(Constant.FOOTER_ZONE)
            && !mFooter.get(Constant.FOOTER_ZONE).isEmpty()
        ) {
            tv_zone.setVisibility(View.VISIBLE);
            tv_zone.setText(mFooter.get(Constant.FOOTER_ZONE));
        }
        //
        tv_qty.setText(hmAux_Trans.get("qty_lbl") + " 0");
    }

    @Override
    public void loadNextOrders(ArrayList<SO_Next_Orders_Obj> nextOrdersObjs) {
        //
        tv_qty.setText(hmAux_Trans.get("qty_lbl") + " "+ (nextOrdersObjs == null ? "0": nextOrdersObjs.size()));
        //
        mAdapter = new Act047_SO_Next_Orders_Adapter(
                context,
                nextOrdersObjs,
                R.layout.act047_cell
        );
        //
        lv_services.setAdapter(mAdapter);
    }

    @Override
    public void showAlert(String ttl, String msg) {
        showAlert(ttl, msg,null);
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
     *
     * Alterado metodo para verificar se o progressDialog ja esta instanciado e, caso esteja, atualiza
     * title, msg e exibe o dialog ao inves de criar uma nova instancia.
     *
     * Teste feito para tentar resolver problemas que acontecem em algumas telas que tem chamadas de
     * ws encadeadas. Como cada chamada do enableProgressDialog, gera uma nova instancia do progressDialog,
     * era possivel empilhar dialogs e não conseguir fechar los ja que o se houvessem 2 aberto,
     * não existe mais referencia do primeiro tornando impossivel fechar o dialog.     *
     *
     * O teste mostrou ser efetivo e talvez fosse interessando aplicar esse conceito direto na BaseACt
     *
     * @param title - Titulo
     * @param msg - Msg
     */
    @Override
    public void showPD(String title, String msg) {
        if(progressDialog == null) {
            enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
            );
        }else{
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
            //
            if(!progressDialog.isShowing()) {
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
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                },
                0
        );
    }

    @Override
    public void showEmptyLogMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_empty_order_list_ttl"),
                hmAux_Trans.get("alert_empty_order_list_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                },
                0
        );
    }

    private void showDetailsDialog(final SO_Next_Orders_Obj item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        Act047SoNextOrdersDialogBinding binding = Act047SoNextOrdersDialogBinding.inflate(LayoutInflater.from(context));
        //
        binding.act047SoNextOrdersDialogTvTitle.setText((hmAux_Trans.get("dialog_so_details_ttl")+" "+ item.getSo_prefix()+"."+item.getSo_code()));

        if (item.getSo_desc()==null || item.getSo_desc().isEmpty()) {
            binding.act047SoNextOrdersDialogLlSoDesc.setVisibility(View.GONE);
        }else{
            binding.act047SoNextOrdersDialogLlSoDesc.setVisibility(View.VISIBLE);
            binding.act047SoNextOrdersDialogTvSoDescLbl.setText(hmAux_Trans.get("dialog_so_desc_lbl"));
            binding.act047SoNextOrdersDialogTvSoDescVal.setText(item.getSo_desc());
        }
         if (item.getComments()==null || item.getComments().isEmpty()) {
             binding.act047SoNextOrdersDialogLlSoComment.setVisibility(View.GONE);
        }else{
            binding.act047SoNextOrdersDialogLlSoComment.setVisibility(View.VISIBLE);
            binding.act047SoNextOrdersDialogTvSoCommentLbl.setText(hmAux_Trans.get("dialog_so_comment_lbl"));
            binding.act047SoNextOrdersDialogTvSoCommentVal.setText(item.getComments());
        }

        binding.act047SoNextOrdersDialogTvServicesLbl.setText(hmAux_Trans.get("dialog_services_lbl"));
        binding.act047SoNextOrdersDialogTvServicesVal.setText(item.getService());

        binding.act047SoNextOrdersDialogMketSerialConfirm.setHint(hmAux_Trans.get("serial_hint"));
        binding.act047SoNextOrdersDialogTvError.setText(hmAux_Trans.get("serial_no_match_hint"));

        binding.act047SoNextOrdersDialogTvPositionLbl.setText(hmAux_Trans.get("dialog_so_serial_position_lbl"));
        if(item.getSerial_site_code() != null) {
            binding.act047SoNextOrdersDialogLlSoPosition.setVisibility(View.VISIBLE);
            if (item.getSerial_site_code().equals(ToolBox_Con.getPreference_Site_Code(context))) {
                binding.act047SoNextOrdersDialogLlSoPositionSite.setVisibility(View.GONE);
                binding.act047SoNextOrdersDialogTvSoPositionSiteVal.setTextColor(getResources().getColor(R.color.font_normal));
                binding.act047SoNextOrdersDialogTvSoPositionZoneVal.setTextColor(getResources().getColor(R.color.font_normal));
                binding.act047SoNextOrdersDialogTvSoPositionLocalVal.setTextColor(getResources().getColor(R.color.font_normal));
            } else {
                binding.act047SoNextOrdersDialogLlSoPositionSite.setVisibility(View.VISIBLE);
                binding.act047SoNextOrdersDialogTvSoPositionSiteVal.setTextColor(getResources().getColor(R.color.namoa_status_error));
                binding.act047SoNextOrdersDialogTvSoPositionZoneVal.setTextColor(getResources().getColor(R.color.namoa_status_error));
                binding.act047SoNextOrdersDialogTvSoPositionLocalVal.setTextColor(getResources().getColor(R.color.namoa_status_error));
            }
        }else{
            binding.act047SoNextOrdersDialogLlSoPosition.setVisibility(View.GONE);
            binding.act047SoNextOrdersDialogLlSoPositionSite.setVisibility(View.GONE);
        }

        binding.act047SoNextOrdersDialogTvSoPositionSiteLbl.setText(hmAux_Trans.get("dialog_so_site_lbl"));
        binding.act047SoNextOrdersDialogTvSoPositionSiteVal.setText(item.getSerial_site_desc());
        binding.act047SoNextOrdersDialogTvSoPositionZoneLbl.setText(hmAux_Trans.get("dialog_so_zone_lbl"));
        binding.act047SoNextOrdersDialogTvSoPositionZoneVal.setText(item.getSerial_zone_desc());
        binding.act047SoNextOrdersDialogTvSoPositionLocalLbl.setText(hmAux_Trans.get("dialog_so_local_lbl"));
        binding.act047SoNextOrdersDialogTvSoPositionLocalVal.setText(item.getSerial_local_desc());

        if (item.getLast_approval_budget_user()==null
                || item.getLast_approval_budget_user().isEmpty()) {
            binding.act047SoNextOrdersDialogLlSoApprovedBy.setVisibility(View.GONE);
        }else{
            binding.act047SoNextOrdersDialogLlSoApprovedBy.setVisibility(View.VISIBLE);
            binding.act047SoNextOrdersDialogTvSoApprovedByLbl.setText(hmAux_Trans.get("dialog_so_approved_by_lbl"));
            binding.act047SoNextOrdersDialogTvSoApprovedByVal.setText(item.getLast_approval_budget_user());
        }

        if (item.getCreate_user()==null
        || item.getCreate_user().isEmpty()) {
            binding.act047SoNextOrdersDialogLlSoCreatedBy.setVisibility(View.GONE);
        }else{
            binding.act047SoNextOrdersDialogLlSoCreatedBy.setVisibility(View.VISIBLE);
            binding.act047SoNextOrdersDialogTvSoCreatedByLbl.setText(hmAux_Trans.get("dialog_so_created_by_lbl"));
            binding.act047SoNextOrdersDialogTvSoCreatedByVal.setText(item.getCreate_user());
        }
        //LUCHE - 13/07/2021 - Add infos add da o.s
        String formatedSoAddInfo = getFormatedSoAddInfo(item);
        if(!formatedSoAddInfo.isEmpty()){
            binding.act047SoNextOrdersDialogLlSoAddInfo.setVisibility(View.VISIBLE);
            binding.act047SoNextOrdersDialogTvSoAddInfoLbl.setText(hmAux_Trans.get("dialog_so_add_info_lbl"));
            binding.act047SoNextOrdersDialogTvSoAddInfoVal.setText(formatedSoAddInfo);
        }else{
            binding.act047SoNextOrdersDialogLlSoAddInfo.setVisibility(View.GONE);
        }

        //Config TIl e Mket do seria
        configSerialViews(binding.act047SoNextOrdersDialogTvError,binding.act047SoNextOrdersDialogMketSerialConfirm,item);
        hideSerialForByPassProfile(binding.act047SoNextOrdersDialogMketSerialConfirm);
        //
        builder
                .setView(binding.getRoot())
                .setPositiveButton(
                    hmAux_Trans.get("sys_alert_btn_ok"),
                    null
                )
                .setNegativeButton(
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    null
                );
        //
        final AlertDialog dialog =  builder.create();
        dialog.show();
        //Setado listner do botão positivo nesse momento, pois era necessaria a passagem do dialog
        //como parametro do metodo checkDialogFlow
        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDialogFlow(dialog,binding.act047SoNextOrdersDialogTvError,binding.act047SoNextOrdersDialogMketSerialConfirm,item);
            }
        });
        //Listener para remover o mket_serial da lista de componentes
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                controls_sta.remove(binding.act047SoNextOrdersDialogMketSerialConfirm);
            }
        });
    }

    /**
     * LUCHE - 13/07/2021
     * <p></p>
     * Metodo que formata a exibição das 6 infos add da o.s
     * A exibição deve seguir, caso exista:
     *  idx : Valor \n
     * @param ordersObj
     * @return
     */
    private String getFormatedSoAddInfo(SO_Next_Orders_Obj ordersObj) {
        String finalAddInfo = "";
        //
        finalAddInfo += getIdxAddInfoWithExists("1",ordersObj.getAdd_inf1());
        finalAddInfo += getIdxAddInfoWithExists("2",ordersObj.getAdd_inf2());
        finalAddInfo += getIdxAddInfoWithExists("3",ordersObj.getAdd_inf3());
        finalAddInfo += getIdxAddInfoWithExists("4",ordersObj.getAdd_inf4());
        finalAddInfo += getIdxAddInfoWithExists("5",ordersObj.getAdd_inf5());
        finalAddInfo += getIdxAddInfoWithExists("6",ordersObj.getAdd_inf6());
        //
        return !finalAddInfo.isEmpty() ? finalAddInfo.substring(0,finalAddInfo.length() -1) : finalAddInfo ;
    }

    /**
     * LUCHE - 13/07/2021
     * <p></p>
     * Metodo que avalia concatena o idx a info caso exista.
     * @param idx
     * @param addInfo
     * @return
     */
    private String getIdxAddInfoWithExists(String idx, String addInfo) {
        if(addInfo != null && !addInfo.isEmpty()){
            return idx + ": " + addInfo.trim()+"\n";
        }
        return "";
    }

    private void hideSerialForByPassProfile(MKEditTextNM mket_serial) {
        if(ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO,Constant.PROFILE_MENU_SO_PARAM_BYPASS_SERIAL_VERIFICATION)){
            mket_serial.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Metodo que define qual fluxo seguir apos a digitação do serial.
     *
     * Se nenhum texto digitado, fecha o dialog
     * Se texto digitado diferente do serial, exibe tv com msg de erro
     * Se texto igual serial, verifica se o.s existe, e se existir, navega para act027.
     * Caso não exista, inicia a sequencia de download do serial e ana sequencia da o.s.
     *
     * Dialog pode ser nulo e quando nulo, identifica que a chamada do metodo foi disparada pelos
     * leitores de Barcode ou OCR
     *
     * @param dialog - Instancia do dialog para fecha-lo caso o campos serial seja vazio.
     * @param tv_error - TextView que exibe o msg de erro caso o serial digitado seja diferente
     * @param mket_serial - Mket do serial
     * @param item - Item da lista
     */
    private void checkDialogFlow(@Nullable AlertDialog dialog, TextView tv_error, MKEditTextNM mket_serial, SO_Next_Orders_Obj item) {
        String mketVal = mket_serial.getText().toString().trim();
        //LUCHE - 17/03/2021 - Aplicado profile que pula necessidade de digitação do serial.
        if(ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO,Constant.PROFILE_MENU_SO_PARAM_BYPASS_SERIAL_VERIFICATION)){
            callSoAction(item);
        }else {
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
     * @param item
     */
    private void callSoAction(SO_Next_Orders_Obj item) {
        if(mPresenter.checkSoExits(item.getSo_prefix(),item.getSo_code())){
            callAct027(
                mPresenter.getAct027Bundle(
                    item.getSo_prefix(),
                    item.getSo_code()
                )
            );
        }else {
            wsTmpItem = item;
            mPresenter.executeSerialDownload(item.getProduct_id(), item.getSerial_id());
        }
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Metodo que configura as views relativas ao serial e seus listeners.
     *
     * Configura os tipos de leitura do mket
     *
     * @param tv_error - TextView que exibe o msg de erro caso o serial digitado seja diferente
     * @param mket_serial - Mket do serial
     * @param item - Item da lista
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
                checkDialogFlow(null, tv_error, mket_serial,item);
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

    private void initActions() {
        lv_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SO_Next_Orders_Obj item = (SO_Next_Orders_Obj) parent.getItemAtPosition(position);
                //
                showDetailsDialog(item);
            }
        });

        lv_services.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    mketFilter.setEnabled(true);
                } else {
                    mketFilter.setEnabled(false);
                }
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
                if(mAdapter != null) {
                    mAdapter.getFilter().filter(s.trim());
                }
            }
        });
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
       processCloseACT(mLink, mRequired,new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_SO_Next_Orders.class.getName())) {
            mPresenter.processNextOrderList(hmAux.get(WS_SO_Next_Orders.SO_NEXT_SERVICES));
            disableProgressDialog();
        }else if(wsProcess.equals(WS_Serial_Search.class.getName())){
            //Não fecha o dialog, pois o mesmo será usado na sequencia para o download a S.O
            //em caso de erro, dialog pé fechado pelo metodo  cleanWsTmpItem();
            //disableProgressDialog();
            mPresenter.extractSearchResult(mLink, wsTmpItem);
        }else if(wsProcess.equals(WS_SO_Search.class.getName())){
            mPresenter.processSoDownloadResult(hmAux,wsTmpItem.getSo_prefix(),wsTmpItem.getSo_code());
            cleanWsTmpItem();
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
        if( wsProcess.equals(WS_Serial_Search.class.getName())
            || wsProcess.equals(WS_SO_Search.class.getName()))
        {
            cleanWsTmpItem();
        }else{
            disableProgressDialog();
            onBackPressed();
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        if( wsProcess.equals(WS_Serial_Search.class.getName())
            || wsProcess.equals(WS_SO_Search.class.getName())
        ){
            cleanWsTmpItem();
        }else{
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

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
