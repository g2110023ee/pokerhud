package club.bluegem.pokerhud

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)
        //Hand count
        val playhand  = Hand()
        var handcount_text: TextView = findViewById(R.id.text_handcount) as TextView

        //Button Function
        val reset_button: Button = findViewById(R.id.button_reset) as Button
        reset_button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                if (v != null) playhand.resetHand()
                handcount_text.text = playhand.getHand().toString()

            }
        })
        val next_button: Button = findViewById(R.id.button_nexthand) as Button
        next_button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                if (v != null) playhand.addHand()
                handcount_text.text = playhand.getHand().toString()
            }
        })

        //List Function
        val players = listOf("seat1", "seat2", "seat3", "seat4", "seat5", "seat6", "seat7", "seat8", "seat9", "seat10")
        val playersList: ListView = findViewById(R.id.PlayerList) as ListView
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players)
        playersList.adapter = adapter



    }
    //Hand counting
    class Hand{
        var handCount: Int
        init {
            handCount = 0
        }
        fun addHand(){
            handCount=handCount+1
        }
        fun getHand(): Int {
            return handCount
        }
        fun resetHand(){
            handCount = 0
        }
    }
}
