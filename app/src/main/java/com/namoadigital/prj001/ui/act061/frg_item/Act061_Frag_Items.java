package com.namoadigital.prj001.ui.act061.frg_item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act061_IO_Items_Adapter;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.ui.act061.frag_drawer.Act061_Frag_Drawer;
import com.namoadigital.prj001.util.ConstantBaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Act061_Frag_Items extends BaseFragment implements Act061_Frag_Items_Contract.I_View{

    private boolean bStatus = false;
    private Context context;
    private int inboundPrefix;
    private int inboundCode;
    private IO_Inbound mInbound;
    private Act061_Frag_Items_Presenter mPresenter;
    private onFragItemInteraction mFragItemListener;
    private boolean pausedByScan = false;
    //
    private MKEditTextNM mketFilter;
    private RecyclerView rvItems;
    private Act061_IO_Items_Adapter mAdapter;
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
        bindViews(view);
        //
        loadInbound();
        //
        mPresenter = new Act061_Frag_Items_Presenter(
            context,
            this,
            hmAux_Trans
        );
        //
    }

    private void bindViews(View view) {
        mketFilter = view.findViewById(R.id.act061_item_frag_mket_filter);
        rvItems = view.findViewById(R.id.act061_item_frag_rv_items);
        //
        mketFilter.setmBARCODE(true);
        mketFilter.setHint(hmAux_Trans.get("filter_hint"));
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
                    mAdapter.getFilter().filter(s);
                }
            }
        });
    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
        if(bStatus) {
            //
            if (mInbound != null) {
                loadAdapter(
                    mPresenter.getItemList(inboundPrefix, inboundCode)
                );
            }
        }
    }

    private void loadAdapter(ArrayList<HMAux> itemList) {
        if(itemList != null){
            mAdapter = new Act061_IO_Items_Adapter(
                context,
                R.layout.act061_frag_item_cell,
                itemList
            );
            //
            mAdapter.setOnIoItemClickListener(new Act061_IO_Items_Adapter.OnIoItemClickListener() {
                @Override
                public void onSerialClick(HMAux item) {
                    Toast.makeText(context,"Serial",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConfClick(HMAux item) {
                    Toast.makeText(context,"Conf",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPutAwayClick(HMAux item) {
                    Toast.makeText(context,"PutAway",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAddItemClick() {
                    Toast.makeText(context,"AddItem",Toast.LENGTH_SHORT).show();
                }
            });
            //
            rvItems.setLayoutManager(new LinearLayoutManager(context));
            rvItems.setAdapter(mAdapter);
        }
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("filter_hint");
        //
        return transListFrag;
    }
}
