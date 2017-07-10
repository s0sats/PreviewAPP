package com.namoadigital.prj001.ui.act026;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act005.Act005_Main;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act026_Main extends Base_Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act026_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
}
