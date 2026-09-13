package com.raywenderlich.androidapp4.repository
// Imports  the  episode response returned by the API.
import com.raywenderlich.androidapp4.data.EpisodeResponse
// Imports the podcast  response returned by the API.
import com.raywenderlich.androidapp4.data.PodcastResponse
//  Imports Retrofit for creating the API connection.
import retrofit2.Retrofit
// Imports the Gson  converter used to read JSON data.
import retrofit2.converter.gson.GsonConverterFactory
// Imports the annotation  used for GET requests.
import retrofit2.http.GET
// Imports  the annotation used for query values.
import retrofit2.http.Query
// Defines the requests sent to the iTunes API.
interface PodcastApiService {
    // Searches iTunes for podcasts matching the entered term.
    @GET("search")
    suspend fun searchPodcasts(
        @Query("term") term: String,
        @Query("media") media: String = "podcast"
    ): PodcastResponse
    // Retrieves episodes belonging to the selected podcast.
    @GET("lookup")
    suspend fun getEpisodes(
        @Query("id") podcastId: Long,
        @Query("entity") entity: String = "podcastEpisode",
        @Query("limit") limit: Int = 20
    ): EpisodeResponse
}
//  Retrieves podcast and  episode information for the application.
class PodcastRepository {
    // Creates the Retrofit  connection to the iTunes API.
    private val podcastApi = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PodcastApiService::class.java)
    // Returns podcasts  matching the entered search term.
    suspend fun searchPodcasts(term: String): PodcastResponse {
        return podcastApi.searchPodcasts(term)
    }
    //   playable  episodes for the selected podcast.
    suspend fun getEpisodes(podcastId: Long) =
        podcastApi.getEpisodes(podcastId).results.filter {
            it.episodeUrl != null
        }
}