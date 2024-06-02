import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusableBox(
    text: String = "",
    boxModifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val keyPressedState = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = if (interactionSource.collectIsFocusedAsState().value) {
        if (keyPressedState.value)
            lerp(MaterialTheme.colors.primaryVariant, Color(64, 64, 64), 0.3f)
        else
            MaterialTheme.colors.primaryVariant
    } else {
        Color.Transparent
    }
    Box(
        modifier = boxModifier
            .border(2.dp, MaterialTheme.colors.primary)
            .background(backgroundColor)
            .onPointerEvent(PointerEventType.Press) { onClick() }
            .onPreviewKeyEvent {
                if (
                    it.key == Key.Enter ||
                    it.key == Key.Spacebar
                ) {
                    when (it.type) {
                        KeyEventType.KeyDown -> {
                            keyPressedState.value = true
                        }

                        KeyEventType.KeyUp -> {
                            keyPressedState.value = false
                            onClick.invoke()
                        }
                    }
                }
                false
            }
            .focusable(interactionSource = interactionSource),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = MaterialTheme.colors.contentColorFor(backgroundColor), modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
    }
}