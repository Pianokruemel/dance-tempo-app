package com.jonwal.dancetempo.data.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.provider.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.URLEncoder

data class DetectedTrack(
    val title: String,
    val artist: String,
    val streamingUrl: String? = null,
    val sourcePackage: String? = null
)

sealed class SessionScanResult {
    object Idle : SessionScanResult()
    object Scanning : SessionScanResult()
    object PermissionRequired : SessionScanResult()
    data class Found(val track: DetectedTrack) : SessionScanResult()
    object NotFound : SessionScanResult()
}

object MediaPlaybackTracker {
    private val _currentTrack = MutableStateFlow<DetectedTrack?>(null)
    val currentTrack: StateFlow<DetectedTrack?> = _currentTrack.asStateFlow()

    fun updateTrack(title: String?, artist: String?, sourcePackage: String?, rawIdOrUrl: String? = null) {
        if (!title.isNullOrBlank()) {
            val cleanTitle = title.trim()
            val cleanArtist = artist?.trim() ?: ""
            val resolvedUrl = resolveStreamingUrl(cleanTitle, cleanArtist, sourcePackage, rawIdOrUrl)

            _currentTrack.value = DetectedTrack(
                title = cleanTitle,
                artist = cleanArtist,
                streamingUrl = resolvedUrl,
                sourcePackage = sourcePackage
            )
        }
    }

    fun clear() {
        _currentTrack.value = null
    }

    /**
     * Resolves direct web links for Spotify, YouTube Music, or generates search fallback links.
     */
    fun resolveStreamingUrl(
        title: String,
        artist: String,
        sourcePackage: String?,
        rawIdOrUrl: String?
    ): String {
        // 1. Direct Spotify URI (e.g. "spotify:track:4iV5W9uYEdYUVa79Axb7Rh")
        if (rawIdOrUrl != null) {
            if (rawIdOrUrl.startsWith("spotify:track:")) {
                val trackId = rawIdOrUrl.removePrefix("spotify:track:")
                return "https://open.spotify.com/track/$trackId"
            }
            if (rawIdOrUrl.startsWith("http://") || rawIdOrUrl.startsWith("https://")) {
                return rawIdOrUrl
            }
            if (sourcePackage == "com.spotify.music" && rawIdOrUrl.length in 15..30 && !rawIdOrUrl.contains(" ")) {
                return "https://open.spotify.com/track/$rawIdOrUrl"
            }
            if (sourcePackage == "com.google.android.apps.youtube.music" && rawIdOrUrl.length in 10..15) {
                return "https://music.youtube.com/watch?v=$rawIdOrUrl"
            }
        }

        // 2. Search URL generator
        val encodedQuery = try {
            URLEncoder.encode(if (artist.isNotBlank()) "$title $artist" else title, "UTF-8")
        } catch (_: Exception) {
            title.replace(" ", "+")
        }

        return if (sourcePackage == "com.spotify.music" || sourcePackage == null) {
            "https://open.spotify.com/search/$encodedQuery"
        } else if (sourcePackage.contains("youtube")) {
            "https://music.youtube.com/search?q=$encodedQuery"
        } else {
            "https://open.spotify.com/search/$encodedQuery"
        }
    }

    fun isNotificationAccessGranted(context: Context): Boolean {
        val packageName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(packageName)
    }

    fun openNotificationSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            val fallback = Intent(Settings.ACTION_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(fallback)
        }
    }

    fun scanForActiveSession(context: Context): SessionScanResult {
        if (!isNotificationAccessGranted(context)) {
            return SessionScanResult.PermissionRequired
        }

        try {
            val sessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as? MediaSessionManager
            val componentName = ComponentName(context, MediaNotificationListenerService::class.java)
            val controllers = sessionManager?.getActiveSessions(componentName)

            if (!controllers.isNullOrEmpty()) {
                for (controller in controllers) {
                    val metadata = controller.metadata
                    val title = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)
                    val artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST)
                        ?: metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
                        ?: metadata?.getString(MediaMetadata.METADATA_KEY_AUTHOR)

                    val rawId = metadata?.getString(MediaMetadata.METADATA_KEY_MEDIA_ID)
                        ?: metadata?.getString(MediaMetadata.METADATA_KEY_MEDIA_URI)

                    if (!title.isNullOrBlank()) {
                        val resolvedUrl = resolveStreamingUrl(
                            title = title.trim(),
                            artist = artist?.trim() ?: "",
                            sourcePackage = controller.packageName,
                            rawIdOrUrl = rawId
                        )

                        val track = DetectedTrack(
                            title = title.trim(),
                            artist = artist?.trim() ?: "",
                            streamingUrl = resolvedUrl,
                            sourcePackage = controller.packageName
                        )
                        _currentTrack.value = track
                        return SessionScanResult.Found(track)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val cached = _currentTrack.value
        return if (cached != null) {
            SessionScanResult.Found(cached)
        } else {
            SessionScanResult.NotFound
        }
    }
}
