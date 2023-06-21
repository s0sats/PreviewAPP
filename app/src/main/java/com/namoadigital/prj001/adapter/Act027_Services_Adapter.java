package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.sql.Sql_Act027_001;
import com.namoadigital.prj001.sql.Sql_Act027_002;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act027_Services_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    private String mResource_Code;
    private HMAux hmAux_Trans;
    private boolean hasExecutionProfile;
    private boolean needSync;

    public Act027_Services_Adapter(Context context, int resource, List<HMAux> source, boolean hasExecutionProfile, boolean needSync) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.hasExecutionProfile = hasExecutionProfile;
        this.needSync = needSync;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "act027_service_adapter"
        );

        loadTranslation();
    }

    public Act027_Services_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "act027_service_adapter"
        );

        loadTranslation();
    }

    public interface IAct027_Services_Adapter {
        void serviceSelected(HMAux item, String selection_type);
    }

    private IAct027_Services_Adapter delegate;

    public void setOnServiceSelectedListener(IAct027_Services_Adapter delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int position) {
        return source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    public int getPositionByPk(String pk) {
        for (int i = 0; i < source.size(); i++) {
            HMAux auxItem = source.get(i);
            String auxPk =
                    auxItem.get(SM_SO_ServiceDao.CUSTOMER_CODE) + "|" +
                            auxItem.get(SM_SO_ServiceDao.SO_PREFIX) + "|" +
                            auxItem.get(SM_SO_ServiceDao.SO_CODE) + "|" +
                            auxItem.get(SM_SO_ServiceDao.PRICE_LIST_CODE) + "|" +
                            auxItem.get(SM_SO_ServiceDao.PACK_CODE) + "|" +
                            auxItem.get(SM_SO_ServiceDao.PACK_SEQ) + "|" +
                            auxItem.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE) + "|" +
                            auxItem.get(SM_SO_ServiceDao.SERVICE_CODE) + "|" +
                            auxItem.get(SM_SO_ServiceDao.SERVICE_SEQ);

            if (auxPk.equals(pk)) {
                return i;
            }
        }
        //
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);

            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = source.get(position);

        LinearLayout ll_bg = (LinearLayout) convertView.findViewById(R.id.act027_services_content_cell_ll_bg);
        LinearLayout ll_done_tv = convertView.findViewById(R.id.act027_services_content_cell_layout_tv_done);
        ImageView icon_done = convertView.findViewById(R.id.act027_services_content_cell_icon_done);
        MaterialButton btn_clip = convertView.findViewById(R.id.act027_services_content_cell_btn_clip);
        TextView tv_done = convertView.findViewById(R.id.act027_services_content_cell_tv_done);
        //TextView exec_seq_oper =  (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_exec_seq_oper);
//        LinearLayout ll_express = (LinearLayout) convertView.findViewById(R.id.act027_services_content_cell_ll_express);
        MaterialButton btn_confirm = convertView.findViewById(R.id.act027_services_content_cell_btn_normal);
        MaterialButton btn_options = convertView.findViewById(R.id.act027_services_content_cell_btn_options);
        MaterialButton btn_done = convertView.findViewById(R.id.act027_services_content_cell_btn_done);

        ImageView iv_flag = convertView.findViewById(R.id.act027_services_content_cell_iv_flag);
//        ImageView iv_plus = (ImageView) convertView.findViewById(R.id.act027_services_content_cell_iv_plus);
//        TextView tv_express_badge = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_badge);

//        TextView tv_pack_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_lbl);
//        TextView tv_pack_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_val);

        LinearLayout ll_zone = (LinearLayout) convertView.findViewById(R.id.act027_services_content_cell_ll_zone);
//        TextView tv_zone_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_zone_lbl);
        TextView tv_zone_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_zone_val);

//        TextView tv_service_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_service_lbl);
        TextView tv_service_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_service_val);

        //ConstraintLayout ll_comment =  convertView.findViewById(R.id.act027_services_content_cell_ll_comment);
//        TextView tv_comment_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_comment_lbl);
        TextView tv_comment_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_comment_val);

        LinearLayout ll_partner = (LinearLayout) convertView.findViewById(R.id.act027_services_content_cell_ll_partner);
//        TextView tv_partner_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_partner_lbl);
        TextView tv_partner_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_partner_val);

//        TextView tv_status_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_status);

//        TextView tv_qty_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_qty_lbl);
        TextView tv_qty_val = convertView.findViewById(R.id.act027_services_content_cell_tv_qty_val);
        ImageView iv_qty_icon = convertView.findViewById(R.id.act027_services_content_cell_tv_qty_val_icon);
        LinearLayout ll_qty_item = convertView.findViewById(R.id.act027_services_content_cell_ll_qty);
        View view_padding = convertView.findViewById(R.id.view_padding_qty_between_zone);

        //Seta valores
        //exec_seq_oper.setText(item.get(SM_SO_ServiceDao.EXEC_SEQ_OPER));

        //tv_pack_lbl.setText(hmAux_Trans.get("pack_id_lbl"));
        // tv_pack_val.setText(item.get(SM_SO_PackDao.PACK_DESC));

        //tv_zone_lbl.setText(hmAux_Trans.get("zone_lbl"));
        //06/08/18 - Se site do serviço diferente do site do logado, muda cor de fundo.
        if (item.get(SM_SO_ServiceDao.SITE_CODE) != null && !item.get(SM_SO_ServiceDao.SITE_CODE).isEmpty() && !item.get(SM_SO_ServiceDao.SITE_CODE).equals(ToolBox_Con.getPreference_Site_Code(context))) {
            ll_bg.setBackground(context.getDrawable(R.drawable.namoa_orange_bg_bordered));
        } else {
            //
            ll_bg.setBackground(context.getDrawable(R.drawable.namoa_cell_8));
        }
        //
        if ((item.get(SM_SO_ServiceDao.SITE_CODE) != null && item.get(SM_SO_ServiceDao.SITE_CODE).length() > 0)) {
            ll_zone.setVisibility(View.VISIBLE);
            //Linha abaixo, so com info de zone, é o esquema ate 06/08/18
            //tv_zone_val.setText(item.get(SM_SO_ServiceDao.ZONE_ID) + " - " + item.get(SM_SO_ServiceDao.ZONE_DESC));
            //06/08/18 , agora será exibido a informação de site_desc + zone_desc
            String site_zone_desc = "";
            site_zone_desc += item.get(SM_SO_ServiceDao.SITE_CODE) != null ? item.get(SM_SO_ServiceDao.SITE_DESC) : site_zone_desc;
            site_zone_desc += item.get(SM_SO_ServiceDao.SITE_CODE) != null && item.get(SM_SO_ServiceDao.ZONE_CODE) != null ? " | " : "";
            site_zone_desc += item.get(SM_SO_ServiceDao.ZONE_CODE) != null ? item.get(SM_SO_ServiceDao.ZONE_DESC) : site_zone_desc;
            tv_zone_val.setText(site_zone_desc);
            //Monta variaveis de comparação
            String site_zone_row = item.get(SM_SO_ServiceDao.SITE_CODE) + "|" + item.get(SM_SO_ServiceDao.ZONE_CODE);
            String site_zone_pref = ToolBox_Con.getPreference_Site_Code(context) + "|" + ToolBox_Con.getPreference_Zone_Code(context);
            //
            if (site_zone_row.equals(site_zone_pref)) {
                tv_zone_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
            } else {
                tv_zone_val.setTextColor(context.getResources().getColor(R.color.namoa_color_danger_red));
            }
            tv_zone_val.setVisibility(View.VISIBLE);
        } else {
            ll_zone.setVisibility(View.GONE);
            tv_zone_val.setVisibility(View.GONE);
        }

        // tv_service_lbl.setText(hmAux_Trans.get("service_lbl"));
        tv_service_val.setText(item.get(SM_SO_ServiceDao.SERVICE_DESC));

        //  tv_comment_lbl.setText(hmAux_Trans.get("comment_lbl"));
        if (item.get(SM_SO_ServiceDao.COMMENTS) != null && item.get(SM_SO_ServiceDao.COMMENTS).length() > 0 && !item.get(SM_SO_ServiceDao.COMMENTS).isEmpty()) {
            tv_comment_val.setVisibility(View.VISIBLE);
            tv_comment_val.setText(item.get(SM_SO_ServiceDao.COMMENTS));
        } else {
            tv_comment_val.setVisibility(View.GONE);
        }

        if (item.get(SM_SO_ServiceDao.PARTNER_CODE) != null && item.get(SM_SO_ServiceDao.PARTNER_CODE).length() > 0) {
            ll_partner.setVisibility(View.VISIBLE);
            //         tv_partner_lbl.setText(hmAux_Trans.get("partner_lbl"));
            tv_partner_val.setText(item.get(SM_SO_ServiceDao.PARTNER_DESC));
            tv_partner_val.setVisibility(View.VISIBLE);
        } else {
            ll_partner.setVisibility(View.GONE);
            //        tv_partner_lbl.setText(hmAux_Trans.get("partner_lbl"));
            tv_partner_val.setVisibility(View.GONE);
        }

        //    tv_status_value.setText(hmAux_Trans.get(item.get(SM_SO_ServiceDao.STATUS)));
        //chama metodo que define a cor do status
        //     ToolBox_Inf.setServiceStatusColor(context, tv_status_value, item.get(SM_SO_ServiceDao.STATUS) );

        //     tv_qty_lbl.setText(hmAux_Trans.get("qty_lbl"));
        int todoService = Integer.parseInt(item.get(SM_SO_ServiceDao.QTY));
        int doneServices = Integer.parseInt(item.get(Sql_Act027_001.QTY_DONE));

        btn_confirm.setEnabled(checkIfIsSyncOrDifferentProccess(item));

        if (item.get(SM_SO_ServiceDao.STATUS).equals(Constant.SYS_STATUS_DONE)) {

            if (item.get(Sql_Act027_002.SERVICE_WITH_COMMENT_OR_PHOTO) != null && !item.get(Sql_Act027_002.SERVICE_WITH_COMMENT_OR_PHOTO).isEmpty() && item.get(Sql_Act027_002.SERVICE_WITH_COMMENT_OR_PHOTO).equals("1")) {
                btn_clip.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_extended_verdeDone_seed)));
                btn_clip.setVisibility(View.VISIBLE);
            } else {
                btn_clip.setVisibility(View.GONE);
            }


            if (doneServices == todoService && todoService > 1) {
                ll_qty_item.setVisibility(View.VISIBLE);
                tv_qty_val.setText(doneServices + " / " + todoService + "  -  " + ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.get(Sql_Act027_002.SERVICE_DONE_DATE)),
                        ToolBox_Inf.nlsDateFormat(context)
                ));
            } else {
                tv_qty_val.setText(ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.get(Sql_Act027_002.SERVICE_DONE_DATE)),
                        ToolBox_Inf.nlsDateFormat(context)
                ));
            }
            tv_qty_val.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_extended_verdeDone_seed));
            iv_qty_icon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_extended_verdeDone_seed)));
            ll_qty_item.setVisibility(View.VISIBLE);
        } else {
            btn_clip.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_onSurfaceVariant)));
            iv_qty_icon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_onSurfaceVariant)));
            if (todoService <= 1) {
                ll_qty_item.setVisibility(View.GONE);
            } else {
                if (doneServices == todoService) {
                    tv_qty_val.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_extended_verdeDone_seed));
                } else {
                    tv_qty_val.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_onSurfaceVariant));
                }
                ll_qty_item.setVisibility(View.VISIBLE);
            }
            tv_qty_val.setText(doneServices + " / " + todoService);
        }


        //Icones
        //Flag
        if (item.get(Sql_Act027_001.SET_FLAG) != null && !item.get(Sql_Act027_001.SET_FLAG).equals("0")) {
            iv_flag.setVisibility(View.VISIBLE);
        } else {
            iv_flag.setVisibility(View.GONE);
        }
        /*
         *
         * APLICAÇÃO DO PROFILE DE  EXECUÇÃO
         *
         */
        if (!hasExecutionProfile) {
            //         ll_express.setVisibility(View.VISIBLE);
            //         btn_confirm.setImageDrawable(null);
            //        btn_confirm.setOnClickListener(null);
            //        iv_plus.setVisibility(View.INVISIBLE);
            btn_confirm.setVisibility(View.GONE);
            //
            btn_options.setTag(item);
            btn_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HMAux item = (HMAux) v.getTag();
                    //
                    if (delegate != null) {
                        delegate.serviceSelected(item, Act027_Main.SELECTION_NORMAL);
                    }
                }
            });
        } else {
            //
            if (item.get(SM_SO_ServiceDao.STATUS).equals(Constant.SYS_STATUS_PENDING)) {
                btn_confirm.setVisibility(View.VISIBLE);
                btn_clip.setVisibility(View.VISIBLE);
                btn_done.setVisibility(View.GONE);
                //
            /*if (item.get(SM_SO_ServiceDao.EXEC_TYPE).equals(Constant.SO_SERVICE_TYPE_YES_NO)) {
                btn_confirm.setVisibility(View.VISIBLE);

            } else {
                btn_confirm.setVisibility(View.VISIBLE);
                btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_play_circle_filled_black_24dp));
                //btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_stop_circle_black_24px));
            }*/
                if (item.get(SM_SO_ServiceDao.EXEC_TYPE).equals(Constant.SO_SERVICE_TYPE_YES_NO)) {
//                if(item.get(Sql_Act027_002.YES_NO_ICON).equals("1")){
//                    ll_express.setVisibility(View.VISIBLE);
//                    //btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_ok_ns_states));
//                    btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_ok_azul_ns_states));
//                }else{
//                    //ll_express.setVisibility(View.GONE);
//                    ll_express.setVisibility(View.VISIBLE);
//                    //btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_escolher_ns_states));
//                    btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_escolher_azul_ns_states));
//                }
                    //
                    //         ll_express.setVisibility(View.VISIBLE);
                    //          btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_ok_azul_ns_states));
                    btn_confirm.setVisibility(View.VISIBLE);
                    btn_confirm.setText(hmAux_Trans.get("confirm_lbl"));
                    btn_confirm.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_done_24));
                    btn_confirm.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.drawable.button_theme_primary));
                    btn_confirm.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_surface));
                    btn_confirm.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_surface)));
                } else {

                    if (item.get(Sql_Act027_002.START_STOP_ICON).equals(Sql_Act027_002.ACTION_PLAY)) {
                        //              ll_express.setVisibility(View.VISIBLE);
                        //btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_play_ns_states));
                        //               btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_play_stop_azul_ns_states));
                        btn_confirm.setVisibility(View.VISIBLE);
                        btn_confirm.setText(hmAux_Trans.get("start_lbl"));
                        btn_confirm.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.drawable.button_theme_primary));
                        btn_confirm.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_baseline_play_arrow_24dp));
                        btn_confirm.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_surface));
                        btn_confirm.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_surface)));
                    } else if (item.get(Sql_Act027_002.START_STOP_ICON).equals(Sql_Act027_002.ACTION_STOP)) {
                        //               ll_express.setVisibility(View.VISIBLE);
                        //btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_finaliza_play_ns_states));
                        //               btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_play_stop_laranja_ns_states));
                        btn_confirm.setVisibility(View.VISIBLE);
                        btn_confirm.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.drawable.button_theme_required));
                        btn_confirm.setText(hmAux_Trans.get("resume_lbl"));
                        btn_confirm.setIcon(ContextCompat.getDrawable(context, R.drawable.resume_48px));
                        btn_confirm.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_onSurface));
                        btn_confirm.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_onSurface)));
                    } else {
                        //ll_express.setVisibility(View.GONE);
                        //              ll_express.setVisibility(View.VISIBLE);
                        //              btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_play_stop_azul_ns_states));
                        btn_confirm.setVisibility(View.VISIBLE);
                        btn_confirm.setText(hmAux_Trans.get("start_lbl"));
                        btn_confirm.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.m3_namoa_surface)));
                        btn_confirm.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_baseline_play_arrow_24dp));
                    }
                }
            } else {

                if (item.get(SM_SO_ServiceDao.STATUS).equals(Constant.SYS_STATUS_DONE)) {
                    btn_done.setVisibility(View.VISIBLE);
                    btn_done.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_done_24));

                    /*if (item.get(SM_SO_ServiceDao.EXEC_TYPE).equals(Constant.SO_SERVICE_TYPE_YES_NO)) {
          //              ll_express.setVisibility(View.VISIBLE);
          //              btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_ok_ns_states));

                    } else {
                        // ll_express.setVisibility(View.INVISIBLE);
                        // btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_escolher_ns_states));
          //              ll_express.setVisibility(View.VISIBLE);
          //              btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_play_stop_verde_ns_states));
                        btn_confirm.setVisibility(View.GONE);
                        ll_done_tv.setVisibility(View.VISIBLE);
                        icon_done.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.play_pause_48px));
                    }*/
                    btn_confirm.setVisibility(View.GONE);
                    btn_done.setText(hmAux_Trans.get("done_lbl"));
                } else {
                    btn_confirm.setVisibility(View.VISIBLE);
                    btn_confirm.setEnabled(false);
                    btn_done.setVisibility(View.GONE);
                    //btn_confirm.setVisibility(View.GONE);
                    //           ll_express.setVisibility(View.GONE);
                    //           btn_confirm.setImageDrawable(context.getDrawable(R.drawable.ic_ok_ns_states));

                }

            }
            //Add Badge
            if (ToolBox_Inf.convertStringToInt(item.get(SM_SO_ServiceDao.QTY)) > 1
                    && item.get(SM_SO_ServiceDao.EXEC_TYPE).equals(Constant.SO_SERVICE_TYPE_START_STOP)) {
                String qty = item.get(SM_SO_ServiceDao.QTY);
                if (item.get(SM_SO_ServiceDao.QTY).length() == 1) {
                    qty = " " + qty + " ";
                }
                //        tv_express_badge.setVisibility(View.GONE);
                //        tv_express_badge.setText(qty);
                //
                //       iv_plus.setVisibility(View.VISIBLE);

            } else {
                //        tv_express_badge.setVisibility(View.GONE);
                //        tv_express_badge.setText("");
                //       iv_plus.setVisibility(View.GONE);
            }
            //
            btn_confirm.setTag(item);
            btn_confirm.setOnClickListener(v -> {
                HMAux item12 = (HMAux) v.getTag();
                //
                if (delegate != null) {
                    //06/08/18 - Aplicado a restrição de execução de site do serviço diferente do site logado
                    //Caso o status do serviço seja diferente de pendente ou
                    // seja pendente, mas não tenha restrição de execução, chama metodo express.
                    //Caso haja restrição, emite dialog informando restrição
                    if (!item12.get(SM_SO_ServiceDao.STATUS).equals(Constant.SYS_STATUS_PENDING)
                            || !ToolBox_Inf.hasServiceSiteRestriction(context, item12.get(SM_SO_ServiceDao.SITE_CODE), hmAux_Trans)
                    ) {
                        delegate.serviceSelected(item12, Act027_Main.SELECTION_EXPRESS);
                    }
                }
            });


            btn_clip.setTag(item);
            btn_clip.setOnClickListener(acess_028_default);
            //
            ll_bg.setTag(item);
            ll_bg.setOnClickListener(acess_028_default);
            //
            btn_done.setTag(item);
            btn_done.setOnClickListener(v -> {
                HMAux item1 = (HMAux) v.getTag();
                //
                if (delegate != null) {
                    delegate.serviceSelected(item1, Act027_Main.SELECTION_NORMAL);
                }
            });

            //
            btn_options.setTag(item);
            btn_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HMAux item = (HMAux) v.getTag();
                    //
                    if (delegate != null) {
                        delegate.serviceSelected(item, Act027_Main.SELECTION_NORMAL);
                    }
                }
            });
        }


        if (tv_zone_val.getVisibility() == View.GONE && tv_partner_val.getVisibility() == View.GONE) {
            view_padding.setVisibility(View.GONE);
        } else {
            view_padding.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private boolean checkIfIsSyncOrDifferentProccess(HMAux item) {
        return !needSync
                &&
                (
                        (
                                item.get(SM_SO_ServiceDao.STATUS).equals(Constant.SYS_STATUS_PENDING)
                                        ||
                                        item.get(SM_SO_ServiceDao.STATUS).equals(Constant.SYS_STATUS_PROCESS)
                        )
                                &&
                                (
                                        item.get(Sql_Act027_002.SO_STATUS).equals(Constant.SYS_STATUS_PENDING)
                                                ||
                                                item.get(Sql_Act027_002.SO_STATUS).equals(Constant.SYS_STATUS_PROCESS)
                                )
                );
    }

    private View.OnClickListener acess_028_default = v -> {
        HMAux item1 = (HMAux) v.getTag();
        //
        if (delegate != null) {
            if (!item1.get(SM_SO_ServiceDao.STATUS).equals(Constant.SYS_STATUS_PENDING)
                    || !ToolBox_Inf.hasServiceSiteRestriction(context, item1.get(SM_SO_ServiceDao.SITE_CODE), hmAux_Trans)
            ) {
                delegate.serviceSelected(item1, Act027_Main.SELECTION_EXPRESS);
            }
        }
    };

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("service_lbl");
        translateList.add("plc_pc_ps_sc_lbl");
        translateList.add("price_list_id_lbl");
        translateList.add("pack_id_lbl");
        translateList.add("zone_lbl");
        translateList.add("comment_lbl");
        translateList.add("partner_lbl");
        translateList.add("qty_lbl");
        translateList.add("start_lbl");
        translateList.add("confirm_lbl");
        translateList.add("done_lbl");
        translateList.add("resume_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }


}
