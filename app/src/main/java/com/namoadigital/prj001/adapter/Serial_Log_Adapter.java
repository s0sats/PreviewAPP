package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    public static final String SYS_PROCESS_SO = "PRC_SERVICE_ORDER";
    public static final String SYS_PROCESS_N_FORM = "PRC_CUSTOM_FORM";

    private Context context;
    //private ArrayList<HMAux> source;
    private ArrayList<Serial_Log_Obj> source;
    private int resource;
    private HMAux hmAux_Trans;
    private String mResource_Code;
    private String mResource_Name = "serial_log_adapter" ;
    private ivDownloadClick ivDownloadClickListner;

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

    public void setIvDownloadClickListner(ivDownloadClick ivDownloadClickListner) {
        this.ivDownloadClickListner = ivDownloadClickListner;
    }

    public interface ivDownloadClick{
        //void onIvDowloadClick(String process, String[] pk, boolean alreadyDownloaded);
        void onIvDowloadClick(String process, Serial_Log_Obj logObj, int position);
    }

    public void updateItemData(Serial_Log_Obj logObj, int position){
        source.set(position,logObj);
        notifyDataSetChanged();
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
        final Serial_Log_Obj logObj = source.get(position);
        //
        TextView tv_process = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_process);
        TextView tv_status = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_status);
        ImageView iv_download = (ImageView) convertView.findViewById(R.id.serial_log_cell_iv_download);
        TextView tv_desc_lbl = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_desc_lbl);
        TextView tv_desc = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_desc);
        TextView tv_location_lbl = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_location_lbl);
        TextView tv_location = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_location);
        TextView tv_datetime = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_datetime);
        TextView tv_user_action = (TextView) convertView.findViewById(R.id.serial_log_cell_tv_user_action);
        //
        tv_process.setText(logObj.getProcess());
        tv_status.setText((hmAux_Trans.get(logObj.getSys_status())));
        tv_status.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(logObj.getSys_status())));
        tv_desc_lbl.setText(hmAux_Trans.get("description_lbl"));
        tv_desc.setText(logObj.getDescription());
        tv_location_lbl.setText(hmAux_Trans.get("location_lbl"));
        tv_location.setText(logObj.getLocation());
        tv_user_action.setText(logObj.getUser_action());
        if(logObj.getDatetime() == null){
            tv_datetime.setVisibility(View.GONE);
        }else {
            tv_datetime.setVisibility(View.VISIBLE);
            tv_datetime.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(logObj.getDatetime()),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
            );
        }
//        //Se for SO e status DONE e usr possui acesso ao menu so, exibe btn de download.
//        if( logObj.getSys_process().equals(SYS_PROCESS_SO)
//            && logObj.getSys_status().equals(Constant.SYS_STATUS_DONE)
//            && ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO,null)
//            && ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO,Constant.PROFILE_MENU_SO_PARAM_DOWNLOAD_SO_HISTORIC)
//        ){
//            Drawable drawable = null;
//            if(logObj.isLog_downloaded()){
//                //iv_download.setImageDrawable(context.getDrawable(R.drawable.ic_n_service2_24x24));
//                drawable = context.getDrawable(R.drawable.ic_file_download_black_24dp);
//                drawable.setTint(context.getResources().getColor(R.color.namoa_status_done));
//                drawable.mutate();
//                iv_download.setImageDrawable(drawable);
//            }else{
//                drawable = context.getDrawable(R.drawable.ic_file_download_black_24dp);
//                drawable.setTint(context.getResources().getColor(R.color.namoa_dark_blue));
//                drawable.mutate();
//                iv_download.setImageDrawable(drawable);
//            }
//            iv_download.setVisibility(View.VISIBLE);
//            //
//            iv_download.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(ivDownloadClickListner != null){
//                        ivDownloadClickListner.onIvDowloadClick(SYS_PROCESS_SO,logObj.getSplitedPk(),logObj.isLog_downloaded());
//                    }
//                }
//            });
//        }else{
//            iv_download.setVisibility(View.GONE);
//            iv_download.setOnClickListener(null);
//        }
        //
        switch (logObj.getSys_process()){
            case SYS_PROCESS_SO:
                processSOView(iv_download,logObj, position);
                break;
            case SYS_PROCESS_N_FORM:
                processNFormView(iv_download,logObj,position);
                break;
            default:
                iv_download.setVisibility(View.GONE);
                iv_download.setOnClickListener(null);
        }

        return convertView;
    }

    private void processNFormView(ImageView iv_download, final Serial_Log_Obj logObj, final int position) {
        Drawable drawable = null;

        if(logObj.getSys_status().equals(Constant.SYS_STATUS_DONE)) {
            //Se PDF ja gerado, verifica se ja foi baixado e define a cor do icone
            //Verde: Se PDF ja existe localmente
            //Azul: Ainda nã baixado.
            //Se não existe PDF gerado, icone fica preto(else)
            if (logObj.getFile_url() != null && !logObj.getFile_url().isEmpty()) {
                if (logObj.isLog_downloaded()) {
                    drawable = context.getDrawable(R.drawable.ic_file_download_black_24dp);
                    drawable.setTint(context.getResources().getColor(R.color.namoa_status_done));
                    drawable.mutate();
                    iv_download.setImageDrawable(drawable);
                } else {
                    drawable = context.getDrawable(R.drawable.ic_file_download_black_24dp);
                    drawable.setTint(context.getResources().getColor(R.color.namoa_dark_blue));
                    drawable.mutate();
                    iv_download.setImageDrawable(drawable);
                }
            } else {
                drawable = context.getDrawable(R.drawable.ic_file_download_black_24dp);
                drawable.setTint(context.getResources().getColor(R.color.namoa_status_process));
                drawable.mutate();
                iv_download.setImageDrawable(drawable);
            }
            //
            iv_download.setVisibility(View.VISIBLE);
            //
            iv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ivDownloadClickListner != null) {
                        ivDownloadClickListner.onIvDowloadClick(
                                SYS_PROCESS_N_FORM,
                                logObj,
                                position
                        );
                    }
                }
            });
        }else{
            iv_download.setVisibility(View.GONE);
            iv_download.setOnClickListener(null);
        }
    }

    private void processSOView(ImageView iv_download, final Serial_Log_Obj logObj, final int position) {
        if( logObj.getSys_status().equals(Constant.SYS_STATUS_DONE)
            && ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO,null)
            && ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO,Constant.PROFILE_MENU_SO_PARAM_DOWNLOAD_SO_HISTORIC)
        ){
            Drawable drawable = null;
            if(logObj.isLog_downloaded()){
                //iv_download.setImageDrawable(context.getDrawable(R.drawable.ic_n_service2_24x24));
                drawable = context.getDrawable(R.drawable.ic_file_download_black_24dp);
                drawable.setTint(context.getResources().getColor(R.color.namoa_status_done));
                drawable.mutate();
                iv_download.setImageDrawable(drawable);
            }else{
                drawable = context.getDrawable(R.drawable.ic_file_download_black_24dp);
                drawable.setTint(context.getResources().getColor(R.color.namoa_dark_blue));
                drawable.mutate();
                iv_download.setImageDrawable(drawable);
            }
            //
            iv_download.setVisibility(View.VISIBLE);
            //
            iv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ivDownloadClickListner != null){
                        ivDownloadClickListner.onIvDowloadClick(
                                SYS_PROCESS_SO,
                                logObj,
                                position);
                    }
                }
            });
        }else{
            iv_download.setVisibility(View.GONE);
            iv_download.setOnClickListener(null);
        }
    }

    private void loadTransation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("description_lbl");
        translateList.add("location_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }
}
