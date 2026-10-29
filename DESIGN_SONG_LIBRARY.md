# Finales Designdokument: Song-Bibliothek & Tanz-Annotation (DanceTempo)

## 1. Übersicht
Nutzer können während des Musikhörens das Tempo (BPM) ermitteln und den Song per Klick auf ein Speichern-Icon auf der entsprechenden Tanz-Ergebniskarte in eine lokale Bibliothek überführen. Die Bibliothek ermöglicht das Filtern über flexible Tanz-Chips (Multi-Select) und das Umschalten der Sortierung nach Bewertung, Tempo oder Hinzufügedatum.

---

## 2. Speichern-Workflow aus dem Tapper

### 2.1 Auslöser auf der Tanzkarte (Entscheidung: Variante A)
* Jede passende Tanzkarte (z. B. *Cha-Cha-Cha*) erhält in der Kopfzeile neben dem Tempo-Badge ein unaufdringliches **Lesezeichen- / Bookmark-Icon** (`Icons.Default.BookmarkAdd`).
* Das Tippen darauf öffnet ein leichtgewichtiges **Modal BottomSheet**:
  1. **Header:** Zeigt BPM (z. B. `124 BPM`) und Taktart (`4/4`).
  2. **Titel & Interpret:**
     * Wird automatisch aus einer laufenden Player-Benachrichtigung (Spotify / YouTube Music) vorausgefüllt, sofern die Berechtigung erteilt ist.
     * Manuell bearbeitbar / eingebbar.
  3. **Tanz-Tags (Multi-Select Chips):**
     * Der Tanz der angetippten Karte ist standardmäßig aktiv (z. B. `[✓ Cha-Cha-Cha]`).
     * Weitere Tänze, die ebenfalls zur gemessenen BPM passen (z. B. `[+ Tango]`, `[+ Discofox]`), werden als Vorschlags-Chips eingeblendet und können mit einem Fingertipp hinzugefügt werden.
  4. **Bewertung (1–5 Sterne):** Interaktive Sterne-Leiste (Standard: 4 oder 5 Sterne).
  5. **Notiz & Streaming-Link (optional):** Einklappbar oder kurze Textzeile für Spotify-Link / Trainingsnotiz.
  6. **Button:** *„In Bibliothek speichern“*.

---

## 3. UI/UX der Bibliothek

### 3.1 Bildschirmaufbau
1. **TopBar:**
   * Titel: *„Meine Tanzsongs“* mit Song-Zähler (z. B. `24 Songs`).
   * Such-Button (blendet Suchleiste ein für Titel / Artist).
   * Sortier-Menü (Icon-Button mit Dropdown):
     * ⭐ *Beste Bewertung* (5 → 1 Sterne)
     * ⏱️ *Tempo (BPM)* (Langsam → Schnell oder Schnell → Langsam)
     * 🕒 *Zuletzt hinzugefügt* (Neueste zuerst)

2. **Horizontale Tanz-Filter-Chips (Multi-Select):**
   * Erste Position: `Alle` (wenn aktiv, werden alle Songs angezeigt).
   * Gefolgt von den Tänzen: `Langsamer Walzer`, `Wiener Walzer`, `Tango`, `Slowfox`, `Quickstep`, `Samba`, `Cha-Cha-Cha`, `Rumba`, `Paso Doble`, `Jive`, `Discofox`, `West Coast Swing`.
   * **Multi-Select-Verhalten:**
     * Antippen von `Cha-Cha-Cha` filtert auf diesen Tanz.
     * Weiteres Antippen von `Discofox` zeigt alle Songs, die *mindestens einen* dieser ausgewählten Tänze annotiert haben.
     * Antippen von `Alle` setzt die Filter zurück.

3. **Song-Liste (Kompakte Zeilenansicht):**
   * **Linke Seite:**
     * Songtitel (fett, 1 Zeile, truncate)
     * Interpret (etwas kleiner, dezente Textfarbe)
     * Kleine farbige Text-Badges der zugeordneten Tänze (z. B. `Cha-Cha`, `Discofox`)
   * **Rechte Seite:**
     * BPM-Badge (z. B. `124 BPM`)
     * Sterne-Anzeige (z. B. `★★★★★` in Akzentfarbe)
   * **Klick auf eine Zeile:**
     * Öffnet eine kompakte Detail- / Bearbeitungsansicht:
       * Vollständige Notiz anzeigen
       * Wenn Streaming-Link vorhanden: Play-Button zum direkten Starten der Spotify- / Streaming-App via Android Intent
       * Bearbeiten & Löschen (`Icons.Default.Delete`)

---

## 4. Navigation (Bottom Navigation Bar)
* **Tab 1: `Tapper`** (Metronom/Tap-Icon)
  * Bestehende Ansicht: Header-Stats, Ergebnisliste (Matches & Mismatches), Tap-Pads ganz unten.
* **Tab 2: `Bibliothek`** (Musikbibliothek/Bookmark-Icon)
  * Filter-Chips, Sortierung, Songliste.

---

## 5. Datenmodell & Speicherung

### Room-Entities:
* `SongEntity`:
  * `id: Long` (Primary Key, Auto-Generate)
  * `title: String`
  * `artist: String`
  * `bpm: Int`
  * `meter: Int`
  * `rating: Int` (1..5)
  * `notes: String?`
  * `streamingUrl: String?`
  * `createdAt: Long`
* `SongDanceCrossRef`:
  * `songId: Long` (FK -> `SongEntity.id` Cascade Delete)
  * `danceId: String` (z. B. `"cha_cha"`)
  * PK: `(songId, danceId)`

### Abfragemöglichkeiten (`SongDao`):
* `getSongsWithDances(sortOrder: SortOrder, danceFilter: List<String>?, query: String): Flow<List<SongWithDances>>`
