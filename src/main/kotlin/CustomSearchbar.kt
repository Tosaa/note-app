import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun CustomSearchbar(query: MutableState<String>, appState: AppState) =
    OutlinedTextField(
        query.value, {
            query.value = it
            appState.searchFile(it)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )