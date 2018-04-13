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

    fun playMusicByUrl(url: String) {
        try {
            mPlayer.reset()
            mPlayer.setDataSource(url)
            mPlayer.setOnPreparedListener { onPrepared(mPlayer) }
            mPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onPrepared(player: MediaPlayer) {
        player.start()
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