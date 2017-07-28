package club.bluegem.pokerhud

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash

class SignupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent
        Log.d("intent", intent.toString())
        val facebookId:String? = intent.getStringExtra("ID")
        val facebookName:String? = intent.getStringExtra("Name")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        Log.d("facebookId", facebookId)
        Log.d("facebookName", facebookName)
    }

    //After sign up, this method changes Login status on SharedPreferences as True.
    fun setLoginStatus(){
        val sharedPre: SharedPreferences =getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        val e: SharedPreferences.Editor = sharedPre.edit();
        e.putBoolean("Login", true)
        e.apply()
    }
}
