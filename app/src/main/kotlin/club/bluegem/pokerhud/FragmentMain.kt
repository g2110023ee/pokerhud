package club.bluegem.pokerhud

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class FragmentMain : Fragment() {
    private var callbackManager: CallbackManager? = null
    private var mainActivity:HudMainActivity? = null
    val topModel:TopModel = TopModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //既にログインしてない場合処理前にFBログアウトを行う
        LoginManager.getInstance().logOut()
        this.callbackManager = CallbackManager.Factory.create()
        button_facebooklogin.setOnClickListener{
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }

        //Login manager
        LoginManager.getInstance().registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            //Login認証成功時
            override fun onSuccess(loginResult: LoginResult) {
                val request = GraphRequest.newMeRequest(loginResult.accessToken) { _, Response ->
                    //ここから既存会員かのチェックをするメソッドに飛んで既存会員ならHUD、新規ならサインアップに飛ばす
                    if(checkSignupStatus(Response.jsonObject.getString("id"))) mainActivity?.getRequest()
                    jumpToSignupActivity(Response.jsonObject.getString("id"),Response.jsonObject.getString("first_name"))
                }
                //読み込んだデータを処理
                val requireParameters = Bundle()
                requireParameters.putString("fields", "id,first_name")
                request.parameters = requireParameters
                request.executeAsync()

            }
            //キャンセルされたとき
            override fun onCancel() {
                LoginManager.getInstance().logOut()
                Toast.makeText(activity,"Would you login to Facebook!!!!",Toast.LENGTH_SHORT).show()
            }
            //エラーが出たとき
            override fun onError(exception: FacebookException) {
                LoginManager.getInstance().logOut()
                Toast.makeText(activity,"Somethings wrong, would you try again login to Facebook!!!!",Toast.LENGTH_SHORT).show()
            }
                })
    }

    private fun  checkSignupStatus(fbid: String): Boolean {
        topModel.accessApiCheckUserStatus(fbid)
        return false
    }


    private fun jumpToSignupActivity(fbid: String?, fbname: String?) {
        val facebookBundle = Bundle()
        facebookBundle.putString("fbid",fbid)
        facebookBundle.putString("fbname",fbname)
        val fragmentSignup: FragmentSignup = FragmentSignup()
        fragmentSignup.arguments = facebookBundle
        Log.d("facebookBundle",facebookBundle.toString())
        Log.d("fragmentSignup",fragmentSignup.arguments.toString())
        fragmentManager.beginTransaction().replace(R.id.fragmentadapter,fragmentSignup).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
    override fun onDetach() {
        super.onDetach()
    }

    override fun onAttach(activity: Activity?) {
        mainActivity = activity as HudMainActivity
        super.onAttach(activity)
    }
}
