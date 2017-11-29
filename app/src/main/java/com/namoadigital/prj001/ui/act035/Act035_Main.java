package com.namoadigital.prj001.ui.act035;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act035_Adapter_Messages;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act035_Main extends Base_Activity implements Act035_Main_View {

    private TextView tv_customer_val;
    private ListView lv_messages;
    private Act035_Main_Presenter mPresenter;


    private ArrayList<HMAux> dados;

    private Bundle bundle;
    private int backAction;
    private String requestingAct;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act035_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT035
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act035_title");
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
        mPresenter =
                new Act035_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans
                );
        //
        tv_customer_val = (TextView) findViewById(R.id.act0035_tv_customer_val);
        lv_messages = (ListView) findViewById(R.id.act0035_lv_messages);

        gerarDados(100);

        lv_messages.setAdapter(
                new Act035_Adapter_Messages(
                        getBaseContext(),
                        R.layout.act035_main_content_cell_me,
                        R.layout.act035_main_content_cell_other,
                        dados
                )
        );
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
        } else {
        }
    }

    private void iniUIFooter() {
        mAct_Title = Constant.ACT035 + "_" + "title";
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();

    }

    private void initActions() {
        tv_customer_val.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));
    }
    //

    private void gerarDados(int quantidade) {
        dados = new ArrayList<>();
        //
        int multi = 3;
        int index = 0;
        //
        for (int i = 1; i <= quantidade; i++) {
            HMAux hmAux = new HMAux();
            //
            hmAux.put(HMAux.ID, String.valueOf(i));
            //
            if (index == 0) {
                hmAux.put(HMAux.TEXTO_02, String.valueOf(0));
            } else {
                hmAux.put(HMAux.TEXTO_02, String.valueOf(1));
            }
            //
            if (++index == multi) {
                index = 0;
                multi *= 2;
            }
            //
            hmAux.put(HMAux.TEXTO_01, "Valor - " + String.valueOf(i));
            //
            dados.add(hmAux);
        }
    }


}
