package org.coup.game.actions

import kotlin.reflect.KClass

interface Action {

    fun requiredCoin(): Int
    fun blockedBy(): List<KClass<out CounterAction>>
}

interface ExecutableAction : Action {
    fun execute()
}

class Income : ExecutableAction {

    override fun requiredCoin() = 0
    override fun blockedBy() = listOf<KClass<CounterAction>>()
    override fun execute() {

    }
}

class ForeignAid : ExecutableAction {

    override fun requiredCoin() = 0
    override fun blockedBy() = listOf(BlockForeignAid::class)
    override fun execute() {

    }
}

class Coup : ExecutableAction {

    override fun requiredCoin() = 7
    override fun blockedBy() = listOf<KClass<CounterAction>>()
    override fun execute() {

    }
}

interface CharacterAction : ExecutableAction {

}

class Tax : CharacterAction {

    override fun requiredCoin() = 0
    override fun blockedBy() = listOf(Challenge::class)
    override fun execute() {

    }
}

class Assassinate : CharacterAction {
    override fun requiredCoin() = 3
    override fun blockedBy() = listOf(Challenge::class, BlockAssassination::class)
    override fun execute() {

    }
}

class Steal : CharacterAction {
    override fun requiredCoin() = 0
    override fun blockedBy() = listOf(Challenge::class, BlockSteal::class)
    override fun execute() {

    }
}

class Exchange : CharacterAction {
    override fun requiredCoin() = 0
    override fun blockedBy() = listOf(Challenge::class)
    override fun execute() {

    }
}

interface CounterAction : Action {
    override fun requiredCoin() = 0
    override fun blockedBy(): List<KClass<out CounterAction>> = listOf(Challenge::class)
}

class Challenge : CounterAction {
    override fun blockedBy() = listOf<KClass<CounterAction>>()
}

class BlockForeignAid : CounterAction {

}

class BlockAssassination : CounterAction {

}

class BlockSteal : CounterAction {

}