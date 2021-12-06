fun main() {
    fun part1(values: List<String>): Int {
        val nearByLanternFishes = values
            .first()
            .split(',')
            .map { LanternFish(it.toInt()) }

        val sea = mutableListOf<LanternFish>()
        sea += nearByLanternFishes

        repeat(80) {
            sea += sea.mapNotNull { it.completeDay() }
        }

        return sea.size
    }

    fun part2(values: List<String>): Long {
        val nearByLanternFishes = values
            .first()
            .split(',')
            .map { it.toInt() }
        val cycles = mutableMapOf<Int, Long>()
        nearByLanternFishes.forEach {
            cycles[it] = cycles.getOrDefault(it, 0).inc()
        }

        repeat(256) {
            val cycleCompletedFishes = cycles.getOrDefault(0, 0)
            (1..8).forEach {
                cycles[it - 1] = cycles.getOrDefault(it, 0)
            }
            cycles[6] = cycles.getOrDefault(6, 0) + cycleCompletedFishes
            cycles[8] = cycleCompletedFishes
        }

        return cycles.toList().sumOf { it.second }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    val part2Answer = "Part 2 Answer: ${part2(input)}"

    println(part1Answer)
    println(part2Answer)
}

class LanternFish(
    cycle: Int = 8
) {
    var cycleTime = cycle
        private set

    fun completeDay(): LanternFish? {
        return if (cycleTime == 0) {
            resetCycle()
            LanternFish()
        } else {
            cycleTime--
            null
        }
    }

    private fun resetCycle() {
        cycleTime = 6
    }

    override fun hashCode(): Int {
        return cycleTime.hashCode()
    }

    override fun toString(): String {
        return "[Cycle Time: $cycleTime]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LanternFish

        if (cycleTime != other.cycleTime) return false

        return true
    }
}
