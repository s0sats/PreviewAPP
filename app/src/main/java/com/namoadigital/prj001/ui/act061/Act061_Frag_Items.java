package com.namoadigital.prj001.ui.act061;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.util.ConstantBaseApp;

import java.util.HashMap;

public class Act061_Frag_Items extends BaseFragment {

    private boolean bStatus = false;
    private Context context;
    private int inboundPrefix;
    private int inboundCode;
    private IO_Inbound mInbound;

    private onFragItemInteraction mFragItemListener;
    //

    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     *
     */
    public interface onFragItemInteraction{

    }

    public static Act061_Frag_Items getInstance(HMAux hmAux_Trans, int inbound_prefix, int inbound_code){
        Act061_Frag_Items fragment = new Act061_Frag_Items();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        args.putInt(IO_InboundDao.INBOUND_PREFIX,inbound_prefix);
        args.putInt(IO_InboundDao.INBOUND_CODE,inbound_code);
        //
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act061_frag_item_content, container, false);
        //
        recoverBundleInfo(getArguments());
        //
        iniVar(view);
        //
        iniAction();
        //
        return view;
    }

    private void recoverBundleInfo(Bundle arguments) {
        this.context = getActivity();
        //
        if(arguments != null){
            hmAux_Trans =  HMAux.getHmAuxFromHashMap((HashMap<String,String>)arguments.getSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY));
            inboundPrefix = arguments.getInt(IO_InboundDao.INBOUND_PREFIX,-1);
            inboundCode = arguments.getInt(IO_InboundDao.INBOUND_CODE,-1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if (context instanceof Act061_Frag_Drawer.onFragDrawerInteraction) {
            mFragItemListener = (onFragItemInteraction) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragItemListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //
        bStatus = false;
    }

    private void iniVar(View view) {
    }

    private void iniAction() {
    }
}
