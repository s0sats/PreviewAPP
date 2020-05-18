package com.namoadigital.prj001.ui.act027;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.ApplyRepairImageFF;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.PictureFF;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Gallery_v2_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_SketchDao;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_All_Product;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Product_Event;
import com.namoadigital.prj001.model.SM_SO_Product_Event_File;
import com.namoadigital.prj001.model.SM_SO_Product_Event_Sketch;
import com.namoadigital.prj001.model.TSO_Save_Env;
import com.namoadigital.prj001.receiver.WBR_SO_Product_Event_Cancel;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_File_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.namoadigital.prj001.ui.act027.Act027_Main.WS_SO_PRODUCT_EVENT_CANCEL;

/**
 * Created by neomatrix on 31/10/17.
 */

public class Act027_Product_Edit extends BaseFragment {

    public static final String EVENT_EDIT_MODE = "event_edit_mode";

    private Context context;

    private TextView tv_tmp_ref_ttl;
    private TextView tv_tmp_ref_val;
    private TextView tv_prod_ttl;
    private TextView tv_desc_ttl;

    private ApplyRepairImageFF arff_applyrepair;
    private CheckBox cb_inspection;
    private LinearLayout ll_qty;
    private MKEditTextNM mk_qty;
    private TextView tv_unit;
    private PictureFF pff_sketch;
    private TextInputLayout tilComment;
    private TextView tv_comments_lbl;
    private TextView tv_photo_amount;
    private MKEditTextNM mk_comments;
    private ImageView iv_gallery;
    private LinearLayout ll_save;
    private ImageView iv_save;

    private LinearLayout ll_log;
    private TextView tv_nick;
    private TextView tv_date;

    private MD_All_ProductDao md_all_productDao;
    private SM_SO_Product_EventDao sm_so_product_eventDao;
    private SM_SO_Product_Event_FileDao sm_so_product_event_fileDao;
    private SM_SO_Product_Event_SketchDao sm_so_product_event_sketchDao;

    private SM_SO mSm_so;
    private MD_All_Product md_all_product;
    private SM_SO_Product_Event mSm_so_product_event;

    private int mProductCode = -1;
    private int mSeqTmp = -1;

    private boolean sdAvoid = false;
    private boolean bStatus = false;

    private StringBuilder sFiles;
    private StringBuilder sSketchs;

    private String mErrorMSG;

    private boolean bStatusNew = false;
    private OnRecoveryFragmentState delegate;
    private LinearLayout ll_delete_prod_event;
    private Button btn_delete_prod_event;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public void setProductEventPk(int product_code, int seq_tmp) {
        mProductCode = product_code;
        mSeqTmp = seq_tmp;
    }

    public boolean eventStatusOpen() {
        if (mSm_so_product_event.getStatus().isEmpty() || mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act027_product_edit_content, container, false);
        //
        recoverBundleInfo();
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void recoverBundleInfo() {
        if(getArguments() != null){
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String, String>) getArguments().getSerializable(Constant.MAIN_HMAUX_TRANS_KEY));
        }
    }

    @Override
    public void setHmAux_Trans(HMAux hmAux_Trans) {
        super.setHmAux_Trans(hmAux_Trans);
        updateFragArgs();
    }

    private void updateFragArgs() {
        Bundle args = getArguments();
        if(args == null){
            args = new Bundle();
        }
        args.putSerializable(Constant.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        //
        this.setArguments(args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        delegate = (OnRecoveryFragmentState) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mSm_so_product_event.getStatus().equalsIgnoreCase("") || mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING)) {
            if (bStatusNew) {
                bStatusNew = false;

            } else {
                if (!pff_sketch.getmValue().equalsIgnoreCase(tempValues.get("sketch_mvalue"))) {
                    tempValues.put("sketch_mvalue", pff_sketch.getmValue());
                }

            }
        }

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();
        loadScreenToData();
        updateFragArgs();
    }

    private void iniVar(View view) {
        if(mSm_so == null || hmAux_Trans == null){
            delegate.callAct005();
        } else {
            context = getActivity();

            hideSoftKeyboard(getActivity());

            tv_tmp_ref_ttl = view.findViewById(R.id.act027_product_edit_content_tv_seq_tmp_ttl);
            tv_tmp_ref_val = view.findViewById(R.id.act027_product_edit_content_tv_seq_tmp_val);

            tv_prod_ttl = view.findViewById(R.id.act027_product_edit_content_tv_prod_ttl);
            tv_desc_ttl = view.findViewById(R.id.act027_product_edit_content_tv_prod_desc);
            arff_applyrepair = view.findViewById(R.id.act027_product_edit_content_arff_applyrepair);
            cb_inspection = view.findViewById(R.id.act027_product_edit_content_cb_inspection);
            cb_inspection.setText(hmAux_Trans.get("event_inspection_lbl"));
            ll_qty = view.findViewById(R.id.act027_product_edit_content_ll_qty_apply);
            tv_unit = view.findViewById(R.id.act027_product_edit_content_tv_unit);
            mk_qty = view.findViewById(R.id.act027_product_edit_content_mk_qty);

            pff_sketch = view.findViewById(R.id.act027_product_edit_content_pff_sketch);

            tilComment = view.findViewById(R.id.act027_product_edit_content_til_comment);
            tv_comments_lbl = view.findViewById(R.id.act027_product_edit_content_tv_comments_lbl);
            tv_comments_lbl.setText(hmAux_Trans.get("event_comments_lbl"));
            mk_comments = view.findViewById(R.id.act027_product_edit_content_mk_comments);
            iv_gallery = view.findViewById(R.id.act027_product_edit_content_iv_gallery);
            tv_photo_amount = view.findViewById(R.id.act027_product_edit_content_tv_photo_amount);
            ll_save = view.findViewById(R.id.act027_product_edit_content_ll_save);
            iv_save = view.findViewById(R.id.act027_product_edit_content_iv_save);

            ll_log = view.findViewById(R.id.act027_product_edit_content_ll_log);
            tv_nick = view.findViewById(R.id.act027_product_edit_content_tv_nick);
            tv_date = view.findViewById(R.id.act027_product_edit_content_tv_date);

            ll_delete_prod_event = view.findViewById(R.id.act027_product_edit_content_ll_delete_prod_event);
            btn_delete_prod_event = view.findViewById(R.id.act027_product_edit_content_btn_delete_prod_event);

            setProductEventDeleteFuction();

            md_all_productDao = new MD_All_ProductDao(context);

            sm_so_product_eventDao = new SM_SO_Product_EventDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );

            sm_so_product_event_fileDao = new SM_SO_Product_Event_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );

            sm_so_product_event_sketchDao = new SM_SO_Product_Event_SketchDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );

            if (mProductCode != -1 && mSeqTmp != -1) {
                mSm_so_product_event = sm_so_product_eventDao.getByString(
                    new SM_SO_Product_Event_Sql_003(
                        mSm_so.getCustomer_code(),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code(),
                        mSeqTmp
                    ).toSqlQuery()
                );

            } else {
                md_all_product = md_all_productDao.getByString(
                    new MD_All_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        (long) mProductCode

                    ).toSqlQuery()
                );


                HMAux hmTmp = sm_so_product_eventDao.getByStringHM(
                    new SM_SO_Product_Event_Sql_002(
                        mSm_so.getCustomer_code(),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code()
                    ).toSqlQuery()
                );

                int seq_tmp = ToolBox_Inf.convertStringToInt(hmTmp.get(SM_SO_Product_Event_Sql_002.NEXT_TMP));

                mSm_so_product_event = new SM_SO_Product_Event();
                mSm_so_product_event.setCustomer_code(mSm_so.getCustomer_code());
                mSm_so_product_event.setSo_prefix(mSm_so.getSo_prefix());
                mSm_so_product_event.setSo_code(mSm_so.getSo_code());
                mSm_so_product_event.setSeq(0);
                mSm_so_product_event.setSeq_tmp(seq_tmp);
                mSm_so_product_event.setProduct_code((int) md_all_product.getProduct_code());
                mSm_so_product_event.setProduct_id(md_all_product.getProduct_id());
                mSm_so_product_event.setProduct_desc(md_all_product.getProduct_desc());
                mSm_so_product_event.setUn(md_all_product.getUn());
                mSm_so_product_event.setFlag_apply(0);
                mSm_so_product_event.setFlag_repair(0);
                mSm_so_product_event.setFlag_inspection(0);
                mSm_so_product_event.setQty_apply("");
                mSm_so_product_event.setSketch_code(md_all_product.getSketch_code());
                mSm_so_product_event.setSketch_name(md_all_product.getSketch_url_local());
                mSm_so_product_event.setSketch_url(md_all_product.getSketch_url());
                mSm_so_product_event.setSketch_url_local(md_all_product.getSketch_url_local());
                mSm_so_product_event.setSketch_lines(md_all_product.getSketch_lines());
                mSm_so_product_event.setSketch_columns(md_all_product.getSketch_columns());
                mSm_so_product_event.setSketch_color(md_all_product.getSketch_color());
                mSm_so_product_event.setComments("");
                mSm_so_product_event.setStatus("");
                mSm_so_product_event.setCreate_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                mSm_so_product_event.setCreate_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
                mSm_so_product_event.setCreate_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
                mSm_so_product_event.setDone_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                mSm_so_product_event.setDone_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
                mSm_so_product_event.setDone_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
                mSm_so_product_event.setIntegrated(0);
            }

            if (mSm_so_product_event.getQty_apply() != null) {
                mSm_so_product_event.setQty_apply(mSm_so_product_event.getQty_apply().replace(",", "."));
            }

            controls_iv.add(iv_gallery);
            controls_sta.add(mk_comments);
            controls_dyn.add(pff_sketch);

            sFiles = new StringBuilder();
            sSketchs = new StringBuilder();

            boolean bFirst = true;

            for (SM_SO_Product_Event_File sm_so_product_event_file : mSm_so_product_event.getFile()) {

                if (!sm_so_product_event_file.getFile_name().isEmpty()) {

                    if (bFirst) {
                        sFiles.append(sm_so_product_event_file.getFile_name());
                        bFirst = false;
                    } else {
                        sFiles.append("#");
                        sFiles.append(sm_so_product_event_file.getFile_name());
                    }

                }
            }
            setMk_comments_refreshing_layout(mSm_so_product_event.getComments());

            tv_unit.setText(mSm_so_product_event.getUn());

            mk_qty.setText(mSm_so_product_event.getQty_apply());

            iv_gallery.setTag(sFiles.toString());

            bFirst = true;

            for (SM_SO_Product_Event_Sketch sm_so_product_event_sketch : mSm_so_product_event.getSketch()) {

                if (bFirst) {
                    sSketchs.append(sm_so_product_event_sketch.getLine())
                        .append(",")
                        .append(sm_so_product_event_sketch.getCol());
                    bFirst = false;
                } else {
                    sSketchs.append("#");
                    sSketchs.append(sm_so_product_event_sketch.getLine())
                        .append(",")
                        .append(sm_so_product_event_sketch.getCol());
                }
            }

            if (mSm_so_product_event.getSketch_code() == null) {
                pff_sketch.setVisibility(View.GONE);
            } else {
                StringBuilder sbOptions = new StringBuilder();
                pff_sketch.setVisibility(View.VISIBLE);
                pff_sketch.setmFName(mSm_so_product_event.getSketch_url_local());

                sbOptions.append("{\"CONTENT\":[{\"LINES\":")
                    .append(mSm_so_product_event.getSketch_lines())
                    .append(" ,\"COLUMNS\":")
                    .append(mSm_so_product_event.getSketch_columns())
                    .append(",\"COLOR\":\"")
                    .append(mSm_so_product_event.getSketch_color())
                    .append("\"}]}");

                pff_sketch.setmOption(sbOptions.toString());

                pff_sketch.setmValue(
                    ToolBox.converterToJson(sSketchs.toString())
                );

            }

            pff_sketch.setmIv_Dots(0);
            pff_sketch.setmV_Line(0);

            if (mSm_so_product_event.getFlag_apply() == 1) {
                arff_applyrepair.setmValue("10");
                mk_qty.setText(mSm_so_product_event.getQty_apply());
                mk_qty.setmRequired(true);
                //
                ll_qty.setVisibility(View.VISIBLE);
//            mk_qty.setVisibility(View.VISIBLE);
//            tv_unit.setVisibility(View.VISIBLE);
            } else if (mSm_so_product_event.getFlag_repair() == 1) {
                arff_applyrepair.setmValue("01");
                mk_qty.setmRequired(false);
                ll_qty.setVisibility(View.GONE);
//            mk_qty.setVisibility(View.INVISIBLE);
//            tv_unit.setVisibility(View.INVISIBLE);
            } else {
                arff_applyrepair.setmValue("00");
                mk_qty.setmRequired(true);
                ll_qty.setVisibility(View.GONE);
//            mk_qty.setVisibility(View.INVISIBLE);
//            tv_unit.setVisibility(View.INVISIBLE);
            }

            if (mSm_so_product_event.getFlag_inspection() == 1) {
                cb_inspection.setChecked(true);
            } else {
                cb_inspection.setChecked(false);
            }

            arff_applyrepair.setmIv_Dots(0);
            arff_applyrepair.setmV_Line(0);

            if (mSm_so_product_event.getStatus().equalsIgnoreCase("") || mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING)) {
                bStatusNew = true;
            } else {
                bStatusNew = false;
            }

            if (mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_DONE)) {
                ll_log.setVisibility(View.VISIBLE);
                //
                tv_nick.setText(mSm_so_product_event.getDone_user_nick());
                tv_date.setText(
                    ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(mSm_so_product_event.getDone_date() != null ? mSm_so_product_event.getDone_date() : "", ""),
                        ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                    )
                );
            } else {
                if (mSm_so_product_event.getStatus().equalsIgnoreCase("")) {
                    ll_log.setVisibility(View.GONE);
                    //
                    tv_nick.setText("");
                    tv_date.setText("");
                } else {
                    ll_log.setVisibility(View.VISIBLE);
                    //
                    tv_nick.setText(mSm_so_product_event.getCreate_user_nick());
                    tv_date.setText(
                        ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(mSm_so_product_event.getCreate_date() != null ? mSm_so_product_event.getCreate_date() : "", ""),
                            ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                        )
                    );
                }
            }

            tempValues.put("arff_applyrepair", arff_applyrepair.getmValue());
            tempValues.put("cb_inspection", cb_inspection.isChecked() ? "1" : "0");

            tempValues.put("mk_comments", mk_comments.getText().toString());
            tempValues.put("mk_qty", mk_qty.getText().toString());
            tempValues.put("img", (String) iv_gallery.getTag());

            tempValues.put("sketch_fname", pff_sketch.getmFName());
            tempValues.put("sketch_options", pff_sketch.getmOption());
            tempValues.put("sketch_mvalue", pff_sketch.getmValue());
        }
    }

    private void setProductEventDeleteFuction() {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_PRODUCT_EVENT_CANCEL)) {
            ll_delete_prod_event.setVisibility(View.VISIBLE);
            btn_delete_prod_event.setVisibility(View.VISIBLE);
        }else{
            ll_delete_prod_event.setVisibility(View.GONE);
            btn_delete_prod_event.setVisibility(View.GONE);
        }

    }

    /**
     * BARRIONUEVO 18-05-2020
     * METODO CRIADO NA CORRERIA PARA ATENDER UM PROJETO ESQUECIDO PELO DEMANDANTE.
     * @return
     */
    private boolean isSoWithinTokenFile() {
        try {
            File[] soToken =
                    ToolBox_Inf.getListOfFiles_v5(
                            ConstantBaseApp.TOKEN_PATH,
                            ToolBox_Inf.buildTokenPrefixWithCustomer(context,ConstantBaseApp.TOKEN_SO_PREFIX)
                    );
            if (soToken.length > 0) {
                Gson gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
                //
                ArrayList<SM_SO> token_so_list =
                        gsonEnv.fromJson(
                                ToolBox_Inf.getContents(soToken[0]),
                                TSO_Save_Env.class
                        ).getSo();
                //
                if (token_so_list.size() == 0) {
                    return false;
                }
                //
                for (SM_SO so : token_so_list) {
                    if (
                            so.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                                    && so.getSo_prefix() == mSm_so.getSo_prefix()
                                    && so.getSo_code() == mSm_so.getSo_code()
                    ) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        return false;
    }


    private void setMk_comments_refreshing_layout(String comment) {
        //Remove counter
        tilComment.setCounterEnabled(false);
        mk_comments.setText(comment);
        //Reabilita counter para atualizar contador
        tilComment.setCounterEnabled(true);
    }

    private void iniAction() {

        arff_applyrepair.setOnSelectionChangedListener(new ApplyRepairImageFF.IApplyRepairImageFF() {
            @Override
            public void selectionChanged(String status) {

                switch (status) {
                    case "10":
                        mk_qty.setmRequired(true);
                        ll_qty.setVisibility(View.VISIBLE);
//                        mk_qty.setVisibility(View.VISIBLE);
//                        tv_unit.setVisibility(View.VISIBLE);
                        break;
                    case "01":
                        mk_qty.setmRequired(false);
                        ll_qty.setVisibility(View.GONE);
//                        mk_qty.setVisibility(View.INVISIBLE);
//                        tv_unit.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        mk_qty.setmRequired(false);
                        ll_qty.setVisibility(View.GONE);
//                        mk_qty.setVisibility(View.INVISIBLE);
//                        tv_unit.setVisibility(View.INVISIBLE);
                        break;
                }

            }
        });

        btn_delete_prod_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSm_so_product_event.getSeq() == 0){
                    //TODO Implementar rotina de delete local
                }else {
                    if(ToolBox_Con.isOnline(context)) {
                        Act027_Main mMain = (Act027_Main) getActivity();
                        if (mSm_so.getSync_required() == 0 && mSm_so.getUpdate_required() == 0 && !isSoWithinTokenFile()) {
                            callProdEventDeleteService();
                        } else {
                            ToolBox.alertMSG(context,
                                    hmAux_Trans.get("alert_sync_before_cancel_product_event_ttl"),
                                    hmAux_Trans.get("alert_sync_before_cancel_product_event_msg"),
                                    null,
                                    0
                            );
                        }
                    }else{
                        ToolBox_Inf.showNoConnectionDialog(context);
                    }
                }
            }
        });
    }

    private void callProdEventDeleteService() {
        Act027_Main mMain = (Act027_Main) getActivity();
        mMain.setWs_process(WS_SO_PRODUCT_EVENT_CANCEL);
        //
        mMain.enableProgressDialog(
                hmAux_Trans.get("progress_alert_product_event_cancel_ttl"),
                hmAux_Trans.get("progress_product_event_cancel_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Product_Event_Cancel.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SM_SO_Product_EventDao.SO_PREFIX, mSm_so_product_event.getSo_prefix());
        bundle.putInt(SM_SO_Product_EventDao.SO_CODE, mSm_so_product_event.getSo_code());
        int prodEventSeq = mSm_so_product_event.getSeq();
        if(prodEventSeq == 0){
            prodEventSeq = mSm_so_product_event.getSeq_tmp();
        }
        bundle.putInt(SM_SO_Product_EventDao.SEQ, prodEventSeq);
        bundle.putInt(SM_SODao.SO_SCN, mSm_so.getSo_scn());
        bundle.putString(SM_SODao.TOKEN, mSm_so.getToken());
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void loadDataToScreen() {

        if (bStatus) {
            if (mSm_so_product_event == null
                    || hmAux_Trans == null) {
                delegate.callAct005();
            } else {
                if (mSm_so_product_event.getSketch_code() != null && !ToolBox_Inf.verifyDownloadFileInf(mSm_so_product_event.getSketch_url_local())) {

                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_sketch_not_ready_title"),
                            hmAux_Trans.get("alert_sketch_not_ready_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Act027_Main mMain = (Act027_Main) getActivity();
                                    //
                                    mMain.setProductListFragOffLine();
                                }
                            },
                            -1
                    );
                }


                tv_tmp_ref_ttl.setText(hmAux_Trans.get("event_tmp_ref_lbl"));
                tv_tmp_ref_val.setText(mSm_so_product_event.getSeq_tmp() > 0 ? String.valueOf(mSm_so_product_event.getSeq_tmp()) : "");

                tv_prod_ttl.setText(hmAux_Trans.get("event_product_ttl"));
                tv_desc_ttl.setText(mSm_so_product_event.getProduct_id() + " - " + mSm_so_product_event.getProduct_desc());

                try {
                    if (widgetset) {
                        widgetset = false;
                    } else {
                        setMk_comments_refreshing_layout(tempValues.get("mk_comments"));
                    }

//                if (tempValues.get("mk_qty") != null && !tempValues.get("mk_qty").isEmpty()) {
//                    mk_qty.setText(tempValues.get("mk_qty"));
//                }


                    if (sdAvoid) {
                        sdAvoid = false;
                    } else {
                        iv_gallery.setTag(tempValues.get("img"));
                    }

                    arff_applyrepair.setmValue(tempValues.get("arff_applyrepair"));

                    cb_inspection.setChecked(tempValues.get("cb_inspection").equalsIgnoreCase("1") ? true : false);

                    mk_qty.setText(tempValues.get("mk_qty"));

                    pff_sketch.setmFName(tempValues.get("sketch_fname"));
                    pff_sketch.setmOption(tempValues.get("sketch_options"));
                    pff_sketch.setmValue(tempValues.get("sketch_mvalue"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                upImgGallery();

                processTaskStatus();
            }
        }
    }

    /**
     * BARRIONUEVO 20-03-2020
     * Alteracao de icone de camera com contador de fotos tiradas.
     */
    private void upImgGallery() {
        if (((String) iv_gallery.getTag()).equalsIgnoreCase("")) {
            iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_camera_on));
            tv_photo_amount.setVisibility(View.GONE);
        } else {
            iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_camera_on));
            int mPhoto_amount = countPhotoAmount(((String) iv_gallery.getTag()));
            if(mPhoto_amount >0){
                String mPhoto_amount_formatted;
                mPhoto_amount_formatted= String.valueOf(mPhoto_amount);
                tv_photo_amount.setText(mPhoto_amount_formatted);
                tv_photo_amount.setVisibility(View.VISIBLE);
            }else {
                tv_photo_amount.setVisibility(View.GONE);
            }
        }

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCamera();
            }
        });
    }

    /**
     * BARRIONUEVO 20-03-2020
     * Envia lista de nomes de imagens para calcular quantidade, mesmo parametro usado para
     * contar total de imagem na galeria v1.
     * @param photos
     * @return
     */
    private int countPhotoAmount(String photos){
        String[] photoArray = photos.split("#");
        if( photoArray != null && !photoArray[0].isEmpty()){
            return photoArray.length;
        }
        return 0;
    }

    @Override
    public void loadScreenToData() {
        if (bStatus) {

            tempValues.put("arff_applyrepair", arff_applyrepair.getmValue());
            tempValues.put("cb_inspection", cb_inspection.isChecked() ? "1" : "0");

            tempValues.put("mk_comments", mk_comments.getText().toString());
            tempValues.put("mk_qty", mk_qty.getText().toString());
            tempValues.put("img", (String) iv_gallery.getTag());

            tempValues.put("sketch_fname", pff_sketch.getmFName());
            tempValues.put("sketch_options", pff_sketch.getmOption());
            tempValues.put("sketch_mvalue", pff_sketch.getmValue());
        }
    }

    private void callCamera() {
        sdAvoid = true;

        Intent mIntent = new Intent(context, Gallery_v2_Activity.class);
        mIntent.putExtra(ConstantBase.PID, iv_gallery.getId());
        mIntent.putExtra(ConstantBase.PTYPE, 10);
        mIntent.putExtra(ConstantBase.PPATH, (String) iv_gallery.getTag());
        mIntent.putExtra(ConstantBase.MPRE, "pp");

        if (mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING) ||
                mSm_so_product_event.getStatus().equalsIgnoreCase("")) {
            mIntent.putExtra(ConstantBase.PENABLED, true);
        } else {
            mIntent.putExtra(ConstantBase.PENABLED, false);
        }

        mIntent.putExtra(ConstantBase.MAXIMAGES, 4);
        //
        context.startActivity(mIntent);
    }

    private void processTaskStatus() {

        Act027_Main mMain = (Act027_Main) getActivity();

        if (!mMain.hasExecutionProfile()
                || hasSOStatusStopOrEdit()) {
            arff_applyrepair.setmEnabled(false);
            cb_inspection.setEnabled(false);
            cb_inspection.setTextColor(0xFF000000);
            mk_qty.setEnabled(false);
            tv_unit.setEnabled(false);
            pff_sketch.setmEnabled(false);
            mk_comments.setEnabled(false);

            String sFF = (String) iv_gallery.getTag();

            if (sFF.length() != 0) {
                iv_gallery.setEnabled(true);
                iv_gallery.setImageDrawable(context.getDrawable(R.drawable.ic_camera_on));
            } else {
                iv_gallery.setEnabled(false);
                iv_gallery.setImageDrawable(context.getDrawable(R.drawable.ic_camera_off));
            }
            ll_save.setVisibility(View.GONE);
            //iv_save.setVisibility(View.GONE);

            mMain.setEventEditOpenStatus(false);

            return;
        }

        if (mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING) ||
                mSm_so_product_event.getStatus().equalsIgnoreCase("")) {

            arff_applyrepair.setmEnabled(true);
            cb_inspection.setEnabled(true);
            mk_qty.setEnabled(true);
            tv_unit.setEnabled(true);
            pff_sketch.setmEnabled(true);
            mk_comments.setEnabled(true);
            ll_save.setVisibility(View.VISIBLE);
            //iv_save.setVisibility(View.VISIBLE);
            iv_save.setOnClickListener(save_listener);

            mMain.setEventEditOpenStatus(true);
        } else {
            arff_applyrepair.setmEnabled(false);
            cb_inspection.setEnabled(false);
            cb_inspection.setTextColor(0xFF000000);
            mk_qty.setEnabled(false);
            tv_unit.setEnabled(false);
            pff_sketch.setmEnabled(false);
            mk_comments.setEnabled(false);

            String sFF = (String) iv_gallery.getTag();

            if (sFF.length() != 0) {
                iv_gallery.setEnabled(true);
                iv_gallery.setImageDrawable(context.getDrawable(R.drawable.ic_camera_on));
            } else {
                iv_gallery.setEnabled(false);
                iv_gallery.setImageDrawable(context.getDrawable(R.drawable.ic_camera_off));
            }

            ll_save.setVisibility(View.GONE);
            //iv_save.setVisibility(View.GONE);

            mMain.setEventEditOpenStatus(false);

        }

    }

    private boolean hasSOStatusStopOrEdit() {
        return mSm_so.getStatus().equals(Constant.SYS_STATUS_STOP)
                || mSm_so.getStatus().equals(Constant.SYS_STATUS_EDIT);
    }

    private View.OnClickListener save_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isValid()) {

                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_product_edit_save_ttl"),
                        hmAux_Trans.get("alert_product_edit_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                informEventActiveClosed();
                            }
                        },
                        1
                );
            } else {
                informEventError(mErrorMSG);
            }
        }
    };

    private boolean isValid() {

        mErrorMSG = "";

        if (arff_applyrepair.getmValue().equalsIgnoreCase("00") && !cb_inspection.isChecked()) {
            mErrorMSG = hmAux_Trans.get("opc_selection_error_msg");

            return false;
        }

        if (arff_applyrepair.getmValue().equalsIgnoreCase("10")
                && convertToDouble(mk_qty.getText().toString()) <= 0
        ) {
            mErrorMSG = hmAux_Trans.get("qty_apply_error_msg");

            return false;
        }

        return true;
    }

    private double convertToDouble(String texto) {
        try {
            return Double.parseDouble(texto);
        } catch (Exception e) {
            return -1.0;
        }
    }

    private void informEventActiveClosed() {

        if (arff_applyrepair.getmValue().equalsIgnoreCase("10")) {
            mSm_so_product_event.setFlag_apply(1);
            mSm_so_product_event.setFlag_repair(0);
            mSm_so_product_event.setQty_apply(mk_qty.getText().toString().trim().replace(".", ","));
        }

        if (arff_applyrepair.getmValue().equalsIgnoreCase("01")) {
            mSm_so_product_event.setFlag_apply(0);
            mSm_so_product_event.setFlag_repair(1);
            mSm_so_product_event.setQty_apply(null);
        }

        if (cb_inspection.isChecked()) {
            mSm_so_product_event.setFlag_inspection(1);
        } else {
            mSm_so_product_event.setFlag_inspection(0);
        }

        if (mk_comments.getText().toString().trim().length() != 0) {
            mSm_so_product_event.setComments(mk_comments.getText().toString().trim());
        } else {
            mSm_so_product_event.setComments(null);
        }

        mSm_so_product_event.setDone_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        mSm_so_product_event.setDone_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        mSm_so_product_event.setDone_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));

        /**
         * Re-create Sketches Marks
         */
        ArrayList<SM_SO_Product_Event_Sketch> sketches = new ArrayList<>();
        String[] marks = ToolBox.converterFromJson(pff_sketch.getmValue()).split("#");

        if (!marks[0].isEmpty()) {
            for (int i = 0; i < marks.length; i++) {
                SM_SO_Product_Event_Sketch eventSketch = new SM_SO_Product_Event_Sketch();

                eventSketch.setCustomer_code(mSm_so_product_event.getCustomer_code());
                eventSketch.setSo_prefix(mSm_so_product_event.getSo_prefix());
                eventSketch.setSo_code(mSm_so_product_event.getSo_code());
                eventSketch.setSeq(0);
                eventSketch.setSeq_tmp(mSm_so_product_event.getSeq_tmp());
                //
                String[] coord = marks[i].split(",");
                //
                eventSketch.setLine(Integer.parseInt(coord[0]));
                eventSketch.setCol(Integer.parseInt(coord[1]));
                //
                sketches.add(eventSketch);
            }
        }

        /**
         * Re-create Photo-List File
         */
        ArrayList<SM_SO_Product_Event_File> eventFiles = new ArrayList<>();
        String[] photos = ((String) iv_gallery.getTag()).split("#");

        if (!photos[0].isEmpty()) {

            HMAux auxFileTmp = new HMAux();

            auxFileTmp = sm_so_product_event_fileDao.getByStringHM(
                    new SM_SO_Product_Event_File_Sql_002(
                            mSm_so_product_event.getCustomer_code(),
                            mSm_so_product_event.getSo_prefix(),
                            mSm_so_product_event.getSo_code(),
                            mSm_so_product_event.getSeq_tmp()
                    ).toSqlQuery()
            );

            int fileTmp = Integer.parseInt(auxFileTmp.get(SM_SO_Product_Event_File_Sql_002.NEXT_TMP));

            for (int i = 0; i < photos.length; i++) {
                SM_SO_Product_Event_File eventFile = new SM_SO_Product_Event_File();

                eventFile.setCustomer_code(mSm_so_product_event.getCustomer_code());
                eventFile.setSo_prefix(mSm_so_product_event.getSo_prefix());
                eventFile.setSo_code(mSm_so_product_event.getSo_code());
                eventFile.setSeq(0);
                eventFile.setSeq_tmp(mSm_so_product_event.getSeq_tmp());
                eventFile.setFile_code(0);
                eventFile.setFile_tmp(fileTmp++);
                eventFile.setFile_name(photos[i]);
                eventFile.setFile_url(null);
                eventFile.setFile_url_local(photos[i]);

                eventFiles.add(eventFile);
            }
        }

        mSm_so_product_event.setSketch(sketches);
        mSm_so_product_event.setFile(eventFiles);
        mSm_so_product_event.setIntegrated(0);
        mSm_so_product_event.setStatus(Constant.SYS_STATUS_DONE);

        sm_so_product_eventDao.addUpdateTmp(mSm_so_product_event);

        processTaskStatus();

        /**
         * Calling WebService
         */
        SM_SODao soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        soDao.getByString(
                new SM_SO_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code()
                ).toSqlQuery()
        );

        uploadFiles(eventFiles);

        activateUpload(context);

        Act027_Main mMain = (Act027_Main) getActivity();

        mMain.setEventEditOpenStatus(false);

        if (ToolBox_Con.isOnline(context)) {
            mMain.cleanUpResults();
            mMain.executeSoSave();
        } else {
            //ToolBox_Inf.showNoConnectionDialog(context);
            //
            mMain.setProductListFragOffLine();
        }
    }

    private void uploadFiles(ArrayList<SM_SO_Product_Event_File> eventFiles) {
        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        ArrayList<GE_File> geFiles = new ArrayList<>();

        for (int i = 0; i < eventFiles.size(); i++) {
            String sFile_v = eventFiles.get(i).getFile_name();

            if (sFile_v.endsWith(".jpg")) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_v);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_v.replace(".jpg", ""));
                    geFile.setFile_path(sFile_v);
                    geFile.setFile_status("OPENED");
                    geFile.setFile_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss"));

                    geFiles.add(geFile);
                }
            }
        }

        geFileDao.addUpdate(geFiles, false);

    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void informEventError(String msg) {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_product_edit_error_ttl"),
                msg,
                null,
                -1
        );
    }

    private void hideKeyBoard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void removeEventPhotosOnLeave() {
        ToolBox_Inf.deleteFileListExceptionSafe(null, (String) iv_gallery.getTag(), null);
    }

    public String getEventStatus() {
        if ((mSm_so_product_event == null || mSm_so_product_event.getStatus().isEmpty() || mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING))
        && !hasSOStatusStopOrEdit()) {
            return EVENT_EDIT_MODE;
        } else {
            return mSm_so_product_event.getStatus();
        }
    }

}