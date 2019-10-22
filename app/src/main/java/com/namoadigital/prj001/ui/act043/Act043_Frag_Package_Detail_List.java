package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Package_Detail_Frag_Item_Adapter;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.view.dialog.ServiceRegisterDialog;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Act043_Frag_Package_Detail_List extends BaseFragment {

    private static final String HMAUX_TRANS = "hmaux_trans";
    private static final String PACKAGE_SERVICE = "PACKAGE_SERVICE";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private HMAux hmAux_Trans = new HMAux();
    private TSO_Service_Search_Obj packageDataset = new TSO_Service_Search_Obj();
    private Context context;
    private Button btn_cancel;
    private Button btn_save;
    private RecyclerView recyclerView ;
    private ImageView ivPackIcon;
    private ImageView ivRemoveIcon;
    private TextView tvPackDesc;
    private boolean bStatus = false;
    private Act043_Package_Detail_Frag_Item_Adapter mAdapter;
    private Act043_I_Add_Service_Interaction delegateAddService;
    private View.OnClickListener removeListener;
    private View.OnClickListener cancelListener;
    private View.OnClickListener saveListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Act043_Frag_Package_Detail_List() {
    }

    public static Act043_Frag_Package_Detail_List newInstance(int columnCount, HMAux hmAux_Trans) {
        Act043_Frag_Package_Detail_List fragment = new Act043_Frag_Package_Detail_List();
        Bundle args = new Bundle();
        args.putSerializable(HMAUX_TRANS, hmAux_Trans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String,String>) getArguments().getSerializable(HMAUX_TRANS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bStatus = true;
        //context = view.getContext();
        context = getActivity();
        //
        View view = inflater.inflate(R.layout.act043_frag_package_detail_list, container, false);
        //
        bindViews(view);
        initListeners();
        initAction();
        //
        return view;
    }

    private void bindViews(View view) {
        ivPackIcon = view.findViewById(R.id.act043_frag_package_detail_list_iv_pack_icon);
        tvPackDesc = view.findViewById(R.id.act043_frag_package_detail_list_tv_pack_desc);
        ivRemoveIcon = view.findViewById(R.id.act043_frag_package_detail_list_iv_remove_icon);
        recyclerView = view.findViewById(R.id.act043_frag_package_detail_list_rv_pack_service_detail);
        btn_save = view.findViewById(R.id.act043_frag_package_detail_list_btn_save);
        btn_cancel = view.findViewById(R.id.act043_frag_package_detail_list_btn_cancel);
    }

    private void initListeners() {
        //Apenas volta para frag anterior
        cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forceBackPress();
            }
        };
        //Descarta alterações resetando o obj para estado original
        removeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.alertPackDetailRemoveConfirm(packageDataset);
            }
        };
        //Valida lista e seta como selecionado.
        saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ToolBox.alertMSG_YES_NO(
                        context,
                        hmAux_Trans.get("alert_add_pack_ttl"),
                        hmAux_Trans.get("alert_add_pack_confirm"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(addPackValidate()) {
                                    packageDataset.setSelected(true);
                                    packageDataset.setDetailed(true);
                                    forceBackPress();
                                }else{
                                    ToolBox.alertMSG(
                                        context,
                                        hmAux_Trans.get("alert_invalid_pack_ttl"),
                                        hmAux_Trans.get("alert_invalid_pack_msg"),
                                        null,
                                        0
                                    );
                                }
                            }
                        },
                        1
                    );

            }
        };
    }

    /**
     * Valida se serviços estão com os valores preenchidos.
     * @return
     */

    private boolean addPackValidate() {

        for (TSO_Service_Search_Detail_Obj serviceObj : packageDataset.getService_list()) {
            //Valida se site e zona preenchidos
            if(serviceObj.getSite_zone() != null
               && serviceObj.getSite_zone().size() > 0
               && ( serviceObj.getSite_code_selected() == null
                  || serviceObj.getZone_code_selected() == null)
            ){
              return false;
            }
            //
            if(serviceObj.getQty() <= 0){
                return false;
            }
            //
            if(serviceObj.getPrice() == null || serviceObj.getPrice() < 0){
                return false;
            }
        }
        //
        return true;
    }

    private void initAction() {
        ivRemoveIcon.setOnClickListener(removeListener);
        //
        btn_cancel.setOnClickListener(packageDataset == null || packageDataset.isSelected() ? cancelListener : removeListener);
        //
        btn_save.setOnClickListener(saveListener);
    }

    private void setRecyclerView() {
        mAdapter =
            new Act043_Package_Detail_Frag_Item_Adapter(
                packageDataset.getService_list(),
                hmAux_Trans
            );
        //
        mAdapter.setmListener(new Act043_Package_Detail_Frag_Item_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(TSO_Service_Search_Detail_Obj item, int adapterPosition) {
                showService_Pack_Details(item);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void showService_Pack_Details(final TSO_Service_Search_Detail_Obj item) {

        ArrayList<HMAux> siteOption = new ArrayList<>();
        ArrayList<HMAux> siteZoneOption = new ArrayList<>();

        if(item.getSite_zone() != null && !item.getSite_zone().isEmpty() ){
            siteOption = delegateAddService.generateSiteOption(item.getSite_zone());
            siteZoneOption = delegateAddService.generateSiteZoneOption(item.getSite_zone());
        }

        final ServiceRegisterDialog dialog =
                new ServiceRegisterDialog(
                        context,
                        ServiceRegisterDialog.ALERT_DIALOG_TYPE_PACKAGE_SERVICE,
                        hmAux_Trans,
                        packageDataset.getPack_service_desc_full(),
                        item,
                        siteOption,
                        siteZoneOption,
                        delegateAddService.getPartnerList()
                );
        //
        final int finalDialogType = ServiceRegisterDialog.ALERT_DIALOG_TYPE_PACKAGE_SERVICE;
        final ArrayList<HMAux> finalSiteOption = siteOption;
        dialog.setBtnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (finalDialogType ){
                    case ServiceRegisterDialog.ALERT_DIALOG_TYPE_PACKAGE_SERVICE:

                        if (dialog.getMk_qtd_val() != null && !dialog.getMk_qtd_val().isEmpty() && Double.valueOf(dialog.getMk_qtd_val()) > 0
                                && dialog.getMk_price_val() != null && !dialog.getMk_price_val().isEmpty()  && Double.valueOf(dialog.getMk_price_val()) >= 0
                                && ((dialog.get_ss_site_content().hasConsistentValue(SearchableSpinner.CODE)
                                && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                                && ((dialog.get_ss_zone_content().hasConsistentValue(SearchableSpinner.CODE)
                                && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                        ) {
                            item.setQty(Integer.valueOf(dialog.getMk_qtd_val()));
                            if(dialog.get_ss_zone_content().hasConsistentValue(SearchableSpinner.CODE)) {
                                item.setZone_code_selected(Integer.valueOf(dialog.get_ss_zone_content().get(SearchableSpinner.CODE)));
                                item.setZone_desc_selected(dialog.get_ss_zone_content().get(SearchableSpinner.DESCRIPTION));
                            }

                            if(dialog.get_ss_site_content().hasConsistentValue(SearchableSpinner.CODE)) {
                                item.setSite_code_selected(Integer.valueOf(dialog.get_ss_site_content().get(SearchableSpinner.CODE)));
                                item.setSite_desc_selected(dialog.get_ss_site_content().get(SearchableSpinner.DESCRIPTION));
                            }

                            if (dialog.get_ss_partner_content().hasConsistentValue(SearchableSpinner.CODE)) {
                                item.setPartner_code_selected(Integer.valueOf(dialog.get_ss_partner_content().get(SearchableSpinner.CODE)));
                                item.setPartner_desc_selected(dialog.get_ss_partner_content().get(SearchableSpinner.DESCRIPTION));
                            } else {
                                item.setPartner_code_selected(null);
                                item.setPartner_desc_selected(null);
                            }
                            item.setComment(dialog.getMk_comments_val());
                            item.setSelected(true);
                            item.setPrice(Double.valueOf(dialog.getMk_price_val()));
                            delegateAddService.calculateTotalPrice(packageDataset);
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_invalid_service_value_ttl"),
                                    hmAux_Trans.get("alert_invalid_service_value_msg"),
                                    null,
                                    0
                            );
                        }

                        break;
                }
            }
        });
        dialog.setBtnCancelListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                + " must implement OnListFragmentInteractionListener");
        }
        //
        if(context instanceof Act043_I_Add_Service_Interaction) {
            delegateAddService = (Act043_I_Add_Service_Interaction) context;
        }else{
            throw new RuntimeException(context.toString()
                + " must implement Act043_I_Add_Service_Interaction");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (packageDataset != null) {
                tvPackDesc.setText(packageDataset.getPack_service_desc_full());
                btn_save.setText(hmAux_Trans.get("btn_save_package_detail"));
                setLayoutCnfig();
                setRecyclerView();
            }
        }

    }

    /**
     * Seta configurações de itens que tem comportamento afetado pela flag is selected
     */
    private void setLayoutCnfig() {
        //Muda label do btn de acordo com a função
        btn_cancel.setText(packageDataset.isSelected() ? hmAux_Trans.get("btn_back") : hmAux_Trans.get("btn_cancel_package_detail") );
        //Exibe botão de remover apenas se pacote ja adicionado
        ivRemoveIcon.setVisibility(packageDataset.isSelected() ? View.VISIBLE : View.GONE );
        //Exibe botão de "save/adicionar" apenas se pacote ainda não adicionado.
        btn_save.setVisibility(packageDataset.isSelected() ? View.GONE : View.VISIBLE );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bStatus = false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void alertPackDetailRemoveConfirm(TSO_Service_Search_Obj packageDetailObj);
    }

    public void setPackageDataset(TSO_Service_Search_Obj packageDataset) {
        Bundle arguments = getArguments();
        arguments.putSerializable(PACKAGE_SERVICE, packageDataset);
        this.setArguments(arguments);
        this.packageDataset = packageDataset;
    }

    public void setDelegateAddService(Act043_I_Add_Service_Interaction delegateAddService) {
        this.delegateAddService = delegateAddService;
    }

    public TSO_Service_Search_Obj getPackObj(){
        return packageDataset;
    }

    void forceBackPress(){
        try {
            getActivity().onBackPressed();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
