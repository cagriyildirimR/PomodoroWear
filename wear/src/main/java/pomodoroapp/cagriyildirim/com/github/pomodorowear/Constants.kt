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
    val timeEnd: Long,
    val taskType: Int,
)

const val START_TIME_KEY = "stk"
const val END_TIME_KEY = "etk"
const val SECOND = 1000L
const val MINUTE = 60_000L
const val ONE_POMODORO = 24 * MINUTE

const val TEST_LOG_PATH = "/test-log"
const val POMODORO_CAPABILITY = "pomodoro_capable"
