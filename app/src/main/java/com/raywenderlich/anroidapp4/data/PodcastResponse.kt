package com.raywenderlich.androidapp4.data
// Stores  the complete response returned by  a podcast search.
data class PodcastResponse(
    // Stores   the number of podcast results returned.
    val resultCount: Int,
    // Stores the podcasts returned by the search.
    val results: List<Podcast>
)
// Stores the information for one  podcast.
data class Podcast(
    //   Stores the podcast's unique identification number.
    val collectionId: Long,
    //  Stores  the podcast title.
    val collectionName: String,
    // Stores  the podcast  creator's name.
    val artistName: String,
    //  Stores the URL for the podcast artwork.
    val artworkUrl100: String,
    // Stores the URL  used to retrieve the podcast feed.
    val feedUrl: String?
)
// Stores the complete response returned by an episode lookup.
data class EpisodeResponse(
    // Stores the number of episode  results returned.
    val resultCount: Int,
    // Stores the episodes returned  by the lookup.
    val results: List<Episode>
)
// Stores the information for one podcast episode.
data class Episode(
    // Identifies the type of result returned by iTunes.
    val wrapperType: String?,
    // Stores the episode's unique identification value.
    val episodeGuid: String?,
    // Stores the episode title.
    val trackName: String?,
    // Stores the episode description.
    val description: String?,
    // Stores the URL for the episode audio.
    val episodeUrl: String?,
    // Stores the episode release date.
    val releaseDate: String?
)