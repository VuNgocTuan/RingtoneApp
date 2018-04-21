package ringtone.studio.vuetu.ringtoneapp.mediaplay

import android.media.MediaPlayer

/**
 * Created by FRAMGIA\vu.ngoc.tuan on 13/04/2018.
 */
class MyMediaPlayer {
    private val mPlayer = MediaPlayer()

    companion object {
        private var mMyPlayer: MyMediaPlayer? = null
        fun getInstance(): MyMediaPlayer {
            if (mMyPlayer == null) {
                mMyPlayer = MyMediaPlayer()
            }
            return mMyPlayer as MyMediaPlayer
        }
    }

    fun playMusicByUrl(url: String,
                       completionListener: MediaPlayer.OnCompletionListener,
                       preparedListener: MediaPlayer.OnPreparedListener) {
        try {
            mPlayer.reset()
            mPlayer.setDataSource(url)
            mPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mPlayer.setOnCompletionListener(completionListener)
        mPlayer.setOnPreparedListener(preparedListener)
    }

    fun startPlayer() {
        mPlayer.start()
    }

    fun stopPlayer(){
        mPlayer.reset()
    }

    fun destroyMediaPlayer() {
        mPlayer.release()
    }

    fun getCurrentPosition(): Float {
        return mPlayer.currentPosition.toFloat()
    }

    fun getDuration(): Float {
        return mPlayer.duration.toFloat()
    }
}

