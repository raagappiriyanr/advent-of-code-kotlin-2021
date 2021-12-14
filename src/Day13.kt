import java.util.*

fun main() {
    fun part1(paper: Paper): Int {
        paper.foldOnce()
        return paper.getNumberOfVisiblePoints()
    }

    fun part2(paper: Paper) {
        paper.foldCompletely()
        paper.display()
    }

    val testInput = readInput("Day13_test").let { Paper.from(it) }
    val input = readInput("Day13").let { Paper.from(it) }

    check(part1(testInput) == 17)

    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)
    part2(input)
}

sealed class Fold {
    abstract val along: Int

    data class Up(override val along: Int): Fold()
    data class Left(override val along: Int): Fold()

    companion object {
        fun from(string: String): Fold {
            return string.replace("fold along ", "").let {
                when {
                    it.startsWith("y") -> Up(it.replace("y=", "").toInt())
                    it.startsWith("x") -> Left(it.replace("x=", "").toInt())
                    else -> throw IllegalArgumentException(
                        "String should be in format [x=value] or [y=value] but was $string"
                    )
                }
            }
        }
    }
}

class Paper(
    private val points: List<Point>,
    private val folds: Queue<Fold>
) {
    private var data: List<MutableList<Boolean>>

    init {
        val xRange = 0..points.maxOf { it.x }
        val yRange = 0..points.maxOf { it.y }

        val matrix = mutableListOf<MutableList<Boolean>>()
        for (y in yRange) {
            val xAxis = mutableListOf<Boolean>()
            for (x in xRange) {
                xAxis += points.any { it.x == x && it.y == y }
            }
            matrix += xAxis
        }

        data = matrix
    }

    fun foldOnce() {
        fold(folds.poll())
    }

    fun foldCompletely() {
        while (folds.isNotEmpty()) {
            fold(folds.poll())
        }
    }

    private fun fold(fold: Fold) {
        when(fold) {
            is Fold.Up -> {
                val top = data.subList(0, fold.along)
                val bottom = data.subList(fold.along + 1, data.size).reversed()
                data = top.zip(bottom) { topY, bottomY ->
                    topY.zip(bottomY) { topX, bottomX ->
                        topX || bottomX
                    }.toMutableList()
                }
            }
            is Fold.Left -> {
                val left = data.map { it.subList(0, fold.along) }
                val right = data.map { it.subList(fold.along + 1, it.size).reversed() }

                data = left.zip(right) { leftY, rightY ->
                    leftY.zip(rightY) { leftX, rightX ->
                        leftX || rightX
                    }.toMutableList()
                }
            }
        }
    }

    fun getNumberOfVisiblePoints() = data.flatten().count { it }

    fun display() {
        for (row in data.indices) {
            for (column in data[row].indices) {
                print(
                    if (data[row][column]) "#" else "."
                )
                print(' ')
            }
            println()
        }

        println()
    }

    companion object {
        fun from(inputs: List<String>) = inputs.let { input ->
            val points = input.takeWhile { it.isNotEmpty() }.map {
                val (x, y) = it.split(',')
                Point(
                    x = x.toInt(),
                    y = y.toInt()
                )
            }.sortedWith(
                compareBy(Point::x, Point::y)
            )

            val folds = input.filter { it.startsWith("fold along") }.map {
                Fold.from(it)
            }

            Paper(
                points = points,
                folds = LinkedList(folds)
            )
        }
    }
}