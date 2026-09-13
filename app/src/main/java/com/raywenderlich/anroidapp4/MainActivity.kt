package com.raywenderlich.anroidapp4
// Imports  the Android activity state bundle.
import android.os.Bundle
//  Imports the base activity used with Jetpack Compose.
import androidx.activity.ComponentActivity
// Imports the function used  to display Compose content.
import androidx.activity.compose.setContent
// Imports Compose  state tools.
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
// Imports the  podcast data class.
import com.raywenderlich.androidapp4.data.Podcast
// Imports the screen  hat displays the selected podcast.
import com.raywenderlich.anroidapp4.ui.EpisodeScreen
// Imports the podcast search  screen.
import com.raywenderlich.androidapp4.ui.PodcastScreen
//  Imports the application  theme.
import com.raywenderlich.anroidapp4.ui.theme.AnroidApp4Theme
// Launches the Podcast application.
class MainActivity : ComponentActivity() {
    // Creates the application interface when the activity starts.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Displays the Compose application content.
        setContent {
            // Applies the application theme.
            AnroidApp4Theme {
                // Stores the podcast selected by the user.
                var selectedPodcast by remember {
                    mutableStateOf<Podcast?>(null)
                }
                // Displays the correct screen for the current selection.
                if (selectedPodcast == null) {
                    // Displays the podcast search screen.
                    PodcastScreen(
                        onPodcastClick = { podcast ->
                            // Stores the selected podcast.
                            selectedPodcast = podcast
                        }
                    )
                } else {
                    // Displays information about the selected podcast.
                    EpisodeScreen(
                        podcast = selectedPodcast!!,
                        onBackClick = {
                            // Returns to the podcast search screen.
                            selectedPodcast = null
                        }
                    )
                }
            }
        }
    }
}