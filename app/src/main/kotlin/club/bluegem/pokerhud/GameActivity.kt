package club.bluegem.pokerhud

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import club.bluegem.pokerhud.databinding.GameBinding
import club.bluegem.pokerhud.databinding.ListviewHuditemBinding
import kotlinx.android.synthetic.main.game.*
import kotlinx.android.synthetic.main.listview_huditem.*

class GameActivity : AppCompatActivity() {
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
        ** Playerの数を決め打ちで9人（大体のホールデムは9人MAX）にしているが
        ** Conf画面を作ってユーザに決めさせたほうがよいのかもしれない
        ** ないしは、IF上にPlayerを増やすボタンを設置する？
        **/
        val players = mutableListOf(Player(seatNumber = "1"))
        for(i in 2..9){
            players.add(Player(seatNumber = "$i"))
        }

        //TEST用のコード
        players[1].status=true
        players[8].status=true

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
            Log.d("Reset", "onClick")
        }
        //Next hand
        val next_button: Button = button_nexthand
        next_button.setOnClickListener { v ->
            if (v != null) playHand.addHand()
            players[1].addCalledHand()
            players[8].addRaisedHand()
            playerNextHand(players)
            binding.hand= playHand
            adapter.notifyDataSetChanged()
        }
    }

    /***
     * 次のハンドへ進むときの処理を全て代行するメソッド
     * ・各プレーヤーのハンドカウントを進めます
     * ・各プレーヤーの各種数字を計算します
     */
    fun playerNextHand(player: List<Player>){
        for(i in 0..player.size-1) if(player[i].status) {
            player[i].addHand()
            player[i].calc()
            if(player[i].dealer){
                player[i].addDealerButton()
                if(player[i].status){
                    player[i].addDealerButtonRaised()
                }
            }

        }
    }

    /***
     * ハンド履歴をリセットするときの処理を全て代行するメソッド
     * ・各プレーヤーのハンド履歴を葬ります
     */
    fun playerResetHand(player: List<Player>){
        for(i in 0..player.size-1) player[i].resetAll()
    }
}

/***
 * オリジナルのリストを表示するためにArrayAdapterをラッパーしたクラス
 * ・Bindingによる動的な変更に対応
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
        return binding?.root
    }
}

/***
 * Handを管理するデータクラス
 * class:Hand
 * 変数:handCount(String)のみ
 */
data class Hand(var handCount:String = "1"){
    init {
        handCount = "1"
    }
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
 * class:Player
 * 変数:seatNumber(String)
 *      dealer(Boolean)
 *      status(Boolean)
 *      playedHandCount(String)
 *      calledHandCount(Int)
 *      raisedHandCount(Int)
 *      dealerButtonCount(Int)
 *      dealerButtonRaisedCount(Int)
 *      vpip(String)
 *      pfr(String)
 *      blindsteal(String)
 */
data class Player(
            val seatNumber:String,
            var dealer: Boolean = false,
            var status:Boolean = false,
            var playedHandCount:String = "0",
            var calledHandCount:Int = 0,
            var raisedHandCount:Int = 0,
            var dealerButtonCount:Int = 0,
            var dealerButtonRaisedCount:Int = 0,
            var vpip:String ="0",
            var pfr:String = "0",
            var blindsteal:String = "0"

    ){
    /***
     * 着席ステータスの変更に利用する
     */
    fun changeStatus(){
        if(this.status == false) this.status = true
        if(this.status == true) this.status = false
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
     */
    fun calc() {
        //VPIP
        val calcedVpip =
                ((calledHandCount + raisedHandCount)/(playedHandCount.toInt())) * 100
        vpip = calcedVpip.toString()
        //Pre-Flop-Raise
        val calcedPfr =
                ((raisedHandCount)/(playedHandCount.toInt())) * 100
        pfr = calcedPfr.toString()
        //BlindSteal
        if(dealerButtonCount != 0) {
            val calcedBlindsteal =
                    (dealerButtonRaisedCount / dealerButtonCount) * 100
            blindsteal = calcedBlindsteal.toString()
        }
    }

    /**
     * プレーヤーデータのリセットが必要になった場合リセットを行うメソッド
     */
    fun resetAll(){
        status = false
        playedHandCount = "0"
        calledHandCount = 0
        raisedHandCount = 0
        dealerButtonCount = 0
        dealerButtonRaisedCount = 0
        vpip = "0"
        pfr = "0"
        blindsteal = "0"
    }
}