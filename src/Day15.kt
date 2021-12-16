fun main() {
    fun part1(values: List<String>): Int {
        val maze = Maze.from(values)
        return maze.findShortestPath().distance
    }

    fun part2(values: List<String>): Int {
        val maze = Maze.from(values, 5)
        return maze.findShortestPath().distance
    }

    val testInput = readInput("Day15_test")
    val input = readInput("Day15")

    check(part1(testInput) == 40)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 315) {
        part2(testInput)
    }
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

class Maze(
    private val data: List<List<Int>>
) {
    fun findShortestPath(
        start: Location = Location(0, 0),
        end: Location = Location(data.size - 1, data.first().size - 1),
    ): Node {
        var currentNode = Node(start)
        val visitedLocations = mutableListOf<Location>().also {
            it.add(currentNode.location)
        }
        val exploredNodes = mutableListOf<Node>()

        while (currentNode.location != end) {
            getAdjacentLocations(currentNode.location)
                .filter { it !in visitedLocations }
                .forEach {
                    val node = Node(
                        location = it,
                        distance = currentNode.distance + this[it],
                        parent = currentNode
                    )
                    exploredNodes += node
                    visitedLocations += node.location
                }

            exploredNodes.sortBy { it.distance }
            currentNode = exploredNodes.removeFirst()
        }

        return currentNode.also { displaySolution(currentNode) }
    }

    private fun getAdjacentLocations(location: Location): List<Location> {
        with(location) {
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

    private fun displaySolution(endNode: Node) {
        val path = mutableListOf<Location>().apply {
            add(endNode.location)
            var currentNode:Node? = endNode.parent
            while (currentNode != null) {
                add(currentNode.location)
                currentNode = currentNode.parent
            }
        }.toList()

        val starCount = data.first().size * 2
        repeat(starCount) { print('*') }
        println()
        for (row in data.indices) {
            for (column in data[row].indices) {
                val location = Location(row, column)
                print(
                    if (path.contains(location))
                        "."
                    else
                        this[location]
                )
                print(' ')
            }
            println()
        }
        repeat(starCount) { print('*') }
        println()
    }

    private operator fun get(location: Location) = with(location) { data[row][column] }

    companion object {
        fun from(values: List<String>, scale: Int = 1): Maze {
            val finalSize = values.size * scale
            val data = MutableList(finalSize) { MutableList(finalSize) { 0 } }

            for (i in 0 until finalSize) {
                for (j in 0 until finalSize) {
                    val size = values.size
                    val row = i % size
                    val column = j % size
                    data[i][j] = (values[row][column].digitToInt() + (i / size) + (j / size)).let { sum ->
                        if (sum > 9) sum - 9 else sum
                    }
                }
            }

            return Maze(data)
        }
    }

    data class Node(
        val location: Location,
        val distance: Int = 0,
        val parent: Node? = null
    )
}