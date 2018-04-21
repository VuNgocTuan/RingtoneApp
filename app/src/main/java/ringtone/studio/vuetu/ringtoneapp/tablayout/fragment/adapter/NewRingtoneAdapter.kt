package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.adapter

import android.media.MediaPlayer
import android.os.Handler
import android.support.v7.widget.RecyclerView
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
    var currentPlayingIndex = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!mRingtoneList[position].isPlaying) {
            holder.resetState()
            holder.changePlayButtonState(false)
        } else {
            holder.changePlayButtonState(true)
        }

        holder.mButtonPlay?.setOnClickListener {
            val my = MyMediaPlayer.getInstance()
            if (mRingtoneList[position].isPlaying) {
                my.stopPlayer()
                holder.resetState()
                currentPlayingIndex = -1
                mRingtoneList[position].isPlaying = false
                holder.changePlayButtonState(mRingtoneList[position].isPlaying)
            } else {
                if (currentPlayingIndex != -1) {
                    mRingtoneList[currentPlayingIndex].isPlaying = false
                    notifyItemChanged(currentPlayingIndex)
                }
                my.playMusicByUrl(mRingtoneList[position].url,
                        MediaPlayer.OnCompletionListener {
                            currentPlayingIndex = -1
                            mRingtoneList[position].isPlaying = false
                            holder.changePlayButtonState(false)
                            holder.changeProgressValue(false)
                        },
                        MediaPlayer.OnPreparedListener {
                            mRingtoneList[position].isPlaying = true
                            my.startPlayer()
                            currentPlayingIndex = position
                            holder.changeProgressValue(mRingtoneList[position].isPlaying)
                            holder.changePlayButtonState(mRingtoneList[position].isPlaying)
                        })
            }
        }
        holder.setData(mRingtoneList[position])
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
        val mButtonPlay: ImageButton? = view.findViewById(R.id.button_play)
        private val mTextName = view.findViewById<TextView>(R.id.text_ringtone_name)
        private val mTextAuthor = view.findViewById<TextView>(R.id.text_ringtone_author)
        private val mButtonDownload = view.findViewById<ImageButton>(R.id.button_download)
        private val mProgressPlayer = view.findViewById<DonutProgress>(R.id.progress_player)
        private lateinit var mRingtone: Ringtone
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
            changeProgressValue(mRingtone.isPlaying)
            mTextName.text = mRingtone.name
            mTextAuthor.text = mRingtone.author
        }

        fun changeProgressValue(isPlaying: Boolean) {
            if (isPlaying) {
                mProgressPlayer.max = MyMediaPlayer.getInstance().getDuration().toInt()
                myRun?.run()
            } else {
                resetState()
            }
        }

        fun changePlayButtonState(isPlaying: Boolean) {
            if (isPlaying) {
                mButtonPlay?.setImageResource(R.drawable.ic_stop_white)
            } else {
                mButtonPlay?.setImageResource(R.drawable.ic_play_white)
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