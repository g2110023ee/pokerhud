package club.bluegem.pokerhud

/***
 * Handを管理するデータクラス
 * @property handCount: Int テーブルでの経過ハンド数
 */
data class Hand(var handCount:String = "1"){
    fun addHand(){
        val hands: Int = handCount.toInt()+1
        handCount="${hands}"
    }
    fun resetHand(){
        handCount = "1"
    }
}
