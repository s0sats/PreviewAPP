package com.namoadigital.prj001.ui.act024;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.SO_Header_Adapter;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act026.Act026_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 28/06/2017.
 */

public class Act024_Main extends Base_Activity implements Act024_Main_View {

    private Act024_Main_Presenter mPresenter;
    private ListView lv_so_headers;
    private Button btn_download;
    private Button btn_new;
    private String serialiazed_so_list;
    private SO_Header_Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act024_main);

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
                Constant.ACT024
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act024_title");
        transList.add("btn_new");
        transList.add("btn_download");
        transList.add("alert_download_mult_so_ttl");
        transList.add("alert_download_mult_so_msg");
        transList.add("alert_download_so_ttl");
        transList.add("alert_download_so_msg");
        transList.add("alert_no_so_selected");
        transList.add("progress_downloading_so_ttl");
        transList.add("progress_downloading_so_msg");
        transList.add("alert_no_so_founded_ttl");
        transList.add("alert_no_so_founded_msg");
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
        mPresenter = new Act024_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans
        );
        //
        lv_so_headers = (ListView) findViewById(R.id.act024_lv_so_headers);
        //
        btn_download = (Button) findViewById(R.id.act024_btn_download);
        btn_download.setTag("btn_download");
        //
        btn_new = (Button) findViewById(R.id.act024_btn_new);
        btn_new.setTag("btn_new");
        //
        views.add(btn_download);
        views.add(btn_new);
        //
        mPresenter.getSoHeaderList(serialiazed_so_list);

    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(Constant.ACT023_SO_HEADER_LIST)) {
                serialiazed_so_list = bundle.getString(Constant.ACT023_SO_HEADER_LIST);

            } else {
                //Tratar quando lista de s.o não for enviado.
                //Caixa de alerta e volta para menu?!?
                ToolBox_Inf.alertBundleNotFound(this,hmAux_Trans);
            }
        } else {
            //Tratar caso não exista bundle
            ToolBox_Inf.alertBundleNotFound(this,hmAux_Trans);
        }

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT024;
        mAct_Title = Constant.ACT024 + "_" + "title";
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
        //
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multDownloadConfirm();
            }
        });
        //Como não exite criação de SO por hora, o btn fica apagado
        btn_new.setVisibility(View.GONE);
        //
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void loadSoHeaders(ArrayList<HMAux> so_list) {
        mAdapter = new SO_Header_Adapter(
                context,
                so_list,
                SO_Header_Adapter.CONFIG_TYPE_DOWNLOAD,
                R.layout.so_header_cell,
                R.layout.so_header_cell
        );
        //
        lv_so_headers.setAdapter(mAdapter);
        //
        mAdapter.setOnDownloadBtnClicked(new SO_Header_Adapter.ISO_Header_Adapter() {
            @Override
            public void downloadBtnClicked(HMAux so) {
                ArrayList<HMAux> so_list = new ArrayList<HMAux>();
                //
                so_list.add(so);
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_download_so_ttl"),
                        hmAux_Trans.get("alert_download_mult_so_msg"),
                        getClickListner(so_list),
                        1
                );

            }

            @Override
            public void refreshSelectedQty(int qty_selected) {
                String textChange = hmAux_Trans.get("btn_download");
                if (qty_selected > 0) {
                    textChange += " (" + qty_selected + ") ";
                }
                btn_download.setText(textChange);
            }
        });

    }

    private DialogInterface.OnClickListener getClickListner(final ArrayList<HMAux> so_list) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mAdapter != null) {
                    mPresenter.downloadMultSo(so_list);
                }
            }
        };
        //
        return listener;
    }

    private void multDownloadConfirm() {
        ArrayList<HMAux> soToDownload = null;
        if (mAdapter != null) {
            soToDownload = mAdapter.getSoToDownload();
        }
        //Se Existe lista de download.
        if (soToDownload != null && soToDownload.size() > 0) {
            //
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_download_so_ttl"),
                    // hmAux_Trans.get("alert_download_mult_so_msg"),
                    hmAux_Trans.get("alert_download_so_msg"),
                    getClickListner(soToDownload),
                    1
            );
        }
    }

    private void singleDownloadConfirm(final SM_SO so) {
        if (so != null) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mAdapter != null) {
                        mPresenter.downloadSingleSo(so);
                    }
                }
            };
            //
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_download_so_ttl"),
                    hmAux_Trans.get("alert_download_so_msg"),
                    listener,
                    1
            );
        }
    }

    @Override
    public void showPD() {
        enableProgressDialog(
                hmAux_Trans.get("progress_downloading_so_ttl"),
                hmAux_Trans.get("progress_downloading_so_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (hmAux.containsKey(WS_SO_Search.SO_LIST_QTY)) {
            //Dispara serviço de downloads
            mPresenter.startDownloadServices();
            //
//            if (hmAux.get(WS_SO_Search.SO_LIST_QTY).equals("1")) {
//                String so[] = hmAux.get(WS_SO_Search.SO_LIST).replace(".", "#").split("#");
//                //
//                HMAux hmAuxSO = new HMAux();
//                //
//                hmAuxSO.put(SM_SODao.SO_PREFIX, so[0]);
//                hmAuxSO.put(SM_SODao.SO_CODE, so[1]);
//                //
//                callAct027(context, hmAuxSO);
//
//            } else {
//                callAct026(context);
//            }

        }
        //
        progressDialog.dismiss();
    }

    //Tratativa SESSION NOT FOUND
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
    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }
    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct026(Context context) {
        Intent mIntent = new Intent(context, Act026_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT024);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct027(Context context, HMAux so) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(SM_SODao.SO_PREFIX, so.get(SM_SODao.SO_PREFIX));
        bundle.putString(SM_SODao.SO_CODE, so.get(SM_SODao.SO_CODE));
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
