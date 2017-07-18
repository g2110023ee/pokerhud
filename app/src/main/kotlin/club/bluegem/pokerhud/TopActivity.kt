package club.bluegem.pokerhud

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class TopActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        Toast.makeText(this, "未実装ですよ！！！！！", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top)
        val game_button: Button = findViewById(R.id.button_game) as Button
        game_button.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                if (v != null) changeHudActivity(v)
            }
        })
        val result_button: Button = findViewById(R.id.button_result) as Button
        result_button.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                if (v != null) changeResultActivity(v)
            }
        })
        val facebook_button: Button = findViewById(R.id.button_facebook) as Button
        facebook_button.setOnClickListener(this)
    }
        fun changeHudActivity(view: View) {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        fun changeResultActivity(view: View) {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
}
