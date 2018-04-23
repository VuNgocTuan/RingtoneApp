package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ringtone.studio.vuetu.ringtoneapp.R
import ringtone.studio.vuetu.ringtoneapp.repository.sqlite.RingtoneDatabaseHelper
import ringtone.studio.vuetu.ringtoneapp.utils.Constants

/**
 * Created by FRAMGIA\vu.ngoc.tuan on 23/04/2018.
 */
class BestRingtoneFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment {
            return BestRingtoneFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDatabase = RingtoneDatabaseHelper.getInstance(context?.applicationContext)
        return inflater.inflate(R.layout.fragment_best_ringtone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestDataFromRemote(Constants.MAIN_URL_BEST_RINGTONE, RingtoneDatabaseHelper.TABLE_BEST_RINGTONE)
    }
}