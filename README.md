# 💃 DanceTempo — Android App

Eine moderne, native Android-App zur präzisen Rhythmus- und Tempoanalyse sowie zur annotierten Song-Bibliothek für **Standard-**, **Latein-** und **Social-Tänze**.

> ### 🤖 KI-Entwicklungs-Hinweis (AI Disclaimer)
> Dieses gesamte Projekt (Architektur, UI/UX-Design in Jetpack Compose, Signal- und Rhythmusanalyse-Algorithmen, Datenbankdesign in Room, Nix-Entwicklungsumgebung sowie CI/CD-Pipelines) wurde **vollständig unter Einsatz moderner KI-Coding-Tools (LLM Agentic Coding im interaktiven Pair-Programming)** konzipiert, implementiert, getestet und gewartet.

---

## 🎯 Zielgruppe & Tanzschulfokus

Die App ist zu 100 % auf **Hobbytanz, Tanzschulen (ADTV / Welttanzprogramm WTP) und Social Dancing** ausgerichtet:
* Erkennt und toleriert tanzschulgerechte Tempi moderner Pop-, Rock- und Swingmusik (z. B. entspannter Quickstep/Foxtrott bei 110–135 BPM, gemütlicher Wiener Walzer ab 150 BPM, Jive ab 135 BPM).
* **Ballroom-Tango vs. Tango Argentino:** Die App fokussiert sich auf den klassischen europäischen / internationalen Standard-Tango (`Tango (Ballroom)`). Der argentinische Tango (Tango de Salón / Milonga / Tango Vals) folgt einer grundlegend anderen Improvisations- und Tempokultur und wird daher bewusst separat gehalten.
* **Fokussiertes Interface:** Auf komplexe Turniersport-Regelwerke (WDSF) wurde zugunsten einer schnellen, übersichtlichen und alltagstauglichen Bedienung verzichtet.

---

## 🎵 Referenztabelle der Tempi (Tanzschule & Welttanzprogramm)

| Tanz | Taktart | Tanzschul-Ideal (BPM) | Toleranzbereich (BPM) | Charakteristik |
| :--- | :--- | :--- | :--- | :--- |
| **Quickstep** | 4/4 | **116–126** | 98–142 | Spritzig zu Pop- & Swingmusik mit Chassés & Locksteps |
| **Foxtrott (Social)** | 4/4 | **112–128** | 104–138 | Der Allrounder im Welttanzprogramm (Slow-Slow-Quick-Quick) |
| **Langsamer Walzer** | 3/4 | **84–90** | 78–94 | Weicher, fließender 3/4-Schwungtakt |
| **Wiener Walzer** | 3/4 | **150–170** | 135–185 | Tanzschulgerechtes Pendeln ab 150 BPM bis zum flotten Walzer |
| **Tango (Ballroom)** | 2/4 (4/4) | **120–128** | 116–134 | Europäischer Standard-Tango, Staccato-Schritte ohne Hüftaktion |
| **Slowfox** | 4/4 | **112–118** | 106–122 | Eleganter, fließender Slow-Quick-Quick Swing |
| **Cha-Cha-Cha** | 4/4 | **118–126** | 112–132 | Kubanischer Rhythmus mit synkopischem Chassé auf 4-und-1 |
| **Rumba** | 4/4 | **98–106** | 92–112 | Romantischer, ruhiger Latin-Groove mit Schrittpause auf 1 |
| **Samba** | 2/4 | **96–104** | 90–110 | Federnder Bounce-Rhythmus (1-a-2) |
| **Jive** | 4/4 | **140–164** | 128–176 | Tanzschulgerecht entspannt bis spritzig-sportlich |
| **Paso Doble** | 2/4 | **118–124** | 112–126 | Strenger spanischer Marsch- und Flamenco-Charakter |
| **Discofox** | 4/4 | **120–132** | 112–142 | Durchgehender 4-on-the-Floor Pop- & Partybeat (1-2-Tap) |
| **West Coast Swing** | 4/4 | **85–115** | 76–124 | Elastischer Slot-Dance zu R&B, Blues und moderner Popmusik |

---

## 📚 Quellenverzeichnis

Die hinterlegten Normen und Toleranzbereiche basieren auf den Richtlinien und Erfahrungswerten des Gesellschaftstanzes:

1. **Allgemeiner Deutscher Tanzlehrerverband (ADTV) & Welttanzprogramm (WTP):**
   * Richtlinien zum Welttanzprogramm für Gesellschaftstanz: [tanzen.de (ADTV)](https://www.tanzen.de/)
   * Tanzschul-Übersicht: [Tanzschule Waal – Tempo der Tänze im Welttanzprogramm](https://www.tanzen-waal.de/post/tempo-der-t%C3%A4nze-im-welttanzprogramm)
2. **Deutscher Tanzsportverband (DTV):**
   * Breitensport- und Tanzsportordnung: [DTV Ordnungen](https://www.tanzsport.de/de/sportwelt/leistungssport/ordnungen)
3. **World DanceSport Federation (WDSF):**
   * Grundmetren und Taktsysteme der Standard- und Lateintänze: [WDSF Rules Portal](https://www.worlddancesport.org/Rule/Competition/General)

---

## 🚀 Kernfunktionen

### 1. Tapper & Rhythmusanalyse
* **Ergonomisches Daumen-Layout:** Große Tap-Pads befinden sich am unteren Bildschirmrand.
* **1.-Schlag-Downbeat-Erkennung:**
  * Bei zugeschaltetem Toggle teilt sich der Tap-Bereich: Links das orangefarbene Feld für Schlag 1 (Downbeat), rechts das blaue Feld für die Folgeschläge (2, 3, 4...).
  * Erkennt automatisch das Metrum (z. B. `3/4` Walzer vs. `4/4` Quickstep/Cha-Cha-Cha).
* **Rhythmik-Karten & Musikcharakter:** Kompakte Hilfestellungen direkt auf jeder Tanzkarte beschreiben den musikalischen Charakter (z. B. Habanera-Marsch im Tango, 4-on-the-Floor im Discofox, federnder Samba-Bounce) zur sicheren Tanzwahl.
* **Visuelle Tempo-Ampel:**
  * 🟢 **Grün:** Tanzschul-Ideal
  * 🔵 **Blau:** Gemütlich / Einsteiger
  * 🟡 **Gelb:** Spritzig / Sportlich
* **Ausschluss-Begründungen:** Zeigt bei nicht passenden Tänzen konkret an, warum sie nicht zutreffen (falsche Taktart, zu langsam inkl. BPM-Differenz $\Delta$, zu schnell).

### 2. Song-Bibliothek, Tanz-Mixer & Backup
* **Schnelles Speichern & Duplikaterkennung:** Lesezeichen-Icon auf jeder Tanzkarte öffnet direkt das Vorbefüllungs-Sheet. Existiert ein Song bereits, werden neue Tanzstile automatisch ergänzt statt Duplikate anzulegen.
* **🎶 Tanz-Mixer (Playlist-Generator):** Generiert per Knopfdruck abwechslungsreiche Tanzabend-Playlists nach Wunschdauer (15, 30, 60 Min oder alle Songs) mit Anti-Ermüdungsschutz (keine zwei schnellen Tänze hintereinander).
* **Automatische Erkennung via MediaSession:** Liest Titel, Interpret und Track-URI laufender Spotify-, YouTube-Music- oder Deezer-Wiedergaben automatisch aus.
* **Multi-Tanz-Annotation:** Ein Song kann mehreren Tänzen gleichzeitig zugewiesen werden.
* **Einstellbare Standard-Musik-App:** In den Einstellungen kann der bevorzugte Player gewählt werden (Spotify, YouTube Music, Deezer, Apple Music oder System-Standard).
* **Direktes Abspielen & Deep-Link Suche:**
  * Ein Klick auf `▶ Abspielen` startet den Song direkt in der gewählten Standard-App (via Android Media Search Intent oder direktem Link).
* **Farbcodierte BPM-Badges:** Die BPM-Anzeige in der Bibliotheksliste übernimmt die Farbcodierung passend zum gefilterten Tanz.
* **Import & Export (Backup):** Über das Einstellungsmenü kann die gesamte Song-Bibliothek als JSON exportiert und wiederhergestellt werden (Storage Access Framework).

---

## 📥 Automatischer APK-Download (Releases)

Jeder Push auf den `main`-Branch löst über **GitHub Actions** einen automatisierten Build aus, der die neueste Android-APK baut und unter Releases bereitstellt:

👉 **[Neueste APK herunterladen (Rolling Pre-Release)](https://github.com/Pianokruemel/dance-tempo-app/releases/tag/latest)**

---

## 🛠️ Entwicklung mit Nix & Android

Das Projekt ist reproduzierbar mit Nix (`shell.nix`) vorkonfiguriert (OpenJDK 17 + Android SDK 35).

```bash
# 1. Nix-Shell starten
nix-shell

# 2. Tests ausführen
./gradlew test

# 3. APK lokal bauen
./gradlew assembleDebug

# 4. Auf verbundenem Android-Smartphone installieren
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 Lizenz

Dieses Projekt ist unter der **GNU General Public License v3.0 (GPLv3)** lizenziert — siehe [LICENSE](LICENSE).
Alle abgeleiteten Arbeiten müssen unter denselben freien Lizenzbedingungen quelloffen bleiben.
