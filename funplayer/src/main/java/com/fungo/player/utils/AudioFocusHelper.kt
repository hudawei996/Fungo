package com.fungo.player.utils

import android.media.AudioManager
import com.fungo.player.FunPlayer
import java.lang.ref.WeakReference

/**
 * @author Pinger
 * @since 18-6-14 上午10:14
 *　音频焦点改变监听
 */

class AudioFocusHelper(var weekPlayer: WeakReference<FunPlayer>, var weekAudioManager: WeakReference<AudioManager>) : AudioManager.OnAudioFocusChangeListener {

    private var startRequested = false
    private var pausedForLoss = false
    private var currentFocus = 0


    override fun onAudioFocusChange(focusChange: Int) {
        if (currentFocus == focusChange) {
            return
        }
        currentFocus = focusChange
        when (focusChange) {
        //　获得焦点
            AudioManager.AUDIOFOCUS_GAIN
                //　暂时获得焦点
                , AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            -> {
                if (startRequested || pausedForLoss) {
                    weekPlayer.get()?.start()
                    startRequested = false
                    pausedForLoss = false
                }
                if (weekPlayer.get()?.mPlayer != null && !weekPlayer.get()!!.isMute())
                //　恢复音量
                    weekPlayer.get()?.mPlayer?.setVolume(1.0f, 1.0f)
            }
        //　焦点丢失
            AudioManager.AUDIOFOCUS_LOSS
                //　焦点暂时丢失
                , AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
            -> if (weekPlayer.get()?.isPlaying() == true) {
                pausedForLoss = true
                weekPlayer.get()?.pause()
            }
        //　此时需降低音量
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
            -> if (weekPlayer.get()?.mPlayer != null && weekPlayer.get()?.isPlaying() == true && weekPlayer.get()?.isMute() == false) {
                weekPlayer.get()?.mPlayer?.setVolume(0.1f, 0.1f)
            }
        }
    }

    /**
     * Requests to obtain the audio focus
     *
     * @return True if the focus was granted
     */
    internal fun requestFocus(): Boolean {
        if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
            return true
        }

        if (weekAudioManager.get() == null) {
            return false
        }

        val status = weekAudioManager.get()?.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
            currentFocus = AudioManager.AUDIOFOCUS_GAIN
            return true
        }

        startRequested = true
        return false
    }

    /**
     * Requests the system to drop the audio focus
     *
     * @return True if the focus was lost
     */
    internal fun abandonFocus(): Boolean {

        if (weekAudioManager.get() == null) {
            return false
        }

        startRequested = false
        val status = weekAudioManager.get()?.abandonAudioFocus(this)
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status
    }
}