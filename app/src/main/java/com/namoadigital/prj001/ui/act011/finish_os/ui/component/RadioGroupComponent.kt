package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.namoadigital.prj001.design.compose.ApplicationTheme


typealias RadioGroupContent = @Composable (RadioGroupItem<*>) -> Unit
typealias SwitchContent = @Composable ColumnScope.() -> Unit

data class RadioGroupItem<T>(
    val id: Int,
    val text: String,
    val value: T,
    val isSelected: Boolean = false,
    val content: List<RadioGroupContent> = emptyList()
)

data class RadioGroupOptions(
    val list: List<RadioGroupItem<*>>
)


@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    radioGroupOptions: RadioGroupOptions,
    isEnabled: Boolean = true,
    onOptionSelected: ((RadioGroupItem<*>) -> Unit)? = null
) {

    var radioGroupSelected by remember { mutableStateOf<RadioGroupItem<*>?>(null) }
    var checkedSelected by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize()
    ) {
        radioGroupOptions.list.forEach { item ->

            if (item.isSelected && !checkedSelected) {
                radioGroupSelected = item
                checkedSelected = true
                onOptionSelected?.invoke(item)
            }

            RadioGroupTextButton(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = ApplicationTheme.spacing.small,
                            topEnd = ApplicationTheme.spacing.small,
                            bottomStart = ApplicationTheme.spacing.small,
                            bottomEnd = ApplicationTheme.spacing.small,
                        )
                    )
                    .fillMaxWidth(),
                isSelected = radioGroupSelected?.id == item.id,
                isEnabled = isEnabled,
                item = item,
                onOptionSelected = {
                    radioGroupSelected = it
                    onOptionSelected?.invoke(it)
                }
            )

            if (radioGroupSelected == item && item.content.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .padding(start = ApplicationTheme.spacing.mediumSmall)
                ) {
                    item.content.forEach { content ->
                        content(radioGroupSelected!!)
                    }
                }
            }
        }
    }
}

@Composable
fun RadioGroupTextButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    item: RadioGroupItem<*>,
    isEnabled: Boolean = true,
    onOptionSelected: (RadioGroupItem<*>) -> Unit
) {

    var hasMoreThanTwoLines by remember { mutableStateOf(false) }

    fun Modifier.addClickable() = if (isEnabled) this.clickable { onOptionSelected(item) } else this

    Row(
        modifier = modifier
            .addClickable()
            .padding(
                top = if (hasMoreThanTwoLines) ApplicationTheme.spacing.small
                else ApplicationTheme.spacing.none,
                bottom = if (hasMoreThanTwoLines) ApplicationTheme.spacing.small
                else ApplicationTheme.spacing.none,
                end = ApplicationTheme.spacing.small
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (hasMoreThanTwoLines) Arrangement.SpaceBetween else Arrangement.Start
    ) {
        RadioButton(
            enabled = isEnabled,
            selected = isSelected,
            onClick = { onOptionSelected(item) }
        )

        Text(
            text = item.text,
            onTextLayout = { textLayout ->
                hasMoreThanTwoLines = textLayout.lineCount > 1
            },
            style = ApplicationTheme.typography.bodyLarge,
            color = if (isEnabled) ApplicationTheme.colors.onSurface else Color.Gray
        )
    }
}