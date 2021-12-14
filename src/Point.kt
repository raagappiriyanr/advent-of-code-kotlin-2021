data class Point(
    val x: Int,
    val y: Int,
) {
    companion object {
        fun from(input: String): Point {
            return input.split(',').let { (x, y) ->
                Point(x.toInt(), y.toInt())
            }
        }
    }
}