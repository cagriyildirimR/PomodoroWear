package pomodoroapp.cagriyildirim.com.github.pomodorowear

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pomodoroapp.cagriyildirim.com.github.pomodorowear.database.PomodoroDatabase
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
            database.pomodoroDao().getAll().collect {
                binding.info.text = it.toString() ?:"Empty List"
            }
        }
    }
}
