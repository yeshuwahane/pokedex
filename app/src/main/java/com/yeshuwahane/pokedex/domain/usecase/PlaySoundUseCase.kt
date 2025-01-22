package com.yeshuwahane.pokedex.domain.usecase

import android.media.MediaPlayer
import android.util.Log
import javax.inject.Inject


class PlaySoundUseCase @Inject constructor() {
    fun execute(soundUrl: String) {
        try {
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(soundUrl) // Set the sound URL
                prepareAsync() // Prepare the media player asynchronously
                setOnPreparedListener { start() } // Start playing when ready
            }

            mediaPlayer.setOnCompletionListener {
                it.release() // Release resources when playback completes
            }

            mediaPlayer.setOnErrorListener { _, what, extra ->
                Log.e("PlaySound", "Error during playback: what=$what, extra=$extra")
                mediaPlayer.release()
                true
            }
        } catch (e: Exception) {
            Log.e("PlaySound", "Error initializing MediaPlayer: ${e.message}", e)
        }
    }
}
