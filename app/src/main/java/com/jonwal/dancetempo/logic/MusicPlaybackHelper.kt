package com.jonwal.dancetempo.logic

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.jonwal.dancetempo.data.local.PreferredMusicApp
import com.jonwal.dancetempo.model.Song
import java.net.URLEncoder

object MusicPlaybackHelper {

    fun playSong(context: Context, song: Song, preferredApp: PreferredMusicApp) {
        val query = if (song.artist.isNotBlank()) "${song.title} ${song.artist}" else song.title

        // 1. Direct streaming URL if it matches the preferred app
        if (!song.streamingUrl.isNullOrBlank()) {
            val url = song.streamingUrl
            val isSpotify = url.contains("spotify.com")
            val isYt = url.contains("youtube.com") || url.contains("youtu.be")
            val isDeezer = url.contains("deezer.com")
            val isApple = url.contains("apple.com")

            val matchesApp = when (preferredApp) {
                PreferredMusicApp.SPOTIFY -> isSpotify
                PreferredMusicApp.YOUTUBE_MUSIC -> isYt
                PreferredMusicApp.DEEZER -> isDeezer
                PreferredMusicApp.APPLE_MUSIC -> isApple
                PreferredMusicApp.SYSTEM_DEFAULT -> true
            }

            if (matchesApp) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    preferredApp.packageName?.let { `package` = it }
                }
                try {
                    context.startActivity(intent)
                    return
                } catch (_: Exception) {
                    try {
                        val fallback = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(fallback)
                        return
                    } catch (_: Exception) {}
                }
            }
        }

        // 2. Play from search intent (starts instant playback in Spotify/Deezer/YT Music)
        try {
            val mediaIntent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(MediaStore.EXTRA_MEDIA_FOCUS, "vnd.android.cursor.item/*")
                putExtra(MediaStore.EXTRA_MEDIA_TITLE, song.title)
                if (song.artist.isNotBlank()) {
                    putExtra(MediaStore.EXTRA_MEDIA_ARTIST, song.artist)
                }
                putExtra(SearchManager.QUERY, query)
                preferredApp.packageName?.let { `package` = it }
            }
            context.startActivity(mediaIntent)
            return
        } catch (_: Exception) {}

        // 3. Fallback search deep-link for preferred app
        val encoded = try {
            URLEncoder.encode(query, "UTF-8")
        } catch (_: Exception) {
            query.replace(" ", "+")
        }

        val fallbackUrl = when (preferredApp) {
            PreferredMusicApp.SPOTIFY -> "https://open.spotify.com/search/$encoded"
            PreferredMusicApp.YOUTUBE_MUSIC -> "https://music.youtube.com/search?q=$encoded"
            PreferredMusicApp.DEEZER -> "https://www.deezer.com/search/$encoded"
            PreferredMusicApp.APPLE_MUSIC -> "https://music.apple.com/search?term=$encoded"
            PreferredMusicApp.SYSTEM_DEFAULT -> "https://open.spotify.com/search/$encoded"
        }

        try {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                preferredApp.packageName?.let { `package` = it }
            }
            context.startActivity(appIntent)
        } catch (_: Exception) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(browserIntent)
        }
    }
}
