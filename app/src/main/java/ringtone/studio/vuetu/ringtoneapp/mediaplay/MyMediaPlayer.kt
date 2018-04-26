package ringtone.studio.vuetu.ringtoneapp.mediaplay

import android.media.MediaPlayer

/**
 * Created by FRAMGIA\vu.ngoc.tuan on 13/04/2018.
 */
class MyMediaPlayer {
    private val mPlayer = MediaPlayer()

    companion object {
        var RINGTONE_ID_PLAYING = -1
        var RINGTONE_NAME_PLAYING = ""
        private var mMyPlayer: MyMediaPlayer? = null
        fun getInstance(): MyMediaPlayer {
            if (mMyPlayer == null) {
                mMyPlayer = MyMediaPlayer()
            }
            return mMyPlayer as MyMediaPlayer
        }
    }

    fun playMusicByUrl(url: String,
                       ringtoneId: Int,
                       ringtoneName: String,
                       completionListener: MediaPlayer.OnCompletionListener,
                       preparedListener: MediaPlayer.OnPreparedListener) {
        try {
            mPlayer.reset()
            mPlayer.setDataSource(url)
            mPlayer.prepareAsync()
            RINGTONE_ID_PLAYING = ringtoneId
            RINGTONE_NAME_PLAYING = ringtoneName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mPlayer.setOnCompletionListener(completionListener)
        mPlayer.setOnPreparedListener(preparedListener)
        mPlayer.setOnErrorListener { p0, p1, p2 ->
            println("error ")
            false
        }
    }

    fun startPlayer() {
        mPlayer.start()
    }

    fun stopPlayer() {
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

    fun isPlaying(): Boolean {
        return mPlayer.isPlaying
    }
}

