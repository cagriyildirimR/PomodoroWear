package pomodoroapp.cagriyildirim.com.github.pomodorowear.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoro_table ORDER BY id ASC")
    fun getAll(): Flow<List<PomodoroEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(e: PomodoroEntity)

    @Query("SELECT * FROM pomodoro_table WHERE timeStart >= :time")
    fun today(time: Long): Flow<List<PomodoroEntity>>
}