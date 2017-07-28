package club.bluegem.pokerhud

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_hudadapter.*

class Fragment_HudController : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_hudadapter, container, false)
        return view
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textbutton_hud.setOnClickListener { getChildFragmentManager().beginTransaction().replace(R.id.fragmenthudadapter,Fragment_Hud()).commit() }
        textbutton_ranking.setOnClickListener { getChildFragmentManager().beginTransaction().replace(R.id.fragmenthudadapter,Fragment_Ranking()).commit() }
        textbutton_result.setOnClickListener { getChildFragmentManager().beginTransaction().replace(R.id.fragmenthudadapter,Fragment_Result()).commit() }
        textbutton_config.setOnClickListener { getChildFragmentManager().beginTransaction().replace(R.id.fragmenthudadapter,Fragment_Config()).commit() }
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    fun launchHud(){
        //Toast.makeText(getActivity(),"HUD",Toast.LENGTH_SHORT).show()
    }
}
