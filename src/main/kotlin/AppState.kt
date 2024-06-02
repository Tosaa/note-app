import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.window.WindowState
import java.io.File
import java.util.UUID
import kotlin.random.Random

class AppState {

    var defaultDirectory = File("")
    val availableFiles: List<File>
        get() = defaultDirectory.listFiles { dir, name -> name.endsWith("txt") }?.toList() ?: emptyList()

    val queriedFiles = mutableStateListOf<File>().apply {
        addAll(availableFiles)
    }

    fun setDefaultDir(file: File) {
        defaultDirectory = file
    }

    fun searchFile(searchQuery: String) {
        queriedFiles.clear()
        if (searchQuery.isNotEmpty()) {
            queriedFiles += defaultDirectory.listFiles()?.filter {
                it.name.contains(searchQuery, true) || it.readText().contains(searchQuery, true)
            } ?: emptyList()
        }
    }

    fun createNewFile() {
        var newFile = File(defaultDirectory, "${Random.nextLong()}.txt")
        while (newFile.exists()) {
            newFile = File(defaultDirectory, "${Random.nextLong()}.text")
        }
        println("createNewFile(): $newFile and open it in a new window")
        allWindows.add(NoteWindow(newFile))
    }

    fun openFile(file: File) {
        println("openFile(): $file and open it in a new window")
        allWindows.add(NoteWindow(file, file.readText()))
    }

    fun closeWindow(noteWindow: NoteWindow) {
        allWindows.removeIf { it.id == noteWindow.id }
    }

    var allWindows = mutableStateListOf<NoteWindow>()

}