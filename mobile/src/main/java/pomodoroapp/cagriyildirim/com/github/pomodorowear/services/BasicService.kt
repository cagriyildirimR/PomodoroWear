package pomodoroapp.cagriyildirim.com.github.pomodorowear.services

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import pomodoroapp.cagriyildirim.com.github.pomodorowear.TESTING_TAG

class BasicService: WearableListenerService() {
    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        when (messageEvent.path) {
            TEST_LOG_PATH -> { Log.i(TESTING_TAG, "Basic Service is running. Data: ${String(messageEvent.data)}")}
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