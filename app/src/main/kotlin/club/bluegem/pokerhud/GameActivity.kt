package club.bluegem.pokerhud

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)
        val players = listOf("seat1", "seat2", "seat3", "seat4", "seat5", "seat6", "seat7", "seat8", "seat9", "seat10")
        val playersList: ListView = findViewById(R.id.PlayerList) as ListView
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players)
        playersList.adapter = adapter

    }
}
