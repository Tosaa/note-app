import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File

// val defaultDirectory = System.getProperty("user.home") + "/Documents"
val defaultDirectory = "/Users/antonsaatze/IdeaProjects/NoteApp/files"

@Composable
fun ApplicationScope.TextEditorApp(appState: AppState, keyEventHandler: (KeyEvent) -> Boolean) {
    MaterialTheme {
        Window(::exitApplication, onKeyEvent = { event -> keyEventHandler(event) }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val query = remember { mutableStateOf("") }
                val matchingFiles = appState.queriedFiles
                Column {
                    Card(Modifier.padding(5.dp)) {
                        LazyColumn {
                            item { CustomSearchbar(query, appState) }
                            items(matchingFiles) {
                                FocusableBox(it.name, boxModifier = Modifier.fillMaxWidth()) {
                                    appState.openFile(it)
                                    appState.searchFile("")
                                    query.value = ""
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Enter a search query to search for files", Modifier.fillMaxWidth().padding(5.dp))
                    Text("Press CMD + N to open a new window", Modifier.fillMaxWidth().padding(5.dp))
                    Text("Press CMD + F to focus this search window", Modifier.fillMaxWidth().padding(5.dp))
                    Text("Press CMD + X to save and close an open window", Modifier.fillMaxWidth().padding(5.dp))
                    Text("Press CMD + S to save an open window", Modifier.fillMaxWidth().padding(5.dp))
                }
            }
        }
    }
}


fun main() = application {
    val appState = remember {
        AppState().also {
            it.setDefaultDir(File(defaultDirectory))
        }
    }

    val applicationKeyEventHandler: (KeyEvent) -> Boolean = {
        when {
            it.type == KeyEventType.KeyDown -> {
                // Only handle KeyEventType.KeyUp
                false
            }

            it.isNew -> {
                println("KeyEvent is new File")
                appState.createNewFile()
                true
            }

            else ->
                false
        }
    }
    TextEditorApp(appState = appState, keyEventHandler = applicationKeyEventHandler)
    appState.allWindows.forEach { window ->
        key(window.id) {
            window.window(appState) { noteWindow, keyEvent -> applicationKeyEventHandler(keyEvent) }
        }
    }
}