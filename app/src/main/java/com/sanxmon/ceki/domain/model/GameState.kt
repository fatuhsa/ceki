package com.sanxmon.ceki.domain.model

data class GameState(
    val players: List<Player>,
    val history: List<HistoryLog>,
    val viewMode: ViewMode,
)

/** Default roster: four players named "Player 1..4" with score 0. */
fun defaultPlayers(): List<Player> = List(4) { index ->
    Player(name = "Player ${index + 1}", score = 0)
}
