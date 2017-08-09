package club.bluegem.pokerhud

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_signup.*

class FragmentSignup : Fragment() {
    private var mainActivity:HudMainActivity? = null
    private val model:TopModel = TopModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val facebookBundle:Bundle? = arguments
        val fbid= facebookBundle?.getString("fbid") ?: ""
        text_facebookname.setText(facebookBundle?.getString("fbname"))
        button_signup.setOnClickListener {
            val userName = text_playername.text.toString()
            model.userSingUp(fbid,userName)
            model.setUser(activity,userName)
            mainActivity?.getHud()}
    }

    override fun onAttach(activity: Activity?) {
        mainActivity = activity as HudMainActivity
        super.onAttach(activity)
    }
}
