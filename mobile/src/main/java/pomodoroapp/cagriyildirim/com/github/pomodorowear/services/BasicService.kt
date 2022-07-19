package pomodoroapp.cagriyildirim.com.github.pomodorowear.services

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pomodoroapp.cagriyildirim.com.github.pomodorowear.PomodoroMobileApp
import pomodoroapp.cagriyildirim.com.github.pomodorowear.TESTING_TAG
import pomodoroapp.cagriyildirim.com.github.pomodorowear.database.PomodoroEntity

class BasicService : WearableListenerService() {

    private val scope = CoroutineScope(Dispatchers.Default)

    @OptIn(ExperimentalSerializationApi::class)
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        when (messageEvent.path) {
            TEST_LOG_PATH -> {
                Log.i(TESTING_TAG, "Basic Service is running. Data: ${String(messageEvent.data)}")

                val pomodoroEntity = Json.decodeFromString<PomodoroEntity>(String(messageEvent.data))
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