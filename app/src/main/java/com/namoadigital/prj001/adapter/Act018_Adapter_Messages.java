package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 02/02/17.
 */

public class Act018_Adapter_Messages extends BaseAdapter {

    public static final String ALERT = "alert";
    public static final String WARNING = "warning";
    public static final String MODULE_CHECKLIST = "CHECKLIST";

    private Context context;
    private int resource;
    private List<HMAux> data;
    private int msg_selected;

    public Act018_Adapter_Messages(Context context, int resource, List<HMAux> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.msg_selected = -1;
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
        return Long.parseLong((data.get(position).get("fcmmessage_code")));
    }

    public void setMsg_selected(int position){
        msg_selected = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = data.get(position);

        TextView tv_customer = (TextView)
                convertView.findViewById(R.id.act018_main_content_cell_tv_customer);

        ImageView iv_001 = (ImageView)
                convertView.findViewById(R.id.act018_main_content_cell_iv_001);

        ImageView iv_002 = (ImageView)
                convertView.findViewById(R.id.act018_main_content_cell_iv_002);

        ImageView iv_003 = (ImageView)
                convertView.findViewById(R.id.act018_main_content_cell_iv_003);


        TextView tv_title = (TextView)
                convertView.findViewById(R.id.act018_main_content_cell_tv_title);

        TextView tv_date = (TextView)
                convertView.findViewById(R.id.act018_main_content_cell_tv_date);

        TextView tv_msg_short = (TextView)
                convertView.findViewById(R.id.act018_main_content_cell_tv_msg_short);

        LinearLayout ll_background = (LinearLayout) convertView.findViewById(R.id.act018_main_content_cell_ll_background);

        switch (item.get("module")) {
            case MODULE_CHECKLIST:
                iv_002.setImageResource(R.drawable.ic_n_form);
                break;
            default:
                iv_002.setVisibility(View.GONE);
                break;
        }

        switch (item.get("type").toLowerCase()) {
            case ALERT:
                iv_003.setImageResource(R.drawable.ic_alert_n);
                break;
            case WARNING:
                iv_003.setImageResource(R.drawable.ic_problem_n);
                break;
            default:
                break;
        }

        tv_customer.setText(item.get("customer_name"));
        tv_title.setText(item.get("title"));
        tv_date.setText(
                ToolBox.millisecondsToString(
                        Long.parseLong(item.get("date_create_ms")),
                        //ToolBox_Inf.nlsDate2SqliteDate(context).replace("%", " ").replace("/ ", "-").replace("Y","y").replace("m","M")
                        ToolBox_Inf.nlsDateFormat(context)
                )
        );

        tv_msg_short.setText(item.get("msg_short"));


        if (item.get("status").equalsIgnoreCase("1")) {
            tv_title.setTypeface(null, Typeface.NORMAL);
            tv_date.setTypeface(null, Typeface.NORMAL);

            Drawable bgDrawble = context.getDrawable(R.drawable.namoa_cell_8_states);
            ll_background.setBackground(bgDrawble);

            Drawable done_icon = context.getDrawable(R.drawable.ic_done_all_black_24dp);
            done_icon.setColorFilter(context.getResources().getColor(R.color.namoa_color_success_green), PorterDuff.Mode.SRC_ATOP);
            iv_001.setImageDrawable(done_icon);

            iv_001.setVisibility(View.VISIBLE);
        } else {
            tv_title.setTypeface(null, Typeface.BOLD);
            tv_date.setTypeface(null, Typeface.BOLD);
//            tv_customer.setTextColor(context.getResources().getColor(R.color.font_required));
//            tv_title.setTextColor(context.getResources().getColor(R.color.font_required));
//            tv_date.setTextColor(context.getResources().getColor(R.color.font_required));
        }
        //Se o item for o selecionado,
        //muda cor da linha e do fundo drawable
        if(position == msg_selected){
            StateListDrawable drawble = (StateListDrawable) context.getDrawable(R.drawable.namoa_cell_4_states);//ll_background.getBackground();
            DrawableContainer.DrawableContainerState dcs = (DrawableContainer.DrawableContainerState) drawble.getConstantState();
            Drawable[] drawableItems = dcs.getChildren();

            GradientDrawable  bgDrawble = (GradientDrawable) drawableItems[0];

            bgDrawble.setStroke(2,context.getResources().getColor(R.color.namoa_color_orange));
            bgDrawble.setColor(context.getResources().getColor(R.color.namoa_color_orange_light));

            ll_background.setBackground(bgDrawble);
        }

        return convertView;
    }
}
