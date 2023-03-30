package org.coup.game.actions

class CoupGame(
    private var players: MutableList<Player> = mutableListOf()
) {
    private var bankCoins = 50

    init {
        players.forEach { it.incrementCoins(2) }
    }

    fun incrementCoins(player: Player, amount: Int) {
        val coins = minOf(amount, bankCoins)
        player.incrementCoins(coins)
        bankCoins -= coins
    }

    fun decrementCoins(player: Player, amount: Int) {
        val coins = minOf(amount, player.coins)
        player.decrementCoins(coins)
        bankCoins += coins
    }
}