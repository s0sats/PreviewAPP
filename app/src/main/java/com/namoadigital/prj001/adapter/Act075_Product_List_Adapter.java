package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act075_Product_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TK_Ticket_Product> mValues;
    private int act_profile = -1;
    private static final int FOOTER_VIEW = 1;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "Act075_Product_List_Adapter";
    private Context context;

    public Act075_Product_List_Adapter(Context context, List<TK_Ticket_Product> mValues, int act_profile) {
        this.mValues = mValues;
        this.act_profile = act_profile;
        this.mResource_Code = mResource_Code;
        this.hmAux_Trans = hmAux_Trans;
        this.mResource_Name = mResource_Name;
        this.context = context;
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("product_withdraw_lbl");
        transList.add("product_aplied_lbl");
        transList.add("product_returned_lbl");
        transList.add("product_amount_lbl");
        transList.add("product_extract_lbl");
        transList.add("add_product_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                ConstantBaseApp.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == FOOTER_VIEW) {
            if (act_profile == 1) {
                View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.act075_approval_form, viewGroup, false);
                AddProductViewHolder vh = new AddProductViewHolder(inflate);
                return vh;
            }
            if (act_profile == 2) {
                View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.act075_approval_form, viewGroup, false);
                ApprovalViewHolder vh = new ApprovalViewHolder(inflate);
                return vh;
            }
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.act075_approval_form, viewGroup, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(inflate, act_profile);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TK_Ticket_Product tk_ticket_product = mValues.get(position);
        if (viewHolder instanceof ProductViewHolder) {
            ProductViewHolder productVH = (ProductViewHolder) viewHolder;
            productVH.onBind(tk_ticket_product);
            if (tk_ticket_product.getQty_used() == tk_ticket_product.getQty_used()) {

            }
        } else if (viewHolder instanceof ApprovalViewHolder) {
            ApprovalViewHolder approvalVH = (ApprovalViewHolder) viewHolder;
            approvalVH.onBind(tk_ticket_product);
        } else if (viewHolder instanceof AddProductViewHolder) {
            AddProductViewHolder addProductVH = (AddProductViewHolder) viewHolder;
            addProductVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mValues.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        if (mValues.size() == 0) {
            return 1;
        }
        return mValues.size() + 1;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_info;
        TextView product_cell_tv_withdrawn;
        TextView product_cell_tv_applied;
        TextView product_cell_tv_returned;
        TextView product_cell_tv_extract;
        ConstraintLayout cl_withdrawn;
        ConstraintLayout cl_applied;
        ConstraintLayout cl_returned;
        ImageView product_cell_iv_withdrawn_substract;
        ImageView product_cell_iv_applied_substract;
        ImageView product_cell_iv_withdrawn_add;
        ImageView product_cell_iv_applied_add;

        public ProductViewHolder(@NonNull View itemView, int act_profile) {
            super(itemView);
            tv_product_info = itemView.findViewById(R.id.tv_product_info);
            product_cell_tv_withdrawn = itemView.findViewById(R.id.product_cell_tv_withdrawn);
            product_cell_tv_applied = itemView.findViewById(R.id.product_cell_tv_applied);
            product_cell_tv_returned = itemView.findViewById(R.id.product_cell_tv_returned);
            product_cell_tv_extract = itemView.findViewById(R.id.product_cell_tv_extract);
            //
            cl_withdrawn = itemView.findViewById(R.id.cl_withdrawn);
            cl_applied = itemView.findViewById(R.id.cl_applied);
            cl_returned = itemView.findViewById(R.id.cl_returned);
            //
            product_cell_iv_withdrawn_substract = itemView.findViewById(R.id.product_cell_iv_withdrawn_substract);
            product_cell_iv_applied_substract = itemView.findViewById(R.id.product_cell_iv_applied_substract);
            product_cell_iv_withdrawn_add = itemView.findViewById(R.id.product_cell_iv_withdrawn_add);
            product_cell_iv_applied_add = itemView.findViewById(R.id.product_cell_iv_applied_add);
            //
            setTranslation();
        }

        private void setTranslation() {
            product_cell_tv_withdrawn.setText(hmAux_Trans.get("product_withdraw_lbl"));
            product_cell_tv_applied.setText(hmAux_Trans.get("product_aplied_lbl"));
            if (act_profile == 1) {
                product_cell_tv_returned.setText(hmAux_Trans.get("product_returned_lbl"));
            } else {
                product_cell_tv_returned.setText(hmAux_Trans.get("product_amount_lbl"));
            }
            product_cell_tv_extract.setText(hmAux_Trans.get("product_extract_lbl"));
        }

        public void onBind(TK_Ticket_Product tk_ticket_product) {
            if (act_profile == 1) {
                setAmountControllersVisibility(View.VISIBLE);
                setDetailsForApprovalVisibility(View.VISIBLE);
            } else if (act_profile == 2) {
                setAmountControllersVisibility(View.GONE);
                setDetailsForApprovalVisibility(View.GONE);
            }
        }

        private void setDetailsForApprovalVisibility(int visibility) {
            cl_withdrawn.setVisibility(visibility);
            cl_applied.setVisibility(visibility);
        }

        private void setAmountControllersVisibility(int visibility) {
            product_cell_iv_withdrawn_substract.setVisibility(visibility);
            product_cell_iv_applied_substract.setVisibility(visibility);
            product_cell_iv_withdrawn_add.setVisibility(visibility);
            product_cell_iv_applied_add.setVisibility(visibility);
        }
    }

    public class AddProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_add_product_lbl;

        public AddProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_add_product_lbl = itemView.findViewById(R.id.tv_add_product_lbl);
            tv_add_product_lbl.setText(hmAux_Trans.get("add_product_lbl"));
        }
    }

    public class ApprovalViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ask_confirm;
        TextView tv_comment_lbl;
        MKEditTextNM mket_comment;
        RadioGroup mRadioGroup;
        RadioButton rg_decline;
        RadioButton rg_approval;
        public ApprovalViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView tv_ask_confirm = itemView.findViewById(R.id.act075_approval_form_tv_ask_confirm);
            TextView tv_comment_lbl = itemView.findViewById(R.id.act075_approval_form_tv_comment_lbl);
            MKEditTextNM mket_comment = itemView.findViewById(R.id.act075_approval_form_mket_comment);
            RadioGroup mRadioGroup = itemView.findViewById(R.id.act075_approval_form_rg);
            RadioButton rg_decline = itemView.findViewById(R.id.act075_approval_form_rg_decline);
            RadioButton rg_approval = itemView.findViewById(R.id.act075_approval_form_rg_approval);

        }

        public void onBind(TK_Ticket_Product tk_ticket_product) {

        }
    }
}
