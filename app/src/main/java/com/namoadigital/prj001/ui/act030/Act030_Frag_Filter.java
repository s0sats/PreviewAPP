package com.namoadigital.prj001.ui.act030;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 21/07/2017.
 */

public class Act030_Frag_Filter extends Fragment {

    private ButtonNFC tv_nfc_reader;
    private TextView tv_product;
    private MKEditTextNM mket_product;
    private TextView tv_product_id;
    private MKEditTextNM mket_product_id;
    private TextView tv_serial;
    private MKEditTextNM mket_serial;
    private TextView tv_tracking;
    private MKEditTextNM mket_tracking;
    private ImageView iv_search;
    private HMAux hmAux_Trans;
    private IAct030_Filter delegate;
    private ArrayList<MKEditTextNM> controls_sta;
    private View.OnClickListener clickListener;
    private boolean supportNFC;

    public interface IAct030_Filter{

        void onIvSearchClick(String product, String product_id, String serial);

    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        tv_nfc_reader.setOnClickListener(clickListener);
    }

    public void setSupportNFC(boolean supportNFC) {
        this.supportNFC = supportNFC;
        if(supportNFC){
            tv_nfc_reader.setVisibility(View.VISIBLE);
        }else{
            tv_nfc_reader.setVisibility(View.GONE);
        }
    }

    public void setOnDrawerClick(IAct030_Filter delegate){
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.act030_drawer_content,container,false);
        iniVar(view);
        iniAction();

        return view;
    }

    private void iniVar(View view) {

        controls_sta = new ArrayList<>();
        //
        tv_nfc_reader = (ButtonNFC) view.findViewById(R.id.act030_drawer_content_tv_nfc_reader);
        tv_nfc_reader.setmCustomer_code(String.valueOf(ToolBox_Con.getPreference_Customer_Code(getContext())));
        tv_nfc_reader.setmProduct(true);
        tv_nfc_reader.setmSerial(true);
        tv_nfc_reader.setmProgressClose(true);
        //
        tv_product = (TextView) view.findViewById(R.id.act030_drawer_content_tv_product_code);
        //
        mket_product = (MKEditTextNM) view.findViewById(R.id.act030_drawer_content_mket_product_code);
        controls_sta.add(mket_product);
        //
        tv_product_id = (TextView) view.findViewById(R.id.act030_drawer_content_tv_product_id);
        //
        mket_product_id = (MKEditTextNM) view.findViewById(R.id.act030_drawer_content_mket_product_id);
        controls_sta.add(mket_product_id);
        //
        tv_serial = (TextView) view.findViewById(R.id.act030_drawer_content_tv_serial);
        //
        mket_serial = (MKEditTextNM) view.findViewById(R.id.act030_drawer_content_mket_serial);
        controls_sta.add(mket_serial);
        //
        tv_tracking = (TextView) view.findViewById(R.id.act030_drawer_content_tv_tracking);
        //
        mket_tracking = (MKEditTextNM) view.findViewById(R.id.act030_drawer_content_mket_tracking);
        controls_sta.add(mket_tracking);
        //
        iv_search = (ImageView) view.findViewById(R.id.act030_drawer_content_iv_search);

        if(supportNFC){
            tv_nfc_reader.setVisibility(View.VISIBLE);
        }else{
            tv_nfc_reader.setVisibility(View.GONE);
        }

    }

    private void iniAction() {

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null){
                    delegate.onIvSearchClick(
                            mket_product_id.getText().toString().trim(),
                            ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim()),
                            mket_tracking.getText().toString().trim()
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
    public String getProductCodeText(){
        return mket_product.getText().toString().trim();
    }

    public void setProductIdText(String text){
        mket_product_id.setText(text.toString());
    }

    public String getProductIdText(){
       return mket_product_id.getText().toString().trim();
    }

    public void setSerialIdText(String text){
        mket_serial.setText(text.toString());
    }

    public String getSerialIdText(){
        return ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim());
    }

    public void setTrackingText(String text){
        mket_tracking.setText(text.toString());
    }

    public String getTrackingText(){
        return mket_tracking.getText().toString().trim();
    }

    public void setHmAux_Trans(HMAux hmAux_Trans) {
        this.hmAux_Trans = hmAux_Trans;
        setTranslation();
    }


    private void setTranslation() {
        tv_nfc_reader.setText(hmAux_Trans.get("component_enable_nfc_lbl"));
        //
        tv_product.setText(hmAux_Trans.get("drawer_product_lbl"));
        //
        tv_product_id.setText(hmAux_Trans.get("drawer_product_id_lbl"));
        // mket_product.setHint(hmAux_Trans.get("search_prod_hint"));
        tv_serial.setText(hmAux_Trans.get("drawer_serial_lbl"));
        //mket_serial.setHint(hmAux_Trans.get("search_prod_hint"));
        tv_tracking.setText(hmAux_Trans.get("drawer_tracking_lbl"));
    }

    public ArrayList<MKEditTextNM> getControlsSta(){
        return controls_sta;
    }

    public void cleanFields(){
        tv_nfc_reader.setText(hmAux_Trans.get("component_enable_nfc_lbl"));
        //
        mket_product.setText("");
        //
        mket_product_id.setText("");
        //
        mket_serial.setText("");
        //
        mket_tracking.setText("");
    }
}
