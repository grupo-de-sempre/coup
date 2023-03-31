package org.coup.game.actions

import kotlin.reflect.KClass

sealed interface Influence {
    val action: KClass<out CharacterAction>?
    val counterAction: KClass<out CounterAction>?
}

object Duke : Influence {
    override val action = Tax::class
    override val counterAction = CounterForeignAid::class
}

object Captain : Influence {
    override val action = Steal::class
    override val counterAction = CounterSteal::class
}

object Assassin : Influence {
    override val action = Assassinate::class
    override val counterAction = null
}

object Ambassador : Influence {
    override val action = Exchange::class
    override val counterAction = CounterSteal::class
}

object Contessa : Influence {
    override val action = null
    override val counterAction = CounterAssassinate::class
}