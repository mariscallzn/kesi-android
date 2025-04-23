package com.kesicollection.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.kesicollection.data.api.BookmarkApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [DataStoreBookmarkApi] is an implementation of [BookmarkApi] that utilizes
 * Android's DataStore to manage article bookmarking.
 *
 * It uses `Preferences` to store whether an article is bookmarked or not.
 * Each article's bookmark status is stored with a unique boolean preference key,
 * which is derived from the article's ID.
 *
 * @property dataStore The DataStore instance used for persistent storage of bookmark data.
 * @constructor Injects the DataStore dependency.
 */
class DataStoreBookmarkApi @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : BookmarkApi {
    override suspend fun bookmarkArticleById(id: String) {
        dataStore.edit { settings ->
            settings[booleanPreferencesKey(id)]?.let {
                settings.remove(booleanPreferencesKey(id))
            } ?: with(settings) {
                this[booleanPreferencesKey(id)] = true
            }
        }
    }

    override suspend fun isBookmarked(id: String): Boolean = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(id)] != null
    }.firstOrNull() ?: false
}