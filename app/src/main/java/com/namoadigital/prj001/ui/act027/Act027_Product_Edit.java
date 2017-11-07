package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.ApplyRepairImageFF;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.PictureFF;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Gallery_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_SketchDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Product_Event;
import com.namoadigital.prj001.model.SM_SO_Product_Event_File;
import com.namoadigital.prj001.model.SM_SO_Product_Event_Sketch;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 31/10/17.
 */

public class Act027_Product_Edit extends BaseFragment {

    private Context context;

    private TextView tv_id_ttl;
    private TextView tv_desc_ttl;
    private ApplyRepairImageFF arff_applyrepair;
    private CheckBox cb_inspection;
    private MKEditTextNM mk_qty;
    private PictureFF pff_sketch;
    private TextView tv_comments_lbl;
    private MKEditTextNM mk_comments;
    private ImageView iv_gallery;
    private ImageView iv_save;

    private MD_ProductDao md_productDao;
    private SM_SO_Product_EventDao sm_so_product_eventDao;
    private SM_SO_Product_Event_FileDao sm_so_product_event_fileDao;
    private SM_SO_Product_Event_SketchDao sm_so_product_event_sketchDao;

    private SM_SO mSm_so;
    private MD_Product md_product;
    private SM_SO_Product_Event mSm_so_product_event;

    private int mProductCode = -1;
    private int mSeqTmp = -1;

    private boolean sdAvoid = false;
    private boolean bStatus = false;

    private StringBuilder sFiles;
    private StringBuilder sSketchs;

    private String sFileName;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public void setProductEventPk(int product_code, int seq_tmp) {
        mProductCode = product_code;
        mSeqTmp = seq_tmp;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act027_product_edit_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

    private void iniVar(View view) {

        context = getActivity();

        tv_id_ttl = (TextView) view.findViewById(R.id.act027_product_edit_content_tv_id_ttl);
        tv_desc_ttl = (TextView) view.findViewById(R.id.act027_product_edit_content_tv_desc_ttl);
        arff_applyrepair = (ApplyRepairImageFF) view.findViewById(R.id.act027_product_edit_content_arff_applyrepair);
        cb_inspection = (CheckBox) view.findViewById(R.id.act027_product_edit_content_cb_inspection);
        mk_qty = (MKEditTextNM) view.findViewById(R.id.act027_product_edit_content_mk_qty);
        pff_sketch = (PictureFF) view.findViewById(R.id.act027_product_edit_content_pff_sketch);
        tv_comments_lbl = (TextView) view.findViewById(R.id.act027_product_edit_content_tv_comments_lbl);
        mk_comments = (MKEditTextNM) view.findViewById(R.id.act027_product_edit_content_mk_comments);
        iv_gallery = (ImageView) view.findViewById(R.id.act027_product_edit_content_iv_gallery);
        iv_save = (ImageView) view.findViewById(R.id.act027_product_edit_content_iv_save);

        md_productDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

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

            // Tirar
            MD_Product md_product = md_productDao.getByString(
                    new MD_Product_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            (long) mProductCode

                    ).toSqlQuery()
            );

            sFileName = md_product.getSketch_url_local();

        } else {
            MD_Product md_product = md_productDao.getByString(
                    new MD_Product_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            (long) mProductCode

                    ).toSqlQuery()
            );

            mSm_so_product_event = new SM_SO_Product_Event();
        }

        mSm_so_product_event.setSketch_url_local(sFileName);

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

        mk_comments.setText(mSm_so_product_event.getComments());

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
        }

        pff_sketch.setmValue(
                ToolBox.converterToJson(sSketchs.toString())
        );

        pff_sketch.setmIv_Dots(0);
        pff_sketch.setmV_Line(0);

        if (mSm_so_product_event.getFlag_apply() == 1) {
            arff_applyrepair.setmValue("10");
            mk_qty.setText(mSm_so_product_event.getQty_apply());
            //
            mk_qty.setVisibility(View.VISIBLE);
        } else if (mSm_so_product_event.getFlag_repair() == 1) {
            arff_applyrepair.setmValue("01");
            mk_qty.setVisibility(View.INVISIBLE);
        } else {
            arff_applyrepair.setmValue("00");
            mk_qty.setVisibility(View.INVISIBLE);
        }

        if (mSm_so_product_event.getFlag_inspection() == 1) {
            cb_inspection.setChecked(true);
        } else {
            cb_inspection.setChecked(false);
        }

        arff_applyrepair.setmIv_Dots(0);
        arff_applyrepair.setmV_Line(0);

        tempValues.put("mk_comments", mk_comments.getText().toString());
        tempValues.put("mk_qty", mk_qty.getText().toString());
        tempValues.put("img", (String) iv_gallery.getTag());
    }

    private void iniAction() {

        arff_applyrepair.setOnSelectionChangedListener(new ApplyRepairImageFF.IApplyRepairImageFF() {
            @Override
            public void selectionChanged(String status) {

                switch (status) {
                    case "10":
                        mk_qty.setVisibility(View.VISIBLE);
                        break;
                    case "01":
                        mk_qty.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        mk_qty.setVisibility(View.INVISIBLE);
                        break;
                }

            }
        });
    }

    @Override
    public void loadDataToScreen() {

        if (bStatus) {

            tv_id_ttl.setText(mSm_so_product_event.getProduct_id());
            tv_desc_ttl.setText(mSm_so_product_event.getProduct_desc());

            try {
                if (widgetset) {
                    widgetset = false;
                } else {
                    mk_comments.setText(tempValues.get("mk_comments"));
                }

                if (tempValues.get("mk_qty") != null && !tempValues.get("mk_qty").isEmpty()) {
                    mk_qty.setText(tempValues.get("mk_qty"));
                }

                if (sdAvoid) {
                    sdAvoid = false;
                } else {
                    iv_gallery.setTag(tempValues.get("img"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            upImgGallery();
        }
    }

    private void upImgGallery() {
        if (((String) iv_gallery.getTag()).equalsIgnoreCase("")) {
            iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_foto_ns));
        } else {
            iv_gallery.setBackground(context.getResources().getDrawable(R.drawable.ic_foto_marcada_ns));
        }

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCamera();
            }
        });
    }

    @Override
    public void loadScreenToData() {
        if (bStatus) {
            tempValues.put("mk_comments", mk_comments.getText().toString());
            tempValues.put("mk_qty", mk_qty.getText().toString());
            tempValues.put("img", (String) iv_gallery.getTag());
        }
    }

    private void callCamera() {
        sdAvoid = true;

        Intent mIntent = new Intent(context, Gallery_Activity.class);
        mIntent.putExtra(ConstantBase.PID, iv_gallery.getId());
        mIntent.putExtra(ConstantBase.PTYPE, 10);
        mIntent.putExtra(ConstantBase.PPATH, (String) iv_gallery.getTag());
        mIntent.putExtra(ConstantBase.MPRE, "pp");

        if (mSm_so_product_event.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PENDING)) {
            mIntent.putExtra(ConstantBase.PENABLED, true);
        } else {
            mIntent.putExtra(ConstantBase.PENABLED, false);
        }

        mIntent.putExtra(ConstantBase.MAXIMAGES, 4);
        //
        context.startActivity(mIntent);
    }


}
