package com.namoadigital.prj001.view.frag.frg_ticket_search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;

import static com.namoadigital.prj001.util.ConstantBaseApp.MAIN_HMAUX_TRANS_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_Ticket_Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_Ticket_Search extends Fragment {

    public static final String CONTRACT_ID = "CONTRACT_ID";
    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String TICKET_ID = "TICKET_ID";
    public static final String SYNCS_QTY = "SYNCS_QTY";
    private Frg_Serial_Search.I_Frg_Serial_Search delegate;
    private Frg_Serial_Search.I_Frg_Serial_Search_Load load_delegate;

    private HMAux hmAux_Trans;
    private Frg_Ticket_Search_Presenter mPresenter;
    private Button btn_option_01;
    private Button btn_option_02;
    private Button btn_option_03;
    private Button btn_option_04;
    private Button btn_option_05;
    private LinearLayout ll_contract;
    private LinearLayout ll_client;
    private LinearLayout ll_ticket;
    private MKEditTextNM mket_contract;
    private MKEditTextNM mket_client;
    private MKEditTextNM mket_ticket;
    private int syncs_qty = 0;
    private int myTicketQty = 0;

    public void setOnSearchClickListener(Frg_Serial_Search.I_Frg_Serial_Search delegate) {
        this.delegate = delegate;
    }

    public void setLoad_delegate(Frg_Serial_Search.I_Frg_Serial_Search_Load load_delegate) {
        this.load_delegate = load_delegate;
    }

    public Frg_Ticket_Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment Frg_Ticket_Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Frg_Ticket_Search newInstance(HMAux hmAux_Trans) {
        Frg_Ticket_Search fragment = new Frg_Ticket_Search();
        Bundle args = new Bundle();

        args.putSerializable(MAIN_HMAUX_TRANS_KEY, hmAux_Trans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hmAux_Trans = (HMAux) getArguments().getSerializable(MAIN_HMAUX_TRANS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_ticket_search, container, false);

        mPresenter = new Frg_Ticket_Search_Presenter(getContext());

        iniVar(view);

        if(load_delegate != null){
            load_delegate.onFragIsReady();
        }

        iniAction();

        return view;
    }

    private void iniAction() {
        btn_option_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onSearchClick(Frg_Serial_Search.BTN_OPTION_01,getHMAuxValues());
            }
        });
        btn_option_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onSearchClick(Frg_Serial_Search.BTN_OPTION_02,null);
            }
        });
        btn_option_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onSearchClick(Frg_Serial_Search.BTN_OPTION_03,null);
            }
        });

        btn_option_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onSearchClick(Frg_Serial_Search.BTN_OPTION_05,null);
            }
        });
    }

    private void iniVar(View view) {
        //
        bindViews(view);
        //
        initButtonsVisibility();
        //
        setupButton();
        //
        apllyUserProfile();
        //
        mket_ticket.setHint(hmAux_Trans.get("ticket_hint"));

    }

    private void setupButton() {
        btn_option_01.setBackground(getActivity().getDrawable(R.drawable.namoa_cell_3_states));
        btn_option_01.setText(hmAux_Trans.get("btn_check_exists"));
        setupSyncButton();
        setupMyTicketsButton();
        btn_option_05.setBackground(getActivity().getDrawable(R.drawable.namoa_cell_2_states));
        btn_option_05.setText(hmAux_Trans.get("btn_scheduled_tickets"));
    }

    private void setupMyTicketsButton() {
        btn_option_03.setBackground(getActivity().getDrawable(R.drawable.namoa_cell_2_states));
        String btn_text = hmAux_Trans.get("btn_my_tickets")
            +  (myTicketQty > 0
                      ? " (" + myTicketQty + ")"
                      : ""
                );
        btn_option_03.setText(btn_text);
    }

    private void setupSyncButton() {
        btn_option_02.setBackground(getActivity().getDrawable(R.drawable.namoa_cell_2_states));
        String btn_text = hmAux_Trans.get("btn_sync_ticket");
        if(syncs_qty > 0) {
            btn_text += " (" + syncs_qty + ")";
            btn_option_02.setText(btn_text);
            btn_option_02.setVisibility(View.VISIBLE);
        }else{
            btn_option_02.setVisibility(View.GONE);
        }
    }

    private void apllyUserProfile() {
        if(mPresenter.getProfileForSearchContractId()) {
            ll_contract.setVisibility(View.VISIBLE);
            mket_contract.setHint(hmAux_Trans.get("contract_hint"));
        }else{
            ll_contract.setVisibility(View.GONE);
        }
        //
        if(mPresenter.getProfileForSearchClientId()) {
            ll_client.setVisibility(View.VISIBLE);
            mket_client.setHint(hmAux_Trans.get("client_hint"));
        }else{
            ll_client.setVisibility(View.GONE);
        }
    }

    private void initButtonsVisibility() {
        btn_option_01.setVisibility(View.VISIBLE);
        if(syncs_qty > 0) {
            btn_option_02.setVisibility(View.VISIBLE);
        }else{
            btn_option_02.setVisibility(View.GONE);
        }
        btn_option_03.setVisibility(View.VISIBLE);
        btn_option_04.setVisibility(View.GONE);
        btn_option_05.setVisibility(View.VISIBLE);
    }

    private void bindViews(View view) {
        btn_option_01 =  view.findViewById(R.id.frg_ticket_search_btn_option_01);
        btn_option_02 =  view.findViewById(R.id.frg_ticket_search_btn_option_02);
        btn_option_03 =  view.findViewById(R.id.frg_ticket_search_btn_option_03);
        btn_option_04 =  view.findViewById(R.id.frg_ticket_search_btn_option_04);
        btn_option_05 =  view.findViewById(R.id.frg_ticket_search_btn_option_05);
        ll_contract =  view.findViewById(R.id.frg_serial_search_ll_contract);
        mket_contract =  view.findViewById(R.id.frg_serial_search_mket_contract);
        ll_client =  view.findViewById(R.id.frg_ticket_search_ll_client);
        mket_client =  view.findViewById(R.id.frg_ticket_search_mket_client);
        ll_ticket =  view.findViewById(R.id.frg_ticket_search_ll_ticket);
        mket_ticket =  view.findViewById(R.id.frg_ticket_search_mket_ticket);
    }

    public HMAux getHMAuxValues() {
        HMAux values = new HMAux();
        values.put(CONTRACT_ID, ToolBox_Inf.removeAllLineBreaks(mket_contract.getText().toString().trim().isEmpty()  ? "" : mket_contract.getText().toString().trim()));
        values.put(CLIENT_ID, ToolBox_Inf.removeAllLineBreaks(mket_client.getText().toString().trim().isEmpty() ? "" : mket_client.getText().toString().trim()));
        values.put(TICKET_ID, ToolBox_Inf.removeAllLineBreaks(mket_ticket.getText().toString().trim().isEmpty() ? "" : mket_ticket.getText().toString().trim()));
        return values;
    }

    public void setSyncsQty(int sync_qty) {
        this.syncs_qty = sync_qty;
        /**
         *  BARRIONUEVO 27-10-2020
         *  Colocado para evitar o crash de NullPointer na inicializacao dos fragmento
         *  Sera necessário revisar como inicializar a qty para sincronizar
         */
        if(btn_option_02 != null) {
            setupSyncButton();
        }
    }

    public void setMyTicketsQty(int myTicketsQty) {
        this.myTicketQty = myTicketsQty;
        setupMyTicketsButton();
    }
}