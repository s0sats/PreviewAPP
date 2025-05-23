package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act011InspectionQuestionFormCellBinding
import com.namoadigital.prj001.extensions.applyTintColor
import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.InspectionCell
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.EXEC_TYPE_ADJUST
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.EXEC_TYPE_ALERT
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.EXEC_TYPE_ALREADY_OK
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.EXEC_TYPE_FIXED
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.EXEC_TYPE_NOT_VERIFIED
import java.util.Objects
import kotlin.math.abs

class Act011InspectionFormAdapter(
    /*
        Barrionuevo 05/10;2021
        Objeto está aqui para facilitar a passagem de param do click listener.
     */
    private val acessoryFormView: AcessoryFormView,
    private val hmAuxTrans: HMAux,
    private val onItemSelected: (position: Int, itemPk: String, partitioned_execution: Int) -> Unit,
    private val onAlreadyOkItemSelected: (position: Int, item: InspectionCell) -> Unit,
    private val onAdapterFilterApplied: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var inspections: MutableList<InspectionCell>
    private val inspectionsFiltered: MutableList<InspectionCell> = mutableListOf()
    protected var textFilter: String = ""
    var mFilter: InspectionFormFilter? = null
    var filterApplied: Boolean = true
    var highlightedItemPosition = -1

    init {
        inspectionsFiltered.clear()
        inspections = acessoryFormView.inspections
        inspectionsFiltered.addAll(acessoryFormView.inspections.filter {
            it.isVisible || it.isDone
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyInspectionFormVH(
            Act011InspectionQuestionFormCellBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as MyInspectionFormVH) {
            val inspectionCell = inspectionsFiltered[position]
            onBinding(inspectionCell)
            //


            if (highlightedItemPosition >= 0
                && highlightedItemPosition == position
            ) {
                binding.clContainer.setBackgroundColor(binding.root.context.resources.getColor(R.color.namoa_myactions_blue_bg))
            } else {
                binding.clContainer.setBackgroundColor(binding.root.context.resources.getColor(R.color.namoa_color_gray_5))
            }
            //
            val openItemListener = View.OnClickListener {
                onItemSelected(
                    position,
                    inspectionCell.itemCodeAndSeq,
                    inspectionCell.partitionedExecution
                )
            }
            binding.root.setOnClickListener(openItemListener)
            holder.binding.tvInspectionVerificationAction.setOnClickListener(openItemListener)
            holder.binding.btnInspectionOngoingAction.setOnClickListener(openItemListener)
            //
            holder.binding.tvAutoAlreadyOk.setOnClickListener {
                onAlreadyOkItemSelected(
                    position,
                    inspectionCell
                )
            }
        }
    }

    fun applyNonForecastFilter(filterApplied: Boolean) {
        //LUCHE - Ao filtrar, highlightedItem deve ser resetado, já que não faz mais sentido exibir
        //vh em azul.
        clearHighlightedItemPosition()
        //
        this.filterApplied = filterApplied
        inspectionsFiltered.clear()
        if (this.filterApplied) {
            inspectionsFiltered.addAll(inspections)
        } else {
            inspectionsFiltered.addAll(inspections.filter {
                it.isVisible || it.isDone
            })
        }
        //
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return inspectionsFiltered.size
    }

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = InspectionFormFilter()
        }
        return mFilter as InspectionFormFilter
    }

    fun refreshItemList(position: Int, onAlreadyOkActionItem: InspectionCell) {
        //LUCHE - 04/11/2021 - Altera highlightedItemPosition para o item passado e notifica mudança
        //no anterior caso exista.
        val oldHighlight = highlightedItemPosition
        highlightedItemPosition = position
        if(oldHighlight > -1) {
            notifyItemChanged(oldHighlight)
        }
        inspectionsFiltered[position] = onAlreadyOkActionItem
        notifyItemChanged(position)
    }

    //TODO [VG_REFRESH] atribuir
    fun refreshList(acessoryFormView: AcessoryFormView) {
        this.inspections = acessoryFormView.inspections
    }

    /**
     * Reseta valor de HighlightedItem
     */
    private fun clearHighlightedItemPosition() {
        highlightedItemPosition = -1
    }

    inner class InspectionFormFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            //LUCHE - Ao filtrar, highlightedItem deve ser resetado, já que não faz mais sentido exibir
            //vh em azul.
            clearHighlightedItemPosition()
            //
            var temp = mutableListOf<InspectionCell>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            textFilter = charFilter
            if (charFilter.isNullOrEmpty()) {
                if (filterApplied) {
                    temp.addAll(inspections)
                } else {
                    temp.addAll(inspectionsFiltered)
                }
            } else {
                temp.addAll(
                    inspections.filter {
                        when (it) {
                            is InspectionCell -> {
                                val allFields =
                                    ToolBox.AccentMapper(it.getAllFieldForFilter().toLowerCase())
                                allFields.contains(charFilter)
                            }
                            //se for o botão, sempre exibe
                            else -> true
                        }
                    }
                )
            }
            val filterResults = FilterResults()
            filterResults.count = temp.size
            filterResults.values = temp
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.let {
                inspectionsFiltered.clear()
                //
                results.values?.let {
                    inspectionsFiltered.addAll(results.values as MutableList<InspectionCell>)
                }
                //LUCHE - 08/11/2021 - Dispara HoF que reporta qtd de itens filtrados.
                onAdapterFilterApplied(inspectionsFiltered.size)
                //
                notifyDataSetChanged()
            }
        }
    }

    inner class MyInspectionFormVH(val binding: Act011InspectionQuestionFormCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBinding(inspection: InspectionCell?) {
            inspection?.apply {

                val context = binding.root.context
                binding.btnInspectionOngoingAction.visibility = View.GONE
                if (isDone) {
                    binding.llAnswerInfo.visibility = View.VISIBLE
                    binding.btnInspectAnswered.visibility = View.VISIBLE
                    binding.btnInspectionOngoingAction.visibility = View.GONE
                    binding.tvInspectionVerificationAction.visibility = View.GONE
                    binding.tvAutoAlreadyOk.visibility = View.GONE
                    binding.ivPartitionExecution.visibility = View.GONE
                } else {
                    binding.llAnswerInfo.visibility = View.GONE
                    binding.btnInspectAnswered.visibility = View.GONE
                    binding.tvInspectionVerificationAction.visibility = View.VISIBLE
                    binding.tvAutoAlreadyOk.visibility = View.VISIBLE
                    //
                    if (inspection.status == InspectionCell.MANUAL_ALERT
                        || inspection.hideAlreadyOKBtn
                        || read_only
                    ) {
                        binding.tvAutoAlreadyOk.visibility = View.GONE
                    }
                    //
                    if (partitionedExecution == 1 && !read_only) {
                        binding.ivPartitionExecution.visibility = View.VISIBLE
                    } else {
                        binding.ivPartitionExecution.visibility = View.GONE
                    }
                }
                //
                binding.tvInspectionDescription.text = description
                //
                binding.tvStatus.apply {
                    if (isDone) {
                        text = hmAuxTrans["inspection_status_answered_item_lbl"]
                    } else {
                        text = statusTransalted
                    }
                    setTextColor(ContextCompat.getColor(context,tagColor))
                    invalidate()
                }
                binding.vCellColorTag.apply {
                    background.setColorFilter(
                        ContextCompat.getColor(context, tagColor),
                        android.graphics.PorterDuff.Mode.SRC_ATOP
                    )
                }
                //
                if (answerStatus != null) {
                    binding.btnInspectAnswered.text = execTypeTranslated
                    when (execType) {
                        EXEC_TYPE_FIXED -> {
                            binding.btnInspectAnswered.icon =
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_build_black_24dp
                                )

                        }

                        EXEC_TYPE_ADJUST -> {
                            binding.btnInspectAnswered.icon =
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_build_black_24dp
                                )
                        }
                        EXEC_TYPE_ALERT -> {
                            binding.btnInspectAnswered.icon =
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_outline_report_problem_24_black
                                )

                        }
                        EXEC_TYPE_ALREADY_OK -> {
                            binding.btnInspectAnswered.icon =
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_done_black_24dp
                                )

                        }

                        EXEC_TYPE_NOT_VERIFIED -> {
                            binding.btnInspectAnswered.icon =
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_baseline_redo_24_black
                                )
                        }
                    }

                    if (!isDone) {
                        binding.btnInspectionOngoingAction.text =
                            hmAuxTrans.get("inspection_ongoing_action_lbl")
                        binding.btnInspectionOngoingAction.visibility = View.VISIBLE
                        binding.tvInspectionVerificationAction.visibility = View.GONE
                        binding.tvAutoAlreadyOk.visibility = View.GONE
                        binding.llAnswerInfo.visibility = View.VISIBLE
                    }
                } else {
                    binding.tvInspectionVerificationAction.visibility = View.VISIBLE
                    binding.tvInspectionVerificationAction.text =
                        hmAuxTrans.get("inspection_verify_action_lbl")
                    binding.tvAutoAlreadyOk.text =
                        hmAuxTrans.get("inspection_already_ok_action_lbl")
                }
                //
                if (isNewItem || dayCount == null) {
                    binding.tvDayCount.visibility = View.GONE
                } else {
                    //LUCHE - 04/11/2021 - Revisado definição de lbl e cor, pois deve se olhar apenas
                    //se data maior ou menor que 0.
                    binding.tvDayCount.apply {
                        visibility = View.VISIBLE
                        text = if(dayCount <= 0){
                            "${hmAuxTrans["inspection_alert_days"]}: ${abs(dayCount)}"
                        }else{
                            "${hmAuxTrans["inspection_missing_days"]}: ${abs(dayCount)}"
                        }
                        //LUCHE - 26/01/2022
                        //Conforme solicitado em BUS-329, sempre exibir a cor cinza.
//                        setTextColor(
//                            if(dayCount < 0 && !isDone){
//                                ContextCompat.getColor(context,R.color.namoa_os_form_problem_red)
//                            } else{
//                                ContextCompat.getColor(context,R.color.gray_colors_menu)
//                            }
//                        )
                        setTextColor(ContextCompat.getColor(context,
                            com.namoa_digital.namoa_library.R.color.gray_colors_menu))
                    }
                }
                //
                if (hasComment) {
                    binding.ivCommentary.applyTintColor(R.color.namoa_color_cone_item)
                } else {
                    if (commentRequired && EXEC_TYPE_ALERT.equals(execType)) {
                        binding.ivCommentary.applyTintColor(R.color.namoa_color_highlight_required_item)
                    } else {
                        binding.ivCommentary.applyTintColor(R.color.namoa_color_gray_9)
                    }
                }
                //
                binding.tvProductAppliedCount.text = materialCount.toString()
                if (materialCount > 0) {
                    binding.ivProductApplied.applyTintColor(R.color.namoa_color_cone_item)
                    binding.tvProductAppliedCount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.namoa_color_cone_item
                        )
                    )
                } else {
                    if (materialRequired && EXEC_TYPE_FIXED.equals(execType)) {
                        binding.ivProductApplied.applyTintColor(R.color.namoa_color_highlight_required_item)
                        binding.tvProductAppliedCount.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.namoa_color_highlight_required_item
                            )
                        )
                    } else {
                        binding.ivProductApplied.applyTintColor(R.color.namoa_color_gray_9)
                        binding.tvProductAppliedCount.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.namoa_color_gray_9
                            )
                        )
                    }
                }
                //
                if(isRequiredPhoto){
                    binding.ivPhoto.applyTintColor(R.color.namoa_color_highlight_required_item)
                    binding.tvPhotoCount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.namoa_color_highlight_required_item
                        )
                    )
                }else{
                    binding.ivPhoto.applyTintColor(R.color.namoa_color_cone_item)
                    binding.tvPhotoCount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.namoa_color_cone_item
                        )
                    )
                }
                //
                binding.tvPhotoCount.text = photoCount.toString()
                //
                if (read_only) {
                    binding.tvInspectionVerificationAction.visibility = View.GONE
                }
                binding.tvAutoAlreadyOk.isEnabled = partitionedExecution != 1
            }
        }

    }
}