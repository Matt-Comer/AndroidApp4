package com.raywenderlich.anroidapp4.ui
// Imports background and  layout tools used to design the screen.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
// Imports   the scrolling list used to display episodes.
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Imports Material components  used by the screen.
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
// Imports Compose  state and lifecycle tools.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
// Imports  color, sizing and text-styling tools.
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// Imports the podcast  and episode data classes.
import com.raywenderlich.androidapp4.data.Episode
import com.raywenderlich.androidapp4.data.Podcast
// Imports the podcast  player.
import com.raywenderlich.anroidapp4.player.PodcastPlayer
// Imports the repository used to retrieve episodes.
import com.raywenderlich.androidapp4.repository.PodcastRepository
// Imports the storage used to save subscriptions.
import com.raywenderlich.anroidapp4.storage.SubscriptionStorage
// Displays the polished e  pisode screen.
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
    // Creates the  repository used to retrieve episodes.
    val repository = remember {
        PodcastRepository()
    }
    // Gets the Android context  needed to access device storage.
    val context = LocalContext.current
    // Creates the storage used to manage subscriptions.
    val subscriptionStorage = remember {
        SubscriptionStorage(context.applicationContext)
    }
    // Stores whether the selected podcast is subscribed.
    var isSubscribed by remember(podcast.collectionId) {
        mutableStateOf(
            subscriptionStorage.isSubscribed(podcast.collectionId)
        )
    }
    // Creates the player used to play podcast episodes.
    val podcastPlayer = remember {
        PodcastPlayer()
    }
    // Retrieves  episodes when the  selected podcast changes.
    LaunchedEffect(podcast.collectionId) {
        try {
            //Requests  the selected podcast's episodes.
            episodes = repository.getEpisodes(podcast.collectionId)
        } catch (exception: Exception) {
              // Displays an error when episodes cannot be loaded.
            errorMessage =
                "Unable to load episodes. Check your internet connection."
        } finally {
            // Hides the loading indicator.
            isLoading = false
        }
    }
    //  Stops   audio when the user leaves this screen.
    DisposableEffect(Unit) {
        onDispose {
            podcastPlayer.stopEpisode()
        }
    }
    // Creates  the matching navy, purple and blue background.
    val screenGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF071426),
            Color(0xFF241044),
            Color(0xFF0B4F6C)
        )
    )
    // Displays the gradient across the entire screen.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenGradient)
    ) {
        // Arranges the podcast information and episodes vertically.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Displays the selected podcast title.
            Text(
                text = podcast.collectionName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF52E5FF)
            )
            // Displays  the podcast creator.
            Text(
                text = podcast.artistName,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFE6E8FF)
            )
            // Arranges the  subscription and back buttons together.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Adds  or removes the podcast subscription.
                Button(
                    onClick = {
                        subscriptionStorage.toggleSubscription(
                            podcast.collectionId
                        )
                        isSubscribed = !isSubscribed
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7A3FF2),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    // Displays  the  current subscription action.
                    Text(
                        if (isSubscribed) {
                            "Unsubscribe"
                        } else {
                            "Subscribe"
                        }
                    )
                }
                // Returns the  user to the podcast search screen.
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00A8C6),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    // Displays  the back-button label.
                    Text("Back")
                }
            }
            // Displays  progress while episodes are loading.
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFF52E5FF)
                )
            }
            // Displays  an episode-loading error.
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFF8FA3)
                )
            }
            //  Displays the episode-section heading.
            if (episodes.isNotEmpty()) {
                Text(
                    text = "Latest Episodes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            //  Displays the episodes as matching coloured cards.
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(episodes) { episode ->
                    //   Displays one episode and its playback controls.
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E2459)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //   Arranges the episode title and controls.
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {
                            //   Displays the episode title.
                            Text(
                                text = episode.trackName
                                    ?: "Untitled Episode",
                                style =
                                    MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            //  Arranges the playback buttons horizontally.
                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {
                                //  Plays the selected episode.
                                Button(
                                    onClick = {
                                        episode.episodeUrl?.let {
                                                audioUrl ->
                                            podcastPlayer.playEpisode(
                                                audioUrl
                                            )
                                        }
                                    },
                                    enabled = episode.episodeUrl != null,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor =
                                            Color(0xFF7A3FF2),
                                        contentColor = Color.White,
                                        disabledContainerColor =
                                            Color(0xFF4B3D66)
                                    )
                                ) {
                                    //  Displays the play-button label.
                                    Text("Play")
                                }
                                //  Stops the episode currently playing.
                                Button(
                                    onClick = {
                                        podcastPlayer.stopEpisode()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor =
                                            Color(0xFF00A8C6),
                                        contentColor = Color.White
                                    )
                                ) {
                                    //  Displays the stop-button label.
                                    Text("Stop")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}