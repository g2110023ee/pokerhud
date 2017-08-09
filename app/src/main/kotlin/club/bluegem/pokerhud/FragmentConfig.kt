package club.bluegem.pokerhud

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_config.*

class FragmentConfig : Fragment() {
    private var mainActivity:HudMainActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_config, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val confMap = getConfig()
        val maxPlayer = confMap["MaxPlayer"] as Int
        val playerPosition = confMap["PlayerPosition"] as Int
        val buttonPosition = confMap["ButtonPosition"] as Int
        text_maxplayer.text = maxPlayer.toString()
        text_player_position.text = playerPosition.toString()
        text_button_position.text = buttonPosition.toString()

        conf_max_add.setOnClickListener {
            val currentMax = text_maxplayer.text.toString().toInt()
            text_maxplayer.text = (currentMax + 1).toString()
            setSharedPref("MaxPlayer", currentMax + 1)
        }
        conf_max_dis.setOnClickListener {
            var currentMax = text_maxplayer.text.toString().toInt()
            if (currentMax > 2) {
                currentMax -= 1
                text_maxplayer.text = currentMax.toString()
                setSharedPref("MaxPlayer", currentMax)
                if ((currentMax) <= text_player_position.text.toString().toInt()) {
                    text_player_position.text = (currentMax).toString()
                    setSharedPref("PlayerPosition", currentMax)
                }
                if ((currentMax) <= text_button_position.text.toString().toInt()) {
                    text_button_position.text = (currentMax).toString()
                    setSharedPref("ButtonPosition", currentMax)
                }
            }
        }
        conf_you_add.setOnClickListener {
            val currentYour = text_player_position.text.toString().toInt()
            if (currentYour < text_maxplayer.text.toString().toInt()) {
                setSharedPref("PlayerPosition", currentYour + 1)
                text_player_position.text = (currentYour + 1).toString()
            }
        }
        conf_you_dis.setOnClickListener {
            val currentYour = text_player_position.text.toString().toInt()
            if (currentYour > 1) {
                setSharedPref("PlayerPosition", currentYour - 1)
                text_player_position.text = (currentYour - 1).toString()
            }
        }
        conf_button_add.setOnClickListener {
            val currentButton = text_button_position.text.toString().toInt()
            if (currentButton < text_maxplayer.text.toString().toInt()) {
                setSharedPref("ButtonPosition", currentButton + 1)
                text_button_position.text = (currentButton + 1).toString()
            }
        }
        conf_button_dis.setOnClickListener {
            val currentButton = text_button_position.text.toString().toInt()
            if (currentButton > 1) {
                setSharedPref("ButtonPosition", currentButton - 1)
                text_button_position.text = (currentButton - 1).toString()
            }
        }
        button_signout.setOnClickListener {
            clearLoginStatus()
            mainActivity?.getHome()
        }

    }

    fun setSharedPref(key: String, number: Int) {
        val data: SharedPreferences = this.activity.getSharedPreferences("config", Context.MODE_PRIVATE)
        val editer: SharedPreferences.Editor = data.edit()
        editer.putInt(key, number)
        editer.apply()
    }

    /***
     * 設定ファイルから最大プレーヤー数を取得するメソッド
     *
     * @return confMap: 設定上の最大人数
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
     * 設定ファイルから最大プレーヤー数を取得するメソッド
     * 現在はハードコーディング
     ***/
    fun clearLoginStatus(){
        val data: SharedPreferences = this.activity.getSharedPreferences("config", Context.MODE_PRIVATE)
        val editer: SharedPreferences.Editor = data.edit()
        editer.putBoolean("status",false)
        editer.apply()
    }
    override fun onAttach(activity: Activity?) {
        mainActivity = activity as HudMainActivity
        super.onAttach(activity)
    }
}