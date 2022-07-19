package pomodoroapp.cagriyildirim.com.github.pomodorowear.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "pomodoro_table")
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val timeStart: Long,
    val endTime: Long,
    val taskType: Int,
)

enum class TaskTypes {
    STUDY, WORKOUT, BREAK
}