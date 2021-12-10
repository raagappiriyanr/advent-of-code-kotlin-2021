import java.util.*

fun main() {
    fun part1(values: List<String>): Int {
        return values.mapNotNull { input ->
            val stack = Stack<Tag>()

            for (tag in input.map { Tag.from(it) }) {
                when {
                    tag.isOpen -> stack += tag
                    stack.isNotEmpty()
                            && stack.peek().bracket == tag.bracket
                            && stack.peek().isOpen -> stack.pop()
                    else -> return@mapNotNull tag
                }
            }

            null
        }
            .groupingBy { it }.eachCount()
            .map {
                it.value * when(it.key.bracket) {
                    Bracket.Parentheses -> 3
                    Bracket.Square -> 57
                    Bracket.Curly -> 1197
                    Bracket.Angle -> 25137
                }
            }.sum()
    }

    fun part2(values: List<String>): Long {
        return values.mapNotNull { input ->
            val stack = Stack<Tag>()

            for (tag in input.map { Tag.from(it) }) {
                when {
                    tag.isOpen -> stack.push(tag)
                    stack.isNotEmpty()
                            && stack.peek().bracket == tag.bracket
                            && stack.peek().isOpen -> stack.pop()
                    else -> return@mapNotNull null
                }
            }

            stack.reversed().fold(0L) { acc, tag ->
                (acc * 5) + when(tag.bracket) {
                    Bracket.Parentheses -> 1
                    Bracket.Square -> 2
                    Bracket.Curly -> 3
                    Bracket.Angle -> 4
                }
            }
        }.sorted().middle()
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    check(part1(testInput) == 26397)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 288957L)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

data class Tag(
    val bracket: Bracket,
    val isOpen: Boolean,
) {
    override fun toString() = buildString {
        with(bracket) {
            append(if (isOpen) open else close)
        }
    }

    companion object {
        fun from(char: Char) = Tag(
            bracket = Bracket.from(char),
            isOpen = brackets.map { it.open }.contains(char)
        )
    }
}

sealed class Bracket(
    val open: Char,
    val close: Char,
) {
    object Parentheses : Bracket('(', ')')
    object Square : Bracket('[', ']')
    object Curly : Bracket('{', '}')
    object Angle : Bracket('<', '>')

    companion object {
        fun from(char: Char) = brackets.single { it.open == char || it.close == char }
    }

    override fun toString() = "$open$close"
}

val brackets = listOf(
    Bracket.Parentheses,
    Bracket.Square,
    Bracket.Curly,
    Bracket.Angle
)

fun <T> List<T>.middle() = this[size/2]