package pomodoroapp.cagriyildirim.com.github.pomodorowear

import kotlinx.serialization.Serializable

const val TESTING_TAG = "pomodoro.app.testing.tag"

const val ASSET_KEY = "ak"
const val DATA_REQUEST_PATH = "/drp"
const val NOTIFICATION_MESSAGE_KEY = "nmk"

@Serializable
data class PomodoroEntity(
    var id: Int = 0,
    val timeStart: Long,
    val endTime: Long,
    val taskType: Int,
)