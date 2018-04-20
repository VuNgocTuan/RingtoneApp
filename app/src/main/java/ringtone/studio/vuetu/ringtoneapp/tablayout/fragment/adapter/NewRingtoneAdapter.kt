package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.adapter

import android.media.MediaPlayer
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.github.lzyzsd.circleprogress.DonutProgress
import ringtone.studio.vuetu.ringtoneapp.R
import ringtone.studio.vuetu.ringtoneapp.mediaplay.MyMediaPlayer
import ringtone.studio.vuetu.ringtoneapp.repository.model.Ringtone


/**
 * Created by vungoctuan on 4/12/18.
 */
class NewRingtoneAdapter : RecyclerView.Adapter<NewRingtoneAdapter.ViewHolder>() {
    var mRingtoneList = mutableListOf<Ringtone>()
    var mCurrentPlayIndex = 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mRingtoneList[position])

        holder.mButtonPlay?.setOnClickListener {
            mRingtoneList[mCurrentPlayIndex].isPlaying = false
            val my = MyMediaPlayer.getInstance()
            my.playMusicByUrl(mRingtoneList[position].url,
                    MediaPlayer.OnCompletionListener {
                        mRingtoneList[position].isPlaying = false
                    },
                    MediaPlayer.OnPreparedListener {
                        mCurrentPlayIndex = position
                        mRingtoneList[position].isPlaying = true
                        my.startPlayer()
                        holder.changeProgressValue()
                    })
        }
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
        val mButtonPlay: ImageButton? = view.findViewById<ImageButton>(R.id.button_play)
        private val mTextName = view.findViewById<TextView>(R.id.text_ringtone_name)
        private val mTextAuthor = view.findViewById<TextView>(R.id.text_ringtone_author)
        private val mButtonDownload = view.findViewById<ImageButton>(R.id.button_download)
        private val mProgressPlayer = view.findViewById<DonutProgress>(R.id.progress_player)
        lateinit var mRingtone: Ringtone
        private var mHandler: Handler = Handler()
        private var myRun: Runnable? = null

        init {
            myRun = Runnable {
                if (mRingtone.isPlaying) {
                    mProgressPlayer.progress = MyMediaPlayer.getInstance().getCurrentPosition()
                    mHandler.postDelayed(myRun, 200)
                } else {
                    resetState()
                }
            }
        }

        fun setData(ringtone: Ringtone) {
            mRingtone = ringtone
            changeProgressValue()
            mTextName.text = mRingtone.name
            mTextAuthor.text = mRingtone.author
        }

        fun changeProgressValue() {
            if (mRingtone.isPlaying) {
                mProgressPlayer.max = MyMediaPlayer.getInstance().getDuration().toInt()
                myRun?.run()
            } else {
                resetState()
            }
        }


        fun resetState() {
            mHandler.removeCallbacks(myRun)
            mProgressPlayer.progress = 0f
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