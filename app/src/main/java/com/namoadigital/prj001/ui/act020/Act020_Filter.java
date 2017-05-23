package com.namoadigital.prj001.ui.act020;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    private TextView tv_nfc_reader;
    private TextView tv_product;
    private MKEditTextNM mket_product;
    private TextView tv_product_id;
    private MKEditTextNM mket_product_id;
    private TextView tv_serial;
    private MKEditTextNM mket_serial;
    private ImageView iv_search;
    private HMAux hmAux_Trans;
    private IAct020_Filter delegate;
    private ArrayList<MKEditTextNM> controls_sta;
    private Drawable drawableNFC;

    public interface IAct020_Filter{

        void onNFCClick(int id);

        void onIvSearchClick(String product, String product_id, String serial);

    }

    public void setOnDrawerClick(IAct020_Filter delegate){
        this.delegate = delegate;
    }

    public Drawable getDrawableNFC() {
        return drawableNFC;
    }

    public void setDrawableNFC(Drawable drawableNFC) {
        this.drawableNFC = drawableNFC;
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
        //
        tv_nfc_reader = (TextView) view.findViewById(R.id.act020_drawer_content_tv_nfc_reader);
        drawableNFC = tv_nfc_reader.getCompoundDrawables()[2];
        drawableNFC.setBounds(0,0,50,50);

        setDrawableNFC(drawableNFC);
        //
        tv_product = (TextView) view.findViewById(R.id.act020_drawer_content_tv_product_code);
        //
        mket_product = (MKEditTextNM) view.findViewById(R.id.act020_drawer_content_mket_product_code);
        controls_sta.add(mket_product);
        //
        tv_product_id = (TextView) view.findViewById(R.id.act020_drawer_content_tv_product_id);
        //
        mket_product_id = (MKEditTextNM) view.findViewById(R.id.act020_drawer_content_mket_product_id);
        controls_sta.add(mket_product_id);
        //
        tv_serial = (TextView) view.findViewById(R.id.act020_drawer_content_tv_serial);
        //
        mket_serial = (MKEditTextNM) view.findViewById(R.id.act020_drawer_content_mket_serial);
        controls_sta.add(mket_serial);
        //
        iv_search = (ImageView) view.findViewById(R.id.act020_drawer_content_iv_search);


    }

    private void iniAction() {

        tv_nfc_reader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (delegate != null) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        delegate.onNFCClick(tv_nfc_reader.getId());
                    }

                }
                return false;
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null){
                    delegate.onIvSearchClick(
                            mket_product.getText().toString().trim(),
                            mket_product_id.getText().toString().trim(),
                            mket_serial.getText().toString().trim()
                    );
                }
            }
        });

    }

    public void setNFCText(String text){
        tv_nfc_reader.setText(text.toString());
    }

    public void setProductCodeText(String text){
        mket_product.setText(text.toString());
    }

    public void setProductIdText(String text){
        mket_product_id.setText(text.toString());
    }

    public void setSerialIdText(String text){
        mket_serial.setText(text.toString());
    }

    public void setHmAux_Trans(HMAux hmAux_Trans) {
        this.hmAux_Trans = hmAux_Trans;
        setTranslation();
    }

    private void setTranslation() {
        tv_product.setText(hmAux_Trans.get("drawer_product_lbl"));
        //
        tv_product_id.setText(hmAux_Trans.get("drawer_product_id_lbl"));
       // mket_product.setHint(hmAux_Trans.get("search_prod_hint"));
        tv_serial.setText(hmAux_Trans.get("drawer_serial_lbl"));
        //mket_serial.setHint(hmAux_Trans.get("search_prod_hint"));
    }

    public ArrayList<MKEditTextNM> getControlsSta(){
        return controls_sta;
    }
}
