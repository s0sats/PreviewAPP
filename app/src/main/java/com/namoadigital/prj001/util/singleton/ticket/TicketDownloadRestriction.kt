package com.namoadigital.prj001.util.singleton.ticket

/**
 * @author Barriounevo
 * @since 11/2024
 * Singleton criado para controlar se o user esta na tela de ticket que não pode sofrer atualizacao em background.
 */
object TicketDownloadRestriction {
    lateinit var ticketPrefixRestriction:String
    lateinit var ticketCodeRestriction:String

    fun isTicketDownloadRestrictionInitialized() = ::ticketPrefixRestriction.isInitialized && ::ticketCodeRestriction.isInitialized

    fun setTicketDownloadRestrictionInitialized(ticketPrefix:Int, ticketCode:Int) {
        ticketPrefixRestriction = ticketPrefix.toString()
        ticketCodeRestriction = ticketCode.toString()
    }

    fun clearTicketDownloadRestrictionInitialized() {
        ticketPrefixRestriction = ""
        ticketCodeRestriction = ""
    }


}