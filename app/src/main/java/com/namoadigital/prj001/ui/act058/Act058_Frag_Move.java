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
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Move;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Act058_Frag_Move.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Act058_Frag_Move#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Act058_Frag_Move extends BaseFragment {

    private static final String VIEW_PARAM = "view_param";
    private static final String IO_MOVE = "io_move";
    public static final String ORIGIN_PARAM = "ORIGIN";

    private int view_param;
    private boolean fromMove;
    private IO_Move ioMove;

    TextView tv_zone_position;
    TextView tv_inbound_val;
    TextView tv_move_order_val;
    TextView tv_move_to_val;

    MKEditTextNM mkedit_zone;
    MKEditTextNM mkedit_position;
    MKEditTextNM mkedit_coments;

    MkDateTime mkdate_confirm;

    SearchableSpinner ss_reason;

    CheckBox chk_change_zone;

    private OnFragmentInteractionListener mListener;

    public Act058_Frag_Move() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Informacao da Movimentacao.
     * @param viewParam Controle de visualizacao de elementos, 0 = oculta Comentario e data
     *                                                         1 = oculta Reason.
     * @param originParam Controla visualizacao do checkbox para mudanca de zona.
     * @return A new instance of fragment Act058_Frag_Move.
     */
    public static Act058_Frag_Move newInstance(IO_Move param1, int viewParam, boolean originParam) {
        Act058_Frag_Move fragment = new Act058_Frag_Move();
        Bundle args = new Bundle();
        args.putSerializable(IO_MOVE, param1);
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
            ioMove = (IO_Move) getArguments().getSerializable(IO_MOVE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.act058_frag_move, container, false);

        tv_zone_position = fragView.findViewById(R.id.act058_tv_zone_position);
        tv_inbound_val = fragView.findViewById(R.id.act058_tv_inbound_val);
        tv_move_order_val = fragView.findViewById(R.id.act058_tv_move_order_val);
        tv_move_to_val = fragView.findViewById(R.id.act058_tv_move_to_val);

        mkedit_zone = fragView.findViewById(R.id.act058_mkedit_zone);
        mkedit_position = fragView.findViewById(R.id.act058_mkedit_position);
        mkedit_coments = fragView.findViewById(R.id.act058_mkedit_coments);

        mkdate_confirm = fragView.findViewById(R.id.act058_mkdate_confirm);

        ss_reason = fragView.findViewById(R.id.act058_ss_reason);
        chk_change_zone = fragView.findViewById(R.id.act058_chk_change_zone);

        return fragView;
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

    public List<String> getFragTranslationsVars() {
        return new ArrayList<>();
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
    }
}
