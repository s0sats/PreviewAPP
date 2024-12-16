package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.namoadigital.prj001.R
import com.namoadigital.prj001.design.compose.ApplicationTheme


@Composable
fun TitleSwitch(
    modifier: Modifier = Modifier,
    title: String,
    isRequiredOption: Boolean = false,
    isEnabled: Boolean = true,
    forceShowContent: Boolean = false,
    initialSwitchState: Boolean = false,
    onSwitchChecked: (Boolean) -> Unit,
    content: SwitchContent? = null
) {
    var switchState by remember { mutableStateOf(initialSwitchState) }

    Column(
        modifier = modifier.animateContentSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = ApplicationTheme.spacing.small,
                        topEnd = ApplicationTheme.spacing.small,
                        bottomStart = ApplicationTheme.spacing.small,
                        bottomEnd = ApplicationTheme.spacing.small,
                    )
                )
                .clickable(enabled = isEnabled) {
                    switchState = !switchState
                    onSwitchChecked(switchState)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = ApplicationTheme.spacing.small,
                        horizontal = ApplicationTheme.spacing.extraSmall
                    )
            ) {
                val (titleRef, switchRef) = createRefs()

                Text(
                    modifier = Modifier
                        .padding(
                            start = ApplicationTheme.spacing.medium,
                            end = ApplicationTheme.spacing.medium
                        )
                        .constrainAs(titleRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(switchRef.start)
                            width = Dimension.fillToConstraints
                        },
                    text = buildAnnotatedString {
                        append(title)

                        if (isRequiredOption && isEnabled) {
                            withStyle(style = SpanStyle(color = colorResource(id = R.color.m3_namoa_extended_LaranjaObrigatorio_color))) {
                                append(" *")
                            }
                        }
                    },
                    style = ApplicationTheme.typography.bodyLarge,
                    color = ApplicationTheme.colors.onSurface,
                    overflow = TextOverflow.Ellipsis
                )

                ApplicationSwitch(
                    modifier = Modifier.constrainAs(switchRef) {
                        start.linkTo(titleRef.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                    },
                    isEnabled = isEnabled,
                    isChecked = switchState,
                    onCheckedChange = {
                        switchState = it
                        onSwitchChecked(it)
                    }
                )
            }
        }

        if (content != null && (switchState || forceShowContent)) {
            content()
        }
    }
}


@Composable
fun ApplicationSwitch(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    var localChecked by remember { mutableStateOf(isChecked) }
    val thumbPosition by animateDpAsState(
        targetValue = if (localChecked) 10.dp else (-10).dp,
        label = "",
        animationSpec = tween(durationMillis = 300)
    )

    val colorState by animateColorAsState(
        targetValue = (
                if (localChecked) {
                    ApplicationTheme.colors.primary.copy(alpha = if (isEnabled) 1f else 0.35f)
                } else {
                    Color.Gray.copy(alpha = if (isEnabled) 1f else 0.35f)
                }
                ),
        label = "",
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(isChecked) {
        localChecked = isChecked
    }

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    if (isEnabled) {
                        localChecked = !localChecked
                        onCheckedChange(localChecked)
                    }
                }
            }
            .padding(ApplicationTheme.spacing.small)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.width(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()

                    .height(15.dp)
                    .background(
                        color = colorState.copy(alpha = if (isEnabled) 0.4f else 0.1f),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures {
                            if (isEnabled) {
                                localChecked = !localChecked
                                onCheckedChange(localChecked)
                            }
                        }
                    }
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center)
                    .graphicsLayer { translationX = thumbPosition.toPx() }
                    .background(color = colorState, shape = RoundedCornerShape(12.dp))

            ) {
                if (localChecked) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}