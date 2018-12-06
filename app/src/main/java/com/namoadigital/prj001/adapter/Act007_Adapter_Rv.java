package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LUCAS.BARRIONUEVO on 05/12/2018.
 */
public class Act007_Adapter_Rv extends RecyclerView.Adapter<Act007_Adapter_Rv.Act007_Adapter_RvHolder>{
    public static final String SYS_PROCESS_SO = "PRC_SERVICE_ORDER";
    private Context context;
    //private ArrayList<HMAux> source;
    private ArrayList<Serial_Log_Obj> source;
    private int resource;
    private HMAux hmAux_Trans;
    private String mResource_Code;
    private String mResource_Name = "serial_log_adapter" ;
    private Serial_Log_Adapter.ivDownloadClick ivDownloadClickListner;


    public Act007_Adapter_Rv(Context context, ArrayList<Serial_Log_Obj> source, int resource) {
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

    @Override
    public int getItemCount() {
        return source.size();
    }

    @Override
    public Act007_Adapter_RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,parent,false);


        Act007_Adapter_RvHolder holder = new Act007_Adapter_RvHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Act007_Adapter_RvHolder holder, int position) {

        final Serial_Log_Obj logObj = source.get(position);

        holder.bind(logObj);
    }

    public void setIvDownloadClickListner(Serial_Log_Adapter.ivDownloadClick ivDownloadClickListner) {
        this.ivDownloadClickListner = ivDownloadClickListner;
    }
    public interface ivDownloadClick{
        void onIvDowloadClick(String process, String[] pk, boolean alreadyDownloaded);
    }

    public class Act007_Adapter_RvHolder extends RecyclerView.ViewHolder{

        TextView tv_process;
        TextView tv_status;
        ImageView iv_download;
        TextView tv_desc_lbl;
        TextView tv_desc;
        TextView tv_location_lbl;
        TextView tv_location;
        TextView tv_datetime;
        TextView tv_user_action;

        public Act007_Adapter_RvHolder(View itemView) {
            super(itemView);
            tv_process = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_process);
            tv_status = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_status);
            iv_download = (ImageView) itemView.findViewById(R.id.serial_log_cell_iv_download);
            tv_desc_lbl = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_desc_lbl);
            tv_desc = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_desc);
            tv_location_lbl = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_location_lbl);
            tv_location = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_location);
            tv_datetime = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_datetime);
            tv_user_action = (TextView) itemView.findViewById(R.id.serial_log_cell_tv_user_action);
        }

        private void bind(final Serial_Log_Obj logObj) {
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
            //Se for SO e status DONE e usr possui acesso ao menu so, exibe btn de download.
            if( logObj.getSys_process().equals(SYS_PROCESS_SO)
                    && logObj.getSys_status().equals(Constant.SYS_STATUS_DONE)
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
                iv_download.setVisibility(View.VISIBLE);
                //
                iv_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ivDownloadClickListner != null){
                            ivDownloadClickListner.onIvDowloadClick(SYS_PROCESS_SO,logObj.getSplitedPk(),logObj.isLog_downloaded());
                        }
                    }
                });
            }else{
                iv_download.setVisibility(View.GONE);
                iv_download.setOnClickListener(null);
            }
        }

    }
}
