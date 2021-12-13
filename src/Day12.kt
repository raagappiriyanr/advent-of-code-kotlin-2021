fun main() {
    fun part1(values: List<String>): Int {
        val graph = Graph<Cave>()
        values.forEach { input ->
            val (source, destination) = input.split('-').map { Cave(it) }
            graph.add(
                edge = Graph.EdgeType.UNDIRECTED,
                source = graph.createVertex(source),
                destination = graph.createVertex(destination)
            )
        }

        return graph.numberOfPathsWhenSmallCaveVisitedOnlyOnce(
            source = Vertex(Cave("start")),
            destination = Vertex(Cave("end")),
        )
    }

    fun part2(values: List<String>): Int {
        val graph = Graph<Cave>()
        values.forEach { input ->
            val (source, destination) = input.split('-').map { Cave(it) }
            graph.add(
                edge = Graph.EdgeType.UNDIRECTED,
                source = graph.createVertex(source),
                destination = graph.createVertex(destination)
            )
        }

        return graph.numberOfPathsWhenSingleSmallCaveVisitedTwice(
            source = Vertex(Cave("start")),
            destination = Vertex(Cave("end")),
        )
    }

    val testInput = readInput("Day12_test")
    val input = readInput("Day12")

    check(part1(testInput) == 226)
    val part1Answer = "Part 1 Answer: ${part1(input)}"
    println(part1Answer)

    check(part2(testInput) == 3509)
    val part2Answer = "Part 2 Answer: ${part2(input)}"
    println(part2Answer)
}

data class Cave(
    val name: String,
) {
    val isBigCave = name.all { it.isUpperCase() }

    override fun toString(): String {
        return name
    }
}

data class Vertex<T>(val data: T)

data class Edge<T>(
    val source: Vertex<T>,
    val destination: Vertex<T>
)

class Graph<T> {
    private val adjacencies: HashMap<Vertex<T>, MutableList<Edge<T>>> = HashMap()

    fun createVertex(data: T): Vertex<T> {
        return adjacencies.keys.find {
            it.data == data
        } ?: Vertex(data).also {
            adjacencies[it] = mutableListOf()
        }
    }

    fun addDirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
    ) {
        val edge = Edge(source, destination)
        adjacencies[source]?.add(edge)
    }

    fun addUndirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
    ) {
        addDirectedEdge(source, destination)
        addDirectedEdge(destination, source)
    }

    fun add(
        edge: EdgeType,
        source: Vertex<T>,
        destination: Vertex<T>,
    ) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(source, destination)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination)
        }
    }

    fun edges(source: Vertex<T>): List<Edge<T>> {
        return adjacencies[source] ?: emptyList()
    }

    override fun toString(): String {
        return buildString {
            adjacencies.forEach { (vertex, edges) ->
                val edgeString = edges.joinToString { it.destination.data.toString() }
                append("${vertex.data} ---> [ $edgeString ]\n")
            }
        }
    }

    enum class EdgeType {
        DIRECTED,
        UNDIRECTED
    }

    data class Ref<T>(var value: T)
}

private fun Graph<Cave>.numberOfPathsWhenSmallCaveVisitedOnlyOnce(
    source: Vertex<Cave>,
    destination: Vertex<Cave>
): Int {
    fun paths(
        source: Vertex<Cave>,
        destination: Vertex<Cave>,
        visited: MutableMap<Vertex<Cave>, Int>,
        pathCount: Graph.Ref<Int>
    ) {
        visited[source] = (visited.getOrDefault(source, 0) + 1)

        if (source == destination) {
            pathCount.value += 1
        } else {
            val neighbors = edges(source)
            neighbors.forEach { edge ->
                if (edge.destination !in visited || edge.destination.data.isBigCave) {
                    paths(edge.destination, destination, visited, pathCount)
                }
            }
        }

        if (visited.getOrDefault(source, 0) > 1) {
            visited[source] = visited.getOrDefault(source, 0) - 1
        } else {
            visited.remove(source)
        }
    }

    val numberOfPaths = Graph.Ref(0)
    val visited: MutableMap<Vertex<Cave>, Int> = mutableMapOf()
    paths(source, destination, visited, numberOfPaths)
    return numberOfPaths.value
}

private fun Graph<Cave>.numberOfPathsWhenSingleSmallCaveVisitedTwice(
    source: Vertex<Cave>,
    destination: Vertex<Cave>
): Int {
    fun paths(
        source: Vertex<Cave>,
        destination: Vertex<Cave>,
        visited: MutableMap<Vertex<Cave>, Int>,
        pathCount: Graph.Ref<Int>
    ) {
        visited[source] = (visited.getOrDefault(source, 0) + 1)

        if (source == destination) {
            pathCount.value += 1
        } else {
            val neighbors = edges(source)
            neighbors.forEach { edge ->
                if (edge.destination !in visited ||
                    edge.destination.data.isBigCave ||
                    (edge.destination.data.name != "start"
                            && edge.destination.data.name != "end"
                            && visited.filter { !it.key.data.isBigCave }.values.all { it < 2 })
                ) {
                    paths(edge.destination, destination, visited, pathCount)
                }
            }
        }

        if (visited.getOrDefault(source, 0) > 1) {
            visited[source] = visited.getOrDefault(source, 0) - 1
        } else {
            visited.remove(source)
        }
    }

    val numberOfPaths = Graph.Ref(0)
    val visited: MutableMap<Vertex<Cave>, Int> = mutableMapOf()
    paths(source, destination, visited, numberOfPaths)
    return numberOfPaths.value
}