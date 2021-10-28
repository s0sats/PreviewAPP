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
import com.namoadigital.prj001.model.GeOsDeviceItem.Companion.EXEC_TYPE_ALERT
import com.namoadigital.prj001.model.GeOsDeviceItem.Companion.EXEC_TYPE_ALREADY_OK
import com.namoadigital.prj001.model.GeOsDeviceItem.Companion.EXEC_TYPE_FIXED
import com.namoadigital.prj001.model.GeOsDeviceItem.Companion.EXEC_TYPE_NOT_VERIFIED
import com.namoadigital.prj001.model.InspectionCell
import java.util.*

class Act011InspectionFormAdapter(
    /*
        Barrionuevo 05/10;2021
        Objeto está aqui para facilitar a passagem de param do click listener.
     */
    private val acessoryFormView: AcessoryFormView,
    private val hmAuxTrans: HMAux,
    private val onItemSelected: (
        position: Int,
        itemPk: String
    ) -> Unit,
    private val onNotVerifyItemSelected: (
        position: Int,
        item: InspectionCell
    ) -> Unit
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
        inspectionsFiltered.addAll(acessoryFormView.inspections)
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
            binding.root.setOnClickListener {
                onItemSelected(
                    position,
                    inspectionCell.itemCodeAndSeq
                )
            }
            holder.binding.tvAutoSkipInspection.setOnClickListener {
                onNotVerifyItemSelected(
                    position,
                    inspectionCell
                )
            }
        }
    }

    fun applyNonForecastFilter(filterApplied: Boolean) {
        this.filterApplied = filterApplied
        inspectionsFiltered.clear()
        if (this.filterApplied) {
            inspectionsFiltered.addAll(inspections.filter {
                it.status != InspectionCell.NORMAL || it.isDone
            })
        } else {
            inspectionsFiltered.addAll(inspections)
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

    fun refreshList(position: Int, onNotVerifyActionItem: InspectionCell) {
        inspectionsFiltered.set(position, onNotVerifyActionItem)
        notifyItemChanged(position)
    }

    inner class InspectionFormFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<InspectionCell>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            textFilter = charFilter
            if (charFilter.isNullOrEmpty()) {
                if (filterApplied) {
                    temp.addAll(inspectionsFiltered)
                } else {
                    temp.addAll(inspections)
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
                //
                notifyDataSetChanged()
            }
        }
    }

    inner class MyInspectionFormVH(val binding: Act011InspectionQuestionFormCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBinding(inspection: InspectionCell) {
            inspection.apply {
                val context = binding.root.context
                binding.tvInspectionOngoingAction.visibility = View.GONE
                if (isDone) {
                    binding.llAnswerInfo.visibility = View.VISIBLE
                    binding.tvInspectAnswered.visibility = View.VISIBLE
                    binding.tvInspectionOngoingAction.visibility = View.GONE
                    binding.tvInspectionVerificationAction.visibility = View.GONE
                    binding.tvAutoSkipInspection.visibility = View.GONE
                } else {
                    binding.llAnswerInfo.visibility = View.GONE
                    binding.tvInspectAnswered.visibility = View.GONE
                    binding.tvInspectionVerificationAction.visibility = View.VISIBLE
                    if (!InspectionCell.NORMAL.equals(inspection.status)) {
                        binding.tvAutoSkipInspection.visibility = View.VISIBLE
                    } else {
                        binding.tvAutoSkipInspection.visibility = View.GONE
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
                    background.setColorFilter(
                        ContextCompat.getColor(context, tagColor),
                        android.graphics.PorterDuff.Mode.SRC_ATOP
                    )
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
                    binding.tvInspectAnswered.text = execTypeTranslated
                    when (execType) {
                        EXEC_TYPE_FIXED -> {
                            binding.ivInspectAnswered.setImageDrawable(
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_build_black_24dp
                                )
                            )
                        }
                        EXEC_TYPE_ALERT -> {
                            binding.ivInspectAnswered.setImageDrawable(
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_outline_report_problem_24_black
                                )
                            )
                        }
                        EXEC_TYPE_ALREADY_OK -> {
                            binding.ivInspectAnswered.setImageDrawable(
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_done_black_24dp
                                )
                            )
                        }
                        EXEC_TYPE_NOT_VERIFIED -> {
                            binding.ivInspectAnswered.setImageDrawable(
                                ContextCompat.getDrawable(
                                    Objects.requireNonNull(context),
                                    R.drawable.ic_baseline_redo_24_black
                                )
                            )
                        }
                    }

                    if (!isDone) {
                        binding.tvInspectionOngoingAction.text =
                            hmAuxTrans.get("inspection_ongoing_action_lbl")
                        binding.tvInspectionOngoingAction.visibility = View.VISIBLE
                        binding.tvInspectionVerificationAction.visibility = View.GONE
                        binding.tvAutoSkipInspection.visibility = View.GONE
                        binding.llAnswerInfo.visibility = View.VISIBLE
                    }
                } else {
                    binding.tvInspectionVerificationAction.visibility = View.VISIBLE
                    binding.tvInspectionVerificationAction.text =
                        hmAuxTrans.get("inspection_verify_action_lbl")
                    binding.tvAutoSkipInspection.text =
                        hmAuxTrans.get("inspection_not_verify_action_lbl")
                }
                //
                if (isNewItem
                    || dayCount == null
                ) {
                    binding.tvDayCount.visibility = View.GONE
                } else {
                    binding.tvDayCount.visibility = View.VISIBLE
                    if (status.equals(InspectionCell.NORMAL)) {
                        binding.tvDayCount.text =
                            "${hmAuxTrans.get("inspection_missing_days")}: ${dayCount}"
                        binding.tvDayCount.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.gray_colors_menu
                            )
                        )
                    } else {
                        binding.tvDayCount.text =
                            "${hmAuxTrans.get("inspection_alert_days")}: ${dayCount}"
                        if (isDone) {
                            binding.tvDayCount.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.gray_colors_menu
                                )
                            )
                        } else {
                            binding.tvDayCount.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.namoa_os_form_problem_red
                                )
                            )
                        }
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
                if (photoCount > 0) {
                    binding.ivPhoto.applyTintColor(R.color.namoa_color_cone_item)
                    binding.tvPhotoCount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.namoa_color_cone_item
                        )
                    )
                } else {
                    binding.ivPhoto.applyTintColor(R.color.namoa_color_gray_9)
                    binding.tvPhotoCount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.namoa_color_gray_9
                        )
                    )
                }
                binding.tvPhotoCount.text = photoCount.toString()
                //
            }
        }

    }
}