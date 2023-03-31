package org.coup.game.actions

import java.util.*

/**
 * Mediator for resolving [PlayerAction]s.
 * The resolution of actions is done in a stack-like manner, where
 * the last action added is the first to be resolved. This is done to allow for actions to be
 * resolved in the order they were added, and to allow for actions to be resolved in a nested
 * manner, such as when a challenge is made against a block action.
 *
 * For example, if a player blocks an action, and another player challenges the block, the
 * challenge will be resolved first, and then the block will be resolved, for the action be executed or not.
 *
 * The consumer of this class should add actions to the stack using the [addAction] method, and
 * then resolve the actions using the [resolveAsSuccess] or [resolveAsFailure] methods.
 *
 * @property actionStack Stack of actions to resolve
 * @constructor Creates a new ActionMediator
 */
internal class ActionMediator {
    private val actionStack: Stack<PlayerAction> = Stack()

    /**
     * Adds a [PlayerAction] to the stack.
     *
     * @param action Action to add to the stack
     * @return [true] if the action was added to the stack, [false] otherwise
     */
    fun addAction(action: PlayerAction) = actionStack.add(action)

    /**
     * Resolves the [PlayerAction]s at the top of the stack as a success.
     *
     * This will resolve in recursive manner, resolving any challenges or counter actions that
     * are on the stack.
     *
     * @param game Game to resolve the action against
     */
    fun resolveAsSuccess(game: CoupGame) = resolveAction(actionStack.pop(), game, true)

    /**
     * Resolves the [PlayerAction]s at the top of the stack as a failure.
     *
     * This will resolve in recursive manner, resolving any challenges or counter actions that
     * are on the stack.
     *
     * @param game Game to resolve the action against
     */
    fun resolveAsFailure(game: CoupGame) = resolveAction(actionStack.pop(), game, false)

    /**
     * Resolves a [PlayerAction].
     *
     * Resolving an action will execute the action if it is [Executable], and will print a message
     * to the console indicating the action was resolved. If the action is a [Challenge] or [CounterAction],
     * the action will be resolved recursively.
     *
     * @param action Action to resolve
     * @param game Game to resolve the action against
     * @param isSuccess Whether the action was successful or not
     */
    private fun resolveAction(action: PlayerAction, game: CoupGame, isSuccess: Boolean) {
        when (action) {
            is Challenge -> resolveChallenge(action, actionStack.pop(), game, isSuccess)
            is CounterAction -> resolveCounterAction(action, actionStack.pop(), game, isSuccess)
            else -> {
                if (action is Executable && isSuccess) action.execute(game)
                val resultMessage = if (isSuccess) "successfully" else "failed to"
                println("${action.dispatcher.name} $resultMessage ${action::class.simpleName}${if (action is Targetable) " ${action.target.name}" else ""}")
            }
        }
    }

    /**
     * Resolves a challenge.
     *
     * @param challenge Challenge to resolve
     * @param previousAction Action that was challenged
     * @param game Game to resolve the action against
     * @param isSuccess Whether the challenge was successful or not
     */
    private fun resolveChallenge(challenge: Challenge, previousAction: PlayerAction, game: CoupGame, isSuccess: Boolean) {
        challenge.resolve(isSuccess, game)
        val resultMessage = if (isSuccess) "succeeded in" else "failed to"
        println("${challenge.dispatcher.name} $resultMessage challenge ${challenge.target.name}")
        resolveAction(previousAction, game, !isSuccess)
    }

    /**
     * Resolves a counter action.
     *
     * @param counter Counter action to resolve
     * @param previousAction Action that was countered
     * @param game Game to resolve the action against
     * @param isSuccess Whether the counter action was successful or not
     */
    private fun resolveCounterAction(counter: CounterAction, previousAction: PlayerAction, game: CoupGame, isSuccess: Boolean) {
        val resultMessage = if (isSuccess) "successfully" else "was not able to"
        println("${counter.dispatcher.name} $resultMessage ${counter::class.simpleName} ${previousAction.dispatcher.name}")
        resolveAction(previousAction, game, !isSuccess)
    }
}