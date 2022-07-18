package pomodoroapp.cagriyildirim.com.github.pomodorowear.broadcastReceivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.wearable.*
import pomodoroapp.cagriyildirim.com.github.pomodorowear.*
import pomodoroapp.cagriyildirim.com.github.pomodorowear.R

class NotificationBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TESTING_TAG, "Notification Broadcast receiver onReceive method is called")

        val message = intent.getStringExtra(NOTIFICATION_MESSAGE_KEY) ?: "Hello World"

        createAndLaunchNotification(context, message)
    }

    private fun createAndLaunchNotification(context: Context, message: String) {
        Log.i(TESTING_TAG, "create notification with message: $message")

        val name = "DefaultName"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val id = "DefaultId"
        val anotherId = "WrongId"

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val mChannel = NotificationChannel(id, name, importance).apply {
            vibrationPattern = LongArray(10) { 400L }
        }
        notificationManager.createNotificationChannel(mChannel)

        val builder = NotificationCompat.Builder(context, anotherId).apply {
            setSmallIcon(R.drawable.icon_alarm_on)
            setContentTitle("Pomodoro finished")
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
        }

        builder.setChannelId(id)

        notificationManager.notify(0, builder.build())
        Log.i(TESTING_TAG, "Notification notified")
    }
}
