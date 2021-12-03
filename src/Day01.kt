fun main() {
    fun part1(values: List<String>): Int {
        return values
            .map { it.toInt() }
            .windowed(2)
            .count { it.first() < it.last() }
    }

    fun part2(values: List<String>): Int {
        return values
            .map { it.toInt() }
            .windowed(4)
            .count { it.first() < it.last() }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    val part1Answer = part1(input)
    val part2Answer = part2(input)

    println(part1Answer)
    println(part2Answer)
}
