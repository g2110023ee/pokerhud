package club.bluegem.pokerhud

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast

class TopActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        Toast.makeText(this, "未実装ですよ！！！！！", Toast.LENGTH_SHORT).show()
        Log.d("Status", "Open")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top)

        //Buttonの機能配置
        //Go to HUD(Game activity)
        val game_button: Button = findViewById(R.id.button_game) as Button
        game_button.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("Status", "onClick")
                if (v != null) changeHudActivity()
            }
        })
        //Go to Result activity
        val result_button: Button = findViewById(R.id.button_result) as Button
        result_button.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                if (v != null) changeResultActivity()
            }
        })

        //Facebook login button
        val facebook_button: Button = findViewById(R.id.button_facebook) as Button
        facebook_button.setOnClickListener(this)
    }

        //GameActivityにインテントを飛ばすメソッド
        fun changeHudActivity() {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        //ResultActivityにインテントを飛ばすメソッド
        fun changeResultActivity() {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
}
