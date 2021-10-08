package com.namoadigital.prj001.ui.act086

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_001
import com.namoadigital.prj001.ui.act086.frg_verification.Act086VerificationFrg
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.lang.Exception

class Act086MainPresenter(
    private val context: Context,
    private val mView: Act086MainContract.I_View,
    private val bundle: Bundle,
    private val mModule_Code: String,
    private val mResource_Code: String,
    private val geOsDeviceItemDao: GeOsDeviceItemDao
) : Act086MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    override fun getTranslation() = hmAuxTrans

    private fun loadTranslation() : HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act086_title",
            "product_ttl",
            "btn_apply",
            "alert_choose_an_answer_msg",
            "query_lbl",
            "alert_form_parameter_error_ttl",
            "alert_form_parameter_error_msg",
            "inspection_missing_lbl",
            "inspection_alert_days_lbl"
        )
        transList.addAll(
            Act086VerificationFrg.getFragTranslationsVars()
        )
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }

    /**
     * @author LUCHE - 23/08/2021
     * Fun que verifica se o bottom da view passada esta fora do campo de visão e se estiver chama
     * fun que fara o scroll para nova posição
     *
     * Em resumo:
     *  - Usa o a altura da tela do device(screenMetricHeight) e o tamanho da actionBar e footer para
     *  definir o tamanho da area visivel(calculatedVisibleArea).
     *  - Soma a essa area visivel(calculatedVisibleArea) o top atual do scroll descobrindo o "bottom"
     *  atual da area visivel(calculatedScrollBottom).
     *  - Verifica se o bottom calculado(calculatedScrollBottom) for menor que o bottom da view(viewBottomPosition), significa
     *  que é o item esta escondido, então a tela precisa realizar o scroll.
     *  - Calcula o nova posicao Top do scroll somando ao top atual(scrollTop) a altura recebida(heightToAdd)
     *
     *
     * @param viewBottomPosition - Posicao Y do bottom da view
     * @param heightToAdd - Altura a ser adicionando no scroll caso necessario
     * @param scrollTop - Posição Y(top) onde o ScrollView esta posicionado
     * @param actionBarHeight - Tamanho da actionBar ou 0 se não existir
     * @param footerHeight - Altura do Footer
     */
    override fun checkViewPositionIsVisible(
        viewBottomPosition: Int,
        heightToAdd: Int,
        scrollTop: Int,
        actionBarHeight: Int,
        footerHeight: Int
    ) {
        //Pega o tamanho da tela do device
        val screenMetricHeight = ToolBox_Inf.getScreenMetrics(context)[1]
        //Desconta do tamanho da tela a action bar e o footer. Teria ainda a statusBar mas muito treta
        val calculatedVisibleArea = screenMetricHeight - (footerHeight  + actionBarHeight)
        //Se o Top do scroll + area de visao disponivel for menor que a posicao Y do bottom do recycler
        //significa que item ficará escondido então tenta o scroll
        val calculatedScrollBottom = calculatedVisibleArea + scrollTop
        if(calculatedScrollBottom < viewBottomPosition){
            //Soma o Top do scroll + a altura da celula ajustada
            val newScrollTop = scrollTop + heightToAdd
            //Faz o scroll
            mView.updateScrollPosition(newScrollTop)
        }
    }

    /**
     * Fun que valida se pk esta "ok"
     * Valida se item veio no bundle, se não é null, se tem "." e se seu split é igual a 10 elementos.
     */
    override fun validBundleParams(): Boolean {
       if( bundle.containsKey(ConstantBaseApp.DEVICE_BUNDLE)
           && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE) != null
           && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.containsKey(ConstantBaseApp.DEVICE_ITEM_PK)
       ){
           val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(ConstantBaseApp.DEVICE_ITEM_PK)
           deviceItemRawPk?.let {
               //Se tiver
               return try {
                   //Se tiver "." e o split tiver 10 elementos
                   it.contains(".")  && it.split(".").size == 10
               }catch (e: Exception){
                   ToolBox_Inf.registerException(javaClass.name,e)
                   false
               }

           }
       }
       return false
    }

    override fun getDeviceItem(newVerification: Boolean): GeOsDeviceItem? {
        val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(ConstantBaseApp.DEVICE_ITEM_PK)
        deviceItemRawPk?.let {
            try {
                val splitedPK = it.split(".")
               return geOsDeviceItemDao.getByString(
                    GeOsDeviceItem_Sql_001(
                        splitedPK[0],
                        splitedPK[1],
                        splitedPK[2],
                        splitedPK[3],
                        splitedPK[4],
                        splitedPK[5],
                        splitedPK[6],
                        splitedPK[7],
                        splitedPK[8],
                        splitedPK[9]
                    ).toSqlQuery()
                )
            }catch (e: Exception){
                ToolBox_Inf.registerException(javaClass.name,e)
            }
        }
        return null
    }

    override fun getPrefixPhoto(
        deviceItem: GeOsDeviceItem
    ): String {
        return "${ConstantBaseApp.GE_OS_DEVICE_ITEM_PREX_IMG}" +
                "${deviceItem.customer_code}_" +
                "${deviceItem.custom_form_type}_" +
                "${deviceItem.custom_form_code}_" +
                "${deviceItem.custom_form_version}_" +
                "${deviceItem.custom_form_data}_" +
                "${deviceItem.product_code}_" +
                "${deviceItem.serial_code}_" +
                "${deviceItem.device_tp_code}_" +
                "${deviceItem.item_check_code}_" +
                "${deviceItem.item_check_seq}_"
    }
}