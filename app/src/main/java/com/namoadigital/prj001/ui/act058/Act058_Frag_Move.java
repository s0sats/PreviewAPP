package com.namoadigital.prj001.ui.act058;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Move;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String VIEW_PARAM = "view_param";
    private static final String IO_MOVE = "io_move";
    public static final String ORIGIN_PARAM = "ORIGIN";

    // TODO: Rename and change types of parameters
    private String view_param;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Act058_Frag_Move() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Informacao da Movimentacao.
     * @param param2 Controle de visualizacao de elementos, 0 = oculta Comentario e data
     *                                                      1 = oculta Reason.
     * @param param3 Controla visualizacao do checkbox para mudanca de zona.
     * @return A new instance of fragment Act058_Frag_Move.
     */
    public static Act058_Frag_Move newInstance(IO_Move param1, int param2, boolean param3) {
        Act058_Frag_Move fragment = new Act058_Frag_Move();
        Bundle args = new Bundle();
        args.putSerializable(IO_MOVE, param1);
        args.putInt(VIEW_PARAM, param2);
        args.putBoolean(ORIGIN_PARAM, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            view_param = getArguments().getString(VIEW_PARAM);
            mParam2 = getArguments().getString(IO_MOVE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.act058_frag_move, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
