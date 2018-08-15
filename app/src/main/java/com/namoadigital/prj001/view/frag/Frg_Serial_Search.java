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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection;

import java.util.ArrayList;
import java.util.List;

public class Frg_Serial_Search extends Fragment {

    public static final String BTN_OPTION_01 = "btn_option_01";
    public static final String BTN_OPTION_02 = "btn_option_02";
    public static final String BTN_OPTION_03 = "btn_option_03";
    public static final String BTN_OPTION_04 = "btn_option_04";
    public static final String BTN_OPTION_05 = "btn_option_05";

    public static final String PRODUCT_ID = "product_id";
    public static final String SERIAL = "serial";
    public static final String TRACKING = "tracking";

    private TextView tv_product_id;
    private MKEditTextNM mket_product_id;
    private ImageView iv_product_change;
    private ImageView iv_product_id;
    private LinearLayout ll_product_id;
    private TextView tv_serial;
    private LinearLayout ll_serial;
    private MKEditTextNM mket_serial;
    private TextView tv_tracking;
    private LinearLayout ll_tracking;
    private MKEditTextNM mket_tracking;

    private HMAux hmAux_Trans;
    private boolean showHint;
    private boolean showTree = false;
    private boolean showAll = false;

    private boolean bTokenPendenciesCheck = true;

    private Button btn_option_01;
    private Button btn_option_02;
    private Button btn_option_03;
    private Button btn_option_04;
    private Button btn_option_05;

    private LinearLayout ll_options;

    private ButtonNFC btn_nfc_reader;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        btn_nfc_reader.setOnClickListener(clickListener);
    }

    public void setbTokenPendenciesCheck(boolean bTokenPendenciesCheck) {
        this.bTokenPendenciesCheck = bTokenPendenciesCheck;
    }

    public boolean isbTokenPendenciesCheck() {
        return bTokenPendenciesCheck;
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
        ll_options = (LinearLayout) view.findViewById(R.id.frg_serial_search_ll_options);
        //
        btn_nfc_reader = (ButtonNFC) view.findViewById(R.id.frg_serial_search_btn_nfc_reader);
        btn_nfc_reader.setmCustomer_code(String.valueOf(ToolBox_Con.getPreference_Customer_Code(getContext())));
        btn_nfc_reader.setmProduct(true);
        btn_nfc_reader.setmSerial(true);
        btn_nfc_reader.setmProgressClose(true);
        //
        tv_product_id = (TextView) view.findViewById(R.id.frg_serial_search_tv_product_id);
        mket_product_id = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_product_id);
        iv_product_change = (ImageView) view.findViewById(R.id.frg_serial_search_iv_product_change);
        iv_product_id = (ImageView) view.findViewById(R.id.frg_serial_search_iv_product_id);
        ll_product_id = (LinearLayout) view.findViewById(R.id.frg_serial_search_ll_product_id);
        //
        controls_sta.add(mket_product_id);
        //
        tv_serial = (TextView) view.findViewById(R.id.frg_serial_search_tv_serial);
        ll_serial = (LinearLayout) view.findViewById(R.id.frg_serial_search_ll_serial);
        mket_serial = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_serial);
        controls_sta.add(mket_serial);
        //
        tv_tracking = (TextView) view.findViewById(R.id.frg_serial_search_tv_tracking);
        ll_tracking = (LinearLayout) view.findViewById(R.id.frg_serial_search_ll_tracking);
        mket_tracking = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_tracking);
        controls_sta.add(mket_tracking);
        //
        btn_option_01 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_01);
        btn_option_02 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_02);
        btn_option_03 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_03);
        btn_option_04 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_04);
        btn_option_05 = (Button) view.findViewById(R.id.frg_serial_search_btn_option_05);

        if (supportNFC) {
            btn_nfc_reader.setVisibility(View.VISIBLE);
        } else {
            btn_nfc_reader.setVisibility(View.GONE);
        }
    }

    private void iniAction() {
        iv_product_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowTree(true);
                setShowAll(false);
                mket_product_id.setText("");
            }
        });

        iv_product_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAct_Product_Selection(getActivity());
            }
        });

        btn_option_01.setOnClickListener(btnActionListener);
        btn_option_02.setOnClickListener(btnActionListener);
        btn_option_03.setOnClickListener(btnActionListener);
        btn_option_04.setOnClickListener(btnActionListener);
        btn_option_05.setOnClickListener(btnActionListener);

        mket_serial.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {

            @Override
            public void reportTextBySpecialist(String s) {
                btn_option_01.performClick();
            }
        });

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
                case R.id.frg_serial_search_btn_option_04:
                    btnAction = BTN_OPTION_04;
                    break;
                case R.id.frg_serial_search_btn_option_05:
                    btnAction = BTN_OPTION_05;
                    break;

                default:
                    btnAction = "";
                    break;
            }

            HMAux values = getHMAuxValues();

//            values.put(PRODUCT_ID, (mket_product_id.getText().toString().trim().isEmpty() || iv_product_change.getVisibility() == View.VISIBLE) ? "" : mket_product_id.getText().toString().trim());
//            values.put(SERIAL, ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim().isEmpty() ? "" : mket_serial.getText().toString().trim()));
//            values.put(TRACKING, mket_tracking.getText().toString().trim().isEmpty() ? "" : mket_tracking.getText().toString().trim());

            MD_Product mdProductAux = productValidCheck(values.get(PRODUCT_ID));

            if (btnAction.equalsIgnoreCase(BTN_OPTION_01)) {

                if (mdProductAux == null && !values.get(PRODUCT_ID).isEmpty()) {
                    ToolBox.alertMSG(
                            getActivity(),
                            hmAux_Trans.get("alert_no_product_ttl"),
                            hmAux_Trans.get("alert_no_product_msg"),
                            null,
                            0
                    );

                } else if (ToolBox_Con.isOnline(getActivity()) &&
                        ToolBox_Inf.checkSerialTokenURStatus(getActivity()) && isbTokenPendenciesCheck()) {
                    ToolBox.alertMSG(
                            getActivity(),
                            hmAux_Trans.get("alert_serial_pendencies_ttl"),
                            hmAux_Trans.get("alert_serial_pendencies_msg"),
                            null,
                            0
                    );
                } else {
                    if (delegate != null) {
                        delegate.onSearchClick(
                                btnAction,
                                values
                        );
                    }
                }
            } else {
                if (delegate != null) {
                    delegate.onSearchClick(
                            btnAction,
                            values
                    );
                }
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
        customSettings();
    }

    // General Methods
    public void setNFCText(String text) {
        btn_nfc_reader.setText(text.toString());
    }

    public void setProductIdText(String text) {
        if (text.length() != 0){
            iv_product_change.performClick();
        }
        //
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

    public void setBtn_Option_01_Label(String label) {
        btn_option_01.setText(label);
    }

    public void setBtn_Option_01_BackGround(int status) {
        btn_option_01.setBackground(getActivity().getDrawable(status));
    }

    public void setBtn_Option_01_Visibility(int status) {
        btn_option_01.setVisibility(status);
    }

    public void setBtn_Option_02_Label(String label) {
        btn_option_02.setText(label);
    }

    public void setBtn_Option_02_BackGround(int status) {
        btn_option_02.setBackground(getActivity().getDrawable(status));
    }

    public void setBtn_Option_02_Visibility(int status) {
        btn_option_02.setVisibility(status);
    }

    public void setBtn_Option_03_Label(String label) {
        btn_option_03.setText(label);
    }

    public void setBtn_Option_03_BackGround(int status) {
        btn_option_03.setBackground(getActivity().getDrawable(status));
    }

    public void setBtn_Option_03_Visibility(int status) {
        btn_option_03.setVisibility(status);
    }

    public void setBtn_Option_04_Label(String label) {
        btn_option_04.setText(label);
    }

    public void setBtn_Option_04_BackGround(int status) {
        btn_option_04.setBackground(getActivity().getDrawable(status));
    }

    public void setBtn_Option_04_Visibility(int status) {
        btn_option_04.setVisibility(status);
    }

    public void setBtn_Option_05_Label(String label) {
        btn_option_05.setText(label);
    }

    public void setBtn_Option_05_BackGround(int status) {
        btn_option_05.setBackground(getActivity().getDrawable(status));
    }

    public void setBtn_Option_05_Visibility(int status) {
        btn_option_05.setVisibility(status);
    }

    public HMAux getHMAuxValues() {
        HMAux values = new HMAux();
        values.put(PRODUCT_ID, (mket_product_id.getText().toString().trim().isEmpty() || iv_product_change.getVisibility() == View.VISIBLE) ? "" : mket_product_id.getText().toString().trim());
        values.put(SERIAL, ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim().isEmpty() ? "" : mket_serial.getText().toString().trim()));
        values.put(TRACKING, mket_tracking.getText().toString().trim().isEmpty() ? "" : mket_tracking.getText().toString().trim());

        return values;
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
        //
        customSettings();
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
        //
        customSettings();
    }

    public void setShowHideProduct(boolean status) {
        if (status) {
            ll_product_id.setVisibility(View.VISIBLE);
        } else {
            ll_product_id.setVisibility(View.GONE);
        }
    }

    public void setShowHideSerial(boolean status) {
        if (status) {
            ll_serial.setVisibility(View.VISIBLE);
        } else {
            ll_serial.setVisibility(View.GONE);
        }
    }

    public void setShowHideTracking(boolean status) {
        if (status) {
            ll_tracking.setVisibility(View.VISIBLE);
        } else {
            ll_tracking.setVisibility(View.GONE);
        }
    }

    private void customSettings() {
        if (showTree) {
            iv_product_change.setVisibility(View.GONE);
            iv_product_id.setVisibility(View.VISIBLE);
            mket_product_id.setmBARCODE(true);
            mket_product_id.setEnabled(true);
            ll_product_id.setBackground(getActivity().getDrawable(R.drawable.border_back));
        } else {
            if (showAll) {
                iv_product_change.setVisibility(View.VISIBLE);
                iv_product_id.setVisibility(View.GONE);
                mket_product_id.setmBARCODE(false);
                mket_product_id.setEnabled(false);
            } else {
                iv_product_change.setVisibility(View.GONE);
                iv_product_id.setVisibility(View.GONE);
                mket_product_id.setmBARCODE(false);
                mket_product_id.setEnabled(false);
            }
        }
    }

    public void setLl_options(View ll_options) {
        this.ll_options.addView(ll_options);
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

            mket_product_id.setText(String.valueOf(pAux.getProduct_id()));
        } else {
        }
    }

    public void callAct_Product_Selection(Context context) {
        Intent mIntent = new Intent(context, Act_Product_Selection.class);
        //
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        startActivityForResult(mIntent, 20);
    }

//    public MD_Product productValidCheck() {
//        return productValidCheck(null);
//    }

    public MD_Product productValidCheck(String product_id) {
        String mProductId = null;

        if (product_id == null || product_id.isEmpty()) {
            mProductId = mket_product_id.getText().toString().trim();
        } else {
            mProductId = product_id;
        }

        MD_ProductDao productDao = new MD_ProductDao(getActivity());

        MD_Product md_product = productDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(getActivity()),
                        "",
                        product_id
                ).toSqlQuery()
        );

        return md_product;
    }

    public String searchProductInfo(String product_code, String product_id) {
        MD_ProductDao productDao = new MD_ProductDao(getActivity());

        MD_Product md_product = productDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(getActivity()),
                        product_code,
                        product_id
                ).toSqlQuery()
        );
        //
        if (md_product != null) {
            return md_product.getProduct_id();
        }
        //
        return "";
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("btn_enable_nfc");
        transListFrag.add("product_lbl");
        transListFrag.add("serial_lbl");
        transListFrag.add("tracking_lbl");
        transListFrag.add("btn_option_01");
        transListFrag.add("btn_option_02");
        transListFrag.add("btn_option_03");
        transListFrag.add("product_hint");
        transListFrag.add("serial_hint");
        transListFrag.add("tracking_hint");
        transListFrag.add("product_all_lbl");
        transListFrag.add("alert_no_product_ttl");
        transListFrag.add("alert_no_product_msg");
        transListFrag.add("alert_serial_pendencies_ttl");
        transListFrag.add("alert_serial_pendencies_msg");
        //
        return transListFrag;
    }

}
