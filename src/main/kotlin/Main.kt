import org.coup.game.actions.*
import org.coup.game.actions.ActionMediator

fun main() {
    val elder = Player("Elder")
    val high = Player("Highlander")
    val game = CoupGame(mutableListOf(elder, high))
    val mediator = ActionMediator()

    mediator.addAction(Steal(dispatcher = elder, target = high))
    mediator.addAction(CounterSteal(dispatcher = high))
    mediator.addAction(Challenge(dispatcher = elder, target = high))

    mediator.resolveAsSuccess(game)

    println("--------------------")

    mediator.addAction(Tax(dispatcher = high))
    mediator.addAction(Challenge(dispatcher = elder, target = high))

    mediator.resolveAsFailure(game)

    println("--------------------")

    mediator.addAction(Assassinate(dispatcher = high, target = elder))
    mediator.addAction(CounterAssassinate(dispatcher = elder))
    mediator.addAction(Challenge(dispatcher = high, target = elder))

    mediator.resolveAsSuccess(game)
}