import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key

val KeyEvent.isCMD :Boolean
    get() = (this.isMetaPressed || this.isCtrlPressed)

val KeyEvent.isSave: Boolean
    get() = this.isCMD && this.key == Key.S
val KeyEvent.isCmdTab: Boolean
    get() = this.isCMD && this.key == Key.Tab
val KeyEvent.isCmdEnter: Boolean
    get() = this.isCMD && this.key == Key.Enter
val KeyEvent.isSearch: Boolean
    get() = this.isCMD && this.key == Key.F
val KeyEvent.isNew: Boolean
    get() = this.isCMD && this.key == Key.N
val KeyEvent.isClose: Boolean
    get() = this.isCMD && this.key == Key.X