package org.coup.game.actions

import java.util.*

internal class ActionMediator {

    private val actionStack: Stack<Action> = Stack()

    fun action(executableAction: ExecutableAction) = actionStack.add(executableAction)

    fun counter(counterAction: CounterAction) = actionStack.add(counterAction)

    fun challenge(challenge: Challenge) = actionStack.add(challenge)

    fun success() = success(actionStack.pop())

    fun fail() = fail(actionStack.pop())

    private fun success(action: Action) {
        when(action) {
            is Challenge -> success(action, actionStack.pop())
            is CounterAction -> success(action, actionStack.pop())
            is ExecutableAction -> {
                println("${action::class.simpleName} success")
                action.execute()
            }
        }
    }

    private fun fail(action: Action) {
        when(action) {
            is Challenge -> fail(action, actionStack.pop())
            is CounterAction -> fail(action, actionStack.pop())
            else -> {
                println("${action::class.simpleName} fail")
            }
        }
    }

    private fun success(challenge: Challenge, previousAction: Action) {
        println("challenge success")
        fail(previousAction)
    }

    private fun success(counter: CounterAction, previousAction: Action) {
        println("counter success")
        fail(previousAction)
    }

    private fun fail(challenge: Challenge, previousAction: Action) {
        println("challenge fail")
        success(previousAction)
    }

    private fun fail(counter: CounterAction, previousAction: Action) {
        println("counter fail")
        success(previousAction)
    }
}