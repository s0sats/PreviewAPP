package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
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

public class Act035_Adapter_Messages extends BaseAdapter {

    private Context context;
    //
    private int resource_01;
    private int resource_02;
    private int resource_03;
    private int resource_04;
    //
    private ArrayList<HMAux> data;

    private String mUser_Code;

    private int mSizeAddUpdate = 0;

    public static boolean processingHMAux = false;

    public Act035_Adapter_Messages(Context context, int resource_01, int resource_02, int resource_03, int resource_04, ArrayList<HMAux> data) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.resource_03 = resource_03;
        this.resource_04 = resource_04;

        this.data = data;

        this.mUser_Code = ToolBox_Con.getPreference_User_Code(context);
    }

    // Interface só é chamada como para as mensagens que não forem as minhas.
//    public interface IAct035_Adapter_Messages {
//        //void updateReadStatus(HMAux hmAux, int position);
//        void updateReadStatus(ArrayList<HMAux> hmAuxs);
//    }

//    private IAct035_Adapter_Messages delegate;

//    public void setOnUpdateReadStatus(IAct035_Adapter_Messages delegate) {
//        this.delegate = delegate;
//    }


    public int getmSizeAddUpdate() {
        int localValue = mSizeAddUpdate;
        mSizeAddUpdate = 0;
        //
        return localValue;
    }

    public void refreshData(HMAux hmAux) {
        for (int i = data.size() - 1; i >= 0; i--) {
            if (hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase(data.get(i).get(CH_MessageDao.MSG_CODE))) {
                data.set(i, hmAux);
                //
                notifyDataSetChanged();
                //
                break;
            }
        }
    }

    public void refill(List<HMAux> dadosR) {
        for (int i = 0; i < dadosR.size(); i++) {
            HMAux msg = dadosR.get(i);
            //
            for (int j = data.size() - 1; j >= 0; j--) {
                HMAux item = data.get(j);
                if (msg.get(CH_MessageDao.TMP).equalsIgnoreCase(item.get(CH_MessageDao.TMP))) {

                    data.set(j, msg);

                    break;
                }
            }
        }

        notifyDataSetChanged();


//        data.clear();
//        data.addAll(dadosR);
//        //
//        notifyDataSetChanged();
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
            //
            if (!sFound) {
                dadosRNew.add(msg);
            }
        }
        //
        if (dadosRNew.size() > 0) {
            //
            reOrder = checkReOrder(data, (ArrayList<HMAux>) dadosRNew);
            //
            data.addAll(dadosRNew);
            //
            mSizeAddUpdate = dadosRNew.size();
            //
//            notifyDataSetChanged();
        } else {
            mSizeAddUpdate = 0;
            //
            reOrder = false;
        }
        //
//        if (countFound > 0){
//            notifyDataSetChanged();
//        }
        //
        notifyDataSetChanged();
        //
        return reOrder;
    }

    public boolean checkReOrder(ArrayList<HMAux> dados, ArrayList<HMAux> messages) {
        String sMessages = messages.get(0).get("msg_pk");
        //
        for (int i = dados.size() - 1; i >= 0; i--) {
            HMAux aux = dados.get(i);
            //
            if (aux.get("msg_pk") != null && !aux.get("msg_pk").isEmpty()) {

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
        return 4;
    }

    @Override
    public int getItemViewType(int position) {

        try {
            HMAux item = data.get(position);
            JSONObject msg_obj = new JSONObject(item.get("msg_obj"));
            JSONObject message = msg_obj.getJSONObject("message");

            if (message.getString("type").equalsIgnoreCase("IMAGE")) {

                if (!item.get("user_code").equalsIgnoreCase(mUser_Code)) {
                    return 0;
                } else {
                    return 1;
                }

            } else {
                if (item.get("user_code").equalsIgnoreCase(mUser_Code)) {
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
        return getItemViewType(position) == 0 || getItemViewType(position) == 1 ? true : false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;
                case 2:
                    convertView = mInflater.inflate(resource_03, parent, false);
                    break;
                case 3:
                    convertView = mInflater.inflate(resource_04, parent, false);
                    break;

            }
        }
        //
        JSONObject message = null;
        HMAux hmAux = data.get(position);

        try {
            JSONObject msg_obj = new JSONObject(hmAux.get("msg_obj"));
            message = msg_obj.getJSONObject("message");
        } catch (JSONException e) {
            message = new JSONObject();
        }
        //
        View v_space_left;
        ImageView iv_other_img;
        LinearLayout ll_item;
        TextView tv_name;
        ImageView iv_foto;
        TextView tv_message;
        TextView tv_hour;
        ImageView iv_me_img;
        View v_space_right;
        ImageView iv_badge = null;

        switch (getItemViewType(position)) {
            // Other IMG
            case 0:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
                iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);
                iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);

                v_space_left.setVisibility(View.GONE);
                iv_other_img.setVisibility(View.VISIBLE);
                ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_from));
                tv_name.setText("Nome");

                if (hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).isEmpty()) {
                    iv_foto.setImageResource(R.drawable.sand_watch_transp);
                } else {
                    iv_foto.setImageBitmap(BitmapFactory.decodeFile(Constant.THU_PATH + "/" +
                            hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).replace(".jpg", "") + "_thumb.jpg"
                    ));
                }

                //tv_message.setText("Message");

                tv_hour.setText(

                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                " HH:mm"
                        )
                );

                tv_name.setText(hmAux.get("user_nick"));

//                if (hmAux.get(CH_MessageDao.READ).equalsIgnoreCase("0")) {
//                    if (delegate != null) {
//                        delegate.updateReadStatus(hmAux, position);
//                    }
//                }

                iv_me_img.setVisibility(View.GONE);
                v_space_right.setVisibility(View.VISIBLE);
                iv_badge.setVisibility(View.GONE);

                break;

            // Me IMG
            case 1:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);

                iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);

                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);
                iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);

                v_space_left.setVisibility(View.VISIBLE);
                iv_other_img.setVisibility(View.GONE);
                ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_me));
                tv_name.setVisibility(View.INVISIBLE);

                if (hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).isEmpty()) {
                    iv_foto.setImageResource(R.drawable.sand_watch_transp);
                } else {
                    iv_foto.setImageBitmap(BitmapFactory.decodeFile(Constant.THU_PATH + "/" +
                            hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).replace(".jpg", "") + "_thumb.jpg"
                    ));
                }

                tv_hour.setText(

                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                " HH:mm"

                        )
                );

                tv_name.setText(hmAux.get("user_nick"));

                iv_me_img.setVisibility(View.VISIBLE);
                v_space_right.setVisibility(View.GONE);
                iv_badge.setVisibility(View.VISIBLE);

                break;

            // Me TXT
            case 2:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);

                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);
                iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);

                v_space_left.setVisibility(View.VISIBLE);
                iv_other_img.setVisibility(View.GONE);
                ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_me));
                tv_name.setVisibility(View.GONE);

                try {
                    tv_message.setText(message.getString("data").trim());
                } catch (JSONException e) {
                    tv_message.setText("Error data");
                }

                tv_hour.setText(

                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                " HH:mm"

                        )
                );


                tv_name.setText(hmAux.get("user_nick"));

                iv_me_img.setVisibility(View.VISIBLE);
                v_space_right.setVisibility(View.GONE);
                iv_badge.setVisibility(View.VISIBLE);

                break;

            // Other TXT
            case 3:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);

                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);
                iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);

                v_space_left.setVisibility(View.GONE);
                iv_other_img.setVisibility(View.VISIBLE);
                ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_from));
                tv_name.setVisibility(View.VISIBLE);

                try {
                    tv_message.setText(message.getString("data").trim());
                } catch (JSONException e) {
                    tv_message.setText("Error data");
                }


                tv_hour.setText(

                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                " HH:mm"

                        )
                );

                tv_name.setText(hmAux.get("user_nick"));

//                if (hmAux.get(CH_MessageDao.READ).equalsIgnoreCase("0")) {
//                    if (delegate != null) {
//                        delegate.updateReadStatus(hmAux, position);
//                    }
//                }

                iv_me_img.setVisibility(View.GONE);
                v_space_right.setVisibility(View.VISIBLE);
                iv_badge.setVisibility(View.GONE);

                break;
        }
        //
        // Badge Status for All
        if (hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase("0")) {
            iv_badge.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clock_chat));
        } else {
            iv_badge.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_black_24dp));
        }
        //
        if (hmAux.get(CH_MessageDao.ALL_DELIVERED).equalsIgnoreCase("1")) {
            iv_badge.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp));
        }
        //
        if (hmAux.get(CH_MessageDao.ALL_READ).equalsIgnoreCase("1")) {
            Drawable done_icon = context.getDrawable(R.drawable.ic_done_all_black_24dp);
            done_icon.setColorFilter(context.getResources().getColor(R.color.namoa_color_success_green), PorterDuff.Mode.SRC_ATOP);
            //
            iv_badge.setImageDrawable(done_icon);
        }
        //
        return convertView;
    }
}