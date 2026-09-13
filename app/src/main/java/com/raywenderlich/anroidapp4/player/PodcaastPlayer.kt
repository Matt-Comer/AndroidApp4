package com.raywenderlich.anroidapp4.player

// Imports Android's audio player.
import android.media.AudioAttributes
import android.media.MediaPlayer
//  Controls  podcast episode playback.
class PodcastPlayer {
    // Stores the  active media player.
    private var mediaPlayer: MediaPlayer? = null
    // Plays an episode using its audio URL.
    fun playEpisode(episodeUrl: String) {
        // Stops any episode that is already loaded.
        stopEpisode()
        // Creates and  prepares the media player.
        mediaPlayer = MediaPlayer().apply {
            //  Identifies the stream as spoken media audio.
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            // Sets  the left and right audio channels to full volume.
            setVolume(1.0f, 1.0f)
            // Provides the online episode audio address.
            setDataSource(episodeUrl)
            // Starts playback after the audio finishes preparing.
            setOnPreparedListener { player ->
                player.start()
            }
            // Releases the player when the episode finishes.
            setOnCompletionListener { player ->
                player.release()
                mediaPlayer = null
            }
            // Releases the player if playback fails.
            setOnErrorListener { player, _, _ ->
                player.release()
                mediaPlayer = null
                true
            }
            // Prepares the online audio without freezing the interface.
            prepareAsync()
        }
    }
    // Stops the current episode and releases the player.
    fun stopEpisode() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}