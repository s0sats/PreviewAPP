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
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.MessageFormat;
import java.util.ArrayList;
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
    public static final String DATEFORMATMKDATE = "yyyy-MM-dd HH:mm:ss Z";

    private int view_param;
    private boolean fromMove;
    private IO_Move ioMove;



    private MD_Product_Serial mdProductSerial;

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
    private MKEditTextNM mkedit_coments;
    private MkDateTime mkdate_confirm;
    private TextViewCT.ITextViewCT tvCtListner;
    private SearchableSpinner ss_zone;
    private SearchableSpinner ss_position;
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

    private Frag_Move_Create_Presenter mPresenter;
    private OnFragmentInteractionListener mListener;
    private boolean useTracking;
    private ArrayList<MD_Product_Serial_Tracking> tracking_list;
    private boolean pausedByScan;
    private String searched_tracking = "";
    private boolean trackingListChanged;
    private Button btn_save;

    public Frag_Move_Create() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1           Informacao da Movimentacao.
     * @param serialInfo
     * @param viewParam        Controle de visualizacao de elementos, 0 = oculta Comentario e data
     *                         1 = oculta Reason.
     * @param originParam      Controla visualizacao do checkbox para mudanca de zona.
     * @param hmAux_Trans_Frag
     * @return A new instance of fragment Frag_Move_Create.
     */
    public static Frag_Move_Create newInstance(IO_Move param1, MD_Product_Serial serialInfo, int viewParam, boolean originParam, HMAux hmAux_Trans_Frag) {
        Frag_Move_Create fragment = new Frag_Move_Create();
        Bundle args = new Bundle();
        args.putSerializable(IO_MoveDao.TABLE, param1);
        args.putSerializable(MD_Product_SerialDao.TABLE, serialInfo);
        args.putSerializable(HMAUX_TRANS, hmAux_Trans_Frag);
        args.putInt(VIEW_PARAM, viewParam);
        args.putBoolean(ORIGIN_PARAM, originParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromMove = getArguments().getBoolean(ORIGIN_PARAM);
            view_param = getArguments().getInt(VIEW_PARAM);
            ioMove = (IO_Move) getArguments().getSerializable(IO_MoveDao.TABLE);
            mdProductSerial = (MD_Product_Serial) getArguments().getSerializable(MD_Product_SerialDao.TABLE);
            this.hmAux_Trans = (HMAux) getArguments().getSerializable(HMAUX_TRANS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.act058_frag_move, container, false);
        mPresenter = new Frag_Move_Create_Presenter(this, getContext(), ioMove.getMove_type(), ioMove.getPlanned_zone_code());
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
        ll_tracking_content = fragView.findViewById(R.id.ll_tracking_content);
        iv_add_tracking = fragView.findViewById(R.id.act058_iv_add_tracking);
        iv_class_icon = fragView.findViewById(R.id.act058_iv_class_icon);
        btn_save = fragView.findViewById(R.id.act058_btn_save);
        ss_zone = fragView.findViewById(R.id.act058_ss_zone);
        ss_position = fragView.findViewById(R.id.act058_ss_position);
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
        iv_serial_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show history
            }
        });

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
//                    mListener.persistIoMove(
//                            ToolBox_Con.getPreference_Customer_Code(getContext()),
//                            ioMove.getMove_prefix(),
//                            ioMove.getMove_code(),
//
//
//                    );
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

        ss_position.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });

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
            mPresenter.loadLocalSS(ss_zone, ss_position, false);
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
                mkdate_confirm.setmHighlightWhenInvalid(true);

                if (!mkdate_confirm.isValid()) {
                    mkdate_confirm.setmHighlightWhenInvalid(true);
                    isSuccessfully = false;
                }

                ss_zone.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));

                if (ss_zone.getmValue() == null || ss_zone.getmValue().hasConsistentValue(SearchableSpinner.ID)) {
                    ss_zone.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
                    isSuccessfully = false;
                }
                break;
        }
        if (ss_reason.getmValue() == null || !ss_reason.getmValue().hasConsistentValue(SearchableSpinner.ID)) {
            ss_reason.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            isSuccessfully = false;
        } else {
            ss_reason.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
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
        tv_zone_position.setText(MessageFormat.format("{0} | {1}", mdProductSerial.getZone_id(), mdProductSerial.getLocal_id()));

        try {
            tv_inbound_val.setText(formatPrefixAndCode(ioMove.getInbound_prefix(), ioMove.getInbound_code()));
        } catch (NullPointerException e) {
            tv_inbound_lbl.setVisibility(View.GONE);
            tv_inbound_val.setVisibility(View.GONE);
        }

        try {
            tv_outbound_val.setText(formatPrefixAndCode(ioMove.getOutbound_prefix(), ioMove.getOutbound_code()));
        } catch (NullPointerException e) {
            tv_outbound_lbl.setVisibility(View.GONE);
            tv_outbound_val.setVisibility(View.GONE);
        }

        tv_move_order_val.setText(formatPrefixAndCode(ioMove.getMove_prefix(), ioMove.getMove_code()));
        String plannedZoneLocal = (ioMove.getPlanned_zone_code() == null) ? "" : String.valueOf(ioMove.getPlanned_zone_code());

        plannedZoneLocal.concat((ioMove.getPlanned_local_code() == null) ? "" : ioMove.getPlanned_local_code() + "|");
        if (plannedZoneLocal.isEmpty()) {
            tv_move_to_lbl.setVisibility(View.GONE);
            tv_move_to_val.setVisibility(View.GONE);
            tv_move_to_val.setText(plannedZoneLocal);
        } else {
            tv_move_to_lbl.setVisibility(View.VISIBLE);
            tv_move_to_val.setVisibility(View.VISIBLE);
            tv_move_to_val.setText(plannedZoneLocal);
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
        for (int i = 0; i < mdProductSerial.getTracking_list().size(); i++) {
            appendTracking(mdProductSerial.getTracking_list().get(i).getTracking());
        }

        ToolBox_Inf.setSSmValue(
                ss_class,
                String.valueOf(mdProductSerial.getClass_code()),
                String.valueOf(mdProductSerial.getClass_code()),
                mdProductSerial.getClass_id(),
                true,
                MD_ClassDao.CLASS_ID, mdProductSerial.getClass_id(),
                MD_ClassDao.CLASS_TYPE, mdProductSerial.getClass_type(),
                MD_ClassDao.CLASS_COLOR, mdProductSerial.getClass_color(),
                MD_ClassDao.CLASS_AVAILABLE, String.valueOf(mdProductSerial.getClass_available())
        );

        ss_class.setmOption(mListener.getClassList());
        setClassIcon(ss_class.getmValue());

        setLabelsAndHint();
        setMkdate();
        setSSMoveReason();

        switch (view_param) {
            case 0:
                ss_reason.setVisibility(View.VISIBLE);
                mkedit_coments.setVisibility(View.GONE);
                mkdate_confirm.setVisibility(View.GONE);
                break;
            case 1:
                ss_reason.setVisibility(View.GONE);
                mkedit_coments.setVisibility(View.VISIBLE);
                mkdate_confirm.setVisibility(View.VISIBLE);
                break;
        }

//        ss_reason.setmHint(hmAux_Trans.get("reason_hint"));
    }

    private void setLabelsAndHint() {
        tv_inbound_lbl.setText(hmAux_Trans.get("inbound_lbl"));
        tv_move_order_lbl.setText(hmAux_Trans.get("move_order_lbl"));
        tv_move_to_lbl.setText(hmAux_Trans.get("move_to_lbl"));
        tv_serial_lbl.setText(hmAux_Trans.get("serial_lbl"));
        btn_save.setText(hmAux_Trans.get("save_lbl"));
        setSSZone();
        setSSPosition();
        mkedit_coments.setHint(hmAux_Trans.get("comments_hint"));
        chk_change_zone.setText(hmAux_Trans.get("change_to_zone_target_lbl"));
    }

    private void setSSPosition() {
        ss_position.setmLabel(hmAux_Trans.get("position_lbl"));
        mPresenter.loadLocalSS(ss_zone, ss_position, false);
    }

    private void setSSZone() {
        ss_zone.setmShowBarcode(true);
        ss_zone.setmShowBarcode(true);
        ss_zone.setmLabel(hmAux_Trans.get("zone_lbl"));
        ss_zone.setmTitle(hmAux_Trans.get("zone_ttl"));
        mPresenter.loadZoneSS(ss_zone, true, false);
    }

    private void processZoneValueChange(HMAux hmAux) {
        mPresenter.loadLocalSS(ss_zone, ss_position, true);
        //
        if (hmAux != null && hmAux.size() > 0 && ss_position.getmOption().size() == 1) {
            ss_position.setmValue(ss_position.getmOption().get(0));
        }
    }

    private void setMkdate() {
        String defaultDateTime = ToolBox.sDTFormat_Agora(DATEFORMATMKDATE);
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
                mdProductSerial.getTracking_list().remove(idx);
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
        ss_reason.setmOption(mListener.getReasonOption());
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
        transList.add("position_ttl");
        transList.add("comments_hint");
        transList.add("save_lbl");
        transList.add("site_reason_lbl");
        transList.add("site_reason_ttl");
        transList.add("dialog_tracking_ttl");
        transList.add("alert_tracking_already_listed_ttl");
        transList.add("alert_tracking_already_listed_msg");

        return transList;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void persistIoMove(long customer_code,
                           int move_prefix,
                           int move_code,
                           Integer to_zone_code,
                           Integer to_local_code,
                           Integer to_class_code,
                           Integer reason_code,
                           String done_date,
                           ArrayList<MD_Product_Serial> serial);

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void showAlert(String title, String msg);

        void onBackPressed();

        boolean isOnline();

        ArrayList<HMAux> getReasonOption();

        void onAddOrRemoveControl(MKEditTextNM mket_tracking, boolean b);

        void onTrackingSearchClick(long product_code, long serial_code, String mket_text, String preference_site_code);

        ArrayList<HMAux> getClassList();

        void callLogAct(Intent logIntent);
    }
}
