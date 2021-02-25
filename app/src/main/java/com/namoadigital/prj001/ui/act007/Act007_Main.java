package com.namoadigital.prj001.ui.act007;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Serial_Log_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_Generate_NForm_PDF;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Log;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act007_Main extends Base_Activity implements Act007_Main_View {

    private Act007_Main_Presenter mPresenter;
    private TextView tv_product_lbl;
    private TextView tv_product_val;
    private TextView tv_serial_lbl;
    private TextView tv_serial_val;
    private ListView lv_logs;
    private Serial_Log_Adapter mAdapter;
    private Bundle bundle;
    private MD_Product_Serial mdProductSerial;
    private String file_name;
    private String wsProcess ="";
    private int itemPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act007_main);

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
                Constant.ACT007
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("alert_log_file_not_found_ttl");
        transList.add("alert_log_file_not_found_msg");
        transList.add("dialog_serial_log_ttl");
        transList.add("dialog_serial_log_start");
        transList.add("alert_empty_log_ttl");
        transList.add("alert_empty_log_msg");
        //
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_download_ttl");
        transList.add("alert_download_confirm_msg");
        transList.add("dialog_so_download_ttl");
        transList.add("dialog_so_download_start");
        transList.add("alert_go_to_so_confirm_msg");
        //
        transList.add("alert_download_form_pdf_ttl");
        transList.add("alert_download_form_pdf_confirm");
        transList.add("alert_generate_form_pdf_ttl");
        transList.add("alert_generate_form_pdf_confirm");
        transList.add("dialog_generate_form_pdf_ttl");
        transList.add("dialog_generate_form_pdf_start");
        transList.add("alert_form_pdf_not_found_ttl");
        transList.add("alert_form_pdf_not_found_msg");
        transList.add("alert_form_pdf_download_error_ttl");
        transList.add("alert_form_pdf_download_error_msg");
        transList.add("alert_form_pdf_name_error_ttl");
        transList.add("alert_form_pdf_name_error_msg");
        transList.add("alert_generate_form_pdf_error_ttl");
        transList.add("alert_generate_form_pdf_error_msg");
        transList.add("dialog_download_form_pdf_ttl");
        transList.add("dialog_download_form_pdf_msg");
        //
        transList.add("alert_starting_pdf_not_supported_ttl");
        transList.add("alert_starting_pdf_not_supported_msg");
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
        recoverIntentsInfo();
        //
        mPresenter = new Act007_Main_Presenter_Impl(
                context,
                this,
                new MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans,
                mdProductSerial,
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        //tv_product_lbl = (TextView) findViewById(R.id.act007_tv_product_lbl);
        //tv_product_lbl.setTag("product_lbl");
        //
        tv_product_val = (TextView) findViewById(R.id.act007_tv_product_val);
        //
        tv_serial_lbl = (TextView) findViewById(R.id.act007_tv_serial_lbl);
        //tv_serial_lbl.setTag("serial_lbl");
        //
        tv_serial_val = (TextView) findViewById(R.id.act007_tv_serial_val);
        //
        lv_logs = (ListView) findViewById(R.id.act007_lv_log);
        //
        //views.add(tv_product_lbl);
        //views.add(tv_serial_lbl);
        //Chama carregamento dos dados do produto.
        setProductInfo();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mdProductSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            //file_name = bundle.getString(WS_Serial_Log.SERIAL_LOG_FILE, "");
        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    public void setProductInfo() {
        tv_product_val.setText(mdProductSerial.getProduct_id() + " - " + mdProductSerial.getProduct_desc());
        //Chama metodo que carrega lista de log do arquivo json
        tv_serial_val.setText(mdProductSerial.getSerial_id());
        //
        mPresenter.executeSerialLog(mdProductSerial);
    }

    @Override
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void disablePD() {
        disableProgressDialog();
    }

    @Override
    public void setItemGeneratedUrl(String url) {
        if(mAdapter != null && itemPosition != -1) {
            Serial_Log_Obj logObj = (Serial_Log_Obj) mAdapter.getItem(itemPosition);
            logObj.setFile_url(url);
            mAdapter.updateItemData(logObj, itemPosition);
        }
    }

    @Override
    public void setItemAsDownloaded() {
        if(mAdapter != null && itemPosition != -1) {
            Serial_Log_Obj logObj = (Serial_Log_Obj) mAdapter.getItem(itemPosition);
            logObj.setLog_downloaded(true);
            mAdapter.updateItemData(logObj, itemPosition);
            //Reseta var de position
            itemPosition = -1;
        }
    }

    @Override
    public void loadLogList(final ArrayList<Serial_Log_Obj> logList) {
        mAdapter = new Serial_Log_Adapter(
                context,
                logList,
                R.layout.serial_log_cell
        );
        //
        mAdapter.setIvDownloadClickListner(new Serial_Log_Adapter.ivDownloadClick() {
//            @Override
//            public void onIvDowloadClick(String process, String[] pk, boolean alreadyDownloaded) {
//                showDownloadMsg(process,pk,alreadyDownloaded);
//            }

            @Override
            public void onIvDowloadClick(String process, Serial_Log_Obj logObj, int position) {
                switch (process) {
                    case Serial_Log_Adapter.SYS_PROCESS_SO:
                        showDownloadMsg(process,logObj.getSplitedPk(),logObj.isLog_downloaded());
                        break;
                    case Serial_Log_Adapter.SYS_PROCESS_N_FORM:
                        itemPosition = position;
                        //
                        if(logObj.getFile_url() != null && !logObj.getFile_url().isEmpty()){
                            if(logObj.isLog_downloaded()){
                                mPresenter.openPDF(logObj.getSplitedPk());
                            }else{
                                showNFormPDFOptions(logObj);
                            }
                        } else{
                            showNFormPDFOptions(logObj);
                        }
                        break;
                    default:

                }
            }
        });
        //
        lv_logs.setAdapter(mAdapter);
    }

    private void showNFormPDFOptions(final Serial_Log_Obj logObj) {
        String ttl = "";
        String msg = "";
        DialogInterface.OnClickListener listener = null;
        //
        if(logObj.getFile_url() != null && !logObj.getFile_url().isEmpty()){
            ttl = hmAux_Trans.get("alert_download_form_pdf_ttl");
            msg = hmAux_Trans.get("alert_download_form_pdf_confirm");
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.executeNFormPDFDownload(logObj.getSplitedPk(),logObj.getFile_url());
                }
            };
        }else{
            ttl = hmAux_Trans.get("alert_generate_form_pdf_ttl");
            msg = hmAux_Trans.get("alert_generate_form_pdf_confirm");
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.executeNFormPDFGeneration(logObj.getSys_pk());
                }
            };
        }
        //
        ToolBox.alertMSG_YES_NO(
                context,
                ttl,
                msg,
                listener,
                1
        );
    }

    private void showDownloadMsg(final String process, final String[] pk, final boolean alreadyDownloaded) {
        String ttl = hmAux_Trans.get("alert_download_ttl");
        String msg = "";
        //
        switch (process){
            case Serial_Log_Adapter.SYS_PROCESS_SO:
                if(alreadyDownloaded) {
                    msg = hmAux_Trans.get("alert_go_to_so_confirm_msg");
                }else{
                    msg = hmAux_Trans.get("alert_download_confirm_msg");
                }
                break;
            default:
                break;
        }
        //
        ToolBox.alertMSG_YES_NO(
                context,
                ttl,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.processDownloadClick(process,pk,alreadyDownloaded);
                    }
                },
                1
        );
    }


    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT007;
        mAct_Title = Constant.ACT007 + "_" + "title";
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
    public void showNoFileMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_log_file_not_found_ttl"),
                hmAux_Trans.get("alert_log_file_not_found_msg"),
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
                hmAux_Trans.get("alert_empty_log_ttl"),
                hmAux_Trans.get("alert_empty_log_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                },
                0
        );
    }

    public void sendResult(Intent data){
        if(data != null){
            setResult(RESULT_OK,data);
        }else{
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if (wsProcess.equals(WS_Serial_Log.class.getName())) {
            mPresenter.setFile_name(hmAux.get(WS_Serial_Log.SERIAL_LOG_FILE));
            //
            mPresenter.getLog();
            disableProgressDialog();
        }else if(wsProcess.equals(WS_SO_Search.class.getName())){
            disableProgressDialog();
            //
            mPresenter.processSoDownloadResult(hmAux);
        }else if(wsProcess.equals(WS_Generate_NForm_PDF.class.getName())){
            disableProgressDialog();
            //
            if( hmAux != null
                && hmAux.hasConsistentValue(WS_Generate_NForm_PDF.NFORM_PK_KEY)
                && hmAux.hasConsistentValue(GE_Custom_Form_BlobDao.BLOB_URL)
            ) {
                setItemGeneratedUrl(hmAux.get(GE_Custom_Form_BlobDao.BLOB_URL));
                //
                mPresenter.executeNFormPDFDownload(
                        hmAux.get(WS_Generate_NForm_PDF.NFORM_PK_KEY).replace("|","@@@").split("@@@"),
                        hmAux.get(GE_Custom_Form_BlobDao.BLOB_URL)
                );
            }else{
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_generate_form_pdf_error_ttl"),
                        hmAux_Trans.get("alert_generate_form_pdf_error_msg"),
                        null,
                        0
                );
            }
        }

    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        disableProgressDialog();
        //
        if (!wsProcess.equals(WS_Generate_NForm_PDF.class.getName())) {
            onBackPressed();
        }

    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        disableProgressDialog();
        //
        onBackPressed();
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
        mPresenter.deleteLogFile();
        //
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

}
