package pomodoroapp.cagriyildirim.com.github.pomodorowear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.wearable.*
import pomodoroapp.cagriyildirim.com.github.pomodorowear.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val list = mutableListOf<String>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.info.text = list.toString()

    }

}