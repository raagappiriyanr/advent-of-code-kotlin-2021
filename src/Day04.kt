import java.util.*

fun main() {

    fun part1(values: List<String>): Int {
        val inputs = values.filter { it.isNotEmpty() }.toMutableList()
        val randomNumbers = inputs.removeFirst().split(',').map { it.toInt() }
        val boards = inputs
            .asSequence()
            .chunked(5)
            .map { items ->
                items.map { item ->
                    item.split( ' ')
                        .filter { it.isNotEmpty() }
                        .map { it.toInt() }
                        .map { Card(it) }
                }
            }
            .map { Board(it) }
            .toList()
        val bingo = Bingo(
            randomNumbers = LinkedList(randomNumbers),
            boards = boards
        )

        return bingo.findWinner()
    }

    fun part2(values: List<String>): Int {
        val inputs = values.filter { it.isNotEmpty() }.toMutableList()
        val randomNumbers = inputs.removeFirst().split(',').map { it.toInt() }
        val boards = inputs
            .asSequence()
            .chunked(5)
            .map { items ->
                items.map { item ->
                    item.split( ' ')
                        .filter { it.isNotEmpty() }
                        .map { it.toInt() }
                        .map { Card(it) }
                }
            }
            .map { Board(it) }
            .toList()
        val bingo = Bingo(
            randomNumbers = LinkedList(randomNumbers),
            boards = boards
        )

        return bingo.findLoser()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    val part2Answer = "Part 2 Answer: ${part2(input)}"

    println(part1Answer)
    println(part2Answer)
}

class Bingo(
    val randomNumbers: Queue<Int>,
    val boards: List<Board>
) {
    fun findWinner(): Int {
        var randomNumber = 0

        while (boards.find { it.hasWon() } == null && randomNumbers.isNotEmpty()) {
            randomNumber = randomNumbers.poll()
            boards.onEach { it.mark(randomNumber) }
        }

        return boards.find { it.hasWon() }?.value
            ?.flatten()
            ?.filter { !it.isMarked }
            ?.sumOf { it.value }
            ?.let { it * randomNumber } ?: throw Exception("No winner found")
    }


    fun findLoser(): Int {
        val boards = boards.toMutableList()
        println(boards.size)
        var randomNumber: Int

        while (randomNumbers.isNotEmpty()) {
            randomNumber = randomNumbers.poll()
            boards.onEach { it.mark(randomNumber) }
            if (boards.size == 1 && boards[0].hasWon()) {
                return boards[0].value
                    .flatten()
                    .filter { !it.isMarked }
                    .sumOf { it.value }
                    .let { it * randomNumber }
            }

            boards.removeAll(boards.filter { it.hasWon() })
        }

        throw Exception("Ran out of Random Numbers")
    }
}

data class Board(
    val value: List<List<Card>>
) {
    fun hasWon(): Boolean {
        value.forEach { row ->
            if (row.all { it.isMarked }) {
                return true
            }
        }

        for (i in value.indices) {
            var isAllMarked = false

            for (j in value.indices) {
                if (!value[j][i].isMarked) {
                    isAllMarked = false
                    break
                }

                isAllMarked = true
            }

           if (isAllMarked) return true
        }

        return false
    }

    fun mark(number: Int) {
        value.flatten().filter { it.value == number }.onEach { it.mark() }
    }
}

data class Card(
    val value: Int
) {
    var isMarked: Boolean = false
        private set

    fun mark() {
        isMarked = true
    }
}

