package org.coup.game.actions

import kotlin.reflect.KClass

/**
 * Represents a top-level action.
 *
 * This interface is used to represent all actions that can be executed in the game. And
 * is only a marker interface.
 */
interface Action

/**
 * Represents a player action. All actions are dispatcher by a [Player].
 *
 * @property dispatcher The [Player] that dispatches the action.
 */
sealed interface PlayerAction : Action {
    val dispatcher: Player
}

/**
 * The [GeneralAction]s are actions that can be executed by any player, and are not
 * character specific.
 *
 * All [GeneralAction]s are [PlayerAction]s that can be [Executable].
 */
interface GeneralAction : PlayerAction, Executable

/**
 * The [CharacterAction]s are actions that can be executed by any player, and are character
 * specific.
 *
 * All [CharacterAction]s are [PlayerAction]s that can be [Executable] and [Challengeable].
 */
interface CharacterAction : PlayerAction, Executable, Challengeable

/**
 * The [CounterAction]s are actions that can be used to counter [Counterable] actions.
 *
 * They only can be used by a [Targetable.target] of it's [Counterable] action.
 */
interface CounterAction : PlayerAction

/**
 * Represents an [Action] that can be executed.
 */
interface Executable : Action {
    /**
     * Executes the action.
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    fun execute(game: CoupGame)
}

/**
 * Represents an [Action] that can be targeted.
 *
 * @property target The [Player] that is targeted by the action.
 */
interface Targetable : Action {
    val target: Player
}

/**
 * Represents an [Action] that needs to be paid for.
 *
 * @property requiredCoin The amount of coins that is required to execute the action.
 */
interface Payable : Action {
    val requiredCoin: Int
}

/**
 * Represents an [Action] that can be countered.
 *
 * @property counteredBy The list of [CounterAction]s that can counter the action.
 */
interface Counterable : Action {
    val counteredBy: List<KClass<out CounterAction>>
}

/**
 * Represents an [Action] that can be challenged.
 */
interface Challengeable : Action

/**
 * The [Income] is a [GeneralAction] that gives the [dispatcher] 1 coin.
 *
 * It cannot be [Counterable] or [Challengeable].
 *
 * @property dispatcher The [Player] that dispatches the action.
 */
data class Income(override val dispatcher: Player) : GeneralAction {
    /**
     * Executes the action by incrementing the coins of the [dispatcher] by 1.
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    override fun execute(game: CoupGame) = game.incrementCoins(dispatcher, 1)
}

/**
 * The [ForeignAid] is a [GeneralAction] that gives the [dispatcher] 2 coins.
 *
 * It is [Counterable] by the [CounterForeignAid] action, but cannot be [Challengeable].
 *
 * @property dispatcher The [Player] that dispatches the action.
 * @property counteredBy The list with the [CounterForeignAid] class, as it can be countered by that action.
 */
data class ForeignAid(override val dispatcher: Player) : GeneralAction, Counterable {
    /**
     * Executes the action by incrementing the coins of the [dispatcher] by 2.
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    override fun execute(game: CoupGame) = game.incrementCoins(dispatcher, 2)
    override val counteredBy = listOf(CounterForeignAid::class)
}

/**
 * The [Coup] is a [GeneralAction] that removes an influence from a [target].
 *
 * It cannot be [Counterable] or [Challengeable]. And always needs to be [Targetable].
 * This action is also [Payable], and requires 7 coins to be executed by the [dispatcher].
 *
 * @property dispatcher The [Player] that dispatches the action.
 * @property target The [Player] that is targeted by the action.
 * @property requiredCoin The amount of coins that is required to execute the action.
 */
data class Coup(override val dispatcher: Player, override val target: Player) : GeneralAction, Payable, Targetable {
    /**
     * Executes the action by decrementing the coins of the [dispatcher] by 7,
     * and removing an influence from the [target].
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    override fun execute(game: CoupGame) {
        game.decrementCoins(dispatcher, requiredCoin)
        game.removeInfluence(target)
    }

    override val requiredCoin = 7
}

/**
 * The [Tax] is a [CharacterAction] that gives the [dispatcher] 3 coins.
 *
 * It cannot be [Counterable], but is [Challengeable].
 *
 * @property dispatcher The [Player] that dispatches the action.
 */
data class Tax(override val dispatcher: Player) : CharacterAction {
    /**
     * Executes the action by incrementing the coins of the [dispatcher] by 3.
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    override fun execute(game: CoupGame) = game.incrementCoins(dispatcher, 3)
}

/**
 * The [Assassinate] is a [CharacterAction] that removes an influence from a [target].
 *
 * It can be [Counterable] by the [CounterAssassinate] action, and is [Challengeable].
 * This action is also [Payable], and requires 3 coins to be executed. And always needs to be [Targetable].
 *
 * @property dispatcher The [Player] that dispatches the action.
 * @property target The [Player] that is targeted by the action.
 * @property requiredCoin The amount of coins that is required to execute the action.
 * @property counteredBy The list with the [CounterAssassinate] class, as it can be countered by that action.
 */
data class Assassinate(override val dispatcher: Player, override val target: Player) : CharacterAction, Payable, Targetable, Counterable {
    /**
     * Executes the action by decrementing the coins of the [dispatcher] by 3,
     * and removing an influence from the [target].
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    override fun execute(game: CoupGame) {
        game.decrementCoins(dispatcher, requiredCoin)
        game.removeInfluence(target)
    }

    override val requiredCoin = 3
    override val counteredBy = listOf(CounterAssassinate::class)
}

/**
 * The [Steal] is a [CharacterAction] that takes at most 2 coins from a [target].
 *
 * It can be [Counterable] by the [CounterSteal] action, and is [Challengeable].
 * This action always needs to be [Targetable].
 *
 * @property dispatcher The [Player] that dispatches the action.
 * @property target The [Player] that is targeted by the action.
 * @property counteredBy The list with the [CounterSteal] class, as it can be countered by that action.
 */
data class Steal(override val dispatcher: Player, override val target: Player) : CharacterAction, Targetable, Counterable {
    /**
     * Executes the action by decrementing the coins of the [target] by 2,
     * and incrementing the coins of the [dispatcher] by 2.
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    override fun execute(game: CoupGame) {
        game.decrementCoins(target, 2)
        game.incrementCoins(dispatcher, 2)
    }

    override val counteredBy = listOf(CounterSteal::class)
}

/**
 * The [Exchange] is a [CharacterAction] that exchanges the [dispatcher]'s cards with the [CoupGame.deck].
 *
 * It cannot be [Counterable], but is [Challengeable].
 *
 * @property dispatcher The [Player] that dispatches the action.
 */
data class Exchange(override val dispatcher: Player) : CharacterAction {
    /**
     * Executes the action by exchanging the [dispatcher]'s cards with the deck.
     *
     * @param game The [CoupGame] that the action is executed in.
     */
    override fun execute(game: CoupGame) {
        TODO("Need to implement the exchange action.")
    }
}

/**
 * The [CounterForeignAid] is a [CounterAction] that counters the [ForeignAid] action.
 *
 * It is not [Challengeable], as [ForeignAid] is not a character specific action.
 *
 * @property dispatcher The [Player] that are countering the action. They can be any player.
 */
data class CounterForeignAid(override val dispatcher: Player) : CounterAction

/**
 * The [CounterAssassinate] is a [CounterAction] that counters the [Assassinate] action.
 *
 * It can be [Challengeable] by any player.
 *
 * Only the [Assassinate.target] of the [Assassinate] action can be the [dispatcher] of this action.
 *
 * @property dispatcher The [Player] that are countering the action and is the target of the [Assassinate] action.
 */
data class CounterAssassinate(override val dispatcher: Player) : CounterAction, Challengeable

/**
 * The [CounterSteal] is a [CounterAction] that counters the [Steal] action.
 *
 * It can be [Challengeable] by any player.
 *
 * Only the [Steal.target] of the [Steal] action can be the [dispatcher] of this action.
 *
 * @property dispatcher The [Player] that are countering the action and is the target of the [Steal] action.
 */
data class CounterSteal(override val dispatcher: Player) : CounterAction, Challengeable

/**
 * The [Challenge] is a [PlayerAction] that can be executed by any player to challenge
 * a [CharacterAction] or some [CounterAction]s.
 *
 * This is [Targetable], as it needs be executed on a [Player] that is dispatching
 * the action that is being challenged.
 *
 * @property dispatcher The [Player] that are challenging the action.
 * @property target The [Player] that is the target of the action.
 */
data class Challenge(override val dispatcher: Player, override val target: Player) : PlayerAction, Targetable {
    /**
     * Executes the action by resolving the challenge.
     * If the challenge is successful, the [target] will lose an influence, otherwise
     * the [dispatcher] will lose an influence.
     *
     * @param succeeded Whether the challenge was successful or not.
     * @param game The [CoupGame] that the action is executed in.
     */
    fun resolve(succeeded: Boolean, game: CoupGame) {
        if (succeeded) {
            game.removeInfluence(target)
        } else {
            game.removeInfluence(dispatcher)
        }
    }
}