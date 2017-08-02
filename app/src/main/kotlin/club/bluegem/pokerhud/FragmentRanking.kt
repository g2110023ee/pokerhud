package club.bluegem.pokerhud

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FragmentRanking : Fragment() {
    companion object {
        fun getInstance(): FragmentRanking {
            return FragmentRanking()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }
}