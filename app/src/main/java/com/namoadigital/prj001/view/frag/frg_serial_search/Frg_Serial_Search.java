package com.namoadigital.prj001.view.frag.frg_serial_search;

import static com.namoadigital.prj001.extensions.StringHelperKt.removeLastCharEnterOrTab;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT005;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BarCode_Activity;
import com.namoa_digital.namoa_library.view.Ocr_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act081.Act081_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
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

    private TextInputLayout til_product;
    private TextView tv_product_id;
    private MKEditTextNM mket_product_id;
    private String productId = "";
    private MaterialButton iv_product_id;
    private ConstraintLayout ll_product_id;
    private ConstraintLayout cl_bottom_buttons;
    private TextView tv_serial;
    private LinearLayout ll_serial;

    private ConstraintLayout constraintLayout_jump_edit;
    private TextInputLayout til_serial;
    private TextInputLayout til_tracking;
    private MKEditTextNM mket_serial;
    private TextView tv_tracking;
    private TextInputLayout ll_tracking;
    private MKEditTextNM mket_tracking;

    private HMAux hmAux_Trans;
    private boolean showHint;
    private boolean showTree = false;
    private boolean showAll = false;

    private boolean bTokenPendenciesCheck = true;

    private SwitchCompat sw_hide_serial_info;

    private TextView tv_hide_serial_info;

    private MaterialButton btn_option_01;
    private MaterialButton btn_option_02;
    private MaterialButton btn_option_03;
    private MaterialButton btn_option_04;
    private MaterialButton btn_option_05;
    private LinearLayout ll_btn_option_04;

    private LinearLayout ll_options;

    private ButtonNFC btn_nfc_reader;

    private Frg_Serial_Search_Presenter mPresenter;
    private On_Frg_Serial_Search mFragListener;
    private On_Frg_Serial_Search.onProductSelectionReturnListener mFragOnProductSelectionReturnListener;
    private On_Frg_Serial_Search.onProductTypingListener mFragOnProductTypingListener;
    //LUCHE - 08/06/2021 - Var que define se deve performar o click ao retornar do barcode.
    private boolean performClickOnEspecialistReturn = true;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            btn_nfc_reader.setOnClickListener(clickListener);
        }
    }

    public void setbTokenPendenciesCheck(boolean bTokenPendenciesCheck) {
        this.bTokenPendenciesCheck = bTokenPendenciesCheck;
    }

    public void handleChkForHideSerialInfo(boolean status) {
        sw_hide_serial_info.setChecked(status);
        sw_hide_serial_info.setVisibility(View.VISIBLE);
        tv_hide_serial_info.setVisibility(View.VISIBLE);
    }

    public void allowChkForHideSerialInfo(boolean status) {
        sw_hide_serial_info.setEnabled(status);
    }

    public void setPerformClickOnEspecialistReturn(boolean performClickOnEspecialistReturn) {
        this.performClickOnEspecialistReturn = performClickOnEspecialistReturn;
    }

    public boolean isbTokenPendenciesCheck() {
        return bTokenPendenciesCheck;
    }

    public void setSupportNFC(boolean supportNFC) {
        this.supportNFC = supportNFC;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (supportNFC) {
                btn_nfc_reader.setVisibility(View.VISIBLE);
            } else {
                btn_nfc_reader.setVisibility(View.GONE);
            }
        }
    }

    private I_Frg_Serial_Search delegate;

    public void callBarCodeActivity() {
        mket_serial.setText("");
        Intent intent;
        boolean isOcrEnabled = ToolBox.isOcrPackageInstalled(getContext());
        boolean useOcrActivity = mket_serial.ismOCR() && isOcrEnabled;

        if (useOcrActivity) {
            intent = new Intent(getActivity(), Ocr_Activity.class);
        } else {
            intent = new Intent(getActivity(), BarCode_Activity.class);
            intent.putExtra(BarCode_Activity.CUSTOM_ON_BACK_PRESSED, ACT005);
        }

        intent.putExtra(ConstantBase.B_C_O_N_ID, mket_serial.getId());
        intent.putExtra(ConstantBase.PREFERENCES_UI_TYPE, mket_serial.getmRType());
        startActivity(intent);
    }

    public interface I_Frg_Serial_Search {

        void onSearchClick(String btn_Action, HMAux optionsInfo);

    }

    private I_Frg_Serial_Search_Load load_delegate;

    public interface I_Frg_Serial_Search_Load {
        /**
         * Interface disparada após rodar Setar as views.
         */
        void onFragIsReady();
    }

    public void setOnSearchClickListener(I_Frg_Serial_Search delegate) {
        this.delegate = delegate;
    }

    public void setLoad_delegate(I_Frg_Serial_Search_Load load_delegate) {
        this.load_delegate = load_delegate;
    }

    private ArrayList<MKEditTextNM> controls_sta;
    private View.OnClickListener clickListener;
    private boolean supportNFC;
    private boolean forceExactSearch = false;
    private MKEditTextNM.IMKEditTextChangeText mketSerialTextChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if (context instanceof On_Frg_Serial_Search) {
            mFragListener = (On_Frg_Serial_Search) context;
        }
        //LUCHE - 10/11/2020
        //Tenta inicializar interface via context da act.
        if (context instanceof On_Frg_Serial_Search.onProductSelectionReturnListener) {
            mFragOnProductSelectionReturnListener = (On_Frg_Serial_Search.onProductSelectionReturnListener) context;
        }
        //LUCHE - 13/11/2020
        //Inicializa interface via context da act
        if (context instanceof On_Frg_Serial_Search.onProductSelectionReturnListener) {
            mFragOnProductTypingListener = (On_Frg_Serial_Search.onProductTypingListener) context;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_serial_search, container, false);
        mPresenter = new Frg_Serial_Search_Presenter(getContext());
        iniVar(view);

        if (load_delegate != null) {
            load_delegate.onFragIsReady();
        }

        iniAction();

        return view;
    }

    private void iniVar(View view) {

        controls_sta = new ArrayList<>();
        //
        ll_options = (LinearLayout) view.findViewById(R.id.frg_serial_search_ll_options);
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            btn_nfc_reader = (ButtonNFC) view.findViewById(R.id.frg_serial_search_btn_nfc_reader);
            btn_nfc_reader.setmCustomer_code(String.valueOf(ToolBox_Con.getPreference_Customer_Code(getContext())));
            btn_nfc_reader.setmProduct(true);
            btn_nfc_reader.setmSerial(true);
            btn_nfc_reader.setmProgressClose(true);
        }
        //
        til_product = view.findViewById(R.id.frg_serial_search_til_prod);
        tv_product_id = (TextView) view.findViewById(R.id.frg_serial_search_tv_product_id);
        mket_product_id = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_product_id);
        iv_product_id = view.findViewById(R.id.frg_serial_search_iv_product_id);
        ll_product_id = view.findViewById(R.id.frg_serial_search_ll_product_id);
        //
        controls_sta.add(mket_product_id);
        //
        tv_serial = (TextView) view.findViewById(R.id.frg_serial_search_tv_serial);
        til_serial = view.findViewById(R.id.frg_serial_search_til_serial);
        til_tracking = view.findViewById(R.id.frg_serial_search_til_tracking);
        mket_serial = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_serial);
        //07/01/18 - Luche
        //Nos campos mket referentes a serial, o valores de mOcr e mBarcode serão preenchidos
        //via parametro do profile.
        mket_serial.setmBARCODE(
                ToolBox_Inf.profileExists(
                        getActivity(),
                        Constant.PROFILE_MENU_PROFILE,
                        Constant.PROFILE_MENU_PROFILE_SERIAL_BARCODE
                )
        );
        //

        mket_serial.setmOCR(ToolBox_Inf.profileExists(
                getActivity(),
                Constant.PROFILE_MENU_PROFILE,
                Constant.PROFILE_MENU_PROFILE_SERIAL_OCR_MOSOLF
        ));
        controls_sta.add(mket_serial);
        //
        tv_tracking = (TextView) view.findViewById(R.id.frg_serial_search_tv_tracking);
        ll_tracking = view.findViewById(R.id.frg_serial_search_til_tracking);
        mket_tracking = (MKEditTextNM) view.findViewById(R.id.frg_serial_search_mket_tracking);
        controls_sta.add(mket_tracking);
        //
        sw_hide_serial_info = view.findViewById(R.id.frg_serial_search_chk_hide_serial_info);
        tv_hide_serial_info = view.findViewById(R.id.frg_serial_search_tv_hide_serial_info);
        //LUCHE - 10/01/2020
        constraintLayout_jump_edit = view.findViewById(R.id.frg_serial_search_clayout);
        configChk();
        //
        cl_bottom_buttons = view.findViewById(R.id.bottom_bar_layout);
        til_serial = view.findViewById(R.id.frg_serial_search_til_serial);
        btn_option_01 = view.findViewById(R.id.frg_serial_search_btn_option_01);
        btn_option_02 = view.findViewById(R.id.frg_serial_search_btn_option_02);
        btn_option_03 = view.findViewById(R.id.frg_serial_search_btn_option_03);
        btn_option_04 = view.findViewById(R.id.frg_serial_search_btn_option_04);
        btn_option_05 = view.findViewById(R.id.frg_serial_search_btn_option_05);
        //
        ll_btn_option_04 = (LinearLayout) view.findViewById(R.id.frg_serial_search_ll_btn_option_04);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (supportNFC) {
                btn_nfc_reader.setVisibility(View.VISIBLE);

            } else {
                btn_nfc_reader.setVisibility(View.GONE);
            }
        }
    }

    public void setVisibilityBtnOption01(int visibility) {
        btn_option_01.setVisibility(visibility);
        chekBottomButtonsVisibility();
    }

    private void chekBottomButtonsVisibility() {
        cl_bottom_buttons.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (btn_nfc_reader.getVisibility() == View.VISIBLE || btn_option_01.getVisibility() == View.VISIBLE) {
                cl_bottom_buttons.setVisibility(View.VISIBLE);
            }
        } else {
            if (btn_option_01.getVisibility() == View.VISIBLE) {
                cl_bottom_buttons.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * LUCHE - 10/01/2020
     * <p>
     * Consolidado logica de configuração do checkbox de hide serial
     * Modificado logica para que, caso não exista nenhum do profiles de hide serial, o valor da
     * preferencia seja resetado para false.
     */
    private void configChk() {
        if (mFragListener.hasHideSerialInfoChk()) {
            if (!mPresenter.getProfileForHideSerialInfo()
                    && !mPresenter.getProfileForceNotShowSerialInfo()) {
                sw_hide_serial_info.setVisibility(View.GONE);
                tv_hide_serial_info.setVisibility(View.GONE);
                sw_hide_serial_info.setChecked(false);
                mPresenter.setChkForHideSerialInfoPreference(false);
            } else {
                if (mPresenter.getProfileForHideSerialInfo()) {
                    sw_hide_serial_info.setVisibility(View.VISIBLE);
                    tv_hide_serial_info.setVisibility(View.VISIBLE);
                }
                sw_hide_serial_info.setChecked(mPresenter.getChkForForceNotShowSerialInfo());
            }
            //Se profile force not show, desabilita o chk
            sw_hide_serial_info.setEnabled(!mPresenter.getProfileForceNotShowSerialInfo());
        } else {
            sw_hide_serial_info.setVisibility(View.GONE);
            tv_hide_serial_info.setVisibility(View.GONE);
        }
        constraintLayout_jump_edit.setVisibility(tv_hide_serial_info.getVisibility());
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
        btn_option_04.setOnClickListener(btnActionListener);
        btn_option_05.setOnClickListener(btnActionListener);

        mket_serial.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
            @Override
            public void reportTextBySpecialist(String s) {
                //LUCHE - 27/10/2020
                //Seta var q define busca exata
                forceExactSearch = true;
                //LUCHE - 08/06/2021
                //Verifica se deve forçar clique apos leitura do barcode. Comportamento padrão
                if (performClickOnEspecialistReturn) {
                    btn_option_01.performClick();
                }
            }
        });
        //Interface que identifica digitação no campo e reseta var forceExactSearch
        mketSerialTextChangeListener = new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                forceExactSearch = false;
            }
        };
        mket_serial.setOnReportTextChangeListner(mketSerialTextChangeListener);
        //
        mket_product_id.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
                //
            }

            @Override
            public void reportTextChange(String text, boolean hasText) {
                if (mFragOnProductTypingListener != null) {
                    mFragOnProductTypingListener.onProductTyping(
                            text.trim()
                    );
                }

            }
        });
        //LUCHE - 10/01/2020
        //Adicionado set da preferencia ao trocar o valor do checkbox.
        sw_hide_serial_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setChkForHideSerialInfoPreference(isChecked);
            }
        });
    }


    private void checkRulesForHintSerial(boolean hasText) {
        if (hasText) {
            MD_Product mdProduct = productValidCheck(productId);
            //
            if (mdProduct != null) {
                setSerialRule(
                        mdProduct.getSerial_rule(),
                        mdProduct.getSerial_min_length(),
                        mdProduct.getSerial_max_length()
                );
            } else {
                setSerialRule(null, null, null);
            }
        } else {
            setSerialRule(null, null, null);
        }
    }

    /**
     * LUCHE - 07/11/2019
     * mMetodo qu seta descrição do produto no textHelper
     *
     * @param productDesc
     */
    private void setProductDesc(String productDesc) {
        til_product.setHelperText(productDesc);
    }

    public boolean isForceExactSearch() {
        return forceExactSearch;
    }

    private View.OnClickListener btnActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String btnAction = null;

            if (v.getId() == R.id.frg_serial_search_btn_option_01) {
                btnAction = BTN_OPTION_01;
            } else if (v.getId() == R.id.frg_serial_search_btn_option_02) {
                btnAction = BTN_OPTION_02;
            } else if (v.getId() == R.id.frg_serial_search_btn_option_03) {
                btnAction = BTN_OPTION_03;
            } else if (v.getId() == R.id.frg_serial_search_btn_option_04) {
                btnAction = BTN_OPTION_04;
            } else if (v.getId() == R.id.frg_serial_search_btn_option_05) {
                btnAction = BTN_OPTION_05;
            } else {
                btnAction = "";
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

                } else if (ToolBox_Con.isOnline(getActivity())
                        && ToolBox_Inf.checkSerialTokenURStatus(getActivity()) && isbTokenPendenciesCheck()
                        && ((getActivity() instanceof Act006_Main || getActivity() instanceof Act081_Main)
                        && !ToolBox_Con.getBooleanPreferencesByKey(getContext(), ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW, false)
                )
                ) {
                    ToolBox.alertMSG(
                            getActivity(),
                            hmAux_Trans.get("alert_serial_pendencies_ttl"),
                            hmAux_Trans.get("alert_serial_pendencies_msg"),
                            null,
                            0
                    );
                } else {
                    //LUCHE - 10/01/2020
                    //Comentado o trecho de codigo pois agora a preferencia é salva
                    //assim que o status do checkbox é alterado.
                    /*if (mFragListener.hasHideSerialInfoChk()) {
                        mPresenter.setChkForHideSerialInfoPreference(chk_hide_serial_info.isChecked());
                    }*/
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            btn_nfc_reader.setText(text.toString());
        }
    }

    public void setProductIdText(String text, String productId) {
        //
        mket_product_id.setText(text.toString());
        setProductId(productId);
        setProductIdHint("");
    }

    public void setProductId(String productId) {
        this.productId = productId;
        checkRulesForHintSerial(!mket_product_id.getText().toString().isEmpty() || !productId.isEmpty());
    }

    public String getProductId() {
        return productId;
    }

    public void setProductIdHint(String hint) {
        if (mket_product_id.getText().toString().isEmpty()) {
            til_product.setHint(hmAux_Trans.get("product_all_lbl"));
        } else {
            til_product.setHint(hmAux_Trans.get("product_contain_id_lbl"));
        }
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
        //btn_option_01.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(status)));
    }

    public void setBtn_Option_01_Visibility(int status) {
        btn_option_01.setVisibility(status);
    }

    public void setBtn_Option_02_Label(String label) {
        btn_option_02.setText(label);
    }

    public void setBtn_Option_02_BackGround(int status) {
        // btn_option_02.setBackgroundColor(ContextCompat.getColor(getActivity(), status));
    }

    public void setBtn_Option_02_Visibility(int status) {
        btn_option_02.setVisibility(status);
    }

    public void setBtn_Option_03_Label(String label) {
        btn_option_03.setText(label);
    }

    public void setBtn_Option_03_BackGround(int status) {
        // btn_option_03.setBackgroundColor(ContextCompat.getColor(getActivity(), status));
    }

    public void setBtn_Option_03_Visibility(int status) {
        btn_option_03.setVisibility(status);
    }

    public void setBtn_Option_04_Label(String label) {
        btn_option_04.setText(label);
    }

    public void setBtn_Option_04_BackGround(int status) {
        //  btn_option_04.setBackgroundColor(ContextCompat.getColor(getActivity(), status));
    }

    public void setBtn_Option_04_Visibility(int status) {
        ll_btn_option_04.setVisibility(status);
        //btn_option_04.setVisibility(status);
    }

    public void setBtn_Option_05_Label(String label) {
        btn_option_05.setText(label);
    }

    public void setBtn_Option_05_BackGround(int status) {
        // btn_option_05.setBackground(getActivity().getDrawable(status));
    }

    public void setBtn_Option_05_Visibility(int status) {
        btn_option_05.setVisibility(status);
    }

    public HMAux getHMAuxValues() {
        HMAux values = new HMAux();
        values.put(PRODUCT_ID, (mket_product_id.getText().toString().trim().isEmpty()) ? "" : productId.trim());
        //LUCHE - 27/10/2020
        //Como o setOnReportTextChangeListner reseta a var de busca exata, foi removido o listener nesse momento.
        String serial_id = removeLastCharEnterOrTab(mket_serial.getText().toString().trim()).toUpperCase();
        mket_serial.setOnReportTextChangeListner(null);
        mket_serial.setText(serial_id);
        mket_serial.setOnReportTextChangeListner(mketSerialTextChangeListener);
        //
        values.put(SERIAL, serial_id.trim().isEmpty() ? "" : serial_id/*ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim().isEmpty() ? "" : mket_serial.getText().toString().trim())*/);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            btn_nfc_reader.setText(hmAux_Trans.get("btn_enable_nfc"));
        }
        tv_product_id.setText(hmAux_Trans.get("product_lbl"));
        til_serial.setHint(hmAux_Trans.get("serial_lbl"));
        til_tracking.setHint(hmAux_Trans.get("tracking_lbl"));
        tv_hide_serial_info.setText(hmAux_Trans.get("chk_hide_serial_info_lbl"));
        til_serial.setHint(hmAux_Trans.get("serial_hint"));
        //
/*        if (showHint) {
            til_product.setHint(hmAux_Trans.get("product_hint"));
            til_tracking.setHint(hmAux_Trans.get("tracking_hint"));
        } else {
            til_serial.setHint(hmAux_Trans.get("serial_hint"));
            til_tracking.setHint(hmAux_Trans.get("tracking_hint"));
        }*/
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

    public void setShowHideTracking(boolean status) {
        if (status) {
            ll_tracking.setVisibility(View.VISIBLE);
        } else {
            ll_tracking.setVisibility(View.GONE);
        }
    }

    private void customSettings() {
        if (showTree) {
            iv_product_id.setVisibility(View.VISIBLE);
        } else {
            if (showAll) {
                iv_product_id.setVisibility(View.VISIBLE);
            } else {
                iv_product_id.setVisibility(View.GONE);
            }
        }
        //LUCHE - 07/11/2019
        //Chama metodo que define a visibilidade do textHelper do produto.
        setProductHelperVisibility();
    }


    public void setLl_options(View ll_options) {
        this.ll_options.addView(ll_options);
    }

    public ArrayList<MKEditTextNM> getControlsSta() {
        return controls_sta;
    }

    public void cleanFields() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            btn_nfc_reader.setText(hmAux_Trans.get("btn_enable_nfc"));
        }
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
            if (data.getBooleanExtra(Constant.ACT_PRODUCT_SELECTION_PRODUCT_ALL_PRODUCT, false)) {
                mket_product_id.getText().clear();
                til_product.setHint(hmAux_Trans.get("product_all_lbl"));
                productId = "";
                checkRulesForHintSerial(false);
                return;
            }

            MD_Product pAux = (MD_Product) data.getSerializableExtra(MD_Product.class.getName());
            //LUCHE - 10/11/2020
            //Ao retornar da seleção de produto, dispara interface com valor do product id atual + o
            //retornado.
            if (mFragOnProductSelectionReturnListener != null) {
                mFragOnProductSelectionReturnListener.onProductSelectionReturn(
                        mket_product_id.getText().toString().trim(),
                        pAux.getProduct_id()
                );
            }
            til_product.setHint(hmAux_Trans.get("product_contain_id_lbl"));
            mket_product_id.setText(String.valueOf(pAux.getProduct_desc()));
            productId = pAux.getProduct_id();
            checkRulesForHintSerial(productId != null || !productId.isEmpty());
            //LUCHE - 08/11/2019
            //Comentado o codigo abaixo pois, o setText acima ja dispara a chamada
            //do metodo setSerialRule().
            /*setSerialRule(
                pAux.getSerial_rule(),
                pAux.getSerial_min_length() ,
                pAux.getSerial_max_length()
            );*/
        } else {
        }
    }

    public void callAct_Product_Selection(Context context) {
        Intent mIntent = new Intent(context, Act_Product_Selection.class);
        //
        Bundle bundle = new Bundle();
        //

        bundle.putBoolean(Constant.ACT_PRODUCT_SELECTION_PRODUCT_ALL_PRODUCT, !mket_product_id.getText().toString().isEmpty());
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
            mProductId = productId;
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

    /**
     * LUCHE - 14/11/18
     * Adicionando metodo para que a validação a "quebra de leitura de vin" fosse possivel.
     * Necessidade MOSOLF
     * Seta regra de validação no campo.
     * Seta também variaveis para ignorarem mim e max e validação de vin após leitura de barcode
     * para true;
     * <p>
     * LUCHE - 07/11/2019
     * Modificado metodo , adicionando campos de min e max que serão usado para definição do
     * textHelper do serial
     *
     * @param serial_rule
     * @param min
     * @param max
     */
    public void setSerialRule(String serial_rule, Integer min, Integer max) {
        mket_serial.setmInputTypeValidator(serial_rule);
        mket_serial.setmIgnoreVINValidationOnRead(true);
        mket_serial.setmIgnoreMaxMinSize(true);
        setSerialHelper(serial_rule, min, max);
    }

    /**
     * LUCHE - 07/11/2019
     * <p>
     * Metodo que seta no textHelper as regras do produto.
     * <p>
     * A validação helper != null é uma gambiarra para corrigir o que parece ser um problema
     * de "redesenho" da espaço do textHelper.
     * A correção foi necessaria pois, quando o produto é selecionado via act de seleção de produto,
     * ao setar o valor no textHelper, o topo do texto ficava "comido", como se tivesse por baixo
     * do Mket do serial.
     * <p>
     * LUCHE - 08/11/2019
     * Removido a gambiarra de desabilitar e abilitar o textHelper, pois
     * descobrimos que o que gerava o "bug" era a segunda chamada do metodo
     * setSerialRule dentro do metodo processResult()
     *
     * @param serial_rule - Regra do Serial ou null se não houver
     * @param min         - Qtd Min de caracteres ou null se não houver
     * @param max         - Qtd Max de caracteres ou null se não houver
     */
    private void setSerialHelper(String serial_rule, Integer min, Integer max) {
        //Chama metodo que retorna texto ja formatado
        til_serial.setHelperText(
                mPresenter.getFormattedRuleHelper(
                        hmAux_Trans,
                        serial_rule,
                        min,
                        max
                )
        );
    }

    /**
     * LUCHE - 07/11/2019
     * Metodo que verifica se user possui acesso apenas um produto
     * e se for ocaso, removeFull a descrição do produto.
     * Regra maluca
     */
    private void setProductHelperVisibility() {
        if (!showTree && !showAll) {
            til_product.setHelperTextEnabled(false);
        }
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
        transListFrag.add("product_contain_id_lbl");
        transListFrag.add("alert_no_product_ttl");
        transListFrag.add("alert_no_product_msg");
        transListFrag.add("alert_serial_pendencies_ttl");
        transListFrag.add("alert_serial_pendencies_msg");
        transListFrag.add("chk_hide_serial_info_lbl");
        //
        transListFrag.add("serial_rule_lbl");
        transListFrag.add("serial_min_length_lbl");
        transListFrag.add("serial_min_max_separator_lbl");
        transListFrag.add("serial_max_length_lbl");
        //
        return transListFrag;
    }

    public boolean lastMketIsSerial(Integer id) {
        return mket_serial.getId() == id;
    }

    public void flowBarcode() {
        callBarCodeActivity();
    }
}
