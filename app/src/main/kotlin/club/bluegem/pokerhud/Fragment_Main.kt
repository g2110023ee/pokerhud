package club.bluegem.pokerhud

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

class Fragment_Main : Fragment() {
    val topModel:TopModel = TopModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_facebooklogin.setOnClickListener{ v ->
            val fragment: Fragment_Signup = Fragment_Signup()
            fragmentManager.beginTransaction().replace(R.id.fragmentadapter,fragment).commit()
            topModel.facebookLogin()
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}
