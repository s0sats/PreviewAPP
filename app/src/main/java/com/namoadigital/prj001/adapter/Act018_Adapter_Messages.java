package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

    public Act018_Adapter_Messages(Context context, int resource, List<HMAux> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = data.get(position);

        ImageView iv_001 = (ImageView)
                convertView.findViewById(R.id.act018_main_content_cell_iv_001);

        ImageView iv_002 = (ImageView)
                convertView.findViewById(R.id.act018_main_content_cell_iv_002);


        TextView tv_title = (TextView)
                convertView.findViewById(R.id.act018_main_content_cell_tv_title);

        TextView tv_date = (TextView)
                convertView.findViewById(R.id.act018_main_content_cell_tv_date);

        TextView tv_msg_short = (TextView)
                convertView.findViewById(R.id.act018_main_content_cell_tv_msg_short);

        switch (item.get("module")) {
            case MODULE_CHECKLIST:
                iv_001.setImageResource(R.drawable.ic_n_form);
                break;
            default:
                iv_001.setVisibility(View.GONE);
                break;
        }

        switch (item.get("type").toLowerCase()) {
            case ALERT:
                iv_002.setImageResource(R.drawable.ic_alert_n);
                break;
            case WARNING:
                iv_002.setImageResource(R.drawable.ic_problem_n);
                break;
            default:
                break;
        }

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
        } else {
            tv_title.setTypeface(null, Typeface.BOLD);
            tv_date.setTypeface(null, Typeface.BOLD);
        }

        return convertView;
    }
}
