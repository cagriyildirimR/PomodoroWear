package pomodoroapp.cagriyildirim.com.github.pomodorowear.services

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pomodoroapp.cagriyildirim.com.github.pomodorowear.PomodoroMobileApp
import pomodoroapp.cagriyildirim.com.github.pomodorowear.TESTING_TAG
import pomodoroapp.cagriyildirim.com.github.pomodorowear.coroutines.scope
import pomodoroapp.cagriyildirim.com.github.pomodorowear.database.PomodoroEntity

class BasicService : WearableListenerService() {
    private val messageClient by lazy { Wearable.getMessageClient(this) }

//    private val scope = CoroutineScope(Dispatchers.Default)
    //private lateinit var database: PomodoroDatabase

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        val pomodoroEntity = PomodoroEntity(
            timeStart = System.currentTimeMillis(),
            endTime = System.currentTimeMillis() + 10_000,
            taskType = 1
        )

        when (messageEvent.path) {
            TEST_LOG_PATH -> {
                Log.i(TESTING_TAG, "Basic Service is running. Data: ${String(messageEvent.data)}")

                scope.launch {
                    (application as PomodoroMobileApp).database
                        .pomodoroDao().insert(pomodoroEntity)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel("onDestroy lifecycle event is called for BasicService")
    }

    companion object {
        private const val TEST_LOG_PATH = "/test-log"
    }
}