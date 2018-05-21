package com.namoadigital.prj001.view.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection;

import java.util.ArrayList;

public class Frg_Serial_Search extends Fragment {

    public static final String BTN_OPTION_01 = "btn_option_01";
    public static final String BTN_OPTION_02 = "btn_option_02";
    public static final String BTN_OPTION_03 = "btn_option_03";

    public static final String PRODUCT_ID = "product_id";
    public static final String SERIAL = "serial";
    public static final String TRACKING = "tracking";

    private ButtonNFC btn_nfc_reader;
    private TextView tv_product_id;
    private MKEditTextNM mket_product_id;
    private ImageView iv_product_id;
    private TextView tv_serial;
    private MKEditTextNM mket_serial;
    private TextView tv_tracking;
    private MKEditTextNM mket_tracking;

    private HMAux hmAux_Trans;
    private boolean showHint;
    private boolean showTree = true;

    private Button btn_option_01;
    private Button btn_option_02;
    private Button btn_option_03;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        btn_nfc_reader.setOnClickListener(clickListener);
    }

    public void setSupportNFC(boolean supportNFC) {
        this.supportNFC = supportNFC;
        if (supportNFC) {
            btn_nfc_reader.setVisibility(View.VISIBLE);
        } else {
            btn_nfc_reader.setVisibility(View.GONE);
        }
    }

    private I_Frg_Serial_Search delegate;

    public interface I_Frg_Serial_Search {

        void onSearchClick(String btn_Action, HMAux optionsInfo);

    }

    public void setOnSearchClickListener(I_Frg_Serial_Search delegate) {
        this.delegate = delegate;
    }

    private ArrayList<MKEditTextNM> controls_sta;
    private View.OnClickListener clickListener;
    private boolean supportNFC;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_serial_search, container, false);

        iniVar(view);
        iniAction();

        return view;
    }

    private void iniVar(View view) {

        controls_sta = new ArrayList<>();
        //
        btn_nfc_reader = (ButtonNFC) view.findViewById(R.id.frg_serial_search_btn_nfc_reader);
        btn_nfc_reader.setmCustomer_code(String.valueOf(ToolBox_Con.getPreference_Customer_Code(getContext())));
        btn_nfc_reader.setmProduct(true);
        btn_nfc_reader.setmSerial(true);
        btn_nfc_reader.setmProgressClose(true);
        //
        tv_product_id = (TextView) view.findViewById(R.id.frg_serial_search_tv_product_id);
        mket_product_id = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_product_id);
        iv_product_id = (ImageView) view.findViewById(R.id.frg_serial_search_iv_product_id);
        //
        controls_sta.add(mket_product_id);
        //
        tv_serial = (TextView) view.findViewById(R.id.frg_serial_search_tv_serial);
        mket_serial = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_serial);
        controls_sta.add(mket_serial);
        //
        tv_tracking = (TextView) view.findViewById(R.id.frg_serial_search_tv_tracking);
        mket_tracking = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_tracking);
        controls_sta.add(mket_tracking);
        //
        btn_option_01 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_01);
        btn_option_02 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_02);
        btn_option_03 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_03);

        if (supportNFC) {
            btn_nfc_reader.setVisibility(View.VISIBLE);
        } else {
            btn_nfc_reader.setVisibility(View.GONE);
        }
    }

    private void iniAction() {
        iv_product_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAct_Product_Selection(getActivity());
            }
        });

        btn_option_01.setOnClickListener(btnActionListener);
        btn_option_02.setOnClickListener(btnActionListener);
        btn_option_03.setOnClickListener(btnActionListener);

    }

    private View.OnClickListener btnActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String btnAction = null;

            switch (v.getId()) {
                case R.id.frg_serial_search_btn_option_01:
                    btnAction = BTN_OPTION_01;
                    break;
                case R.id.frg_serial_search_btn_option_02:
                    btnAction = BTN_OPTION_02;
                    break;
                case R.id.frg_serial_search_btn_option_03:
                    btnAction = BTN_OPTION_03;
                    break;
                default:
                    btnAction = "";
                    break;
            }

            HMAux values = new HMAux();
            values.put(PRODUCT_ID, mket_product_id.getText().toString().trim().isEmpty() ? "" : mket_product_id.getText().toString().trim());
            values.put(SERIAL, ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim().isEmpty() ? "" : mket_serial.getText().toString().trim()));
            values.put(TRACKING, mket_tracking.getText().toString().trim().isEmpty() ? "" : mket_tracking.getText().toString().trim());

            if (delegate != null) {
                delegate.onSearchClick(
                        btnAction,
                        values
                );
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }

    private void loadDataToScreen() {
        if (showTree) {
            iv_product_id.setVisibility(View.VISIBLE);
        } else {
            iv_product_id.setVisibility(View.GONE);
        }
    }

    // General Methods
    public void setNFCText(String text) {
        btn_nfc_reader.setText(text.toString());
    }

    public void setProductIdText(String text) {
        mket_product_id.setText(text.toString());
    }

    public void setProductIdHint(String hint) {
        mket_product_id.setHint(hint.toString());
    }

    public void setSerialIdText(String text) {
        mket_serial.setText(text.toString());
    }

    public void setSerialIdHint(String hint) {
        mket_serial.setHint(hint.toString());
    }

    public void setTrackingText(String text) {
        mket_tracking.setText(text.toString());
    }

    public void setTrackingdHint(String hint) {
        mket_tracking.setHint(hint.toString());
    }

    public void setHmAux_Trans(HMAux hmAux_Trans) {
        setHmAux_Trans(hmAux_Trans, true);
    }

    public void setBtnOption01(String label) {
        btn_option_01.setText(label);
    }

    public void setBtnOption02(String label) {
        btn_option_02.setText(label);
    }

    public void setBtnOption03(String label) {
        btn_option_03.setText(label);
    }

    public void setHmAux_Trans(HMAux hmAux_Trans, boolean showHint) {
        this.hmAux_Trans = hmAux_Trans;
        this.showHint = showHint;
        //
        setTranslation();
    }

    private void setTranslation() {
        btn_nfc_reader.setText(hmAux_Trans.get("btn_enable_nfc"));
        tv_product_id.setText(hmAux_Trans.get("product_lbl"));
        tv_serial.setText(hmAux_Trans.get("serial_lbl"));
        tv_tracking.setText(hmAux_Trans.get("tracking_lbl"));
        //
        btn_option_01.setText(hmAux_Trans.get("btn_option_01"));
        btn_option_02.setText(hmAux_Trans.get("btn_option_02"));
        btn_option_03.setText(hmAux_Trans.get("btn_option_03"));
        //
        if (showHint) {
            mket_product_id.setHint(hmAux_Trans.get("product_hint"));
            mket_serial.setHint(hmAux_Trans.get("serial_hint"));
            mket_tracking.setHint(hmAux_Trans.get("tracking_hint"));
        } else {
            mket_product_id.setHint("");
            mket_serial.setHint("");
            mket_tracking.setHint("");
        }
    }

    public void setShowTree(boolean showTree) {
        this.showTree = showTree;
    }

    public ArrayList<MKEditTextNM> getControlsSta() {
        return controls_sta;
    }

    public void cleanFields() {
        btn_nfc_reader.setText(hmAux_Trans.get("btn_enable_nfc"));
        //
        mket_product_id.setText("");
        //
        mket_serial.setText("");
        //
        mket_tracking.setText("");
        //
        btn_option_01.setText(hmAux_Trans.get("btn_option_01"));
        btn_option_02.setText(hmAux_Trans.get("btn_option_02"));
        btn_option_03.setText(hmAux_Trans.get("btn_option_03"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 20:
                processResult(resultCode, data);
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processResult(int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            MD_Product pAux = (MD_Product) data.getSerializableExtra(MD_Product.class.getName());
            mket_product_id.setText(String.valueOf(pAux.getProduct_code()));
        } else {
            mket_product_id.setText("Não achou");
        }
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
}
