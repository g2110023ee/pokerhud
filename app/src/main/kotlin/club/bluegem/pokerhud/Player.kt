package club.bluegem.pokerhud

import io.realm.Realm

/**
 * Playerの情報を全て管理するデータクラス
 */
data class Player(
        val seatNumber: Int,
        var dealerButton: Boolean = false,
        var playerStatus: Boolean = false,
        var whoIsMe: Boolean = false,
        var isNeedReset:Boolean = true,
        var isPlayed:Boolean = false,
        var playedHandCount: Int = 0,
        var calledHandCount: Int = 0,
        var raisedHandCount: Int = 0,
        var dealerButtonCount: Int = 0,
        var dealerButtonRaisedCount: Int = 0,
        var vpipCalculation: Float = 0.0f,
        var pfrCalculation: Float = 0.0f,
        var blindstealCalculation: Float = 0.0f,
        var information: String =""

) {
    /***
     * 着席ステータスの変更に利用する
     */
    fun changeStatus(){
        this.playerStatus=!this.playerStatus
    }
    /**
     * プレーヤーのプレイハンド数をインクリメントするメソッド
     * Bindingの制約でInt型が使えない為、
     * 一旦Int型にキャストしてインクリメントしてから再度Stringに変換
     */
    fun addHand() {
        this.playedHandCount++
        this.isPlayed=false
        this.information = ""
        if(this.whoIsMe) this.information = "You(Player)"
        if(this.dealerButton) this.information = "Button"
        if(this.whoIsMe&&this.dealerButton) this.information = "You are Button"
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
     **/
    fun calc() {
        //VPIP
        val calcedVpip =
                ((calledHandCount + raisedHandCount)/(playedHandCount.toFloat())) * 100
        vpipCalculation = calcedVpip

        //Pre-Flop-Raise
        val calcedPfr =
                ((raisedHandCount)/(playedHandCount.toFloat())) * 100
        pfrCalculation = calcedPfr
        //BlindSteal
        if(dealerButtonCount != 0) {
            val calcedBlindsteal =
                    (dealerButtonRaisedCount / dealerButtonCount.toFloat()) * 100
            blindstealCalculation = calcedBlindsteal
        }
    }

    /**
     * プレーヤーデータのリセットが必要になった場合リセットを行うメソッド
     */
    fun resetAll(){
        playerStatus = false
        playedHandCount = 0
        calledHandCount = 0
        raisedHandCount = 0
        dealerButtonCount = 0
        dealerButtonRaisedCount = 0
        vpipCalculation = 0.0f
        pfrCalculation = 0.0f
        blindstealCalculation = 0.0f
        isNeedReset =false
        information =""
        if(whoIsMe){
            information = "You(Player)"
            playerStatus = true
        }
        if(dealerButton){
            information = "Button"
            playerStatus = true
        }
        if(whoIsMe&&dealerButton){
            information = "You are Button"
            playerStatus = true
        }
    }

}
