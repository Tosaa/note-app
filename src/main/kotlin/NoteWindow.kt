import androidx.compose.ui.window.WindowState
import java.io.File

data class NoteWindow(
    val file: File,
    var text: String = "",
    var isOpen: Boolean = false,
) {
    val id = file.hashCode()

    fun save(filename: String? = null, content: String? = null) {
        if (!content.isNullOrEmpty()){
            this.text = content
            if (!file.exists()) {
                file.createNewFile()
            }
            file.writeText(text)
            println("Content (${text.count()}) saved into ${file.name}")
        }
        if (file.name != filename && !filename.isNullOrEmpty()) {
            val oldFileName = file.name
            val suffix = if (!filename.endsWith(".txt")) ".txt" else ""
            file.renameTo(File(file.parentFile, filename + suffix))
            println("File renamed: $oldFileName -> ${filename + suffix}")
        }
    }
}