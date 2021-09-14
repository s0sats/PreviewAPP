package com.namoadigital.prj001.ui.act011.frags

import android.content.Context
import android.os.Bundle
import android.view.View
import com.namoa_digital.namoa_library.ctls.CustomFF
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.databinding.Act011FrgFfBinding
import com.namoadigital.prj001.util.Constant
import java.util.*

class Act011FrgFF : Act011BaseFrg<Act011FrgFfBinding>(
) {
    lateinit var customFF: ArrayList<CustomFF>
    private var _mFrgListener: Act011FrgFFInteraction? = null
    private val mFrgListener get() = _mFrgListener!!

    /**
     * Fun static para construcao do obj
     */
    companion object {
        @JvmStatic fun newInstance(
            hmAuxTrans: HMAux,
            tabIndex: Int = 0,
            tabLastIndex: Int = 0,
            formStatus: String,
            scheduleDesc: String?,
            scheduleComments: String?
        ) = Act011FrgFF()
            .apply{
                arguments = Bundle().apply {
                    putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAuxTrans)
                    putString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS,formStatus)
                    putInt(GE_Custom_Form_Field_LocalDao.PAGE,tabIndex)
                    putInt(PARAM_LAST_INDEX,tabLastIndex)
                    putString(MD_Schedule_ExecDao.SCHEDULE_DESC,scheduleDesc)
                    putString(GE_Custom_Form_Field_LocalDao.COMMENT,scheduleComments)
                }
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            hmAuxTrans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            tabIndex = it.getInt(GE_Custom_Form_Field_LocalDao.PAGE)
            tabLastIndex = it.getInt(PARAM_LAST_INDEX)
            formStatus = it.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS,"")
            scheduleDesc = it.getString(MD_Schedule_ExecDao.SCHEDULE_DESC)
            scheduleComments = it.getString(GE_Custom_Form_Field_LocalDao.COMMENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let{
            customFF = mFrgListener.getCustomFF()
        }
        //
        loadControls()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Act011FrgFFInteraction){
            _mFrgListener = context
        }else{
            throw RuntimeException("${context.toString()} must implement FrgFFInteraction")
        }
    }

    override fun getViewBinding() = Act011FrgFfBinding.inflate(layoutInflater)
    override fun getHeaderInclude() = binding.incHeader
    override fun getNavegationInclude() = binding.incNavegation
    override fun getHistoricInclude() = binding.incHistoric

    /**
     * Seta os componentes dessa tab no fragmento, adicionando "indice" da pergunta no label.
     */
    private fun loadControls() {
        if(!customFF.isNullOrEmpty()){
            var count = 0
            val filter = customFF.filter {
                it.getmPage() == tabIndex
            }
            filter.forEach { ff ->
                if(ff.getmInclude() == 1){
                    count++
                    ff.setmLabel("$count. ${ff.getmLabel()}")
                    binding.llControls.addView(ff)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        _mFrgListener = null
    }
}