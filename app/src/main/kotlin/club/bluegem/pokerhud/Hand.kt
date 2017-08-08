package club.bluegem.pokerhud

import java.util.*

/***
 * Handを管理するデータクラス
 * @property handCount: Int テーブルでの経過ハンド数
 */
data class Hand(var handCount:Int = 1,var handID:String ){

    fun addHand(){
        handCount++
    }
    fun resetHand(handID:String){
        handCount = 1
        this.handID = handID
    }
}
