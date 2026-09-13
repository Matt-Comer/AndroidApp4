SuperPodcast
SuperPodcast is an Android podcast application created for Assignment 7. It uses the iTunes Search API to find podcasts, display real episodes, save subscriptions, and play episode audio.
Features
- Search for podcasts using the iTunes Search API
- Display matching podcast titles and creators
- Filter results by podcast-title word count
  - All titles
  - Titles containing 1–3 words
  - Titles containing 4 or more words
- Open a selected podcast and retrieve its real episodes
- Play and stop podcast episode audio
- Subscribe and unsubscribe from a podcast
- Save subscriptions locally so they remain after the app restarts
- Display loading indicators and basic API error messages
Technology Used
- Kotlin
- Android Studio
- Jetpack Compose
- Retrofit
- Gson converter
- Kotlin coroutines
- Android MediaPlayer
- Android SharedPreferences
- iTunes Search API
Project Structure
data/
  PodcastResponse.kt       Podcast and episode data classes

player/
  PodcaastPlayer.kt        Episode audio playback

repository/
  PodfcastRepository.kt    Retrofit API requests

storage/
  SubscriptionStorage.kt   Locally saved subscriptions

ui/
  PodcastSceen.kt          Search results and word-count filtering
  EpisodeScreen.kt         Episodes, subscriptions, and playback controls

MainActivity.kt            Switches between the search and episode screens
How the App Works
1. Enter a podcast name in the search field.
2. Select Search to retrieve matching podcasts.
3. Use the filter button to show all titles, 1–3 word titles, or titles with 4 or more words.
4. Select a podcast to load its episodes.
5. Use Subscribe or Unsubscribe to save the podcast preference.
6. Use Play and Stop to control episode audio.
7. Select Back to Podcasts to return to the search results.
Running the Project
1. Open the project in Android Studio.
2. Allow Gradle to finish syncing.
3. Start an Android emulator or connect an Android device.
4. Run the app configuration.
5. An internet connection is required for podcast searches, episode retrieval, and playback.
Repository
GitHub – AndroidApp4
Author
Matthew Comer
