package club.bluegem.pokerhud

import android.support.v4.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import io.realm.Realm
import io.realm.RealmConfiguration



class HudMainActivity :  FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().build())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hud_main)
        //test
        val model = TopModel()
        //Check user status
        if(model.getUser(this)==false) {
            this.getHome()
        }else{
            this.getHud()
        }
    }

    fun getHud(){
        val hudFragment: FragmentHudController = FragmentHudController()
        val hudTransaction:FragmentTransaction = getSupportFragmentManager().beginTransaction()
        hudTransaction.replace(R.id.maincontainer,hudFragment)
        hudTransaction.commit()

    }

    fun getHome() {
        val topFragment: FragmentController = FragmentController()
        val topTransaction:FragmentTransaction = getSupportFragmentManager().beginTransaction()
        topTransaction.replace(R.id.maincontainer,topFragment)
        topTransaction.commit()
    }
}
