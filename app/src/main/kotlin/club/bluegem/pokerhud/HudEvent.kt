package club.bluegem.pokerhud

import android.app.Activity
import android.util.Log

enum class HudEvent {
    EVENT1{
        override fun apply(activity: Activity){
            Log.d("Event","event1")
        }
    },
    EVENT2{
        override fun apply(activity: Activity){
            Log.d("Event","event2")
        }
    };
    abstract fun apply(activity: Activity)

}