fun main() {

    fun part1(heightMap: HeightMap): Int {
        return heightMap.riskLevel
    }

    fun part2(heightMap: HeightMap): Int {
        return heightMap.basins
            .sortedByDescending { it.locations.size }
            .take(3)
            .fold(1) { acc, basin ->
                acc * basin.locations.size
            }
    }

    val testInput = readInput("Day09_test")
        .let { input -> HeightMap.from(input) }
        .also { it.display() }

    val input = readInput("Day09")
        .let { input -> HeightMap.from(input) }
        .also { it.display() }

    check(part1(testInput) == 15)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 1134)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

class HeightMap(
    private val data: List<List<Int>>
) {
    private val lowPoints = data.mapIndexed { row, columns ->
        columns.filterIndexed { column, height ->
            height < getAdjacent(Location(row, column)).minOf { it }
        }
    }.flatten()

    val riskLevel = lowPoints.sumOf { it + 1 }

    val basins = findAllBasin()

    fun display() {
        val basinLocations = basins.flatMap { it.locations }
        val starCount = data.first().size.times(2)
        repeat(starCount) {
            print('*')
        }
        println()
        for (row in data.indices) {
            for (column in data[row].indices) {
                print(
                    if (basinLocations.contains(Location(row, column)))
                        '+'
                    else
                        '-'
                )
                print(' ')
            }
            println()
        }
        repeat(starCount) {
            print('*')
        }
        println()
    }

    private fun getAdjacentLocations(matrixPosition: Location): List<Location> {
        with(matrixPosition) {
            return listOf(
                // UP
                Location (row - 1 , column),
                // Left
                Location (row, column - 1),
                // RIGHT
                Location (row, column + 1),
                // DOWN
                Location (row + 1, column)
            ).filter { (row, column) ->
                row in data.indices && column in data[row].indices
            }
        }
    }

    private fun getAdjacent(matrixPosition: Location): List<Int> {
        return getAdjacentLocations(matrixPosition).map { (row, column) ->
            data[row][column]
        }
    }

    private fun findAllBasin(): List<Basin> {
        val visitedLocations = mutableListOf<Location>()
        val basins = mutableListOf<Basin>()

        fun findBasinLocations(matrixPosition: Location): List<Location> {
            with(matrixPosition) {
                if (visitedLocations.contains(this) || data[row][column] == 9) {
                    return emptyList()
                }

                visitedLocations += this
                return mutableListOf(this) + getAdjacentLocations(this).map { findBasinLocations(it) }.flatten()
            }
        }

        for (row in data.indices) {
            for (column in data[row].indices) {
                if (visitedLocations.contains(Location(row, column)) || data[row][column] == 9) {
                    continue
                }

                basins += Basin(findBasinLocations(Location(row, column)))
            }
        }

        return basins
    }

    companion object {
        fun from(input: List<String>): HeightMap {
            return input.map { row ->
                row.toList().map { column ->
                    column.digitToInt()
                }
            }.let {
                HeightMap(it)
            }
        }
    }
}

data class Location(
    val row: Int,
    val column: Int,
)

data class Basin(
    val locations: List<Location>
)