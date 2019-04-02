package com.namoadigital.prj001.ui.act058;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag_Move_Create.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag_Move_Create#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_Move_Create extends BaseFragment {

    private static final String VIEW_PARAM = "view_param";
    public static final String ORIGIN_PARAM = "ORIGIN";
    public static final String HMAUX_TRANS = "hmaux_trans";

    private int view_param;
    private boolean fromMove;
    private IO_Move ioMove;
    private MD_Product_Serial mdProductSerial;

    private View serialLayout;

    private TextView tv_zone_position;

    private TextView tv_inbound_lbl;
    private TextView tv_move_order_lbl;
    private TextView tv_move_to_lbl;
    private TextView tv_inbound_val;
    private TextView tv_move_order_val;
    private TextView tv_move_to_val;

    private MKEditTextNM mkedit_zone;
    private MKEditTextNM mkedit_position;
    private MKEditTextNM mkedit_coments;

    private MkDateTime mkdate_confirm;

    private SearchableSpinner ss_reason;

    private CheckBox chk_change_zone;

    private OnFragmentInteractionListener mListener;
    private TextView tv_product_cod_val;
    private TextView tv_serial_val;

    public Frag_Move_Create() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Informacao da Movimentacao.
     * @param serialInfo
     * @param viewParam Controle de visualizacao de elementos, 0 = oculta Comentario e data
     *                                                         1 = oculta Reason.
     * @param originParam Controla visualizacao do checkbox para mudanca de zona.
     * @param hmAux_Trans_Frag
     * @return A new instance of fragment Frag_Move_Create.
     */
    public static Frag_Move_Create newInstance(IO_Move param1, MD_Product_Serial serialInfo, int viewParam, boolean originParam, HMAux hmAux_Trans_Frag) {
        Frag_Move_Create fragment = new Frag_Move_Create();
        Bundle args = new Bundle();
        args.putSerializable(IO_MoveDao.TABLE, param1);
        args.putSerializable(MD_Product_SerialDao.TABLE, serialInfo);
        args.putSerializable(HMAUX_TRANS, hmAux_Trans_Frag);
        args.putInt(VIEW_PARAM, viewParam);
        args.putBoolean(ORIGIN_PARAM, originParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromMove = getArguments().getBoolean(ORIGIN_PARAM);
            view_param = getArguments().getInt(VIEW_PARAM);
            ioMove = (IO_Move) getArguments().getSerializable(IO_MoveDao.TABLE);
            mdProductSerial = (MD_Product_Serial) getArguments().getSerializable(MD_Product_SerialDao.TABLE);
            this.hmAux_Trans= (HMAux) getArguments().getSerializable(HMAUX_TRANS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.act058_frag_move, container, false);

        serialLayout = fragView.findViewById(R.id.act058_serial_layout);

        tv_product_cod_val = serialLayout.findViewById(R.id.tv_product_cod_desc);
        tv_serial_val = serialLayout.findViewById(R.id.tv_serial_val);

        tv_zone_position = fragView.findViewById(R.id.act058_tv_zone_position);
        tv_inbound_val = fragView.findViewById(R.id.act058_tv_inbound_val);
        tv_move_order_val = fragView.findViewById(R.id.act058_tv_move_order_val);
        tv_move_to_val = fragView.findViewById(R.id.act058_tv_move_to_val);
        tv_inbound_lbl = fragView.findViewById(R.id.act058_tv_inbound_lbl);
        tv_move_order_lbl = fragView.findViewById(R.id.act058_tv_move_order_lbl);
        tv_move_to_lbl = fragView.findViewById(R.id.act058_tv_move_to_lbl);

        mkedit_zone = fragView.findViewById(R.id.act058_mkedit_zone);
        mkedit_position = fragView.findViewById(R.id.act058_mkedit_position);
        mkedit_coments = fragView.findViewById(R.id.act058_mkedit_coments);

        mkdate_confirm = fragView.findViewById(R.id.act058_mkdate_confirm);

        ss_reason = fragView.findViewById(R.id.act058_ss_reason);
        chk_change_zone = fragView.findViewById(R.id.act058_chk_change_zone);

        return fragView;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindLabelsAndHints();
        bindValues();
    }

    private void bindValues() {
        tv_product_cod_val.setText(mdProductSerial.getProduct_id() + " " + mdProductSerial.getProduct_desc());
        tv_serial_val.setText(mdProductSerial.getSerial_id());
    }

    private void bindLabelsAndHints() {

        tv_inbound_lbl.setText(hmAux_Trans.get("inbound_lbl"));
        tv_move_order_lbl.setText(hmAux_Trans.get("move_order_lbl"));
        tv_move_to_lbl.setText(hmAux_Trans.get("move_to_lbl"));

        mkedit_zone.setHint(hmAux_Trans.get("zone_hint"));
        mkedit_position.setHint(hmAux_Trans.get("position_hint"));
        mkedit_coments.setHint(hmAux_Trans.get("comments_hint"));

        mkdate_confirm.setmLabel(hmAux_Trans.get("confirm_date_lbl"));

        ss_reason.setmHint(hmAux_Trans.get("reason_hint"));
        chk_change_zone.setText(hmAux_Trans.get("change_to_zone_target_lbl"));
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transList = new ArrayList<String>();
        transList.add("serial_lbl");
        transList.add("inbound_lbl");
        transList.add("move_order_lbl");
        transList.add("move_to_lbl");
        transList.add("confirm_date_lbl");
        transList.add("change_to_zone_target_lbl");
        transList.add("zone_hint");
        transList.add("position_hint");
        transList.add("reason_hint");
        transList.add("tracking_hint");
        transList.add("class_hint");
        transList.add("comments_hint");
        transList.add("btn_save");

        return transList;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void showAlert(String title, String msg);

        void onBackPressed();
    }
}
