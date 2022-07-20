package pomodoroapp.cagriyildirim.com.github.pomodorowear

import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pomodoroapp.cagriyildirim.com.github.pomodorowear.database.PomodoroDatabase
import pomodoroapp.cagriyildirim.com.github.pomodorowear.database.PomodoroEntity
import pomodoroapp.cagriyildirim.com.github.pomodorowear.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val scope = CoroutineScope( Dispatchers.Default )
    private lateinit var database: PomodoroDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        scope.launch {
            database = (application as PomodoroMobileApp).database
        }

        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            database.pomodoroDao().today(getTodayInMillis()).collect {
                binding.today.text = parsePomodoroData(it)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            database.pomodoroDao().getAll().collect {
                binding.info.text = it.toString() ?:"Empty List"
            }
        }
    }

    private fun getTodayInMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            //this[Calendar.DAY_OF_WEEK] = firstDayOfWeek
            this[Calendar.HOUR_OF_DAY] = 0
            clear(java.util.Calendar.MINUTE)
            clear(java.util.Calendar.SECOND)
            clear(java.util.Calendar.MILLISECOND)
        }
        return calendar.timeInMillis
    }

    private fun parsePomodoroData(list : List<PomodoroEntity>): String {
        if (list.isEmpty()) return "There is nothing to show"

        val totalTime = list.fold(0L) {acc, pomodoroEntity ->
            acc + pomodoroEntity.durationInMinute()
        }
        return "Today you have done ${list.size} pomodoro in total $totalTime minutes"
    }
}
