package com.jonwal.dancetempo.data.service

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class MediaNotificationListenerService : NotificationListenerService() {

    private val spotifyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val action = intent.action ?: return
            if (action == "com.spotify.music.metadatachanged") {
                val track = intent.getStringExtra("track")
                val artist = intent.getStringExtra("artist")
                val trackUri = intent.getStringExtra("id")
                if (!track.isNullOrBlank()) {
                    MediaPlaybackTracker.updateTrack(track, artist, "com.spotify.music", trackUri)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter().apply {
            addAction("com.spotify.music.metadatachanged")
            addAction("com.spotify.music.playbackstatechanged")
        }
        try {
            registerReceiver(spotifyReceiver, filter, Context.RECEIVER_EXPORTED)
        } catch (_: Exception) {
            try {
                registerReceiver(spotifyReceiver, filter)
            } catch (_: Exception) {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(spotifyReceiver)
        } catch (_: Exception) {}
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        // Immediately scan active sessions on connect
        MediaPlaybackTracker.scanForActiveSession(applicationContext)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return

        val extras = sbn.notification.extras ?: return
        val isMedia = sbn.notification.category == Notification.CATEGORY_TRANSPORT ||
                extras.containsKey(Notification.EXTRA_MEDIA_SESSION)

        if (isMedia) {
            val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
            val artist = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
                ?: extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString()

            if (!title.isNullOrBlank()) {
                MediaPlaybackTracker.updateTrack(
                    title = title,
                    artist = artist,
                    sourcePackage = sbn.packageName
                )
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        val current = MediaPlaybackTracker.currentTrack.value
        if (current != null && current.sourcePackage == sbn?.packageName) {
            MediaPlaybackTracker.clear()
        }
    }
}
