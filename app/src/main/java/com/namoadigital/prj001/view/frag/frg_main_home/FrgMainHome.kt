package com.namoadigital.prj001.view.frag.frg_main_home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var hmAux_Trans_Frag: HMAux

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
        loadTranslation()
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

    private fun loadTranslation() {

            var transListFrag = ArrayList<String>()
            //
            transListFrag.add("sys_main_menu_calendar_lbl")
            transListFrag.add("empty_list_lbl")
            transListFrag.add("messenger_lbl")
            transListFrag.add("sys_main_menu_search_lbl")
            transListFrag.add("tag_list_lbl")
            transListFrag.add("tag_item_qty")
            transListFrag.add("tag_item_form_in_execution")
            transListFrag.add("all_tag_list_item")
            //
            val mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                    context,
                    APP_MODULE,
                    FRG_MAIN_HOME
            )
            //
        hmAux_Trans_Frag = ToolBox_Inf.setLanguage(
                    context,
                    mModule_Code,
                    mResource_Code_Frag,
                    ToolBox_Con.getPreference_Translate_Code(context),  //transListFrag
                    transListFrag
            )

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
            handleAllTagItem(tagList)
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
        //
        mListener?.let { refreshChatBadge(it.getChatBadgeQty()) }
    }
    /**
     *  BARRIONUEVO 23-05-2021
     *  Metodo que verifica o aviso de horario errado.
     */
    fun setDatetimeVisibility() {
        if (!ToolBox_Inf.isLocalDatetimeOk(context)) {
            binding.cvInvalidDatetimeCard.setVisibility(View.VISIBLE)
        } else {
            binding.cvInvalidDatetimeCard.setVisibility(View.GONE)
        }
    }

    private fun setLabels() {
        binding.tvCalendar.text = hmAux_Trans_Frag.get("sys_main_menu_calendar_lbl")
        binding.tvListPlaceholder.text = hmAux_Trans_Frag.get("empty_list_lbl")
        binding.tvMessenger.text = hmAux_Trans_Frag.get("messenger_lbl")
        binding.tvSearch.text = hmAux_Trans_Frag.get("sys_main_menu_search_lbl")
        binding.tvListTagLbl.text = hmAux_Trans_Frag.get("tag_list_lbl")

        binding.tvDatetimeWarning.text = mListener?.getDatetimeWarning()
    }

    private fun setActions() {
        binding.llCalendar.setOnClickListener {
            mListener?.let {
                it.onSelectCalendar()
            }
        }
        //
        binding.llSearch.setOnClickListener {
            mListener?.let {
                it.onSelectSearch()
            }
        }
        //
        binding.llMessenger.setOnClickListener {
            mListener?.let {
                it.onSelectMessenger()
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

    override fun onResume() {
        super.onResume()
        setDatetimeVisibility()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
        fun onSelectCalendar()

        //
        fun onSelectSearch()

        //
        fun onSelectMessenger()

        //
        fun getTagList(periodFilter: String, sitesFilter: String, focusFilter: String): MutableList<MainTagMenu>
        fun getDatetimeWarning(): String
        fun getChatBadgeQty(): Int
    }

    override fun onApply(periodFilter: String, siteFilter: String, focusFilter: String) {
        mListener?.let {
            if(adapter != null) {
                refreshList(it.getTagList(periodFilter, siteFilter, focusFilter))
            }
        }
    }

    fun refreshList(tagList: MutableList<MainTagMenu>) {
        //
        if (tagList == null || tagList.isEmpty()) {
            //DEvido placeholder de lista vazia, se faz necessario reloadar a tradução.
            loadTranslation()
            binding.tvListPlaceholder.visibility = View.VISIBLE
            binding.tvListPlaceholder.text = hmAux_Trans_Frag["empty_list_lbl"]
            binding.rvTags.visibility = View.GONE
            adapter.mMainTagMenu.clear()
        } else {
            binding.rvTags.visibility = View.VISIBLE
            binding.tvListPlaceholder.visibility = View.GONE
            handleAllTagItem(tagList)
            if (adapter.mMainTagMenu != null) {
                adapter.mMainTagMenu.clear()
                adapter.mMainTagMenu.addAll(tagList)
            }
        }
        //
        adapter.notifyDataSetChanged()
    }

    private fun handleAllTagItem(tagList: MutableList<MainTagMenu>) {
        if (tagList.size > 1) {
            val lastItem = tagList.size - 1
            tagList.get(lastItem).tagName = hmAux_Trans_Frag.get("all_tag_list_item")!!;
        }
    }

    fun refreshChatBadge(chatBadgeQty: Int) {
        binding.tvMessengerBadge.visibility = View.GONE
        if(chatBadgeQty >0) {
            binding.tvMessengerBadge.text = chatBadgeQty.toString()
            binding.tvMessengerBadge.visibility = View.VISIBLE
        }
    }

}