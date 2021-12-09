fun main() {
    fun part1(values: List<String>): Int {
        return values.map { entry ->
            val (signals, output) = entry.split(" | ")
            Entry(
                signals = signals.split(' '),
                outputs = output.split(' ')
            )
        }.flatMap {
            it.outputs
        }.count {
            it.length in listOf(2, 4, 3, 7)
        }
    }

    fun part2(values: List<String>): Int {
        return values.map { entry ->
            val (signals, output) = entry.split(" | ")
            Entry(
                signals = signals.split(' '),
                outputs = output.split(' ')
            )
        }.sumOf { it.getOutput() }
    }

    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    check(part1(testInput) == 26)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 61229)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

data class Entry(
    val signals: List<String>,
    val outputs: List<String>
) {
    private val map = processSignals()

    private fun processSignals(): Map<Int, Set<Char>> {
        val map = mutableMapOf<Int, Set<Char>>()

        map[1] = signals.single { it.length == 2}.toSet()
        map[4] = signals.single { it.length == 4}.toSet()
        map[7] = signals.single { it.length == 3}.toSet()
        map[8] = signals.single { it.length == 7}.toSet()
        map[3] = signals.single { it.length == 5 && it.containsAll(map[1]) }.toSet()
        map[9] = signals.single { it.length == 6 && it.containsAll(map[3]) }.toSet()
        map[0] = signals.single { it.length == 6 && it.containsAll(map[7]) && it != map[9].toString() }.toSet()
        map[6] = signals.single { it.length == 6 && it != map[0].toString() && it != map[9].toString() }.toSet()
        map[5] = signals.single { it.length == 5 && map[6].toString().containsAll(it) }.toSet()
        map[2] = signals.single { it.length == 5 && it != map[3].toString() && it != map[5].toString() }.toSet()

        return map
    }

    fun getOutput(): Int {
        return outputs.map { output ->
            map.filter { it.value == output.toSet() }.toList().single().first
        }.joinToString("") {
            it.toString()
        }.toInt()
    }
}

private fun Set<Char>?.toString(): String = orEmpty().joinToString("")

private fun String?.containsAll(string: String): Boolean {
    if (this == null) return false
    return toSet().containsAll(string.toSet())
}

private fun String?.containsAll(chars: Set<Char>?): Boolean {
    if (this == null) return false
    return toSet().containsAll(chars.orEmpty())
}