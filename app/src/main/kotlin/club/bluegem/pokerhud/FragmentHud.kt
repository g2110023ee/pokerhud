package club.bluegem.pokerhud

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import club.bluegem.pokerhud.databinding.FragmentHudBinding
import club.bluegem.pokerhud.databinding.ListviewHuditemBinding
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_hud.*
import java.io.Serializable
import java.util.*

class FragmentHud() : Fragment() {
    var saveStatus:Bundle? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_hud, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?)  {
        super.onActivityCreated(savedInstanceState)
        //Handカウントの初期化
        val playHand = Hand(handID = UUID.randomUUID().toString())
        //Bindingの起動（Handカウント用）
        val binding: FragmentHudBinding = FragmentHudBinding.bind(view)
        binding.hand = playHand
        /*
        ** TODO:
        ** Playerの最大人数はgetMaxPlayer()をコールすることで取得するように修正（済）
        ** Conf画面からMaxPlayerの値を変更できるように実装する
        **/
        val confMap = getConfig()
        val maxPlayer = confMap["MaxPlayer"] as Int
        val playerPosition = confMap["PlayerPosition"] as Int
        val buttonPosition = confMap["ButtonPosition"] as Int
        val players: MutableList<Player> = mutableListOf(Player(seatNumber = 1))
        (2..maxPlayer).forEach { players.add(Player(seatNumber = it)) }
        setDealerPlayer(playerPosition,buttonPosition,players)
        playerResetHand(players)
        //HudAdapterによりPlayerListをList化
        val adapter = HudAdapter(context, players)
        PlayerList.adapter = adapter
        adapter.notifyDataSetChanged()
        //Buttonのそれぞれ機能
        //Reset all
        val reset_button: Button = button_reset
        reset_button.setOnClickListener {
            playerResetHand(players)
            playHand.resetHand(handID = UUID.randomUUID().toString())
            binding.hand= playHand
            adapter.notifyDataSetChanged()
        }

        //Next hand
        val next_button: Button = button_nexthand
        next_button.setOnClickListener {
            saveStatus?.putSerializable("player",players as Serializable)
            Log.d("Serializable",saveStatus.toString())
            playHand.addHand()
            playerNextHand(players,playHand.handID)
            binding.hand= playHand
            adapter.notifyDataSetChanged()
        }
    }

    private fun  setDealerPlayer(playerPosition: Int, buttonPosition: Int, players: MutableList<Player>) {
        for(i in 0..players.lastIndex){
            if((i+1)==playerPosition){
                players[i].whoIsMe=true
                players[i].playerStatus =true
            }
            if((i+1)==buttonPosition){
                players[i].dealerButton=true
                players[i].playerStatus =true
            }
        }
    }

    /***
     * 設定ファイルから最大プレーヤー数を取得するメソッド
     *
     * @return confMap: 設定情報
     ***/
    fun getConfig(): Map<String, Int> {
        val data: SharedPreferences = this.activity.getSharedPreferences("config", Context.MODE_PRIVATE)
        val maxPlayer = data.getInt("MaxPlayer",9)
        val playerPosition = data.getInt("PlayerPosition",1)
        val buttonPosition = data.getInt("ButtonPosition",1)
        val confMap = mapOf("MaxPlayer" to maxPlayer, "PlayerPosition" to playerPosition,"ButtonPosition" to buttonPosition)
        return confMap
    }

    /***
     * 次のハンドへ進むときの処理を全て代行するメソッド
     * ・各プレーヤーのハンドカウントを進めます
     * ・各プレーヤーの各種数字を計算します
     *  @param player: Player型のListを要求します
     *
     */
    fun playerNextHand(player: List<Player>, handID: String) {
        checkDealerButtonStatus(player)
        player.forEach {
            if(it.playerStatus){
                it.addHand()
                it.calc()
                if(it.dealerButton) it.addDealerButton()
                if(it.whoIsMe) updateUserResult(handID,it)
            }
        }

    }

    fun updateUserResult(handID: String,player:Player){
        val realm = Realm.getDefaultInstance()
        realm.use { realms ->
            realms.executeTransaction {
                val realmDbObj:ResultHolder = ResultHolder()
                realmDbObj.handID = handID
                realmDbObj.playedHandCount = player.playedHandCount
                realmDbObj.vpipCalculation = player.vpipCalculation
                realmDbObj.pfrCalculation = player.pfrCalculation
                realmDbObj.blindstealCalculation = player.blindstealCalculation
                realms.copyToRealmOrUpdate(realmDbObj)
            }
        }

    }

    fun checkDealerButtonStatus(players: List<Player>) {
        for (i in 0..players.lastIndex) {
            if (players[i].dealerButton) {
                for (j in i..players.lastIndex) {
                    if (j == players.lastIndex) {
                        for (k in 0..i) {
                            if (players[k].playerStatus) {
                                players[i].dealerButton = false
                                players[k].dealerButton = true
                                return
                            }
                        }
                    } else {
                        if (players[j + 1].playerStatus) {
                            players[i].dealerButton = false
                            players[j + 1].dealerButton = true
                            return
                        }
                    }
                }
            }
        }
    }
    /***
     * ハンド履歴をリセットするときの処理を全て代行するメソッド
     * ・各プレーヤーのハンド履歴を葬ります
     *  @param player: Player型のListを要求します
     */
    fun playerResetHand(player: List<Player>) {
        player.forEach {
            it.resetAll()
            it.isNeedReset =false
        }
    }

    /***
     * オリジナルのリストを表示するためにArrayAdapterをラッパーしたクラス
     * @param context: Contextを要求します
     * @param players: Player型のListを要求します
     * @property
     */
    class HudAdapter(context: Context, val players: List<Player>) : ArrayAdapter<Player>(context, 0, players) {
        var binding: ListviewHuditemBinding? = null
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
            } else {
                binding = convertView.tag as ListviewHuditemBinding
            }
            binding?.player = getItem(position)
            val seatedPlayer = players[position]
            if(!seatedPlayer.playerStatus && binding?.switchSeated?.isChecked==true) binding?.switchSeated?.isChecked=false
            if(seatedPlayer.playerStatus && seatedPlayer.whoIsMe&& binding?.switchSeated?.isChecked==false) binding?.switchSeated?.isChecked=true
            if(seatedPlayer.playerStatus && seatedPlayer.dealerButton&& binding?.switchSeated?.isChecked==false) binding?.switchSeated?.isChecked=true
            binding?.switchSeated?.setOnCheckedChangeListener { _, _ ->
                if(seatedPlayer.isNeedReset) seatedPlayer.changeStatus()
            }
            binding?.toggleCall?.setOnClickListener {
                if(seatedPlayer.playerStatus&&!seatedPlayer.isPlayed) {
                    seatedPlayer.information="Called"
                    seatedPlayer.isPlayed=true
                    seatedPlayer.addCalledHand()
                    notifyDataSetChanged()
                }
            }
            binding?.toggleRaise?.setOnClickListener {
                if (seatedPlayer.playerStatus&&!seatedPlayer.isPlayed) {
                    seatedPlayer.information="Raised"
                    seatedPlayer.isPlayed=true
                    seatedPlayer.addRaisedHand()
                    if (seatedPlayer.dealerButton) seatedPlayer.addDealerButtonRaised()
                    notifyDataSetChanged()
            }
            }
            if(!seatedPlayer.isNeedReset) seatedPlayer.isNeedReset = true
            return binding?.root
        }
    }
}