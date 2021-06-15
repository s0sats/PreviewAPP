package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.sql.Sql_Act015_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 */

public class Local_Data_List_Adapter extends BaseAdapter implements Filterable {

    private Context context;
    private int resource;
    private List<HMAux> source_filtered = new ArrayList<>();
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private OnIvScheduleWarningClickListner onIvScheduleWarningClickListner;
    private ValueFilter valueFilter;
    private List<HMAux> source = new ArrayList<>();
    private boolean isScheduled;

    public Local_Data_List_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source_filtered = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "local_data_list_adapter"
        );
        loadTranslation();
    }

    public Local_Data_List_Adapter(Context context, int resource, List<HMAux> source, String mket_filter) {
        this.context = context;
        this.resource = resource;
        this.source_filtered = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "local_data_list_adapter"
        );
        this.source = source;
        getFilter().filter(mket_filter);
        loadTranslation();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public interface OnIvScheduleWarningClickListner {
        void OnIvScheduleWarningClick(
                String fcmNewStatus,
                String fcmUserNick,
                String errorMsg
            );
    }

    public void setOnIvScheduleWarningClickListner(OnIvScheduleWarningClickListner onIvScheduleWarningClickListner) {
        this.onIvScheduleWarningClickListner = onIvScheduleWarningClickListner;
    }

    @Override
    public int getCount() {
        return source_filtered.size();
    }

    @Override
    public Object getItem(int position) {
        return source_filtered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    /**
     * LUCHE - 24/03/2020
     * <P></P>
     * Metodo que reemplementa  o clique no item da lista.
     * Na teoria não existem itens con os status abaixo, mas com o advento do novo agendamento, pode
     * ser que algo mude.
     * @param position Posição do item da list.
     * @return - True se houver chave status e diferente dos status abaixo.
     */
//    @Override
//    public boolean isEnabled(int position) {
//        //return super.isEnabled(position);
//        if(source.size() > 0) {
//            HMAux item = source.get(position);
//            //Se tem o status e é diferente de cancelled e rejected, permite clique
//            return
//                item.hasConsistentValue(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)
//                    && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_CANCELLED)
//                    && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_REJECTED)
//                    && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_IGNORED)
//                    && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED)
//                ;
//        }else{
//            return super.isEnabled(position);
//        }
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);

        }

        //Resgata HmAux com as informações
        final HMAux item = source_filtered.get(position);

        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = convertView.findViewById(R.id.local_data_list_cell_01_ll_bg);
        ConstraintLayout clHeader = convertView.findViewById(R.id.local_data_list_cell_01_cl_header);
        //
        TextView tv_schedule_lbl = convertView.findViewById(R.id.local_data_list_cell_01_tv_schedule_label);
        TextView tv_schedule_comments_lbl = convertView.findViewById(R.id.local_data_list_cell_01_tv_schedule_comment_ttl);

        setSchedulePk(item, tv_schedule_lbl);
        setScheduleComments(item, tv_schedule_comments_lbl);

        String dateStart = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT) ? item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT) : "", ""),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );

        String dateEnd = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT) ? item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT) : "", ""),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );

        TextView tv_date_lbl = convertView.findViewById(R.id.local_data_list_cell_01_tv_date_label);
        //
        TextView tv_id_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_label);
        tv_id_lbl.setText(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID) + " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        //
        TextView tv_serial_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_serial_label);
        tv_serial_lbl.setText(hmAux_Trans.get("lbl_serial_id") + " " + item.get(GE_Custom_Form_LocalDao.SERIAL_ID));

        if (item.get(GE_Custom_Form_LocalDao.SERIAL_ID).trim().length() == 0) {
            tv_serial_lbl.setVisibility(View.GONE);
        }else{
            tv_serial_lbl.setVisibility(View.VISIBLE);
        }
        //
        TextView tv_form_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_label);
        tv_form_lbl.setText(hmAux_Trans.get("lbl_form") + " " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
        //
        TextView tv_site_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_site_lbl);
        //Tratativa para caso site diferente do logado.
        //Caso caso seja 0, considera igual tb....
        if( item.get(MD_Schedule_ExecDao.SITE_CODE).equals("0")
            || item.get(MD_Schedule_ExecDao.SITE_CODE).equals(String.valueOf(ToolBox_Con.getPreference_Site_Code(context)))
        ){
            tv_site_lbl.setVisibility(View.GONE);
        }else{
            tv_site_lbl.setVisibility(View.VISIBLE);
            if(item.get(MD_SiteDao.SITE_ID) != null &&
                !item.get(MD_SiteDao.SITE_ID).equals("null") &&
                item.get(MD_SiteDao.SITE_ID).trim().length() > 0
            ) {
                tv_site_lbl.setText(hmAux_Trans.get("lbl_site") + " " + item.get(MD_SiteDao.SITE_ID) + " - " + item.get(MD_SiteDao.SITE_DESC));
            }else {
                tv_site_lbl.setText(hmAux_Trans.get("lbl_site") + " " + item.get(MD_SiteDao.SITE_CODE));
            }
        }

        TextView tv_status_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_status_val);
        tv_status_val.setVisibility(View.VISIBLE);

        LinearLayout ll_so = convertView.findViewById(R.id.local_data_list_cell_01_ll_so);
        TextView tv_so_code_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_so_code_ttl);
        TextView tv_so_code_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_so_code_val);

        String mSo_prefix = item.get("so_prefix");
        String mSo_code = item.get("so_code");

        if (mSo_prefix != null && !mSo_prefix.isEmpty()) {
            ll_so.setVisibility(View.VISIBLE);
            tv_so_code_lbl.setVisibility(View.VISIBLE);
            tv_so_code_lbl.setText(hmAux_Trans.get("lbl_so_code"));
            tv_so_code_val.setVisibility(View.VISIBLE);
            tv_so_code_val.setText(mSo_prefix + "." + mSo_code);
        } else {
            ll_so.setVisibility(View.GONE);
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_val.setText("");
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_val.setText("");
        }
        //Ticket
        TextView tv_ticket_code_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_ticket_code_val);
        setTicketPk(item, tv_ticket_code_val);

        //
        LinearLayout llIcons = convertView.findViewById(R.id.local_data_list_cell_01_ll_icons);
        ImageView ivScheduleWarningInfos = convertView.findViewById(R.id.local_data_list_cell_01_iv_schedule_warning_infos);
        ImageView ivNonConformity = convertView.findViewById(R.id.local_data_list_cell_01_iv_nc);
        //
        defineScheduleWarningInfos(ivScheduleWarningInfos,item);
        switch (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)) {

            case Constant.SYS_STATUS_IN_PROCESSING:
                if(!isScheduled) {
                    tv_date_lbl.setText(
                            hmAux_Trans.get("lbl_date") + " " +
                                    ToolBox_Inf.millisecondsToString(
                                            ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_START)),
                                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                    )
                    );
                    setTvDateLblConstraint(clHeader, ConstraintSet.PARENT_ID);
                }else{
                    tv_date_lbl.setText(ToolBox_Inf.formatScheduleIntervalDateFormatted(context, dateStart, dateEnd));
                    setTvDateLblConstraint(clHeader, R.id.local_data_list_cell_01_tv_status_val);
                }
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_PROCESS));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_PROCESS))
                );
                break;
            //
            case Constant.SYS_STATUS_WAITING_SYNC:

                tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                                ToolBox_Inf.millisecondsToString(
                                        ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_END)),
                                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                )
                );

                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_WAITING_SYNC));
                tv_status_val.setTextColor(
                        ToolBox_Inf.getStatusColorV2(context,Constant.SYS_STATUS_WAITING_SYNC)
                );
                break;
            case Constant.SYS_STATUS_DONE:
                tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_END)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_DONE));
                tv_status_val.setTextColor(
                    ToolBox_Inf.getStatusColorV2(context,Constant.SYS_STATUS_DONE)
                );

                break;
            case Constant.SYS_STATUS_SCHEDULE:
                tv_date_lbl.setText(ToolBox_Inf.formatScheduleIntervalDateFormatted(context, dateStart, dateEnd));

                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_SCHEDULE));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_SCHEDULE))
                );
                break;
            case Constant.SYS_STATUS_DELETED:
               tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                                ToolBox_Inf.millisecondsToString(
                                        ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_END)),
                                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                )
                );
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_DELETED));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_DELETED))
                );
                break;
            case ConstantBaseApp.SYS_STATUS_CANCELLED:
            case ConstantBaseApp.SYS_STATUS_IGNORED:
                //ATUALMENTE ESSES STATUS SÓ EXISTEM NO AGENDAMENTO , MAS VAI SABER....
                if(!isScheduled) {
                    tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                            ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_START)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                            )
                    );
                    setTvDateLblConstraint(clHeader, ConstraintSet.PARENT_ID);
                }else{
                    tv_date_lbl.setText(ToolBox_Inf.formatScheduleIntervalDateFormatted(context, dateStart, dateEnd));
                    setTvDateLblConstraint(clHeader, R.id.local_data_list_cell_01_tv_status_val);
                }
                //
                tv_status_val.setText(hmAux_Trans.get(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)));
                tv_status_val.setTextColor(
                    ToolBox_Inf.getStatusColorV2(context,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS))
                );
                break;
            default:
                tv_status_val.setText(hmAux_Trans.get(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)));
                tv_status_val.setTextColor(
                    ToolBox_Inf.getStatusColorV2(context,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS))
                );
                break;
        }
        //
        defineIvNonConformityVisibility(ivNonConformity,item);
        defineLlIconsVisibility(llIcons,ivScheduleWarningInfos,ivNonConformity);
        //
        return convertView;
    }

    private void setTicketPk(HMAux item, TextView tv_ticket_val) {
        try {
            if (!item.hasConsistentValue(GE_Custom_Form_DataDao.TICKET_PREFIX)
                || !item.hasConsistentValue(GE_Custom_Form_DataDao.TICKET_CODE)
                || (item.get(GE_Custom_Form_DataDao.TICKET_PREFIX) + item.get(GE_Custom_Form_DataDao.TICKET_CODE)).isEmpty()) {
                tv_ticket_val.setVisibility(View.GONE);
            } else {
                tv_ticket_val.setVisibility(View.VISIBLE);
                String ticket_pk = hmAux_Trans.get("lbl_ticket") + " " + item.get(GE_Custom_Form_DataDao.TICKET_PREFIX) +"."+ item.get(GE_Custom_Form_DataDao.TICKET_CODE);
                tv_ticket_val.setText(ticket_pk);
            }
        }catch (NullPointerException e){
            tv_ticket_val.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 15/04/2020
     * <p></p>
     * Metodo que define a visibilidade do icone de nao conformidade.
     * Somente visivel se a chave existe e seu valor for maior que 0
     * @param ivNonConformity ImageView do Icone
     * @param item HmAux do item
     */
    private void defineIvNonConformityVisibility(ImageView ivNonConformity, HMAux item) {
        ivNonConformity.setVisibility(View.GONE);
        //
        if( valueExists(item,Sql_Act015_001.HAS_NONCONFORMITY_FIELD)
            && ToolBox_Inf.convertStringToInt(item.get(Sql_Act015_001.HAS_NONCONFORMITY_FIELD)) > 0
        ){
            ivNonConformity.setVisibility(View.VISIBLE);
        }
    }

    /**
     * LUCHE - 15/04/2020
     * <p></p>
     * Metodo que valida se chave existe e é !- da string null
     * @param item HmAux do item
     * @param key Chave do HmAux
     * @return - Verdadeiro se Chave existir, não for null, nem vazia e nem "null"
     */
    private boolean valueExists(HMAux item, String key) {
        return  item.hasConsistentValue(key)
            && !item.get(key).isEmpty()
            && !item.get(key).equalsIgnoreCase("null");
    }

    /**
     * LUCHE - 14/04/2020
     * <p></p>
     * Metodo que defini visibilidade do linear layout que contem os icones.
     * Se um dos icone visiveis, exibe linear, caso contrario, esconde.
     * @param llIcons LinearLayout que contem os icones
     * @param ivScheduleWarningInfos Icone de infos do agendamento
     * @param ivNonConformity Icone de se existe não conformidade.
     */
    private void defineLlIconsVisibility(LinearLayout llIcons, ImageView ivScheduleWarningInfos, ImageView ivNonConformity) {
        if(ivScheduleWarningInfos.getVisibility() == View.VISIBLE || ivNonConformity.getVisibility() == View.VISIBLE ){
            llIcons.setVisibility(View.VISIBLE);
        }else{
            llIcons.setVisibility(View.GONE);
        }
    }

    private void defineScheduleWarningInfos(ImageView ivScheduleWarningInfos, final HMAux item) {
        ivScheduleWarningInfos.setVisibility(View.INVISIBLE);
        ivScheduleWarningInfos.setOnClickListener(null);
        //
        if(item.hasConsistentValue(MD_Schedule_ExecDao.FCM_NEW_STATUS)
           && item.hasConsistentValue(MD_Schedule_ExecDao.FCM_USER_NICK)
           && item.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_ERRO_MSG)
        ){
            if( !item.get(MD_Schedule_ExecDao.FCM_NEW_STATUS).isEmpty()
                || !item.get(MD_Schedule_ExecDao.FCM_USER_NICK).isEmpty()
                || !item.get(MD_Schedule_ExecDao.SCHEDULE_ERRO_MSG).isEmpty()
            ){
                int color = !item.get(MD_Schedule_ExecDao.SCHEDULE_ERRO_MSG).isEmpty()
                    ? R.color.namoa_color_danger_red
                    : R.color.light_to_dark_blue_color;
                ivScheduleWarningInfos.setVisibility(View.VISIBLE);
                ivScheduleWarningInfos.setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
                ivScheduleWarningInfos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onIvScheduleWarningClickListner != null){
                            onIvScheduleWarningClickListner.OnIvScheduleWarningClick(
                                item.get(MD_Schedule_ExecDao.FCM_NEW_STATUS),
                                item.get(MD_Schedule_ExecDao.FCM_USER_NICK),
                                item.get(MD_Schedule_ExecDao.SCHEDULE_ERRO_MSG)
                            );
                        }
                    }
                });
            }
        }
    }

    private void setTvDateLblConstraint(ConstraintLayout clHeader, int parentId) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(clHeader);
        constraintSet.connect(R.id.local_data_list_cell_01_tv_date_label, ConstraintSet.RIGHT, parentId, ConstraintSet.LEFT, 4);
        constraintSet.applyTo(clHeader);
    }

    /**
     * L.BARRIONUEVO - 05/03/2020
     * Metodo que identifica a necessidade de exibir ou não a informção de comentario do agendamento.
     *
     * LUCHE - 07/05/2020
     * Modificado metodo, alterado a chave buscado no HmAux, pois estava buscando pela chama errada.
     * @param item HmAux com dados do item
     * @param tv_schedule_comments_lbl TextView onde sera aplicado o texto e visibilidade.
     */
    private void setScheduleComments(HMAux item, TextView tv_schedule_comments_lbl) {
        String schedule_comments = item.get(GE_Custom_Form_LocalDao.SCHEDULE_COMMENTS);
        try {
            if (schedule_comments.isEmpty()) {
                tv_schedule_comments_lbl.setVisibility(View.GONE);
            } else {
                tv_schedule_comments_lbl.setVisibility(View.VISIBLE);
                tv_schedule_comments_lbl.setText(schedule_comments);
            }
        }catch (NullPointerException e){
            tv_schedule_comments_lbl.setVisibility(View.GONE);
        }
    }

    private void setSchedulePk(HMAux item, TextView tv_schedule_lbl) {
        try {
            if ((item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX) + item.get(MD_Schedule_ExecDao.SCHEDULE_CODE) + item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)).isEmpty()) {
                tv_schedule_lbl.setVisibility(View.INVISIBLE);
                isScheduled = false;
            } else {
                isScheduled = true;
                tv_schedule_lbl.setVisibility(View.VISIBLE);
                String schedule_pk = item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX) + "." + item.get(MD_Schedule_ExecDao.SCHEDULE_CODE) + "." + item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC);
                tv_schedule_lbl.setText(schedule_pk);

            }
        }catch (NullPointerException e){
            tv_schedule_lbl.setVisibility(View.GONE);
        }
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("lbl_date");
        translateList.add("lbl_hour");
        translateList.add("ttl_product");
        translateList.add("lbl_product_code");
        translateList.add("lbl_product_id");
        translateList.add("lbl_product_desc");
        translateList.add("ttl_form");
        translateList.add("lbl_type");
        translateList.add("lbl_form");
        translateList.add("lbl_data_serv");
        translateList.add("lbl_site");
        translateList.add("lbl_date_schedule_start");
        translateList.add("lbl_date_schedule_end");
        translateList.add("lbl_so_code");
        translateList.add("lbl_status");
        translateList.add("lbl_ticket");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    private class ValueFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String search = ToolBox.AccentMapper(constraint.toString().toLowerCase());

            if (search != null && search.length() > 0) {
                ArrayList<HMAux> filterList = new ArrayList<HMAux>();

                for(HMAux item: source){

                    String serial_id = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.SERIAL_ID)).toLowerCase();
                    String custom_product_desc = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC)).toLowerCase();
                    String custom_product_id = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID)).toLowerCase();
                    String custom_form_desc = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC)).toLowerCase();

                    if ((serial_id != null && serial_id.contains(search))
                            || (custom_product_desc != null && custom_product_desc.contains(search))
                            || (custom_product_id != null && custom_product_id.contains(search))
                            || (custom_form_desc != null && custom_form_desc.contains(search))
                    ) {
                        filterList.add(item);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = source.size();
                results.values = source;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            source_filtered = (ArrayList<HMAux>) results.values;
            //
            notifyDataSetChanged();
        }
    }
}
