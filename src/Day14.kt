fun main() {
    fun part1(values: List<String>): Long {
        val polymer = Polymer.from(values)
        repeat(10) { polymer.nextStep() }
        return polymer.getResult()
    }

    fun part2(values: List<String>): Long {
        val polymer = Polymer.from(values)
        repeat(40) { polymer.nextStep() }
        return polymer.getResult()
    }

    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    check(part1(testInput) == 1588L)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 2188189693529L)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

class Polymer(
    startTemplate: String,
    private val insertMap: Map<String, String>
) {
    private var value = startTemplate
        .windowed(2)
        .groupingBy { it }.eachCount().toList()
        .associate { it.first to it.second.toLong() }

    fun nextStep() {
        val map = mutableMapOf<String, Long>()
        value.forEach {
            val (key, value) = it
            val insert = insertMap[key]
            if (insert != null) {
                val firstPair = "${key.first()}$insert"
                val secondPair = "$insert${key.last()}"
                map[firstPair] = map.getOrDefault(firstPair, 0) + value
                map[secondPair] = map.getOrDefault(secondPair, 0) + value
            }
        }

        value = map
    }

    fun getResult(): Long {
        val result = value.map {
            val (first, second) = it.key.toCharArray()
            listOf(
                first to it.value,
                second to it.value
            )
        }.flatten()
            .groupBy { it.first }
            .map { (key, value) ->
                CharCount(
                    char = key,
                    count = value.sumOf { it.second }.let { (it / 2) + (it % 2) }
                )
            }

        return result.maxOf { it.count } - result.minOf { it.count }
    }

    companion object {
        fun from(values: List<String>): Polymer {
            val inputs = values.filter { it.isNotEmpty() }.toMutableList()
            val startTemplate = inputs.removeFirst()
            val inserts = inputs.associate {
                val (key, value) = it.split(" -> ")
                key to value
            }

            return Polymer(startTemplate, inserts)
        }
    }
}

data class CharCount(
    val char: Char,
    val count: Long
)