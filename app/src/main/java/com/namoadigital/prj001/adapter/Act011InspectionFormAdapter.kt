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
import com.namoadigital.prj001.model.InspectionCellActions
import com.namoadigital.prj001.ui.act011.frags.InspectionListFragmentInteraction

class Act011InspectionFormAdapter(
    /*
        Barrionuevo 05/10;2021
        Objeto está aqui para facilitar a passagem de param do click listener.
     */
    private val acessoryFormView: AcessoryFormView,
    private val hmAuxTrans: HMAux,
    private val myInspectionClickListener: InspectionListFragmentInteraction
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val inspections: List<InspectionCell> = acessoryFormView.inspections
    private var inspectionsFiltered: MutableList<InspectionCell> = mutableListOf()
    protected var textFilter:String = ""
    val mFilter = InspectionFormFilter()

    init {
        inspectionsFiltered.clear()
        inspectionsFiltered.addAll(inspections)
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
            if(inspectionCell.answer != null) {
                binding.root.setOnClickListener {
                    myInspectionClickListener.onInspectionSelected(
                        acessoryFormView,
                        InspectionCellActions.VERIFY,
                        position,
                        textFilter
                    )
                }
            }
        }


        holder.binding.tvAutoSkipInspection.setOnClickListener {
            myInspectionClickListener.onInspectionSelected(
                acessoryFormView,
                InspectionCellActions.VERIFY_LATER,
                position,
                textFilter
            )
        }

        holder.binding.tvInspectionVerificationAction.setOnClickListener {
            myInspectionClickListener.onInspectionSelected(
                acessoryFormView,
                InspectionCellActions.VERIFY,
                position,
                textFilter
            )
        }

    }

    fun applyNonForecastFilter(filterApplied: Boolean) {
        inspectionsFiltered.clear()
        if (filterApplied) {
            inspectionsFiltered.addAll(inspections.filter {
                it.status != InspectionCell.NORMAL
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
        return mFilter
    }

    inner class InspectionFormFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<InspectionCell>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            textFilter = charFilter
            if (charFilter.isNullOrEmpty()) {
                temp = inspections as MutableList<InspectionCell>
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
                inspectionsFiltered = results.values as MutableList<InspectionCell>
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
                    binding.tvAutoSkipInspection.visibility = View.VISIBLE
                    binding.tvAutoSkipInspection.setOnClickListener {
                        myInspectionClickListener.onInspectionSelected(
                            acessoryFormView,
                            InspectionCellActions.VERIFY_LATER,
                            position,
                            textFilter
                        )
                    }

                }
                //
                binding.tvInspectionDescription.text = description
                //
                binding.tvStatus.apply {
                    text = status
                    background.setColorFilter(ContextCompat.getColor(context, tagColor), android.graphics.PorterDuff.Mode.SRC_ATOP)
                }
                binding.vCellColorTag.apply {
                    background.setColorFilter(ContextCompat.getColor(context, tagColor), android.graphics.PorterDuff.Mode.SRC_ATOP)
                }
                //
                if (answer != null) {
                    binding.tvInspectAnswered.text = answer
                    if (!isDone) {
                        binding.tvInspectionOngoingAction.text = hmAuxTrans.get("inpection_ongoing_action_lbl")
                        binding.tvInspectionOngoingAction.visibility = View.VISIBLE
                        binding.tvInspectionVerificationAction.visibility = View.GONE
                        binding.tvAutoSkipInspection.visibility = View.GONE
                        binding.llAnswerInfo.visibility = View.VISIBLE
                    }
                } else {
                    binding.tvInspectionVerificationAction.visibility = View.VISIBLE
                    binding.tvInspectionVerificationAction.text = hmAuxTrans.get("inpection_verify_action_lbl")
                    binding.tvAutoSkipInspection.text =  hmAuxTrans.get("inpection_verify_action_lbl")
                }
                //
                if (isNewItem) {
                    binding.tvDayCount.visibility = View.GONE
                } else {
                    binding.tvDayCount.visibility = View.VISIBLE
                    if (status.equals(InspectionCell.NORMAL)) {
                        binding.tvDayCount.text =
                            "${hmAuxTrans.get("inspection_missing_days")}: ${dayCount}"
                        binding.tvDayCount.setTextColor(ContextCompat.getColor(context, R.color.gray_colors_menu))
                    } else {
                        binding.tvDayCount.text =
                            "${hmAuxTrans.get("inspection_alert_days")}: ${dayCount}"
                        binding.tvDayCount.setTextColor(ContextCompat.getColor(context, R.color.namoa_os_form_problem_red))
                    }
                }
                //
                if (hasComment) {
                    binding.ivCommentary.applyTintColor(R.color.namoa_color_cone_item)
                } else {
                    if (commentRequired) {
                        binding.ivCommentary.applyTintColor(R.color.namoa_color_highlight_required_item)
                    } else {
                        binding.ivCommentary.applyTintColor(R.color.namoa_color_gray_9)
                    }
                }
                //
                binding.tvProductAppliedCount.text = materialCount.toString()
                if (materialCount > 0) {
                    binding.ivProductApplied.applyTintColor(R.color.namoa_color_cone_item)
                    binding.tvProductAppliedCount.setTextColor(ContextCompat.getColor(context, R.color.namoa_color_cone_item))
                } else {
                    if (materialRequired) {
                        binding.ivProductApplied.applyTintColor(R.color.namoa_color_highlight_required_item)
                        binding.tvProductAppliedCount.setTextColor(ContextCompat.getColor(context, R.color.namoa_color_highlight_required_item))
                    } else {
                        binding.ivProductApplied.applyTintColor(R.color.namoa_color_gray_9)
                        binding.tvProductAppliedCount.setTextColor(ContextCompat.getColor(context, R.color.namoa_color_gray_9))
                    }
                }
                //
                if (photoCount > 0) {
                    binding.ivPhoto.applyTintColor(R.color.namoa_color_cone_item)
                    binding.tvPhotoCount.setTextColor(ContextCompat.getColor(context, R.color.namoa_color_cone_item))
                } else {
                    binding.ivPhoto.applyTintColor(R.color.namoa_color_gray_9)
                    binding.tvPhotoCount.setTextColor(ContextCompat.getColor(context, R.color.namoa_color_gray_9))
                }
                binding.tvPhotoCount.text = photoCount.toString()
                //
            }
        }

    }
}