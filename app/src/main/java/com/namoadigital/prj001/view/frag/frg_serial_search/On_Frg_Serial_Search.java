package com.namoadigital.prj001.view.frag.frg_serial_search;

public interface On_Frg_Serial_Search {
    boolean hasHideSerialInfoChk();
    /**
     * LUCHE - 10/11/2020
     * Interface disparada quando o Frg_Serial_Search, recebe retorno da Act_Product_Selection.
     */
    interface onProductSelectionReturnListener{
        void onProductSelectionReturn(String current_product_id, String returned_product_id);
    }

    /**
     * LUCHE - 13/11/2020
     * Interface disparada quando o campo prod_id é digitado
     */
    interface onProductTypingListener{
        void onProductTyping(String typed_product_id);
    }
}
