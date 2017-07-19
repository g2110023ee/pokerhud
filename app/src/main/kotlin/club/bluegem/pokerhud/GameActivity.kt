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
import kotlinx.android.synthetic.main.game.*
import kotlinx.android.synthetic.main.listview_huditem.*


class GameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Status", "onCreate")
        setContentView(R.layout.game)
        Log.d("Status", "Loaded view")
        val playHand = Hand("0")
        //Binding
        val binding : GameBinding
        binding = DataBindingUtil.setContentView(this, R.layout.game)
        binding.hand= playHand
        //Hand count

        val seat1 = Player(seatNumber = 1)
        var players = mutableListOf(seat1)
        val seat2 = Player(seatNumber = 2)
        players.add(seat2)

        //Button Function
        val reset_button: Button = button_reset as Button
        reset_button.setOnClickListener { v ->
            if (v != null) playHand.resetHand()
            textUpdate()
            seat1.resetAll()
            Log.d("Reset", "onClick")
        }
        val next_button: Button = button_nexthand as Button
        next_button.setOnClickListener { v ->
            if (v != null) playHand.addHand()
            textUpdate()
            seat1.addHand()
            Log.d("Hands", seat1.playedHandCount.toString())
            seat2.addCalledHand()
            Log.d("Called", seat2.calledHandCount.toString())
        }

        //List Function
        val adapter = HudAdapter(this,players)
        PlayerList.adapter = adapter

        //val playersList: ListView = findViewById(R.id.PlayerList) as ListView
        //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players)
        //playersList.adapter = adapter
    }
    fun textUpdate(){
        //text_handcount.text = playHand.getHand().toString()
    }
}
class HudAdapter(context: Context, players: List<Player>) : ArrayAdapter<Player>(context, 0, players) {
    val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //val binding: ItemListViewBinding
        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_huditem, parent, false)
        }
        val players = getItem(position) as Player
        var handCound: TextView? = view?.findViewById(R.id.textView2)
        handCound?.text = players.playedHandCount.toString()
        var callCound: TextView? = view?.findViewById(R.id.textView3)
        callCound?.text = players.calledHandCount.toString()
        return view!!
    }

}
    //Hand counting
    data class Hand(var handCount:String){
        fun addHand(){
            handCount=handCount+1
        }
        fun resetHand(){
            handCount = "0"
        }
    }

    data class Player(
            val seatNumber:Int,
            var status:Int = 0,
            var playedHandCount:Int = 0,
            var calledHandCount:Int = 0,
            var raisedHandCount:Int = 0,
            var dealerButtonCount:Int = 0,
            var dealerButtonRaisedCount:Int = 0
    ){
        fun changeStatus(){
            if(this.status == 0) this.status = 1
            if(this.status == 1) this.status = 0
        }
        fun addHand(){
            this.playedHandCount++
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
        fun getVpipStatus(): String {
            val vpip = 0
            return vpip.toString()
        }
        fun resetAll(){
            status = 0
            playedHandCount = 0
            calledHandCount = 0
            raisedHandCount = 0
            dealerButtonCount = 0
            dealerButtonRaisedCount = 0
        }
    }
