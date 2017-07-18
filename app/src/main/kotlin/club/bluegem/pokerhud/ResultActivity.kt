package club.bluegem.pokerhud

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)
        val result = listOf("seat1", "seat2", "seat3", "seat4", "seat5", "seat6", "seat7", "seat8", "seat9", "seat10")
        val resultList: ListView = findViewById(R.id.ResultList) as ListView
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result)
        resultList.adapter = adapter
    }
}
