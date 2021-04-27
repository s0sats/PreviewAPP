package com.namoadigital.prj001.ui.act005;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.databinding.Act005OpcContentBinding;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 31/01/2017.
 */

public class Act005_Opc_Bkp extends Fragment {

    public static final String DRAWER_KEY_OPC_ID = "drawer_opc_id";
    public static final String DRAWER_KEY_OPC_ICON = "drawer_opc_icon";
    public static final String DRAWER_KEY_OPC_DESC = "drawer_opc_desc";
    //
    public static final String DRAWER_OPC_CUSTOMER = "drawer_opc_customer";
    public static final String DRAWER_OPC_SITE = "drawer_opc_site";
    public static final String DRAWER_OPC_ZONE = "drawer_opc_zone";
    public static final String DRAWER_OPC_OPERATION = "drawer_opc_operation";
    public static final String DRAWER_OPC_LOGOUT = "drawer_opc_logout";
    //
    private Act005OpcContentBinding binding;
    //
    private ImageView iv_logo;
    private ListView lv_opc;
    private LinearLayout ll_unfinished_forms;
    private LinearLayout ll_sync;
    private LinearLayout ll_logout;

    private TextView tv_unfinished_forms_label;
    private TextView tv_sync_label;
    private TextView tv_logout_label;
    private IAct005_Opc delegate;
    private List<HMAux> drawerItemList;
    private HMAux hmAux_Trans;
    private SimpleAdapter sAdapter;

    public interface IAct005_Opc {
        void itemClicked(String index);

        void syncClicked();

        void logoutClicked();
    }

    public void setOnOpcItemClicked(IAct005_Opc delegate) {
        this.delegate = delegate;
    }
    //

    public void setHmAux_Trans(HMAux hmAux_Trans, String mModule_Code, String mResource_Code) {
        this.hmAux_Trans = hmAux_Trans;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Act005OpcContentBinding.inflate(inflater,  container, false);
        //
        iniVar();
        iniAction();
        //
        return binding.getRoot();

    }

    private void iniVar() {

        //setLabels();

    }

    private void lvSetup() {
        String[] from = {DRAWER_KEY_OPC_ICON, DRAWER_KEY_OPC_DESC};
        int[] to = {R.id.act005_opc_cell_iv_icon, R.id.act005_opc_cell_tv_desc};
        sAdapter = new SimpleAdapter(
                getActivity(),
                loadOptions(),
                R.layout.act005_opc_cell,
                from,
                to
        );

        lv_opc.setAdapter(sAdapter);

    }

    private void iniAction() {
//        lv_opc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (delegate != null) {
//                    HMAux item = (HMAux) parent.getItemAtPosition(position);
//                    delegate.itemClicked(item.get(DRAWER_KEY_OPC_ID));
//                }
//            }
//        });
//
//        ll_sync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                delegate.syncClicked();
//            }
//        });
//
//        ll_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                delegate.logoutClicked();
//            }
//        });
    }

    private List<HMAux> loadOptions() {
        String[] id = {
                DRAWER_OPC_CUSTOMER,
                DRAWER_OPC_SITE,
                DRAWER_OPC_ZONE,
                DRAWER_OPC_OPERATION,
                //   DRAWER_OPC_LOGOUT
        };
        String[] icon = {
                "",
                "",
                "",
                "",
                // String.valueOf(R.drawable.ic_settings_power_red_24dp)
        };

        String[] desc = {
                "lbl_change_customer",
                "lbl_change_site",
                "lbl_change_zone",
                "lbl_change_operation",
                //  "lbl_logout",
        };
        drawerItemList = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {

            if ( id[i].equals(DRAWER_OPC_ZONE)
                 && !ToolBox_Inf.profileExists(getActivity(), Constant.PROFILE_PRJ001_SO, null)
                 && !ToolBox_Inf.profileExists(getActivity(), Constant.PROFILE_PRJ001_OI, null)
            ) {
                continue;
            }
            HMAux hmAux = new HMAux();
            hmAux.put(DRAWER_KEY_OPC_ID, id[i]);
            hmAux.put(DRAWER_KEY_OPC_ICON, icon[i]);
            hmAux.put(DRAWER_KEY_OPC_DESC, desc[i]);

            drawerItemList.add(hmAux);
        }

        return drawerItemList;
    }

    public void setPendingForms(boolean status) {
//        if (status) {
//            ll_unfinished_forms.setVisibility(View.VISIBLE);
//        } else {
//            ll_unfinished_forms.setVisibility(View.GONE);
//        }
    }
}
