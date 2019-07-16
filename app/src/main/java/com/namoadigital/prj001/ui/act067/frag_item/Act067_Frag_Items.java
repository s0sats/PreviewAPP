package com.namoadigital.prj001.ui.act067.frag_item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act067_IO_Items_Adapter;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.ui.act067.frag_drawer.Act067_Frag_Drawer;
import com.namoadigital.prj001.util.ConstantBaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Act067_Frag_Items extends BaseFragment implements Act067_Frag_Items_Contract.I_View{
    public static final String FRAG_SWITCH_STATE = "FRAG_SWITCH_STATE";

    private boolean bStatus = false;
    private Context context;
    private int outboundPrefix;
    private int outboundCode;
    private IO_Outbound mOutbound;
    private Act067_Frag_Items_Presenter mPresenter;
    private onFragItemInteraction mFragItemListener;
    private boolean pausedByScan = false;
    private boolean filterActionPendencies = true;
    //
    private MKEditTextNM mketFilter;
    private Switch swActionFilter;
    private RecyclerView rvItems;
    private Act067_IO_Items_Adapter mAdapter;
    //private Button btnAddItem;

    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     *
     */
    public interface onFragItemInteraction{
        /**
         * Metodo chamado para carregar Outbound do banco de dados.
         *
         * @param prefix
         * @param code
         */
        IO_Outbound getOutboundFromAct(int prefix, int code);

        void addFragItemsControlsMk(ArrayList<MKEditTextNM> controls_sta);

        void removeFragHeaderControlsSta(ArrayList<MKEditTextNM> controls_sta);

        void callAddItemAct();

        void callOutConfCreateItemAct(HMAux item);

        void callPickingCreateItemAct(HMAux item);

        void callSerialEdition(HMAux item);
    }

    public static Act067_Frag_Items getInstance(HMAux hmAux_Trans, int outbound_prefix, int outbound_code){
        Act067_Frag_Items fragment = new Act067_Frag_Items();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        args.putInt(IO_OutboundDao.OUTBOUND_PREFIX,outbound_prefix);
        args.putInt(IO_OutboundDao.OUTBOUND_CODE,outbound_code);
        args.putBoolean(FRAG_SWITCH_STATE, true);
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
        View view = inflater.inflate(R.layout.act067_frag_item_content, container, false);
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
            outboundPrefix = arguments.getInt(IO_OutboundDao.OUTBOUND_PREFIX,-1);
            outboundCode = arguments.getInt(IO_OutboundDao.OUTBOUND_CODE,-1);
            filterActionPendencies = arguments.getBoolean(FRAG_SWITCH_STATE,true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Se chamada do reload foi por causa da leitura de barcode.
        //pula recarga de dados.
        if (!pausedByScan) {
            loadDataToScreen();
            //filterWithExistis();
        }
        //
        pausedByScan = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if (context instanceof Act067_Frag_Drawer.onFragDrawerInteraction) {
            mFragItemListener = (onFragItemInteraction) context;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //
        if(getArguments() != null){
            getArguments().putBoolean(FRAG_SWITCH_STATE,filterActionPendencies);
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
        if(mFragItemListener != null){
            mFragItemListener.removeFragHeaderControlsSta(controls_sta);
        }
        //
        bStatus = false;
    }

    private void iniVar(View view) {
        bindViews(view);
        //
        loadOutbound();
        //
        mPresenter = new Act067_Frag_Items_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
    }

    private void bindViews(View view) {
        mketFilter = view.findViewById(R.id.act067_item_frag_mket_filter);
        swActionFilter = view.findViewById(R.id.act067_item_frag_sw_filter);
        rvItems = view.findViewById(R.id.act067_item_frag_rv_items);
        //
        mketFilter.setmBARCODE(true);
        mketFilter.setHint(hmAux_Trans.get("serial_filter_hint"));
        //
        controls_sta.add(mketFilter);
        //
        if(mFragItemListener != null){
            mFragItemListener.addFragItemsControlsMk(controls_sta);
        }
    }

    private void loadOutbound() {
        if (mFragItemListener != null) {
            mOutbound = mFragItemListener.getOutboundFromAct(outboundPrefix,outboundCode);
        }
    }

    private void iniAction() {
        mketFilter.setOnReportDrawbleRightClick(new MKEditTextNM.IMKEditTextDrawableRight() {
            @Override
            public void reportDrawbleRightClick(int i) {
                pausedByScan = true;
            }
        });
        //
        mketFilter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if(mAdapter != null){
                    reapplyFilters();
                }
            }
        });
        //
        swActionFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mAdapter != null){
                    filterActionPendencies = isChecked;
                    reapplyFilters();
                }
            }
        });

    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
        if(bStatus) {
            loadOutbound();
            //
            if (mOutbound != null) {
                //
                if(mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)){
                    filterActionPendencies = !mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE);
                }
                swActionFilter.setChecked(
                        filterActionPendencies
                );
                //
                loadAdapter(
                        mPresenter.getItemList(outboundPrefix, outboundCode)
                );
                //
                reapplyFilters();
            }
        }
    }

    private void loadAdapter(ArrayList<HMAux> itemList) {
        if(itemList != null){
            mAdapter = new Act067_IO_Items_Adapter(
                    context,
                    R.layout.act067_frag_item_cell,
                    itemList,
                    allowNewItem(),
                    swActionFilter.isChecked()
            );
            //
            mAdapter.setOnIoItemClickListener(new Act067_IO_Items_Adapter.OnIoItemClickListener() {
                @Override
                public void onSerialClick(HMAux item) {
                    if(mFragItemListener != null) {
                        mFragItemListener.callSerialEdition(item);
                    }
                }

                @Override
                public void onPickingDoneClick(HMAux item) {
                    if(mFragItemListener != null) {
                        mFragItemListener.callOutConfCreateItemAct(item);
                    }
                }

                @Override
                public void onPickingClick(HMAux item) {
                    if(mFragItemListener != null) {
                        mFragItemListener.callPickingCreateItemAct(item);
                    }
                }

                @Override
                public void onAddItemClick() {
                    if(mFragItemListener != null){
                        mFragItemListener.callAddItemAct();
                    }
                }
            });
            //
            rvItems.setLayoutManager(new LinearLayoutManager(context));
            rvItems.setAdapter(mAdapter);
            rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //
                    if(newState ==  RecyclerView.SCROLL_STATE_IDLE){
                        mketFilter.setEnabled(true);
                        swActionFilter.setEnabled(true);
                    }else{
                        mketFilter.setEnabled(false);
                        swActionFilter.setEnabled(false);
                    }
                }
            });

        }
    }

    private void reapplyFilters(){
        if(mAdapter != null) {
            mAdapter.updateFilterActionPendenciesStatus(filterActionPendencies);
            //
            mAdapter.getFilter().filter(mketFilter.getText().toString().trim());
        }
    }

    private boolean allowNewItem() {
        return mOutbound.getAllow_new_item() == 1
                && (!mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)
                    && !mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_CANCELLED));
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("serial_filter_hint");
        //
        return transListFrag;
    }
}
