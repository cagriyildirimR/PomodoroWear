package pomodoroapp.cagriyildirim.com.github.pomodorowear.broadcastReceivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.wearable.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pomodoroapp.cagriyildirim.com.github.pomodorowear.*
import pomodoroapp.cagriyildirim.com.github.pomodorowear.R

class NotificationBroadcast : BroadcastReceiver() {
    private val scope = CoroutineScope(Dispatchers.Default)

    private lateinit var nodeClient: NodeClient
    private lateinit var messageClient: MessageClient
    private lateinit var capabilityClient: CapabilityClient

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TESTING_TAG, "Notification Broadcast receiver onReceive method is called")
        nodeClient = Wearable.getNodeClient(context)
        messageClient = Wearable.getMessageClient(context)
        capabilityClient = Wearable.getCapabilityClient(context)
        val message = intent.getStringExtra(NOTIFICATION_MESSAGE_KEY) ?: "Hello World"
        val startTime = intent.getLongExtra(START_TIME_KEY, 0L)
        val endTime = intent.getLongExtra(END_TIME_KEY, 0L)

        createAndLaunchNotification(context, message)
        startServiceInPhone(startTime, endTime)
    }

    private fun createAndLaunchNotification(context: Context, message: String) {
        Log.i(TESTING_TAG, "create notification with message: $message")

        val name = "DefaultName"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val id = "DefaultId"
//        val anotherId = "WrongId"

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val mChannel = NotificationChannel(id, name, importance).apply {
            vibrationPattern = LongArray(10) { 400L }
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(mChannel)

        val builder = NotificationCompat.Builder(context, id).apply {
            setSmallIcon(R.drawable.icon_alarm_on)
            setContentTitle("Pomodoro finished")
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
        }

        builder.setChannelId(id)

        notificationManager.notify(0, builder.build())
        Log.i(TESTING_TAG, "Notification notified")
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun startServiceInPhone(startTime: Long, endTime: Long) {
        val data = PomodoroEntity(
            timeStart = startTime,
            timeEnd = endTime,
            taskType = 0
        )
        val message = Json.encodeToString(data)

        scope.launch {
            val capabilityInfo: CapabilityInfo = capabilityClient.getCapability(
                POMODORO_CAPABILITY, CapabilityClient.FILTER_REACHABLE
            ).await()

            try {
                val nodes = nodeClient.connectedNodes.await() //
                Log.i(
                    TESTING_TAG,
                    "Capability node has ${capabilityInfo.nodes} with nodes: $nodes"
                )
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(
                            node.id,
                            TEST_LOG_PATH,
                            message.toByteArray()
                        ).apply {
                            addOnSuccessListener {
                                Log.i(TESTING_TAG, "Message successfully sent to phone")
                            }
                            addOnCompleteListener {
                                Log.i(TESTING_TAG, "Message sent to phone completed")
                            }
                            addOnFailureListener {
                                Log.i(TESTING_TAG, "Message failed with e:$it")
                            }
                        }
                            .await()
                    }
                }.awaitAll()

                Log.i(TESTING_TAG, "Service start Request send successfully")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.i(TESTING_TAG, "Service start failed with exception: $e")
            }
        }
    }
}
