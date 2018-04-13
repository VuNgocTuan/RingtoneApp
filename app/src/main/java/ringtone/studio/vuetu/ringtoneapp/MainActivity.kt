package ringtone.studio.vuetu.ringtoneapp

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import ringtone.studio.vuetu.ringtoneapp.mediaplay.MyMediaPlayer
import ringtone.studio.vuetu.ringtoneapp.tablayout.MainPagerAdapter

/**
 * Created by vungoctuan on 4/11/18.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.viewpager_main)
        mViewPager.adapter = MainPagerAdapter(supportFragmentManager, applicationContext)

        mTabLayout = findViewById(R.id.tab_main)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onDestroy() {
        MyMediaPlayer.getInstance().destroyMediaPlayer()
        super.onDestroy()
    }
}