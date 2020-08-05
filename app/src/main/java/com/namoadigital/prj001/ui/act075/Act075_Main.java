package com.namoadigital.prj001.ui.act075;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.ctls.FabMenuItem;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act075_Product_List_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.List;

public class Act075_Main extends Base_Activity_Frag implements Act075_Main_Contract.I_View, Act075_Product_List_Adapter.OnProductInteract {
    public static final String PRODUCT_LIST = "PRODUCT_LIST";
    public static final String IS_ADD_PRODUCT_LIST = "IS_ADD_PRODUCT_LIST";
    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private String mResource_CodeFrg = "0";
    private HMAux hmAux_Trans_frg_pipeline_header;
    public static final String VIEW_PROFILE  = "VIEW_PROFILE";
    public static final int PRODUCT_VIEW_ID  = 1;
    public static final int APPROVAL_VIEW_ID = 2;
    /*
        BARRIONUEVO - 22.07.2020
        act_profile - 1 Produto
                      2 Aprovacao
     */
    private TK_Ticket tkTicket;
    private int act_profile;
    RecyclerView rvProduct;
    Button btnSave;
    FabMenu fabMenu;
    FabMenuItem fabStep;
    FabMenuItem fabProduct;
    Act075_Main_Presenter mPresenter;
    private ArrayList<FabMenuItem> fabMenuItems = new ArrayList<>();
    private ArrayList<TK_Ticket_Product> tk_ticket_products = new ArrayList<>();
    private Act075_Product_List_Adapter mAdapter;
    private int mTkPrefix;
    private int mTkCode;
    private boolean hasFABActive = false;
    private boolean hasUpdated = false;
    private String wsProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act075_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        rvProduct = findViewById(R.id.act075_rv_product);
        btnSave = findViewById(R.id.act075_save_product);
        fabMenu = (FabMenu) findViewById(R.id.act075_fabMenu_anchor);
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
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT075
        );
        //
        loadTranslation();
        loadTranslationFrg_Pipeline_Header();

    }

    private void loadTranslationFrg_Pipeline_Header() {

    }

    private void initVars() {
        mPresenter = new Act075_Main_Presenter(context, this, hmAux_Trans);
        recoverIntentsInfo();
        //
        btnSave.setText(hmAux_Trans.get("save_product_lbl"));
        //
        tkTicket = mPresenter.getTicket(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix,mTkCode);
        tk_ticket_products = tkTicket.getProduct();
        //
        setProductList();
        //
        if(act_profile == 1) {
            //
            if(tkTicket.getUpdate_required_product() == 1
            || !hasUpdate()) {
                btnSave.setEnabled(false);
            }else{
                btnSave.setEnabled(true);
            }
            setProductHeaderFragment();
        }else{
            fabMenu.setVisibility(View.GONE);
            setApprovalHeaderFragment();
        }
        //
        initFabMenuItens();
    }

    private void setApprovalHeaderFragment() {

    }

    private void setProductList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvProduct.setLayoutManager(layoutManager);
        //
        mAdapter = new Act075_Product_List_Adapter(
                context,
                hmAux_Trans,
                tk_ticket_products,
                act_profile,
                tkTicket.getInventory_control(),
                tkTicket.getTicket_status().equalsIgnoreCase(Constant.SYS_STATUS_PENDING)
                ||tkTicket.getTicket_status().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS),
                mPresenter.getWithdrawStatus(tkTicket),
                mPresenter.getAppliedStatus(tkTicket),
                this
        );
        //
        rvProduct.setAdapter(mAdapter);
    }

    private void setProductHeaderFragment() {
        fm = getSupportFragmentManager();
        //
        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForProduct(
                tkTicket.getTicket_id(),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(tkTicket.getOpen_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_site_code(),
                tkTicket.getOpen_site_desc(),
                tkTicket.getOpen_serial_id(),
                tkTicket.getOpen_product_desc(),
                tkTicket.getOrigin_desc()
        );
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.header_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            act_profile = bundle.getInt(VIEW_PROFILE, -1);
            mTkPrefix = bundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = bundle.getInt(TK_TicketDao.TICKET_CODE, -1);
        } else {
            act_profile = -1;
        }
    }

    private void initFabMenuItens() {
        int lblBgColor = getResources().getColor(R.color.namoa_pipeline_background_icon);
        int lblColor = getResources().getColor(R.color.padrao_WHITE);
        int btnBgColor = getResources().getColor(R.color.namoa_sync_pipeline_background_btn);
        int iconColor = getResources().getColor(R.color.colorPrimary);
        //atalho para step
        fabStep = new FabMenuItem(context);
        fabStep.setTag("to_step_lbl");
        fabStep.setmLabel(hmAux_Trans.get("to_step_lbl"));
        fabStep.setmLabel_Back_Color(lblBgColor);
        fabStep.setmLabel_Text_Color(lblColor);
        fabStep.setmButton_Back_Color(btnBgColor);
        fabStep.setmButton_Resource_Color(iconColor);
        fabStep.setmButton_Resource(R.drawable.ic_baseline_assignment_24);
        fabMenuItems.add(fabStep);
        //atalaho para produto.
        fabProduct = new FabMenuItem(context);
        fabProduct.setTag("to_product_lbl");
        fabProduct.setmLabel(hmAux_Trans.get("to_product_lbl"));
        fabProduct.setmLabel_Back_Color(lblBgColor);
        fabProduct.setmLabel_Text_Color(lblColor);
        fabProduct.setmButton_Back_Color(btnBgColor);
        fabProduct.setmButton_Resource_Color(iconColor);
        fabProduct.setmButton_Resource(R.drawable.ic_baseline_build_24);
        fabMenuItems.add(fabProduct);
        fabMenu.setFabMenuItens(fabMenuItems);
        //
        //Seta tradução e itens no FabMenu
        for (FabMenuItem item : fabMenuItems) {
            if (item != null && hmAux_Trans.get((String) item.getTag()) != null) {
                item.setmLabel(hmAux_Trans.get((String) item.getTag()));
            } else {
                item.setmLabel(ToolBox.setNoTrans(mModule_Code, mResource_Code, (String) item.getTag()));
            }
        }
        //
        fabMenu.setFabMenuItens(fabMenuItems);
        fabMenu.setmIcons_Enabled(true);
    }


    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT075;
        mAct_Title = Constant.ACT075 + "_" + "title";
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
        ToolBox_Inf.buildFooterDialog(context, false);
    }

    private void initActions() {
        fabMenu.setOnFabClickListener(new FabMenu.IFabMenu() {
            @Override
            public void onFabClick(View view) {
                int id = view.getId();

                if ((id == fabProduct.getId())) {


                }else if (id == fabStep.getId()){
                    verifyChangesBeforeExit();
                }
            }

            @Override
            public void onFabStatusChanged(boolean b) {
                hasFABActive = b;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolBox_Con.isOnline(context)) {
                    mPresenter.saveproduct(tkTicket.getScn(), (ArrayList<TK_Ticket_Product>) mAdapter.getmValues());
                }else{
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }
        });
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(ttl,
                msg,
                context.getString(R.string.generic_msg_cancel),
                context.getString(R.string.generic_msg_ok));
    }

    private void verifyChangesBeforeExit() {
        if(hasUpdate()) {
            ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans.get("exit_without_save_ttl"),
                    hmAux_Trans.get("exit_without_save_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callAct070();
                        }
                    },
                    0
            );
        }else{
            callAct070();
        }
    }

    private void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX,tkTicket.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE,tkTicket.getTicket_code());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act075_title");
        transList.add("save_product_lbl");
        transList.add("set_product_qty_lbl");
        transList.add("set_product_qty_used_lbl");
        transList.add("exit_without_save_ttl");
        transList.add("exit_without_save_msg");
        transList.add("to_product_lbl");
        transList.add("to_step_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public void onAddProduct() {
        callAct_Product_Selection(context, tk_ticket_products);
    }

    @Override
    public void callHasChanges(boolean b) {
        updateSaveBUtton(hasUpdate());
    }

    @Override
    public void callQtyDialog(final int position, TK_Ticket_Product tk_ticket_product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mDialogVIew = inflater.inflate(R.layout.act075_set_qties_dialog, null);
        TextView dialog_set_qty_lbl =  mDialogVIew.findViewById(R.id.act075_dialog_set_qty_lbl);
        final MKEditTextNM dialog_set_mkedt_qty =  mDialogVIew.findViewById(R.id.act075_dialog_set_mkedt_qty);
        dialog_set_mkedt_qty.setmInputType("NUMBER");
        dialog_set_mkedt_qty.setText(String.format("%f", tk_ticket_product.getQty()));
        dialog_set_qty_lbl.setText(hmAux_Trans.get("set_product_qty_lbl"));

        builder.setView(mDialogVIew)
                // Add action buttons
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        tk_ticket_products.get(position).setQty(Double.valueOf(dialog_set_mkedt_qty.getText().toString()));
                        mAdapter.notifyItemChanged(position);
                    }
                })
                .setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }

    @Override
    public void callQtyUsedDialog(final int position, TK_Ticket_Product tk_ticket_product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mDialogVIew = inflater.inflate(R.layout.act075_set_qties_dialog, null);
        TextView dialog_set_qty_lbl =  mDialogVIew.findViewById(R.id.act075_dialog_set_qty_lbl);
        final MKEditTextNM dialog_set_mkedt_qty =  mDialogVIew.findViewById(R.id.act075_dialog_set_mkedt_qty);
        dialog_set_mkedt_qty.setmInputType("NUMBER");
        dialog_set_mkedt_qty.setText(String.format("%f", tk_ticket_product.getQty_used()));
        dialog_set_qty_lbl.setText(hmAux_Trans.get("set_product_qty_used_lbl"));

        builder.setView(mDialogVIew)
                // Add action buttons
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        tk_ticket_products.get(position).setQty_used(Double.valueOf(dialog_set_mkedt_qty.getText().toString()));
                        mAdapter.notifyItemChanged(position);
                    }
                })
                .setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }


    public void callAct_Product_Selection(Context context, ArrayList<TK_Ticket_Product> tk_ticket_products) {
        Intent mIntent = new Intent(context, Act_Product_Selection.class);
        //
        Bundle bundle = new Bundle();
        //
        bundle.putBoolean(IS_ADD_PRODUCT_LIST, true);
        bundle.putSerializable(PRODUCT_LIST, tk_ticket_products);
        mIntent.putExtras(bundle);
        //
        startActivityForResult(mIntent, 20);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 20:
                processResult(resultCode, data);
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processResult(int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            updateSaveBUtton(true);
            MD_Product pAux = (MD_Product) data.getSerializableExtra(MD_Product.class.getName());
            mAdapter.getmValues().add(
                    new TK_Ticket_Product(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            tkTicket.getTicket_prefix(),
                            tkTicket.getTicket_code(),
                            (int) pAux.getProduct_code(),
                            pAux.getProduct_id(),
                            pAux.getProduct_desc(),
                            pAux.getUn(),
                            1.0,
                            1.0,
                            "pickup_status",
                            0.0
                    )
            );
            mAdapter.notifyDataSetChanged();
        } else {
        }
    }

    @Override
    public void onBackPressed() {
        if(hasFABActive){
            fabMenu.animateFAB();
        }else {
            verifyChangesBeforeExit();
        }
    }

    @Override
    public void showMsg(String ttl, String msg) {
        ToolBox.alertMSG(context, ttl, msg, null, 0);
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess=wsProcess;
    }

    @Override
    public void resetHasUpdate() {
        updateSaveBUtton(false);
    }

    private void updateSaveBUtton(boolean b) {
        hasUpdated = b;
        btnSave.setEnabled(hasUpdated);
    }

    public boolean hasUpdate(){
        List<TK_Ticket_Product> products = mAdapter.getmValues();
        if (tk_ticket_products.equals(products)) {
            return false;
        }else {
            return true;
        }
    }
}