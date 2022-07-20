package pomodoroapp.cagriyildirim.com.github.pomodorowear.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "pomodoro_table")
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val timeStart: Long,
    val timeEnd: Long,
    val taskType: Int,
) {
    fun durationInMinute(): Long {
        return (timeStart - timeEnd) / 60_000
    }
}

enum class TaskTypes {
    STUDY, WORKOUT, BREAK
}