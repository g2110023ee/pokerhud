package club.bluegem.pokerhud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
import java.util.*
import com.facebook.AccessToken
import org.json.JSONArray
import org.json.JSONObject


class TopActivity : AppCompatActivity(), View.OnClickListener {
    private var callbackManager: CallbackManager? = null
    override fun onClick(p0: View?) {
        Toast.makeText(this, "未実装ですよ！！！！！", Toast.LENGTH_SHORT).show()
        this.setLoginStatus()
        Log.d("Status", "Open")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Obtain the FirebaseAnalytics instance.
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mFirebaseAnalytics.setUserProperty("User","User1")
        val bundle:Bundle = Bundle()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top)
        FirebaseCrash.report(Exception("My first Android MainActivity created"))
        //FirebaseCrash.log("MainActivity created")
        if(checkLoginStatus()){
            Log.d("Status", "Logged outted")
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "001")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Login")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }

        //Buttonの機能配置
        //Go to HUD(Game activity)
        val game_button: Button = findViewById(R.id.button_game) as Button
        game_button.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("Status", "onClick")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "002")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "HUD")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                if (v != null) changeHudActivity()
            }
        })
        //Go to Result activity
        val result_button: Button = findViewById(R.id.button_result) as Button
        result_button.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "003")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Result")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                //FB logout test
                LoginManager.getInstance().logOut()
                //if (v != null) changeResultActivity()
            }
        })

        //Facebook login button
        val facebook_button: Button = findViewById(R.id.login_button) as Button
        this.callbackManager = CallbackManager.Factory.create()
        facebook_button.setOnClickListener{
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }
        LoginManager.getInstance().registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult) {
                val request = GraphRequest.newMeRequest(loginResult.accessToken
                ) { obj, Response ->
                    val facebookId = Response.jsonObject.getString("id")
                    val facebookName = Response.jsonObject.getString("first_name")
                    Log.e("facebookId in", facebookId)
                    Log.e("facebookName in", facebookName)
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,first_name")
                request.parameters = parameters
                request.executeAsync()
                Log.e("Logged in", request.toString())
            }

            override fun onCancel() {
                Log.e("Abort", "Abort login")
            }

            override fun onError(exception: FacebookException) {
                LoginManager.getInstance().logOut()
                Log.e("Error", exception.toString())
            }

        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
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

    fun checkLoginStatus(): Boolean {
        val sharedPre: SharedPreferences =getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        val loginStatus:Boolean = sharedPre.getBoolean("Login",true)
        Log.d("loginStatus", loginStatus.toString())
        return loginStatus
    }

    fun setLoginStatus(){
        val sharedPre: SharedPreferences =getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        val e: SharedPreferences.Editor = sharedPre.edit();
        e.putBoolean("Login", false)
        e.apply()
    }
}
