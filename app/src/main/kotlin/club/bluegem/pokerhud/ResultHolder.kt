package club.bluegem.pokerhud

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

open class ResultHolder : RealmObject() {
    @PrimaryKey
    var handID: String = ""

    var playedHandCount: Int = 0
    var vpipCalculation: Float = 0.0f
    var pfrCalculation: Float = 0.0f
    var blindstealCalculation: Float = 0.0f
    var date: String = SimpleDateFormat("yy/MM/dd").format(Date())

    @Ignore
    var sessionId: Int = 0
}