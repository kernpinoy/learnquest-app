package org.cutie.learnquest.interfaces.mainmenu

import android.content.Context
import android.media.MediaPlayer
import android.content.SharedPreferences
import org.cutie.learnquest.R

object AudioManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicPlaying = false
    private var isUserPaused = false // ✅ Track if user manually paused music

    private const val PREFS_NAME = "audio_prefs"
    private const val KEY_USER_PAUSED = "isUserPaused"

    fun initialize(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.bg_music).apply {
                isLooping = true
            }
            // ✅ Restore user pause state
            val prefs = getPrefs(context)
            isUserPaused = prefs.getBoolean(KEY_USER_PAUSED, false)
        }
    }

    fun startOrResumeMusic(context: Context) {
        if (mediaPlayer == null || isUserPaused) return // ❌ Don't play if the user paused it
        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
            isMusicPlaying = true
        }
    }

    fun pauseMusic(context: Context) {
        mediaPlayer?.pause()
        isMusicPlaying = false
        isUserPaused = true // ✅ Mark as user-paused
        savePausedState(context)
    }

    fun stopMusic(context: Context) {
        mediaPlayer?.release()
        mediaPlayer = null
        isMusicPlaying = false
        isUserPaused = false // Reset user pause state
        savePausedState(context)
    }

    fun toggleMusic(context: Context) {
        isUserPaused = !isUserPaused // ✅ Toggle user pause state
        savePausedState(context)

        if (isUserPaused) {
            pauseMusic(context)
        } else {
            startOrResumeMusic(context)
        }
    }

    fun isPlaying(): Boolean {
        return isMusicPlaying
    }

    fun wasPausedByUser(): Boolean {
        return isUserPaused
    }

    private fun savePausedState(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().putBoolean(KEY_USER_PAUSED, isUserPaused).apply() // ✅ Save pause state
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}
