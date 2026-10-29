package com.jonwal.dancetempo.data.local

import com.jonwal.dancetempo.model.DanceRepository
import com.jonwal.dancetempo.model.Song
import org.json.JSONArray
import org.json.JSONObject

data class ImportResult(
    val importedCount: Int,
    val skippedCount: Int,
    val error: String? = null
)

object LibraryBackupHelper {

    /**
     * Serializes songs and their assigned dance IDs to a clean JSON string.
     */
    fun exportToJson(songs: List<Song>): String {
        val root = JSONObject()
        root.put("version", 1)
        root.put("app", "DanceTempo")
        root.put("exportedAt", System.currentTimeMillis())

        val array = JSONArray()
        songs.forEach { song ->
            val obj = JSONObject()
            obj.put("title", song.title)
            obj.put("artist", song.artist)
            obj.put("bpm", song.bpm)
            obj.put("meter", song.meter)
            obj.put("notes", song.notes ?: JSONObject.NULL)
            obj.put("streamingUrl", song.streamingUrl ?: JSONObject.NULL)

            val danceArray = JSONArray()
            song.dances.forEach { dance ->
                danceArray.put(dance.id)
            }
            obj.put("dances", danceArray)
            array.put(obj)
        }
        root.put("songs", array)

        return root.toString(2)
    }

    /**
     * Parses a JSON string and extracts Song objects for database insertion.
     */
    fun parseJson(jsonString: String): List<Pair<Song, List<String>>> {
        val result = mutableListOf<Pair<Song, List<String>>>()
        val root = JSONObject(jsonString)
        val array = root.optJSONArray("songs") ?: return emptyList()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val title = obj.getString("title")
            val artist = obj.optString("artist", "")
            val bpm = obj.getInt("bpm")
            val meter = obj.optInt("meter", 4)
            val notes = if (obj.isNull("notes")) null else obj.optString("notes", null)
            val streamingUrl = if (obj.isNull("streamingUrl")) null else obj.optString("streamingUrl", null)

            val danceIds = mutableListOf<String>()
            val danceArray = obj.optJSONArray("dances")
            if (danceArray != null) {
                for (j in 0 until danceArray.length()) {
                    danceIds.add(danceArray.getString(j))
                }
            }

            val dances = danceIds.mapNotNull { id -> DanceRepository.allDances.find { it.id == id } }

            val song = Song(
                title = title,
                artist = artist,
                bpm = bpm,
                meter = meter,
                notes = notes,
                streamingUrl = streamingUrl,
                dances = dances
            )

            result.add(Pair(song, danceIds))
        }

        return result
    }

    /**
     * Generates a readable CSV export.
     */
    fun exportToCsv(songs: List<Song>): String {
        val sb = StringBuilder()
        sb.append("Titel;Interpret;BPM;Taktart;Tänze;Notiz;Streaming-Link\n")
        songs.forEach { song ->
            val danceNames = song.dances.joinToString(", ") { it.name }
            val cleanTitle = song.title.replace(";", ",")
            val cleanArtist = song.artist.replace(";", ",")
            val cleanNotes = (song.notes ?: "").replace(";", ",").replace("\n", " ")
            val cleanUrl = song.streamingUrl ?: ""

            sb.append("\"$cleanTitle\";\"$cleanArtist\";${song.bpm};\"${song.meter}/4\";\"$danceNames\";\"$cleanNotes\";\"$cleanUrl\"\n")
        }
        return sb.toString()
    }
}
