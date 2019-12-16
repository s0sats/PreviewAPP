package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Act035_Adapter_Messages extends BaseAdapter {

    public static final int ITEM_VIEW_TYPE_IMAGE_OTHER = 0;
    public static final int ITEM_VIEW_TYPE_IMAGEM_MINE = 1;
    public static final int ITEM_VIEW_TYPE_TEXT_OTHER = 2;
    public static final int ITEM_VIEW_TYPE_TEXT_MINE = 3;
    public static final int ITEM_VIEW_TYPE_DATE = 4;
    public static final int ITEM_VIEW_TYPE_NO_MORE = 5;
    public static final int ITEM_VIEW_TYPE_TRANSLATE = 6;
    public static final int ITEM_VIEW_TYPE_FORM_AP_OTHER = 7;
    public static final int ITEM_VIEW_TYPE_SO = 8;
    public static final int ITEM_VIEW_TYPE_NO_READ = 9;
    public static final int ITEM_VIEW_TYPE_FORM_AP_MINE = 10;
    public static final int ITEM_VIEW_TYPE_TICKET_OTHER = 11;
    public static final int ITEM_VIEW_TYPE_TICKET_MINE = 12;

    public static HMAux hmAuxColors = new HMAux();

    private Context context;
    private ArrayList<HMAux> data;
    private String mUser_Code;
    private int mSizeAddUpdate = 0;
    public static boolean processingHMAux = false;

    private HMAux hmAux_Trans;
    private HMAux hmAux_Trans_Extra;
    private String mResource_Code;
    private String mResource_Name = "act037_adapter_ap";
    //Se usr tem acesso ao profile de form_ap ou não
    private boolean profile_ap = false;
    private boolean profile_ticket = false;
    //LUCHE - 27/11/2019
    private LinkedHashMap<Integer, Integer> resources = new LinkedHashMap<>();

    public Act035_Adapter_Messages(Context context, ArrayList<HMAux> data, HMAux hmAux_Trans, HMAux hmAux_Trans_Extra, boolean profile_ap, boolean profile_ticket) {
        this.context = context;

        loadResources();

        this.data = data;

        this.mUser_Code = ToolBox_Con.getPreference_User_Code(context);

        this.hmAux_Trans = hmAux_Trans;

        this.hmAux_Trans_Extra = hmAux_Trans_Extra;

        this.profile_ap = profile_ap;

        this.profile_ticket = profile_ticket;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            Constant.APP_MODULE,
            mResource_Name
        );

        loadTranslation();
    }

    private void loadResources() {
        resources.put(ITEM_VIEW_TYPE_IMAGE_OTHER, R.layout.act035_main_content_cell_whats_img_other);
        resources.put(ITEM_VIEW_TYPE_IMAGEM_MINE, R.layout.act035_main_content_cell_whats_img_me);
        resources.put(ITEM_VIEW_TYPE_TEXT_OTHER, R.layout.act035_main_content_cell_whats_text_other);
        resources.put(ITEM_VIEW_TYPE_TEXT_MINE, R.layout.act035_main_content_cell_whats_text_me);
        resources.put(ITEM_VIEW_TYPE_DATE, R.layout.act035_main_content_cell_whats_text_data);
        resources.put(ITEM_VIEW_TYPE_NO_MORE, R.layout.act035_main_content_cell_whats_text_end);
        resources.put(ITEM_VIEW_TYPE_TRANSLATE, R.layout.act035_main_content_cell_whats_text_trans);
        resources.put(ITEM_VIEW_TYPE_FORM_AP_OTHER, R.layout.act035_main_content_cell_namoa_ap_other);
        resources.put(ITEM_VIEW_TYPE_SO, R.layout.act035_main_content_cell_whats_text_other);
        resources.put(ITEM_VIEW_TYPE_NO_READ, R.layout.act035_main_content_cell_whats_text_no_read);
        resources.put(ITEM_VIEW_TYPE_FORM_AP_MINE, R.layout.act035_main_content_cell_namoa_ap_me);
        resources.put(ITEM_VIEW_TYPE_TICKET_OTHER, R.layout.act035_main_content_cell_namoa_ticket_other);
        resources.put(ITEM_VIEW_TYPE_TICKET_MINE, R.layout.act035_main_content_cell_namoa_ticket_me);
    }

    public void removeNoRead() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get("type") != null && data.get(i).get("type").equalsIgnoreCase("NO_READ")) {
                data.remove(i);
                //
                notifyDataSetChanged();
            }
        }
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("form_ttl");
        translateList.add("form_type_lbl");
        translateList.add("form_code_lbl");
        translateList.add("form_data_lbl");
        translateList.add("product_code_lbl");
        translateList.add("serial_lbl");
        translateList.add("ap_ttl");
        translateList.add("ap_code_lbl");
        translateList.add("ap_status_lbl");
        translateList.add("ap_what_lbl");
        translateList.add("ap_who_lbl");
        translateList.add("ap_when_lbl");
        //
        hmAux_Trans.putAll(ToolBox_Inf.setLanguage(
            context,
            Constant.APP_MODULE,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            translateList
        ));
        //
        hmAux_Trans.putAll(hmAux_Trans_Extra);
    }

    public interface IAct035_Adapter_Messages {
        void showInfo(HMAux hmAux);

        void download_AP(String pk, String custom_form_url);

        void join_AP(String pk);

        boolean checkTicketProfile(String site_code, String operation_code, String product_code);

        void downloadTicket(String pk, String site_code, String operation_code, String product_code);

        void showTicketForOtherCustomerMsg();
    }

    private IAct035_Adapter_Messages delegate;

    public void setOnshowInfoListener(IAct035_Adapter_Messages delegate) {
        this.delegate = delegate;
    }


    public int getmSizeAddUpdate() {
        int localValue = mSizeAddUpdate;
        mSizeAddUpdate = 0;
        //
        return localValue;
    }

    public void refreshData(HMAux hmAux) {
        for (int i = data.size() - 1; i >= 0; i--) {

            if (data.get(i).get(CH_MessageDao.TMP) != null) {
                if (hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase(data.get(i).get(CH_MessageDao.MSG_CODE))) {
                    data.set(i, hmAux);
                    //
                    notifyDataSetChanged();
                    //
                    break;
                }
            }
        }
    }

    public void refill(List<HMAux> dadosR) {
        for (int i = 0; i < dadosR.size(); i++) {
            HMAux msg = dadosR.get(i);
            //
            for (int j = data.size() - 1; j >= 0; j--) {
                HMAux item = data.get(j);
                if (item.get(CH_MessageDao.TMP) != null) {
                    if (msg.get(CH_MessageDao.TMP).equalsIgnoreCase(item.get(CH_MessageDao.TMP))) {

                        data.set(j, msg);

                        break;
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    public boolean addMessages(List<HMAux> dadosR) {
        boolean reOrder = false;
        int countFound = 0;
        //
        List<HMAux> dadosRNew = new ArrayList<>();
        //
        for (int i = 0; i < dadosR.size(); i++) {
            HMAux msg = dadosR.get(i);
            //
            boolean sFound = false;
            //
            for (int j = data.size() - 1; j >= 0; j--) {
                HMAux item = data.get(j);
                if (item.get(CH_MessageDao.TMP) != null) {
                    if (msg.get(CH_MessageDao.TMP).equalsIgnoreCase(item.get(CH_MessageDao.TMP))) {
                        sFound = true;
                        //
                        data.set(j, msg);
                        //
                        countFound++;
                        //
                        break;
                    }
                }
            }
            //
            if (!sFound) {
                dadosRNew.add(msg);
            }
        }
        //
        if (dadosRNew.size() > 0) {
            //
            HMAux fisrtAux = new HMAux();
            fisrtAux.put("msg_date_zone", dadosRNew.get(0).get("msg_date_zone"));
            fisrtAux.put("type", "DATE");
            //
            dadosRNew.add(0, fisrtAux);
            for (int i = 1; i < dadosRNew.size(); i++) {
                if (!ToolBox_Inf.equalDate(dadosRNew.get(i - 1).get("msg_date_zone"), dadosRNew.get(i).get("msg_date_zone"))) {
                    HMAux mAux = new HMAux();
                    mAux.put("msg_date_zone", dadosRNew.get(i).get("msg_date_zone"));
                    //
                    dadosRNew.add(i, mAux);
                }
            }
            //
            reOrder = checkReOrder(data, (ArrayList<HMAux>) dadosRNew);
            //
            if (data.size() > 0) {
                if (ToolBox_Inf.equalDate(data.get(data.size() - 1).get("msg_date_zone"), dadosRNew.get(0).get("msg_date_zone"))) {
                    dadosRNew.remove(0);
                }
            }
            //
            data.addAll(dadosRNew);
            //
            for (int i = data.size() - 1; i >= 0; i--) {
                if (data.get(i).get("type") != null && data.get(i).get("type").equalsIgnoreCase("NO_READ")) {
                    data.get(i).put("count", String.valueOf(data.size() - (i + 1)));
                }
            }
            //
            mSizeAddUpdate = dadosRNew.size();
        } else {
            mSizeAddUpdate = 0;
            //
            reOrder = false;
        }
        //
        notifyDataSetChanged();
        //
        return reOrder;
    }

    public boolean checkReOrder(ArrayList<HMAux> dados, ArrayList<HMAux> messages) {
        String sMessages = "";

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).get("msg_pk") != null) {
                sMessages = messages.get(i).get("msg_pk");
                //
                break;
            }
        }
        //
        for (int i = dados.size() - 1; i >= 0; i--) {
            HMAux aux = dados.get(i);
            //
            if (aux.get(CH_MessageDao.TMP) != null) {
                if (aux.get("msg_pk") != null && !aux.get("msg_pk").isEmpty() && !sMessages.isEmpty()) {
                    int compare = aux.get("msg_pk").compareToIgnoreCase(sMessages);

                    if (compare < 0) {
                        //aux é menor do que sMessage
                        return false;
                    } else if (compare > 0) {
                        //aux é maior do que sMessage
                        return true;
                    } else {
                        //aux é igual a sMessage
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
        //
        return false;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        //return 13;
        return resources.size();
    }

    @Override
    public int getItemViewType(int position) {
        try {
            HMAux item = data.get(position);

            if (item.get("tmp") == null) {
                if (item.get("type").equalsIgnoreCase("DATE")) {
                    return ITEM_VIEW_TYPE_DATE;
                } else if (item.get("type").equalsIgnoreCase("END")) {
                    return ITEM_VIEW_TYPE_NO_MORE;
                } else if (item.get("type").equalsIgnoreCase("NO_READ")) {
                    return ITEM_VIEW_TYPE_NO_READ;
                } else {
                }
            }

            JSONObject msg_obj = new JSONObject(item.get("msg_obj"));
            JSONObject message = msg_obj.getJSONObject("message");
            String msgType = message.getString("type").toUpperCase();
            //
            switch (msgType) {
                case ConstantBaseApp.CHAT_MESSAGE_TYPE_IMAGE:
                    if (!item.get(CH_MessageDao.USER_CODE).equalsIgnoreCase(mUser_Code)) {
                        return ITEM_VIEW_TYPE_IMAGE_OTHER;
                    } else {
                        return ITEM_VIEW_TYPE_IMAGEM_MINE;
                    }
                case ConstantBaseApp.CHAT_MESSAGE_TYPE_TRANSLATE:
                    return ITEM_VIEW_TYPE_TRANSLATE;
                case ConstantBaseApp.CHAT_MESSAGE_TYPE_FORM_AP:
                    if (!item.get(CH_MessageDao.USER_CODE).equalsIgnoreCase(mUser_Code)) {
                        return ITEM_VIEW_TYPE_FORM_AP_OTHER;
                    } else {
                        return ITEM_VIEW_TYPE_FORM_AP_MINE;
                    }
                case ConstantBaseApp.CHAT_MESSAGE_TYPE_SO:
                    return ITEM_VIEW_TYPE_SO;
                case ConstantBaseApp.CHAT_MESSAGE_TYPE_TICKET:
                    if (!item.get(CH_MessageDao.USER_CODE).equalsIgnoreCase(mUser_Code)) {
                        return ITEM_VIEW_TYPE_TICKET_OTHER;
                    } else {
                        return ITEM_VIEW_TYPE_TICKET_MINE;
                    }
                case ConstantBaseApp.CHAT_MESSAGE_TYPE_TEXT:
                default:
                    if (!item.get(CH_MessageDao.USER_CODE).equalsIgnoreCase(mUser_Code)) {
                        return ITEM_VIEW_TYPE_TEXT_OTHER;
                    } else {
                        return ITEM_VIEW_TYPE_TEXT_MINE;
                    }
            }

        } catch (Exception e) {
            return ITEM_VIEW_TYPE_IMAGE_OTHER;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        boolean results = false;

        switch (getItemViewType(position)) {
            case ITEM_VIEW_TYPE_DATE:
            case ITEM_VIEW_TYPE_NO_MORE:
            case ITEM_VIEW_TYPE_TRANSLATE:
            case ITEM_VIEW_TYPE_SO:
            case ITEM_VIEW_TYPE_NO_READ:
                results = false;
                break;
            // 0 - Other IMG / 1 - Me IMG / 2 - Other TXT / 3 - Me TXT / 7 - FORM_AP
            default:
                results = true;
                break;
        }

        return results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                case ITEM_VIEW_TYPE_IMAGE_OTHER:
                    //convertView = mInflater.inflate(resource_01, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_IMAGE_OTHER), parent, false);
                    break;

                case ITEM_VIEW_TYPE_IMAGEM_MINE:
                    //convertView = mInflater.inflate(resource_02, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_IMAGEM_MINE), parent, false);
                    break;

                case ITEM_VIEW_TYPE_TEXT_OTHER:
                    //convertView = mInflater.inflate(resource_03, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_TEXT_OTHER), parent, false);
                    break;

                case ITEM_VIEW_TYPE_TEXT_MINE:
                    //convertView = mInflater.inflate(resource_04, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_TEXT_MINE), parent, false);
                    break;

                case ITEM_VIEW_TYPE_DATE:
                    //convertView = mInflater.inflate(resource_05, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_DATE), parent, false);
                    break;

                case ITEM_VIEW_TYPE_NO_MORE:
                    //convertView = mInflater.inflate(resource_06, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_NO_MORE), parent, false);
                    break;

                case ITEM_VIEW_TYPE_TRANSLATE:
                    //convertView = mInflater.inflate(resource_07, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_TRANSLATE), parent, false);
                    break;

                case ITEM_VIEW_TYPE_FORM_AP_OTHER:
                    //convertView = mInflater.inflate(resource_08, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_FORM_AP_OTHER), parent, false);
                    break;

                case ITEM_VIEW_TYPE_SO:
                    //convertView = mInflater.inflate(resource_09, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_SO), parent, false);
                    break;

                case ITEM_VIEW_TYPE_NO_READ:
                    //convertView = mInflater.inflate(resource_10, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_NO_READ), parent, false);
                    break;

                case ITEM_VIEW_TYPE_FORM_AP_MINE:
                    //convertView = mInflater.inflate(resource_11, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_FORM_AP_MINE), parent, false);
                    break;

                case ITEM_VIEW_TYPE_TICKET_OTHER:
                    //convertView = mInflater.inflate(resource_11, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_TICKET_OTHER), parent, false);
                    break;

                case ITEM_VIEW_TYPE_TICKET_MINE:
                    //convertView = mInflater.inflate(resource_11, parent, false);
                    convertView = mInflater.inflate(resources.get(ITEM_VIEW_TYPE_TICKET_MINE), parent, false);
                    break;
            }
        }

        JSONObject message = null;
        HMAux hmAux = data.get(position);

        if (hmAux.get("tmp") != null) {

            try {
                JSONObject msg_obj = new JSONObject(hmAux.get("msg_obj"));
                message = msg_obj.getJSONObject("message");
            } catch (JSONException e) {
                message = new JSONObject();
            }

        }

        switch (getItemViewType(position)) {
            case ITEM_VIEW_TYPE_IMAGE_OTHER:
                processImageOther(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_IMAGEM_MINE:
                processImageMe(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_TEXT_OTHER:
                processTxTOther(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_TEXT_MINE:
                processTxTMe(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_DATE:
                processDate(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_NO_MORE:
                processNoMore(convertView);
                break;

            case ITEM_VIEW_TYPE_TRANSLATE:
                processTranslate(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_FORM_AP_OTHER:
                processForm_APOther(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_SO:
                processSo(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_NO_READ:
                processNoRead(hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_FORM_AP_MINE:
                processForm_APMe(message, hmAux, convertView);
                break;

            case ITEM_VIEW_TYPE_TICKET_OTHER:
                processTicket(message, hmAux, convertView, ITEM_VIEW_TYPE_TICKET_OTHER);
                break;

            case ITEM_VIEW_TYPE_TICKET_MINE:
                processTicket(message, hmAux, convertView, ITEM_VIEW_TYPE_TICKET_MINE);
                break;
        }
        //
        return convertView;
    }

    private void processImageOther(JSONObject message, HMAux hmAux, View convertView) {
        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
        ImageView iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);

        tv_name.setText(hmAux.get("user_nick"));

        if (hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL) == null || hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).isEmpty()) {
            iv_foto.setImageResource(R.drawable.sand_watch_transp);
        } else {
            iv_foto.setImageBitmap(BitmapFactory.decodeFile(Constant.THU_PATH + "/" +
                hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).replace(".jpg", "") + "_thumb.jpg"
            ));
        }

        tv_hour.setText(
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"
            )
        );

        if (hmAuxColors.get(hmAux.get("user_code")) == null) {
            hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
        }

        tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
    }

    private void processImageMe(JSONObject message, HMAux hmAux, View convertView) {
        ImageView iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
        ImageView iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);

        if (hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL) == null || hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).isEmpty()) {
            iv_foto.setImageResource(R.drawable.sand_watch_transp);
        } else {
            iv_foto.setImageBitmap(BitmapFactory.decodeFile(Constant.THU_PATH + "/" +
                hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).replace(".jpg", "") + "_thumb.jpg"
            ));
        }

        tv_hour.setText(

            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"

            )
        );

        iv_badge.setImageDrawable(processBadgeImage(hmAux));
    }

    private void processTxTOther(JSONObject message, HMAux hmAux, View convertView) {
        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
        //
        try {

            if (hmAuxColors.get(hmAux.get("user_code")) == null) {
                hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
            }

            tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));


            tv_message.setText(message.getString("data").trim());

        } catch (JSONException e) {
            tv_message.setText("Error data");
        }

        tv_name.setText(hmAux.get("user_nick"));

        tv_hour.setText(

            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"

            )
        );
    }

    private void processTxTMe(JSONObject message, HMAux hmAux, View convertView) {
        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
        ImageView iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);

        try {
            tv_message.setText(message.getString("data").trim());
        } catch (JSONException e) {
            tv_message.setText("Error data");
        }

        tv_hour.setText(

            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"

            )
        );

        iv_badge.setImageDrawable(processBadge(hmAux));
    }

    private void processDate(JSONObject message, HMAux hmAux, View convertView) {
        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);

        String resultado = "";

        if (ToolBox_Inf.isToday_Yesterday(
            ToolBox_Inf.dateToMillisecondsChat(hmAux.get("msg_date_zone"), ""),
            true
        )) {

            resultado = hmAux_Trans.get("TODAY");

        }
        //
        if (ToolBox_Inf.isToday_Yesterday(
            ToolBox_Inf.dateToMillisecondsChat(hmAux.get("msg_date_zone"), ""),
            false
        )) {

            resultado = hmAux_Trans.get("YESTERDAY");

        }
        //
        if (resultado.equalsIgnoreCase("")) {
            tv_message.setText(
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMillisecondsChat(hmAux.get("msg_date_zone"), ""),
//                            ""
                    ToolBox_Inf.nlsDateFormat(context)
                )
            );

        } else {
            tv_message.setText(resultado);
        }
    }

    private void processNoMore(View convertView) {
        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
        tv_message.setText(hmAux_Trans.get("NO_MORE_MESSAGES"));

    }

    private void processTranslate(JSONObject message, HMAux hmAux, View convertView) {
        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
        String resultTranslate = "";

        try {

            String msgParts[] = message.getString("data").replace("|", "#").split("#");

            msgParts = message.getString("data").replace("|", "#").split("#");
            resultTranslate = hmAux_Trans.get(msgParts[0]) + (msgParts.length > 1 ? msgParts[1] : "");
            //
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_message.setText(resultTranslate);
    }

    private void processForm_APOther(JSONObject message, HMAux hmAux, View convertView) {
        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_ttl);
        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_hour_ttl);
        TextView tv_type = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_label);
        TextView tv_form_label = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_label);
        TextView tv_data_serv = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_lbl);
        TextView tv_product = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_lbl);
        TextView tv_serial = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_lbl);
        TextView tv_ap_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_ttl);
        TextView tv_ap_code = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_lbl);
        TextView tv_ap_status = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_lbl);
        TextView tv_ap_what = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_lbl);
        TextView tv_ap_who = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_lbl);
        TextView tv_ap_when = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_lbl);
        //
        TextView tv_type_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_val);
        TextView tv_form_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_val);
        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_val);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_val);
        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_val);
        TextView tv_ap_code_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_val);
        TextView tv_ap_status_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_val);
        TextView tv_ap_what_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_val);
        TextView tv_ap_when_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_val);
        TextView tv_ap_who_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_val);
        LinearLayout ll_action_btn = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_ll_action_btn);
        //
        Button iv_download_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_download_ap);
        Button iv_join_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_join_ap);
        //
        ll_action_btn.setVisibility(View.VISIBLE);
        //
        HashMap<String, String> item = new HashMap<>();

        try {
            item = ToolBox_Inf.JsonToHashMap(
                message.getJSONObject("data"),
                "form_ap"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        if (hmAuxColors.get(hmAux.get("user_code")) == null) {
            hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
        }
        tv_name.setText(hmAux.get("user_nick"));
        tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
        //
        tv_form_ttl.setText(hmAux_Trans.get("form_ttl"));
        //
        tv_hour.setText(
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"
            )
        );
        //
        tv_type.setText(hmAux_Trans.get("form_type_lbl"));
        tv_type_val.setText(
            item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE) + " - " +
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC)
        );
        //
        tv_form_label.setText(hmAux_Trans.get("form_code_lbl"));
        tv_form_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE) + " - " +
            item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION) + " - " +
            item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC)
        );
        //
        tv_data_serv.setText(hmAux_Trans.get("form_data_lbl"));
        tv_data_serv_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        //
        tv_product.setText(hmAux_Trans.get("product_code_lbl"));
        tv_product_val.setText(
            item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_ID) + " - " +
                item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_DESC)
        );
        //
        tv_serial.setText(hmAux_Trans.get("serial_lbl"));
        tv_serial_val.setText(item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) != "null" ? item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) : "");
        //
        tv_ap_ttl.setText(hmAux_Trans.get("ap_ttl"));
        //
        tv_ap_code.setText(hmAux_Trans.get("ap_code_lbl"));
        tv_ap_code_val.setText(
            item.get(GE_Custom_Form_ApDao.AP_CODE) + " - " +
                item.get(GE_Custom_Form_ApDao.AP_DESCRIPTION)
        );
        tv_ap_status.setText(hmAux_Trans.get("ap_status_lbl"));
        tv_ap_status_val.setText(
            hmAux_Trans.get(item.get(
                GE_Custom_Form_ApDao.AP_STATUS
                )
            )
        );
        ToolBox_Inf.setAPStatusColor(
            context,
            tv_ap_status_val,
            item.get(GE_Custom_Form_ApDao.AP_STATUS)
        );
        tv_ap_what.setText(hmAux_Trans.get("ap_what_lbl"));

        if (item.get(GE_Custom_Form_ApDao.AP_WHAT) != "null" && !item.get(GE_Custom_Form_ApDao.AP_WHAT).isEmpty()) {
            tv_ap_what_val.setText(
                ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(item.get(GE_Custom_Form_ApDao.AP_WHAT)), 45)
            );
        } else {
            tv_ap_what_val.setText("");
        }
        tv_ap_who.setText(hmAux_Trans.get("ap_who_lbl"));
        tv_ap_who_val.setText(
            item.get("ap_who_name") != "null" ? item.get("ap_who_name") : ""
        );
        tv_ap_when.setText(hmAux_Trans.get("ap_when_lbl"));
        tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) != null ? item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) : "");

        if (item.get(GE_Custom_Form_ApDao.AP_WHEN) != null &&
            !item.get(GE_Custom_Form_ApDao.AP_WHEN).equalsIgnoreCase("null") &&
            !item.get(GE_Custom_Form_ApDao.AP_WHEN).isEmpty()) {
            tv_ap_when_val.setText(
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_ApDao.AP_WHEN), ""),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
            );
        } else {
            tv_ap_when_val.setText("");
            //tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHEN));
        }

        iv_download_ap.setTag(item);
        iv_download_ap.setText(hmAux_Trans.get("lbl_checklist"));
        iv_download_ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();

                if (delegate != null) {
                    delegate.download_AP(itemAux.get("pk"), itemAux.get("custom_form_url"));
                }
            }
        });

        iv_join_ap.setTag(item);
        iv_join_ap.setText(hmAux_Trans.get("lbl_form_ap"));
        iv_join_ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();

                if (delegate != null) {
                    delegate.join_AP(itemAux.get("pk"));
                }
            }
        });
        //Seta visibilidade do botão de pendend do perfil do user
        iv_join_ap.setVisibility(profile_ap ? View.VISIBLE : View.INVISIBLE);
        //
        if (iv_join_ap.getVisibility() == View.VISIBLE) {
            if (ToolBox_Inf.pkCustomerCode(item.get("pk")) != ToolBox_Con.getPreference_Customer_Code(context)) {
                iv_join_ap.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void processForm_APMe(JSONObject message, HMAux hmAux, View convertView) {
        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_ttl);
        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_hour_ttl);
        TextView tv_type = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_label);
        TextView tv_form_label = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_label);
        TextView tv_data_serv = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_lbl);
        TextView tv_product = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_lbl);
        TextView tv_serial = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_lbl);
        TextView tv_ap_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_ttl);
        TextView tv_ap_code = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_lbl);
        TextView tv_ap_status = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_lbl);
        TextView tv_ap_what = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_lbl);
        TextView tv_ap_who = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_lbl);
        TextView tv_ap_when = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_lbl);
        //
        TextView tv_type_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_val);
        TextView tv_form_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_val);
        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_val);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_val);
        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_val);
        TextView tv_ap_code_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_val);
        TextView tv_ap_status_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_val);
        TextView tv_ap_what_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_val);
        TextView tv_ap_when_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_val);
        TextView tv_ap_who_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_val);
        LinearLayout ll_action_btn = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_ll_action_btn);
        //
        Button iv_download_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_download_ap);
        Button iv_join_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_join_ap);
        //
        ll_action_btn.setVisibility(View.VISIBLE);
        //
        HashMap<String, String> item = new HashMap<>();

        try {
            item = ToolBox_Inf.JsonToHashMap(
                message.getJSONObject("data"),
                "form_ap"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        if (hmAuxColors.get(hmAux.get("user_code")) == null) {
            hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
        }

        tv_name.setText(hmAux.get("user_nick"));
        tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
        tv_name.setVisibility(View.GONE);
        //
        tv_form_ttl.setText(hmAux_Trans.get("form_ttl"));
        //
        tv_hour.setText(
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"
            )
        );
        //
        tv_type.setText(hmAux_Trans.get("form_type_lbl"));
        tv_type_val.setText(
            item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE) + " - " +
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC)
        );
        //
        tv_form_label.setText(hmAux_Trans.get("form_code_lbl"));
        tv_form_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE) + " - " +
            item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION) + " - " +
            item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC)
        );
        //
        tv_data_serv.setText(hmAux_Trans.get("form_data_lbl"));
        tv_data_serv_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        //
        tv_product.setText(hmAux_Trans.get("product_code_lbl"));
        tv_product_val.setText(
            item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_ID) + " - " +
                item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_DESC)
        );
        //
        tv_serial.setText(hmAux_Trans.get("serial_lbl"));
        tv_serial_val.setText(item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) != "null" ? item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) : "");
        //
        tv_ap_ttl.setText(hmAux_Trans.get("ap_ttl"));
        //
        tv_ap_code.setText(hmAux_Trans.get("ap_code_lbl"));
        tv_ap_code_val.setText(
            item.get(GE_Custom_Form_ApDao.AP_CODE) + " - " +
                item.get(GE_Custom_Form_ApDao.AP_DESCRIPTION)
        );
        tv_ap_status.setText(hmAux_Trans.get("ap_status_lbl"));
        tv_ap_status_val.setText(
            hmAux_Trans.get(item.get(
                GE_Custom_Form_ApDao.AP_STATUS
                )
            )
        );
        ToolBox_Inf.setAPStatusColor(
            context,
            tv_ap_status_val,
            item.get(GE_Custom_Form_ApDao.AP_STATUS)
        );
        tv_ap_what.setText(hmAux_Trans.get("ap_what_lbl"));

        if (item.get(GE_Custom_Form_ApDao.AP_WHAT) != "null" && !item.get(GE_Custom_Form_ApDao.AP_WHAT).isEmpty()) {
            tv_ap_what_val.setText(
                ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(item.get(GE_Custom_Form_ApDao.AP_WHAT)), 45)
            );
        } else {
            tv_ap_what_val.setText("");
        }
        tv_ap_who.setText(hmAux_Trans.get("ap_who_lbl"));
        tv_ap_who_val.setText(
            item.get("ap_who_name") != "null" ? item.get("ap_who_name") : ""
        );
        tv_ap_when.setText(hmAux_Trans.get("ap_when_lbl"));
        tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) != null ? item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) : "");

        if (item.get(GE_Custom_Form_ApDao.AP_WHEN) != null &&
            !item.get(GE_Custom_Form_ApDao.AP_WHEN).equalsIgnoreCase("null") &&
            !item.get(GE_Custom_Form_ApDao.AP_WHEN).isEmpty()) {
            tv_ap_when_val.setText(
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_ApDao.AP_WHEN), ""),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
            );
        } else {
            tv_ap_when_val.setText("");
            //tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHEN));
        }

        iv_download_ap.setTag(item);
        iv_download_ap.setText(hmAux_Trans.get("lbl_checklist"));
        iv_download_ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();

                if (delegate != null) {
                    delegate.download_AP(itemAux.get("pk"), itemAux.get("custom_form_url"));
                }
            }
        });

        iv_join_ap.setTag(item);
        iv_join_ap.setText(hmAux_Trans.get("lbl_form_ap"));
        iv_join_ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();

                if (delegate != null) {
                    delegate.join_AP(itemAux.get("pk"));
                }
            }
        });
        //Seta visibilidade do botão de pendend do perfil do user
        iv_join_ap.setVisibility(profile_ap ? View.VISIBLE : View.INVISIBLE);
        //
        if (iv_join_ap.getVisibility() == View.VISIBLE) {
            if (ToolBox_Inf.pkCustomerCode(item.get("pk")) != ToolBox_Con.getPreference_Customer_Code(context)) {
                iv_join_ap.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void processTicket(JSONObject message, HMAux hmAux, View convertView, int itemViewTypeTicket) {
        TextView tv_name = convertView.findViewById(R.id.act035_main_content_cell_ticket_tv_name);
        TextView tv_ttl = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_ttl);
        TextView tv_id_val = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_id_val);
        TextView tv_path_val = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_path);
        TextView tv_type_val = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_type);
        TextView tv_product_val = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_product);
        TextView tv_serial_val = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_serial);
        TextView tv_comment_val = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_comment);
        TextView tv_hour = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_tv_hour_ttl);
        ImageButton ibDownload = convertView.findViewById(R.id.act035_main_content_cell_namoa_ticket_iv_download);
        TextView tv_warning_msg = convertView.findViewById(R.id.act035_main_content_cell_ticket_tv_warning_msg);
        //
        HMAux item = new HMAux();
        //
        try {
            item = HMAux.getHmAuxFromHashMap(
                ToolBox_Inf.JsonToHashMap(
                    message,
                    "data"
                )
            );
            //Reseta visibilida de text de msg de aviso
            tv_warning_msg.setVisibility(View.GONE);
            //
            if (hmAuxColors.get(hmAux.get("user_code")) == null) {
                hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
            }
            //
            tv_name.setText(hmAux.get("user_nick"));
            tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
            tv_name.setVisibility(itemViewTypeTicket == ITEM_VIEW_TYPE_TICKET_OTHER ? View.VISIBLE : View.GONE);
            //
            tv_hour.setText(ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"
            ));
            //
            tv_ttl.setText(hmAux_Trans.get("ticket_ttl"));
            tv_ttl.setVisibility(View.VISIBLE);
            //
            tv_id_val.setText(item.get("ticket_id"));
            tv_id_val.setVisibility(View.VISIBLE);
            //
            if(item.hasConsistentValue("type_path")) {
                tv_path_val.setText(item.get("type_path"));
                tv_path_val.setVisibility(View.VISIBLE);
            }else{
                tv_path_val.setText("");
                tv_path_val.setVisibility(View.GONE);
            }
            //
            tv_type_val.setText(item.get("type_desc"));
            tv_type_val.setVisibility(View.VISIBLE);
            //
            if(item.hasConsistentValue("product_desc") && !item.get("product_desc").isEmpty()) {
                tv_product_val.setText(item.get("product_desc"));
                tv_product_val.setVisibility(View.VISIBLE);
            }else{
                tv_product_val.setText("");
                tv_product_val.setVisibility(View.GONE);
            }

            tv_serial_val.setText(item.get("serial_id"));
            tv_serial_val.setVisibility(View.VISIBLE);
            //
            if (item.hasConsistentValue("open_comments")) {
                tv_comment_val.setText(item.get("open_comments"));
                tv_comment_val.setVisibility(View.VISIBLE);
            } else {
                tv_comment_val.setText("");
                tv_comment_val.setVisibility(View.GONE);
            }
            ibDownload.setVisibility(View.VISIBLE);
            if(!profile_ticket){
                ibDownload.setVisibility(View.GONE);
            }else {
                //Valida de se customer da msg é o mesmo que o logado.
                if (item.hasConsistentValue("pk") && !item.get("pk").isEmpty()) {
                    String customerPk = item.get("pk").substring(0, item.get("pk").indexOf("|"));
                    if (String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)).equalsIgnoreCase(customerPk)) {
                        final HMAux finalItem = item;
                        ibDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (delegate != null
                                    && finalItem.hasConsistentValue("pk")
                                    && finalItem.hasConsistentValue("site_code")
                                    && finalItem.hasConsistentValue("operation_code")
                                    && finalItem.hasConsistentValue("product_code")
                                ) {
                                    delegate.downloadTicket(
                                        finalItem.get("pk"),
                                        finalItem.get("site_code"),
                                        finalItem.get("operation_code"),
                                        finalItem.get("product_code")
                                    );
                                }
                            }
                        });
                        //Valida de usuario tem acesso ao MD do ticket
                        if (delegate != null
                            && finalItem.hasConsistentValue("site_code")
                            && finalItem.hasConsistentValue("operation_code")
                            && finalItem.hasConsistentValue("product_code")
                        ) {
                            if (!delegate.checkTicketProfile(
                                finalItem.get("site_code"),
                                finalItem.get("operation_code"),
                                finalItem.get("product_code"))
                            ) {
                                tv_warning_msg.setVisibility(View.VISIBLE);
                                tv_warning_msg.setText(hmAux_Trans.get("no_profile_msg"));
                                ibDownload.setVisibility(View.GONE);
                            }
                        }
                        //
                    } else {
                        ibDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (delegate != null) {
                                    delegate.showTicketForOtherCustomerMsg();
                                }
                            }
                        });
                        tv_warning_msg.setVisibility(View.VISIBLE);
                        tv_warning_msg.setText(hmAux_Trans.get("ticket_for_another_customer_msg"));
                    }
                } else {
                    tv_warning_msg.setVisibility(View.VISIBLE);
                    tv_warning_msg.setText(hmAux_Trans.get("ticket_key_not_found_msg"));
                }
            }

        } catch (Exception e) {
            //
            tv_name.setText(hmAux.get("user_nick"));
            tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
            tv_name.setVisibility(itemViewTypeTicket == ITEM_VIEW_TYPE_TICKET_OTHER ? View.VISIBLE : View.GONE);
            //
            tv_hour.setText(ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                " HH:mm"
            ));
            tv_warning_msg.setVisibility(View.VISIBLE);
            tv_warning_msg.setText(hmAux_Trans.get("corrupted_message_msg"));
            //
            tv_ttl.setVisibility(View.GONE);
            tv_id_val.setVisibility(View.GONE);
            tv_path_val.setVisibility(View.GONE);
            tv_type_val.setVisibility(View.GONE);
            tv_product_val.setVisibility(View.GONE);
            tv_serial_val.setVisibility(View.GONE);
            tv_comment_val.setVisibility(View.GONE);
            ibDownload.setVisibility(View.GONE);
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        }

    }

    private void processSo(JSONObject message, HMAux hmAux, View convertView) {

    }

    private void processNoRead(HMAux hmAux, View convertView) {
        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
        tv_message.setText("(" + hmAux.get("count") + ") " + hmAux_Trans.get("UNREAD_MESSAGES"));

    }

    private Drawable processBadge(HMAux hmAux) {
        // Badge Status for All
        if (hmAux.get(CH_MessageDao.ALL_READ).equalsIgnoreCase("1")) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_green_24dp);
        } else if (hmAux.get(CH_MessageDao.ALL_DELIVERED).equalsIgnoreCase("1")) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
        } else if (!hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase("0")) {
            return context.getResources().getDrawable(R.drawable.ic_done_black_24dp);
        } else {
            return context.getResources().getDrawable(R.drawable.ic_clock_chat);
        }
    }

    private Drawable processBadgeImage(HMAux hmAux) {
        // Badge Status for All
        if (hmAux.get(CH_MessageDao.ALL_READ).equalsIgnoreCase("1")) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_green_24dp);
        } else if (hmAux.get(CH_MessageDao.ALL_DELIVERED).equalsIgnoreCase("1")) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
        } else if (!hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase("0")
            && hmAux.get(CH_MessageDao.FILE_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SENT)) {
            return context.getResources().getDrawable(R.drawable.ic_done_black_24dp);
        } else {
            return context.getResources().getDrawable(R.drawable.ic_clock_chat);
        }
    }
}