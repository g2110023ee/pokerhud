package club.bluegem.pokerhud

import android.os.AsyncTask
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Created by 01020409 on 2017/07/26.
 */
open class TopModel{

    fun accessApiCheckUserStatus(facebookID:String):Boolean{
        //val url = "http://api.bluegem.club/v1/user?fid=$facebookID"
        val url = "http://10.0.2.2/v1/user?fid=$facebookID"
        Log.d("url", url)
        getRequest(url)
        //okHTTPを呼ぶコードがここに入る。
        return true
    }

    fun getRequest(url:String){
        object: HttpController(){
            override fun doInBackground(vararg p0: Void?): String{
                var res: String = ""
                try{
                    val httpClient: OkHttpClient = OkHttpClient()
                    val request: Request = Request.Builder().url(url).build()
                    val response: Response = httpClient.newCall(request).execute()
                    val res: String? = response.body()?.string()
                    Log.d("res", res)
                }catch (e: Exception){
                    Log.d("Error", e.toString())
                }
                return res
            }
        }.execute()

    }
}