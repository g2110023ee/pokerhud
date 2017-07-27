package club.bluegem.pokerhud

import android.support.v4.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.FragmentActivity
//import android.support.v7.app.AppCompatActivity

class HudMainActivity :  FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hud_main)

        //val fragment:Fragment_Controller = Fragment_Controller()
        val fragment:Fragment_HudController = Fragment_HudController()

        val transaction:FragmentTransaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.maincontainer,fragment)
        transaction.commit()


    }
}
