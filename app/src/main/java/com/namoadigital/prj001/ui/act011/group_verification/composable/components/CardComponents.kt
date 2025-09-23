package com.namoadigital.prj001.ui.act011.group_verification.composable.components

import RowNamoaBadge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.ApplicationSwitch
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.GROUP_EXPIRED_VERIFICATION_GROUP_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.GROUP_IN_EXECUTION_VERIFICATION_GROUP_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.GROUP_PREDICTED_DATE_VERIFICATION_GROUP_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.ITEM_USER_VERIFICATION_GROUP_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.ITEM_WITH_TICKET_VERIFICATION_GROUP_LBL
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroup
import com.namoadigital.prj001.util.ConstantBaseApp.MAX_DATE_VALUE

@Composable
fun VerificationGroupCard(
    group: VerificationGroup,
    translateMap: TranslateMap,
    onSwitchChange: (Boolean) -> Unit
) {

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(NamoaTheme.spacing.mediumSmall))
            .clickable(enabled = group.canToggle) {
                onSwitchChange(!group.isActive)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(NamoaTheme.spacing.extraSmall)
        ) {
            when {
                group.expired -> {
                    Text(
                        text = translateMap.textOf(GROUP_EXPIRED_VERIFICATION_GROUP_LBL),
                        color = Color.Red,
                        style = NamoaTheme.typography.labelMedium
                    )
                }

                group.predictedDate != null -> {
                    val value =
                        if (MAX_DATE_VALUE == group.predictedDate) "-" else context.formatDate(
                            FormatDateType.OnlyDate(group.predictedDate)
                        )
                    Text(
                        text = "${translateMap.textOf(GROUP_PREDICTED_DATE_VERIFICATION_GROUP_LBL)} $value",
                        style = NamoaTheme.typography.labelMedium
                    )
                }

                group.inExecution -> {
                    Text(
                        text = translateMap.textOf(GROUP_IN_EXECUTION_VERIFICATION_GROUP_LBL),
                        color = Color.Gray,
                        style = NamoaTheme.typography.labelMedium
                    )
                }
            }

            Spacer(Modifier.height(NamoaTheme.spacing.extraSmall))
            group.title?.let {
                Text(it, style = NamoaTheme.typography.bodyLarge)
            }
            Spacer(Modifier.height(NamoaTheme.spacing.small))
            RowNamoaBadge(balls = group.alerts)

            if (group.inExecution) {
                Spacer(modifier = Modifier.height(NamoaTheme.spacing.mediumLarge))
                Text(
                    "${translateMap.textOf(ITEM_WITH_TICKET_VERIFICATION_GROUP_LBL)} ${group.ticket}, ${
                        translateMap.textOf(
                            ITEM_USER_VERIFICATION_GROUP_LBL
                        )
                    } ${group.user}",
                    style = NamoaTheme.typography.labelMedium
                )
            }
        }

        ApplicationSwitch(
            isEnabled = group.canToggle,
            isChecked = group.isActive,
            onCheckedChange = {
                onSwitchChange(!group.isActive)
            }
        )
    }
}
