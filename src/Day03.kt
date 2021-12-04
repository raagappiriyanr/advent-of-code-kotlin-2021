fun main() {

    fun part1(values: List<String>): Int {
        val bitLength = values.first().length
        val gammaRate = StringBuilder()
        val epsilonRate = StringBuilder()

        for (index in 0 until bitLength) {
            val totalOnBit = values.count { it[index] == ON }
            val totalOffBit = values.size - totalOnBit

            if (totalOnBit > totalOffBit) {
                gammaRate.append(ON)
                epsilonRate.append(OFF)
            } else {
                gammaRate.append(OFF)
                epsilonRate.append(ON)
            }
        }

        return gammaRate.toString().toInt(2) * epsilonRate.toString().toInt(2)
    }

    fun part2(values: List<String>): Int {
        val bitLength = values.first().length
        val oxygenGeneratorRating = values.toMutableList()
        val co2ScrubberRating = values.toMutableList()

        for (index in 0 until bitLength) {
            if (oxygenGeneratorRating.size == 1 && co2ScrubberRating.size == 1) {
                break
            }

            if (oxygenGeneratorRating.size > 1) {
                val totalOnBit = oxygenGeneratorRating.count { it[index] == ON }
                val totalOffBit = oxygenGeneratorRating.size - totalOnBit

                oxygenGeneratorRating.removeAll {
                    it[index] == if (totalOnBit >= totalOffBit) OFF else ON
                }
            }

            if (co2ScrubberRating.size > 1) {
                val totalOnBit = co2ScrubberRating.count { it[index] == ON }
                val totalOffBit = co2ScrubberRating.size - totalOnBit

                co2ScrubberRating.removeAll {
                    it[index] == if (totalOnBit >= totalOffBit) ON else OFF
                }
            }
        }

        return oxygenGeneratorRating.first().toInt(2) *
                co2ScrubberRating.first().toInt(2)
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    val part2Answer = "Part 2 Answer: ${part2(input)}"

    println(part1Answer)
    println(part2Answer)
}

const val ON = '1'
const val OFF = '0'

