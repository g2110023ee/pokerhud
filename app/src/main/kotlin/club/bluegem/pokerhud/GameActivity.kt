package club.bluegem.pokerhud

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import club.bluegem.pokerhud.databinding.GameBinding
import club.bluegem.pokerhud.databinding.ListviewHuditemBinding
import kotlinx.android.synthetic.main.game.*
import kotlinx.android.synthetic.main.listview_huditem.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash

class GameActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)
        //Handカウントの初期化
        val playHand = Hand()
        //Bindingの起動（Handカウント用）
        val binding : GameBinding = DataBindingUtil.setContentView(this, R.layout.game)
        binding.hand= playHand

        //Making players
        /*
        ** TODO:
        ** Playerの最大人数はgetMaxPlayer()をコールすることで取得するように修正（済）
        ** Conf画面からMaxPlayerの値を変更できるように実装する
        **/
        val maxPlayer = getMaxPlayer()
        val players = mutableListOf(Player(seatNumber = "1"))
        for(i in 2..maxPlayer){
            players.add(Player(seatNumber = "$i"))
        }

        //TEST用のコード
        players[1].playerStatus=true
        players[8].playerStatus=true

        //HudAdapterによりPlayerListをList化
        val adapter = HudAdapter(this,players)
        PlayerList.adapter = adapter

        //Buttonのそれぞれ機能
        //Reset all
        val reset_button: Button = button_reset
        reset_button.setOnClickListener { v ->
            if (v != null) playHand.resetHand()
            playerResetHand(players)
            binding.hand= playHand
            adapter.notifyDataSetChanged()
        }

        //Next hand
        val next_button: Button = button_nexthand
        next_button.setOnClickListener { v ->
            if (v != null) playHand.addHand()
            playerNextHand(players)
            binding.hand= playHand
            adapter.notifyDataSetChanged()
        }
    }

    /***
     * 設定ファイルから最大プレーヤー数を取得するメソッド
     * 現在はハードコーディング
     * TODO:
     * 設定画面を実装の上、設定から取得できるようにする
     *
     * @return maxPlayer: 設定上の最大人数
     ***/
    fun getMaxPlayer(): Int {
        val maxPlayer= 9
        return maxPlayer
    }

    /***
     * 次のハンドへ進むときの処理を全て代行するメソッド
     * ・各プレーヤーのハンドカウントを進めます
     * ・各プレーヤーの各種数字を計算します
     *  @param player: Player型のListを要求します
     *
     */
    fun playerNextHand(player: List<Player>){
        for(i in 0..player.size-1) if(player[i].playerStatus) {
            player[i].addHand()
            player[i].calc()
            if(player[i].dealerButton){
                player[i].addDealerButton()
            }

        }
    }

    /***
     * ハンド履歴をリセットするときの処理を全て代行するメソッド
     * ・各プレーヤーのハンド履歴を葬ります
     *  @param player: Player型のListを要求します
     */
    fun playerResetHand(player: List<Player>){
        for(i in 0..player.size-1) player[i].resetAll()
    }
}

/***
 * オリジナルのリストを表示するためにArrayAdapterをラッパーしたクラス
 * @param context: Contextを要求します
 * @param players: Player型のListを要求します
 * @property
 */
class HudAdapter(context: Context, val players: List<Player>) : ArrayAdapter<Player>(context, 0, players) {
    var binding:ListviewHuditemBinding? = null
    var inflater: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int = players.size

    /***
     * TODO:
     * CallとRaiseのボタン処理をどうするか考える
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        if (convertView == null) {
            binding = ListviewHuditemBinding.inflate(inflater, parent, false)
            binding?.root?.tag = binding
        }else{
            binding = convertView.tag as ListviewHuditemBinding
        }
        binding?.player = getItem(position)
        //Go to Result activity
        binding?.toggleCall?.setOnCheckedChangeListener { _, _ -> players[position].addCalledHand() }
        binding?.toggleRaise?.setOnCheckedChangeListener { _,_ ->
            players[position].addRaisedHand()
            if(players[position].dealerButton) players[position].addDealerButtonRaised()
        }
        return binding?.root
    }
}

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

    ){
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
        vpipCalculation = calcedVpip.toString()

        //Pre-Flop-Raise
        val calcedPfr =
                ((raisedHandCount)/(playedHandCount.toFloat())) * 100
        pfrCalculation = calcedPfr.toString()
        //BlindSteal
        if(dealerButtonCount != 0) {
            val calcedBlindsteal =
                    (dealerButtonRaisedCount / dealerButtonCount.toFloat()) * 100
            blindstealCalculation = calcedBlindsteal.toString()
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