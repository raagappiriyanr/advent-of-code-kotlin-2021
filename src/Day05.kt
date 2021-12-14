fun main() {
    fun part1(values: List<String>): Int {
        return values
            .asSequence()
            .map { it.split(" -> ") }
            .map { Line.from(it) }
            // only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.
            .filter { it.start.x == it.end.x || it.start.y == it.end.y }
            .map { it.createLinePath() }
            .flatMap { it }
            .groupingBy { it }.eachCount()
            .count { it.value >= 2 }
    }

    fun part2(values: List<String>): Int {
        return values
            .asSequence()
            .map { it.split(" -> ") }
            .map { Line.from(it) }
            .map { it.createLinePath() }
            .flatMap { it }
            .groupingBy { it }.eachCount()
            .count { it.value >= 2 }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    val part2Answer = "Part 2 Answer: ${part2(input)}"

    println(part1Answer)
    println(part2Answer)
}

data class Line(
    val start: Point,
    val end: Point,
) {
    fun createLinePath(): List<Point> {
        val path = mutableListOf<Point>()

        val xRange = if (start.x >= end.x) {
            start.x downTo end.x
        } else {
            start.x..end.x
        }

        val yRange = if (start.y >= end.y) {
            start.y downTo end.y
        } else {
            start.y..end.y
        }

        var x = start.x
        var y = start.y
        val xDirection = when {
            start.x == end.x -> 0
            start.x < end.x -> 1
            else -> -1
        }
        val yDirection = when {
            start.y == end.y -> 0
            start.y < end.y -> 1
            else -> -1
        }

        while (x in xRange && y in yRange) {
            path += Point(x, y)
            x += xDirection
            y += yDirection
        }

        return path.toList()
    }

    companion object {
        fun from(input: List<String>): Line {
            return input.let { (start, end) ->
                Line(
                    start = Point.from(start),
                    end = Point.from(end),
                )
            }
        }
    }
}
