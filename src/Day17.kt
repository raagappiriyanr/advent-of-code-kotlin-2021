import kotlin.math.abs

fun main() {
    fun part1(input: String): Int {
        val targetArea = TargetArea.from(input)
        return Launcher(targetArea)
            .computeVelocitiesThatCanHitTheTarget()
            .maxOf { it.yVelocity.summation() }
    }

    fun part2(input: String): Int {
        val targetArea = TargetArea.from(input)
        return Launcher(targetArea).computeVelocitiesThatCanHitTheTarget().size
    }

    val testInput = readInput("Day17_test").first()
    val input = readInput("Day17").first()

    check(part1(testInput) == 45)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 112)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

class Launcher(
    private val target: TargetArea
) {
    fun computeVelocitiesThatCanHitTheTarget(): List<Velocity> {
        val velocities = mutableListOf<Velocity>()
        for (xVelocity in 0..target.xRange.last) {
            val range = abs(target.yRange.first)
            for (yVelocity in -range..range) {
                velocities += Velocity(xVelocity, yVelocity)
            }
        }

        return velocities.filter {
            Probe().launch(
                atVelocity = it,
                toTargetArea = target
            )
        }
    }
}

class Probe {
    private var position = Point(0, 0)
    private var velocity = Velocity(0, 0)

    fun launch(atVelocity: Velocity, toTargetArea: TargetArea): Boolean {
        velocity = atVelocity
        position = position.copy(
            x = this.velocity.xVelocity,
            y = this.velocity.yVelocity,
        )

        var didHitTarget = toTargetArea.didHitTarget(position)
        var didMissTarget = toTargetArea.didMissTarget(position)

        while (!didHitTarget && !didMissTarget) {
            nextStep()
            didHitTarget = toTargetArea.didHitTarget(position)
            didMissTarget = toTargetArea.didMissTarget(position)
        }

        return didHitTarget
    }

    private fun nextStep() {
        velocity = velocity.copy(
            xVelocity = maxOf(0,velocity.xVelocity - 1),
            yVelocity = velocity.yVelocity - 1
        )
        position = position.copy(
            x = position.x + velocity.xVelocity,
            y = position.y + velocity.yVelocity,
        )
    }
}

data class TargetArea(
    val xRange: IntRange,
    val yRange: IntRange,
) {
    fun didHitTarget(point: Point): Boolean {
        return with(point) { x in xRange && y in yRange }
    }

    fun didMissTarget(point: Point): Boolean {
        return with(point) { x > xRange.last || y < yRange.first }
    }

    companion object {
        fun from(input: String): TargetArea {
            return input
                .replace("target area: ", "")
                .split(", ")
                .let { (xRange, yRange) ->
                    TargetArea(
                        xRange = xRange
                            .replace("x=", "")
                            .split("..")
                            .let { (min, max) -> IntRange(min.toInt(), max.toInt()) },
                        yRange = yRange
                            .replace("y=", "")
                            .split("..")
                            .let { (min, max) -> IntRange(min.toInt(), max.toInt()) }
                    )
                }
        }
    }
}

data class Velocity(
    val xVelocity: Int,
    val yVelocity: Int,
)

fun Int.summation() = this * (this + 1) / 2