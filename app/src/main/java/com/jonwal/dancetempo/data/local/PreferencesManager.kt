package com.jonwal.dancetempo.data.local

import android.content.Context
import android.content.SharedPreferences

enum class PreferredMusicApp(
    val displayName: String,
    val packageName: String?,
    val searchUrlPrefix: String?
) {
    SPOTIFY(
        displayName = "Spotify",
        packageName = "com.spotify.music",
        searchUrlPrefix = "https://open.spotify.com/search/"
    ),
    YOUTUBE_MUSIC(
        displayName = "YouTube Music",
        packageName = "com.google.android.apps.youtube.music",
        searchUrlPrefix = "https://music.youtube.com/search?q="
    ),
    DEEZER(
        displayName = "Deezer",
        packageName = "deezer.android.app",
        searchUrlPrefix = "https://www.deezer.com/search/"
    ),
    APPLE_MUSIC(
        displayName = "Apple Music",
        packageName = "com.apple.android.music",
        searchUrlPrefix = "https://music.apple.com/search?term="
    ),
    SYSTEM_DEFAULT(
        displayName = "System-Standard / Installierte App",
        packageName = null,
        searchUrlPrefix = null
    )
}

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("dance_tempo_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PREFERRED_MUSIC_APP = "preferred_music_app"
    }

    fun getPreferredMusicApp(): PreferredMusicApp {
        val storedName = prefs.getString(KEY_PREFERRED_MUSIC_APP, PreferredMusicApp.SPOTIFY.name)
        return try {
            PreferredMusicApp.valueOf(storedName ?: PreferredMusicApp.SPOTIFY.name)
        } catch (_: Exception) {
            PreferredMusicApp.SPOTIFY
        }
    }

    fun setPreferredMusicApp(app: PreferredMusicApp) {
        prefs.edit().putString(KEY_PREFERRED_MUSIC_APP, app.name).apply()
    }
}
