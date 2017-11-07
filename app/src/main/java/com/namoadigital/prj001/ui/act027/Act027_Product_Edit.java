package com.namoadigital.prj001.ui.act027;

import android.content.Context;
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
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO;

/**
 * Created by neomatrix on 31/10/17.
 */

public class Act027_Product_Edit extends BaseFragment{

    private Context context;

    private TextView tv_id_ttl;
    private TextView tv_desc_ttl;
    private ApplyRepairImageFF arff_applyrepair;
    private CheckBox cb_inspection;
    private MKEditTextNM mk_qtd;
    private PictureFF pff_sketch;
    private TextView tv_comments_lbl;
    private MKEditTextNM mk_comments;
    private ImageView iv_gallery;
    private ImageView iv_save;

    private SM_SO mSm_so;
    private int mProductCode = -1;
    private int mSeqTmp = -1;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public void setProductEventPk(int product_code, int seq_tmp){
        mProductCode = product_code;
        mSeqTmp = seq_tmp;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act027_product_edit_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {


//        sketch = (PictureFF) view.findViewById(R.id.act027_product_edit_sketch);
//        sketch.setmLabel("Novo Titulo");
//        sketch.setmValue("1,1");
//        sketch.setmIv_Dots(0);
//        sketch.setmV_Line(0);
//        sketch.setmValue(ToolBox.converterToJson("1,1#2,2#2,5"));
//
//        applyRepairImageFF = (ApplyRepairImageFF) view.findViewById(R.id.act027_product_edit_applyrepair);
//        applyRepairImageFF.setmLabel("Novo Titulo");
//        applyRepairImageFF.setIv_dots(false);
//        applyRepairImageFF.setV_Line(false);
//
//        applyRepairImageFF.setmIv_Dots(0);
//        applyRepairImageFF.setmV_Line(0);
//
//        applyRepairImageFF.setmValue("01");

    }

    private void iniAction() {
    }
}
