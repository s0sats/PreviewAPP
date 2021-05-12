package com.namoadigital.prj001.view.frag.frg_main_home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.Act005MainMenuTagAdapter
import com.namoadigital.prj001.databinding.FrgMainHomeBinding
import com.namoadigital.prj001.model.MainTagMenu
import com.namoadigital.prj001.util.ConstantBaseApp.*
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.dialog.ActionByTagFilterDialog


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM_MODULE_CODE = "ARG_PARAM_MODULE_CODE"

/**
 * A simple [Fragment] subclass.
 * Use the [FrgMainHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FrgMainHome : BaseFragment(), Frg_Main_Home_Contract.View, ActionByTagFilterDialog.OnApplyFilterListener {

    private var mListener: FrgMainHome.OnFrgMainHomeIteract? = null
    private var mModule_Code: String? = null

    private val hmAux_Trans_Frag: HMAux by lazy {
        var transListFrag = ArrayList<String>()
        //
        transListFrag.add("calendar_lbl")
        transListFrag.add("empty_list_lbl")
        transListFrag.add("messenger_lbl")
        transListFrag.add("search_lbl")
        transListFrag.add("tag_list_lbl")
        transListFrag.add("tag_item_qty")
        transListFrag.add("tag_item_form_in_execution")
        //
        val mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                APP_MODULE,
                FRG_MAIN_HOME
        )
        //
        ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code_Frag,
                ToolBox_Con.getPreference_Translate_Code(context),  //transListFrag
                transListFrag
        )
    }

    private var _binding: FrgMainHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: Act005MainMenuTagAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mModule_Code = it.getSerializable(ARG_PARAM_MODULE_CODE) as String?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FrgMainHomeBinding.inflate(inflater, container, false)
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
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_PERIOD_FILTER, PREFERENCE_HOME_ALL_TIME_OPTION),
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_SITES_FILTER, PREFERENCE_HOME_ALL_SITE_OPTION),
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_FOCUS_FILTER, PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)
        )
        if (tagList != null && !tagList.isEmpty()) {
            binding.rvTags.visibility = View.VISIBLE
            binding.tvListPlaceholder.visibility = View.GONE
            if(mListener != null){
                adapter = Act005MainMenuTagAdapter(tagList, hmAux_Trans_Frag, mListener)
            }
        }else{
            adapter = Act005MainMenuTagAdapter(mutableListOf(), hmAux_Trans_Frag, mListener)
            binding.rvTags.visibility = View.GONE
            binding.tvListPlaceholder.visibility = View.VISIBLE
        }
        binding.rvTags.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        binding.rvTags.layoutManager = layoutManager
        binding.rvTags.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
    }

    private fun setLabels() {
        binding.tvCalendar.text = hmAux_Trans_Frag.get("calendar_lbl")
        binding.tvListPlaceholder.text = hmAux_Trans_Frag.get("empty_list_lbl")
        binding.tvMessenger.text = hmAux_Trans_Frag.get("messenger_lbl")
        binding.tvSearch.text = hmAux_Trans_Frag.get("search_lbl")
        binding.tvListTagLbl.text = hmAux_Trans_Frag.get("tag_list_lbl")
    }

    private fun setActions() {
        binding.llCalendar.setOnClickListener {
            mListener?.let {
                it.onSelectHeaderCalendar()
            }
        }
        //
        binding.llSearch.setOnClickListener {
            mListener?.let {
                it.onSelectHeadeSearch()
            }
        }
        //
        binding.llMessenger.setOnClickListener {
            mListener?.let {
                it.onSelectHeaderMessenger()
            }
        }
        //
        binding.fabSearchBySerial.setOnClickListener {
            mListener?.let {
                it.onSelectFABAssetLocal()
            }
        }
        //
        binding.ivFilter.setOnClickListener {
            val actionByTagFilterDialog = ActionByTagFilterDialog(this.context!!, this)
            actionByTagFilterDialog.show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFrgMainHomeIteract) {
            mListener = context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener")
        }
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
        fun newInstance(mModule_Code: String?) =
                FrgMainHome().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM_MODULE_CODE, mModule_Code)
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

    override fun onApply(periodFilter: String, siteFilter: String, focusFilter: String) {
        mListener?.let {
            val tagList = it.getTagList(periodFilter, siteFilter, focusFilter)
            if(adapter.mMainTagMenu != null && !adapter.mMainTagMenu.isEmpty()) {
                adapter.mMainTagMenu.clear()
            }
            adapter.mMainTagMenu.addAll(tagList)
            adapter.notifyDataSetChanged()
            //
            if (tagList == null || tagList.isEmpty()){
                binding.tvListPlaceholder.visibility = View.VISIBLE
            }else{
                binding.tvListPlaceholder.visibility = View.GONE
            }
            //
        }
    }

}