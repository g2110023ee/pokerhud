package club.bluegem.pokerhud

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_hudadapter.*

class FragmentHudController : Fragment() {
    var currentFragment="Hud"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_hudadapter, container, false)
        childFragmentManager.beginTransaction().add(R.id.fragmenthudadapter, FragmentHud()).commit()
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textbutton_hud.setOnClickListener {
            if(currentFragment!="Hud") childFragmentManager.beginTransaction().replace(R.id.fragmenthudadapter, FragmentHud()).commit()
            currentFragment="Hud"
        }
        textbutton_ranking.setOnClickListener {
            if(currentFragment!="Ranking") childFragmentManager.beginTransaction().replace(R.id.fragmenthudadapter, FragmentRanking()).commit()
            currentFragment="Ranking"
        }
        textbutton_result.setOnClickListener {
            if(currentFragment!="Result") childFragmentManager.beginTransaction().replace(R.id.fragmenthudadapter, FragmentResult()).commit()
            currentFragment="Result"
        }
        textbutton_config.setOnClickListener {
            if(currentFragment!="Config") childFragmentManager.beginTransaction().replace(R.id.fragmenthudadapter, FragmentConfig()).commit()
            currentFragment="Config"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}
