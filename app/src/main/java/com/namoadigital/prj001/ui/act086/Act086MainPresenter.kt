package com.namoadigital.prj001.ui.act086

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsDeviceItemHistDao
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceItemHist
import com.namoadigital.prj001.sql.GeOsDeviceItemHistSql_002
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_001
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_005
import com.namoadigital.prj001.ui.act086.frg_historic.Act086HistoricFrg
import com.namoadigital.prj001.ui.act086.frg_verification.Act086VerificationFrg
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.IOException
import java.lang.Exception

class Act086MainPresenter(
    private val context: Context,
    private val mView: Act086MainContract.I_View,
    private val bundle: Bundle,
    private val mModule_Code: String,
    private val mResource_Code: String,
    private val geOsDeviceItemDao: GeOsDeviceItemDao,
    private val geOsDeviceItemHistDao: GeOsDeviceItemHistDao
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
            "info_lbl",
            "alert_form_parameter_error_ttl",
            "alert_form_parameter_error_msg",
            "inspection_missing_lbl",
            "inspection_alert_days_lbl",
            "alert_unsaved_data_will_be_lost_ttl",
            "alert_unsaved_data_will_be_lost_confirm",
            "info_ttl",
            "new_check_item_ttl",
            "check_item_ttl"
        )
        transList.addAll(
            Act086VerificationFrg.getFragTranslationsVars()
        )
        transList.addAll(
            Act086HistoricFrg.getFragTranslationsVars()
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
    override fun validBundleParams(isNewVerification: Boolean): Boolean {
       if( bundle.containsKey(ConstantBaseApp.DEVICE_BUNDLE)
           && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE) != null
           && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.containsKey(ConstantBaseApp.DEVICE_ITEM_PK)
       ){
           val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(ConstantBaseApp.DEVICE_ITEM_PK)
           deviceItemRawPk?.let {
               //Se tiver
               return try {
                   //Se tiver "." e o split tiver 10 elementos
                   it.contains(".")  && validPkSize(isNewVerification, it)
               }catch (e: Exception){
                   ToolBox_Inf.registerException(javaClass.name,e)
                   false
               }

           }
       }
       return false
    }

    /**
     * Valida se o split possui o tamanho esperado
     *  - Novo item: 8
     *  - Item Existente: 10
     */
    private fun validPkSize(isNewVerification: Boolean, it: String): Boolean {
        return (
                (!isNewVerification && it.split(".").size == 10)
                || (isNewVerification && it.split(".").size == 8)
                )
    }

    /**
     * Fun que retorna o obj GeOsDeviceItem, resgatando do banco via pk, ou criando o no caso de novo
     */
    override fun getDeviceItem(newVerification: Boolean): GeOsDeviceItem? {
        val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(ConstantBaseApp.DEVICE_ITEM_PK)
        deviceItemRawPk?.let {
            try {
                val splitedPK = it.split(".")
                if(newVerification) {
                    return createNewDeviceItem(splitedPK)
                } else {
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
                }
            }catch (e: Exception){
                ToolBox_Inf.registerException(javaClass.name,e)
            }
        }
        return null
    }


    /**
     * Fun que gera o obj GeOsDeviceItem para novos itens
     * Busca no banco o proximo item_check_seq e depois controi o obj
     *
     */
    @Throws(IOException::class)
    private fun createNewDeviceItem(splitedPK: List<String>): GeOsDeviceItem? {
        val nextItemCheckSeqAux = geOsDeviceItemDao.getByStringHM(
            GeOsDeviceItem_Sql_005(
                splitedPK[0],
                splitedPK[1],
                splitedPK[2],
                splitedPK[3],
                splitedPK[4],
                splitedPK[5],
                splitedPK[6],
                splitedPK[7]
            ).toSqlQuery()
        )
        //
        if( nextItemCheckSeqAux != null
            && nextItemCheckSeqAux.hasConsistentValue(GeOsDeviceItemDao.ITEM_CHECK_SEQ)
            && nextItemCheckSeqAux[GeOsDeviceItemDao.ITEM_CHECK_SEQ] != null
            && nextItemCheckSeqAux[GeOsDeviceItemDao.ITEM_CHECK_SEQ]!!.isNotEmpty()
        ){
            val nextItemCheckSeq = nextItemCheckSeqAux[GeOsDeviceItemDao.ITEM_CHECK_SEQ]!!.toInt()
            return GeOsDeviceItem(
                    customer_code =  splitedPK[0].toLong(),
                    custom_form_type = splitedPK[1].toInt(),
                    custom_form_code = splitedPK[2].toInt(),
                    custom_form_version = splitedPK[3].toInt(),
                    custom_form_data = splitedPK[4].toInt(),
                    product_code = splitedPK[5].toInt(),
                    serial_code = splitedPK[6].toInt(),
                    device_tp_code = splitedPK[7].toInt(),
                    item_check_code = 0,
                    item_check_seq = nextItemCheckSeq,
                    item_check_id = "0",
                    item_check_desc = GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL,
                    apply_material = GeOsDeviceItem.APPLY_MATERIAL_OPTIONAL  ,
                    verification_instruction = null,
                    require_justify_problem = 0,
                    critical_item = 0,
                    order_seq = nextItemCheckSeq ,
                    structure = 0 ,
                    manual_desc = null,
                    next_cycle_measure = null ,
                    next_cycle_measure_date = null ,
                    next_cycle_limit_date = null,
                    value_sufix = null,
                    item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            )
        }
        //
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

    override fun getDeviceItemHist(isNewVerification: Boolean): ArrayList<GeOsDeviceItemHist>? {
        //
        if(!isNewVerification) {
            val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!
                .getString(ConstantBaseApp.DEVICE_ITEM_PK)
            deviceItemRawPk?.let {
                try {
                    val splitedPK = it.split(".")
                    return geOsDeviceItemHistDao.query(
                        GeOsDeviceItemHistSql_002(
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
                    ) as ArrayList
                } catch (e: Exception) {
                    ToolBox_Inf.registerException(javaClass.name, e)
                }
            }
        }
        //
        return null
    }

    /**
     * Fun que verifica se há alguma dado visivel no frag de historico / consulta.
     * Verifica o check status, next_cycle, intrução se lista de historico.
     */
    override fun hasAnyVisibleInfoIntoConsultFrag(
        deviceItem: GeOsDeviceItem,
        itemHist:ArrayList<GeOsDeviceItemHist>?
    ): Boolean {
        //Se for um dos "status de alerta", verdadeiro
        when(deviceItem.item_check_status){
            GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL_ALERT ,
            GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT ,
            GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED ,
            GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED -> {
                return true
            }
            else -> {
                /*
                 *  Se o alert não será exibido, verfica se existe algumas das infos abaixo
                 *  - Historico
                 *  - Proximo Ciclo
                 *  - Instrucao
                 */
                if( (itemHist != null && itemHist.isNotEmpty())
                    || deviceItem.next_cycle_measure != null
                    || deviceItem.next_cycle_measure_date != null
                    ||  deviceItem.next_cycle_limit_date != null
                    ||  deviceItem.verification_instruction != null
                ){
                    return true
                }
            }
        }
        //Se não, nenhuma info a exibir.
        return false
    }

    override fun getActionBarTitle(currentFrag: Fragment, newOrCreatedByApp: Boolean): String? {
        return when(currentFrag){
                is Act086HistoricFrg -> {
                    hmAuxTrans["info_ttl"]
                }
                else ->{
                    if(newOrCreatedByApp){
                        hmAuxTrans["new_check_item_ttl"]
                    }else{
                        hmAuxTrans["check_item_ttl"]
                    }
                }
        }
    }

    override fun onBackPressedClicked(fragmentManager: FragmentManager, deviceItem: GeOsDeviceItem) {
        //
        when(getCurrentFrag(fragmentManager)){
            is Act086HistoricFrg ->{
                mView.popToVerificationFrag()
            }
            is Act086VerificationFrg->{
                //Se é um item criado via app structure == 0 e não foi respondido, status_answer == null
                //exibe msg de perda de dados ao sair
                if(deviceItem.structure == 0 && deviceItem.status_answer == null){
                    mView.showAlert(
                        hmAuxTrans["alert_unsaved_data_will_be_lost_ttl"],
                        hmAuxTrans["alert_unsaved_data_will_be_lost_confirm"],
                        DialogInterface.OnClickListener { dialog, which ->
                            mView.callAct011()
                        },
                        1
                    )
                }else{
                    mView.callAct011()
                }
            }
            else -> mView.callAct011()
        }
    }

    private fun getCurrentFrag(frgManager: FragmentManager): Fragment? {
        val currentFrag = frgManager.fragments.find {
            it.isVisible
        }
        return currentFrag
    }
}