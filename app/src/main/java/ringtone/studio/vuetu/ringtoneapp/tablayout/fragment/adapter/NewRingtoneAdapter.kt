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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
        private val mButtonPlay = view.findViewById<ImageButton>(R.id.button_play)
        private val mTextName = view.findViewById<TextView>(R.id.text_ringtone_name)
        private val mTextAuthor = view.findViewById<TextView>(R.id.text_ringtone_author)
        private val mButtonDownload = view.findViewById<ImageButton>(R.id.button_download)
        private val mProgressPlayer = view.findViewById<DonutProgress>(R.id.progress_player)
        lateinit var mRingtone: Ringtone
        private var mIsPlaying: Boolean = false
        private var mHandler: Handler = Handler()
        private var myRun: Runnable? = null

        init {
            mButtonPlay.setOnClickListener {
                val my = MyMediaPlayer.getInstance()
                my.playMusicByUrl(mRingtone.url,
                        MediaPlayer.OnCompletionListener {
                            Log.d("Holder", "Play ok")
                            mIsPlaying = false
                        },
                        MediaPlayer.OnPreparedListener {
                            Log.d("Holder", "Prepared ok")
                            mIsPlaying = true
                            my.startPlayer()
                            changeProgressValue()
                        })
            }
        }

        fun setData(ringtone: Ringtone) {
            mRingtone = ringtone
//            changeProgressValue()
            mTextName.text = mRingtone.name
            mTextAuthor.text = mRingtone.author
        }

        private fun changeProgressValue() {
            if (mIsPlaying) {
                mProgressPlayer.max = MyMediaPlayer.getInstance().getDuration().toInt()
                Log.d("Runnable max", "" + mProgressPlayer.max)

                myRun = Runnable {
                    mProgressPlayer.progress = MyMediaPlayer.getInstance().getCurrentPosition()
                    Log.d("Runnable", "" + MyMediaPlayer.getInstance().getCurrentPosition())
                    mHandler.postDelayed(myRun, 200)
                }
                myRun?.run()
            } else {
                mProgressPlayer.progress = 0f
            }
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