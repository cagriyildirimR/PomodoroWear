package pomodoroapp.cagriyildirim.com.github.pomodorowear

import android.app.Application
import pomodoroapp.cagriyildirim.com.github.pomodorowear.database.PomodoroDatabase

class PomodoroMobileApp: Application() {

    val database by lazy { PomodoroDatabase.getDatabase(this) }
}