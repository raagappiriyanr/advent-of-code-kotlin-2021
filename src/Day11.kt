fun main() {
    fun part1(values: List<String>): Int {
        val octopusGrid = values.map { input ->
            input.map {
                Octopus(
                    initialEnergyLevel = it.digitToInt()
                )
            }
        }.let { OctopusGrid(it) }
        octopusGrid.simulate(100)

        return octopusGrid.getFlashCount()
    }

    fun part2(values: List<String>): Int {
        val octopusGrid = values.map { input ->
            input.map {
                Octopus(
                    initialEnergyLevel = it.digitToInt()
                )
            }
        }.let { OctopusGrid(it) }

        return octopusGrid.findStepWhenAllOctopusFlash()
    }

    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    check(part1(testInput) == 1656)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 195)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

data class OctopusGrid(
    val data: List<List<Octopus>>
) {
    fun findStepWhenAllOctopusFlash(): Int {
        var steps = 0
        val octopuses = data.flatten()

        while(octopuses.any { it.energyLevel != 0 }) {
            nextStep()
            steps++
        }

        return steps
    }
    fun simulate(steps: Int) {
        repeat(steps) {
            nextStep()
            println()
            println("------ After Step: ${it.inc()} ------")
            display()
        }
    }

    private fun display() {
        val starCount = data.first().size.times(2)
        repeat(starCount) {
            print('*')
        }
        println()
        for (row in data.indices) {
            for (column in data[row].indices) {
                print(data[row][column].energyLevel)
                print(' ')
            }
            println()
        }
        repeat(starCount) {
            print('*')
        }
        println()
    }

    private fun nextStep() {
        fun increaseEnergyLevel(location: Location) {
            with(location) {
                if (data[row][column].increaseEnergyLevel()) {
                    getNearByLocations(this).onEach {
                        increaseEnergyLevel(it)
                    }
                }
            }
        }

        for (row in data.indices) {
            for (column in data[row].indices) {
                increaseEnergyLevel(
                    location = Location(row, column)
                )
            }
        }

        data.flatten().onEach { it.resetFlashedStatus() }
    }

    private fun getNearByLocations(location: Location): List<Location> {
        with(location) {
            return listOf(
                // UP
                Location (row - 1 , column),
                // UP - LEFT
                Location (row - 1 , column - 1),
                // UP - RIGHT
                Location (row - 1 , column + 1),

                // Left
                Location (row, column - 1),
                // RIGHT
                Location (row, column + 1),

                // DOWN
                Location (row + 1, column),
                // UP - LEFT
                Location (row + 1 , column - 1),
                // UP - RIGHT
                Location (row + 1 , column + 1),
            ).filter { (row, column) ->
                row in data.indices && column in data[row].indices
            }
        }
    }

    fun getFlashCount() = data.flatten().sumOf { it.noOfTimesFlashed }
}

class Octopus(
    initialEnergyLevel: Int
) {
    private var flashed = false

    var noOfTimesFlashed = 0
        private set

    var energyLevel = initialEnergyLevel
        private set

    fun increaseEnergyLevel(): Boolean {
        if (flashed) {
            return false
        }

        energyLevel += 1

        return if (energyLevel > 9) {
            flash()
            true
        } else {
            false
        }
    }

    fun resetFlashedStatus() {
        flashed = false
    }

    private fun flash() {
        flashed = true
        energyLevel = 0
        noOfTimesFlashed++
    }

    override fun toString(): String {
        return "$energyLevel"
    }

    override fun hashCode(): Int {
        return energyLevel.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Octopus

        if (energyLevel != other.energyLevel) return false

        return true
    }
}