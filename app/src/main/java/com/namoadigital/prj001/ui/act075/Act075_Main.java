package com.namoadigital.prj001.ui.act075;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.List;

public class Act075_Main extends Base_Activity_Frag implements Act075_Main_Contract.I_View, Act075_Product_List_Adapter.OnProductInteract, Frg_Pipeline_Header.OnPipelineFragmentInteractionListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act075_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
//        rvProduct = findViewById(R.id.act075_rv_product);
        btnSave = findViewById(R.id.act075_save_product);
        fabMenu = (FabMenu) findViewById(R.id.act075_fabMenu_anchor);
        fabMenu.setmIcons_Enabled(false);
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
        //
        mResource_CodeFrg = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Pipeline_Header();

    }

    private void loadTranslationFrg_Pipeline_Header() {

    }

    private void initVars() {
        mPresenter = new Act075_Main_Presenter(context, this, hmAux_Trans);
        recoverIntentsInfo();
        //
        if(act_profile == 1) {
            setProductHeaderFragment();
        }else{
            setApprovalHeaderFragment();
        }

        //
        initFabMenuItens();
        //
        setProductList();
    }

    private void setApprovalHeaderFragment() {

    }

    private void setProductList() {

    }

    private void setProductHeaderFragment() {
        fm = getSupportFragmentManager();
        //
        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForProduct(
                "mTicket_id",
                "status",
                "prod_desc",
                "mTicket_date",
                "site_desc",
                "serial_id"
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
            tk_ticket_products = (ArrayList<TK_Ticket_Product>) bundle.getSerializable("TK_Ticket_Product");
        } else {
            act_profile = -1;
            tk_ticket_products = new ArrayList<>();
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
                    ToolBox.alertMSG(
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
                }
            }

            @Override
            public void onFabStatusChanged(boolean b) {

            }
        });


    }

    private void callAct070() {

    }
    

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act075_title");
        transList.add("withdrawn_lbl");
        transList.add("taken_lbl");
        transList.add("applied_lbl");
        transList.add("btn_save");
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
        //call Product Selection
    }

    @Override
    public void callQtyDialog(final int position, TK_Ticket_Product tk_ticket_product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mDialogVIew = inflater.inflate(R.layout.act075_set_qties_dialog, null);
        TextView dialog_set_qty_lbl =  mDialogVIew.findViewById(R.id.act075_dialog_set_qty_lbl);
        final MKEditTextNM dialog_set_mkedt_qty =  mDialogVIew.findViewById(R.id.act075_dialog_set_mkedt_qty);
        dialog_set_mkedt_qty.setText(String.format("%d", tk_ticket_product.getQty()));
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
    }

    @Override
    public void callQtyUsedDialog(final int position, TK_Ticket_Product tk_ticket_product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mDialogVIew = inflater.inflate(R.layout.act075_set_qties_dialog, null);
        TextView dialog_set_qty_lbl =  mDialogVIew.findViewById(R.id.act075_dialog_set_qty_lbl);
        final MKEditTextNM dialog_set_mkedt_qty =  mDialogVIew.findViewById(R.id.act075_dialog_set_mkedt_qty);
        dialog_set_mkedt_qty.setText(String.format("%d", tk_ticket_product.getQty_used()));
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
    }

    @Override
    public void syncPipeline() {

    }
}