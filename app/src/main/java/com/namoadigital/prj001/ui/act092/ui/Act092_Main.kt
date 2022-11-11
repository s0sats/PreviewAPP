package com.namoadigital.prj001.ui.act092.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.MyActionsAdapter
import com.namoadigital.prj001.databinding.Act092MainBinding
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act006.Act006_Main
import com.namoadigital.prj001.ui.act016.Act016_Main
import com.namoadigital.prj001.ui.act068.Act068_Main
import com.namoadigital.prj001.ui.act092.Act092Presenter
import com.namoadigital.prj001.ui.act092.Act092_Contract
import com.namoadigital.prj001.ui.act092.repository.IActionSerialRepository
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.base.BaseActivityMvp
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Act092_Main : BaseActivityMvp<Act092_Contract.Presenter, Act092MainBinding>(),
    Act092_Contract.View {
    private lateinit var bundle: Bundle

    private val _mainUserFilter = MutableStateFlow(false)

    override val binding: Act092MainBinding by lazy {
        Act092MainBinding.inflate(layoutInflater)
    }

    private var myActionFilterParam = MutableStateFlow(MyActionFilterParam())

    override val presenter: Act092Presenter by lazy {
        Act092Presenter(
            myActionFilterParam.value,
            bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005),
            hmAux_Trans,
            ActionUseCases.Companion.ActionUseCasesFactory(context).instance(
                IActionSerialRepository.Companion.ActionSerialRepositoryFactory(context).instance()
            ),
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            setContentView(root)
            bundle = (savedInstanceState ?: intent.extras) as Bundle
            setSupportActionBar(topAppBar)
            mAct_Title = "act92_title"
            setTitleLanguage()
            getBundle()
        }

        initView {
            presenter.setView(this)
        }
    }

    fun getBundle() {
        val filterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM)
        myActionFilterParam.value =
            filterParam?.let { it as MyActionFilterParam } ?: MyActionFilterParam()
    }

    override fun callAct006(bundle: Bundle) {
        val mIntent = Intent(context, Act006_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct016(bundle: Bundle) {
        val mIntent = Intent(context, Act016_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct068() {
        val mIntent = Intent(context, Act068_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mIntent)
        finish()
    }

    override fun callAct005() {
        val mIntent = Intent(context, Act005_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(mIntent)
        finish()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        presenter.onBackPressedClicked()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onState(state: Act092UiEvent) {
        CoroutineScope(Dispatchers.Main).launch {
            with(binding) {
                when (state) {
                    is Act092UiEvent.IsLoading -> {
                        if (!state.isLoading) {
                            progressLoading.visibility = View.GONE
                            return@launch
                        }
                        progressLoading.visibility = View.VISIBLE
                    }

                    is Act092UiEvent.EmptyOrError -> {
                        if (!state.isError) {
                            emptyList.apply {
                                visibility = View.VISIBLE
                                text = /*hmAux_Trans["empty_list_lbl"]*/ "Nenhum serial encontrado."
                            }
                        }
                        recyclerSerialList.visibility = View.GONE
                        progressLoading.visibility = View.GONE
                    }

                    is Act092UiEvent.ListingSerialSteels -> {
                        if (state.list.isEmpty()) {
                            onState(Act092UiEvent.EmptyOrError())
                            return@launch
                        }
                        emptyList.visibility = View.GONE
                        progressLoading.visibility = View.GONE
                        recyclerSerialList.apply {
                            visibility = View.VISIBLE
                            adapter = MyActionsAdapter(
                                state.list,
                                hmAux_Trans,
                                {}, {}, {}, {}
                            )
                            layoutManager =
                                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        }

                    }

                    is Act092UiEvent.ShowSnackbar -> {
                        showSnackbar(state.message)
                    }

                    is Act092UiEvent.FilterMainUser -> {
                        toggleMainUserFilter(_mainUserFilter.value)
                    }
                }
            }
        }
    }


    private fun toggleMainUserFilter(value: Boolean) {
        if (value) {
            binding.mainUserSelection.setBackgroundDrawable(resources.getDrawable(R.drawable.my_action_toogle_default))
            binding.mainUserSelection.setImageResource(R.drawable.ic_person_black_24dp)
        } else {
            binding.mainUserSelection.setImageResource(R.drawable.ic_person_white_24dp)
            binding.mainUserSelection.setBackgroundDrawable(resources.getDrawable(R.drawable.my_action_toogle_pressed))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.act092_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info -> {
                showSnackbar("Em manutenção")
            }
        }
        return true
    }

    override fun initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT092
        )
    }

    override fun initTrans() {
    }

    override fun initVars() {

        binding.serialTitle.text = myActionFilterParam.value.serialId

    }

    override fun initAction() {

        binding.mainUserSelection.setOnClickListener {
            _mainUserFilter.value = !_mainUserFilter.value
            onState(Act092UiEvent.FilterMainUser)
        }

    }

    private fun initRecyclerView() {
        with(binding) {

            recyclerSerialList.apply {

            }

        }
    }

}