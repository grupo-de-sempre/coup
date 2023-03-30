package org.coup.game.actions

class Player(private val name: String) {
    var coins: Int = 0
        private set

    fun incrementCoins(amount: Int) {
        coins += amount
    }

    fun decrementCoins(amount: Int) {
        coins -= amount
    }
}