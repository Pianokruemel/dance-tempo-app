# 🗺️ DanceTempo — Produkt-Roadmap & Architekturplanung

Diese Roadmap dokumentiert die strategischen Entscheidungen und Meilensteine für die Weiterentwicklung von **DanceTempo**.

---

## 📌 Phase 1: Fundament & UX-Schärfung (Aktuell)
- [x] **#5 Turniertanz entfernen:** Konsequente Fokussierung auf Hobbytanz, Tanzschule (ADTV / Welttanzprogramm) und Social Dancing. Entfernen des Turnier-Toggles zur Entlastung des Interfaces.
- [x] **#10 Tango-Fokussierung:** Festhalten am europäischen / internationalen Ballroom-Tango; Dokumentation im README bezüglich Abgrenzung zu Tango Argentino.
- [x] **#11 Universelles Speichern:** Lesezeichen-Icon auch bei nicht-passenden Tänzen; Möglichkeit, im Speichern-Dialog jeden beliebigen Tanz manuell zuzuweisen.
- [x] **#3 Intelligente Duplikaterkennung:** Automatische Prüfung vor dem Speichern anhand von Titel und Interpret. Bestehende Einträge werden um neue Tänze ergänzt statt doppelt angelegt.
- [x] **#6 Minimalistisches App-Icon:** Entwurf und Bereitstellung eleganter Vektor-Icons (Android Adaptive Icon) zur Auswahl.

---

## 📌 Phase 2: Daten & Konnektivität
- [x] **#1 Universelle Musik-App-Unterstützung & Einstellungsseite:**
  - Einstellungsmenü mit Auswahl des bevorzugten Standard-Players (Spotify, YouTube Music, Deezer, Apple Music, System-Standard).
  - Direkter Start via Android Search Intent oder App-spezifischem Deep-Link beim Klick auf *„Abspielen“*.
- [x] **#2 Import & Export der Bibliothek:**
  - JSON-Backup & Restore über das native Android Storage Access Framework.
  - Optionaler CSV-Export für Excel/Tabellen.

---

## 📌 Phase 3: Veröffentlichung & Distribution
- [ ] **#7 F-Droid Readyness:**
  - Auditierung aller Abhängigkeiten (100 % freie Software ohne Google Play Services / Tracker).
  - Erstellung der Metadaten und Vorbereitung des Merge Requests für `fdroiddata`.
- [ ] **#8 CI/CD Feinschliff:**
  - Automatisierte PR-Tests (`test.yml`).
  - Semantische Releases bei Git-Tags (`v1.0.0`).

---

## 📌 Phase 4: Fortgeschrittene Tanz-Features & Automation
- [ ] **#9 Rhythmus-Mustererkennung (Tap-Pattern-Matching):**
  - Erkennung von Tanzrhythmen ohne Mikrofon über getappte Zeitverhältnisse (z. B. *Slow-Slow-Quick-Quick* für Foxtrott, *Slow-Quick-Quick* für Slowfox, *1-a-2* für Samba Bounce).
  - Algorithmus: Normalisiertes Intervallverhältnis / Dynamic Time Warping (DTW).
- [ ] **#4 Musik-Mixer & Playlist-Generator:**
  - Automatische Generierung von Tanzabend-Playlists nach Kriterien (Dauer, Tanzstile, abwechselnde Tempi).
  - Übergabe / Erstellung der Playlist in der konfigurierten Musik-App.
