package org.coup.game.actions

/**
 * This class represents a player in the game.
 *
 * @param name The name of the player.
 * @property coins The number of coins the player has.
 * @property influences The influence the player has.
 * @constructor Creates a new player.
 */
data class Player(val name: String) {
    var coins: Int = 0
        private set
    var influences: MutableSet<Influence> = mutableSetOf()

    /**
     * Increments the number of coins the player has.
     *
     * @param amount The amount of coins to increment.
     */
    fun incrementCoins(amount: Int) {
        coins += amount
    }

    /**
     * Decrements the number of coins the player has.
     *
     * @param amount The amount of coins to decrement.
     */
    fun decrementCoins(amount: Int) {
        coins -= amount
    }
}