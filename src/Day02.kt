fun main() {
    fun part1(values: List<String>): Int {
        val submarine = Submarine(
            pilot = FirstPilot()
        )
        values.map { Command.from(it) }.forEach { submarine command it }
        return submarine.position.let { it.horizontal * it.depth }
    }

    fun part2(values: List<String>): Int {
        val submarine = Submarine(
            pilot = SecondPilot()
        )
        values.map { Command.from(it) }.forEach { submarine command it }
        return submarine.position.let { it.horizontal * it.depth }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    val part1Answer = part1(input)
    val part2Answer = part2(input)

    println(part1Answer)
    println(part2Answer)
}

sealed class Command {
    data class Forward(val value: Int): Command()
    data class Down(val value: Int): Command()
    data class Up(val value: Int): Command()
    object Unknown : Command()

    companion object {
        fun from(input: String): Command {
            val (command, value) = input.split(" ").let { Pair(it[0], it[1].toInt()) }
            return when(command) {
                "forward" -> Forward(value)
                "down" -> Down(value)
                "up" -> Up(value)
                else -> Unknown
            }
        }
    }
}

data class Position(
    val horizontal: Int = 0,
    val depth: Int = 0,
    val aim: Int = 0,
)

interface Pilot {
    val position: Position
    infix fun command(command: Command)
}

class FirstPilot : Pilot {
    override var position: Position = Position()
        private set

    override infix fun command(command: Command) {
        position = when (command) {
            is Command.Forward -> {
                position.copy(
                    horizontal = position.horizontal + command.value
                )
            }
            is Command.Down -> {
                position.copy(
                    depth = position.depth + command.value
                )
            }
            is Command.Up -> {
                position.copy(
                    depth = position.depth - command.value
                )
            }
            Command.Unknown -> position
        }
    }
}

class SecondPilot : Pilot {
    override var position: Position = Position()
        private set

    override fun command(command: Command) {
        position = when (command) {
            is Command.Forward -> {
                position.copy(
                    horizontal = position.horizontal + command.value,
                    depth = position.depth + (position.aim * command.value),
                )
            }
            is Command.Down -> {
                position.copy(
                    aim = position.aim + command.value
                )
            }
            is Command.Up -> {
                position.copy(
                    aim = position.aim - command.value
                )
            }
            Command.Unknown -> position
        }
    }
}

class Submarine(pilot: Pilot) : Pilot by pilot
