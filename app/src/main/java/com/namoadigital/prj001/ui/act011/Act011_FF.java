package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.CustomFF;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.List;

/**
 * Created by neomatrix on 06/02/17.
 */

public class Act011_FF extends Fragment {

    private transient Context context;

    private transient TextView tv_schedule_desc;
    private transient TextView tv_comments;

    private transient LinearLayout ll_controls;

    private transient List<CustomFF> customFFs;

    private transient LinearLayout ll_footer;

    private transient LinearLayout ll_pre;
    private transient LinearLayout ll_nex;

    private transient LinearLayout ll_drawer;
    private transient LinearLayout ll_check;

    private transient TextView tv_check_new;
    private transient TextView tv_drawer;
    private transient TextView tv_check;
    private ImageView iv_product_serial_id;
    private TextView tv_product_serial_id;
    private TextView tv_product_serial_infos;
    private ImageView iv_editable_serial;
    private CardView cv_product_serial_card;
    private HMAux hmAux_Trans;

    private String formStatus = "";

    private int tabIndex = 0;

    private String comments;
    private String schedule_desc;
    //LUCHE - 17/01/2019 - RotateBugFixed
    private Act011_Main mAct = null;
    private Button go_to_history;
    private Button go_to_home;

    public void setHmAux_Trans(HMAux hmAux_Trans) {
        this.hmAux_Trans = hmAux_Trans;
    }

    public void setCustomFFs(List<CustomFF> customFFs, int indice) {
        this.customFFs = customFFs;
        this.tabIndex = indice;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setSchedule_desc(String schedule_desc) {
        this.schedule_desc = schedule_desc;
    }

    public interface ICustom_Form_FF_ll {
        public void openDrawer();

        public void check();

        public void previosTab();

        public void nextTab();

        public void checkWithNew();
    }

    private ICustom_Form_FF_ll delegate_ll;

    public void setOnDrawerCheckListener(ICustom_Form_FF_ll delegate_ll) {
        this.delegate_ll = delegate_ll;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act011_ff, container, false);
        /**
         * LUCHE - 17/01/2019 - RotateBugFixed
         *
         * Recupera dados de pagina, status e comentario do bundle quando o fragmento for ser
         * reconstruido.
         *
         */
        if(savedInstanceState != null &&
            savedInstanceState.containsKey(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS) &&
            savedInstanceState.containsKey(GE_Custom_Form_Field_LocalDao.PAGE) &&
            savedInstanceState.containsKey(GE_Custom_Form_Field_LocalDao.COMMENT) &&
            savedInstanceState.containsKey(MD_Schedule_ExecDao.SCHEDULE_DESC)
        ){
            formStatus = savedInstanceState.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS);
            tabIndex = savedInstanceState.getInt(GE_Custom_Form_Field_LocalDao.PAGE);
            comments = savedInstanceState.getString(GE_Custom_Form_Field_LocalDao.COMMENT);
            schedule_desc = savedInstanceState.getString(MD_Schedule_ExecDao.SCHEDULE_DESC);
        }
        //
        iniVars(view);
        iniActions();
        //
        return view;
    }

    private void iniVars(View view) {
        context = getActivity();
        mAct = ((Act011_Main)context);
        /**
         *  LUCHE - 17/01/2019 - RotateBugFixed
         *
         *  Se a hmAux_Trans for null, significa que o fragmento esta sendo reconstruido , então
         *  recupera informações primordiais atraves dos gets da Act011.
         *
         */
        if(hmAux_Trans == null){
            //if(context != null){
            //if(context instanceof Act011_Main){
                //mAct = ((Act011_Main)context);
                this.hmAux_Trans = mAct.getHmAuxTrans();
                this.setOnDrawerCheckListener(mAct.getFFInterface());
                this.customFFs = mAct.getFf();
            //}
        }
        //
        tv_schedule_desc = (TextView) view.findViewById(R.id.act011_ff_tv_schedule_desc);
        tv_comments = (TextView) view.findViewById(R.id.act011_ff_tv_comments);
        //
        ll_controls = (LinearLayout) view.findViewById(R.id.act011_ff_ll_controls);
        //
        ll_footer = (LinearLayout) view.findViewById(R.id.act011_ff_ll_footer);
        //
        go_to_history = (Button) view.findViewById(R.id.act011_go_to_history);
        go_to_home = (Button) view.findViewById(R.id.act011_go_to_home);
        //
        ll_pre = (LinearLayout) view.findViewById(R.id.act011_ff_ll_pre);
        ll_nex = (LinearLayout) view.findViewById(R.id.act011_ff_ll_nex);
        ll_drawer = (LinearLayout) view.findViewById(R.id.act011_ff_ll_drawer);
        ll_check = (LinearLayout) view.findViewById(R.id.act011_ff_ll_check);
        tv_drawer = (TextView) view.findViewById(R.id.act011_ff_tv_drawer);
        tv_check = (TextView) view.findViewById(R.id.act011_ff_tv_check);
        tv_check_new = (TextView) view.findViewById(R.id.act011_ff_tv_check_new);
        iv_product_serial_id = view.findViewById(R.id.iv_product_serial_id);
        tv_product_serial_id = view.findViewById(R.id.tv_product_serial_id);
        tv_product_serial_infos = view.findViewById(R.id.tv_product_serial_infos);
        iv_editable_serial = view.findViewById(R.id.iv_editable_serial);
        cv_product_serial_card = view.findViewById(R.id.cv_product_serial_card);
        //
        tv_drawer.setText(hmAux_Trans.get("btn_open_drawer"));
        tv_check.setText(hmAux_Trans.get("btn_check"));
        tv_check_new.setText(hmAux_Trans.get("btn_check_new"));
        //
        loadControls(ll_controls);
        if(tabIndex == 1) {
            cv_product_serial_card.setVisibility(View.VISIBLE);
            setSerialInfo();
        }else{
            cv_product_serial_card.setVisibility(View.GONE);
        }

        if(formStatus.equalsIgnoreCase(Constant.SYS_STATUS_DONE)){
            go_to_history.setText(hmAux_Trans.get("btn_history"));
            go_to_history.setVisibility(View.VISIBLE);
            go_to_home.setVisibility(View.VISIBLE);
            go_to_home.setText(hmAux_Trans.get("btn_home"));

        }else{
            go_to_history.setVisibility(View.GONE);
            go_to_home.setVisibility(View.GONE);
        }

    }

    private void iniActions() {
        ll_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.openDrawer();
                }
            }
        });

        ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.check();
                }
            }
        });

        ll_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.previosTab();
                }
            }
        });

        ll_nex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.nextTab();
                }
            }
        });
        //
        tv_check_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.checkWithNew();
                }
            }
        });
        //
        go_to_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAct.callAct015();
            }
        });
        //
        go_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAct.callAct005(context);
            }
        });
    }

    private void loadControls(LinearLayout ll_controls) {
        if (customFFs != null) {

            int count = 0;

            for (CustomFF fAux : customFFs) {
                if (fAux.getmPage() == tabIndex) {

                    if (fAux.getmInclude() == 1) {
                        count++;

                        fAux.setmLabel(String.valueOf(count) + ". " + fAux.getmLabel());
                    }

                    /**
                     *  LUCHE - 17/01/2019 - RotateBugFixed
                     */

//                    try {
//                        ((ViewGroup) fAux.getParent()).removeView(fAux);
//                    } catch (Exception e) {
//                        //ToolBox_Inf.registerException(getClass().getName(),e);
//                        e.printStackTrace();
//                    }
                    //
                    ll_controls.addView(fAux);
                } else {
                }
            }

            if (tabIndex == customFFs.get(customFFs.size() - 1).getmPage()) {
                ll_footer.setVisibility(View.VISIBLE);
                ll_nex.setVisibility(View.GONE);
                ll_pre.setVisibility(View.VISIBLE);
            } else {
                ll_footer.setVisibility(View.GONE);
                //
                if (tabIndex == 1){
                    ll_nex.setVisibility(View.VISIBLE);
                    ll_pre.setVisibility(View.GONE);
                } else {
                    ll_nex.setVisibility(View.VISIBLE);
                    ll_pre.setVisibility(View.VISIBLE);
                }
            }

            if (tabIndex == customFFs.get(customFFs.size() - 1).getmPage() && customFFs.get(0).getmPage() == customFFs.get(customFFs.size() - 1).getmPage()){
                ll_nex.setVisibility(View.GONE);
                ll_pre.setVisibility(View.GONE);
            }
            //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
            //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
            if (formStatus.equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                    formStatus.equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//                 || formStatus.equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
                    ) {
                ll_check.setVisibility(View.GONE);
                tv_check_new.setVisibility(View.GONE);
            } else {
                ll_check.setVisibility(View.VISIBLE);
                if(mAct != null && mAct.allowFinalizeWithNewBtn()){
                    tv_check_new.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * Metodo que popula as informações do card de Produto Serial
     */
    private void setSerialInfo() {
        if (mAct != null) {
            iv_editable_serial.setVisibility(View.GONE);
            MD_Product_Serial serialInfo = mAct.getSerialInfo();
            String serial_id = serialInfo.getSerial_id();
            //Se existe serial, tenta preencher infos de Brand ,model e color
            if(serial_id != null && !serial_id.isEmpty()) {
                try {
                    tv_product_serial_id.setText(serial_id);
                    String serial_bmd =
                        ToolBox_Inf.formatSerialBrandModelColor(serialInfo) != null ? ToolBox_Inf.formatSerialBrandModelColor(serialInfo) : "" ;
                    tv_product_serial_infos.setText(serial_bmd);
                    tv_product_serial_infos.setVisibility(View.VISIBLE);
                    if (serial_bmd.isEmpty()) {
                        tv_product_serial_infos.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    tv_product_serial_id.setText(hmAux_Trans.get("lbl_no_serial_placeholder"));
                    tv_product_serial_infos.setText("");
                    tv_product_serial_infos.setVisibility(View.GONE);
                }
            } else{
                tv_product_serial_id.setText(hmAux_Trans.get("lbl_no_serial_placeholder"));
                tv_product_serial_infos.setText("");
                tv_product_serial_infos.setVisibility(View.GONE);
            }
            //Defini a exibição dos dados do produto
            String product_icon_name = mAct.getProduct_icon();
            if (product_icon_name != null && !product_icon_name.isEmpty()) {
                if (ToolBox_Inf.verifyDownloadFileInf(product_icon_name, Constant.CACHE_PATH)) {
                    File imgFile = new File(Constant.CACHE_PATH + "/" + product_icon_name);
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        iv_product_serial_id.setImageBitmap(myBitmap);
                    }
                }
            } else {
                iv_product_serial_id.setVisibility(View.GONE);
                if(serial_id == null || serial_id.isEmpty()) {
                    tv_product_serial_id.setVisibility(View.GONE);
                    cv_product_serial_card.setVisibility(View.GONE);
                }
                tv_product_serial_infos.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv_product_serial_id.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        tv_schedule_desc.setText(schedule_desc);
        tv_comments.setText(comments);
        //
        if (comments.length() > 0){
            tv_comments.setVisibility(View.VISIBLE);
        } else {
            tv_comments.setVisibility(View.GONE);
        }

        if (schedule_desc.length() > 0){
            tv_schedule_desc.setVisibility(View.VISIBLE);
        } else {
            tv_schedule_desc.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 17/01/2019 - RotateBugFixed
     *
     * Adicionado propriedades formStatus, tabIndex e comments no bundle do SaveInstance do fragment
     * @param outState
     */

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //
        outState.putString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS,formStatus);
        outState.putInt(GE_Custom_Form_Field_LocalDao.PAGE,tabIndex);
        outState.putString(GE_Custom_Form_Field_LocalDao.COMMENT,comments);
        outState.putString(MD_Schedule_ExecDao.SCHEDULE_DESC,schedule_desc);
    }
}
