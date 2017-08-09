package club.bluegem.pokerhud

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.string
import okhttp3.*
import org.json.JSONObject




open class TopModel{
    val domain:String = "api.bluegem.club"
    //val domain:String = "10.0.2.2"

    fun accessApiCheckUserStatus(facebookID:String){

        val url = "http://$domain/v1/user?fid=$facebookID"
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
        val statusCode = parsed.string("statusCode") ?: "400"
        Log.d("statusCode",statusCode)
        if(statusCode.equals("200")){
            val msg = parsed.get("msg") as JsonObject
            val userName = msg.string("username") ?: ""
            return userName
        }
        return statusCode
    }

    fun userSingUp(facebookID:String,userName:String){
        val url = "http://$domain/v1/user"
        postRequest(url,facebookID,userName)
    }

    fun postRequest(url:String,facebookID:String,userName:String){
        object: AsyncController(){
            override fun doInBackground(vararg p0: Void?): String{
                var statusCode = "400"
                try{
                    val client = OkHttpClient()
                    val MIMEType = MediaType.parse("application/json; charset=utf-8")
                    val requestBody = RequestBody.create(MIMEType, "{\"fbid\":\"$facebookID\",\"username\":\"$userName\"}")
                    val request = Request.Builder().url(url).post(requestBody).build()
                    val res = JSONObject(client.newCall(request).execute().body()?.string())
                    statusCode = res.getString("statusCode") ?: "400"
                    Log.d("statusCode",statusCode)
                }catch (e: Exception){
                    Log.d("catchException",e.toString())
                }
                return statusCode
            }
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        }.execute()
        return
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