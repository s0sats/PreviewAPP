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
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.sql.Sql_Act028_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act028_Exec_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Act028_Exec_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "act028_exec_adapter"
        );

        loadTranslation();
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

        //TextView tv_exec_tmp_label = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_tmp_label);
        TextView tv_exec_tmp_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_tmp_value);
        ImageView iv_usr = (ImageView) convertView.findViewById(R.id.act028_main_content_cell_iv_usr);
        TextView tv_exec_status = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_status);
        //
        LinearLayout ll_line2 = (LinearLayout) convertView.findViewById(R.id.act028_main_content_cell_ll_line2);
        ImageView iv_percent = (ImageView) convertView.findViewById(R.id.act028_main_content_cell_iv_percent);
        TextView tv_percent_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_percent_val);
        //
        ImageView iv_sum_time = (ImageView) convertView.findViewById(R.id.act028_main_content_cell_iv_sum_time);
        TextView tv_sum_time_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_sum_time_val);
        //
        LinearLayout ll_line3 = (LinearLayout) convertView.findViewById(R.id.act028_main_content_cell_ll_line3);
        //
        LinearLayout ll_comments = (LinearLayout) convertView.findViewById(R.id.act028_main_content_cell_ll_comments);
        ImageView iv_comment = (ImageView) convertView.findViewById(R.id.act028_main_content_cell_iv_comment);
        TextView tv_comment_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_comment_val);
        //
        LinearLayout ll_gallery = (LinearLayout) convertView.findViewById(R.id.act028_main_content_cell_ll_gallery);
        ImageView iv_gallery = (ImageView) convertView.findViewById(R.id.act028_main_content_cell_iv_gallery);
        TextView tv_gallery_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_gallery_val);

        //tv_exec_tmp_label.setText(hmAux_Trans.get("exec_tmp_lbl"));
        tv_exec_tmp_val.setText(String.valueOf(item.get(SM_SO_Service_ExecDao.EXEC_TMP)));
        tv_exec_status.setText(hmAux_Trans.get(item.get(SM_SO_Service_ExecDao.STATUS)));
        ToolBox_Inf.setExecStatusColor(context,tv_exec_status,item.get(SM_SO_Service_ExecDao.STATUS));
        //
        if(item.get(SM_SO_Service_ExecDao.STATUS).equals(Constant.SO_STATUS_INCONSISTENT)){
            ll_line2.setVisibility(View.GONE);
            ll_line3.setVisibility(View.GONE);
        }else{
            ll_line2.setVisibility(View.VISIBLE);
        }
        //
        if(item.get(Sql_Act028_001.MY_TASK) != null
                && !item.get(Sql_Act028_001.MY_TASK).equals("0")
                && !item.get(Sql_Act028_001.MY_TASK).equals("")

                ){
            iv_usr.setVisibility(View.VISIBLE);
        }else {
            iv_usr.setVisibility(View.GONE);
        }
        //
        if(item.get(Sql_Act028_001.TASK_PERC) != null){
            iv_percent.setVisibility(View.VISIBLE);
            tv_percent_val.setText(item.get(Sql_Act028_001.TASK_PERC) + "%");
        }else{
            iv_percent.setVisibility(View.INVISIBLE);
            tv_percent_val.setText("");
        }
        //
        if(item.get(Sql_Act028_001.SUM_EXEC_TIME) != null){
            iv_sum_time.setVisibility(View.VISIBLE);
            tv_sum_time_val.setText(item.get(Sql_Act028_001.SUM_EXEC_TIME));
        }else{
            iv_sum_time.setVisibility(View.INVISIBLE);
            tv_sum_time_val.setText("");
        }

        tv_comment_val.setText(item.get(Sql_Act028_001.QTY_COMMENT));
        tv_gallery_val.setText(item.get(Sql_Act028_001.QTY_FILES));

        return convertView;
    }

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("exec_tmp_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }
}
