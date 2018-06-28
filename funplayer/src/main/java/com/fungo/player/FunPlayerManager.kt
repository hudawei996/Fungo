package com.fungo.player

/**
 * @author Pinger
 * @since 18-6-14 上午11:53
 * 播放器管理，使用本管理器要先在[PlayerConfig]中将[addToPlayerManager]设置为true
 * 管理播放器的引用和播放的状态
 */

class FunPlayerManager {

    companion object {
        @JvmStatic
        val instance: FunPlayerManager = FunPlayerManager()
    }

    /**
     * 播放器引用
     */
    private var mPlayer: FunPlayer? = null


    /**
     *　设置当前的播放器
     * @param player 当前播放器对象
     */
    fun setCurrentVideoPlayer(player: FunPlayer) {
        mPlayer = player
    }


    /**
     * 获取当前的播放器对象
     * @return 当前播放器对象，可能为null
     */
    fun getCurrentPlayer(): FunPlayer? {
        return mPlayer
    }

    /**
     * 释放当前播放器，会释放播放器引用的一切资源，并且置空对象引用
     */
    fun releasePlayer() {
        mPlayer?.release()
        mPlayer = null
    }

    /**
     * 播放器的返回处理，如果可以返回则会返回false
     * @return 是否要处理播放器返回
     */
    fun onBackPressed(): Boolean {
        return mPlayer?.onBackPressed() ?: false
    }


    /**
     * 暂停播放器，当按下Home键时，在onPause中调用本方法可以暂停播放器
     * 重新播放器时直接从当前位置开始，不需要重新加载资源
     */
    fun pausePlayer(){
        mPlayer?.pause()
    }


    /**
     * 停止播放器，当按下Home键时，在onStop中调用本方法可以停止播放器
     * 播放器停止后，会将当前的状态清空，重新播放时需要重新加载资源
     */
    fun stopPlayer() {
        mPlayer?.stopPlayback()
    }


    /**
     * 开始播放或者重新开始播放
     */
    fun restartPlayer(){
        mPlayer?.start()
    }
}