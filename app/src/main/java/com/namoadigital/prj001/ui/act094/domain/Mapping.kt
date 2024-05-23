package com.namoadigital.prj001.ui.act094.domain

import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailables
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable


fun DestinationAvailables.toAdapterList() = SelectionDestinationAvailable(
    destinationType = destinationType,
    siteCode = siteCode,
    siteDesc = siteDesc,
    ticketPrefix = ticketPrefix,
    ticketCode = ticketCode,
    serialId = serialId,
    address = address,
    countryId = countryId,
    state = state,
    city = city,
    district = district,
    street = street,
    lat = lat,
    lon = lon,
    streetnumber = streetnumber,
    complement = complement,
    zipCode = zipcode,
    plusCode = plusCode,
    contactName = contactName,
    contactPhone = contactPhone,
    siteMainUser = siteMainUser,
    regionCode = regionCode,
    regionDesc = regionDesc,
    minDate = minDate,
    priorityCnt = priorityCnt,
    todayCnt = todayCnt,
    lateCnt = lateCnt,
    nextCnt = nextCnt,
    serialCnt = serialCnt
)
fun FsTripDestination.toDestinationDetailDialog() = SelectionDestinationAvailable(
    destinationType = destinationType,
    siteCode = destinationSiteCode,
    siteDesc = destinationSiteDesc,
    ticketPrefix = ticketPrefix,
    ticketCode = ticketCode,
    serialId = null,
    address = null,
    countryId = countryId,
    state = state,
    city = city,
    district = district,
    street = street,
    lat = latitude,
    lon = longitude,
    streetnumber = streetnumber,
    complement = complement,
    zipCode = zipCode,
    plusCode = plusCode,
    contactName = contactName,
    contactPhone = contactPhone,
    siteMainUser = siteMainUser,
    regionCode = destinationRegionCode,
    regionDesc = destinationRegionDesc,
    minDate = minDate,
    priorityCnt = null,
    todayCnt = null,
    lateCnt = null,
    nextCnt = null,
    serialCnt = serialCnt
)