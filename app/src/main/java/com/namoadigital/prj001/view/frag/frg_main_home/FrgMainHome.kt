package com.namoadigital.prj001.view.frag.frg_main_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.Act005MainMenuTagAdapter
import com.namoadigital.prj001.databinding.FrgMainHomeBinding
import com.namoadigital.prj001.model.MainTagMenu
import com.namoadigital.prj001.model.MdTag
import com.namoadigital.prj001.util.ConstantBaseApp.*
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.view.dialog.ActionByTagFilterDialog
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header.OnPipelineFragmentInteractionListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [FrgMainHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FrgMainHome : BaseFragment(), Frg_Main_Home_Contract.View, ActionByTagFilterDialog.OnApplyFilterListener {

    private var mListener: FrgMainHome.OnFrgMainHomeIteract? = null
    private var _binding: FrgMainHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: Act005MainMenuTagAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            hmAux_Trans = it.getSerializable(ARG_PARAM1) as HMAux?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FrgMainHomeBinding.inflate(inflater, container, false)
        //
        if (context is OnPipelineFragmentInteractionListener) {
            mListener = context as OnFrgMainHomeIteract?
        }
        //
        initializeLayoutVisibility()
        //
        setLabels()
        //
        setActions()
        //
        val view = binding.root
        return view
    }

    private fun initializeLayoutVisibility() {

        val tagList = mListener?.getTagList(
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_PERIOD_FILTER, PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION),
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_SITES_FILTER, PREFERENCE_HOME_ALL_SITE_OPTION),
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_FOCUS_FILTER, PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)
        )
        if (tagList != null) {
            mListener?.let {
                adapter = Act005MainMenuTagAdapter(tagList, hmAux_Trans, it)
                binding.rvTags.adapter = adapter
            }
        }
    }

    private fun setLabels() {
        _binding?.tvCalendar?.text = hmAux_Trans.get("calendar_lbl")
        _binding?.tvListPlaceholder?.text = hmAux_Trans.get("empty_list_lbl")
        _binding?.tvMessenger?.text = hmAux_Trans.get("messenger_lbl")
        _binding?.tvSearch?.text = hmAux_Trans.get("search_lbl")
        _binding?.tvListTagLbl?.text = hmAux_Trans.get("tag_list_lbl")
    }

    private fun setActions() {
        _binding?.llCalendar?.setOnClickListener {
            mListener?.let {
                it.onSelectHeaderCalendar()
            }
        }
        //
        _binding?.llSearch?.setOnClickListener {
            mListener?.let {
                it.onSelectHeadeSearch()
            }
        }
        //
        _binding?.llMessenger?.setOnClickListener {
            mListener?.let {
                it.onSelectHeaderMessenger()
            }
        }
        //
        _binding?.fabSearchBySerial?.setOnClickListener {
            mListener?.let {
                it.onSelectFABAssetLocal()
            }
        }
        //
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment Frg_Main_Home.
         */
        //
        @JvmStatic
        fun newInstance(tagList: List<MdTag>, hmAux_Trans: HMAux) =
                FrgMainHome().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, hmAux_Trans)
                    }
                }
    }

    interface OnFrgMainHomeIteract {
        fun onSelectMenuTagItem(item: MainTagMenu)

        //
        fun onSelectFABAssetLocal()

        //
        fun onSelectHeaderCalendar()

        //
        fun onSelectHeadeSearch()

        //
        fun onSelectHeaderMessenger()

        //
        fun getTagList(periodFilter: String, sitesFilter: String, focusFilter: String): MutableList<MainTagMenu>
    }

    override fun onApply(periodFilter: String, sitesFilter: String, focusFilter: String) {
        mListener?.let {
            val tagList = it.getTagList(periodFilter, sitesFilter, focusFilter)
            adapter.mMainTagMenu.clear()
            adapter.mMainTagMenu.addAll(tagList)
            adapter.notifyDataSetChanged()
        }
    }

}