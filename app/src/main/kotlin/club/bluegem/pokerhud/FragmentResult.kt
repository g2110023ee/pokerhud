package club.bluegem.pokerhud

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_result.*


class FragmentResult : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)  {
        super.onActivityCreated(savedInstanceState)
    }

}
