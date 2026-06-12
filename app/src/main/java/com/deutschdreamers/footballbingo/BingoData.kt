package com.deutschdreamers.footballbingo

import kotlin.random.Random

enum class Language(val displayName: String, val flag: String) {
    ENGLISH("English", "🏴󠁧󠁢󠁥󠁮󠁧󠁿"),
    GERMAN("Deutsch", "🇩🇪"),
    HINDI("हिन्दी", "🇮🇳")
}

data class GameState(
    val language: Language,
    val cardNumber: Int,
    val items: List<String>,
    val markedSquares: Set<Int> = emptySet(),
    val winningLines: List<List<Int>> = emptyList()
) {
    val hasWon: Boolean get() = winningLines.isNotEmpty()

    companion object {
        fun create(language: Language, cardNumber: Int): GameState {
            val pool = BingoData.items[language] ?: emptyList()
            val random = Random(language.ordinal.toLong() * 100_000L + cardNumber)
            val selected = pool.shuffled(random).take(25)
            return GameState(language, cardNumber, selected)
        }
    }

    fun toggleSquare(index: Int): GameState {
        val newMarked = if (index in markedSquares) markedSquares - index else markedSquares + index
        return copy(markedSquares = newMarked, winningLines = computeWinners(newMarked))
    }

    private fun computeWinners(marked: Set<Int>): List<List<Int>> {
        val winners = mutableListOf<List<Int>>()
        for (row in 0..4) {
            val line = List(5) { col -> row * 5 + col }
            if (line.all { it in marked }) winners.add(line)
        }
        for (col in 0..4) {
            val line = List(5) { row -> row * 5 + col }
            if (line.all { it in marked }) winners.add(line)
        }
        val diag1 = listOf(0, 6, 12, 18, 24)
        val diag2 = listOf(4, 8, 12, 16, 20)
        if (diag1.all { it in marked }) winners.add(diag1)
        if (diag2.all { it in marked }) winners.add(diag2)
        return winners
    }
}

object BingoData {
    val items: Map<Language, List<String>> = mapOf(
        Language.ENGLISH to listOf(
            "Wrong throw-in is called",
            "Club mascot appears on screen",
            "Throw-in reaches the penalty area",
            "Free kick hits the wall",
            "Away team's last game is mentioned",
            "Back pass to keeper from opponent's half",
            "Commentator makes a bad pun",
            "Away team takes the lead",
            "Goalkeeper makes a big save",
            "Last time these teams met is discussed",
            "Clear chance is missed",
            "Penalty is awarded",
            "Referee checks VAR",
            "Fans display a banner",
            "Referees consult each other",
            "It's a draw in the second half",
            "Three players from one team are booked",
            "Manager / coach chews gum",
            "Goalkeeper keeps a clean sheet",
            "Academy graduate starts the game",
            "Player's age is mentioned",
            "Goal from a header",
            "Offside claimed",
            "Yellow card for dissent",
            "Corner kick leads to a goal",
            "Substitute scores",
            "Red card shown",
            "Ball hits the post or crossbar",
            "Goal is disallowed",
            "Player goes down holding their face",
            "Commentator mispronounces a name",
            "Long ball played over the top",
            "Player dives in the penalty box",
            "Manager gestures wildly from touchline",
            "Home team equalizes",
            "Kick-off is delayed",
            "Injured player comes back on",
            "Commentator uses a cliché",
            "Handball appeal in the box",
            "Away team wastes time",
            "5+ minutes of injury time announced",
            "Captain argues with referee",
            "Shot hits the side netting",
            "Both keepers are mentioned together"
        ),
        Language.GERMAN to listOf(
            "Falscher Einwurf wird gepfiffen",
            "Ein Vereinsmaskottchen ist zu sehen",
            "Einwurf bis in den Sechzehner",
            "Freistoß in die Mauer",
            "Das letzte Spiel der Gastmannschaft wird erwähnt",
            "Rückpass zum Torwart aus der gegnerischen Hälfte",
            "Kommentator bringt ein schlechtes Wortspiel",
            "Die Gastmannschaft geht in Führung",
            "Jürgen Klopp macht Werbung",
            "Gelb wegen Meckerns",
            "Der Gästetorwart pariert",
            "Das letzte Aufeinandertreffen wird thematisiert",
            "Eine klare Torchance wird vergeben",
            "Elfmeter",
            "Der Schiri schaut sich eine Szene noch mal an",
            "Fans hissen ein Spruchband",
            "Die Schiedsrichter beraten sich",
            "Es steht unentschieden in der zweiten Hälfte",
            "Drei Spieler einer Mannschaft haben Gelb",
            "Ein Trainer kaut Kaugummi",
            "Ein Torhüter bewahrt eine weiße Weste",
            "Ein Spieler aus der eigenen Jugend spielt",
            "Das Alter eines Spielers wird erwähnt",
            "Kopfballtor",
            "Jemand reklamiert Abseits",
            "Einwechslung in der 60. bis 75. Minute",
            "Trainer wird auf die Tribüne geschickt",
            "Rote Karte",
            "Ball trifft Pfosten oder Latte",
            "Tor nach Eckball",
            "Tor wird aberkannt",
            "Spieler täuscht eine Verletzung vor",
            "Eigentor",
            "Kommentator nennt eine Statistik",
            "Verlängerung droht",
            "Handspiel im Strafraum",
            "Anschlusstreffer in der Nachspielzeit",
            "Elfmeter wird gehalten",
            "Flitzer auf dem Spielfeld",
            "Kapitän diskutiert mit dem Schiedsrichter",
            "Kommentator nennt den falschen Namen",
            "Wiederholung einer umstrittenen Szene",
            "Beide Torwarte in einem Atemzug genannt",
            "5+ Minuten Nachspielzeit"
        ),
        Language.HINDI to listOf(
            "गलत थ्रो-इन पर फाउल",
            "क्लब का मैस्कॉट दिखा",
            "थ्रो-इन पेनल्टी एरिया तक पहुंचा",
            "फ्री किक दीवार में लगी",
            "विजिटिंग टीम का पिछला मैच याद किया",
            "गोलकीपर को बैकपास",
            "कमेंटेटर ने बुरा जोक किया",
            "विजिटिंग टीम ने बढ़त ली",
            "गोलकीपर ने शानदार बचाव किया",
            "दोनों टीमों की पिछली मुलाकात का जिक्र",
            "पक्का मौका गंवाया",
            "पेनल्टी मिली",
            "रेफरी ने VAR चेक किया",
            "दर्शकों ने बैनर लहराया",
            "रेफरी आपस में सलाह कर रहे हैं",
            "दूसरे हाफ में बराबरी",
            "एक टीम को तीन येलो कार्ड",
            "कोच गम चबा रहा है",
            "गोलकीपर ने क्लीन शीट बनाई",
            "अकादमी का खिलाड़ी खेला",
            "खिलाड़ी की उम्र बताई गई",
            "हेडर से गोल",
            "ऑफसाइड का दावा",
            "बहस पर येलो कार्ड",
            "कॉर्नर से गोल",
            "सब्स्टीट्यूट ने गोल किया",
            "रेड कार्ड दिखाया",
            "गेंद पोस्ट या क्रॉसबार से लगी",
            "गोल रद्द हुआ",
            "खिलाड़ी चेहरा पकड़कर गिरा",
            "कमेंटेटर ने नाम गलत बोला",
            "लंबी बॉल का प्रयास",
            "पेनल्टी बॉक्स में खिलाड़ी गिरा",
            "कोच ने टचलाइन पर हावभाव दिखाए",
            "घरेलू टीम ने बराबरी की",
            "किक-ऑफ में देरी",
            "इंजरी टाइम में गोल",
            "हैंडबॉल का दावा",
            "विजिटिंग टीम ने समय बर्बाद किया",
            "कैप्टन ने रेफरी से बहस की",
            "कमेंटेटर ने क्लिशे बोला",
            "5+ मिनट का इंजरी टाइम",
            "विवादित दृश्य का रिप्ले",
            "साइड नेट में शॉट"
        )
    )
}
