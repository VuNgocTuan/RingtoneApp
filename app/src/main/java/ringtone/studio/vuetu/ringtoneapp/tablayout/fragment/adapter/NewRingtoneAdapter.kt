package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.adapter

import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.txusballesteros.widgets.FitChart
import ringtone.studio.vuetu.ringtoneapp.R
import ringtone.studio.vuetu.ringtoneapp.mediaplay.MyMediaPlayer
import ringtone.studio.vuetu.ringtoneapp.repository.model.Ringtone
import java.util.*

/**
 * Created by vungoctuan on 4/12/18.
 */
class NewRingtoneAdapter : RecyclerView.Adapter<NewRingtoneAdapter.ViewHolder>() {
    var mRingtoneList = mutableListOf<Ringtone>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mRingtoneList[position])
        Log.d("duration", "" + MyMediaPlayer.getInstance().getDuration() / 1000)
        Log.d("currentPosition", "" + MyMediaPlayer.getInstance().getCurrentPosition() / 1000)
    }

    override fun getItemCount(): Int {
        return mRingtoneList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.item_ringtone, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImageBackground = view.findViewById<ImageView>(R.id.image_item_background)
        private val mButtonPlay = view.findViewById<ImageButton>(R.id.button_play)
        private val mTextName = view.findViewById<TextView>(R.id.text_ringtone_name)
        private val mTextAuthor = view.findViewById<TextView>(R.id.text_ringtone_author)
        private val mButtonDownload = view.findViewById<ImageButton>(R.id.button_download)
        private val mProgressPlayer = view.findViewById<FitChart>(R.id.progress_player)
        lateinit var mRingtone: Ringtone

        init {
            mButtonPlay.setOnClickListener {
                val my = MyMediaPlayer.getInstance()
                my.playMusicByUrl(mRingtone.url)
                mProgressPlayer.setValue(80f)
                mRingtone.isPlaying = true
                mProgressPlayer.maxValue = MyMediaPlayer.getInstance().getDuration()
            }
        }

        fun setData(ringtone: Ringtone) {
            mRingtone = ringtone
            mRingtone.isPlaying.let {
                if (!it) {
                    mProgressPlayer.setValue(0f)
                } else {
                    mProgressPlayer.maxValue = MyMediaPlayer.getInstance().getDuration()
                }
            }
            mTextName.text = mRingtone.name
            mTextAuthor.text = mRingtone.author
        }
    }

    fun setData(list: MutableList<Ringtone>) {
        mRingtoneList.clear()
        mRingtoneList.addAll(list)
        notifyDataSetChanged()
    }

    fun addRingtone(ringtone: Ringtone) {
        mRingtoneList.add(mRingtoneList.size, ringtone)
        notifyItemInserted(mRingtoneList.size - 1)
    }
}