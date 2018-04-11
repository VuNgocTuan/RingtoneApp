package ringtone.studio.vuetu.ringtoneapp.tablayout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ringtone.studio.vuetu.ringtoneapp.R

/**
 * Created by vungoctuan on 4/11/18.
 */
class NewRingtoneFragment : Fragment() {

    companion object {
        fun newInstance(): NewRingtoneFragment{
            return NewRingtoneFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_ringtone, container, false)
    }
}