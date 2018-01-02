package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
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

    public void refill(List<HMAux> dadosR) {
        data.clear();
        data.addAll(dadosR);
        //
        notifyDataSetChanged();
    }

    public void addMessages(List<HMAux> dadosR) {
        data.addAll(dadosR);
        //
        notifyDataSetChanged();
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

        switch (getItemViewType(position)) {
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

                iv_me_img.setVisibility(View.INVISIBLE);
                v_space_right.setVisibility(View.VISIBLE);

                break;

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

                break;

            case 2:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);

                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);

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
                break;

            case 3:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);

                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);

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

                iv_me_img.setVisibility(View.GONE);
                v_space_right.setVisibility(View.VISIBLE);

                break;
        }
        //
        return convertView;
    }
}