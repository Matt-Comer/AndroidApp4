package com.raywenderlich.androidapp4.ui
// Imports layout tools used  to arrange and size the screen.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
// Imports the  scrolling list used to display podcast results.
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Imports Material components used by the screen.
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
// Imports Compose state tools.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
// Imports gradient, colour, sizing and text-styling tools.
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Imports the podcast  data class.
import com.raywenderlich.androidapp4.data.Podcast

// Imports the repository used to search for podcasts.
import com.raywenderlich.androidapp4.repository.PodcastRepository
// Imports coroutine support for the API request.
import kotlinx.coroutines.launch
// Displays  the polished podcast search screen.
@Composable
fun PodcastScreen(
    onPodcastClick: (Podcast) -> Unit
) {
    // Stores the text entered into the search field.
    var searchTerm by remember {
        mutableStateOf("")
    }
    // Stores the podcasts returned by the API.
    var podcasts by remember {
        mutableStateOf(emptyList<Podcast>())
    }
    // Stores whether the app is currently loading results.
    var isLoading by remember {
        mutableStateOf(false)
    }
    // Stores the  selected title word-count filter.
    var filterMode by remember {
        mutableStateOf(0)
    }
    // Stores an error or input message for the user.
    var errorMessage by remember {
        mutableStateOf("")
    }
    // Stores whether a  search has been completed.
    var hasSearched by remember {
        mutableStateOf(false)
    }
    // Creates the repository used for podcast  searches.
    val repository = remember {
        PodcastRepository()
    }
    // Creates a coroutine scope for running the API request.
    val coroutineScope = rememberCoroutineScope()
    //Filters podcasts using the number of words in each title.
    val filteredPodcasts = when (filterMode) {
        1 -> podcasts.filter { podcast ->
            podcast.collectionName
                .trim()
                .split(Regex("\\s+"))
                .size <= 3
        }
        2 -> podcasts.filter { podcast ->
            podcast.collectionName
                .trim()
                .split(Regex("\\s+"))
                .size >= 4
        }
        else -> podcasts
    }
    // Creates  the main navy, purple  and blue background gradient.
    val screenGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF071426),
            Color(0xFF241044),
            Color(0xFF0B4F6C)
        )
    )
    //  Displays the gradient across the entire screen.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenGradient)
    ) {
        // Arranges the title, controls, messages and results vertically.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Displays the  application title.
            Text(
                text = "SuperPodcast",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF52E5FF),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            // Explains the purpose of the  application.
            Text(
                text = "Discover, filter and play your favourite podcasts.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFE6E8FF),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            //  Allows the user to enter a podcast search term.
            OutlinedTextField(
                value = searchTerm,
                onValueChange = {
                    // Stores the updated search text.
                    searchTerm = it
                    //  Removes the previous message when the user types.
                    errorMessage = ""
                },
                label = {
                    // Displays the search-field label.
                    Text("Search podcasts")
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF52E5FF),
                    unfocusedBorderColor = Color(0xFFA98BFF),
                    focusedLabelColor = Color(0xFF52E5FF),
                    unfocusedLabelColor = Color(0xFFD5C8FF),
                    cursorColor = Color(0xFF52E5FF)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            // Starts  the podcast  search when selected.
            Button(
                onClick = {
                    // Displays feedback when the search field is empty.
                    if (searchTerm.isBlank()) {
                        errorMessage = "Enter a podcast name."
                        hasSearched = false
                    } else {
                        // Runs the API request without blocking the interface.
                        coroutineScope.launch {
                            // Prepares the interface for a new search.
                            isLoading = true
                            errorMessage = ""
                            hasSearched = false

                            try {
                                // Retrieves  and stores matching podcasts.
                                podcasts = repository
                                    .searchPodcasts(searchTerm.trim())
                                    .results
                                // Records that the search completed.
                                hasSearched = true
                            } catch (exception: Exception) {
                                // Clears old results when the request fails.
                                podcasts = emptyList()
                                // Displays a network-error message.
                                errorMessage =
                                    "Unable to search. Check your internet connection."
                            } finally {
                                // Hides the loading indicator.
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7A3FF2),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF4B3D66)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Displays the  correct search-button label.
                Text(
                    if (isLoading) {
                        "Searching..."
                    } else {
                        "Search"
                    }
                )
            }
            // Changes the unusual  title word-count filter.
            Button(
                onClick = {
                    // Moves through all three filtering options.
                    filterMode = (filterMode + 1) % 3
                },
                enabled = podcasts.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00A8C6),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF34495E)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Displays the currently  selected filter.
                Text(
                    when (filterMode) {
                        1 -> "Filter: 1–3 Word Titles"
                        2 -> "Filter: 4+ Word Titles"
                        else -> "Filter: All Titles"
                    }
                )
            }
            //  Displays progress while  podcast results are loading.
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFF52E5FF)
                )
            }
            //  Displays an input or network-error message.
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFF8FA3)
                )
            }
            // Displays  feedback when a search  has no results.
            if (
                hasSearched &&
                podcasts.isEmpty() &&
                errorMessage.isEmpty()
            ) {
                Text(
                    text = "No podcasts found.",
                    color = Color.White
                )
            }
            // Displays the  number of results matching the filter.
            if (podcasts.isNotEmpty()) {
                Text(
                    text = "${filteredPodcasts.size} podcasts match this filter",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF52E5FF)
                )
            }
            // Displays feedback when  the filter has no matches.
            if (
                podcasts.isNotEmpty() &&
                filteredPodcasts.isEmpty()
            ) {
                Text(
                    text = "No podcast titles match this filter.",
                    color = Color.White
                )
            }
            // Displays the filtered  podcasts as colourful cards.
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredPodcasts) { podcast ->
                    // Displays one selectable  podcast result.
                    Card(
                        onClick = {
                            // Opens the selected podcast.
                            onPodcastClick(podcast)
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E2459)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Arranges the podcast title and creator.
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(5.dp)
                        ) {
                            // Displays the n podcast title.
                            Text(
                                text = podcast.collectionName,
                                style =
                                    MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            //  Displays the podcast creator.
                            Text(
                                text = podcast.artistName,
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFBDEEFF)
                            )
                        }
                    }
                }
            }
        }
    }
}