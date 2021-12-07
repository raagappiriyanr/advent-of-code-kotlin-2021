import kotlin.math.abs
import kotlin.math.ceil

fun main() {
    fun part1(values: List<String>): Int {
        val positions = values.first()
            .split(',')
            .map { it.toInt() }

        val average = ceil(positions.average()).toInt()
        println("Average: $average")

        val averagePositionRange = positions.minOf { it }..average
        val fuelCost = averagePositionRange
            .map { position -> position to positions.sumOf { abs(it - position) } }
            .onEach { println(it) }
            .minOf { it.second }

        println("FuelCost: $fuelCost")

        return fuelCost
    }

    fun part2(values: List<String>): Int {
        val positions = values.first()
            .split(',')
            .map { it.toInt() }

        val average = ceil(positions.average()).toInt()
        println("Average: $average")

        val averagePositionRange = positions.minOf { it }..average
        val fuelCost = averagePositionRange
            .map { position ->
                position to positions.sumOf {
                val diff = abs(it - position)
                diff.times(diff + 1).div(2)
                }
            }
            .onEach { println(it) }
            .minOf { it.second }

        println("FuelCost: $fuelCost")

        return fuelCost
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    val part2Answer = "Part 2 Answer: ${part2(input)}"

    println(part1Answer)
    println(part2Answer)
}
