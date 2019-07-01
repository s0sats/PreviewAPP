package com.namoadigital.prj001.ui.act058.frag;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.ctls.TextViewCT;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Class;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag_Move_Create.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag_Move_Create#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_Move_Create extends BaseFragment implements Frag_Move_Create_Contract.I_View {

    private static final String VIEW_PARAM = "view_param";
    public static final String ORIGIN_PARAM = "ORIGIN";
    public static final String HMAUX_TRANS = "hmaux_trans";
    public static final String DATE_FORMAT_MKDATE = "yyyy-MM-dd HH:mm:ss Z";

    private int view_param;
    private boolean fromMove;
    private MD_Product_Serial mdProductSerial;
//region UI Elements
    private View serialLayout;
    private ScrollView sv_create_move;
    private LinearLayout ll_tracking_content;
    private TextView tv_zone_position;
    private TextView tv_inbound_lbl;
    private TextView tv_outbound_lbl;
    private TextView tv_move_order_lbl;
    private TextView tv_move_to_lbl;
    private TextView tv_inbound_val;
    private TextView tv_outbound_val;
    private TextView tv_move_order_val;
    private TextView tv_move_to_val;
    private TextView tv_class_lbl;
    private MKEditTextNM mkedit_coments;
    private MKEditTextNM mket_serial;
    private MkDateTime mkdate_confirm;
    private TextViewCT.ITextViewCT tvCtListner;
    private SearchableSpinner ss_zone;
    private SearchableSpinner ss_local;
    private SearchableSpinner ss_reason;
    private SearchableSpinner ss_class;
    private CheckBox chk_change_zone;
    private TextView tv_product_cod_val;
    private TextView tv_serial_lbl;
    private TextView tv_serial_val;
    private ImageView iv_offline_mode;
    private ImageView iv_serial_history;
    private ImageView iv_add_tracking;
    private ImageView iv_class_icon;
//endregion

//region data
    private Integer to_local_code;
    private Integer to_zone_code;
    private int move_prefix;
    private int move_code;
    private Integer reason_code;
    private String move_type;
    private Integer planned_zone_code;
    private Integer outbound_prefix;
    private Integer inbound_prefix;
    private Integer outbound_code;
    private Integer inbound_code;
    private Integer planned_local_code;
    private String status;
    private Integer to_class_code;
//endregion

    private Frag_Move_Create_Presenter mPresenter;
    private OnFragmentInteractionListener mListener;
    private boolean useTracking;
    private ArrayList<MD_Product_Serial_Tracking> tracking_list;
    private boolean pausedByScan;
    private String searched_tracking = "";
    private boolean trackingListChanged;
    private Button btn_save;
    private List<IO_Move_Tracking> trackingFromMove =new ArrayList<>();


    public Frag_Move_Create() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param serialInfo
     * @param viewParam        Controle de visualizacao de elementos, 0 = oculta Comentario e data
     *                         1 = oculta Reason.
     * @param originParam      Controla visualizacao do checkbox para mudanca de zona.
     * @param hmAux_Trans_Frag
     * @return A new instance of fragment Frag_Move_Create.
     */
    public static Frag_Move_Create newInstance(MD_Product_Serial serialInfo, int viewParam, boolean originParam, HMAux hmAux_Trans_Frag, Integer to_local_code, Integer to_zone_code, int move_prefix, int move_code, Integer reason_code, String move_type, Integer planned_zone_code,Integer outbound_prefix, Integer inbound_prefix, Integer outbound_code, Integer inbound_code, Integer planned_local_code, String status, Integer to_class_code) {
        Frag_Move_Create fragment = new Frag_Move_Create();
        Bundle args = new Bundle();
        args.putSerializable(MD_Product_SerialDao.TABLE, serialInfo);
        args.putSerializable(HMAUX_TRANS, hmAux_Trans_Frag);
        args.putInt(VIEW_PARAM, viewParam);
        args.putBoolean(ORIGIN_PARAM, originParam);
        args.putSerializable(IO_MoveDao.TO_LOCAL_CODE,to_local_code);
        args.putSerializable(IO_MoveDao.TO_ZONE_CODE,to_zone_code);
        args.putInt(IO_MoveDao.MOVE_PREFIX,move_prefix);
        args.putInt(IO_MoveDao.MOVE_CODE,move_code);
        args.putSerializable(IO_MoveDao.REASON_CODE,reason_code);
        args.putString(IO_MoveDao.MOVE_TYPE,move_type);
        args.putSerializable(IO_MoveDao.PLANNED_ZONE_CODE,planned_zone_code);
        args.putSerializable(IO_MoveDao.OUTBOUND_PREFIX,outbound_prefix);
        args.putSerializable(IO_MoveDao.INBOUND_PREFIX,inbound_prefix);
        args.putSerializable(IO_MoveDao.OUTBOUND_CODE,outbound_code);
        args.putSerializable(IO_MoveDao.INBOUND_CODE,inbound_code);
        args.putSerializable(IO_MoveDao.PLANNED_LOCAL_CODE,planned_local_code);
        args.putString(IO_MoveDao.STATUS,status);
        args.putSerializable(IO_MoveDao.TO_CLASS_CODE,to_class_code);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromMove = getArguments().getBoolean(ORIGIN_PARAM);
            view_param = getArguments().getInt(VIEW_PARAM);
            mdProductSerial = (MD_Product_Serial) getArguments().getSerializable(MD_Product_SerialDao.TABLE);
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String,String>) getArguments().getSerializable(HMAUX_TRANS));
            to_local_code = (Integer) getArguments().getSerializable(IO_MoveDao.TO_LOCAL_CODE);
            to_zone_code = (Integer) getArguments().getSerializable(IO_MoveDao.TO_ZONE_CODE);
            move_prefix = getArguments().getInt(IO_MoveDao.MOVE_PREFIX);
            move_code = getArguments().getInt(IO_MoveDao.MOVE_CODE);
            reason_code = (Integer) getArguments().getSerializable(IO_MoveDao.REASON_CODE);
            move_type = getArguments().getString(IO_MoveDao.MOVE_TYPE);
            planned_zone_code = (Integer) getArguments().getSerializable(IO_MoveDao.PLANNED_ZONE_CODE);
            outbound_prefix = (Integer) getArguments().getSerializable(IO_MoveDao.OUTBOUND_PREFIX);
            inbound_prefix = (Integer) getArguments().getSerializable(IO_MoveDao.INBOUND_PREFIX);
            outbound_code = (Integer) getArguments().getSerializable(IO_MoveDao.OUTBOUND_CODE);
            inbound_code = (Integer) getArguments().getSerializable(IO_MoveDao.INBOUND_CODE);
            planned_local_code = (Integer) getArguments().getSerializable(IO_MoveDao.PLANNED_LOCAL_CODE);
            status = getArguments().getString(IO_MoveDao.STATUS);
            to_class_code = (Integer) getArguments().getSerializable(IO_MoveDao.TO_CLASS_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.act058_frag_move, container, false);
        mPresenter = new Frag_Move_Create_Presenter(this, getContext(), to_local_code, to_zone_code, move_prefix, move_code, reason_code, move_type, planned_zone_code, status);
        bindViews(fragView);
        initializeViews();
        initAction();
        return fragView;
    }

    private void bindViews(View fragView) {
        tv_zone_position = fragView.findViewById(R.id.act058_tv_zone_position);
        tv_inbound_val = fragView.findViewById(R.id.act058_tv_inbound_val);
        tv_outbound_val = fragView.findViewById(R.id.act058_tv_outbound_val);
        tv_move_order_val = fragView.findViewById(R.id.act058_tv_move_order_val);
        tv_move_to_val = fragView.findViewById(R.id.act058_tv_move_to_val);
        tv_inbound_lbl = fragView.findViewById(R.id.act058_tv_inbound_lbl);
        tv_outbound_lbl = fragView.findViewById(R.id.act058_tv_outbound_lbl);
        tv_move_order_lbl = fragView.findViewById(R.id.act058_tv_move_order_lbl);
        tv_move_to_lbl = fragView.findViewById(R.id.act058_tv_move_to_lbl);
        tv_class_lbl = fragView.findViewById(R.id.act058_tv_class_ttl);
        ll_tracking_content = fragView.findViewById(R.id.ll_tracking_content);
        iv_add_tracking = fragView.findViewById(R.id.act058_iv_add_tracking);
        iv_class_icon = fragView.findViewById(R.id.act058_iv_class_icon);
        btn_save = fragView.findViewById(R.id.act058_btn_save);
        mket_serial = fragView.findViewById(R.id.act058_mket_serial);
        ss_zone = fragView.findViewById(R.id.act058_ss_zone);
        ss_local = fragView.findViewById(R.id.act058_ss_local);
        mkedit_coments = fragView.findViewById(R.id.act058_mkedit_coments);
        mkdate_confirm = fragView.findViewById(R.id.act058_mkdate_confirm);
        sv_create_move = fragView.findViewById(R.id.act058_sv_create_move);
        ss_reason = fragView.findViewById(R.id.act058_ss_reason);
        ss_class = fragView.findViewById(R.id.act058_ss_class);
        chk_change_zone = fragView.findViewById(R.id.act058_chk_change_zone);
        serialLayout = fragView.findViewById(R.id.act058_serial_layout);
        tv_product_cod_val = serialLayout.findViewById(R.id.tv_product_cod_desc);
        tv_serial_lbl = serialLayout.findViewById(R.id.tv_serial_lbl);
        tv_serial_val = serialLayout.findViewById(R.id.tv_serial_val);
        iv_offline_mode = serialLayout.findViewById(R.id.iv_offline_mode);
        iv_serial_history = serialLayout.findViewById(R.id.iv_serial_history);
    }

    private void initAction() {

        iv_add_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTracking) {
                    showTrackingDialog();
                }
            }
        });


        iv_serial_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolBox_Con.isOnline(getContext())) {
                    callLogAct();
                } else {
                    ToolBox_Inf.showNoConnectionDialog(getContext());
                }
            }
        });

        ss_class.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                setClassIcon(hmAux);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    ToolBox.alertMSG_YES_NO(
                            getContext(),
                            hmAux_Trans.get("alert_update_move_ttl"),
                            hmAux_Trans.get("alert_update_move_confirm"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    persistIoMoveChanges();
                                }
                            },
                            1
                    );

                }
            }
        });

        ss_zone.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                processZoneValueChange(hmAux);
            }
        });

        ss_zone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
              @Override
              public void onItemPreSelected(HMAux hmAux) {

              }

              @Override
              public void onItemPostSelected(HMAux hmAux) {
                  processZoneValueChange(hmAux);
              }
          }
        );

        ss_local.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });

    }

    private void persistIoMoveChanges() {
        Integer classCode = null;
        Integer reasonCode = null;
        Integer zoneCode = Integer.valueOf(ss_zone.getmValue().get(SearchableSpinner.CODE));

        if (ss_class.getmValue() != null && ss_class.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            classCode = Integer.valueOf(ss_class.getmValue().get(SearchableSpinner.CODE));
        }

        if (ss_reason.getmValue() != null && ss_reason.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            reasonCode = Integer.valueOf(ss_reason.getmValue().get(SearchableSpinner.CODE));
        }

        if (chk_change_zone.isChecked()) {
            ToolBox_Con.setPreference_Zone_Code(
                    getContext(),
                    zoneCode
            );
        }
        switch (move_type){
            case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
            case ConstantBaseApp.IO_PROCESS_MOVE:
            case ConstantBaseApp.IO_INBOUND:
            case ConstantBaseApp.IO_OUTBOUND:
                mListener.persistIoMovePlanned(
                        ToolBox_Con.getPreference_Customer_Code(getContext()),
                        Integer.valueOf(ss_zone.getmValue().get(SearchableSpinner.CODE)),
                        Integer.valueOf(ss_local.getmValue().get(SearchableSpinner.CODE)),
                        classCode,
                        reasonCode,
                        mkedit_coments.getText().toString().trim(),
                        mkdate_confirm.getmValue(),
                        mdProductSerial,
                        trackingFromMove
                );
                break;
            case ConstantBaseApp.IO_PROCESS_IN_CONF:
            case ConstantBaseApp.IO_PROCESS_OUT_CONF:
                mListener.persistIoMovePlanned(
                        ToolBox_Con.getPreference_Customer_Code(getContext()),
                        Integer.valueOf(ss_zone.getmValue().get(SearchableSpinner.CODE)),
                        Integer.valueOf(ss_local.getmValue().get(SearchableSpinner.CODE)),
                        classCode,
                        reasonCode,
                        mkedit_coments.getText().toString().trim(),
                        mkdate_confirm.getmValue(),
                        mdProductSerial,
                        trackingFromMove
                );
                break;
        }
    }

    private void processLocalValueChange(HMAux hmAux) {
        if (!ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            mPresenter.loadZoneSS(ss_zone, false, true);
            //
            ToolBox_Inf.setSSmValue(
                    ss_zone,
                    hmAux.get(MD_Site_ZoneDao.ZONE_CODE),
                    hmAux.get(MD_Site_ZoneDao.ZONE_ID),
                    hmAux.get(MD_Site_ZoneDao.ZONE_DESC),
                    false
            );
            //
            mPresenter.loadLocalSS(ss_zone, ss_local, false);
        }
    }

    private boolean validateFields() {
        boolean isSuccessfully = true;
        switch (view_param) {
            case 0:
                if (ss_reason.getmValue() == null || !ss_reason.getmValue().hasConsistentValue(SearchableSpinner.ID)) {
                    ss_reason.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
                    isSuccessfully = false;
                } else {
                    ss_reason.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
                }
                break;
            case 1:
            case 2:
            case 3:
                mkdate_confirm.setmHighlightWhenInvalid(true);

                if (!mkdate_confirm.isValid()) {
                    mkdate_confirm.setmHighlightWhenInvalid(true);
                    isSuccessfully = false;
                }
                break;
        }

        if (ss_zone.getmValue() == null || !ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            ss_zone.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            isSuccessfully = false;
        } else {
            ss_zone.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        }

        if (ss_local.getmValue() == null || !ss_local.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            ss_local.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            isSuccessfully = false;
        } else {
            ss_local.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        }

        mket_serial.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));

        if (mket_serial.getVisibility() == View.VISIBLE) {
            if (!mdProductSerial.getSerial_id().equalsIgnoreCase(mket_serial.getText().toString().trim())) {
                isSuccessfully = false;
                mket_serial.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            }
        }

        return isSuccessfully;
    }

    private void callLogAct() {
        Intent logIntent = new Intent(getContext(), Act007_Main.class);
        //
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, mdProductSerial);
        logIntent.putExtras(bundle);
        //

        mListener.callLogAct(logIntent);

    }

    private void showTrackingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.namoa_dialog_add_tracking, null);

        final MKEditTextNM mket_tracking = (MKEditTextNM) view.findViewById(R.id.namoa_dialog_add_tracking_mket_tracking);

        if (mListener != null) {
            mListener.onAddOrRemoveControl(mket_tracking, true);
        }
        //
        TextView tv_tracking_ttl = (TextView) view.findViewById(R.id.namoa_dialog_add_tracking_tv_lbl);
        ImageView iv_action = (ImageView) view.findViewById(R.id.namoa_dialog_add_tracking_iv_action);
        final ImageView iv_close = (ImageView) view.findViewById(R.id.namoa_dialog_add_tracking_iv_close);
        //
        tv_tracking_ttl.setText(hmAux_Trans.get("dialog_tracking_ttl"));
        //setDelegateTextBySpecialist
        mket_tracking.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
            @Override
            public void reportTextBySpecialist(String s) {
                pausedByScan = true;
            }
        });
        builder.setView(view);
        builder.setCancelable(false);
        //
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //controls_sta.remove(mket_tracking);
                if (mListener != null) {
                    mListener.onAddOrRemoveControl(mket_tracking, false);
                }
            }
        });
        //
        final AlertDialog show = builder.show();
        /*
         *Ini Action
         */
        iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mket_text = mket_tracking.getText().toString().trim().toUpperCase();

                if (mket_text.length() > 0) {
                    if (!isTrackingListed(mket_text)) {
                        if (ToolBox_Con.isOnline(getContext())) {
                            ToolBox_Inf.closeKeyboard(getContext(), mket_tracking.getWindowToken());
                            //
                            searched_tracking = mket_text;
                            //
                            if (mListener != null) {
                                mListener.onTrackingSearchClick(
                                        mdProductSerial.getProduct_code(),
                                        mdProductSerial.getSerial_code(),
                                        mket_text,
                                        ToolBox_Con.getPreference_Site_Code(getContext())
                                );
                            }
                            //
                            show.dismiss();
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(getContext());
                        }
                    } else {
                        showAlertDialog(
                                hmAux_Trans.get("alert_tracking_already_listed_ttl"),
                                hmAux_Trans.get("alert_tracking_already_listed_msg"),
                                null);
                    }
                }
            }
        });
        //
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                show.dismiss();
            }
        });
    }

    public void processTrackingResult(HMAux auxResult) {
        if (auxResult.containsKey(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY)) {
            if (auxResult.get(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY).equals(WS_Serial_Tracking_Search.NOT_EXISTS)) {
                //
                mdProductSerial.getTracking_list().add(
                        buildTrackingObj(searched_tracking)
                );
                IO_Move_Tracking io_move_tracking = new IO_Move_Tracking();
                //
                io_move_tracking.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(getContext()));
                io_move_tracking.setMove_prefix(move_prefix);
                io_move_tracking.setMove_code(move_code);
                io_move_tracking.setTracking(searched_tracking);
                trackingFromMove.add(io_move_tracking);
                //
                appendTracking(searched_tracking);
                //
                setTrackingListChanged(true);
                //
                cleanSearched_tracking();
                //
                scrollToView(ll_tracking_content);
            } else {
                showAlertDialog(
                        hmAux_Trans.get("alert_tracking_unavailable_ttl"),
                        hmAux_Trans.get("alert_tracking_unavailable_msg"),
                        null);
            }
        }
    }

    //region Scrolls
    private void scrollToView(View view) {
        int x = (int) view.getX();
        int y = view.getTop() + ((View) view.getParent()).getTop();

        sv_create_move.smoothScrollTo(x, y);
    }

    private void setTrackingListChanged(boolean trackingListChanged) {
        this.trackingListChanged = trackingListChanged;
    }

    private MD_Product_Serial_Tracking buildTrackingObj(String searched_tracking) {
        MD_Product_Serial_Tracking auxTracking = new MD_Product_Serial_Tracking();
        //
        auxTracking.setTracking(searched_tracking);
        auxTracking.setPk(mdProductSerial);
        //
        return auxTracking;
    }

    public void appendTracking(String tracking) {
        TextViewCT viewCT = new TextViewCT(getContext());
        viewCT.setmValue(tracking);
        viewCT.setOnRemoveViewsListener(tvCtListner);
        //
        ll_tracking_content.addView(viewCT);
    }

    public void cleanSearched_tracking() {
        searched_tracking = "";
    }


    public boolean isTrackingListed(String tracking) {
        for (int i = 0; i < mdProductSerial.getTracking_list().size(); i++) {
            if (mdProductSerial.getTracking_list().get(i).getTracking().equals(tracking)) {
                return true;
            }
        }
        return false;
    }

    private void showAlertDialog(String title, String msg) {
        showAlertDialog(title, msg, null);
    }

    private void showAlertDialog(String title, String msg, @Nullable DialogInterface.OnClickListener positiveListner) {
        ToolBox.alertMSG(
                getContext(),
                title,
                msg,
                positiveListner,
                0
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        bindValues();
    }

    private void bindValues() {

        tv_product_cod_val.setText(mdProductSerial.getProduct_id() + " " + mdProductSerial.getProduct_desc());
        tv_serial_val.setText(mdProductSerial.getSerial_id());
        String zone_position = "";

        if(mdProductSerial.getZone_id() != null ) {
            zone_position = mdProductSerial.getZone_desc();
            if(mdProductSerial.getLocal_id() != null){
                zone_position = MessageFormat.format("{0} | {1}", zone_position, mdProductSerial.getLocal_id());
            }
        }
        if(zone_position.isEmpty()){
            tv_zone_position.setVisibility(View.GONE);
        }else{
            tv_zone_position.setVisibility(View.VISIBLE);
            tv_zone_position.setText(zone_position);
        }

        try {
            tv_inbound_val.setText(formatPrefixAndCode(inbound_prefix, inbound_code));
            tv_inbound_lbl.setVisibility(View.VISIBLE);
            tv_inbound_val.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            tv_inbound_lbl.setVisibility(View.GONE);
            tv_inbound_val.setVisibility(View.GONE);
        }

        try {
            tv_outbound_val.setText(formatPrefixAndCode(outbound_prefix, outbound_code));
            tv_outbound_lbl.setVisibility(View.VISIBLE);
            tv_outbound_val.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            tv_outbound_lbl.setVisibility(View.GONE);
            tv_outbound_val.setVisibility(View.GONE);
        }
        if(move_prefix>0 && move_code >0) {
            tv_move_order_val.setText(formatPrefixAndCode(move_prefix, move_code));
            tv_move_order_val.setVisibility(View.VISIBLE);
            tv_move_order_lbl.setVisibility(View.VISIBLE);
        }else{
            tv_move_order_lbl.setVisibility(View.GONE);
            tv_move_order_val.setVisibility(View.GONE);
        }

        String toZoneLocal = (planned_zone_code == null || planned_zone_code == -1 ) ? "" : mPresenter.getZoneDesc(planned_zone_code);
        toZoneLocal = (planned_local_code == null || planned_local_code == -1) ? toZoneLocal+"" : toZoneLocal + "|" +mPresenter.getLocalId(planned_local_code, planned_zone_code);

        if (toZoneLocal.isEmpty() || toZoneLocal.equals("-1")) {
            tv_move_to_lbl.setVisibility(View.GONE);
            tv_move_to_val.setVisibility(View.GONE);
        } else {
            tv_move_to_lbl.setVisibility(View.VISIBLE);
            tv_move_to_val.setVisibility(View.VISIBLE);
            tv_move_to_val.setText(toZoneLocal);
        }
    }

    @NonNull
    private String formatPrefixAndCode(int prefix, int code) {
        return prefix + "." + code;
    }

    private void initializeViews() {

        if (tvCtListner == null) {
            initializeTrackingRemovelistner();
        }
        //Remove todas as views do layout antes de começar.
        ll_tracking_content.removeAllViews();
        tracking_list = new ArrayList<>();

        useTracking = ToolBox_Con.getPreference_Customer_Uses_Tracking(getContext()) == 1;

        if (mListener.isOnline()) {
            iv_serial_history.setVisibility(View.VISIBLE);
            iv_offline_mode.setVisibility(View.GONE);
        } else {
            iv_serial_history.setVisibility(View.GONE);
            iv_offline_mode.setVisibility(View.VISIBLE);
        }

        //Insere lista de tracking vindo do banco.
        if (to_class_code != null && status.equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)) {
            trackingFromMove.clear();
            trackingFromMove = mPresenter.getTrackingFromMove();
            for (int i = 0; i < trackingFromMove.size(); i++) {
                appendTracking(trackingFromMove.get(i).getTracking());
            }
        } else {
            for (int i = 0; i < mdProductSerial.getTracking_list().size(); i++) {
                appendTracking(mdProductSerial.getTracking_list().get(i).getTracking());
                IO_Move_Tracking io_move_tracking = new IO_Move_Tracking();
                //
                io_move_tracking.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(getContext()));
                io_move_tracking.setMove_prefix(move_prefix);
                io_move_tracking.setMove_code(move_code);
                io_move_tracking.setTracking(mdProductSerial.getTracking_list().get(i).getTracking());
                trackingFromMove.add(io_move_tracking);
            }
        }
        setClassSS();
        setLabelsAndHint();
        setMkdate();

        if (mPresenter.hasSerialBypass()) {
            mket_serial.setVisibility(View.GONE);
        } else {
            setSerialMKEditText();
        }

        setSSMoveReason();
        setViewEnable();

        switch (view_param) {
            case 0:
                ss_reason.setVisibility(View.VISIBLE);
                mkedit_coments.setVisibility(View.GONE);
                mkdate_confirm.setVisibility(View.GONE);
                break;
            case 1:
                ss_reason.setVisibility(View.GONE);
                mkedit_coments.setVisibility(View.GONE);
                mkdate_confirm.setVisibility(View.GONE);
                break;
            case 2:
                ss_reason.setVisibility(View.GONE);
                mkedit_coments.setVisibility(View.VISIBLE);
                mkdate_confirm.setVisibility(View.VISIBLE);
                break;
            case 3:
                ss_reason.setVisibility(View.GONE);
                ss_zone.setmEnabled(false);
                ss_local.setmEnabled(false);
                mkedit_coments.setVisibility(View.VISIBLE);
                mkdate_confirm.setVisibility(View.VISIBLE);
                break;
        }

        mListener.onAddOrRemoveControlSS(ss_zone, true);
        mListener.onAddOrRemoveControlSS(ss_local, true);
        mListener.onAddOrRemoveControl(mket_serial, true);
        mListener.onAddOrRemoveControl(mkedit_coments, true);
    }

    private void setViewEnable() {

        if (status != null
                && (status.equals(ConstantBaseApp.SYS_STATUS_PENDING)
                || status.equals(ConstantBaseApp.SYS_STATUS_PUT_AWAY))) {
            enableForm(true, View.VISIBLE);
        } else {
            enableForm(false, View.GONE);
            mket_serial.setVisibility(View.GONE);
        }

        if(move_type.equals(ConstantBaseApp.IO_PROCESS_MOVE_PLANNED)
        ||move_type.equals(ConstantBaseApp.IO_PROCESS_MOVE)){
            chk_change_zone.setVisibility(View.VISIBLE);
        }else{
            chk_change_zone.setVisibility(View.GONE);
        }
    }

    private void enableForm(boolean isEnable, int visibility) {
        mket_serial.setEnabled(isEnable);
        ss_zone.setmEnabled(isEnable);
        ss_reason.setmEnabled(isEnable);
        ss_local.setmEnabled(isEnable);
        ss_class.setmEnabled(isEnable);
        iv_add_tracking.setEnabled(isEnable);
        chk_change_zone.setEnabled(isEnable);
        btn_save.setVisibility(visibility);
    }

    private void setClassSS() {
        MD_Class md_class = mPresenter.getClassFromMove(to_class_code);
        String class_code;
        String class_id;
        String class_type;
        String class_color;
        Integer class_available;

        if (md_class != null) {
            class_code = String.valueOf(md_class.getClass_code());
            class_id = md_class.getClass_id();
            class_type = md_class.getClass_type();
            class_color = md_class.getClass_color();
            class_available = md_class.getClass_available();
        } else {
            class_code = String.valueOf(mdProductSerial.getClass_code());
            class_id = mdProductSerial.getClass_id();
            class_type = mdProductSerial.getClass_type();
            class_color = mdProductSerial.getClass_color();
            class_available = mdProductSerial.getClass_available();
        }

        ToolBox_Inf.setSSmValue(
                ss_class,
                String.valueOf(class_code),
                String.valueOf(class_code),
                class_id,
                true,
                MD_ClassDao.CLASS_ID, class_id,
                MD_ClassDao.CLASS_TYPE, class_type,
                MD_ClassDao.CLASS_COLOR, class_color,
                MD_ClassDao.CLASS_AVAILABLE, String.valueOf(class_available)
        );

        ss_class.setmShowLabel(false);
        ss_class.setmOption(mPresenter.getClassList());
        setClassIcon(ss_class.getmValue());
    }

    private void setSerialMKEditText() {

        mket_serial.setVisibility(View.VISIBLE);

        mket_serial.setmBARCODE(
                ToolBox_Inf.profileExists(
                        getActivity(),
                        Constant.PROFILE_MENU_PROFILE,
                        Constant.PROFILE_MENU_PROFILE_SERIAL_BARCODE
                )
        );
        //
        mket_serial.setmOCR(false);
        if (ToolBox_Inf.isMicroBlinkImported()) {
            mket_serial.setmOCRVin(
                    ToolBox_Inf.profileExists(
                            getActivity(),
                            Constant.PROFILE_MENU_PROFILE,
                            Constant.PROFILE_MENU_PROFILE_SERIAL_OCR_VIN
                    )
            );
        } else {
            mket_serial.setmOCRVin(false);
        }
        controls_sta.add(mket_serial);
    }



    private void setLabelsAndHint() {
        tv_inbound_lbl.setText(hmAux_Trans.get("inbound_lbl"));
        tv_move_order_lbl.setText(hmAux_Trans.get("move_order_lbl"));
        tv_move_to_lbl.setText(hmAux_Trans.get("move_to_lbl"));
        tv_serial_lbl.setText(hmAux_Trans.get("serial_lbl"));
        tv_class_lbl.setText(hmAux_Trans.get("class_lbl"));
        btn_save.setText(hmAux_Trans.get("save_lbl"));
        mkedit_coments.setHint(hmAux_Trans.get("comments_hint"));
        chk_change_zone.setText(hmAux_Trans.get("change_to_zone_target_lbl"));
        setSSZone();
        setSSLocal();
        setSSReason();
    }

    private void setSSReason() {
        mPresenter.setDefaultReason(ss_reason);
    }

    private void setSSLocal() {
        ss_local.setmLabel(hmAux_Trans.get("position_lbl"));
        ss_local.setmShowBarcode(true);
        ss_local.setmShowLabel(false);
        mPresenter.loadLocalSS(ss_zone, ss_local, false);


        //feito na pressa, rever apos demonstracao
        if(move_type != null && move_type.equalsIgnoreCase(ConstantBaseApp.IO_PROCESS_IN_CONF)){
            mPresenter.setLocalValue(ss_local, planned_zone_code, planned_local_code);
        } else {
            if (status.equals(ConstantBaseApp.SYS_STATUS_PENDING)
                    || status.equals(ConstantBaseApp.SYS_STATUS_PUT_AWAY)) {
                mPresenter.setLocalValue(ss_local, 0, 0);
            } else {
                mPresenter.setLocalValue(ss_local, to_zone_code, to_local_code);
            }
        }
    }

    private void setSSZone() {
        ss_zone.setmShowBarcode(true);
        ss_zone.setmLabel(hmAux_Trans.get("zone_lbl"));
        ss_zone.setmTitle(hmAux_Trans.get("zone_ttl"));
        mPresenter.loadZoneSS(ss_zone, true, false);
    }

    private void processZoneValueChange(HMAux hmAux) {
        mPresenter.loadLocalSS(ss_zone, ss_local, true);
        //
        if (hmAux != null && hmAux.size() > 0 && ss_local.getmOption().size() == 1) {
            ss_local.setmValue(ss_local.getmOption().get(0));
        }
    }

    private void setMkdate() {
        String defaultDateTime = ToolBox.sDTFormat_Agora(DATE_FORMAT_MKDATE);
        mkdate_confirm.setmLabel(hmAux_Trans.get("confirm_date_lbl"));
        mkdate_confirm.setmValue(defaultDateTime, true);
        mkdate_confirm.setmRequired(true);
    }

    private void setClassIcon(HMAux item) {
        if (item != null && item.containsKey(MD_ClassDao.CLASS_AVAILABLE) && item.get(MD_ClassDao.CLASS_AVAILABLE) != null && item.containsKey(MD_ClassDao.CLASS_COLOR) && item.get(MD_ClassDao.CLASS_COLOR) != null) {
            iv_class_icon.setVisibility(View.VISIBLE);
            if (item.get(MD_ClassDao.CLASS_AVAILABLE).equals("1")) {
                Drawable drawable = getContext().getDrawable(R.drawable.ic_tag_black_24dp);
                drawable.setColorFilter(Color.parseColor(item.get(MD_ClassDao.CLASS_COLOR)), PorterDuff.Mode.SRC_ATOP);
                iv_class_icon.setImageDrawable(drawable);
            } else {
                Drawable drawable = getContext().getDrawable(R.drawable.ic_ban_black_24dp);
                drawable.setColorFilter(Color.parseColor(item.get(MD_ClassDao.CLASS_COLOR)), PorterDuff.Mode.SRC_ATOP);
                iv_class_icon.setImageDrawable(drawable);
            }
        } else {
            iv_class_icon.setImageDrawable(null);
            iv_class_icon.setVisibility(View.INVISIBLE);
        }
    }


    private void initializeTrackingRemovelistner() {
        //
        tvCtListner = new TextViewCT.ITextViewCT() {
            @Override
            public void removeViews(TextViewCT textViewCT) {
                int idx = ll_tracking_content.indexOfChild(textViewCT);
                //

//                mdProductSerial.getTracking_list().remove(idx);
                if(mPresenter.removeTrackingFromMove(trackingFromMove.get(idx))) {
                    trackingFromMove.remove(idx);
                }
                //
                ll_tracking_content.removeView(textViewCT);
                //
                setTrackingListChanged(true);
            }
        };
    }

    private void setSSMoveReason() {
        ss_reason.setmLabel(hmAux_Trans.get("site_reason_lbl"));
        ss_reason.setmTitle(hmAux_Trans.get("site_reason_ttl"));
        ss_reason.setmOption(mPresenter.getMoveReasonList());
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transList = new ArrayList<String>();

        transList.add("serial_lbl");//
        transList.add("inbound_lbl");
        transList.add("move_order_lbl");
        transList.add("move_to_lbl");
        transList.add("class_lbl");
        transList.add("confirm_date_lbl");
        transList.add("change_to_zone_target_lbl");
        transList.add("zone_lbl");
        transList.add("zone_ttl");
        transList.add("position_lbl");
        //transList.add("position_ttl");
        transList.add("comments_hint");
        transList.add("save_lbl");
        transList.add("site_reason_lbl");
        transList.add("site_reason_ttl");
        transList.add("dialog_tracking_ttl");
        transList.add("alert_tracking_already_listed_ttl");
        transList.add("alert_tracking_already_listed_msg");
        transList.add("alert_update_move_ttl");
        transList.add("alert_update_move_confirm");

        return transList;
    }

    public void restoreUIFields(MD_Product_Serial serialInfo, int viewMode, boolean fromMove, HMAux hmAux_Trans_Frag, Integer to_local_code, Integer to_zone_code, int move_prefix, int move_code, Integer reason_code, String move_type, Integer planned_zone_code, Integer outbound_prefix, Integer inbound_prefix, Integer outbound_code, Integer inbound_code, Integer planned_local_code, String status, Integer to_class_code) {
        this.fromMove = fromMove;
        this.view_param = viewMode;
        mdProductSerial = serialInfo;
        this.hmAux_Trans = hmAux_Trans_Frag;
        this.to_local_code = to_local_code;
        this.to_zone_code = to_zone_code;
        this.move_prefix = move_prefix;
        this.move_code = move_code;
        this.reason_code = reason_code;
        this.move_type = move_type;
        this.planned_zone_code =planned_zone_code;
        this.outbound_prefix =outbound_prefix;
        this.inbound_prefix =inbound_prefix;
        this.outbound_code = outbound_code;
        this.inbound_code = inbound_code;
        this.planned_local_code = planned_local_code;
        this.status = status;
        this.to_class_code = to_class_code;
        initializeViews();
    }

    public HMAux getZoneInfo() {
        return ss_zone.getmValue();
    }

    public HMAux getLocalInfo() {
        return ss_local.getmValue();
    }


    public interface OnFragmentInteractionListener {
        void persistIoMovePlanned(long customer_code,
                                  Integer to_zone_code,
                                  Integer to_local_code,
                                  Integer to_class_code,
                                  Integer reason_code,
                                  String comments,
                                  String done_date,
                                  MD_Product_Serial serial,
                                  List<IO_Move_Tracking> trackingFromMove);


        void onFragmentInteraction(Uri uri);

        void showAlert(String title, String msg);

        void onBackPressed();

        boolean isOnline();

        void onAddOrRemoveControl(MKEditTextNM mket_tracking, boolean b);

        void onAddOrRemoveControlSS(SearchableSpinner searchableSpinner, boolean b);

        void onTrackingSearchClick(long product_code, long serial_code, String mket_text, String preference_site_code);

        void callLogAct(Intent logIntent);
    }
}
