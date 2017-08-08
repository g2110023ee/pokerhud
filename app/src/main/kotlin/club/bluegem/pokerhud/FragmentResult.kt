package club.bluegem.pokerhud

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import club.bluegem.pokerhud.databinding.ListviewResultBinding
import kotlinx.android.synthetic.main.fragment_result.*
import android.content.DialogInterface
import io.realm.*


class FragmentResult : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)  {
        super.onActivityCreated(savedInstanceState)
        var realm = Realm.getDefaultInstance()
        val realmResult = realm.where(ResultHolder::class.java).findAll()
        val realmResultSort = realmResult.sort("date", Sort.DESCENDING)
        val adapter = FragmentResult.RealmResultAdapter(context,realmResultSort)
        ResultList.adapter = adapter
        button_reset.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle("Do you want to reset your result?")
                    .setMessage("You can not recover your result after reset")
                    .setPositiveButton("OK", { _, _ ->
                        realm.use { realms ->
                            realms.executeTransaction {
                                realms.delete(ResultHolder::class.java)

                            }
                        }
                        realm = Realm.getDefaultInstance()
                    })
                    .setNegativeButton("Cancel", null)
                    .show()
            adapter.notifyDataSetChanged()

        }
    }
    class RealmResultAdapter(context: Context, val realmResults: OrderedRealmCollection<ResultHolder>) : RealmBaseAdapter<ResultHolder>(realmResults), ListAdapter {
        var binding: ListviewResultBinding? = null
        var inflater: LayoutInflater = LayoutInflater.from(context)
        override fun getCount(): Int = realmResults.size

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            if (convertView == null) {
                binding = ListviewResultBinding.inflate(inflater, parent, false)
                binding?.root?.tag = binding
            } else {
                binding = convertView.tag as ListviewResultBinding?
            }
            binding?.result = getItem(position)
            return binding?.root
        }
    }
}
