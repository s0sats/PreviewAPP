package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 10/07/17.
 */

public class Act027_Header extends BaseFragment {

    private Context context;

    private HMAux data;

    //private HMAux hmAux_Trans;

    private TextView tv_product_code;
    private TextView tv_product_id;
    private TextView tv_product_desc;

    private TextView tv_so_id_title;
    private TextView tv_so_id;

    private TextView tv_so_desc_title;
    private TextView tv_so_desc;

    private TextView tv_prefix_code_title;
    private TextView tv_prefix_code;

    private TextView tv_serial_title;
    private TextView tv_serial;

    private TextView tv_category_price_id_title;
    private TextView tv_category_price_id;

    private TextView tv_category_price_desc_title;
    private TextView tv_category_price_desc;

    private TextView tv_segment_id_title;
    private TextView tv_segment_id;

    private TextView tv_segment_desc_title;
    private TextView tv_segment_desc;

    private TextView tv_site_id_title;
    private TextView tv_site_id;

    private TextView tv_site_desc_title;
    private TextView tv_site_desc;

    private TextView tv_operation_id_title;
    private TextView tv_operation_id;

    private TextView tv_operation_desc_title;
    private TextView tv_operation_desc;

    private TextView tv_deadline_title;
    private TextView tv_deadline;

    private TextView tv_status_title;
    private TextView tv_status;

    private TextView tv_priority_desc_title;
    private TextView tv_priority_desc;

    private TextView tv_contract_desc_title;
    private TextView tv_contract_desc;

    private TextView tv_contract_po_erp_title;
    private TextView tv_contract_po_erp;

    private TextView tv_contract_po_client1_title;
    private TextView tv_contract_po_client1;

    private TextView tv_contract_po_client2_title;
    private TextView tv_contract_po_client2;

    private TextView tv_quality_approval_user_title;
    private TextView tv_quality_approval_user;

    private TextView tv_quality_approval_user_nick_title;
    private TextView tv_quality_approval_user_nick;

    private TextView tv_quality_approval_date_title;
    private TextView tv_quality_approval_date;

    private TextView tv_comments_title;
    private TextView tv_comments;

    private TextView tv_client_type_title;
    private TextView tv_client_type;

    private TextView tv_client_user_title;
    private TextView tv_client_user;

    private TextView tv_client_code_title;
    private TextView tv_client_code;

    private TextView tv_client_id_title;
    private TextView tv_client_id;

    private TextView tv_client_name_title;
    private TextView tv_client_name;

    private TextView tv_client_email_title;
    private TextView tv_client_email;

    private TextView tv_client_phone_title;
    private TextView tv_client_phone;

    //private TextView tv_client_approval_image_title;
    //private TextView tv_client_approval_image;

    //private TextView tv_client_approval_image_name_title;
    //private TextView tv_client_approval_image_name;

    //private TextView tv_client_approval_image_url_title;
    //private TextView tv_client_approval_image_url;

    private TextView tv_client_approval_date_title;
    private TextView tv_client_approval_date;

    private TextView tv_client_approval_user_title;
    private TextView tv_client_approval_user;

    private TextView tv_client_approval_user_nick_title;
    private TextView tv_client_approval_user_nick;

    private TextView tv_total_qty_service_title;
    private TextView tv_total_qty_service;

    private TextView tv_total_price_title;
    private TextView tv_total_price;


    public void setData(HMAux data) {
        this.data = data;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act027_header_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void setHMAuxScreen() {
        if (data != null) {
            tv_product_code.setText(
                    hmAux_Trans.get("product_header_lbl") + " " +
                            data.get(SM_SODao.PRODUCT_CODE)
            );

            tv_product_id.setText(
                    hmAux_Trans.get("product_id_header_lbl") + " " +
                            data.get(SM_SODao.PRODUCT_ID)
            );

            tv_product_desc.setText(data.get(SM_SODao.PRODUCT_DESC));

            tv_so_id_title.setText(hmAux_Trans.get("so_id"));
            tv_so_id.setText(data.get(SM_SODao.SO_ID));

            tv_so_desc_title.setText(hmAux_Trans.get("so_desc"));
            tv_so_desc.setText(data.get(SM_SODao.SO_DESC));

            tv_prefix_code_title.setText(hmAux_Trans.get("so_code_lbl"));
            tv_prefix_code.setText(data.get(SM_SODao.SO_PREFIX) + " / " + data.get(SM_SODao.SO_CODE));

            tv_serial_title.setText(hmAux_Trans.get("serial"));
            tv_serial.setText(data.get(SM_SODao.SERIAL_ID));

            tv_category_price_id_title.setText(hmAux_Trans.get("category_price_id"));
            tv_category_price_id.setText(data.get(SM_SODao.CATEGORY_PRICE_ID));

            tv_category_price_desc_title.setText(hmAux_Trans.get("category_price_desc"));
            tv_category_price_desc.setText(data.get(SM_SODao.CATEGORY_PRICE_DESC));

            tv_segment_id_title.setText(hmAux_Trans.get("segment_id"));
            tv_segment_id.setText(data.get(SM_SODao.SEGMENT_ID));

            tv_segment_desc_title.setText(hmAux_Trans.get("segment_desc"));
            tv_segment_desc.setText(data.get(SM_SODao.SEGMENT_DESC));

            tv_site_id_title.setText(hmAux_Trans.get("site_id"));
            tv_site_id.setText(data.get(SM_SODao.SITE_ID));

            tv_site_desc_title.setText(hmAux_Trans.get("site_desc"));
            tv_site_desc.setText(data.get(SM_SODao.SITE_DESC));

            tv_operation_id_title.setText(hmAux_Trans.get("operation_id"));
            tv_operation_id.setText(data.get(SM_SODao.OPERATION_ID));

            tv_operation_desc_title.setText(hmAux_Trans.get("operation_desc"));
            tv_operation_desc.setText(data.get(SM_SODao.OPERATION_DESC));

            tv_deadline_title.setText(hmAux_Trans.get("deadline"));
            tv_deadline.setText(data.get(SM_SODao.DEADLINE));

            tv_status_title.setText(hmAux_Trans.get("status_lbl"));
            tv_status.setText(data.get(SM_SODao.STATUS));

            tv_priority_desc_title.setText(hmAux_Trans.get("priority_lbl"));
            tv_priority_desc.setText(data.get(SM_SODao.PRIORITY_DESC));

            tv_contract_desc_title.setText(hmAux_Trans.get("contract_desc"));
            tv_contract_desc.setText(data.get(SM_SODao.CONTRACT_DESC));

            tv_contract_po_erp_title.setText(hmAux_Trans.get("contract_po_erp"));
            tv_contract_po_erp.setText(data.get(SM_SODao.CONTRACT_PO_ERP));

            tv_contract_po_client1_title.setText(hmAux_Trans.get("contract_po_client1"));
            tv_contract_po_client1.setText(data.get(SM_SODao.CONTRACT_PO_CLIENT1));

            tv_contract_po_client2_title.setText(hmAux_Trans.get("contract_po_client2"));
            tv_contract_po_client2.setText(data.get(SM_SODao.CONTRACT_PO_CLIENT2));

            tv_quality_approval_user_title.setText(hmAux_Trans.get("quality_approval_user"));
            tv_quality_approval_user.setText(data.get(SM_SODao.QUALITY_APPROVAL_USER));

            tv_quality_approval_user_nick_title.setText(hmAux_Trans.get("quality_approval_user_nick"));
            tv_quality_approval_user_nick.setText(data.get(SM_SODao.QUALITY_APPROVAL_USER_NICK));

            tv_quality_approval_date_title.setText(hmAux_Trans.get("quality_approval_date"));
            tv_quality_approval_date.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(data.get(SM_SODao.QUALITY_APPROVAL_DATE), ""),
                            ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                    )
            );

            tv_comments_title.setText(hmAux_Trans.get("comments"));
            tv_comments.setText(data.get(SM_SODao.COMMENTS));

            tv_client_type_title.setText(hmAux_Trans.get("client_type"));
            tv_client_type.setText(data.get(SM_SODao.CLIENT_TYPE));

            tv_client_user_title.setText(hmAux_Trans.get("client_user"));
            tv_client_user.setText(data.get(SM_SODao.CLIENT_USER));

            tv_client_code_title.setText(hmAux_Trans.get("client_code"));
            tv_client_code.setText(data.get(SM_SODao.CLIENT_CODE));

            tv_client_id_title.setText(hmAux_Trans.get("client_id"));
            tv_client_id.setText(data.get(SM_SODao.CLIENT_ID));

            tv_client_name_title.setText(hmAux_Trans.get("client_name"));
            tv_client_name.setText(data.get(SM_SODao.CLIENT_NAME));

            tv_client_email_title.setText(hmAux_Trans.get("client_email"));
            tv_client_email.setText(data.get(SM_SODao.CLIENT_EMAIL));

            tv_client_phone_title.setText(hmAux_Trans.get("client_phone"));
            tv_client_phone.setText(data.get(SM_SODao.CLIENT_PHONE));

            //private TextView tv_client_approval_image_title;
            //private TextView tv_client_approval_image;

            //private TextView tv_client_approval_image_name_title;
            //private TextView tv_client_approval_image_name;

            //private TextView tv_client_approval_image_url_title;
            //private TextView tv_client_approval_image_url;

            tv_client_approval_date_title.setText(hmAux_Trans.get("client_approval_date"));
            tv_client_approval_date.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(data.get(SM_SODao.CLIENT_APPROVAL_DATE), ""),
                            ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                    )
            );

            tv_client_approval_user_title.setText(hmAux_Trans.get("client_approval_user"));
            tv_client_approval_user.setText(data.get(SM_SODao.CLIENT_APPROVAL_USER));

            tv_client_approval_user_nick_title.setText(hmAux_Trans.get("client_approval_user_nick"));
            tv_client_approval_user_nick.setText(data.get(SM_SODao.CLIENT_APPROVAL_USER_NICK));

            tv_total_qty_service_title.setText(hmAux_Trans.get("total_qty_service"));
            tv_total_qty_service.setText(data.get(SM_SODao.TOTAL_QTY_SERVICE));

            tv_total_price_title.setText(hmAux_Trans.get("total_price"));
            tv_total_price.setText(data.get(SM_SODao.TOTAL_PRICE));

        }
    }

    @Override
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    private void iniVar(View view) {
        tv_product_code = (TextView) view.findViewById(R.id.act027_header_content_tv_product_code_lbl);
        tv_product_id = (TextView) view.findViewById(R.id.act027_header_content_tv_product_id_lbl);
        tv_product_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_product_desc_value);
        tv_prefix_code = (TextView) view.findViewById(R.id.act027_header_content_tv_prefix_code);

        tv_so_id_title = (TextView) view.findViewById(R.id.act027_header_content_tv_so_id_title);
        tv_so_id = (TextView) view.findViewById(R.id.act027_header_content_tv_so_id);

        tv_so_desc_title = (TextView) view.findViewById(R.id.act027_header_content_tv_so_desc_title);
        tv_so_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_so_desc);

        tv_prefix_code_title = (TextView) view.findViewById(R.id.act027_header_content_tv_prefix_code_title);
        tv_prefix_code = (TextView) view.findViewById(R.id.act027_header_content_tv_prefix_code);

        tv_serial_title = (TextView) view.findViewById(R.id.act027_header_content_tv_serial_title);
        tv_serial = (TextView) view.findViewById(R.id.act027_header_content_tv_serial);

        tv_category_price_id_title = (TextView) view.findViewById(R.id.act027_header_content_tv_category_price_id_title);
        tv_category_price_id = (TextView) view.findViewById(R.id.act027_header_content_tv_category_price_id);

        tv_category_price_desc_title = (TextView) view.findViewById(R.id.act027_header_content_tv_category_price_desc_title);
        tv_category_price_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_category_price_desc);

        tv_segment_id_title = (TextView) view.findViewById(R.id.act027_header_content_tv_segment_id_title);
        tv_segment_id = (TextView) view.findViewById(R.id.act027_header_content_tv_segment_id);

        tv_segment_desc_title = (TextView) view.findViewById(R.id.act027_header_content_tv_segment_desc_title);
        tv_segment_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_segment_desc);

        tv_site_id_title = (TextView) view.findViewById(R.id.act027_header_content_tv_site_id_title);
        tv_site_id = (TextView) view.findViewById(R.id.act027_header_content_tv_site_id);

        tv_site_desc_title = (TextView) view.findViewById(R.id.act027_header_content_tv_site_desc_title);
        tv_site_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_site_desc);

        tv_operation_id_title = (TextView) view.findViewById(R.id.act027_header_content_tv_operation_id_title);
        tv_operation_id = (TextView) view.findViewById(R.id.act027_header_content_tv_operation_id);

        tv_operation_desc_title = (TextView) view.findViewById(R.id.act027_header_content_tv_operation_desc_title);
        tv_operation_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_operation_desc);

        tv_deadline_title = (TextView) view.findViewById(R.id.act027_header_content_tv_deadline_title);
        tv_deadline = (TextView) view.findViewById(R.id.act027_header_content_tv_deadline);

        tv_status_title = (TextView) view.findViewById(R.id.act027_header_content_tv_status_title);
        tv_status = (TextView) view.findViewById(R.id.act027_header_content_tv_status);

        tv_priority_desc_title = (TextView) view.findViewById(R.id.act027_header_content_tv_priority_desc_title);
        tv_priority_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_priority_desc);

        tv_contract_desc_title = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_desc_title);
        tv_contract_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_desc);

        tv_contract_po_erp_title = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_po_erp_title);
        tv_contract_po_erp = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_po_erp);

        tv_contract_po_client1_title = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_po_client1_title);
        tv_contract_po_client1 = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_po_client1);

        tv_contract_po_client2_title = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_po_client2_title);
        tv_contract_po_client2 = (TextView) view.findViewById(R.id.act027_header_content_tv_contract_po_client2);

        tv_quality_approval_user_title = (TextView) view.findViewById(R.id.act027_header_content_tv_quality_approval_user_title);
        tv_quality_approval_user = (TextView) view.findViewById(R.id.act027_header_content_tv_quality_approval_user);

        tv_quality_approval_user_nick_title = (TextView) view.findViewById(R.id.act027_header_content_tv_quality_approval_user_nick_title);
        tv_quality_approval_user_nick = (TextView) view.findViewById(R.id.act027_header_content_tv_quality_approval_user_nick);

        tv_quality_approval_date_title = (TextView) view.findViewById(R.id.act027_header_content_tv_quality_approval_date_title);
        tv_quality_approval_date = (TextView) view.findViewById(R.id.act027_header_content_tv_quality_approval_date);

        tv_comments_title = (TextView) view.findViewById(R.id.act027_header_content_tv_comments_title);
        tv_comments = (TextView) view.findViewById(R.id.act027_header_content_tv_comments);

        tv_client_type_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_type_title);
        tv_client_type = (TextView) view.findViewById(R.id.act027_header_content_tv_client_type);

        tv_client_user_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_user_title);
        tv_client_user = (TextView) view.findViewById(R.id.act027_header_content_tv_client_user);

        tv_client_code_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_code_title);
        tv_client_code = (TextView) view.findViewById(R.id.act027_header_content_tv_client_code);

        tv_client_id_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_id_title);
        tv_client_id = (TextView) view.findViewById(R.id.act027_header_content_tv_client_id);

        tv_client_name_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_name_title);
        tv_client_name = (TextView) view.findViewById(R.id.act027_header_content_tv_client_name);

        tv_client_email_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_email_title);
        tv_client_email = (TextView) view.findViewById(R.id.act027_header_content_tv_client_email);

        tv_client_phone_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_phone_title);
        tv_client_phone = (TextView) view.findViewById(R.id.act027_header_content_tv_client_phone);

        //private TextView tv_client_approval_image_title;
        //private TextView tv_client_approval_image;

        //private TextView tv_client_approval_image_name_title;
        //private TextView tv_client_approval_image_name;

        //private TextView tv_client_approval_image_url_title;
        //private TextView tv_client_approval_image_url;

        tv_client_approval_date_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_approval_date_title);
        tv_client_approval_date = (TextView) view.findViewById(R.id.act027_header_content_tv_client_approval_date);

        tv_client_approval_user_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_approval_user_title);
        tv_client_approval_user = (TextView) view.findViewById(R.id.act027_header_content_tv_client_approval_user);

        tv_client_approval_user_nick_title = (TextView) view.findViewById(R.id.act027_header_content_tv_client_approval_user_nick_title);
        tv_client_approval_user_nick = (TextView) view.findViewById(R.id.act027_header_content_tv_client_approval_user_nick);

        tv_total_qty_service_title = (TextView) view.findViewById(R.id.act027_header_content_tv_total_qty_service_title);
        tv_total_qty_service = (TextView) view.findViewById(R.id.act027_header_content_tv_total_qty_service);

        tv_total_price_title = (TextView) view.findViewById(R.id.act027_header_content_tv_total_price_title);
        tv_total_price = (TextView) view.findViewById(R.id.act027_header_content_tv_total_price);
        //
        setHMAuxScreen();
    }

    private void iniAction() {

    }

}
