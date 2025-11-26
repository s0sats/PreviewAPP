package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.extensions.date.convertDateToFullTimeStampGMT
import com.namoadigital.prj001.extensions.date.convertToDate
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.Calendar


data class DateTimeSelected(
    val date: String,
    val hour: String
) {
    val fullTimeStampGMT: String = "$date $hour".convertDateToFullTimeStampGMT(
        inputFormat = "yyyy-MM-dd HH:mm",
    )
}

@Composable
fun DateTimePicker(
    modifier: Modifier = Modifier,
    initialDate: String,
    dateHint: String,
    timeHint: String,
    isDateEnabled: Boolean = true,
    isTimeEnabled: Boolean = true,
    isError: Boolean = false,
    errorText: String = "",
    showOnlyThePast: Boolean = false,
    showOnlyTheFuture: Boolean = false,
    onDateTimeSelected: (DateTimeSelected) -> Unit
) {

    val context = LocalContext.current

    var dateText by remember {
        mutableStateOf(
            if (initialDate.isEmpty()) dateHint
            else initialDate.convertDateToFullTimeStampGMT(
                inputFormat = FULL_TIMESTAMP_TZ_FORMAT_GMT,
                outputFormat = ToolBox_Inf.nlsDateFormat(context)
            )
        )
    }

    var timeText by remember {
        mutableStateOf(
            if (initialDate.isEmpty()) timeHint
            else initialDate.convertDateToFullTimeStampGMT(
                inputFormat = FULL_TIMESTAMP_TZ_FORMAT_GMT,
                outputFormat = "HH:mm"
            )
        )
    }

    var dateFormatted by remember {
        mutableStateOf(
            if (initialDate.isEmpty()) "" else dateText.convertDateToFullTimeStampGMT(
                inputFormat = ToolBox_Inf.nlsDateFormat(context),
                outputFormat = "yyyy-MM-dd"
            )
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val calendar = remember {
        if (initialDate.isEmpty()) Calendar.getInstance()
        else Calendar.getInstance().apply { time = initialDate.convertToDate()!! }
    }

    LaunchedEffect(dateFormatted, timeText) {
        if (timeText != timeHint) {
            onDateTimeSelected(
                DateTimeSelected(
                    date = dateFormatted,
                    hour = timeText
                )
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                dateText = "$year-0${month + 1}-$dayOfMonth".convertDateToFullTimeStampGMT(
                    inputFormat = "yyyy-MM-dd",
                    outputFormat = ToolBox_Inf.nlsDateFormat(context)
                )
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateFormatted = dateText.convertDateToFullTimeStampGMT(
                    inputFormat = ToolBox_Inf.nlsDateFormat(context),
                    outputFormat = "yyyy-MM-dd"
                )
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            if (showOnlyThePast) datePicker.maxDate = System.currentTimeMillis()
            if (showOnlyTheFuture) datePicker.minDate = System.currentTimeMillis()
            setOnCancelListener { showDatePicker = false }
            show()
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                timeText = "$hourOfDay:$minute".convertDateToFullTimeStampGMT(
                    inputFormat = "HH:mm",
                    outputFormat = "HH:mm"
                )
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).apply {
            setOnCancelListener { showTimePicker = false }
            show()
        }
    }

    Column(
        modifier = modifier
            .animateContentSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NamoaTheme.spacing.small)
                .animateContentSize()
                .wrapContentHeight()
        ) {

            val (dateRef, timeRef) = createRefs()
            val focusManager = LocalFocusManager.current

            fun Modifier.addClickableInDate() =
                if (isDateEnabled) this.clickable { showDatePicker = true } else this

            fun Modifier.addClickableInTime() =
                if (isTimeEnabled) this.clickable { showTimePicker = true } else this

            CalendarTextField(
                text = dateText,
                isError = isError,
                placeHolder = dateHint,
                isEnabled = isDateEnabled,
                icon = Icons.Outlined.Event,
                modifier = Modifier
                    .addClickableInDate()
                    .fillMaxWidth(fraction = 0.55f)
                    .onFocusChanged {
                        if (it.isFocused && isDateEnabled) {
                            focusManager.clearFocus()
                            showDatePicker = true
                        }
                    }
                    .constrainAs(dateRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(timeRef.start)
                        width = Dimension.wrapContent
                    }
            ) {
                if (isDateEnabled) showDatePicker = true
            }

            CalendarTextField(
                text = timeText,
                isError = isError,
                isEnabled = isTimeEnabled,
                placeHolder = timeHint,
                icon = Icons.Default.Schedule,
                modifier = Modifier
                    .addClickableInTime()
                    .fillMaxWidth(0.5f)
                    .onFocusChanged {
                        if (it.isFocused && isTimeEnabled) {
                            focusManager.clearFocus()
                            showTimePicker = true
                        }
                    }
                    .constrainAs(timeRef) {
                        start.linkTo(dateRef.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                    }
            ) {
                if (isTimeEnabled) showTimePicker = true
            }
        }


        if (isError) {
            TextFieldError(
                modifier = Modifier.padding(start = NamoaTheme.spacing.small),
                text = errorText
            )
        }
    }

}


@Composable
private fun CalendarTextField(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    placeHolder: String,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = text,
        readOnly = true,
        textStyle = NamoaTheme.typography.bodyMedium,
        singleLine = true,
        enabled = isEnabled,
        modifier = modifier.padding(horizontal = NamoaTheme.spacing.small),
        label = { Text(placeHolder) },
        leadingIcon = {
            if (isEnabled) {
                IconButton(onClick = onClick) {
                    iconTextField(icon, isError, true)
                }
            } else {
                iconTextField(icon, isError, false)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = NamoaTheme.colors.surface,
            unfocusedContainerColor = NamoaTheme.colors.surface,
            disabledContainerColor = NamoaTheme.colors.surface,
            errorContainerColor = NamoaTheme.colors.surface,
        ),
        isError = isError,
        onValueChange = {}
    )
}

@Composable
private fun iconTextField(
    icon: ImageVector,
    isError: Boolean,
    isEnabled: Boolean
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isError && isEnabled) NamoaTheme.colors.error
        else if (isEnabled) NamoaTheme.colors.onSurface
        else Color.Gray
    )
}


@Composable
fun TextFieldError(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = "",
            tint = NamoaTheme.colors.error
        )
        Text(
            modifier = Modifier.padding(start = NamoaTheme.spacing.small),
            text = text,
            color = NamoaTheme.colors.error,
            style = NamoaTheme.typography.bodyMedium
        )
    }
}