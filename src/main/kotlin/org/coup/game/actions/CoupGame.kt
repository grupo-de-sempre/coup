package org.coup.game.actions

/**
 * This class represents a game of Coup.
 *
 * It is responsible for managing the game state and the players.
 *
 * @param players The players in the game
 * @property bank The number of coins in the bank
 * @property deck The deck of cards
 * @constructor Creates a new game of Coup, giving each [Player] 2 coins and 2 influences.
 */
class CoupGame(
    private var players: MutableList<Player> = mutableListOf()
) {
    private var bank = 50
    private var deck: MutableList<Influence> = mutableListOf()

    init {
        deck = createDeck()
        players.forEach { player ->
            incrementCoins(player, 2)
            repeat(2) { dealCard(player) }
        }
    }

    private fun createDeck(): MutableList<Influence> {
        val deck = mutableListOf<Influence>()
        repeat(3) {
            deck.add(Duke)
            deck.add(Assassin)
            deck.add(Captain)
            deck.add(Ambassador)
            deck.add(Contessa)
        }
        return deck
    }

    fun dealCard(player: Player) {
        deck.shuffled().first().let { influence ->
            player.influences += influence
            deck.remove(influence)
        }
    }

    /**
     * Gives a [player] coins from the [bank]. Given the [amount] of coins.
     *
     * It will give the player the maximum amount of coins possible. If the [bank]
     * has fewer coins than the [amount], it will give the [player] all the coins
     * in the [bank].
     *
     * @param player The player to give coins to
     * @param amount The amount of coins to give
     */
    fun incrementCoins(player: Player, amount: Int) {
        val coins = minOf(amount, bank)
        player.incrementCoins(coins)
        bank -= coins
    }

    /**
     * Takes a [player] coins and put it in the [bank]. Given the [amount] of coins.
     *
     * It will take the player the maximum amount of coins possible. If the [player]
     * has fewer coins than the [amount], it will take all the coins from the [player].
     *
     * @param player The player to take coins from
     * @param amount The amount of coins to take
     */
    fun decrementCoins(player: Player, amount: Int) {
        val coins = minOf(amount, player.coins)
        player.decrementCoins(coins)
        bank += coins
    }

    /**
     * Removes an influence from a [player].
     *
     * If the [player] has no more influences, it will be eliminated from the game.
     *
     * @param player The player to remove an influence from
     * @see checkPlayerInfluence
     */
    fun removeInfluence(player: Player) {
        println("${player.name} lost an influence")
        player.influences -= player.influences.random()
        checkPlayerInfluence(player)
    }

    /**
     * Checks if a [player] has no more influences. If so, they are eliminated from the game.
     *
     * @param player The player to check
     */
    private fun checkPlayerInfluence(player: Player) {
        if (player.influences == emptySet<Influence>()) {
            println("${player.name} lost the game")
            players.remove(player)
        }
    }
}
