import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteWindow.window(appState: AppState, keyEventHandler: (NoteWindow, KeyEvent) -> Boolean) {
    val fileName = remember { mutableStateOf(file.name) }
    val state = rememberRichTextState()
    remember(appState) { state.setText(text) }
    Window(
        onCloseRequest = {
            save(filename = fileName.value, content = state.toMarkdown())
            appState.closeWindow(this)
        },
        title = file.name,
        onKeyEvent = {
            when {
                keyEventHandler(this, it) -> {
                    println("KeyEvent is handled by the ApplicationKeyEventHandler - ${this.file.name}")
                    true
                }

                it.isSave -> {
                    println("KeyEvent is save - ${this.file.name}")
                    save(filename = fileName.value, content = state.toMarkdown())
                    true
                }

                it.isClose -> {
                    println("KeyEvent is close - ${this.file.name}")
                    save(filename = fileName.value, content = state.toMarkdown())
                    appState.closeWindow(this)
                    true
                }

                else ->
                    false
            }
        }
    ) {
        val focusManager = LocalFocusManager.current
        Column {
            OutlinedTextField(
                value = fileName.value,
                onValueChange = { fileName.value = it.replace(" ", "_") },
                label = { Text("FileName") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            )
            Divider()
            RichTextEditor(
                state = state,
                modifier = Modifier.fillMaxSize().onKeyEvent { keyEvent ->
                    if (keyEvent.isCmdTab || keyEvent.isCmdEnter) {
                        focusManager.moveFocus(FocusDirection.Previous)
                    }
                    false
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Previous)
                })
            )
        }
    }
}