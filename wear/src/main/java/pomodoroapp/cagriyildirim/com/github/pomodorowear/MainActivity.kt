package pomodoroapp.cagriyildirim.com.github.pomodorowear

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import pomodoroapp.cagriyildirim.com.github.pomodorowear.broadcastReceivers.NotificationBroadcast
import pomodoroapp.cagriyildirim.com.github.pomodorowear.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60_000L
const val ONE_POMODORO = 3000L //24 * MINUTE

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var timer: CountDownTimer

    private val scope = CoroutineScope(Dispatchers.Main)
    private val nodeClient by lazy { Wearable.getNodeClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var time = ONE_POMODORO
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        binding.startButton.setOnClickListener { startButton ->

            val broadcastIntent = Intent(this, NotificationBroadcast::class.java).apply {
                putExtra(NOTIFICATION_MESSAGE_KEY, "I hope this works")
            }
            val pendingBroadcastIntent = PendingIntent.getBroadcast(
                this,
                0,
                broadcastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            /** start button has two states. Start (not selected) and Pause (selected).
             * When button is in selected state, time is running and button shows Pause icon image.
             * When button is not selected state, time stops and button shows Play icon image
             */
            if (startButton.isSelected) { // What is the default value of image buttons seleceted field?
                Log.i(TESTING_TAG, "timer is stopped")
                startButton.isSelected = false
                timer.cancel()
                alarmManager.cancel(pendingBroadcastIntent)

            } else {
                Log.i(TESTING_TAG, "timer is set to $time")
                timer = timer(time,
                    binding.timer,
                    doOnTick = { remaining ->
                        time = remaining
                    },
                    doOnFinish = {
                        time = ONE_POMODORO
                        startButton.isSelected = false // resetting button image to Play icon
                    })

                startButton.isSelected = true
                timer.start()

                Log.i("testing", "Creating notification")
                alarmManager.setExact(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + time,
                    pendingBroadcastIntent
                )
            }
        }

        binding.testing.setOnClickListener {
            Log.i(TESTING_TAG, "Startin to send request to Phone to start BasicService")
            startServiceInPhone()
        }
    }

    /**
     * Creates CountDownTimer
     * @param time time from countdown starts
     * @param textView TextView we change to show timer countdown
     * @param doOnTick callback to get remaining time
     * @param doOnFinish callback to reset timer
     */
    private fun timer(
        time: Long,
        textView: TextView,
        doOnTick: (remainingTime: Long) -> Unit,
        doOnFinish: () -> Unit
    ): CountDownTimer {
        return object : CountDownTimer(time, SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                textView.text = SimpleDateFormat("mm:ss", Locale.ENGLISH).format(millisUntilFinished)
                doOnTick(millisUntilFinished)
            }

            override fun onFinish() {
                Log.i(TESTING_TAG, "timer onFinish method is called")
                textView.text = "Fin"
                doOnFinish()
            }
        }
    }

    private fun startServiceInPhone() {
        scope.launch {
            try {
                val nodes = nodeClient.connectedNodes.await()
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(node.id, TEST_LOG_PATH, "Hello".toByteArray()).await()
                    }
                }.awaitAll()

                Log.i(TESTING_TAG, "Service start Request send successfully")
            } catch (e: CancellationException) {
                throw  e
            } catch (e: Exception) {
                Log.i(TESTING_TAG, "Service start failed with exception: $e")
            }
        }
    }

    companion object {
        private const val TEST_LOG_PATH = "/test-log"
    }
}