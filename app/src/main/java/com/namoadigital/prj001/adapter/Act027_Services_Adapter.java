package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.sql.Sql_Act027_001;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);

            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = source.get(position);

        LinearLayout ll_bg = (LinearLayout) convertView.findViewById(R.id.act027_services_content_cell_ll_bg);

        TextView exec_seq_oper =  (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_exec_seq_oper);
        ImageView iv_express = (ImageView) convertView.findViewById(R.id.act027_services_content_cell_btn_express);
        ImageView iv_normal = (ImageView) convertView.findViewById(R.id.act027_services_content_cell_btn_normal);
        ImageView iv_flag = (ImageView) convertView.findViewById(R.id.act027_services_content_cell_iv_flag);

        TextView tv_pack_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_lbl);
        TextView tv_pack_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_val);

        TextView tv_zone_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_zone_lbl);
        TextView tv_zone_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_zone_val);

        TextView tv_service_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_service_lbl);
        TextView tv_service_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_service_val);

        LinearLayout ll_comment = (LinearLayout) convertView.findViewById(R.id.act027_services_content_cell_ll_comment);
        TextView tv_comment_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_comment_lbl);
        TextView tv_comment_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_comment_val);

        LinearLayout ll_partner = (LinearLayout) convertView.findViewById(R.id.act027_services_content_cell_ll_partner);
        TextView tv_partner_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_partner_lbl);
        TextView tv_partner_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_partner_val);

        TextView tv_status_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_status);

        TextView tv_qty_lbl = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_qty_lbl);
        TextView tv_qty_val = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_qty_val);

        //Seta valores
        exec_seq_oper.setText(item.get(SM_SO_ServiceDao.EXEC_SEQ_OPER));

        tv_pack_lbl.setText(hmAux_Trans.get("pack_id_lbl"));
        tv_pack_val.setText(item.get(SM_SO_PackDao.PACK_ID) +" - "+ item.get(SM_SO_PackDao.PACK_DESC));

        tv_zone_lbl.setText(hmAux_Trans.get("zone_lbl"));
        //tv_zone_val.setText(item.get(SM_SO_ServiceDao.ZONE_ID) +" - "+ item.get(SM_SO_ServiceDao.ZONE_DESC));

        tv_service_lbl.setText(hmAux_Trans.get("service_lbl"));
        tv_service_val.setText(item.get(SM_SO_ServiceDao.SERVICE_ID) +" - "+item.get(SM_SO_ServiceDao.SERVICE_DESC));

        if(item.get(SM_SO_ServiceDao.COMMENTS) != null && item.get(SM_SO_ServiceDao.COMMENTS).length() > 0){
            ll_comment.setVisibility(View.VISIBLE);
            tv_comment_lbl.setText(hmAux_Trans.get("comment_lbl"));
            tv_comment_val.setText(item.get(SM_SO_ServiceDao.COMMENTS));
        }else{
            ll_comment.setVisibility(View.GONE);
            tv_comment_lbl.setText(hmAux_Trans.get("comment_lbl"));
            tv_comment_val.setText("");
        }

        if(item.get(SM_SO_ServiceDao.PARTNER_CODE) != null && item.get(SM_SO_ServiceDao.PARTNER_CODE).length() > 0 ){
            ll_partner.setVisibility(View.VISIBLE);
            tv_partner_lbl.setText(hmAux_Trans.get("partner_lbl"));
            tv_partner_val.setText(item.get(SM_SO_ServiceDao.PARTNER_ID) +" - " +item.get(SM_SO_ServiceDao.PARTNER_DESC));
        }else{
            ll_partner.setVisibility(View.GONE);
            tv_partner_lbl.setText(hmAux_Trans.get("partner_lbl"));
            tv_partner_val.setText("");
        }

        tv_status_value.setText(hmAux_Trans.get(item.get(SM_SO_ServiceDao.STATUS)));
        //chama metodo que define a cor do status
        ToolBox_Inf.setServiceStatusColor(context, tv_status_value, item.get(SM_SO_ServiceDao.STATUS) );

        tv_qty_lbl.setText(hmAux_Trans.get("qty_lbl"));
        tv_qty_val.setText(item.get(Sql_Act027_001.QTY_DONE) +" / " +item.get(SM_SO_ServiceDao.QTY));

        //Icones
        //Flag
        if (item.get(Sql_Act027_001.SET_FLAG) != null && !item.get(Sql_Act027_001.SET_FLAG).equals("0")) {
            iv_flag.setVisibility(View.VISIBLE);
        } else {
            iv_flag.setVisibility(View.INVISIBLE);
        }
        //
        if(item.get(SM_SO_ServiceDao.EXEC_TYPE).equals(Constant.SO_SERVICE_TYPE_YES_NO)){
            iv_express.setImageDrawable(context.getDrawable(R.drawable.ic_check_circle));
        }else{
            iv_express.setImageDrawable(context.getDrawable(R.drawable.ic_play_circle_filled_black_24dp));
            //iv_express.setImageDrawable(context.getDrawable(R.drawable.ic_stop_circle_black_24px));
        }


        iv_express.setTag(item);
        iv_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HMAux item = (HMAux) v.getTag();
                //
                if (delegate != null) {
                    delegate.serviceSelected(item, Act027_Main.SELECTION_EXPRESS);
                }
            }
        });
        //
        iv_normal.setTag(item);
        iv_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HMAux item = (HMAux) v.getTag();
                //
                if (delegate != null) {
                    delegate.serviceSelected(item, Act027_Main.SELECTION_NORMAL);
                }
            }
        });
//        //
//        SM_SO_Service_ExecDao execDao =
//                new SM_SO_Service_ExecDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM
//                );
//        String setFlag = execDao.getByStringHM(
//                new Act027_Services_Adapter_Sql_001(
//                        item.get(SM_SO_ServiceDao.CUSTOMER_CODE),
//                        item.get(SM_SO_ServiceDao.SO_PREFIX),
//                        item.get(SM_SO_ServiceDao.SO_CODE),
//                        item.get(SM_SO_ServiceDao.PRICE_LIST_CODE),
//                        item.get(SM_SO_ServiceDao.PACK_CODE),
//                        item.get(SM_SO_ServiceDao.PACK_SEQ),
//                        item.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE),
//                        item.get(SM_SO_ServiceDao.SERVICE_CODE),
//                        item.get(SM_SO_ServiceDao.SERVICE_SEQ)
//
//                ).toSqlQuery()
//        ).get(Act027_Services_Adapter_Sql_001.SET_FLAG);
//        if (setFlag != null && !setFlag.equals("0")) {
//            iv_flag.setVisibility(View.VISIBLE);
//        } else {
//            iv_flag.setVisibility(View.GONE);
//        }

        return convertView;
    }

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

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }


}
