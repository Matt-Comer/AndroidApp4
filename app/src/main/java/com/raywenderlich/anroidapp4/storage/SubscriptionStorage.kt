package com.raywenderlich.anroidapp4.storage
// Imports the Android  application context.
import android.content.Context
//  Stores podcast subscriptions on the device.
class SubscriptionStorage(context: Context) {
    // Opens the file used to save subscriptions.
    private val preferences = context.getSharedPreferences(
        "podcast_subscriptions",
        Context.MODE_PRIVATE
    )
    // Reports whether a podcast is  already subscribed.
    fun isSubscribed(podcastId: Long): Boolean {
        return getSubscribedIds().contains(podcastId.toString())
    }
    // Adds or removes a podcast  subscription.
    fun toggleSubscription(podcastId: Long) {
        // Creates an editable  copy of the saved podcast IDs.
        val subscribedIds = getSubscribedIds().toMutableSet()
        // Adds or removes the  selected podcast.
        if (subscribedIds.contains(podcastId.toString())) {
            subscribedIds.remove(podcastId.toString())
        } else {
            subscribedIds.add(podcastId.toString())
        }
        // Saves the updated  subscription list.
        preferences.edit()
            .putStringSet("subscribed_ids", subscribedIds)
            .apply()
    }
    // Returns the podcast  IDs saved on the device.
    private fun getSubscribedIds(): Set<String> {
        return preferences.getStringSet(
            "subscribed_ids",
            emptySet()
        ) ?: emptySet()
    }
}