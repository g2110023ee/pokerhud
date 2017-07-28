package club.bluegem.pokerhud

import android.support.v4.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class HudMainActivity :  FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hud_main)

        val topFragment:Fragment_Controller = Fragment_Controller()
        val topTransaction:FragmentTransaction = getSupportFragmentManager().beginTransaction()
        topTransaction.add(R.id.maincontainer,topFragment)
        topTransaction.commit()
        getRequest()

    }
    fun getRequest(){
        object: AsyncController(){
            override fun doInBackground(vararg p0: Void?): String{
                Thread.sleep(10000)

                val hudFragment:Fragment_HudController = Fragment_HudController()
                val hudTransaction:FragmentTransaction = getSupportFragmentManager().beginTransaction()
                hudTransaction.replace(R.id.maincontainer,hudFragment)
                hudTransaction.commit()
                return ""
            }
        }.execute()

    }
}
