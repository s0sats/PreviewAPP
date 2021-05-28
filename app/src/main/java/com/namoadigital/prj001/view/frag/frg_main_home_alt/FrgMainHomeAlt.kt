package com.namoadigital.prj001.view.frag.frg_main_home_alt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.adapter.Act005MainMenuModuleAdapter
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.FrgMainHomeAltBinding
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FrgMainHomeAlt.newInstance] factory method to
 * create an instance of this fragment.
 */
class FrgMainHomeAlt : Fragment(),  FrgMainHomeAltContract.View{
    // TODO: Rename and change types of parameters
    private var mListener: OnFrgMainHomeAltInteract? = null
    //
    private val mPresenter by lazy {
        FrgMainHomeAltPresenter(
                context,
                hmAux_Trans_Frag,
                TK_TicketDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                TkTicketCacheDao(
                        context!!,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                MD_Schedule_ExecDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                GE_Custom_Form_ApDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                IO_InboundDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                IO_OutboundDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                IO_MoveDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                IO_Blind_MoveDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        )
    }

    //
    private val hmAux_Trans_Frag: HMAux by lazy {
        val transListFrag = ArrayList<String>()
        //
        transListFrag.add("sys_main_menu_os_downloaded_lbl")
        transListFrag.add("sys_main_menu_os_downloaded_detail")
        transListFrag.add("sys_main_menu_os_next_lbl")
        transListFrag.add("sys_main_menu_os_next_detail")
        transListFrag.add("sys_main_menu_os_express_lbl")
        transListFrag.add("sys_main_menu_os_express_detail")
        transListFrag.add("sys_main_menu_os_by_vin_search_lbl")
        transListFrag.add("sys_main_menu_os_by_vin_search_detail")
        transListFrag.add("sys_main_menu_assets_lbl")
        transListFrag.add("sys_main_menu_assets_detail")
        transListFrag.add("sys_main_menu_tag_lbl")
        transListFrag.add("sys_main_menu_tag_detail")
        transListFrag.add("sys_main_menu_tag_by_serial_search_lbl")
        transListFrag.add("sys_main_menu_tag_by_serial_search_detail")
        //
        val mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                ConstantBaseApp.APP_MODULE,
                ConstantBaseApp.FRG_MAIN_HOME_ALT
        )
        //
        ToolBox_Inf.setLanguage(
                context,
                ConstantBaseApp.APP_MODULE,
                mResource_Code_Frag,
                ToolBox_Con.getPreference_Translate_Code(context),  //transListFrag
                transListFrag
        )
    }

    private var _binding: FrgMainHomeAltBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: Act005MainMenuModuleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FrgMainHomeAltBinding.inflate(inflater, container, false)
        //
        setList()
        //
        setActions()
        //
        val view = binding.root
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFrgMainHomeAltInteract) {
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

    private fun setActions() {
        binding.fabMessenger.setOnClickListener(){
            mListener?.onSelectMessenger()
        }
    }

    private fun setList() {
        val moduleList = mPresenter.getModules()

        binding.rvModules.visibility = View.VISIBLE
        if(mListener != null){
            adapter = Act005MainMenuModuleAdapter(moduleList, hmAux_Trans_Frag, mListener)
        }

        binding.rvModules.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        binding.rvModules.layoutManager = layoutManager
        //
        setDatetimeVisibility()
    }

    fun refreshModuleList() {
        setList()
    }

    fun setDatetimeVisibility() {
        if (!ToolBox_Inf.isLocalDatetimeOk(context)) {
            binding.cvInvalidDatetimeCard.setVisibility(View.VISIBLE)
        } else {
            binding.cvInvalidDatetimeCard.setVisibility(View.GONE)
        }
    }


    interface OnFrgMainHomeAltInteract {
        //
        fun onSelectAsset()
        //
        fun onSelectTags(tagName: String)
        //
        fun onSelectTagsBySerialSearch()
        //
        fun onSelectCalendar()
        //
        fun onSelectOS()
        //
        fun onSelectOSVinSearch()
        //
        fun onSelectOSExpress()
        //
        fun onSelectOSNext()
        //
        fun onSelectMessenger()
        //
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FrgMainHomeAlt.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                FrgMainHomeAlt().apply {

                }
    }
}