package com.namoadigital.prj001.ui.act070.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.translate
import com.namoadigital.prj001.design.compose.ApplicationTheme
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.model.ticket.TkSerialSearchRequest.Companion.DEFAULT_PAGE_SIZE
import com.namoadigital.prj001.ui.act070.model.ListSerialSearchItemsArguments
import com.namoadigital.prj001.ui.act070.view.dialog.DialogSerialSearchFragment.Companion.DIALOG_HINT_SERIAL_SEARCH
import com.namoadigital.prj001.ui.act070.view.dialog.DialogSerialSearchFragment.Companion.DIALOG_LABEL_CARD_SITE_SERIAL_SEARCH
import com.namoadigital.prj001.ui.act070.view.dialog.DialogSerialSearchFragment.Companion.DIALOG_LABEL_CARD_TICKET_OPEN_SERIAL_SEARCH
import com.namoadigital.prj001.ui.act070.view.dialog.DialogSerialSearchFragment.Companion.DIALOG_LABEL_EMPTY_LIST_SERIAL_SEARCH
import com.namoadigital.prj001.ui.act070.view.dialog.DialogSerialSearchFragment.Companion.DIALOG_LABEL_SECTION_SERIAL_SEARCH
import com.namoadigital.prj001.ui.act070.view.dialog.DialogSerialSearchFragment.Companion.DIALOG_TITLE_SERIAL_SEARCH

interface OnActionListSerialSearchItems {
    fun selectSerial(serial: BaseSerialSearchItem.SerialSearchItem)
}


@Composable
fun SerialListLayout(
    modifier: Modifier = Modifier,
    arguments: ListSerialSearchItemsArguments,
    onClose: () -> Unit,
    onSelectItem: (BaseSerialSearchItem.SerialSearchItem) -> Unit
) {
    SerialListComponent(
        modifier = modifier,
        list = arguments.list,
        translateMap = arguments.translateMap,
        onClose = onClose,
        onSelectItem = onSelectItem
    )
}


@Composable
private fun SerialListComponent(
    modifier: Modifier = Modifier,
    list: List<BaseSerialSearchItem>,
    translateMap: TranslateMap,
    onClose: () -> Unit,
    onSelectItem: (BaseSerialSearchItem.SerialSearchItem) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val filteredList = list.filter { item ->
        when (item) {
            is BaseSerialSearchItem.SerialSearchItem -> {
                val query = searchText.trim().lowercase()
                listOfNotNull(item.serialId, item.productDesc, item.siteDesc)
                    .any { it.lowercase().contains(query) }
            }

            else -> searchText.isEmpty()
        }
    }

    Column(modifier) {
        HeaderSection(
            searchText,
            title = translateMap.translate(DIALOG_TITLE_SERIAL_SEARCH),
            hint = translateMap.translate(DIALOG_HINT_SERIAL_SEARCH),
            sectionHeader = translateMap.translate(DIALOG_LABEL_SECTION_SERIAL_SEARCH),
            emptyList = translateMap.translate(DIALOG_LABEL_EMPTY_LIST_SERIAL_SEARCH),
            itemCount = filteredList.filterIsInstance<BaseSerialSearchItem.SerialSearchItem>().size,
            onSearchTextChange = { searchText = it },
            onClose = onClose
        )

        SerialList(
            list = filteredList,
            translateMap = translateMap,
            onSelectItem = onSelectItem
        )
    }
}

@Composable
private fun HeaderSection(
    searchText: String,
    title: String,
    hint: String,
    sectionHeader: String,
    emptyList: String,
    itemCount: Int,
    onSearchTextChange: (String) -> Unit,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = ApplicationTheme.spacing.medium)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopBarApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ApplicationTheme.spacing.medium),
                title = title,
                onClose = onClose
            )

            OutlinedFilterSerial(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                hint = hint,
                itemCount = itemCount,
                emptyList = emptyList,
                sectionHeader = sectionHeader
            )
        }
    }
}

@Composable
private fun OutlinedFilterSerial(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    hint: String,
    itemCount: Int,
    emptyList: String,
    sectionHeader: String
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ApplicationTheme.spacing.mediumSmall)
            .padding(horizontal = ApplicationTheme.spacing.medium),
        value = searchText,
        onValueChange = onSearchTextChange,
        label = { Text(hint) },
        singleLine = true
    )

    DividerSection("$sectionHeader $itemCount")
}

@Composable
private fun TopBarApp(
    modifier: Modifier,
    title: String,
    onClose: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier.padding(top = ApplicationTheme.spacing.medium)
    ) {
        val (text, icon) = createRefs()

        Text(
            text = title,
            style = ApplicationTheme.typography.titleLarge,
            modifier = Modifier
                .constrainAs(text) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Fechar",
            modifier = Modifier
                .clickable { onClose() }
                .constrainAs(icon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun DividerSection(text: String) {
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ApplicationTheme.spacing.medium),
        text = text,
        textAlign = TextAlign.Center,
        style = ApplicationTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        color = ApplicationTheme.colors.outline
    )
    HorizontalDivider(thickness = 1.dp, color = ApplicationTheme.colors.outline)
}

@Composable
private fun ColumnScope.SerialList(
    list: List<BaseSerialSearchItem>,
    translateMap: TranslateMap,
    onSelectItem: (BaseSerialSearchItem.SerialSearchItem) -> Unit
) {

    if(list.isEmpty()){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ){
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = ApplicationTheme.typography.bodyMedium,
                color = ApplicationTheme.colors.outline,
                text = translateMap.translate(DIALOG_LABEL_EMPTY_LIST_SERIAL_SEARCH)
            )
        }
    }else{
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ApplicationTheme.spacing.medium)
                .background(color = ApplicationTheme.colors.outlineVariant.copy(alpha = 0.09f))
                .weight(1f)
        ) {
            items(
                items = list,
                key = { item -> item.hashCode() }
            ) { item ->
                when (item) {
                    is BaseSerialSearchItem.SerialSearchItem -> SerialItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = item,
                        labelSite = translateMap.translate(DIALOG_LABEL_CARD_SITE_SERIAL_SEARCH),
                        labelTicketOpen = translateMap.translate(DIALOG_LABEL_CARD_TICKET_OPEN_SERIAL_SEARCH),
                    ) { onSelectItem(it) }

                    is BaseSerialSearchItem.SerialSearchExceededItem -> ExceedRecordsWidget(
                        modifier = Modifier.fillMaxWidth(),
                        foundQty = item.foundQty,
                        sizeLimit = DEFAULT_PAGE_SIZE,
                        labelCounterFound = item.pageLabel!!,
                        labelCounterLimit = item.foundQtyLbl!!,
                        labelExceeded = item.exceedMsg!!
                    )

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun SerialItem(
    modifier: Modifier,
    item: BaseSerialSearchItem.SerialSearchItem,
    labelSite: String,
    labelTicketOpen: String,
    onClick: (BaseSerialSearchItem.SerialSearchItem) -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = ApplicationTheme.spacing.extraSmall)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = ApplicationTheme.spacing.extraSmall),
            onClick = { onClick(item) }
        ) {
            Column(
                modifier = Modifier.padding(ApplicationTheme.spacing.small)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = ApplicationTheme.spacing.medium),
                    text = item.serialId,
                    style = ApplicationTheme.typography.bodyLarge.copy(
                        color = ApplicationTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    modifier = Modifier.padding(horizontal = ApplicationTheme.spacing.medium),
                    text = item.productDesc,
                    style = ApplicationTheme.typography.labelLarge.copy(
                        color = ApplicationTheme.colors.onSurface,
                    )
                )

                item.siteDesc?.let { BuildLabeledText(labelSite, item.siteDesc) }

                BuildLabeledText(
                    label = labelTicketOpen,
                    value = item.ticketOpenQty?.toString() ?: "0",
                    ticketOpenQty = item.ticketOpenQty ?: 0
                )
            }
        }
    }
}

@Composable
private fun TicketIcons() {
    val ticketsToShow = 3
    Box(
        modifier = Modifier
            .height(ApplicationTheme.spacing.large)
    ) {
        repeat(ticketsToShow) { i ->
            Icon(
                painter = painterResource(R.drawable.ticket_icon),
                contentDescription = null,
                tint = ApplicationTheme.colors.primary.copy(alpha = 1f - (i * 0.3f)),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(ApplicationTheme.spacing.mediumLarge)
                    .offset(x = (i * -6).dp)
            )
        }
    }
}

@Composable
private fun BuildLabeledText(label: String, value: String?, ticketOpenQty: Int = 0) {
    if (ticketOpenQty > 0) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ApplicationTheme.spacing.medium)
        ) {
            Text(
                modifier = Modifier
                    .padding(end = ApplicationTheme.spacing.small),
                text = "$label: ",
                style = ApplicationTheme.typography.bodyMedium.copy(
                    color = ApplicationTheme.colors.outline,
                    fontWeight = FontWeight.Bold
                )
            )
            TicketIcons()
            Text(
                style = ApplicationTheme.typography.bodyMedium,
                text = value!!
            )
        }

    } else {
        Text(
            modifier = Modifier.padding(horizontal = ApplicationTheme.spacing.medium),
            text = buildAnnotatedString {
                withStyle(
                    style = ApplicationTheme.typography.bodyMedium.copy(
                        color = ApplicationTheme.colors.outline,
                        fontWeight = FontWeight.Bold
                    ).toSpanStyle(),
                ) {
                    append("$label: ")
                }

                withStyle(
                    style = ApplicationTheme.typography.bodyMedium.copy(
                        color = ApplicationTheme.colors.onSurface
                    ).toSpanStyle(),
                ) {
                    append(value ?: "")
                }
            }
        )
    }

}


@Composable
fun ExceedRecordsWidget(
    modifier: Modifier,
    labelExceeded: String,
    labelCounterLimit: String,
    labelCounterFound: String,
    foundQty: Int,
    sizeLimit: Int,
) {
    Box(
        modifier = modifier
            .padding(horizontal = ApplicationTheme.spacing.medium)
            .padding(vertical = ApplicationTheme.spacing.small)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (tvExceedRecordsLbl, tvCounterVal, tvPageVal) = createRefs()
            val gdCenter = createGuidelineFromStart(0.5f)

            Text(
                text = labelExceeded,
                modifier = Modifier
                    .constrainAs(tvExceedRecordsLbl) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    },
                textAlign = TextAlign.Center
            )

            Text(
                text = "$labelCounterFound: $foundQty",
                color = colorResource(R.color.namoa_color_gray_9),
                style = ApplicationTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .constrainAs(tvCounterVal) {
                        start.linkTo(gdCenter)
                        end.linkTo(parent.end, margin = 4.dp)
                        top.linkTo(tvExceedRecordsLbl.bottom)
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = "$labelCounterLimit: $sizeLimit",
                color = colorResource(R.color.namoa_color_gray_9),
                style = ApplicationTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .constrainAs(tvPageVal) {
                        start.linkTo(parent.start, margin = 4.dp)
                        end.linkTo(gdCenter)
                        top.linkTo(tvExceedRecordsLbl.bottom)
                        width = Dimension.fillToConstraints
                    }
            )
        }
    }
}
