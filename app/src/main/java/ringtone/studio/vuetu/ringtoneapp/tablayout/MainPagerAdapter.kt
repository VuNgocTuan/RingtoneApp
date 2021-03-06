package ringtone.studio.vuetu.ringtoneapp.tablayout

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.BestRingtoneFragment
import ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.HotRingtoneFragment
import ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.NewRingtoneFragment

/**
 * Created by vungoctuan on 4/11/18.
 */
class MainPagerAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {
    private val mPageCount = 3
    private val tabTitles = arrayOf("Mới Nhất", "Hot Nhất", "Hay Nhất")
    private var mContext: Context = context

    override fun getCount(): Int {
        return mPageCount
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return NewRingtoneFragment.newInstance()
            }
            1 -> {
                return HotRingtoneFragment.newInstance()
            }
            2
            -> {
                return BestRingtoneFragment.newInstance()
            }

        }
        return null
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    
}