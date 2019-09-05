package com.namoadigital.prj001.view.act.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

public class Test extends Base_Activity_Frag_NFC_Geral implements On_Frg_Serial_Search {

    private FragmentManager fm;
    private Frg_Serial_Search frgSearch;

    private HMAux hmAux_Trans_frg_serial_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        initVars();
        initActions();
    }

    private void initVars() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Serial_Search();
        //
        frgSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.test_frg_search);
        frgSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        frgSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(frgSearch.getControlsSta());
        frgSearch.setClickListener(actionBTN);
        frgSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {

                // Remover. Apenas Teste
                //Log.d("HMAUX", optionsInfo.get(Frg_Serial_Search.PRODUCT_ID).isEmpty() ? "Foi ruim" : optionsInfo.get(Frg_Serial_Search.PRODUCT_ID) );

                Log.d("HMAUX", optionsInfo.get(Frg_Serial_Search.PRODUCT_ID) );

            }
        });

    }

    private void loadTranslationFrg_Serial_Search() {
        List<String> transList = new ArrayList<String>();
        transList.add("btn_enable_nfc");
        transList.add("product_lbl");
        transList.add("serial_lbl");
        transList.add("tracking_lbl");
        transList.add("btn_option_01");
        transList.add("btn_option_02");
        transList.add("btn_option_03");
        transList.add("product_hint");
        transList.add("serial_hint");
        transList.add("tracking_hint");

        hmAux_Trans_frg_serial_search = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initActions() {

    }

    @Override
    public void onBackPressed() {
        callAct003(getBaseContext());
    }

    public void callAct003(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    public void callAct_Product_Selection(Context context) {
        Intent mIntent = new Intent(context, Act_Product_Selection.class);
        //mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        startActivityForResult(mIntent, 20);
    }

    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);

        if (!status) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            ToolBox.alertMSG(
                    context,
                    "Erro:",
                    value[0],
                    null,
                    0
            );

        } else {
            frgSearch.cleanFields();
//            ToolBox_Inf.hideSoftKeyboard(Act025_Main.this);
            String product_id = "";
            //
            switch (value[0]) {
                case PRODUCT:
//                    product_id = mPresenter.searchProductInfo(value[2], "");
//                    //
//                    if (!product_id.equals("")) {
//                        fragFilters.setNFCText(hmAux_Trans.get("drawer_product_lbl"));
//                        fragFilters.setProductCodeText(value[2]);
//                        fragFilters.setProductIdText(product_id);
//                        mPresenter.executeSerialSearch(product_id, "", "");
//                    } else {
//                        ToolBox.alertMSG(
//                                context,
//                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
//                                hmAux_Trans.get("alert_local_product_not_found_msg"),
//                                null,
//                                0
//                        );
//                    }
                    break;
                case SERIAL:
                    //product_id = mPresenter.searchProductInfo(value[2], "");
                    product_id = value[2];
                    //
                    //if (!product_id.equals("")) {
                    //frgSearch.setNFCText(hmAux_Trans.get("drawer_serial_lbl"));
                    //frgSearch.setProductCodeText(value[2]);
                    //frgSearch.setProductIdText(product_id);
                    //frgSearch.setSerialIdText(value[3]);
                    frgSearch.setProductIdText(value[2]);
                    frgSearch.setSerialIdText(value[1]);
                    frgSearch.setTrackingText("nada");
                    //mPresenter.executeSerialSearch(product_id,value[3],"");
                    //} else {
//                        ToolBox.alertMSG(
//                                context,
//                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
//                                hmAux_Trans.get("alert_local_product_not_found_msg"),
//                                null,
//                                0
//                        );
//                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 20:
                // Remover. Apenas Teste
                Log.d("VAI", "PROCESSO DE CHAMADA");
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean hasHideSerialInfoChk() {
        return false;
    }
}
