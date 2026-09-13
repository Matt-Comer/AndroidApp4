package com.raywenderlich.androidapp4.ui
// Imports layout tools used to arrange the screen.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
// Imports the scrolling list used to display podcast results.
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Imports Material components used by the screen.
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
// Imports Compose state tools.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
// Imports sizing and spacing values.
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Imports the podcast data class.
import com.raywenderlich.androidapp4.data.Podcast
// Imports the repository used to search for podcasts.
import com.raywenderlich.androidapp4.repository.PodcastRepository
// Imports coroutine support for the API request.
import kotlinx.coroutines.launch
// Displays the podcast search screen.
@Composable
fun PodcastScreen(
    onPodcastClick: (Podcast) -> Unit
) {
    // Stores the text entered into the search field.
    var searchTerm by remember { mutableStateOf("") }
    // Stores the podcasts returned by the API.
    var podcasts by remember { mutableStateOf(emptyList<Podcast>()) }
    // Stores whether the app is currently loading results.
    var isLoading by remember { mutableStateOf(false) }
    // Creates the repository used for podcast searches.
    val repository = remember { PodcastRepository() }
    // Creates a coroutine scope for running the API request.
    val coroutineScope = rememberCoroutineScope()
    // Arranges the title, search controls, and results vertically.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Displays the screen title.
        Text(
            text = "Podcast Search",
            style = MaterialTheme.typography.headlineMedium
        )
        // Allows the user to enter a podcast search term.
        OutlinedTextField(
            value = searchTerm,
            onValueChange = { searchTerm = it },
            label = { Text("Search podcasts") },
            modifier = Modifier.fillMaxWidth()
        )
        // Starts the podcast search when selected.
        Button(
            onClick = {
                // Prevents an empty search from being submitted.
                if (searchTerm.isNotBlank()) {
                    // Runs the API request without blocking the interface.
                    coroutineScope.launch {
                        // Displays the loading indicator.
                        isLoading = true
                        // Retrieves and stores the matching podcasts.
                        podcasts = repository.searchPodcasts(searchTerm).results
                        // Hides the loading indicator.
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Displays the search-button label.
            Text("Search")
        }
        // Displays progress while the podcast results are loading.
        if (isLoading) {
            CircularProgressIndicator()
        }
        // Displays the podcasts returned by the API.
        LazyColumn {
            items(podcasts) { podcast ->
                // Displays one selectable podcast result.
                Button(
                    onClick = { onPodcastClick(podcast) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // Displays the podcast title and creator.
                    Text(
                        text = "${podcast.collectionName}\n${podcast.artistName}"
                    )
                }
            }
        }
    }
}