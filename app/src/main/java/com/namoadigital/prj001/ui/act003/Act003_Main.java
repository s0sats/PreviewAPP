package com.namoadigital.prj001.ui.act003;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_OperationDao;

import java.util.List;

/**
 * Created by neomatrix on 17/01/17.
 */

public class Act003_Main extends Base_Activity implements Act003_Main_View{

    private Context context;
    private ListView lv_operations;
    private Act003_Main_Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act003_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        initVars();
        initActions();
    }

    private void initVars() {
        context = getBaseContext();
        //
        mPresenter =  new Act003_Main_Presenter_Impl(context,this);
        //
        lv_operations = (ListView) findViewById(R.id.act003_lv_customers);
        //
        mPresenter.getOperations();
        //
    }


    private void initActions() {
    }


    @Override
    public void loadOperations(List<HMAux> operations) {

        String[] from = {MD_OperationDao.OPERATION_DESC};
        int[] to = {R.id.lib_custom_cell_tv_item};
        lv_operations.setAdapter(
                new SimpleAdapter(
                        context,
                        operations,
                        R.layout.lib_custom_cell,
                        from,
                        to)
        );
    }
}
