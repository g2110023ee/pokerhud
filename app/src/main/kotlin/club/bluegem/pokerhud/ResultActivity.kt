package club.bluegem.pokerhud

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.result.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.CallbackManager



class ResultActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)
        val result = listOf("seat1", "seat2", "seat3", "seat4", "seat5", "seat6", "seat7", "seat8", "seat9", "seat10")
        val resultList: ListView = ResultList
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result)
        resultList.adapter = adapter
    }

}
