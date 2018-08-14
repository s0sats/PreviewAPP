package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Serial_Log_Adapter extends BaseAdapter {

    private Context context;
    //private ArrayList<HMAux> source;
    private ArrayList<Serial_Log_Obj> source;
    private int resource;
    private HMAux hmAux_Trans;
    private String mResource_Code;
    private String mResource_Name = "serial_log_adapter" ;

    public Serial_Log_Adapter(Context context, ArrayList<Serial_Log_Obj> source, int resource) {
        this.context = context;
        this.source = source;
        this.resource = resource;
        //
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                mResource_Name
        );
        //
        loadTransation();
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

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource,parent,false);
        }
        //
        Serial_Log_Obj logObj = source.get(position);
        //
        TextView tv_process = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_process);
        TextView tv_status = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_status);
        TextView tv_desc = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_desc);
        TextView tv_location = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_location);
        TextView tv_datetime = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_datetime);
        TextView tv_user_action = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_user_action);
        //
//        tv_process.setText(item.get(Serial_Log_Obj.PROCESS));
//        tv_status.setText(item.get(Serial_Log_Obj.STATUS));
//        tv_desc.setText(item.get(Serial_Log_Obj.DESCRIPTION));
//        tv_location.setText(item.get(Serial_Log_Obj.LOCATION));
//        tv_user_action.setText(item.get(Serial_Log_Obj.USER_ACTION));
//        tv_datetime.setText(
//                ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(item.get(Serial_Log_Obj.DATETIME)),
//                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
//                )
//        );
        //
        tv_process.setText(logObj.getProcess());
        tv_status.setText((hmAux_Trans.get(logObj.getSys_status())));
        tv_status.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(logObj.getSys_status())));
        tv_desc.setText(logObj.getDescription());
        tv_location.setText(logObj.getLocation());
        tv_user_action.setText(logObj.getUser_action());
        tv_datetime.setText(
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(logObj.getDatetime()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
        );
        //
        return convertView;
    }

    private void loadTransation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("so_main_title");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }


}
