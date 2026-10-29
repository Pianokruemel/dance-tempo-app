package com.jonwal.dancetempo.model

object DanceRepository {

    /**
     * Welttanzprogramm (ADTV / WTP), Social Dancing & Tanzschule.
     * Ausgelegt auf musikalisches Tanzen zu moderner Pop-, Rock- und Swingmusik.
     */
    val allDances: List<Dance> = listOf(
        Dance(
            id = "quickstep",
            name = "Quickstep / schneller Foxtrott",
            category = DanceCategory.STANDARD,
            meter = 4,
            tpmIdealMin = 29,
            tpmIdealMax = 32,
            bpmScaleMin = 98,
            bpmScaleMax = 142,
            idealBpmMin = 116,
            idealBpmMax = 126,
            canHalfTap = true,
            rhythmHint = "Spritziger Pop- oder Swing-Groove. Lebhaft und schnell mit raumgreifenden Chassés und Locksteps."
        ),
        Dance(
            id = "foxtrott",
            name = "Foxtrott (Social)",
            category = DanceCategory.SOCIAL,
            meter = 4,
            tpmIdealMin = 28,
            tpmIdealMax = 32,
            bpmScaleMin = 104,
            bpmScaleMax = 138,
            idealBpmMin = 112,
            idealBpmMax = 128,
            rhythmHint = "Entspannter 4/4-Beat (Pop, Jazz, Schlager). Universeller Klassiker mit unkompliziertem Slow-Slow-Quick-Quick-Gehrhythmus."
        ),
        Dance(
            id = "discofox",
            name = "Discofox",
            category = DanceCategory.SOCIAL,
            meter = 4,
            tpmIdealMin = 30,
            tpmIdealMax = 33,
            bpmScaleMin = 112,
            bpmScaleMax = 142,
            idealBpmMin = 120,
            idealBpmMax = 132,
            rhythmHint = "Gleichmäßiger 'Four-on-the-Floor'-Partybeat (Pop, Schlager, Dance). Flotte Dreh- und Wickelfiguren im 1-2-Tap-Schrittmuster."
        ),
        Dance(
            id = "cha_cha",
            name = "Cha-Cha-Cha",
            category = DanceCategory.LATIN,
            meter = 4,
            tpmIdealMin = 29,
            tpmIdealMax = 32,
            bpmScaleMin = 112,
            bpmScaleMax = 132,
            idealBpmMin = 118,
            idealBpmMax = 126,
            rhythmHint = "Lebhafter Latin-Pop mit spürbarem Beat auf 4-und-1. Freche Hüftaktion mit schnellem Chassé (Cha-Cha-Cha)."
        ),
        Dance(
            id = "tango",
            name = "Tango (Ballroom)",
            category = DanceCategory.STANDARD,
            meter = 2,
            altMeter = 4,
            tpmIdealMin = 30,
            tpmIdealMax = 32,
            bpmScaleMin = 116,
            bpmScaleMax = 134,
            idealBpmMin = 120,
            idealBpmMax = 128,
            halfBpmMin = 60,
            halfBpmMax = 64,
            rhythmHint = "Dramatischer Staccato-Marsch (Habanera-Akzent). Schleichende Schritte mit scharfen Kopfwendungen und klaren Pausen; keine Hüftbewegung."
        ),
        Dance(
            id = "rumba",
            name = "Rumba",
            category = DanceCategory.LATIN,
            meter = 4,
            tpmIdealMin = 24,
            tpmIdealMax = 27,
            bpmScaleMin = 92,
            bpmScaleMax = 112,
            idealBpmMin = 98,
            idealBpmMax = 106,
            rhythmHint = "Ruhiger, romantischer Latin-Klang. Gefühlvolle Hüftbewegung mit Schrittpause auf Schlag 1 und Schritten auf 2, 3, 4-1."
        ),
        Dance(
            id = "jive",
            name = "Jive",
            category = DanceCategory.LATIN,
            meter = 4,
            tpmIdealMin = 35,
            tpmIdealMax = 41,
            bpmScaleMin = 128,
            bpmScaleMax = 176,
            idealBpmMin = 140,
            idealBpmMax = 164,
            rhythmHint = "Energievoller Rock'n'Roll oder Upbeat-Pop (Betonung auf 2 & 4). Spritzig mit federnder Knieaktion, Chassés und Kicks."
        ),
        Dance(
            id = "slow_waltz",
            name = "Langsamer Walzer",
            category = DanceCategory.STANDARD,
            meter = 3,
            tpmIdealMin = 28,
            tpmIdealMax = 30,
            bpmScaleMin = 78,
            bpmScaleMax = 94,
            idealBpmMin = 84,
            idealBpmMax = 90,
            rhythmHint = "Schwebender, weicher 3/4-Takt (Akustik, Balladen). Elegante Schwünge mit sanftem Heben und Senken (Rise & Fall)."
        ),
        Dance(
            id = "viennese_waltz",
            name = "Wiener Walzer",
            category = DanceCategory.STANDARD,
            meter = 3,
            tpmIdealMin = 50,
            tpmIdealMax = 58,
            bpmScaleMin = 135,
            bpmScaleMax = 185,
            idealBpmMin = 150,
            idealBpmMax = 170,
            rhythmHint = "Schwungvoller 3/4-Drehtakt mit deutlicher Betonung auf Schlag 1. Fließende Rechts-/Linksdrehungen oder raumgreifendes Pendeln."
        ),
        Dance(
            id = "samba",
            name = "Samba",
            category = DanceCategory.LATIN,
            meter = 2,
            tpmIdealMin = 48,
            tpmIdealMax = 52,
            bpmScaleMin = 90,
            bpmScaleMax = 110,
            idealBpmMin = 96,
            idealBpmMax = 104,
            rhythmHint = "Pulsierende Latin-Percussion mit Akzent auf Schlag 2. Unverkennbar durch die federnde Knieaktion (Samba-Bounce auf 1-a-2)."
        ),
        Dance(
            id = "slow_foxtrot",
            name = "Slowfox",
            category = DanceCategory.STANDARD,
            meter = 4,
            tpmIdealMin = 28,
            tpmIdealMax = 30,
            bpmScaleMin = 106,
            bpmScaleMax = 122,
            idealBpmMin = 112,
            idealBpmMax = 118,
            rhythmHint = "Edler Big-Band- und Swing-Klang. Weite, fließende Gehwellen ohne sichtbare Stopps im Slow-Quick-Quick-Rhythmus."
        ),
        Dance(
            id = "paso_doble",
            name = "Paso Doble",
            category = DanceCategory.LATIN,
            meter = 2,
            tpmIdealMin = 59,
            tpmIdealMax = 62,
            bpmScaleMin = 112,
            bpmScaleMax = 126,
            idealBpmMin = 118,
            idealBpmMax = 124,
            rhythmHint = "Feuriger spanischer Marsch im Stierkampf-Charakter. Aufrechte, stolze Haltung mit kraftvollen Marschschritten."
        ),
        Dance(
            id = "west_coast_swing",
            name = "West Coast Swing",
            category = DanceCategory.SOCIAL,
            meter = 4,
            tpmIdealMin = 22,
            tpmIdealMax = 28,
            bpmScaleMin = 76,
            bpmScaleMax = 124,
            idealBpmMin = 85,
            idealBpmMax = 115,
            rhythmHint = "Elastischer Groove zu R&B, Pop oder Blues. Getanzt entlang einer Linie (Slot) mit Raum für lockere Improvisation."
        )
    )
}
