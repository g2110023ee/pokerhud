package club.bluegem.pokerhud

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.string
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

open class TopModel{

    fun accessApiCheckUserStatus(facebookID:String){
        //val url = "http://api.bluegem.club/v1/user?fid=$facebookID"
        val url = "http://10.0.2.2/v1/user?fid=$facebookID"
        getRequest(url)
    }

    fun getRequest(url:String){
        object: AsyncController(){
            override fun doInBackground(vararg p0: Void?): String{
                var res: String = ""
                try{
                    val httpClient: OkHttpClient = OkHttpClient()
                    val request: Request = Request.Builder().url(url).build()
                    val response: Response = httpClient.newCall(request).execute()
                    res = response.body()?.string() ?: ""
                }catch (e: Exception){
                }
                return jsonParseLoginCheck(res)
            }
        }.execute()
    }

    fun jsonParseLoginCheck(response:String) : String{
        val parsed: JsonObject = Parser().parse(StringBuilder(response)) as JsonObject
        Log.d("AsyncController",parsed.toString())
        val statusCode = parsed.string("status") ?: "400"
        Log.d("statusCode",statusCode)
        if(statusCode.equals("200")){
            val msg = parsed.get("msg") as JsonObject
            val userName = msg.string("username") ?: ""
            return userName
        }
        return statusCode
    }

    fun facebookLogin(){

    }
    /***
     * 設定ファイルから最大プレーヤー数を取得するメソッド
     * 現在はハードコーディング
     * TODO:
     * 設定画面を実装の上、設定から取得できるようにする
     *
     * @return maxPlayer: 設定上の最大人数
     ***/
    fun getUser(activity:Activity): Boolean {
        val data: SharedPreferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE)
        val userStatus = data.getBoolean("status",false)
        return userStatus
    }
    /***
     * 設定ファイルから最大プレーヤー数を取得するメソッド
     * 現在はハードコーディング
     * TODO:
     * 設定画面を実装の上、設定から取得できるようにする
     *
     * @return maxPlayer: 設定上の最大人数
     ***/
    fun setUser(activity:Activity,username: String) {
        val data: SharedPreferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE)
        val editer: SharedPreferences.Editor = data.edit()
        editer.putString("username", username)
        editer.putBoolean("status",true)
        editer.apply()
    }
}