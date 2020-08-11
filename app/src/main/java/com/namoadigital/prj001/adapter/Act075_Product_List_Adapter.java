package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_GET_MATERIAL;
import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_OPERATIONAL;
import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_RETURN_MATERIAL;

public class Act075_Product_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TK_Ticket_Product> mValues = new ArrayList<>();
    private int act_profile;
    private int inventory_control;
    private static final int FOOTER_VIEW = 1;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "act075_product_list_adapter";
    private Context context;
    private boolean isEditable;
    private boolean hasWithdrawApproved;
    private boolean hasAppliedApproved;
    private OnProductInteract mProductListener;
    private TK_Ticket_Approval tkTicketApproval;
    private OnApproveInteract mApproveListener;
    private ApprovalViewHolder approvalVH;

    public Act075_Product_List_Adapter(Context context, HMAux hmAux_Trans, List<TK_Ticket_Product> mValues, int act_profile, int inventory_control, boolean isEditable, boolean hasWithdrawApproved, boolean hasAppliedApproved, OnProductInteract mProductListener) {
        this.mValues.addAll(mValues);
        this.act_profile = act_profile;
        this.inventory_control = inventory_control;
        this.hmAux_Trans = hmAux_Trans;
        this.context = context;
        this.isEditable = isEditable;
        this.hasWithdrawApproved = hasWithdrawApproved;
        this.hasAppliedApproved = hasAppliedApproved;
        this.mProductListener = mProductListener;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                mResource_Name
        );
        //
        loadTranslation();
    }

    public Act075_Product_List_Adapter(Context context, HMAux hmAux_Trans, List<TK_Ticket_Product> mValues, TK_Ticket_Approval tkTicketApproval, int act_profile, int inventory_control, boolean isEditable, boolean hasWithdrawApproved, boolean hasAppliedApproved, OnApproveInteract mApproveListener) {
        this.mValues.addAll(mValues);
        this.tkTicketApproval = tkTicketApproval;
        this.act_profile = act_profile;
        this.inventory_control = inventory_control;
        this.hmAux_Trans = hmAux_Trans;
        this.context = context;
        this.isEditable = isEditable;
        this.hasWithdrawApproved = hasWithdrawApproved;
        this.hasAppliedApproved = hasAppliedApproved;
        this.mApproveListener = mApproveListener;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                mResource_Name
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("product_withdraw_lbl");
        transList.add("product_aplied_lbl");
        transList.add("product_returned_lbl");
        transList.add("product_confirm_lbl");
        transList.add("product_amount_lbl");
        transList.add("product_extract_lbl");
        transList.add("product_approve_option");
        transList.add("product_decline_option");
        transList.add("add_product_lbl");
        transList.add("product_comment_lbl");
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
                View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.act075_add_product_footer, viewGroup, false);
                return new AddProductViewHolder(inflate);
            }
            if (act_profile == 2) {
                View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.act075_approval_form, viewGroup, false);
                return new ApprovalViewHolder(inflate);
            }
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.act075_product_item, viewGroup, false);
        return new ProductViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TK_Ticket_Product tk_ticket_product = new TK_Ticket_Product();

        if (viewHolder instanceof ProductViewHolder) {
            if (mValues.size() > 0) {
                tk_ticket_product = mValues.get(position);
            }
            ProductViewHolder productVH = (ProductViewHolder) viewHolder;
            productVH.onBind(tk_ticket_product, position);
        } else if (viewHolder instanceof ApprovalViewHolder) {
            approvalVH = (ApprovalViewHolder) viewHolder;
            approvalVH.onBind();
        } else if (viewHolder instanceof AddProductViewHolder) {
            AddProductViewHolder addProductVH = (AddProductViewHolder) viewHolder;
            addProductVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProductListener.onAddProduct();
                }
            });
        }
    }

    public String getApprovalCommments() {
        return approvalVH.getApprovalComment();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mValues.size() && (!hasWithdrawApproved
        || act_profile == 2 )) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        if (mValues == null) {
            return 0;
        }

        if (act_profile == 1 && hasWithdrawApproved
        || act_profile == 1 && !isEditable) {
            return mValues.size();
        } else {
            if (mValues.size() == 0) {
                return 1;
            }
            return mValues.size() + 1;
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_info;
        TextView product_cell_tv_withdrawn;
        TextView product_cell_tv_applied;
        TextView product_cell_tv_returned;
        TextView product_cell_tv_extract;
        TextView product_cell_tv_withdrawn_qty;
        TextView product_cell_tv_applied_qty;
        TextView product_cell_tv_returned_qty;
        ConstraintLayout cl_withdrawn;
        ConstraintLayout cl_applied;
        ConstraintLayout cl_returned;
        ImageView product_cell_iv_withdrawn_substract;
        ImageView product_cell_iv_applied_substract;
        ImageView product_cell_iv_withdrawn_add;
        ImageView product_cell_iv_applied_add;
        ImageView product_cell_iv_returned_substract;
        ImageView product_cell_iv_returned_add;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_product_info = itemView.findViewById(R.id.tv_product_info);
            product_cell_tv_withdrawn = itemView.findViewById(R.id.product_cell_tv_withdrawn);
            product_cell_tv_applied = itemView.findViewById(R.id.product_cell_tv_applied);
            product_cell_tv_returned = itemView.findViewById(R.id.product_cell_tv_returned);
            product_cell_tv_extract = itemView.findViewById(R.id.product_cell_tv_extract);
            product_cell_tv_withdrawn_qty = itemView.findViewById(R.id.product_cell_tv_withdrawn_qty);
            product_cell_tv_applied_qty = itemView.findViewById(R.id.product_cell_tv_applied_qty);
            product_cell_tv_returned_qty = itemView.findViewById(R.id.product_cell_tv_returned_qty);
            //
            cl_withdrawn = itemView.findViewById(R.id.cl_withdrawn);
            cl_applied = itemView.findViewById(R.id.cl_applied);
            cl_returned = itemView.findViewById(R.id.cl_returned);
            //
            product_cell_iv_withdrawn_substract = itemView.findViewById(R.id.product_cell_iv_withdrawn_substract);
            product_cell_iv_applied_substract = itemView.findViewById(R.id.product_cell_iv_applied_substract);
            product_cell_iv_withdrawn_add = itemView.findViewById(R.id.product_cell_iv_withdrawn_add);
            product_cell_iv_applied_add = itemView.findViewById(R.id.product_cell_iv_applied_add);
            product_cell_iv_returned_substract = itemView.findViewById(R.id.product_cell_iv_returned_substract);
            product_cell_iv_returned_add = itemView.findViewById(R.id.product_cell_iv_returned_add);
            //
            setTranslation();
            //
            product_cell_iv_returned_substract.setVisibility(View.INVISIBLE);
            product_cell_iv_returned_add.setVisibility(View.INVISIBLE);
        }

        private void setTranslation() {
            if(act_profile == 1) {
                product_cell_tv_withdrawn.setText(hmAux_Trans.get("product_withdraw_lbl"));
                product_cell_tv_applied.setText(hmAux_Trans.get("product_aplied_lbl"));
                product_cell_tv_returned.setText(hmAux_Trans.get("product_returned_lbl"));
            }else{
                product_cell_tv_withdrawn.setText(hmAux_Trans.get("product_amount_lbl"));
                product_cell_tv_applied.setText(hmAux_Trans.get("product_amount_lbl"));
                product_cell_tv_returned.setText(hmAux_Trans.get("product_amount_lbl"));
            }
            product_cell_tv_extract.setText(hmAux_Trans.get("product_extract_lbl"));
        }

        public void onBind(final TK_Ticket_Product tk_ticket_product, final int position) {
            //
            Log.d("PRODUCT", "qty: " + tk_ticket_product.getQty());
            Log.d("PRODUCT", "qty_used: " + tk_ticket_product.getQty_used());
            //
            tv_product_info.setText(tk_ticket_product.getProduct_desc());
            //
            product_cell_tv_withdrawn_qty.setText(String.format("%s %s", tk_ticket_product.getQty(), tk_ticket_product.getUn()));
            product_cell_tv_applied_qty.setText(String.format("%s %s", tk_ticket_product.getQty_used(), tk_ticket_product.getUn()));
            product_cell_tv_returned_qty.setText(String.format("%s %s", tk_ticket_product.getQty_returned(), tk_ticket_product.getUn()));
            //
            if (tk_ticket_product.getQty() == null) {
                tk_ticket_product.setQty(0.0);
            }
            //
            if (tk_ticket_product.getQty() <= 0) {
                product_cell_iv_withdrawn_substract.setEnabled(false);
            } else {
                product_cell_iv_withdrawn_substract.setEnabled(true);
            }
            if (tk_ticket_product.getQty_used() <= 0) {
                product_cell_iv_applied_substract.setEnabled(false);
            } else {
                product_cell_iv_applied_substract.setEnabled(true);
            }
            //
            if (tk_ticket_product.getQty_used() == null) {
                tk_ticket_product.setQty_used(0.0);
                product_cell_iv_applied_add.setEnabled(true);
            }
            //
            if(inventory_control == 1) {
                if (tk_ticket_product.getQty_used() < tk_ticket_product.getQty()) {
                    product_cell_iv_applied_add.setEnabled(true);
                }else{
                    product_cell_iv_applied_add.setEnabled(false);
                }
            }
            //
            if (act_profile == 1) {
                if (inventory_control == 0) {
                    noInventoryControlLayout();
                    product_cell_tv_extract.setVisibility(View.GONE);
                } else {
                    if(!hasWithdrawApproved){
                        cl_withdrawn.setVisibility(View.VISIBLE);
                        cl_applied.setVisibility(View.GONE);
                        cl_returned.setVisibility(View.GONE);
                        product_cell_tv_extract.setVisibility(View.GONE);
                        if(isEditable){
                            enableWithdrawLayout();
                        }else{
                            disableWithdrawLayout();
                        }
                    }else if(!hasAppliedApproved){
                        cl_withdrawn.setVisibility(View.VISIBLE);
                        cl_applied.setVisibility(View.VISIBLE);
                        cl_returned.setVisibility(View.GONE);
                        product_cell_tv_extract.setVisibility(View.VISIBLE);
                        disableWithdrawLayout();
                        if(isEditable){
                            enableAppliedLayout();
                        }else{
                            disableAppliedLayout();
                        }
                        calculateProductBalance(tk_ticket_product);
                    }else{
                        cl_withdrawn.setVisibility(View.VISIBLE);
                        cl_applied.setVisibility(View.VISIBLE);
                        cl_returned.setVisibility(View.VISIBLE);
                        product_cell_tv_extract.setVisibility(View.VISIBLE);
                        disableWithdrawLayout();
                        disableAppliedLayout();
                    }
                }

                if(isEditable) {

                    if(mProductListener.hasWithdrawnDataChange(tk_ticket_product)) {
                        product_cell_tv_withdrawn_qty.setBackground(context.getResources().getDrawable(R.color.namoa_color_ticket_process_highlight));
                    }else{
                        product_cell_tv_withdrawn_qty.setBackground(context.getResources().getDrawable(R.color.padrao_WHITE));
                    }

                    if(mProductListener.hasAppliedDataChange(tk_ticket_product)) {
                        product_cell_tv_applied_qty.setBackground(context.getResources().getDrawable(R.color.namoa_color_ticket_process_highlight));
                    }else{
                        product_cell_tv_applied_qty.setBackground(context.getResources().getDrawable(R.color.padrao_WHITE));
                    }

                    product_cell_tv_withdrawn_qty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mProductListener.callQtyDialog(position, tk_ticket_product);

                        }
                    });
                    //
                    product_cell_tv_applied_qty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mProductListener.callQtyUsedDialog(position, tk_ticket_product);
                        }
                    });
                    //
                    product_cell_iv_withdrawn_substract.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double mWithdraw = tk_ticket_product.getQty() - 1;
                            tk_ticket_product.setQty(mWithdraw);
                            notifyItemChanged(position);
                        }
                    });
                    //
                    product_cell_iv_withdrawn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double mWithdraw = tk_ticket_product.getQty() + 1;
                            tk_ticket_product.setQty(mWithdraw);
                            notifyItemChanged(position);
                        }
                    });
                    //
                    product_cell_iv_applied_substract.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            double mApplied = tk_ticket_product.getQty_used() - 1;
                            tk_ticket_product.setQty_used(mApplied);
                            notifyItemChanged(position);
                        }
                    });

                    //
                    product_cell_iv_applied_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double mApplied = tk_ticket_product.getQty_used() + 1;
                            tk_ticket_product.setQty_used(mApplied);
                            notifyItemChanged(position);
                        }
                    });
                }
            } else if (act_profile == 2) {
                if(!hasAppliedApproved){
                    product_cell_tv_extract.setVisibility(View.GONE);
                }
                setAmountControllersVisibility(View.GONE);

                setDetailsForApprovalVisibility();
            }
            //
        }

        private void calculateProductBalance(TK_Ticket_Product tk_ticket_product) {
            double qtyUsed = 0.0;
            if(tk_ticket_product.getQty_used()!= null){
                qtyUsed = tk_ticket_product.getQty_used();
            }
            double balance = tk_ticket_product.getQty() - qtyUsed;
            product_cell_tv_extract.setText(hmAux_Trans.get("product_extract_lbl") + " " + balance);
        }

        private void noInventoryControlLayout() {

            cl_withdrawn.setVisibility(View.GONE);
            cl_applied.setVisibility(View.VISIBLE);
            cl_returned.setVisibility(View.GONE);
            product_cell_tv_extract.setVisibility(View.GONE);

            if(isEditable){
                enableAppliedLayout();
            }else{
                disableAppliedLayout();
            }
        }

        private void disableAppliedLayout() {
            product_cell_iv_applied_substract.setVisibility(View.INVISIBLE);
            product_cell_iv_applied_add.setBackground(context.getResources().getDrawable(R.color.padrao_WHITE));
            product_cell_iv_applied_add.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_white_24dp));
            product_cell_iv_applied_add.setImageTintList(context.getResources().getColorStateList(R.color.namoa_product_extract_check));
        }

        private void enableAppliedLayout() {
            product_cell_iv_applied_substract.setVisibility(View.VISIBLE);
            product_cell_iv_applied_add.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_black_24px));
            product_cell_iv_applied_add.setImageTintList(context.getResources().getColorStateList(R.color.colorFooterWhite));
        }

        private void enableWithdrawLayout() {
            product_cell_iv_withdrawn_substract.setVisibility(View.VISIBLE);
            product_cell_iv_withdrawn_add.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_black_24px));
            product_cell_iv_withdrawn_add.setImageTintList(context.getResources().getColorStateList(R.color.colorFooterWhite));
        }

        private void disableWithdrawLayout() {
            product_cell_iv_withdrawn_substract.setVisibility(View.INVISIBLE);
            product_cell_iv_withdrawn_add.setBackground(context.getResources().getDrawable(R.color.padrao_WHITE));
            product_cell_iv_withdrawn_add.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_white_24dp));
            product_cell_iv_withdrawn_add.setImageTintList(context.getResources().getColorStateList(R.color.namoa_product_extract_check));
        }

        //
        private void setDetailsForApprovalVisibility() {
            if(APPROVAL_GET_MATERIAL.equalsIgnoreCase(tkTicketApproval.getApproval_type())){
                cl_withdrawn.setVisibility(View.VISIBLE);
                cl_applied.setVisibility(View.GONE);
                cl_returned.setVisibility(View.GONE);
            }
            if( APPROVAL_RETURN_MATERIAL.equalsIgnoreCase(tkTicketApproval.getApproval_type())){
                cl_withdrawn.setVisibility(View.GONE);
                cl_applied.setVisibility(View.VISIBLE);
                cl_returned.setVisibility(View.GONE);
            }
            if( APPROVAL_OPERATIONAL.equalsIgnoreCase(tkTicketApproval.getApproval_type())){
                cl_withdrawn.setVisibility(View.VISIBLE);
                cl_applied.setVisibility(View.VISIBLE);
                cl_returned.setVisibility(View.VISIBLE);
            }
        }

        //
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
            tv_add_product_lbl = itemView.findViewById(R.id.footer_add_product);
            tv_add_product_lbl.setText(hmAux_Trans.get("add_product_lbl"));
        }
    }

    public class ApprovalViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ask_confirm;
        TextView tv_comment_lbl;
        TextInputLayout til_comment;
        MKEditTextNM mket_comment;
        RadioGroup mRadioGroup;
        RadioButton rg_decline;
        RadioButton rg_approval;

        public ApprovalViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ask_confirm = itemView.findViewById(R.id.act075_approval_form_tv_ask_confirm);
            tv_comment_lbl = itemView.findViewById(R.id.act075_approval_form_tv_comment_lbl);
            til_comment = itemView.findViewById(R.id.act075_approval_form_til_comment);
            mket_comment = itemView.findViewById(R.id.act075_approval_form_mket_comment);
            mRadioGroup = itemView.findViewById(R.id.act075_approval_form_rg);
            rg_approval = itemView.findViewById(R.id.act075_approval_form_rg_approval);
            rg_decline = itemView.findViewById(R.id.act075_approval_form_rg_decline);
            //
            setTranslation();
            //
        }

        private void setTranslation() {
            tv_comment_lbl.setText(hmAux_Trans.get("product_comment_lbl"));
            rg_approval.setText(hmAux_Trans.get("product_approve_option"));
            rg_decline.setText(hmAux_Trans.get("product_decline_option"));
        }

        public void onBind() {
            String question = tkTicketApproval.getApproval_question() != null ? tkTicketApproval.getApproval_question() : "";
            tv_ask_confirm.setText(question);
            String comments = tkTicketApproval.getApproval_comments() != null ? tkTicketApproval.getApproval_comments() : "";
            //
            mket_comment.setText(comments);
            //
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    switch (checkedId) {
                        case R.id.act075_approval_form_rg_approval:
                            mApproveListener.onSelectOption(true);
                            mApproveListener.setSaveEnable(true);
                            break;
                        case R.id.act075_approval_form_rg_decline:
                            mApproveListener.onSelectOption(false);
                            if (mket_comment.getText().toString().isEmpty()) {
                                mApproveListener.setSaveEnable(false);
                            }else{
                                mApproveListener.setSaveEnable(true);
                            }
                            break;
                    }
                }
            });
            if(!isEditable) {
                til_comment.setCounterEnabled(false);
                if (ConstantBase.SYS_STATUS_DONE.equalsIgnoreCase(tkTicketApproval.getApproval_status())) {
                    rg_approval.setChecked(true);
                } else {
                    rg_decline.setChecked(true);
                }
                rg_approval.setEnabled(false);
                rg_decline.setEnabled(false);
                mRadioGroup.setEnabled(false);
                mket_comment.setEnabled(false);
            }else{
                mket_comment.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String comment = s.toString();
                        if (comment.isEmpty() && rg_decline.isChecked()) {
                            mApproveListener.setSaveEnable(false);
                        }else{
                            mApproveListener.setSaveEnable(true);
                        }
                    }
                });
            }
            //
        }
        //
        public String getApprovalComment() {
            if (mket_comment != null) {
                return mket_comment.getText().toString();
            }
            return "";
        }
        //
        public Boolean isApprovaed() {
            if (!rg_approval.isChecked() && !rg_decline.isChecked()) {
                return null;
            }
            return rg_approval.isChecked();
        }
    }

    public List<TK_Ticket_Product> getmValues() {
        return mValues;
    }

    public void setmValues(List<TK_Ticket_Product> mValues) {
        this.mValues.clear();
        this.mValues.addAll(mValues);
        notifyDataSetChanged();
    }

    public interface OnProductInteract {
        void onAddProduct();

        void callQtyDialog(int position, TK_Ticket_Product tk_ticket_product);

        void callQtyUsedDialog(int position, TK_Ticket_Product tk_ticket_product);

        boolean hasWithdrawnDataChange(TK_Ticket_Product tk_ticket_product);

        boolean hasAppliedDataChange(TK_Ticket_Product tk_ticket_product);
    }

    public interface OnApproveInteract {
        void onSelectOption(boolean isApproved);
        void setSaveEnable(boolean isEnable);

    }

}
