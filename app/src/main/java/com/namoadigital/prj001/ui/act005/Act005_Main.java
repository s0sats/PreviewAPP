package com.namoadigital.prj001.ui.act005;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act005_Adapter;
import com.namoadigital.prj001.ui.act001.Act001_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main extends Base_Activity implements Act005_Main_View {

    public static final String MENU_ICON = "menu_icon" ;
    public static final String MENU_DESC = "menu_desc";

    private Context context;
    private List<HMAux> menu_list;
    private GridView gv_menu;
    private Act005_Main_Presenter mPresenter;
    private Act005_Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act005_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    private void iniSetup() {
        context = getBaseContext();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT005
        );
        //
        loadTranslation();
        //
    }

    private void initVars() {
        mPresenter = new Act005_Main_Presenter_Impl(context,this);
        //
        gv_menu = (GridView) findViewById(R.id.act005_gv_menu);
        //
        mPresenter.getMenuItens(hmAux_Trans);

    }

    private void initActions() {
        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                Toast.makeText(context,item.get(Act005_Main.MENU_DESC),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void loadMenu(List<HMAux> menus) {
        //Traduz menus
        for (HMAux item : menus) {
            if (hmAux_Trans.get(item.get(Act005_Main.MENU_DESC)) != null) {
                item.put(Act005_Main.MENU_DESC, hmAux_Trans.get(item.get(Act005_Main.MENU_DESC)));
            } else {
                item.put(Act005_Main.MENU_DESC, ToolBox.setNoTrans(mModule_Code, mResource_Code, item.get(Act005_Main.MENU_DESC)));
            }
        }


        mAdapter =  new Act005_Adapter(context,R.layout.act005_item_menu,menus);
        gv_menu.setAdapter(mAdapter);
    }

    private void loadTranslation(){
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context)
        );

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT005;
        mAct_Title = Constant.ACT005 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act005_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.act05_action_settings) {

            ToolBox_Con.cleanPreferences(context);

            Intent mIntent = new Intent(context, Act001_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
