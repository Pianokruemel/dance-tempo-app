# 💃 DanceTempo — Android App

Eine native Android-App zur präzisen Rhythmus- und Tempoanalyse sowie zur annotierten Song-Bibliothek für **Standard-**, **Latein-** und **Social-Tänze**.

Die App unterstützt zwei klar getrennte Modi:
1. **Hobby / Tanzschule (Default):** Optimiert für ADTV / Welttanzprogramm (WTP) und Social Dancing zu moderner Pop-, Rock- und Swing-Musik.
2. **Turnier (WDSF / DTV):** Strikt nach den internationalen Wettkampf-Vorgaben der *World DanceSport Federation*.

---

## 🎵 Referenztabelle & Tempovorgaben (Hobby vs. Turnier)

Alle Tempowerte basieren auf offiziellen Richtlinien und sind unten detailliert quellenbelegt.

| Tanz | Taktart | Hobby-Ideal (BPM) | Hobby-Toleranz (BPM) | WDSF Turnier (TPM) | WDSF Turnier (BPM) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Quickstep** | 4/4 | **116–126** | 98–142 *(z. B. Pop/Swing)* | 50–52 | **200–208** *(Halbtakt 100–104)* |
| **Foxtrott (Social)** | 4/4 | **112–128** | 104–138 *(WTP Allrounder)* | – *(nur Slowfox/Quickstep)* | – |
| **Langsamer Walzer** | 3/4 | **84–90** | 78–94 | 28–30 | **84–90** |
| **Wiener Walzer** | 3/4 | **150–170** | 135–185 *(gemütlicher Einstieg)* | 58–60 | **174–180** |
| **Tango** | 2/4 (4/4) | **120–128** | 116–134 | 31–33 | **124–132** *(Viertel: 62–66)* |
| **Slowfox** | 4/4 | **112–118** | 106–122 | 28–30 | **112–120** |
| **Cha-Cha-Cha** | 4/4 | **118–126** | 112–132 | 30–32 | **120–128** |
| **Rumba** | 4/4 | **98–106** | 92–112 | 25–27 | **100–108** |
| **Samba** | 2/4 | **96–104** | 90–110 | 50–52 | **100–104** |
| **Jive** | 4/4 | **140–164** | 128–176 *(tanzschulgerecht)* | 42–44 | **168–176** |
| **Paso Doble** | 2/4 | **118–124** | 112–126 | 60–62 | **120–124** |
| **Discofox** | 4/4 | **120–132** | 112–142 | – | **124–132** |
| **West Coast Swing** | 4/4 | **85–115** | 76–124 | – | **85–115** |

---

## 📚 Quellenverzeichnis für alle Tempoangaben

Die in der App hinterlegten Tempi und Toleranzbereiche stützen sich auf folgende verbindliche Quellen:

1. **WDSF Competition Rules (Turniersport):**
   * **Quelle:** World DanceSport Federation (WDSF), *Competition Rules – Rule D.2 (Couples)*: Tempi for Standard & Latin dances.
   * **Offizieller Link:** [WDSF Competition Rules](https://www.worlddancesport.org/Rule/Competition/General)
   * **Regelwerk PDF:** [WDSF Competition Rules PDF (Rule D.2)](https://cdnb.worlddancesport.org/legacy-docs/competition/rules%20and%20bidding/2013/Competition%20Rules.pdf)

2. **DTV Turnier- und Sportordnung (TSO):**
   * **Quelle:** Deutscher Tanzsportverband e.V. (DTV), TSO Abschnitt F (Tänze und Tempi).
   * **Offizieller Link:** [DTV Ordnungen & Regelwerke](https://www.tanzsport.de/de/sportwelt/leistungssport/ordnungen)

3. **ADTV & Welttanzprogramm (Hobby- & Tanzschulbereich):**
   * **Quelle:** Allgemeiner Deutscher Tanzlehrerverband (ADTV), Richtlinien zum *Welttanzprogramm (WTP)* für Gesellschaftstanz.
   * **Offizieller Link:** [ADTV Welttanzprogramm (WTP)](https://www.tanzen.de/)
   * **Tanzschul-Praxisübersicht:** [Tanzschule Waal – Tempo der Tänze im Welttanzprogramm (BPM & TPM)](https://www.tanzen-waal.de/post/tempo-der-t%C3%A4nze-im-welttanzprogramm)
   * **ADTV Tanzlehrerausbildung Musiklehre:** Prüfungskatalog Tanzrhythmen, Takt- und Tempobereiche für Tanzschulen.

---

## 🚀 Kernfunktionen der App

### 1. Tapper & Rhythmusanalyse
- **Umschalter Hobby / Turnier:** Wechselt mit einem Klick zwischen tanzschulgerechten Pop-Tempi und WDSF-Wettkampfstandards.
- **Split-Tap Button (1. Schlag Downbeat-Feld):**
  - Normaler Modus: Großer Tap-Bereich ganz unten.
  - Mit 1.-Schlag-Toggle: Linkes orangefarbenes Feld für Schlag 1 (Downbeat) und rechtes blaues Feld für die Folgeschläge (2, 3, 4...).
  - Erkennt automatisch das Metrum (z. B. `3/4` Walzer vs. `4/4` Quickstep/Cha-Cha-Cha).
- **Visuelle Skala & Ampelfarben:**
  - 🟢 **Grün:** Tanzschul-Ideal bzw. Turniertempo
  - 🔵 **Blau:** Gemütlich / Einsteiger
  - 🟡 **Gelb:** Spritzig / Sportlich
- **„Nicht passende Tänze“ mit Begründung:** Zeigt bei jedem Tempo an, warum andere Tänze nicht passen (falscher Takt, zu langsam mit konkreter BPM-Differenz $\Delta$, zu schnell).

### 2. Annotierte Song-Bibliothek & Spotify-Integration
- **Speichern aus dem Tapper:** Lesezeichen-Icon auf jeder Tanzkarte öffnet ein schnelles BottomSheet.
- **Automatische Songerkennung via MediaSession:** Erkennt bei erteiltem Benachrichtigungszugriff automatisch den laufenden Song aus Spotify / YouTube Music inklusive Track-Link.
- **Multi-Tanz-Annotation:** Ein Song kann mehreren Tänzen gleichzeitig zugewiesen werden (z. B. Cha-Cha-Cha + Discofox).
- **Sterne-Bewertung (1–5 Sterne) & Notizen.**
- **Direktes Abspielen & Spotify-Suche:**
  - Hat der Song einen Streaming-Link, öffnet `▶ Abspielen` direkt den Titel in Spotify.
  - Fehlt ein Link, generiert die App mit `🔍 In Spotify suchen` automatisch einen Deep-Link zur Suche.
- **Farbcodierte BPM in der Bibliothek:** Die BPM-Anzeige jedes Songs ist passend zum jeweiligen Tanzstatus grün, blau oder gelb hinterlegt.

---

## 📄 Lizenz

Dieses Projekt ist unter der **GNU General Public License v3.0 (GPLv3)** lizenziert — siehe [LICENSE](LICENSE).

---

## 🛠️ Entwicklung & Build mit Nix

Das Repository enthält eine vollständig isolierte Nix-Shell (`shell.nix`) mit OpenJDK 17, Gradle und Android SDK Platform-Tools 35.

### 1. Nix-Shell betreten
```bash
cd /home/walter/coding_projects/DanceTempoApp
nix-shell
```

### 2. Unit-Tests ausführen
```bash
./gradlew test
```

### 3. Debug-APK bauen
```bash
./gradlew assembleDebug
```
Das fertige APK liegt unter:
`app/build/outputs/apk/debug/app-debug.apk`

### 4. Auf verbundenem Android-Smartphone (z. B. Pixel 8) installieren
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
