package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Act035_Adapter_Messages_bk extends BaseAdapter {

    public static HMAux hmAuxColors = new HMAux();

    private Context context;
    //
    private int resource_01;
    private int resource_02;
    private int resource_03;
    private int resource_04;
    private int resource_05;
    private int resource_06;
    private int resource_07;
    //
    private ArrayList<HMAux> data;

    private String mUser_Code;

    private int mSizeAddUpdate = 0;

    public static boolean processingHMAux = false;

    private HMAux hmAux_Trans;


    public Act035_Adapter_Messages_bk(Context context, int resource_01, int resource_02, int resource_03, int resource_04, int resource_05, int resource_06, int resource_07, ArrayList<HMAux> data, HMAux hmAux_Trans) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.resource_03 = resource_03;
        this.resource_04 = resource_04;
        this.resource_05 = resource_05;
        this.resource_06 = resource_06;
        this.resource_07 = resource_07;

        this.data = data;

        this.mUser_Code = ToolBox_Con.getPreference_User_Code(context);

        this.hmAux_Trans = hmAux_Trans;
    }

    public interface IAct035_Adapter_Messages {
        void showInfo(HMAux hmAux);
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
            fisrtAux.put("msg_date", dadosRNew.get(0).get("msg_date"));
            fisrtAux.put("type", "DATE");
            //
            dadosRNew.add(0, fisrtAux);
            for (int i = 1; i < dadosRNew.size(); i++) {
                if (!ToolBox_Inf.equalDate(dadosRNew.get(i - 1).get("msg_date"), dadosRNew.get(i).get("msg_date"))) {
                    HMAux mAux = new HMAux();
                    mAux.put("msg_date", dadosRNew.get(i).get("msg_date"));
                    //
                    dadosRNew.add(i, mAux);
                }
            }
            //
            reOrder = checkReOrder(data, (ArrayList<HMAux>) dadosRNew);
            //
            if (ToolBox_Inf.equalDate(data.get(data.size() - 1).get("msg_date"), dadosRNew.get(0).get("msg_date"))) {
                dadosRNew.remove(0);
            }
            //
            data.addAll(dadosRNew);
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
        return 7;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            HMAux item = data.get(position);

            if (item.get("tmp") == null) {
                if (item.get("type").equalsIgnoreCase("DATE")) {
                    return 4;
                } else if (item.get("type").equalsIgnoreCase("END")) {
                    return 5;
                } else {
                }
            }

            JSONObject msg_obj = new JSONObject(item.get("msg_obj"));
            JSONObject message = msg_obj.getJSONObject("message");

            if (message.getString("type").equalsIgnoreCase("IMAGE")) {

                if (!item.get("user_code").equalsIgnoreCase(mUser_Code)) {
                    return 0;
                } else {
                    return 1;
                }

            } else if (message.getString("type").equalsIgnoreCase("TRANSLATE")) {
                return 6;
            } else {
                if (!item.get("user_code").equalsIgnoreCase(mUser_Code)) {
                    return 2;
                } else {
                    return 3;
                }

            }

        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        boolean results = false;

        switch (getItemViewType(position)) {

            // Date
            case 4:
                results = false;
                break;

            // No More
            case 5:
                results = false;
                break;

            // Translate
            case 6:
                results = false;
                break;

            // 0 - Other IMG / 1 - Me IMG / 2 - Other TXT / 3 - Me TXT
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

                // Other IMG
                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;

                // Me IMG
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;

                // Other TXT
                case 2:
                    convertView = mInflater.inflate(resource_03, parent, false);
                    break;

                // Me TXT
                case 3:
                    convertView = mInflater.inflate(resource_04, parent, false);
                    break;

                // Date
                case 4:
                    convertView = mInflater.inflate(resource_05, parent, false);
                    break;

                // No More
                case 5:
                    convertView = mInflater.inflate(resource_06, parent, false);
                    break;

                // Translate
                case 6:
                    convertView = mInflater.inflate(resource_07, parent, false);
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
            // Other IMG
            case 0:
                processImageOther(message, hmAux, convertView);
                break;

            // Me IMG
            case 1:
                processImageMe(message, hmAux, convertView);
                break;

            // Other TXT
            case 2:
                processTxTOther(message, hmAux, convertView);
                break;

            // Me TXT
            case 3:
                processTxTMe(message, hmAux, convertView);
                break;

            // Date
            case 4:
                processDate(message, hmAux, convertView);
                break;

            // No More
            case 5:
                processNoMore(convertView);
                break;

            // Translate
            case 6:
                processTranslate(message, hmAux, convertView);
                break;
        }
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

        iv_badge.setImageDrawable(processBadge(hmAux));
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
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                true
        )) {

            resultado = hmAux_Trans.get("TODAY");

        }
        //
        if (ToolBox_Inf.isToday_Yesterday(
                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
                false
        )) {

            resultado = hmAux_Trans.get("YESTERDAY");

        }
        //
        if (resultado.equalsIgnoreCase("")) {
            tv_message.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
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

}