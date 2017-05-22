package com.namoadigital.prj001.ui.act020;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.ArrayList;

/**
 * Created by d.luche on 22/05/2017.
 */

public class Act020_Filter extends Fragment {

    private TextView tv_product;
    private MKEditTextNM mket_product;
    private TextView tv_serial;
    private MKEditTextNM mket_serial;
    private ImageView iv_search;
    private HMAux hmAux_Trans;
    private IAct020_Filter delegate;
    private ArrayList<MKEditTextNM> controls_sta;

    public interface IAct020_Filter{

        void onIvSearchClick(String product, String serial);

    }

    public void setOnDrawerClick(IAct020_Filter delegate){
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.act020_drawer_content,container,false);
        iniVar(view);
        iniAction();

        return view;
    }

    private void iniVar(View view) {

        controls_sta = new ArrayList<>();

        tv_product = (TextView) view.findViewById(R.id.act020_drawer_content_tv_product);
        //
        mket_product = (MKEditTextNM) view.findViewById(R.id.act020_drawer_content_mket_product);
        controls_sta.add(mket_product);
        //
        tv_serial = (TextView) view.findViewById(R.id.act020_drawer_content_tv_serial);
        //
        mket_serial = (MKEditTextNM) view.findViewById(R.id.act020_drawer_content_mket_serial);
        controls_sta.add(mket_serial);
        //
        iv_search = (ImageView) view.findViewById(R.id.act020_drawer_content_iv_search);

    }

    private void iniAction() {

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null){
                    delegate.onIvSearchClick(
                            mket_product.getText().toString().trim(),
                            mket_serial.getText().toString().trim()
                    );
                }
            }
        });

    }

    public void setHmAux_Trans(HMAux hmAux_Trans) {
        this.hmAux_Trans = hmAux_Trans;
        setTranslation();
    }

    private void setTranslation() {
        tv_product.setText(hmAux_Trans.get("drawer_product_lbl"));
       // mket_product.setHint(hmAux_Trans.get("search_prod_hint"));
        tv_serial.setText(hmAux_Trans.get("drawer_serial_lbl"));
        //mket_serial.setHint(hmAux_Trans.get("search_prod_hint"));
    }

    public ArrayList<MKEditTextNM> getControlsSta(){
        return controls_sta;
    }
}
