package club.bluegem.pokerhud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import android.app.ProgressDialog

class TopActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // Obtain the FirebaseAnalytics instance.
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mFirebaseAnalytics.setUserProperty("User","Unregistered User")
        val fbBundle:Bundle = Bundle()

        //既にログインしているか確認し、ログイン済みならHUDに飛ばす。未ログインであればそのまま
        if(checkLoginStatus()){
            fbBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "001")
            fbBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Logged in")
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, fbBundle)
            jumpToHudActivity()
        }
        //既にログインしてない場合処理前にFBログアウトを行う
        LoginManager.getInstance().logOut()
        //Viewのロード
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top)

        //Facebook login button
        val facebook_button: Button = findViewById(R.id.login_button) as Button
        this.callbackManager = CallbackManager.Factory.create()
        facebook_button.setOnClickListener{
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }

        //Login manager
        LoginManager.getInstance().registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            //Login認証成功時
            @Suppress("DEPRECATION")
            override fun onSuccess(loginResult: LoginResult) {
                val progressDialog = ProgressDialog(this@TopActivity)
                val intent = Intent(this@TopActivity, SignupActivity::class.java)
                val request = GraphRequest.newMeRequest(loginResult.accessToken) { obj, Response ->
                    intent.putExtra("ID",Response.jsonObject.getString("id"))
                    intent.putExtra("Name",Response.jsonObject.getString("first_name"))
                    //ぐるぐる終わり
                    progressDialog.dismiss()
                    //ここから既存会員かのチェックをするメソッドに飛んで既存会員ならHUD、新規ならサインアップに飛ばす処理が必要
                    if(checkSignupStatus(Response.jsonObject.getString("id")))
                    //ユーザ登録に飛ばす
                    startActivity(intent)
                }
                //読み込んだデータを処理
                val requireParameters = Bundle()
                requireParameters.putString("fields", "id,first_name")
                request.parameters = requireParameters
                request.executeAsync()

                //処理が終わるまでぐるぐる
                progressDialog.run {
                    setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    setMessage("Loading")
                    setCancelable(false)
                    show()
                }
            }
            //キャンセルされたとき
            override fun onCancel() {
                LoginManager.getInstance().logOut()
                Toast.makeText(this@TopActivity,"Would you login to Facebook!!!!",Toast.LENGTH_SHORT).show()
            }
            //エラーが出たとき
            override fun onError(exception: FacebookException) {
                LoginManager.getInstance().logOut()
                Toast.makeText(this@TopActivity,"Somethings wrong, would you try again login to Facebook!!!!",Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    //アプリが終了された場合、登録が終わっていないのでログアウトとcallbackのunregisterを行う。
    override fun onDestroy() {
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().unregisterCallback(callbackManager)
        super.onDestroy()
    }

    //GameActivityにインテントを飛ばすメソッド
    fun jumpToHudActivity() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    //checking login status.
    fun checkLoginStatus(): Boolean {
        val sharedPre: SharedPreferences =getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        val loginStatus:Boolean = sharedPre.getBoolean("Login",false)
        Log.d("loginStatus", loginStatus.toString())
        return loginStatus
    }

    //既に登録されているユーザかAPIを呼んで確認した結果既存ユーザであればHUDに飛ばすメソッド
    fun checkSignupStatus(facebookID:String):Boolean{
        //OkHTTPでAPI叩くメソッド呼ぶ
        if(accessApiCheckUserStatus(facebookID))
            return true
        jumpToHudActivity()
        return false
    }

    //APIにアクセスして既存ユーザかチェックする
    fun accessApiCheckUserStatus(facebookID:String):Boolean{
        return true
    }
}
