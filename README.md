# 💃 DanceTempo — Android App

Eine moderne, native Android-App zur präzisen Rhythmus- und Tempoanalyse sowie zur annotierten Song-Bibliothek für **Standard-**, **Latein-** und **Social-Tänze**.

> ### 🤖 KI-Entwicklungs-Hinweis (AI Disclaimer)
> Dieses gesamte Projekt (Architektur, UI/UX-Design in Jetpack Compose, Signal- und Rhythmusanalyse-Algorithmen, Datenbankdesign in Room, Nix-Entwicklungsumgebung sowie CI/CD-Pipelines) wurde **vollständig unter Einsatz moderner KI-Coding-Tools (LLM Agentic Coding im interaktiven Pair-Programming)** konzipiert, implementiert, getestet und gewartet.

---

## 🎯 Zwei Zielgruppen: Hobby vs. Turnier

Die App schlägt eine Brücke zwischen Gesellschaftstanz zu modernen Pop-Songs und dem leistungsorientierten Turniersport:

1. **Hobby / Tanzschule (Standardmodus):**
   * Orientiert am **Welttanzprogramm (WTP / ADTV)** und Social Dancing.
   * Erkennt und toleriert tanzschulgerechte Tempi moderner Pop-, Rock- und Swingmusik (z. B. entspannter Quickstep/Foxtrott bei 110–135 BPM, gemütlicher Wiener Walzer ab 150 BPM, Jive ab 135 BPM).
2. **Turnier (WDSF / DTV):**
   * Strikte Anwendung der Wettkampf-Vorgaben der **World DanceSport Federation (WDSF Rule D.2)**.
   * Wettkampf-Korridore (z. B. Quickstep strikt 200–208 BPM bzw. Halbtakt 100–104 BPM).

Ein Klick auf den Schalter `[Hobby]` / `[Turnier]` in der Kopfzeile schaltet die gesamte Auswertung und Benennung nahtlos um.

---

## 🎵 Referenztabelle der Tempi

| Tanz | Taktart | Hobby-Ideal (BPM) | Hobby-Toleranz (BPM) | WDSF Turnier (TPM) | WDSF Turnier (BPM) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Quickstep** | 4/4 | **116–126** | 98–142 *(Pop / Swing)* | 50–52 | **200–208** *(Halbtakt 100–104)* |
| **Foxtrott (Social)** | 4/4 | **112–128** | 104–138 *(WTP Allrounder)* | – *(nur Slowfox/Quickstep)* | – |
| **Langsamer Walzer** | 3/4 | **84–90** | 78–94 | 28–30 | **84–90** |
| **Wiener Walzer** | 3/4 | **150–170** | 135–185 *(Social Einstieg)* | 58–60 | **174–180** |
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

## 📚 Quellenverzeichnis

Die hinterlegten Normen und Toleranzbereiche basieren auf den Regelwerken der führenden Tanzsportverbände:

1. **WDSF Competition Rules (Turniersport):**
   * World DanceSport Federation (WDSF), *Competition Rules – Rule D.2 (Couples)*: [WDSF Rules Portal](https://www.worlddancesport.org/Rule/Competition/General) · [Offizielles PDF (S. 31)](https://cdnb.worlddancesport.org/legacy-docs/competition/rules%20and%20bidding/2013/Competition%20Rules.pdf)
2. **Deutscher Tanzsportverband (DTV):**
   * Turnier- und Sportordnung (TSO Abschnitt F): [DTV Ordnungen & Regelwerke](https://www.tanzsport.de/de/sportwelt/leistungssport/ordnungen)
3. **Allgemeiner Deutscher Tanzlehrerverband (ADTV) & Welttanzprogramm (WTP):**
   * Welttanzprogramm Richtlinien für Gesellschaftstanz: [tanzen.de (ADTV)](https://www.tanzen.de/)
   * Tanzschul-Übersicht: [Tanzschule Waal – Tempo der Tänze im Welttanzprogramm](https://www.tanzen-waal.de/post/tempo-der-t%C3%A4nze-im-welttanzprogramm)

---

## 🚀 Kernfunktionen

### 1. Tapper & Rhythmusanalyse
* **Ergonomisches Daumen-Layout:** Große Tap-Pads befinden sich am unteren Bildschirmrand.
* **1.-Schlag-Downbeat-Erkennung:**
  * Bei zugeschaltetem Toggle teilt sich der Tap-Bereich: Links das orangefarbene Feld für Schlag 1 (Downbeat), rechts das blaue Feld für die Folgeschläge (2, 3, 4...).
  * Erkennt automatisch das Metrum (z. B. `3/4` Walzer vs. `4/4` Quickstep/Cha-Cha-Cha).
* **Visuelle Tempo-Ampel:**
  * 🟢 **Grün:** Tanzschul-Ideal bzw. WDSF-Turniertempo
  * 🔵 **Blau:** Gemütlich / Einsteiger
  * 🟡 **Gelb:** Spritzig / Sportlich
* **Ausschluss-Begründungen:** Zeigt bei nicht passenden Tänzen konkret an, warum sie nicht zutreffen (falsche Taktart, zu langsam inkl. BPM-Differenz $\Delta$, zu schnell).

### 2. Song-Bibliothek & Spotify-Integration
* **Schnelles Speichern:** Lesezeichen-Icon auf jeder passenden Tanzkarte öffnet direkt das Vorbefüllungs-Sheet.
* **Automatische Erkennung via MediaSession:** Liest Titel, Interpret und Track-URI laufender Spotify- oder YouTube-Music-Wiedergaben automatisch aus.
* **Multi-Tanz-Annotation:** Ein Song kann mehreren Tänzen gleichzeitig zugewiesen werden.
* **Streaming & Suche:**
  * Songs mit hinterlegter URL starten mit einem Klick direkt in Spotify (`▶ Abspielen`).
  * Songs ohne URL erhalten einen automatischen Suchlink (`🔍 In Spotify suchen`).
* **Farbcodierte BPM-Badges:** Die BPM-Anzeige in der Bibliotheksliste übernimmt die Farbcodierung passend zum gefilterten Tanz.

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
