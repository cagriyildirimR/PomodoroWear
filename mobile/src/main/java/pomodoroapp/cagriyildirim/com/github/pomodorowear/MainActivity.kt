package pomodoroapp.cagriyildirim.com.github.pomodorowear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.wearable.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import pomodoroapp.cagriyildirim.com.github.pomodorowear.coroutines.scope
import pomodoroapp.cagriyildirim.com.github.pomodorowear.database.PomodoroDatabase
import pomodoroapp.cagriyildirim.com.github.pomodorowear.databinding.ActivityMainBinding
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //    private val scope = CoroutineScope( Dispatchers.Default )
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
                binding.info.text = it.size.toString() ?:"Empty List"
            }
        }

    }

}