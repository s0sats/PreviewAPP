import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.ui.act011.group_verification.composable.components.badge.model.NamoaBadges


@Composable
fun Int?.toComposeColor(): Color {
    return this?.let {
        colorResource(it)
    } ?: Color.Unspecified
}

@Composable
fun RowNamoaBadge(
    modifier: Modifier = Modifier,
    balls: List<NamoaBadges>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(NamoaTheme.spacing.small),
    ) {
        balls.forEachIndexed { index, (resourceColor, number) ->
            NamoaBadge(number = number, color = GeOsDeviceItemStatusColor.toColor(resourceColor).toComposeColor())
        }
    }
}

@Composable
private fun NamoaBadge(number: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(NamoaTheme.spacing.large)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = NamoaTheme.typography.bodySmall
        )
    }
}

fun getContrastingTextColor(background: Color): Color {
    val luminance = (0.299 * background.red + 0.587 * background.green + 0.114 * background.blue)
    return if (luminance > 0.5) Color.Black else Color.White
}
