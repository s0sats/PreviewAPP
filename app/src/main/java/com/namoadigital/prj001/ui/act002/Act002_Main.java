package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.ui.act001.Act001_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 13/01/17.
 */

public class Act002_Main extends Base_Activity implements Act002_Main_View{
    private Context context;
    private ListView lv_customers;
    private Act002_Main_Presenter mPresenter;
    private Lib_Custom_Cell_Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act002_main);

        SERVICE_TYPE = "SESSION";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        initVars();
        initActions();
    }

    private void initVars() {
        context =  getBaseContext();
        //
        mPresenter = new Act002_Main_Presenter_Impl(context,this);
        //
        lv_customers = (ListView) findViewById(R.id.act002_lv_customers);
        //
        mPresenter.getAllCustomers();
        //
    }

    private void initActions() {
        lv_customers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                /*ToolBox_Con.setPreference_Customer_Code(context, Long.parseLong(item.get(EV_User_CustomerDao.CUSTOMER_CODE)));
                ToolBox_Con.setPreference_Customer_Code_Name(context, EV_User_CustomerDao.CUSTOMER_NAME);
                ToolBox_Con.setPreference_Customer_nls_date_format (context, EV_User_CustomerDao.NLS_DATE_FORMAT);*/

                if(item.get(EV_User_CustomerDao.SESSION_APP).trim().length() == 0) {
                    mPresenter.executeSessionProcess(
                            ToolBox_Con.getPreference_User_Email(context),
                            ToolBox_Con.getPreference_User_Pwd(context),
                            ToolBox_Con.getPreference_User_NFC(context),
                            item,
                            0, //Forced Login
                            1, //Valida Update Required. 1 = não !!
                            0  //Valida User_others_device. 1 = não, 0 = sim
                    );
                }else{
                    callAct003(context);
                }
            }
        });

    }


    @Override
    public void loadCustomers(List<HMAux> customers) {

        mAdapter =  new Lib_Custom_Cell_Adapter(context,R.layout.lib_custom_cell,customers);
        lv_customers.setAdapter(mAdapter);

        /*String[] from = {EV_User_CustomerDao.CUSTOMER_NAME};
        int[] to = {R.id.lib_custom_cell_tv_item};
        lv_customers.setAdapter(
                new SimpleAdapter(
                        context,
                        customers,
                        R.layout.lib_custom_cell,
                        from,
                        to
                )
        );*/
    }

    @Override
    public void processLogin() {
        ToolBox_Inf.call_Act001_Main(context);
    }

    @Override
    public void callAct003(Context context) {
        Intent mIntent =  new Intent(context, Act003_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void showPD() {
        enableProgressDialog(
                "Get Session",
                "Start Processing...",
                "Cancel",
                "Ok"
        );
    }

    @Override
    protected void processOtherDevice() {
        super.processOtherDevice();
        HMAux item = new HMAux();
        //
        item.put(EV_User_CustomerDao.CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code_TMP(context));
        item.put(EV_User_CustomerDao.TRANSLATE_CODE,ToolBox_Con.getPreference_Translate_Code_TMP(context));
        //
        mPresenter.executeSessionProcess(
                ToolBox_Con.getPreference_User_Email(context),
                ToolBox_Con.getPreference_User_Pwd(context),
                ToolBox_Con.getPreference_User_NFC(context),
                item,
                1, //Forced Login
                1, //Valida Update Required. 1 = não !!
                1  //Valida User_others_device. 1 = não, 0 = sim
        );

    }

    @Override
    protected void processSync() {
        super.processSync();

        disableProgressDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act001_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.act01_action_settings) {

            ToolBox_Con.cleanPreferences(context);

            Intent mIntent = new Intent(context, Act001_Main.class);

            context.startActivity(mIntent);

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
