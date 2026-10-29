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
- [x] **#7 F-Droid Readyness:**
  - Auditierung aller Abhängigkeiten (100 % freie Software ohne Google Play Services / Tracker).
  - Fastlane-Metadatenstruktur (`fastlane/metadata/android/de-DE` und `en-US`) inklusive Beschreibungen und Changelogs.
- [x] **#8 CI/CD Feinschliff:**
  - Automatisierte PR-Validierung (`test.yml`).
  - Duale Release-Pipeline: Tag-basierte stabile Releases (`v1.0.0`) und automatisches Rolling Pre-Release (`latest`).

---

## 📌 Phase 4: Fortgeschrittene Tanz-Features & Automation
- [x] **#9 Rhythmus-Mustererkennung (Tap-Pattern-Matching):**
  - Erkennung von Tanzrhythmen ohne Mikrofon über getappte Zeitverhältnisse (z. B. *Slow-Slow-Quick-Quick* für Foxtrott, *Slow-Quick-Quick* für Slowfox, *1-a-2* für Samba Bounce, *2-3-4-&-1* für Cha-Cha-Cha).
  - Algorithmus: Normalisiertes MSE-Intervallverhältnis mit dynamischer Skalierung.
- [x] **#4 Musik-Mixer & Playlist-Generator:**
  - Automatische Generierung von Tanzabend-Playlists nach Kriterien (Dauer z. B. 15/30/60 Min, Tanzstile, Schutz vor zwei schnellen Tänzen hintereinander).
  - Direktes Abspielen der Mix-Songs in der konfigurierten Standard-Streaming-App.
