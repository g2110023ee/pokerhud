package club.bluegem.pokerhud

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_signup.*

class FragmentSignup : Fragment() {
    private var mainActivity:HudMainActivity? = null
    private var facebookBundle:Bundle? =null
    private val model:TopModel = TopModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        facebookBundle = this.arguments
        Log.d("onCreate",facebookBundle.toString())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        facebookBundle = this.arguments
        Log.d("onCreateView",facebookBundle.toString())
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facebookBundle = this.arguments
        Log.d("onViewCreated",facebookBundle.toString())
        val textViewFacebookName:TextView = text_facebookname
        textViewFacebookName.setText(facebookBundle?.getString("fbname"))
        Log.d("ID",facebookBundle?.getString("fbid"))
        val buttonSubmit:Button = button_signup
        val userName = text_playername.text.toString()
        buttonSubmit.setOnClickListener {
            model.setUser(activity,userName)
            mainActivity?.getRequest()}
    }

    override fun onAttach(activity: Activity?) {
        mainActivity = activity as HudMainActivity
        super.onAttach(activity)
    }
}
