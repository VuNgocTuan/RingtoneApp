package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import ringtone.studio.vuetu.ringtoneapp.repository.sqlite.RingtoneDatabaseHelper
import ringtone.studio.vuetu.ringtoneapp.utils.Constants

/**
 * Created by vungoctuan on 4/11/18.
 */
class NewRingtoneFragment : BaseFragment() {

    companion object {
        fun newInstance(): Fragment {
            return NewRingtoneFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestDataFromRemote(Constants.MAIN_URL, RingtoneDatabaseHelper.TABLE_NEWEST_RINGTONE)
    }
}