package com.namoadigital.prj001.ui.act061.frg_item;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act061_IO_Items_Adapter;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.ui.act061.frag_drawer.Act061_Frag_Drawer;
import com.namoadigital.prj001.util.ConstantBaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Act061_Frag_Items extends BaseFragment implements Act061_Frag_Items_Contract.I_View{
    public static final String FRAG_SWITCH_STATE = "FRAG_SWITCH_STATE";

    private boolean bStatus = false;
    private Context context;
    private int inboundPrefix;
    private int inboundCode;
    private IO_Inbound mInbound;
    private Act061_Frag_Items_Presenter mPresenter;
    private onFragItemInteraction mFragItemListener;
    private boolean pausedByScan = false;
    private boolean filterActionPendencies = true;
    //
    private MKEditTextNM mketFilter;
    private Switch swActionFilter;
    private RecyclerView rvItems;
    private Act061_IO_Items_Adapter mAdapter;
    private String productCode;
    private String serialCode;
    //private Button btnAddItem;

    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     *
     */
    public interface onFragItemInteraction{
        /**
         * Metodo chamado para carregar Inbound do banco de dados.
         *
         * @param prefix
         * @param code
         */
        IO_Inbound getInboundFromAct(int prefix, int code);

        void addFragItemsControlsMk(ArrayList<MKEditTextNM> controls_sta);

        void removeFragItemsControlsMk(ArrayList<MKEditTextNM> controls_sta);

        void callAddItemAct();

        void callInConfCreateItemAct(HMAux item);

        void callPutAwayCreateItemAct(HMAux item);

        void callSerialEdition(HMAux item);
    }

    public static Act061_Frag_Items getInstance(HMAux hmAux_Trans, int inbound_prefix, int inbound_code, String productCode, String serialCode){
        Act061_Frag_Items fragment = new Act061_Frag_Items();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        args.putInt(IO_InboundDao.INBOUND_PREFIX,inbound_prefix);
        args.putInt(IO_InboundDao.INBOUND_CODE,inbound_code);
        args.putString(IO_Inbound_ItemDao.PRODUCT_CODE,productCode);
        args.putString(IO_Inbound_ItemDao.SERIAL_CODE,serialCode);
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
            productCode = arguments.getString(IO_Inbound_ItemDao.PRODUCT_CODE,"-1");
            serialCode = arguments.getString(IO_Inbound_ItemDao.SERIAL_CODE,"-1");
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
        if (context instanceof Act061_Frag_Drawer.onFragDrawerInteraction) {
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
            mFragItemListener.removeFragItemsControlsMk(controls_sta);
        }
        //
        bStatus = false;
    }

    private void iniVar(View view) {
        bindViews(view);
        //
        loadInbound();
        //
        mPresenter = new Act061_Frag_Items_Presenter(
            context
        );
        //
    }

    private void bindViews(View view) {
        mketFilter = view.findViewById(R.id.act061_item_frag_mket_filter);
        swActionFilter = view.findViewById(R.id.act061_item_frag_sw_filter);
        rvItems = view.findViewById(R.id.act061_item_frag_rv_items);
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

    private void loadInbound() {
        if (mFragItemListener != null) {
            mInbound = mFragItemListener.getInboundFromAct(inboundPrefix,inboundCode);
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

    /**
     * Metodo que atualizar args e vars de inbound
     * @param inboundPrefix
     * @param inboundCode
     */
    public void updateInboundArguments(int inboundPrefix, int inboundCode){
        this.inboundPrefix = inboundPrefix;
        this.inboundCode = inboundCode;
        //
        Bundle args = getArguments();
        if (args != null) {
            args.putInt(IO_InboundDao.INBOUND_PREFIX, inboundPrefix);
            args.putInt(IO_InboundDao.INBOUND_CODE, inboundCode);
        }

    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
        if(bStatus) {
            loadInbound();
            //
            if (mInbound != null) {
                //
                if(isImmutableStatus()){
                    filterActionPendencies = false;
                    swActionFilter.setEnabled(false);
                }
                swActionFilter.setChecked(
                        filterActionPendencies
                );
                //
                loadAdapter(
                    mPresenter.getItemList(inboundPrefix, inboundCode)
                );
                //
                reapplyFilters();
            }
        }
    }

    private void loadAdapter(ArrayList<HMAux> itemList) {
        if(itemList != null){
            mAdapter = new Act061_IO_Items_Adapter(
                context,
                R.layout.act061_frag_item_cell,
                itemList,
                allowNewItem(),
                swActionFilter.isChecked(),
                productCode,
                serialCode
            );
            //
            mAdapter.setOnIoItemClickListener(new Act061_IO_Items_Adapter.OnIoItemClickListener() {
                @Override
                public void onSerialClick(HMAux item) {
                    if(mFragItemListener != null) {
                        mFragItemListener.callSerialEdition(item);
                    }
                }

                @Override
                public void onConfClick(HMAux item) {
                    if(mFragItemListener != null) {
                        mFragItemListener.callInConfCreateItemAct(item);
                    }
                }

                @Override
                public void onPutAwayClick(HMAux item) {
                    if(mFragItemListener != null) {
                        mFragItemListener.callPutAwayCreateItemAct(item);
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
            //Se foi passado serial, modifica cor da celula e posiciona no serial passado.
            if(!productCode.equals("-1") && !serialCode.equals("-1")){
                int serialPosition = mPresenter.getSerialCodePosition(itemList,productCode,serialCode);
                if(serialPosition > 0){
                    rvItems.scrollToPosition(serialPosition);
                }
            }
            //Se status diferente de done ou cancelled, seta evento de scroll, pois , caso contrario
            //não faz sentido.
            if(!isImmutableStatus()) {
                rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        //
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            mketFilter.setEnabled(true);
                            swActionFilter.setEnabled(true);
                        } else {
                            mketFilter.setEnabled(false);
                            swActionFilter.setEnabled(false);
                        }
                    }
                });
            }

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
        return mInbound.getAllow_new_item() == 1  && !isImmutableStatus();
    }

    /**
     * LUCHE - 16/07/2019
     *
     * Metodo que avalia se o status é DONE, CANCELED OU WAITING SYNC
     *
     * @return True se for um dos status "imutaveis"
     */
    private boolean isImmutableStatus() {
        return mInbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)
            || mInbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)
            || mInbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_CANCELLED);
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("serial_filter_hint");
        //
        return transListFrag;
    }
}
