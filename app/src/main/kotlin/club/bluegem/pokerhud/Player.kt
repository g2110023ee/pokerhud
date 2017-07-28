package club.bluegem.pokerhud

/****
 * Playerの情報を全て管理するデータクラス
 */
data class Player(
        val seatNumber: String,
        var dealerButton: Boolean = false,
        var playerStatus: Boolean = false,
        var whoIsMe: Boolean = false,
        var playedHandCount: String = "0",
        var calledHandCount: Int = 0,
        var raisedHandCount: Int = 0,
        var dealerButtonCount: Int = 0,
        var dealerButtonRaisedCount: Int = 0,
        var vpipCalculation: String ="0",
        var pfrCalculation: String = "0",
        var blindstealCalculation: String = "0"

) {

    /***
     * 着席ステータスの変更に利用する
     */
    fun changeStatus(){
        when(this.playerStatus){
            true -> this.playerStatus = false
            false -> this.playerStatus = true
        }
    }
    /**
     * プレーヤーのプレイハンド数をインクリメントするメソッド
     * Bindingの制約でInt型が使えない為、
     * 一旦Int型にキャストしてインクリメントしてから再度Stringに変換
     */
    fun addHand(){
        this.playedHandCount = (this.playedHandCount.toInt()+1).toString()
    }

    fun addCalledHand(){
        this.calledHandCount++
    }
    fun addRaisedHand(){
        this.raisedHandCount++
    }
    fun addDealerButton(){
        this.dealerButtonCount++
    }
    fun addDealerButtonRaised(){
        this.dealerButtonRaisedCount++
    }

    /**
     * プレーヤーデータの計算を行うメソッド
     * VPIP=（コールしたハンド数＋レイズしたハンド数）／総ハンド数
     * PFR=レイズしたハンド数／総ハンド数
     * BlindSteal=ディーラーボタンでレイズしたハンド数/ディーラーボタン回数
     *
     */
    fun calc() {
        //VPIP
        val calcedVpip =
                ((calledHandCount + raisedHandCount)/(playedHandCount.toFloat())) * 100
        vpipCalculation = String.format("%.2f",calcedVpip)

        //Pre-Flop-Raise
        val calcedPfr =
                ((raisedHandCount)/(playedHandCount.toFloat())) * 100
        pfrCalculation = String.format("%.2f",calcedPfr)
        //BlindSteal
        if(dealerButtonCount != 0) {
            val calcedBlindsteal =
                    (dealerButtonRaisedCount / dealerButtonCount.toFloat()) * 100
            blindstealCalculation = String.format("%.2f",calcedBlindsteal)
        }
    }

    /**
     * プレーヤーデータのリセットが必要になった場合リセットを行うメソッド
     */
    fun resetAll(){
        playerStatus = false
        playedHandCount = "0"
        calledHandCount = 0
        raisedHandCount = 0
        dealerButtonCount = 0
        dealerButtonRaisedCount = 0
        vpipCalculation = "0"
        pfrCalculation = "0"
        blindstealCalculation = "0"
    }

}
