fun main() {
    fun part1(value: String): Int {
        val bits = BITS(value)
        return bits.result.versionSum()
    }

    fun part2(value: String): Long {
        val bits = BITS(value)
        return bits.result.value()
    }

    val testInput = readInput("Day16_test").first()
    val input = readInput("Day16").first()

    check(part1(testInput) == 20)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 1L)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

class BITS(
    transmission: String
) {
    val result by lazy {
        val decodedMessage = transmission.map {
            it.digitToInt(16).toString(2).padStart(4, '0')
        }.joinToString("")
        processMessage(decodedMessage.toMutableList())
    }

    private fun processMessage(message: MutableList<Char>): Packet {
        return when(message.drop(3).take(3).binaryToInt()) {
            4 -> processAsLiteralPacket(message)
            else -> processAsOperatorPacket(message)
        }
    }

    private fun processAsLiteralPacket(message: MutableList<Char>): Packet {
        val version = message.takeAndRemove(3).binaryToInt()
        val typeId = message.takeAndRemove(3).binaryToInt()
        val valueBits = mutableListOf<List<Char>>()

        while (message.isNotEmpty()) {
            val startsWith = message.takeAndRemove(1).first()
            val value = message.takeAndRemove(4)
            valueBits.add(value)

            if (startsWith == '0') break
        }

        return Packet(
            header = Header(version, typeId),
            body = Body.LiteralValue(valueBits.flatten().binaryToLong())
        )
    }

    private fun processAsOperatorPacket(message: MutableList<Char>): Packet {
        val version = message.takeAndRemove(3).binaryToInt()
        val typeId = message.takeAndRemove(3).binaryToInt()
        val lengthTypeId = message.takeAndRemove(1).first()
        val subPackets = mutableListOf<Packet>()
        if (lengthTypeId == '0') {
            val lengthOfSubPackets = message.takeAndRemove(15).binaryToInt()
            val subPacketBits = message.takeAndRemove(lengthOfSubPackets).toMutableList()
            while (subPacketBits.isNotEmpty()) {
                subPackets += processMessage(subPacketBits)
            }
        } else {
            val numberOfSubPackets = message.takeAndRemove(11).binaryToInt()
            repeat(numberOfSubPackets) {
                subPackets += processMessage(message)
            }
        }
        return Packet(
            header = Header(version, typeId),
            body = Body.OperatorPacket(
                operator = getOperatorFrom(typeId),
                subPackets = subPackets
            )
        )
    }
}

data class Packet(
    val header: Header,
    val body: Body
) {
    fun value() = body.value()
    fun versionSum() = header.version + body.versionSum()
}

data class Header(
    val version: Int,
    val typeId: Int,
)

sealed class Body {
    abstract fun value(): Long
    abstract fun versionSum(): Int

    data class LiteralValue(val value: Long) : Body() {
        override fun value(): Long = value
        override fun versionSum() = 0
    }

    data class OperatorPacket(val operator: Operator, val subPackets: List<Packet>) : Body() {
        override fun value(): Long = when(operator) {
            Operator.SUM -> subPackets.sumOf { it.value() }
            Operator.PRODUCT -> subPackets.fold(1) { acc, packet -> acc * packet.value() }
            Operator.MIN -> subPackets.minOf { it.value() }
            Operator.MAX -> subPackets.maxOf { it.value() }
            Operator.GREATER -> subPackets.let { (first, second) -> if (first.value() > second.value()) 1 else 0 }
            Operator.LESSER -> subPackets.let { (first, second) -> if (first.value() < second.value()) 1 else 0 }
            Operator.EQUAL -> subPackets.let { (first, second) -> if (first.value() == second.value()) 1 else 0 }
        }
        override fun versionSum() = subPackets.sumOf { it.versionSum() }
    }
}

enum class Operator {
    SUM,
    PRODUCT,
    MIN,
    MAX,
    GREATER,
    LESSER,
    EQUAL
}

fun getOperatorFrom(typeId: Int) = when(typeId) {
    0 -> Operator.SUM
    1 -> Operator.PRODUCT
    2 -> Operator.MIN
    3 -> Operator.MAX
    5 -> Operator.GREATER
    6 -> Operator.LESSER
    7 -> Operator.EQUAL
    else -> throw IllegalArgumentException("Unknown [typeId:$typeId]")
}

fun <T> MutableList<T>.takeAndRemove(n: Int): List<T> {
    return this.take(n).also { repeat(n) { this.removeFirst() } }
}
fun List<Char>.binaryToInt() = this.joinToString("").toInt(2)
fun List<Char>.binaryToLong() = this.joinToString("").toLong(2)