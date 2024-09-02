package com.namoadigital.prj001.view.frag.frg_main_home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.Act005MainMenuTagAdapter
import com.namoadigital.prj001.databinding.FrgMainHomeBinding
import com.namoadigital.prj001.extensions.getColorStateListId
import com.namoadigital.prj001.extensions.sendCommandToServiceTripLocation
import com.namoadigital.prj001.model.MainTagMenu
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.service.location.util.LocationServiceConstants
import com.namoadigital.prj001.ui.act005.OnResfreshUI
import com.namoadigital.prj001.ui.act005.trip.fragment.base.OnFrgMainHomeInteract
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
class FrgMainHome : BaseFragment(), Frg_Main_Home_Contract.View, ActionByTagFilterDialog.OnApplyFilterListener, OnResfreshUI {

    private var mListener: OnFrgMainHomeInteract? = null
    private var mModule_Code = APP_MODULE
    private lateinit var mPresenter: Frg_Main_Home_Presenter
    private lateinit var hmAux_Trans_Frag: HMAux

    private var _binding: FrgMainHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: Act005MainMenuTagAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FrgMainHomeBinding.inflate(inflater, container, false)
        //
        mPresenter = Frg_Main_Home_Presenter(
            requireContext(),
            this
        )
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
            transListFrag.add("sys_main_menu_home_lbl")
            transListFrag.add("sys_main_menu_trip_lbl")
            transListFrag.add("sys_main_menu_extract_lbl")
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
                ToolBox_Inf.getActionTimeDefaultOption(context),
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
        if(mListener != null){
            refreshChatBadge(mListener!!.getChatBadgeQty())
        }

    }
    /**
     *  BARRIONUEVO 23-05-2021
     *  Metodo que verifica o aviso de horario errado.
     */
    fun setDatetimeVisibility() {
        binding.llHeader.apply {
            if (!ToolBox_Inf.isLocalDatetimeOk(context)) {
                cvInvalidDatetimeCard.visibility = View.VISIBLE
            } else {
                cvInvalidDatetimeCard.visibility = View.GONE
            }
        }
    }

    private fun setLabels() {
        binding.tvListPlaceholder.text = hmAux_Trans_Frag["empty_list_lbl"]
        binding.tvListTagLbl.text = hmAux_Trans_Frag["tag_list_lbl"]

        binding.llHeader.apply {
            tvDatetimeWarning.text = mListener?.getDatetimeWarning()
            tvHome.text = hmAux_Trans_Frag["sys_main_menu_home_lbl"]
            ivHome.background = AppCompatResources.getDrawable(requireContext(), R.drawable.frg_header_menu_btn)
            ivHome.imageTintList = requireContext().getColorStateListId(R.color.m3_namoa_shadow)
            //
            if(mPresenter.hasFieldServiceEnable()) {
                llTrip.visibility = View.VISIBLE
                tvTrip.text = hmAux_Trans_Frag["sys_main_menu_trip_lbl"]
                ivTrip.background = null
                ivTrip.imageTintList = requireContext().getColorStateListId(R.color.m3_namoa_surface)
            }else{
                llTrip.visibility = View.GONE
            }
            //
            tvCalendar.text = hmAux_Trans_Frag["sys_main_menu_calendar_lbl"]
            tvMessenger.text = hmAux_Trans_Frag["messenger_lbl"]
            tvSearch.text = hmAux_Trans_Frag["sys_main_menu_search_lbl"]
        }
    }

    private fun setActions() {
        binding.llHeader.apply {
            llTrip.setOnClickListener{
                mListener?.onSelectTrip()
            }
            llCalendar.setOnClickListener {
                mListener?.onSelectCalendar()
            }
            //
            llSearch.setOnClickListener {
                mListener?.onSelectSearch()
            }
            //
            llMessenger.setOnClickListener {
                mListener?.onSelectMessenger()
            }
        }
        //
        binding.fabSearchBySerial.setOnClickListener {
            mListener?.onSelectFABAssetLocal()
        }
        //
        binding.ivFilter.setOnClickListener {
            val actionByTagFilterDialog = ActionByTagFilterDialog(requireContext(), this)
            actionByTagFilterDialog.show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (FsTripLocationService.isTracking.value) {
            context.sendCommandToServiceTripLocation(LocationServiceConstants.STOP_LOCATION)
        }
        if (context is OnFrgMainHomeInteract) {
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
        fun newInstance() =
                FrgMainHome().apply {

                }
    }

    override fun onApply(periodFilter: String, siteFilter: String, focusFilter: String) {
        mListener?.let {
            if(adapter != null) {
                refreshList(it.getTagList(periodFilter, siteFilter, focusFilter))
            }
        }

    }

    fun refreshList(tagList: MutableList<MainTagMenu>) {
        //DEvido placeholder de lista vazia, se faz necessario reloadar a tradução.
        loadTranslation()
        setLabels()
        //
        if (tagList == null || tagList.isEmpty()) {
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

    override fun refreshChatBadge(chatBadgeQty: Int) {
        binding.llHeader.tvMessengerBadge.visibility = View.GONE
        if(chatBadgeQty >0) {
            binding.llHeader.tvMessengerBadge.text = chatBadgeQty.toString()
            binding.llHeader.tvMessengerBadge.visibility = View.VISIBLE
        }
    }

}