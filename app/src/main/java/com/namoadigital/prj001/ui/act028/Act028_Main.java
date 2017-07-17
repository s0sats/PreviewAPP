package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.HashMap;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act028_Main extends Base_Activity_Frag {

    private Context context;

    private Bundle bundle;

    private long mCustomer_code;
    private int mSO_PREFIX;
    private int mSO_CODE;

    private HashMap<String, String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act028_main);
        //
        context = getBaseContext();
        //
        recoverGetIntents();
        //
        int i = 10;
    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mCustomer_code = ToolBox_Con.getPreference_Customer_Code(context);
            mSO_PREFIX = Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, "-1"));
            mSO_CODE = Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, "-1"));
            mData = (HashMap<String, String>) bundle.getSerializable("data");
        } else {
            mCustomer_code = ToolBox_Con.getPreference_Customer_Code(context);
            mSO_PREFIX = -1;
            mSO_CODE = -1;
            mData = new HMAux();
        }
    }
}
