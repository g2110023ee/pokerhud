package club.bluegem.pokerhud

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class TopActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top)
        val game_button: Button = findViewById(R.id.button_game) as Button
        game_button.setOnClickListener(this)
        val result_button: Button = findViewById(R.id.button_result) as Button
        game_button.setOnClickListener(this)
    }

    fun changeActivity(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?){
        if (v != null) {
            changeActivity(v)
        }
    }
}
