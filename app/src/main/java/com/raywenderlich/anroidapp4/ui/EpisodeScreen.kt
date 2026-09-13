package com.raywenderlich.anroidapp4.ui
// Imports layout tools used to arrange the screen.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
// Imports the scrolling list used to display episodes.
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Imports Material components used by the screen.
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
// Imports Compose state tools.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
// Imports sizing, spacing and Android context values.
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
// Imports the podcast and episode data classes.
import com.raywenderlich.androidapp4.data.Episode
import com.raywenderlich.androidapp4.data.Podcast
// Imports the podcast player.
import com.raywenderlich.anroidapp4.player.PodcastPlayer
// Imports the repository used to retrieve episodes.
import com.raywenderlich.androidapp4.repository.PodcastRepository
// Imports the storage used to save subscriptions.
import com.raywenderlich.anroidapp4.storage.SubscriptionStorage
// Displays information and episodes for the selected podcast.
@Composable
fun EpisodeScreen(
    podcast: Podcast,
    onBackClick: () -> Unit
) {
    // Stores the episodes returned by the API.
    var episodes by remember {
        mutableStateOf(emptyList<Episode>())
    }
    // Stores whether the episodes are currently loading.
    var isLoading by remember {
        mutableStateOf(true)
    }
    // Stores an error message if the request fails.
    var errorMessage by remember {
        mutableStateOf("")
    }
    // Creates the repository used to retrieve episodes.
    val repository = remember {
        PodcastRepository()
    }
    // Gets the Android context needed to access device storage.
    val context = LocalContext.current
    //m Creates them storage used to manage subscriptions.
    val subscriptionStorage = remember {
        SubscriptionStorage(context.applicationContext)
    }
    // Stores whether mthe selected podcast is subscribed.
    var isSubscribed by remember(podcast.collectionId) {
        mutableStateOf(
            subscriptionStorage.isSubscribed(podcast.collectionId)
        )
    }
    //   Creates the player used to play podcast episodes.
    val podcastPlayer = remember {
        PodcastPlayer()
    }
    // Retrieves episodesn   when the selected podcast changes.
    LaunchedEffect(podcast.collectionId) {
        try {
            episodes = repository.getEpisodes(podcast.collectionId)
        } catch (exception: Exception) {
            errorMessage = "Unable to load episodes."
        } finally {
            isLoading = false
        }
    }
    // Stops audio when the user  leaves this screen.
    DisposableEffect(Unit) {
        onDispose {
            podcastPlayer.stopEpisode()
        }
    }
    // Arranges  the selected podcast information vertically.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Display s the selected podcast title.
        Text(
            text = podcast.collectionName,
            style = MaterialTheme.typography.headlineMedium
        )
        // Displays  the podcast creator.
        Text(
            text = podcast.artistName,
            style = MaterialTheme.typography.bodyLarge
        )
        // Adds or  removes the selected  podcast subscription.
        Button(
            onClick = {
                subscriptionStorage.toggleSubscription(podcast.collectionId)
                isSubscribed = !isSubscribed
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            //  Displays the current subscription action.
            Text(
                if (isSubscribed) {
                    "Unsubscribe"
                } else {
                    "Subscribe"
                }
            )
        }
        // Returns  the user to the podcast search screen.
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Displays the  back-button label.
            Text("Back to Podcasts")
        }
        // Displays a loading indicator while retrieving episodes.
        if (isLoading) {
            CircularProgressIndicator()
        }
        // Displays an error if the episode  request fails.
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage)
        }
        //  Displays the episodes returned by the API.
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(episodes) { episode ->
                // Arranges one episode title and its controls.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Displays the episode title.
                    Text(
                        text = episode.trackName ?: "Untitled Episode",
                        style = MaterialTheme.typography.titleMedium
                    )
                    // Arranges the playback buttons horizontally.
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Plays the selected episode.
                        Button(
                            onClick = {
                                episode.episodeUrl?.let { audioUrl ->
                                    podcastPlayer.playEpisode(audioUrl)
                                }
                            },
                            enabled = episode.episodeUrl != null
                        ) {
                            // Displays the play-button label.
                            Text("Play")
                        }
                        // Stops the episode currently playing.
                        Button(
                            onClick = {
                                podcastPlayer.stopEpisode()
                            }
                        ) {
                            // Displays the stop-button label.
                            Text("Stop")
                        }
                    }
                }
            }
        }
    }
}